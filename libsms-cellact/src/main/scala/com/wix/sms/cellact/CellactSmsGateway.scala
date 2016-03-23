package com.wix.sms.cellact

import java.io.IOException

import com.google.api.client.http.{GenericUrl, HttpRequestFactory, HttpResponse, UrlEncodedContent}
import com.twitter.util.{Return, Throw, Try}
import com.wix.sms.cellact.model._
import com.wix.sms.model.{Sender, SmsGateway}
import com.wix.sms.{CommunicationException, SmsErrorException, SmsException}

import scala.collection.JavaConversions._
import scala.concurrent.duration.Duration

object Endpoints {
  val production = "https://cellactpro.net/GlobalSms/ExternalClient/GlobalAPI.asp"
}

class CellactSmsGateway(requestFactory: HttpRequestFactory,
                        endpoint: String = Endpoints.production,
                        connectTimeout: Option[Duration] = None,
                        readTimeout: Option[Duration] = None,
                        numberOfRetries: Int = 0,
                        credentials: Credentials) extends SmsGateway {
  private val paloParser = new PaloParser
  private val responseParser = new ResponseParser

  override def getId: String = CellactSmsGateway.id

  override def sendPlain(sender: Sender, destPhone: String, text: String): Try[String] = {
    Try {
      val palo = CellactHelper.createPalo(
        credentials = credentials,
        sender = sender.name.getOrElse(sender.phone.orNull),
        destPhone = destPhone,
        text = text
      )

      val requestXml = paloParser.stringify(palo)
      val params = Map(
        Fields.xmlString -> requestXml
      )

      val httpRequest = requestFactory.buildPostRequest(new GenericUrl(endpoint), new UrlEncodedContent(mapAsJavaMap(params)))
      connectTimeout.foreach { duration => httpRequest.setConnectTimeout(duration.toMillis.toInt) }
      readTimeout.foreach { duration => httpRequest.setReadTimeout(duration.toMillis.toInt) }
      httpRequest.setNumberOfRetries(numberOfRetries)

      val httpResponse = httpRequest.execute()
      val responseXml = extractAndCloseResponse(httpResponse)
      val response = responseParser.parse(responseXml)

      response.RESULTCODE match {
        case ResultCodes.success => response.BLMJ
        case errorCode => throw new SmsErrorException(message = s"code = $errorCode, message = ${response.RESULTMESSAGE}")
      }
    } match {
      case Return(blmj) => Return(blmj)
      case Throw(e: SmsException) => Throw(e)
      case Throw(e: IOException) => Throw(new CommunicationException(e.getMessage, e))
      case Throw(e) => Throw(new SmsErrorException(e.getMessage, e))
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

object CellactSmsGateway {
  val id = "il.co.cellact"
}