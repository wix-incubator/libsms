package com.wix.sms.bulksms

import java.io.IOException

import com.google.api.client.http._
import com.wix.sms.bulksms.model.{ResponseParser, RoutingGroups, StatusCodes}
import com.wix.sms.model.{Sender, SmsGateway}
import com.wix.sms.{CommunicationException, SmsErrorException, SmsException}

import scala.collection.JavaConversions._
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success, Try}

object Endpoints {
  val production = "https://bulksms.vsms.net/eapi/"
}

class BulksmsSmsGateway(requestFactory: HttpRequestFactory,
                        endpoint: String = Endpoints.production,
                        connectTimeout: Option[Duration] = None,
                        readTimeout: Option[Duration] = None,
                        numberOfRetries: Int = 0,
                        credentials: Credentials,
                        routingGroup: String = RoutingGroups.standard) extends SmsGateway {
  private val responseParser = new ResponseParser

  override def getId: String = BulksmsSmsGateway.id

  override def sendPlain(sender: Sender, destPhone: String, text: String): Try[String] = {
    Try {
      // See http://developer.bulksms.com/eapi/submission/send_sms/
      val params = BulksmsHelper.createRequestParams(
        credentials = credentials,
        sender = sender,
        destPhone = destPhone,
        text = text,
        routingGroup = routingGroup
      )

      val httpRequest = requestFactory.buildPostRequest(
        new GenericUrl(s"${endpoint}submission/send_sms/2/2.0"),
        new UrlEncodedContent(mapAsJavaMap(params)))

      connectTimeout.foreach { duration => httpRequest.setConnectTimeout(duration.toMillis.toInt) }
      readTimeout.foreach { duration => httpRequest.setReadTimeout(duration.toMillis.toInt) }
      httpRequest.setNumberOfRetries(numberOfRetries)

      val httpResponse = httpRequest.execute()
      val responseText = extractAndCloseResponse(httpResponse)

      val response = responseParser.parse(responseText)

      (response.code, response.batchId) match {
        case (StatusCodes.inProgress, Some(batchId)) => batchId
        case (code, _) => throw new SmsErrorException(s"code = $code, description = ${response.description}")
      }
    } match {
      case Success(batchId) => Success(batchId)
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

object BulksmsSmsGateway {
  val id = "com.bulksms"
}