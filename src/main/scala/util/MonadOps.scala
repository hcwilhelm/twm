package util

import cats.Monad
import cats.syntax.all._

object MonadOps {
  private def forever[F[_] : Monad, A](fa: F[A]): F[A] = fa.flatMap(_ => forever(fa))

  implicit class RichMonad[F[_], A](val self: F[A])(implicit val F: Monad[F]) {
    def forever: F[A] = MonadOps.forever(self)
  }
}
