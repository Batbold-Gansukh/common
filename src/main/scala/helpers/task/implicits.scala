package helpers.task

import cats.data.Xor
import monix.eval.Task

/**
  * Created by batbold on 6/24/16.
  */
object implicits {

  implicit class TaskToXor[T](task: Task[T]) {
    def xor = task.materialize.map(cats.data.Xor.fromTry)
  }

  implicit class LeftThrowableXorToTask[L <: Throwable, R](xor: Xor[L, R]) {
    def task = xor match {
      case Xor.Left(l) ⇒ Task.raiseError(l)
      case Xor.Right(r) ⇒ Task(r)
    }
  }

  implicit class LeftStringXorToTask[R](xor: Xor[String, R]) {
    def task = xor match {
      case Xor.Left(str) ⇒ Task.raiseError(new Throwable(str))
      case Xor.Right(r) ⇒ Task(r)
    }
  }

  implicit class RightAnyXorToTask[R](right: Xor.Right[R]) {
    def task = right match {
      case Xor.Right(r) ⇒ Task(r)
    }
  }

  implicit class LeftStringOnlyXorToTask(left: Xor.Left[String]) {
    def task: Task[Unit] = left match {
      case Xor.Left(str) ⇒ Task.raiseError(new Throwable(str))
    }
  }

  implicit class LeftThrowableOnlyXorToTask(left: Xor.Left[Throwable]) {
    def task = left match {
      case Xor.Left(throwable) ⇒ Task.raiseError(throwable)
    }
  }

}
