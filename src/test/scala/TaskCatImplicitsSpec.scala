import cats.data.Xor
import org.specs2.mutable.Specification

import scala.concurrent.Await
import scala.concurrent.duration._
import monix.execution.Scheduler.Implicits._

import scala.util.{Failure, Success, Try}

/**
  * Created by batbold on 6/25/16.
  */
class TaskCatImplicitsSpec extends Specification {

  "Xor to Task" should {
    val value = 1
    val string = "string"
    implicit val timeout = 4.seconds
    "throwable xor" in {
      import helpers.task.cats.implicits.LeftThrowableXorToTask
      val right = Xor.right[Throwable, Int](value).task
      Await.result(right.runAsync, timeout) mustEqual value
      val throwable = new Throwable(string)
      val left = Xor.left[Throwable, Int](throwable).task
      val resultThrowable: Try[Int] = Await.result(left.materialize.runAsync, timeout)
      resultThrowable.isFailure mustEqual true
      resultThrowable match {
        case Failure(err) ⇒ err.getMessage mustEqual string
        case _ ⇒ ko
      }
    }
    "string xor" in {
      import helpers.task.cats.implicits.LeftStringXorToTask
      val right = Xor.right[String, Int](value).task
      Await.result(right.runAsync, timeout) mustEqual value
      val left = Xor.left[String, Int](string).task
      val resultThrowable = Await.result(left.materialize.runAsync, timeout)
      resultThrowable.isFailure mustEqual true
      resultThrowable match {
        case Failure(err) ⇒ err.getMessage mustEqual string
        case _ ⇒ ko
      }
    }
    "Xor.Right any" in {
      import helpers.task.cats.implicits._
      val right = Xor.Right(value).task
      Await.result(right.runAsync, timeout) mustEqual value
    }
    "Xor.Left[String]" in {
      import helpers.task.cats.implicits._
      val left = Xor.Left(string).task
      val res = Await.result(left.materialize.runAsync, timeout)
      res.isFailure mustEqual true
      res match {
        case Failure(throwable) ⇒ throwable.getMessage mustEqual string
        case Success(_) ⇒ ko
      }
    }
    "Xor.Left[Throwable]" in {
      import helpers.task.cats.implicits._
      val throwable = new Throwable(string)
      val left = Xor.Left(throwable).task
      val res = Await.result(left.materialize.runAsync, timeout)
      res.isFailure mustEqual true
      res match {
        case Failure(err) ⇒ err.getMessage mustEqual string
        case Success(_) ⇒ ko
      }
    }
  }

}
