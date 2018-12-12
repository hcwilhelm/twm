package interpreters

import algebras.Console
import cats.implicits._

class ConsoleInterpreterMonix extends Console[MonixEffect] {
  override def readLine: MonixEffect[String] = scala.io.StdIn.readLine().pure[MonixEffect]
  override def writeLine(line: String): MonixEffect[Unit] = println(line).pure[MonixEffect]
}

object ConsoleInterpreterMonix {
  def apply(): Console[MonixEffect] = new ConsoleInterpreterMonix()
}

class ConsoleInterpreterCats extends Console[CatsEffect] {
  override def readLine: CatsEffect[String] = scala.io.StdIn.readLine().pure[CatsEffect]
  override def writeLine(line: String): CatsEffect[Unit] = println(line).pure[CatsEffect]
}

object ConsoleInterpreterCats {
  def apply(): Console[CatsEffect] = new ConsoleInterpreterCats()
}