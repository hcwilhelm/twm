import cats.MonadError
import cats.mtl.{ApplicativeAsk, MonadState}
import domain.Domain._

package object algebras {

  /**
    * Access to Config inside context fo F[_]
    */
  type ConfigAsk[F[_]] = ApplicativeAsk[F, Config]

  object ConfigAsk {
    def apply[F[_]: ConfigAsk]: ConfigAsk[F] = implicitly[ConfigAsk[F]]
  }

  /**
    * Ability to raise domain errors inside context F[_]
    *
    * Note: I could not use ApplicativeError here, cause later in the programs I get
    *
    * Error:(14, 41) ambiguous implicit values:
    * both value evidence$1 in class Program of type algebras.ErrorHandler[F]
    * and value evidence$2 in class Program of type cats.Monad[F]
    * match expected type cats.Applicative[F]
    * case "Berlin" => City(cityName).pure[F]
    *
    */
  type ErrorHandler[F[_]] = MonadError[F, Error]

  object ErrorHandler {
    def apply[F[_]: ErrorHandler]: ErrorHandler[F] = implicitly[MonadError[F, Error]]
  }

  /**
    * Ability to deal with state inside context F[_]
    */
  type RequestState[F[_]] = MonadState[F, Requests]

  object RequestState {
    def apply[F[_]: RequestState]: RequestState[F] = implicitly[MonadState[F, Requests]]
  }
}
