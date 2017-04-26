package helpers.task

import cats.syntax.either._
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

  implicit class TaskToEither[T](task: Task[T]) {
    def either: Task[Either[Throwable, T]] = task.materialize.map(Either.fromTry)
  }

  implicit class LeftThrowableEitherToTask[L <: Throwable, R](either: Either[L, R]) {
    def task: Task[R] = either match {
      case Left(l) ⇒ Task.raiseError(l)
      case Right(r) ⇒ Task(r)
    }
  }

  implicit class LeftStringEitherToTask[R](either: Either[String, R]) {
    def task: Task[R] = either match {
      case Left(str) ⇒ Task.raiseError(new Throwable(str))
      case Right(r) ⇒ Task(r)
    }
  }

  implicit class RightAnyEitherToTask[R](right: Right[Any, R]) {
    def task: Task[R] = right match {
      case Right(r) ⇒ Task(r)
    }
  }

  implicit class LeftStringOnlyEitherToTask(left: Left[String, Any]) {
    def task: Task[Unit] = left match {
      case Left(str) ⇒ Task.raiseError(new Throwable(str))
    }
  }

  implicit class LeftThrowableOnlyEitherToTask(left: Left[Throwable, Any]) {
    def task: Task[Nothing] = left match {
      case Left(throwable) ⇒ Task.raiseError(throwable)
    }
  }

}
