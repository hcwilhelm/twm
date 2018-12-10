package domain

import akka.io.Tcp.Message

object Domain {

  case class City(name: String)

  case class Config(host: String, port: Int)

  sealed trait TemperatureUnit
  case object Celsisus extends TemperatureUnit
  case object Fahrenheit extends TemperatureUnit

  case class Temperature(value: Double, unit: TemperatureUnit)

  case class Forecast(temperature: Temperature)

  type Requests = Map[City, Forecast]

  object Requests {
    def empty: Requests = Map.empty[City, Forecast]
  }

  sealed trait Error
  case class UnknownCity(city: String) extends Error
  case object RequestMapEmpty extends Error
  case class HttpError(code: String, message: String) extends Error
  case class JsonError(message: String) extends Error
  case object NoTemperatureFound extends Error
}
