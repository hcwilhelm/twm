import algebras._
import domain.Domain._
import interpreters._

object Main extends App {

  val config = Config("http://api.openweathermap.org/data/2.5/weather", 80)

  implicit val configAskInterpreter: ConfigAsk[CatsEffect] = ConfigAskInterpreterCats(config)
  implicit val consoleInterpreter: Console[CatsEffect] = ConsoleInterpreterCats()
  implicit val weatherInterpreter: Weather[CatsEffect] = OpenWeatherInterpreterCats(config)
  implicit val requestStateInterpreter: AtomicMonadStateCats[Requests] = AtomicMonadStateCats(Requests.empty)

  val program = new Program[CatsEffect]

  //import interpreters.io
  program.run.value.unsafeRunSync()
}
