import cats.data.EitherT
import domain.Domain.Error
import monix.eval.Task
import monix.execution.Scheduler
import monix.execution.schedulers.SchedulerService

package object interpreters {
  /**
    * Monad stack able to provide an instance for MonadError
    */
  type MonixEffect[A] = EitherT[Task, Error, A]

  /**
    * Monix SchedulerService
    */
  implicit val io: SchedulerService = Scheduler.io()
}
