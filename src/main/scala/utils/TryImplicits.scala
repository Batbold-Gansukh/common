package utils

/**
  * Created by batbold on 2/5/16.
  */

import cats.data.Xor

import scala.util.Try

object TryImplicits {

  implicit class TryInstance[T](tr: Try[T]) {
    def toXor: Throwable Xor T = Xor.fromTry(tr)
  }

}
