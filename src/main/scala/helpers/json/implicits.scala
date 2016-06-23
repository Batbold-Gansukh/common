package helpers.json

import java.sql.Timestamp

import cats.data.Xor
import org.joda.time.DateTime
import play.api.data.validation.ValidationError
import play.api.libs.json.{JsError, JsSuccess, Reads, _}

/**
  * Created by batbold on 6/24/16.
  */
object implicits {

  implicit val sqlTimestampLongFormat = new Format[Timestamp] {

    override def reads(json: JsValue): JsResult[Timestamp] = Json.fromJson[DateTime](json).map { x ⇒
      new Timestamp(x.getMillis)
    }

    override def writes(o: Timestamp): JsValue = Json.toJson(new DateTime(o.getTime))

  }

  implicit class JsValueToXor(jsLookupResult: JsLookupResult) {
    def toXor[T](
      implicit fjs: Reads[T]
    ): ValidationError Xor T = {
      jsLookupResult.toEither match {
        case Left(ve: ValidationError) => Xor.left(ve)
        case Right(value: JsValue) =>
          value.asOpt[T] match {
            case Some(t) => Xor.right[ValidationError, T](t)
            case None => Xor.left[ValidationError, T](
              ValidationError(s"a value exists by name but unable to convert to T"))
          }
      }
    }
  }

  implicit class JsResultToXor[T](jsResult: JsResult[T]) {
    def toXor: String Xor T = jsResult match {
      case JsSuccess(t, _) ⇒ Xor.Right(t)
      case jsError: JsError ⇒ Xor.Left(
        jsError.errors.map(x ⇒ x._2.map(_.message).mkString("\n")).mkString("\n")
      )
    }
  }

  implicit class JsResultToXorWithError[T](jsResult: JsResult[T]) {
    def toXorWithError[E](error: E): E Xor T = jsResult match {
      case JsSuccess(t, _) ⇒ Xor.Right(t)
      case _: JsError ⇒ Xor.Left(error)
    }
  }

}
