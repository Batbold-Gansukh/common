import org.specs2.mutable.Specification
import helpers.util.FieldNameExtractor
import helpers.common.pp

/**
  * Created by batbold on 8/20/16.
  */
class FieldNameExtractorSpec extends Specification {

  case class Test(field1: String, field2: Int, field3: Long, field4: List[String])

  "FieldNameExtractor" should {
    "correct extract field names" in {
      val fieldNames = FieldNameExtractor.extract[Test]
      pp(s"fieldNames:$fieldNames")
      fieldNames.size mustEqual 4
      fieldNames('field1) mustEqual "field1"
      fieldNames('field2) mustEqual "field2"
      fieldNames('field3) mustEqual "field3"
      fieldNames('field4) mustEqual "field4"
    }
  }

}
