package helpers.utiltry

import cats.data.Xor

import scala.util.Try

/**
  * Created by batbold on 6/24/16.
  */
object implicits {

  implicit class TryInstance[T](tr: Try[T]) {
    def toXor: Throwable Xor T = Xor.fromTry(tr)
  }

}
