package helpers.slick.cats

import cats.data.Xor
import slick.dbio.Effect.All
import slick.dbio.{DBIO, DBIOAction, NoStream}

import scala.concurrent.ExecutionContext

/**
  * Created by batbold on 6/24/16.
  */
object implicits {

  import helpers.utiltry.implicits._

  implicit class DBIOInstance[+T](dbio: DBIO[T]) {
    def asXor(implicit ec: ExecutionContext): DBIOAction[Xor[Throwable, T], NoStream, All] = dbio.asTry.map(_.toXor)
  }

}
