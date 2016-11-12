package helpers.task

import cats.data.Xor
import monix.eval.Task
import monix.execution.{CancelableFuture, Scheduler}
import play.api.mvc.Result
import play.api.mvc.Results._

import scala.util.{Failure, Success}

/**
  * Created by batbold on 6/24/16.
  */
object implicits {

  implicit class TaskToResultPlayLogger(task: Task[Result]) {
    def resultWithInfo()(implicit sch: Scheduler, logger: play.api.Logger.type): CancelableFuture[Result] =
      task.materialize.runAsync map {
        case Success(result) ⇒ result
        case Failure(reason) ⇒
          logger.error(reason.getMessage, reason)
          InternalServerError(reason.getMessage)
      }

    def result()(implicit sch: Scheduler, logger: play.api.Logger.type): CancelableFuture[Result] =
      task.materialize.runAsync map {
        case Success(result) ⇒ result
        case Failure(reason) ⇒
          logger.error(reason.getMessage, reason)
          InternalServerError
      }
  }


  implicit class TaskToResultScalaLogging(task: Task[Result]) {
    def resultWithInfo()(implicit sch: Scheduler, logger: com.typesafe.scalalogging.Logger): CancelableFuture[Result] =
      task.materialize.runAsync map {
        case Success(result) ⇒ result
        case Failure(reason) ⇒
          logger.error(reason.getMessage, reason)
          InternalServerError(reason.getMessage)
      }

    def result()(implicit sch: Scheduler, logger: com.typesafe.scalalogging.Logger): CancelableFuture[Result] =
      task.materialize.runAsync map {
        case Success(result) ⇒ result
        case Failure(reason) ⇒
          logger.error(reason.getMessage, reason)
          InternalServerError
      }
  }

  implicit class TaskToXor[T](task: Task[T]) {
    def xor: Task[Xor[Throwable, T]] = task.materialize.map(cats.data.Xor.fromTry)
  }

  implicit class LeftThrowableXorToTask[L <: Throwable, R](xor: Xor[L, R]) {
    def task: Task[R] = xor match {
      case Xor.Left(l) ⇒ Task.raiseError(l)
      case Xor.Right(r) ⇒ Task(r)
    }
  }

  implicit class LeftStringXorToTask[R](xor: Xor[String, R]) {
    def task: Task[R] = xor match {
      case Xor.Left(str) ⇒ Task.raiseError(new Throwable(str))
      case Xor.Right(r) ⇒ Task(r)
    }
  }

  implicit class RightAnyXorToTask[R](right: Xor.Right[R]) {
    def task: Task[R] = right match {
      case Xor.Right(r) ⇒ Task(r)
    }
  }

  implicit class LeftStringOnlyXorToTask(left: Xor.Left[String]) {
    def task: Task[Unit] = left match {
      case Xor.Left(str) ⇒ Task.raiseError(new Throwable(str))
    }
  }

  implicit class LeftThrowableOnlyXorToTask(left: Xor.Left[Throwable]) {
    def task: Task[Nothing] = left match {
      case Xor.Left(throwable) ⇒ Task.raiseError(throwable)
    }
  }

}
