import algebras._
import cats.Monad
import cats.syntax.all._
import domain.Domain._
import util.MonadOps.RichMonad

class Program[F[_]: ErrorHandler](implicit console: Console[F], config: ConfigAsk[F], weather: Weather[F], state: RequestState[F]) {

   def host: F[String] = config.reader(_.host)

   def port: F[Int] = config.reader(_.port)

   def cityByName(cityName: String): F[City] = cityName match {
    case "Berlin" => City(cityName).pure[F]
    case "Hamburg" => City(cityName).pure[F]
    case "Paris" => City(cityName).pure[F]
    case "Cadiz" => City(cityName).pure[F]
    case _ => ErrorHandler[F].raiseError(UnknownCity(cityName))
  }

  def fetchForecast(city: City): F[Forecast] = for {
    maybeForcast  <- state.inspect(_.get(city))
    forcast       <- maybeForcast.fold(weather.forecast(city))(_.pure[F])
    _             <- state.modify(_ + (city -> forcast))
  } yield forcast

  def findHottest(map: Map[City, Forecast]): F[(City, Temperature)] =
    if (map.isEmpty)
      ErrorHandler[F].raiseError(RequestMapEmpty)
    else
      map.maxBy { case (_, forecast) => forecast.temperature.value } match {
        case (city, forecast) => (city, forecast.temperature).pure[F]
      }

  def hottestCity: F[(City, Temperature)] = for {
    requests  <- state.get
    hottest   <- findHottest(requests)
  } yield hottest

  def askCity: F[City] = for {
    _         <- console.writeLine("What is the next city ?")
    cityName  <- console.readLine
    city      <- cityByName(cityName)
  } yield city

  def askFetchJudge: F[Unit] = for {
    city      <- askCity
    forcast   <- fetchForecast(city)
    _         <- console.writeLine(s"Forcast for $city is ${forcast.temperature}")
    hottest   <- hottestCity
    _         <- console.writeLine(s"Hottest city found so far is $hottest")
  } yield ()

  def run: F[Unit] = for {
    h <- host
    p <- port
    _ <- console.writeLine(s"Host : $h | Port : $p")
    _ <- askFetchJudge.forever
  } yield ()
}
