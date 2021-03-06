import java.security.SecureRandom

import cats.syntax.either._
import helpers.common._
import monix.execution.Scheduler.Implicits._
import org.specs2.mutable.Specification

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._


/**
  * Created by batbold on 5/20/16.
  */
class AbbreviationSpec extends Specification {

  "Helpers" should {
    "FXLT" in {
      import helpers.abbreviations.FXLT
      val string = "string"
      val fxlt = FXLT(string)
      fxlt.isCompleted mustEqual true
      Await.result(fxlt.map { xlt ⇒
        xlt.isLeft mustEqual true
        xlt match {
          case Left(throwable) ⇒ throwable.getMessage mustEqual string
          case _ ⇒ failure
        }
        xlt.isInstanceOf[Left[Throwable,_]] mustEqual true
      }, 2.seconds)
    }

    "XLT" in {
      import helpers.abbreviations.XLT
      val string = "string"
      val xlt = XLT(string)
      xlt.isLeft mustEqual true
      xlt.isInstanceOf[Left[Throwable,_]] mustEqual true
      xlt.leftMap { throwable ⇒
        throwable.getMessage mustEqual string
      }
      success
    }

    "FXT" in {
      import helpers.abbreviations.FXT
      val string = "string"
      val fxt = FXT[Unit](string)
      fxt.isCompleted mustEqual true
      Await.result(fxt.map { xt ⇒
        xt.isLeft mustEqual true
        xt.isInstanceOf[Either[Throwable, Unit]] mustEqual true
      }, 2.seconds)
    }

    "XT" in {
      import helpers.abbreviations.XT
      val string = "string"
      val xt = XT[Unit](string)
      xt.isLeft mustEqual true
      xt.isInstanceOf[Either[Throwable, Unit]] mustEqual true
    }

    "FXL" in {
      import helpers.abbreviations.FXL
      val value = Some(1)
      val fxl = FXL[Option[Int]](value)
      fxl.isCompleted mustEqual true
      Await.result(fxl.map { xl ⇒
        xl.isLeft mustEqual true
        xl.isInstanceOf[Left[Option[Int],_]] mustEqual true
      }, 2.seconds)
    }

    "XL" in {
      import helpers.abbreviations.XL
      val value = Some(1)
      val xl = XL[Option[Int]](value)
      xl.isLeft mustEqual true
      xl.isInstanceOf[Left[Option[Int],_]] mustEqual true
    }

    "XR" in {
      import helpers.abbreviations.XR
      val value = Some(1)
      val xl = XR[Option[Int]](value)
      xl.isRight mustEqual true
      xl.isInstanceOf[Right[_,Option[Int]]] mustEqual true
    }

    "FX" in {
      import helpers.abbreviations.FX
      val value = Some(1)
      val fx = FX[Option[Int], Unit](value)
      Await.result(fx.map { x ⇒
        x.isLeft mustEqual true
        x.isInstanceOf[Option[Int] Either Unit] mustEqual true
      }, 2.seconds)
    }

    "FS" in {
      import helpers.abbreviations.FS
      val value = Some(1)
      val fs1 = FS(value)
      Await.result(fs1.map { v ⇒
        v mustEqual value
      }, 2.seconds)
      val fs2 = FS {
        value
      }
      Await.result(fs2.map { v ⇒
        v mustEqual value
      }, 2.seconds)
    }

    "X" in {
      import helpers.abbreviations.X
      val value = Some(1)
      val x = X[Option[Int], Unit](value)
      x.isLeft mustEqual true
      x.isInstanceOf[Option[Int] Either Unit] mustEqual true
    }

    "TXLT" in {
      import helpers.abbreviations.TXLT
      val txlt = TXLT("throwable")
      val value = Await.result(txlt.runAsync, 2.seconds)
      value.isLeft mustEqual true
    }

    "TXT" in {
      import helpers.abbreviations.TXT
      val txt = TXT[Int]("string")
      val value = Await.result(txt.runAsync, 2.seconds)
      value match {
        case Left(throwable) ⇒ throwable.getMessage mustEqual "string"
        case _ ⇒ failure
      }
      value.isLeft mustEqual true
    }

    "TXL" in {
      import helpers.abbreviations.TXL
      val txl = TXL(1)
      val value = Await.result(txl.runAsync, 2.seconds)
      value.isLeft mustEqual true
      value must beLeft(1)
    }

    "TX" in {
      import helpers.abbreviations.TX
      val tx = TX[Int, String](1)
      val value = Await.result(tx.runAsync, 2.seconds)
      value.isLeft mustEqual true
      value.isInstanceOf[Either[Int, String]] mustEqual true
      value must beLeft(1)
    }

    "FT" in {
      import helpers.abbreviations.FT
      val random = SecureRandom.getInstanceStrong
      var state = 1

      val task = FT(Future.apply({
        state += 1
        pp(s"from future:${random.nextInt()} state:$state", Console.YELLOW)
        state
      }))

      pp("first wait")
      Await.result(task.runAsync, 2.seconds)
      state mustEqual 2

      pp("second wait")
      Await.result(task.runAsync, 2.seconds)
      state mustEqual 3
    }

    "TT" in {
      import helpers.abbreviations.TT
      val str = "string"
      val res = Await.result(TT(str).materialize.runAsync, 2.seconds)
      res.isFailure mustEqual true
    }
  }
}
