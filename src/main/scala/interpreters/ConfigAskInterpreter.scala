package interpreters

import domain.Domain.Config
import algebras.ConfigAsk
import cats.Applicative
import cats.mtl.DefaultApplicativeAsk

object ConfigAskInterpreter {

  def apply(config: Config): ConfigAsk[Effect] = new DefaultApplicativeAsk[Effect, Config] {
    override val applicative: Applicative[Effect] = Applicative[Effect]
    override def ask: Effect[Config] = applicative.pure(config)
  }
}

