package com.wix.sms.twilio

import java.io.IOException

import com.google.api.client.http._
import com.google.api.client.util.Base64
import com.wix.sms.model.{Sender, SmsGateway}
import com.wix.sms.twilio.model.SmsResponseParser
import com.wix.sms.{CommunicationException, SmsErrorException, SmsException}

import scala.collection.JavaConversions._
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success, Try}

object Endpoints {
  val production = "https://api.twilio.com/2010-04-01/"
}

class TwilioSmsGateway(requestFactory: HttpRequestFactory,
                       endpoint: String = Endpoints.production,
                       connectTimeout: Option[Duration] = None,
                       readTimeout: Option[Duration] = None,
                       numberOfRetries: Int = 0,
                       credentials: Credentials) extends SmsGateway {
  private val responseParser = new SmsResponseParser

  override def getId: String = TwilioSmsGateway.id

  private def createBasicAuthorization(user: String, password: String): String = {
    s"Basic ${Base64.encodeBase64String(s"$user:$password".getBytes("UTF-8"))}"
  }

  override def sendPlain(sender: Sender, destPhone: String, text: String): Try[String] = {
    Try {
      val params = TwilioHelper.createRequestParams(
        sender = sender,
        destPhone = destPhone,
        text = text
      )

      val httpRequest = requestFactory.buildPostRequest(
        new GenericUrl(s"${endpoint}Accounts/${credentials.accountSid}/Messages.json"),
        new UrlEncodedContent(mapAsJavaMap(params)))

      connectTimeout.foreach { duration => httpRequest.setConnectTimeout(duration.toMillis.toInt) }
      readTimeout.foreach { duration => httpRequest.setReadTimeout(duration.toMillis.toInt) }
      httpRequest.setNumberOfRetries(numberOfRetries)

      httpRequest.getHeaders.setAuthorization(createBasicAuthorization(
        user = credentials.accountSid,
        password = credentials.authToken
      ))

      val httpResponse = httpRequest.execute()
      val responseJson = extractAndCloseResponse(httpResponse)
      val response = responseParser.parse(responseJson)

      response.sid match {
        case Some(sid) => sid
        case None => throw new SmsErrorException(s"code = ${response.code.orNull}, message = ${response.message.orNull}")
      }
    } match {
      case Success(sid) => Success(sid)
      case Failure(e: SmsException) => Failure(e)
      case Failure(e: IOException) => Failure(new CommunicationException(e.getMessage, e))
      case Failure(e) => Failure(new SmsErrorException(e.getMessage, e))
    }
  }

  private def extractAndCloseResponse(httpResponse: HttpResponse): String = {
    try {
      httpResponse.parseAsString()
    } finally {
      httpResponse.ignore()
    }
  }
}

object TwilioSmsGateway {
  val id = "com.twilio"
}