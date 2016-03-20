import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import utils.Utils._

/**
  * Created by batbold on 3/20/16.
  */
@RunWith(classOf[JUnitRunner])
class ColorPrintSpec extends Specification {

  "pp" should {
    "print colored string correctly " in {
      pp("It is magenta")
      pp("It is blue", Console.BLUE)
      true
    }
  }

}
