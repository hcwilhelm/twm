package interpreters

import domain.Domain.Config
import algebras.ConfigAsk
import cats.Applicative
import cats.mtl.DefaultApplicativeAsk

object ConfigAskInterpreter {

  def apply(config: Config): ConfigAsk[MonixEffect] = new DefaultApplicativeAsk[MonixEffect, Config] {
    override val applicative: Applicative[MonixEffect] = Applicative[MonixEffect]
    override def ask: MonixEffect[Config] = applicative.pure(config)
  }
}

