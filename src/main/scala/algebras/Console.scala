package algebras

/**
  * Algebra to read and write to console
  */
trait Console[F[_]] {
  def readLine: F[String]
  def writeLine(line: String): F[Unit]
}

object Console {
  def apply[F[_]: Console]: Console[F] = implicitly[Console[F]]
}