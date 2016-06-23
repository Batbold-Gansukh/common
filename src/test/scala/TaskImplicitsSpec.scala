import cats.data.Xor
import monix.eval.Task
import org.specs2.mutable._

import scala.concurrent.Await
import scala.concurrent.duration._
import helpers.task.implicits._
import monix.execution.Scheduler.Implicits._
import helpers.common.pp

/**
  * Created by batbold on 6/24/16.
  */
class TaskImplicitsSpec extends Specification {

  "Task" should {
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
