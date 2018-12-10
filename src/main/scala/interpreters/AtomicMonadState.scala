package interpreters

import cats.Monad
import cats.mtl.MonadState
import monix.execution.atomic.{Atomic, AtomicAny}

class AtomicMonadState[S](atomic: Atomic[S]) extends MonadState[Effect, S] {
  override val monad: Monad[Effect] = Monad[Effect]

  override def get: Effect[S] = monad.pure(atomic.get())

  override def set(s: S): Effect[Unit] = monad.pure(atomic.set(s))

  override def inspect[A](f: S => A): Effect[A] = monad.pure(f(atomic.get()))

  override def modify(f: S => S): Effect[Unit] = monad.pure(atomic.transform(f))
}

object AtomicMonadState {
  def apply[S <: AnyRef](s: S): AtomicMonadState[S] = new AtomicMonadState(AtomicAny[S](s))
}
