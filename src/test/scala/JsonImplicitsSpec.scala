/**
  * Created by batbold on 1/30/16.
  */

import org.specs2.mutable._
import play.api.libs.json._

class JsonImplicitsSpec extends Specification {

  import helpers.json.implicits._

  "JSLookupResult.toEither" should {
    val testJson = Json.parse(
      """
        |{"test":"test","int":123,"array":[11,22,33]}
      """.stripMargin)
    "correctly work" in {
      (testJson \ "test").toEither must beRight(JsString("test"))
      (testJson \ "test").toEither must beAnInstanceOf[Right[JsonValidationError, JsString]]
      (testJson \ "int").toEither must beRight(JsNumber(123))
      (testJson \ "int").toEither must beRight(JsNumber(123))
      (testJson \ "array").toEither.map(_.as[Array[Int]]) must beRight(Array(11, 22, 33))
      (testJson \ "array").toEither must beAnInstanceOf[Right[JsonValidationError, _]]
      (testJson \ "array1").toEither must beAnInstanceOf[Left[JsonValidationError, _]]
    }
  }

  "JsResult.toEither" should {
    "JsSuccess" in {
      JsSuccess(1L).toEither must beRight(1L)
      JsSuccess(Some(1)).toEitherWithError("error") must beRight(Some(1))
    }
    "JsError" in {
      JsError("error").toEither must beLeft("error")
      JsError("error").toEitherWithError("anotherError") must beLeft("anotherError")
      JsError("error").toEitherWithError(Some("anotherError")) must beLeft(Some("anotherError"))
    }
  }

}
