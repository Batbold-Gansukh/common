import cats.data.Xor
import org.specs2.mutable.Specification

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by batbold on 5/20/16.
  */
class HelpersSpec extends Specification {

  "Helpers" should {
    "FXLT" in {
      import utils.Helpers.FXLT
      val string = "string"
      val fxlt = FXLT(string)
      fxlt.isCompleted mustEqual true
      fxlt.foreach { xlt ⇒
        xlt.isLeft mustEqual true
        xlt.isInstanceOf[Xor.Left[Throwable]] mustEqual true
        xlt.leftMap { throwable ⇒
          throwable.getMessage mustEqual string
        }
      }
      success
    }

    "XLT" in {
      import utils.Helpers.XLT
      val string = "string"
      val xlt = XLT(string)
      xlt.isLeft mustEqual true
      xlt.isInstanceOf[Xor.Left[Throwable]] mustEqual true
      xlt.leftMap { throwable ⇒
        throwable.getMessage mustEqual string
      }
      success
    }

    "FXT" in {
      import utils.Helpers.FXT
      val string = "string"
      val fxt = FXT[Unit](string)
      fxt.isCompleted mustEqual true
      fxt.foreach { xt ⇒
        xt.isLeft mustEqual true
        xt.isInstanceOf[Xor[Throwable, Unit]] mustEqual true
      }
      success
    }

    "XT" in {
      import utils.Helpers.XT
      val string = "string"
      val xt = XT[Unit](string)
      xt.isLeft mustEqual true
      xt.isInstanceOf[Xor[Throwable, Unit]] mustEqual true
      success
    }

    "FXL" in {
      import utils.Helpers.FXL
      val value = Some(1)
      val fxl = FXL[Option[Int]](value)
      fxl.isCompleted mustEqual true
      fxl.foreach { xl ⇒
        xl.isLeft mustEqual true
        xl.isInstanceOf[Xor.Left[Option[Int]]] mustEqual true
      }
      success
    }

    "XL" in {
      import utils.Helpers.XL
      val value = Some(1)
      val xl = XL[Option[Int]](value)
      xl.isLeft mustEqual true
      xl.isInstanceOf[Xor.Left[Option[Int]]] mustEqual true
      success
    }

    "FX" in {
      import utils.Helpers.FX
      val value = Some(1)
      val fx = FX[Option[Int], Unit](value)
      fx.foreach { x ⇒
        x.isLeft mustEqual true
        x.isInstanceOf[Option[Int] Xor Unit] mustEqual true
      }
      success
    }

    "FS" in {
      import utils.Helpers.FS
      val value = Some(1)
      val fs1 = FS(value)
      fs1.foreach { v ⇒
        v mustEqual value
      }
      val fs2 = FS {
        value
      }
      fs2.foreach { v ⇒
        v mustEqual value
      }
      success
    }

    "X" in {
      import utils.Helpers.X
      val value = Some(1)
      val x = X[Option[Int], Unit](value)
      x.isLeft mustEqual true
      x.isInstanceOf[Option[Int] Xor Unit] mustEqual true
      success
    }
  }

}
