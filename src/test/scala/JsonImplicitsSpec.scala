/**
  * Created by batbold on 1/30/16.
  */

import cats.data.Xor
import org.specs2.mutable._
import play.api.data.validation.ValidationError
import play.api.libs.json.{JsError, JsSuccess, Json}

class JsonImplicitsSpec extends Specification {

  import helpers.json.implicits._

  "JSLookupResult.toXor" should {
    val testJson = Json.parse(
      """
        |{"test":"test","int":123,"array":[11,22,33]}
      """.stripMargin)
    "correctly work" in {
      (testJson \ "test").toXor[String] must beEqualTo(Xor.right[ValidationError, String]("test"))
      (testJson \ "test").toXor[Int] must beAnInstanceOf[Xor.Left[ValidationError]]
      (testJson \ "int").toXor[Int] must beEqualTo(Xor.right[ValidationError, Int](123))
      (testJson \ "int").toXor[Long] must beEqualTo(Xor.right[ValidationError, Int](123))
      (testJson \ "array").toXor[List[Int]] must beEqualTo(Xor.right[ValidationError, List[Int]](List(11, 22, 33)))
      (testJson \ "array").toXor[List[String]] must beAnInstanceOf[Xor.Left[ValidationError]]
      (testJson \ "array1").toXor[List[String]] must beAnInstanceOf[Xor.Left[ValidationError]]
    }
  }

  "JsResult.toXor" should {
    "JsSuccess" in {
      JsSuccess(1L).toXor must beEqualTo(Xor.Right(1L))
      JsSuccess(Some(1)).toXorWithError("error") must beEqualTo(Xor.Right(Some(1L)))
    }
    "JsError" in {
      JsError("error").toXor must beEqualTo(Xor.Left("error"))
      JsError("error").toXorWithError("anotherError") must beEqualTo(Xor.Left("anotherError"))
      JsError("error").toXorWithError(Some("anotherError")) must beEqualTo(Xor.Left(Some("anotherError")))
    }
  }

}
