package interpreters

import algebras.Console
import cats.implicits._

object ConsoleInterpreter {

  def apply(): Console[MonixEffect] = new Console[MonixEffect] {
    override def readLine: MonixEffect[String] =
      scala.io.StdIn.readLine().pure[MonixEffect]

    override def writeLine(line: String): MonixEffect[Unit] =
      println(line).pure[MonixEffect]
  }
}

