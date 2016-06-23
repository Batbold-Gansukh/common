/**
  * Created by batbold on 2/5/16.
  */

import cats.data.Xor
import org.specs2.mutable._

import scala.util.{Failure, Success}

class TryImplicitsSpec extends Specification {

  import helpers.utiltry.implicits._

  "TryImplicits" should {
    "work correctly" in {
      Success("success").toXor shouldEqual Xor.right[Throwable, String]("success")
      val throwable = new Throwable("throwable")
      Failure(throwable).toXor shouldEqual Xor.left[Throwable, String](throwable)
    }
  }

}
