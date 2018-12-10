package algebras

import domain.Domain.{City, Forecast}


trait Weather[F[_]] {
  def forecast(city: City): F[Forecast]
}

object Weather {
  def apply[F[_]: Weather]: Weather[F] = implicitly[Weather[F]]
}
