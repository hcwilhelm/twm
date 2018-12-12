package interpreters

import algebras.Weather
import cats.data.EitherT
import cats.effect.IO
import cats.implicits._
import domain.Domain.{City, Config, Forecast}
import monix.eval.Task
import services.{OpenWeatherClient, WeatherClient}

object WeatherInterpreter {
  def apply(config: Config): Weather[MonixEffect] = new Weather[MonixEffect] {
    val client = new WeatherClient(config.host, config.port)
    override def forecast(city: City): MonixEffect[Forecast] = client.forecast(city).pure[MonixEffect]
  }
}

class OpenWeatherInterpreterMonix(config: Config) extends Weather[MonixEffect] {
  val client = new OpenWeatherClient(config.host, config.port)
  override def forecast(city: City): MonixEffect[Forecast] = EitherT(Task.fromFuture(client.forecast(city)))
}

object OpenWeatherInterpreterMonix {
  def apply(config: Config): OpenWeatherInterpreterMonix = new OpenWeatherInterpreterMonix(config)
}

class OpenWeatherInterpreterCats(config: Config) extends Weather[CatsEffect] {
  val client = new OpenWeatherClient(config.host, config.port)
  override def forecast(city: City): CatsEffect[Forecast] = EitherT(IO.fromFuture(IO(client.forecast(city))))
}

object OpenWeatherInterpreterCats {
  def apply(config: Config): OpenWeatherInterpreterCats = new OpenWeatherInterpreterCats(config)
}