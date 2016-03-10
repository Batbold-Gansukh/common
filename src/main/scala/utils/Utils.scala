package utils

import java.text.SimpleDateFormat
import java.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

import cats.data.Xor
import play.api.libs.json._

/**
  * Created by batbold on 11/1/15.
  */
object Utils {

  def test = "From common.Utils"

  def parseInt(string: String): Throwable Xor Int = {
    try {
      Xor.right(string.toInt)
    } catch {
      case e: Exception ⇒
        Xor.left(e.getCause)
    }
  }

  def decodeUrlToJson(string: String): Throwable Xor JsValue = {
    try {
      Xor.right(Json.parse(Base64.getUrlDecoder.decode(string.getBytes("UTF-8"))))
    } catch {
      case e: Exception ⇒
        Xor.left(e.getCause)
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

  def parseUnixDate(string: String): Throwable Xor java.sql.Timestamp = try {
    Xor.right(new java.sql.Timestamp(unixDateFormat.parse(string).getTime))
  } catch {
    case e: Throwable ⇒
      Xor.left(e.getCause)
  }

}
