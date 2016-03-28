package com.wix.sms.clickatell.testkit

import com.wix.hoopoe.http.testkit.EmbeddedHttpProbe
import com.wix.sms.clickatell.model._
import com.wix.sms.clickatell.{ClickatellHelper, Credentials}
import com.wix.sms.model.Sender
import spray.http._

class ClickatellDriver(port: Int) {
  private val probe = new EmbeddedHttpProbe(port, EmbeddedHttpProbe.NotFoundHandler)
  private val requestParser = new MessageRequestParser
  private val responseParser = new MessageResponseParser

  def startProbe() {
    probe.doStart()
  }

  def stopProbe() {
    probe.doStop()
  }

  def resetProbe() {
    probe.handlers.clear()
  }

  def aMessageFor(credentials: Credentials, sender: Sender, destPhone: String, text: String): MessageCtx = {
    new MessageCtx(
      credentials = credentials,
      sender = sender,
      destPhone = destPhone,
      text = text)
  }

  class MessageCtx(credentials: Credentials, sender: Sender, destPhone: String, text: String) {
    private val expectedRequest = ClickatellHelper.createMessageRequest(
      sender = sender,
      destPhone = destPhone,
      text = text
    )

    def returns(msgId: String): Unit = {
      val response = new MessageResponse(
        data = Some(Data(
          message = Seq(Message(
            accepted = true,
            to = destPhone,
            apiMessageId = msgId
          ))
        ))
      )

      val responseJson = responseParser.stringify(response)
      respondWith(HttpResponse(
        status = StatusCodes.OK,
        entity = HttpEntity(ContentTypes.`application/json`, responseJson))
      )
    }

    def failsWith(code: String, description: String): Unit = {
      val response = new MessageResponse(
        error = Some(Error(
          code = code,
          description = description,
          documentation = s"http://www.clickatell.com/help/apidocs/error/$code.htm"
        ))
      )

      val responseJson = responseParser.stringify(response)
      respondWith(HttpResponse(
        status = StatusCodes.BadRequest,
        entity = HttpEntity(ContentTypes.`application/json`, responseJson))
      )
    }

    def isUnauthorized(): Unit = {
      respondWith(HttpResponse(
        status = StatusCodes.Unauthorized,
        entity = HttpEntity.Empty)
      )
    }

    private def respondWith(httpResponse: HttpResponse): Unit = {
      probe.handlers += {
        case HttpRequest(
        HttpMethods.POST,
        Uri.Path("/message"),
        headers,
        entity,
        _) if isStubbedRequestEntity(entity) && isStubbedHeaders(headers) => httpResponse
      }
    }

    private def isStubbedRequestEntity(entity: HttpEntity): Boolean = {
      val requestJson = entity.asString
      val request = requestParser.parse(requestJson)

      request == expectedRequest
    }

    private def isStubbedHeaders(headers: Seq[HttpHeader]): Boolean = {
      isAuthorized(headers) && isCorrectVersion(headers)
    }

    private def isAuthorized(headers: Seq[HttpHeader]): Boolean = {
      val expectedAuthorizationValue = s"Bearer ${credentials.accessToken}"

      headers.exists { header =>
        header.name.equalsIgnoreCase(HttpHeaders.Authorization.name) && header.value == expectedAuthorizationValue
      }
    }

    private def isCorrectVersion(headers: Seq[HttpHeader]): Boolean = {
      headers.exists { header =>
        header.name.equalsIgnoreCase(Headers.version) && header.value == Versions.`1`
      }
    }
  }
}
