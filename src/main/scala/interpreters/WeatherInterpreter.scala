package interpreters

import algebras.Weather
import cats.data.EitherT
import cats.implicits._
import domain.Domain.{City, Config, Forecast}
import monix.eval.Task
import services.{OpenWeatherClient, WeatherClient}

object WeatherInterpreter {
  def apply(config: Config): Weather[Effect] = new Weather[Effect] {
    val client = new WeatherClient(config.host, config.port)
    override def forecast(city: City): Effect[Forecast] = client.forecast(city).pure[Effect]
  }
}

object OpenWatherInterpreter {
  def apply(config: Config): Weather[Effect] = new Weather[Effect] {
    val client = new OpenWeatherClient(config.host, config.port)
    override def forecast(city: City): Effect[Forecast] = EitherT(Task.fromFuture(client.forecast(city)))
  }
}