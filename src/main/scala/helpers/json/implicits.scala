package helpers.json

import java.sql.Timestamp

import org.joda.time.DateTime
import play.api.libs.json.{JsError, JsSuccess, _}
import play.api.libs.json.JodaReads._

/**
  * Created by batbold on 6/24/16.
  */
object implicits {

  //  val dateFormat = "yyyy-MM-dd HH:mm:ss.SSS"
  //
  //  implicit val jodaDateReads: Reads[DateTime] = Reads[DateTime](js =>
  //    js.validate[String].map[DateTime](dtString =>
  //      DateTime.parse(dtString, DateTimeFormat.forPattern(dateFormat))
  //    )
  //  )
  //
  //  implicit val jodaDateWrites: Writes[DateTime] = new Writes[DateTime] {
  //    def writes(d: DateTime): JsValue = JsString(d.toString())
  //  }

  implicit val sqlTimestampLongFormat: Format[Timestamp] = new Format[Timestamp] {

    override def reads(json: JsValue): JsResult[Timestamp] = Json.fromJson[DateTime](json).map { x ⇒
      new Timestamp(x.getMillis)
    }

    override def writes(o: Timestamp): JsValue = Json.toJson(o.getTime)

  }

  //  implicit class JsValueToEither(jsLookupResult: JsLookupResult) {
  //    def toEither[T](
  //      implicit fjs: Reads[T]
  //    ): JsonValidationError Either T = {
  //      jsLookupResult.toEither match {
  //        case Left(ve: JsonValidationError) => Left(ve)
  //        case Right(value: JsValue) =>
  //          value.asOpt[T] match {
  //            case Some(t) => Right[JsonValidationError, T](t)
  //            case None => Left[JsonValidationError, T](
  //              JsonValidationError(s"a value exists by name but unable to convert to T"))
  //          }
  //      }
  //    }
  //  }
  //
  implicit class JsResultToEither[T](jsResult: JsResult[T]) {
    def toEither: String Either T = jsResult match {
      case JsSuccess(t, _) ⇒ Right(t)
      case jsError: JsError ⇒ Left(
        jsError.errors.map(x ⇒ x._2.map(_.message).mkString("\n")).mkString("\n")
      )
    }
  }

  implicit class JsResultToEitherWithError[T](jsResult: JsResult[T]) {
    def toEitherWithError[E](error: E): E Either T = jsResult match {
      case JsSuccess(t, _) ⇒ Right(t)
      case _: JsError ⇒ Left(error)
    }
  }

}
