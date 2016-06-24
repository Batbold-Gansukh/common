package helpers.slick.cats

import slick.dbio.DBIO

import scala.concurrent.ExecutionContext

/**
  * Created by batbold on 6/24/16.
  */
object implicits {

  import helpers.utiltry.implicits._

  implicit class DBIOInstance[+T](dbio: DBIO[T]) {
    def asXor(implicit ec:ExecutionContext) = dbio.asTry.map(_.toXor)
  }

}
