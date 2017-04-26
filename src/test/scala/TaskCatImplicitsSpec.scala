import org.specs2.mutable.Specification

import scala.concurrent.Await
import scala.concurrent.duration._
import monix.execution.Scheduler.Implicits._

import scala.util.{Failure, Success, Try}

/**
  * Created by batbold on 6/25/16.
  */
class TaskCatImplicitsSpec extends Specification {

  "Either to Task" should {
    val value = 1
    val string = "string"
    implicit val timeout = 4.seconds
    "throwable either" in {
      import helpers.task.implicits.LeftThrowableEitherToTask
      val right = Right[Throwable, Int](value).task
      Await.result(right.runAsync, timeout) mustEqual value
      val throwable = new Throwable(string)
      val left = Left[Throwable, Int](throwable).task
      val resultThrowable: Try[Int] = Await.result(left.materialize.runAsync, timeout)
      resultThrowable.isFailure mustEqual true
      resultThrowable match {
        case Failure(err) ⇒ err.getMessage mustEqual string
        case _ ⇒ ko
      }
    }
    "string either" in {
      import helpers.task.implicits.LeftStringEitherToTask
      val right = Right[String, Int](value).task
      Await.result(right.runAsync, timeout) mustEqual value
      val left = Left[String, Int](string).task
      val resultThrowable = Await.result(left.materialize.runAsync, timeout)
      resultThrowable.isFailure mustEqual true
      resultThrowable match {
        case Failure(err) ⇒ err.getMessage mustEqual string
        case _ ⇒ ko
      }
    }
    "Either.Right any" in {
      import helpers.task.implicits._
      val right = Right[Any, Int](value).task
      Await.result(right.runAsync, timeout) mustEqual value
    }
    "Either.Left[String]" in {
      import helpers.task.implicits._
      val left = Left(string).task
      val res = Await.result(left.materialize.runAsync, timeout)
      res.isFailure mustEqual true
      res match {
        case Failure(throwable) ⇒ throwable.getMessage mustEqual string
        case Success(_) ⇒ ko
      }
    }
    "Either.Left[Throwable]" in {
      import helpers.task.implicits._
      val throwable = new Throwable(string)
      val left = Left(throwable).task
      val res = Await.result(left.materialize.runAsync, timeout)
      res.isFailure mustEqual true
      res match {
        case Failure(err) ⇒ err.getMessage mustEqual string
        case Success(_) ⇒ ko
      }
    }
  }

}
