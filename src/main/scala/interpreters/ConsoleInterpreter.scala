package interpreters

import algebras.Console
import cats.implicits._

object ConsoleInterpreter {

  def apply(): Console[Effect] = new Console[Effect] {
    override def readLine: Effect[String] =
      scala.io.StdIn.readLine().pure[Effect]

    override def writeLine(line: String): Effect[Unit] =
      println(line).pure[Effect]
  }
}

