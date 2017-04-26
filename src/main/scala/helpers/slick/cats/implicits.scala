package helpers.slick.cats

import slick.dbio.Effect.All
import slick.dbio.{DBIO, DBIOAction, NoStream}

import scala.concurrent.ExecutionContext

/**
  * Created by batbold on 6/24/16.
  */
object implicits {

//  import helpers.utiltry.implicits._

  implicit class DBIOInstance[+T](dbio: DBIO[T]) {
    def asEither(implicit ec: ExecutionContext): DBIOAction[Either[Throwable, T], NoStream, All] = dbio.asTry.map(_.toEither)
  }

}
