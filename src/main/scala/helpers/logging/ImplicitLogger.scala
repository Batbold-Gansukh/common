package helpers.logging

import org.slf4j.LoggerFactory

/**
  * Created by batbold on 9/24/16.
  */
trait ImplicitLogger {

  protected implicit lazy val logger: com.typesafe.scalalogging.Logger =
    com.typesafe.scalalogging.Logger(LoggerFactory.getLogger(getClass))

}
