package helpers.json

import java.sql.Timestamp

import cats.data.Xor
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import play.api.libs.json.{JsError, JsSuccess, Reads, _}

/**
  * Created by batbold on 6/24/16.
  */
object implicits {

  val dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"

  implicit val jodaDateReads: Reads[DateTime] = Reads[DateTime](js =>
    js.validate[String].map[DateTime](dtString =>
      DateTime.parse(dtString, DateTimeFormat.forPattern(dateFormat))
    )
  )

  implicit val jodaDateWrites: Writes[DateTime] = new Writes[DateTime] {
    def writes(d: DateTime): JsValue = JsString(d.toString())
  }

  implicit val sqlTimestampLongFormat = new Format[Timestamp] {

    override def reads(json: JsValue): JsResult[Timestamp] = Json.fromJson[DateTime](json).map { x ⇒
      new Timestamp(x.getMillis)
    }

    override def writes(o: Timestamp): JsValue = Json.toJson(new DateTime(o.getTime))

  }

  implicit class JsValueToXor(jsLookupResult: JsLookupResult) {
    def toXor[T](
      implicit fjs: Reads[T]
    ): JsonValidationError Xor T = {
      jsLookupResult.toEither match {
        case Left(ve: JsonValidationError) => Xor.left(ve)
        case Right(value: JsValue) =>
          value.asOpt[T] match {
            case Some(t) => Xor.right[JsonValidationError, T](t)
            case None => Xor.left[JsonValidationError, T](
              JsonValidationError(s"a value exists by name but unable to convert to T"))
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
