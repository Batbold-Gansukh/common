package helpers.logging

import org.slf4j.LoggerFactory

/**
  * Created by batbold on 9/24/16.
  */
trait Logger {

  protected lazy val logger: com.typesafe.scalalogging.Logger =
    com.typesafe.scalalogging.Logger(LoggerFactory.getLogger(getClass))

}
