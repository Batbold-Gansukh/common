import cats.data.Xor
import org.specs2.mutable.Specification

import scala.concurrent.ExecutionContext.Implicits.global

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
  }

}
