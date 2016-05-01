package com.wix.sms.nexmo

import java.io.IOException

import com.google.api.client.http._
import com.wix.sms.model.{Sender, SmsGateway}
import com.wix.sms.nexmo.model.{ResponseParser, Statuses}
import com.wix.sms.{CommunicationException, SmsErrorException, SmsException}

import scala.collection.JavaConversions._
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success, Try}

object Endpoints {
  val production = "https://rest.nexmo.com/"
}

class NexmoSmsGateway(requestFactory: HttpRequestFactory,
                           endpoint: String = Endpoints.production,
                           connectTimeout: Option[Duration] = None,
                           readTimeout: Option[Duration] = None,
                           numberOfRetries: Int = 0,
                           credentials: Credentials) extends SmsGateway {
  private val responseParser = new ResponseParser

  override def getId: String = NexmoSmsGateway.id

  override def sendPlain(sender: Sender, destPhone: String, text: String): Try[String] = {
    Try {
      val params = NexmoHelper.createRequestParams(
        sender = sender,
        destPhone = destPhone,
        text = text,
        credentials = credentials
      )

      val httpRequest = requestFactory.buildPostRequest(
        new GenericUrl(s"${endpoint}sms/json"),
        new UrlEncodedContent(mapAsJavaMap(params)))

      connectTimeout.foreach { duration => httpRequest.setConnectTimeout(duration.toMillis.toInt) }
      readTimeout.foreach { duration => httpRequest.setReadTimeout(duration.toMillis.toInt) }
      httpRequest.setNumberOfRetries(numberOfRetries)

      httpRequest.setThrowExceptionOnExecuteError(false)
      val httpResponse = httpRequest.execute()
      val responseJson = extractAndCloseResponse(httpResponse)
      val response = responseParser.parse(responseJson)

      val message = response.messages.head
      (message.status, message.`error-text`) match {
        case (Statuses.success, _) => message.`message-id`.getOrElse("")
        case (errorStatus, Some(errorText)) => throw new SmsErrorException(s"status = $errorStatus, text = $errorText")
        case (errorStatus, None) => throw new SmsErrorException(s"status = $errorStatus")
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

object NexmoSmsGateway {
  val id = "com.nexmo"
}