/**
  * Created by batbold on 2/5/16.
  */

import org.specs2.mutable._

import scala.util.{Failure, Success}

class TryImplicitsSpec extends Specification {

  "TryImplicits" should {
    "work correctly" in {
      Success("success").toEither shouldEqual Right[Throwable, String]("success")
      val throwable = new Throwable("throwable")
      Failure(throwable).toEither shouldEqual Left[Throwable, String](throwable)
    }
  }

}
