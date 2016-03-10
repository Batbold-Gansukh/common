package utils

import java.sql.Timestamp

import org.joda.time.DateTime
import play.api.data.validation.ValidationError
import play.api.libs.json._
import cats.data.Xor

/**
  * Created by batbold on 12/2/15.
  */
object CommonJsonImplicits {

  implicit val sqlTimestampFormat = new Format[Timestamp] {

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

}
