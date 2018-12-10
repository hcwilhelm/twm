package services

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import cats.instances.option._
import cats.syntax.apply._
import cats.syntax.either._
import domain.Domain.{Celsisus, City, Error, Forecast, HttpError, JsonError, NoTemperatureFound, Temperature}
import io.circe.Json
import io.circe.optics.JsonPath._
import io.circe.parser._

import scala.concurrent.{ExecutionContextExecutor, Future}


class WeatherClient(host: String, port: Int) {
  def forecast(city: City): Forecast = city match {
    case City("Hamburg") => Forecast(Temperature(20, Celsisus))
    case City("Berlin") => Forecast(Temperature(22, Celsisus))
    case City("Paris") => Forecast(Temperature(25, Celsisus))
    case City("Cadiz") => Forecast(Temperature(29, Celsisus))
    case _ => throw new RuntimeException("Unknown City")
  }
}

class OpenWeatherClient(host: String, port: Int) {
  val appID = "31ee415c28fb16ec573ddc0aeaa3c6b6"
  val baseUri = Uri(host).withPort(port)

  private implicit val system: ActorSystem = ActorSystem()
  private implicit val materializer: ActorMaterializer = ActorMaterializer()
  private implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  private val errorCodeIntLens = root.cod.int
  private val errorCodeStringLens = root.cod.string
  private val errorCodeLens = (json: Json) => errorCodeStringLens.getOption(json) orElse errorCodeIntLens.getOption(json).map(_.toString)
  private val errorMessageLens = root.message.string
  private val temperatureLens = root.main.temp.double

  def forecast(city: City): Future[Either[Error, Forecast]] = {
    val query = Query("q" -> city.name, "APPID" -> appID, "units" -> "metric")
    val uri = baseUri.withQuery(query)

    Http().singleRequest(HttpRequest(uri = uri)).flatMap {

      case HttpResponse(StatusCodes.OK, _, entity, _) =>
        entity.dataBytes.runReduce(_ ++ _)
          .map(_.utf8String)
          .map { value =>
            parse(value)
              .map(temperatureLens.getOption)
              .leftMap[Error](error => JsonError(error.message))
              .flatMap {
                case Some(temperature) => Either.right(Forecast(Temperature(temperature, Celsisus)))
                case None => Either.left(NoTemperatureFound)
              }
          }

      case HttpResponse(_, _, entity, _) =>
        entity.dataBytes.runReduce(_ ++ _)
          .map(_.utf8String)
          .map { value =>
            parse(value)
              .map{json => (errorCodeLens(json), errorMessageLens.getOption(json)).mapN(HttpError.apply)}
              .leftMap[Error](error => JsonError(error.message))
              .flatMap {
                case Some(error) => Either.left(error)
                case None => Either.left(JsonError("Invalid json response"))
              }
          }
    }
  }
}
