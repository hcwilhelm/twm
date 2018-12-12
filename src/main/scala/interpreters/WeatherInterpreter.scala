package interpreters

import algebras.Weather
import cats.data.EitherT
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

object OpenWatherInterpreter {
  def apply(config: Config): Weather[MonixEffect] = new Weather[MonixEffect] {
    val client = new OpenWeatherClient(config.host, config.port)
    override def forecast(city: City): MonixEffect[Forecast] = EitherT(Task.fromFuture(client.forecast(city)))
  }
}