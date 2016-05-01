package com.wix.sms.clickatell

import java.io.IOException

import com.google.api.client.http._
import com.wix.sms.clickatell.model.{Headers, MessageRequestParser, MessageResponseParser, Versions}
import com.wix.sms.model.{Sender, SmsGateway}
import com.wix.sms.{CommunicationException, SmsErrorException, SmsException}

import scala.concurrent.duration.Duration
import scala.util.{Failure, Success, Try}

object Endpoints {
  val production = "https://api.clickatell.com/rest/"
}

class ClickatellSmsGateway(requestFactory: HttpRequestFactory,
                           endpoint: String = Endpoints.production,
                           connectTimeout: Option[Duration] = None,
                           readTimeout: Option[Duration] = None,
                           numberOfRetries: Int = 0,
                           credentials: Credentials) extends SmsGateway {
  private val requestParser = new MessageRequestParser
  private val responseParser = new MessageResponseParser

  override def getId: String = ClickatellSmsGateway.id

  override def sendPlain(sender: Sender, destPhone: String, text: String): Try[String] = {
    Try {
      val request = ClickatellHelper.createMessageRequest(
        sender = sender,
        destPhone = destPhone,
        text = text
      )

      val requestJson = requestParser.stringify(request)

      val httpRequest = requestFactory.buildPostRequest(
        new GenericUrl(s"${endpoint}message"),
        new ByteArrayContent("application/json", requestJson.getBytes("UTF-8")))

      connectTimeout.foreach { duration => httpRequest.setConnectTimeout(duration.toMillis.toInt) }
      readTimeout.foreach { duration => httpRequest.setReadTimeout(duration.toMillis.toInt) }
      httpRequest.setNumberOfRetries(numberOfRetries)

      httpRequest.getHeaders.setAuthorization(s"Bearer ${credentials.accessToken}")
      httpRequest.getHeaders.set(Headers.version, Versions.`1`)

      httpRequest.setThrowExceptionOnExecuteError(false)
      val httpResponse = httpRequest.execute()
      val responseJson = extractAndCloseResponse(httpResponse)
      val response = responseParser.parse(responseJson)

      (response.error, response.data) match {
        case (None, Some(data)) => data.message.head.apiMessageId
        case (None, None) => throw new SmsErrorException(s"http status = ${httpResponse.getStatusCode}")
        case (Some(error), _) => throw new SmsErrorException(s"http status = ${httpResponse.getStatusCode}, code = ${error.code}, description = ${error.description}")
      }
    } match {
      case Success(apiMessageId) => Success(apiMessageId)
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

object ClickatellSmsGateway {
  val id = "com.clickatell"
}