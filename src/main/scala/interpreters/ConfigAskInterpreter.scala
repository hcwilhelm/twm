package interpreters

import domain.Domain.Config
import algebras.ConfigAsk
import cats.Applicative
import cats.mtl.DefaultApplicativeAsk


class ConfigAskInterpreterMonix(config: Config) extends DefaultApplicativeAsk[MonixEffect, Config] {
  override val applicative: Applicative[MonixEffect] = Applicative[MonixEffect]
  override def ask: MonixEffect[Config] = applicative.pure(config)
}

object ConfigAskInterpreterMonix {
  def apply(config: Config): ConfigAsk[MonixEffect] = new ConfigAskInterpreterMonix(config)
}

class ConfigAskInterpreterCats(config: Config) extends DefaultApplicativeAsk[CatsEffect, Config] {
  override val applicative: Applicative[CatsEffect] = Applicative[CatsEffect]
  override def ask: CatsEffect[Config] = applicative.pure(config)
}

object ConfigAskInterpreterCats {
  def apply(config: Config): ConfigAsk[CatsEffect] = new ConfigAskInterpreterCats(config)
}