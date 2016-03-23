package com.wix.sms.plivo.testkit

import com.google.api.client.util.Base64
import com.wix.hoopoe.http.testkit.EmbeddedHttpProbe
import com.wix.sms.model.Sender
import com.wix.sms.plivo.model.{SendMessageRequestParser, SendMessageResponse, SendMessageResponseParser}
import com.wix.sms.plivo.{Credentials, PlivoHelper}
import spray.http._

class PlivoDriver(port: Int) {
  private val probe = new EmbeddedHttpProbe(port, EmbeddedHttpProbe.NotFoundHandler)
  private val sendMessageRequestParser = new SendMessageRequestParser
  private val sendMessageResponseParser = new SendMessageResponseParser

  def startProbe() {
    probe.doStart()
  }

  def stopProbe() {
    probe.doStop()
  }

  def resetProbe() {
    probe.handlers.clear()
  }

  def aSendMessageFor(credentials: Credentials, sender: Sender, destPhone: String, text: String): SendMessageCtx = {
    new SendMessageCtx(
      credentials = credentials,
      sender = sender,
      destPhone = destPhone,
      text = text)
  }

  class SendMessageCtx(credentials: Credentials, sender: Sender, destPhone: String, text: String) {
    private val expectedRequest = PlivoHelper.createSendMessageRequest(
      sender = sender,
      destPhone = destPhone,
      text = text
    )

    def returns(msgId: String) = {
      val response = new SendMessageResponse(
        api_id = "some api id",
        message_uuid = Some(Seq(msgId))
      )

      val responseJson = sendMessageResponseParser.stringify(response)
      returnsJson(responseJson)
    }

    def failsWith(error: String) = {
      val response = new SendMessageResponse(
        api_id = "some api id",
        error = Some(error)
      )

      val responseJson = sendMessageResponseParser.stringify(response)
      returnsJson(responseJson)
    }

    private def returnsJson(responseJson: String): Unit = {
      val path = s"/Account/${credentials.authId}/Message/"
      probe.handlers += {
        case HttpRequest(
        HttpMethods.POST,
        Uri.Path(`path`),
        headers,
        entity,
        _) if isStubbedRequestEntity(entity) && isStubbedHeaders(headers) =>
          HttpResponse(
            status = StatusCodes.OK,
            entity = HttpEntity(ContentTypes.`application/json`, responseJson))
      }
    }

    private def isStubbedRequestEntity(entity: HttpEntity): Boolean = {
      val requestJson = entity.asString
      val request = sendMessageRequestParser.parse(requestJson)

      request == expectedRequest
    }

    private def isStubbedHeaders(headers: Seq[HttpHeader]): Boolean = {
      val expectedAuthorizationValue = s"Basic ${Base64.encodeBase64String(s"${credentials.authId}:${credentials.authToken}".getBytes("UTF-8"))}"

      headers.exists { header =>
        header.name == HttpHeaders.Authorization.name &&
          header.value == expectedAuthorizationValue
      }
    }
  }
}
