package utils

import slick.dbio.DBIO

import scala.concurrent.ExecutionContext

/**
  * Created by batbold on 2/5/16.
  */
object SlickCatImplicits {

  import TryImplicits._

  implicit class DBIOInstance[+T](dbio: DBIO[T]) {
    def asXor(implicit ec:ExecutionContext) = dbio.asTry.map(_.toXor)
  }

}
