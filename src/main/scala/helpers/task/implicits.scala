package helpers.task

import cats.data.Xor
import monix.eval.Task

/**
  * Created by batbold on 6/24/16.
  */
object implicits {

  implicit class TaskToXor[T](task: Task[T]) {
    def xor = task.materialize.map(Xor.fromTry)
  }

}
