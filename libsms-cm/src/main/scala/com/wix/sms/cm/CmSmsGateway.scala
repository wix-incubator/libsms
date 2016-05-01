package com.wix.sms.cm

import java.io.IOException

import com.google.api.client.http._
import com.wix.sms.cm.model.MessagesParser
import com.wix.sms.model.{Sender, SmsGateway}
import com.wix.sms.{CommunicationException, SmsErrorException, SmsException}

import scala.concurrent.duration.Duration
import scala.util.{Failure, Success, Try}

object Endpoints {
  val production = "https://sgw01.cm.nl/gateway.ashx"
}

class CmSmsGateway(requestFactory: HttpRequestFactory,
                   endpoint: String = Endpoints.production,
                   connectTimeout: Option[Duration] = None,
                   readTimeout: Option[Duration] = None,
                   numberOfRetries: Int = 0,
                   credentials: Credentials) extends SmsGateway {
  private val messagesParser = new MessagesParser

  override def getId: String = CmSmsGateway.id

  override def sendPlain(sender: Sender, destPhone: String, text: String): Try[String] = {
    Try {
      val messages = CmHelper.createMessages(
        credentials = credentials,
        sender = sender,
        destPhone = destPhone,
        text = text
      )

      val requestXml = messagesParser.stringify(messages)

      val httpRequest = requestFactory.buildPostRequest(
        new GenericUrl(endpoint),
        new ByteArrayContent("application/xml", requestXml.getBytes("UTF-8")))
      connectTimeout.foreach { duration => httpRequest.setConnectTimeout(duration.toMillis.toInt) }
      readTimeout.foreach { duration => httpRequest.setReadTimeout(duration.toMillis.toInt) }
      httpRequest.setNumberOfRetries(numberOfRetries)

      val httpResponse = httpRequest.execute()
      val responseText = extractAndCloseResponse(httpResponse)

      if (responseText.isEmpty) {
        // CM returns empty response on success (there is no concept of message IDs)
        ""
      } else {
        throw new SmsErrorException(responseText)
      }
    } match {
      case Success(msgId) => Success(msgId)
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

object CmSmsGateway {
  val id = "com.cmtelecom"
}