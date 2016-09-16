import cats.data.Xor
import monix.eval.Task
import org.specs2.mutable._

import scala.concurrent.Await
import scala.concurrent.duration._
import helpers.task.implicits._
import monix.execution.Scheduler.Implicits._
import helpers.common.pp
import monix.execution.Scheduler
import play.api.Logger
import play.api.mvc.Result

import scala.concurrent.duration._

/**
  * Created by batbold on 6/24/16.
  */
class TaskImplicitsSpec extends Specification {

  "Task" should {
    {
      import play.api.mvc.Results._
      import helpers.task.implicits.TaskToResult
      implicit val sch = Scheduler
      implicit val implicitLogger = Logger

      "task to result when succeed" in {
        val task = Task.now(Ok)

        val result = Await.result(task.result(), 5.seconds)
        result mustEqual Ok
        result mustNotEqual Ok("Error")
      }
      "task to result when throw" in {
        val msg = "Throwable"
        val task = Task.raiseError[Result](new Throwable(msg))

        val result = Await.result(task.result(), 5.seconds)
        result mustEqual InternalServerError
        result mustNotEqual InternalServerError(msg)
      }

      "task to result with info when succeed" in {
        val msg = "ok"
        val task = Task.now(Ok(msg))

        val result = Await.result(task.resultWithInfo(), 5.seconds)
        result mustEqual Ok(msg)
        result mustNotEqual Ok
      }

      "task to result with info when throw" in {
        val msg = "Throwable"
        val task = Task.raiseError[Result](new Throwable(msg))

        val result = Await.result(task.resultWithInfo(), 5.seconds)
        result mustEqual InternalServerError(msg)
        result mustNotEqual InternalServerError
      }
    }

    "xor when throw" in {
      val shouldThrow = Task {
        if (true) throw new Throwable("from Task")
        else 1
      }
      Await.result(shouldThrow.runAsync, 3.seconds) must throwA(new Throwable("from Task"))
      val res = Await.result(shouldThrow.xor.runAsync, 3.seconds)
      res.isLeft mustEqual true
    }
    "xor when succeed" in {
      val shouldSucceed = Task {
        if (false) throw new Throwable("from task")
        else 1
      }
      Await.result(shouldSucceed.runAsync, 3.seconds) must not throwA new Throwable("from task")
      val res = Await.result(shouldSucceed.xor.runAsync, 3.seconds)
      res.isRight mustEqual true
      res mustEqual Xor.Right(1)
    }
    "short circuit" in {
      var state = 0
      val t1 = Task {
        state += 1
        state
      }
      val t2 = Task {
        pp(s"state:$state")
        if (state >= 0) throw new Throwable("from Task")
        else state += 1
        state
      }
      val t3 = Task {
        state += 1
        state
      }
      val task = for {
        r1 ← t1
        r2 ← t2
        r3 ← t3
      } yield r1 + r2 + r3

      Await.result(task.runAsync, 4.seconds) must throwA[Throwable]
      val res = Await.result(task.xor.runAsync, 5.seconds)
      res.isLeft mustEqual true
    }
  }

}
