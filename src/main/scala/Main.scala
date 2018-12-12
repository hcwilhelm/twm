import algebras._
import domain.Domain._
import interpreters._

object Main extends App {

  val config = Config("http://api.openweathermap.org/data/2.5/weather", 80)

  implicit val configAskInterpreter: ConfigAsk[MonixEffect] = ConfigAskInterpreter(config)
  implicit val consoleInterpreter: Console[MonixEffect] = ConsoleInterpreter()
  implicit val weatherInterpreter: Weather[MonixEffect] = OpenWatherInterpreter(config)
  implicit val requestStateInterpreter: AtomicMonadState[Requests] = AtomicMonadState(Requests.empty)

  val program = new Program[MonixEffect]

  import interpreters.io
  program.run.value foreach println
}
