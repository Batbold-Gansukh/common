package helpers

import java.text.SimpleDateFormat
import java.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

import play.api.libs.json._

/**
  * Created by batbold on 11/1/15.
  */
object common {

  def test = "From common.Utils"

  def parseInt(string: String): Throwable Either Int = {
    try {
      Right(string.toInt)
    } catch {
      case e: Exception ⇒
        Left(e.getCause)
    }
  }

  def parseLong(string: String): Throwable Either Long = {
    try {
      Right(string.toLong)
    } catch {
      case e: Exception ⇒
        Left(e.getCause)
    }
  }

  def parseFloat(string: String): Throwable Either Float = {
    try {
      Right(string.toFloat)
    } catch {
      case e: Exception ⇒
        Left(e.getCause)
    }
  }

  def decodeUrlToJson(string: String): Throwable Either JsValue = {
    try {
      Right(Json.parse(Base64.getUrlDecoder.decode(string.getBytes("UTF-8"))))
    } catch {
      case e: Exception ⇒
        Left(e.getCause)
    }
  }

  def generateDataSign(dataRaw: String, appSecret: String, appId: String): String = {
    val key = new SecretKeySpec(appSecret.getBytes("UTF-8"), "HmacSHA256")
    val mac = Mac.getInstance("HmacSHA256")
    mac.init(key)
    val hmacData = mac.doFinal(dataRaw.getBytes("UTF-8"))
    new String(Base64.getUrlEncoder.encode(hmacData), "UTF-8")
  }

  val unixDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")

  def parseUnixDate(string: String): Throwable Either java.sql.Timestamp = try {
    Right(new java.sql.Timestamp(unixDateFormat.parse(string).getTime))
  } catch {
    case e: Throwable ⇒
      Left(e.getCause)
  }

  def pp(toString: Any, color: String = Console.MAGENTA): Unit = {
    println(color + toString.toString + Console.RESET)
  }

  def ppEach(toStrings: (Any, String)*): Unit = {
    toStrings.foreach {
      case (toString, color) ⇒ print(color + toString.toString + Console.RESET)
    }
    println(Console.RESET)
  }

  def ppAll(toStrings: Any*)(color: String): Unit = {
    toStrings.foreach { toString ⇒
      print(color + toString.toString)
    }
    println(Console.RESET)
  }

}
