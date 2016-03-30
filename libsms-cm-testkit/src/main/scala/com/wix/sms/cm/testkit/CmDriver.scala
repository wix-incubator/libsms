package com.wix.sms.cm.testkit

import com.wix.hoopoe.http.testkit.EmbeddedHttpProbe
import com.wix.sms.cm.model.MessagesParser
import com.wix.sms.cm.{CmHelper, Credentials}
import com.wix.sms.model.Sender
import spray.http._

class CmDriver(port: Int) {
  private val probe = new EmbeddedHttpProbe(port, EmbeddedHttpProbe.NotFoundHandler)
  private val messagesParser = new MessagesParser

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
    private val expectedMessages = CmHelper.createMessages(
      credentials = credentials,
      sender = sender,
      destPhone = destPhone,
      text = text
    )

    def succeeds() = {
      returnsText("")
    }

    def isUnauthorized(): Unit = {
      failsWith("Error: ERROR Invalid product token.")
    }

    def failsWith(error: String): Unit = {
      returnsText(error)
    }

    private def returnsText(responseText: String): Unit = {
      probe.handlers += {
        case HttpRequest(
        HttpMethods.POST,
        Uri.Path("/"),
        _,
        entity,
        _) if isStubbedRequestEntity(entity) =>
          HttpResponse(
            status = StatusCodes.OK,
            entity = HttpEntity(MediaTypes.`text/plain`, responseText))
      }
    }

    private def isStubbedRequestEntity(entity: HttpEntity): Boolean = {
      val requestXml = entity.asString
      val messages = messagesParser.parse(requestXml)

      messages == expectedMessages
    }
  }
}
