package helpers.future

import monix.eval.Task

import scala.concurrent.Future

/**
  * Created by batbold on 6/24/16.
  */
object implicits {

  implicit class FutureToTask[T](future: Future[T]) {
    def task: Task[T] = Task.defer(Task.fromFuture(future))
  }

}
