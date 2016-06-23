/**
  * Created by batbold on 2/5/16.
  */

import cats.data.Xor
import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._

import scala.util.{Failure, Success}

@RunWith(classOf[JUnitRunner])
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
