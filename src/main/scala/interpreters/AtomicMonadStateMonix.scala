package interpreters

import cats.Monad
import cats.mtl.MonadState
import monix.execution.atomic.{Atomic, AtomicAny}

class AtomicMonadStateMonix[S](atomic: Atomic[S]) extends MonadState[MonixEffect, S] {
  override val monad: Monad[MonixEffect] = Monad[MonixEffect]

  override def get: MonixEffect[S] = monad.pure(atomic.get())

  override def set(s: S): MonixEffect[Unit] = monad.pure(atomic.set(s))

  override def inspect[A](f: S => A): MonixEffect[A] = monad.pure(f(atomic.get()))

  override def modify(f: S => S): MonixEffect[Unit] = monad.pure(atomic.transform(f))
}

object AtomicMonadStateMonix {
  def apply[S <: AnyRef](s: S): AtomicMonadStateMonix[S] = new AtomicMonadStateMonix(AtomicAny[S](s))
}


/**
  * Somehow duplicated code. I will try later to abstract it away
  */
class AtomicMonadStateCats[S](atomic: Atomic[S]) extends MonadState[CatsEffect, S] {
  override val monad: Monad[CatsEffect] = Monad[CatsEffect]

  override def get: CatsEffect[S] = monad.pure(atomic.get())

  override def set(s: S): CatsEffect[Unit] = monad.pure(atomic.set(s))

  override def inspect[A](f: S => A): CatsEffect[A] = monad.pure(f(atomic.get()))

  override def modify(f: S => S): CatsEffect[Unit] = monad.pure(atomic.transform(f))
}

object AtomicMonadStateCats {
  def apply[S <: AnyRef](s: S): AtomicMonadStateCats[S] = new AtomicMonadStateCats(AtomicAny[S](s))
}