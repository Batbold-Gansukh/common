package helpers.utiltry

import cats.syntax.either._
import scala.util.Try

/**
  * Created by batbold on 6/24/16.
  */
object implicits {

  implicit class TryInstance[T](tr: Try[T]) {
    def toEither: Throwable Either T = Either.fromTry(tr)
  }

}
