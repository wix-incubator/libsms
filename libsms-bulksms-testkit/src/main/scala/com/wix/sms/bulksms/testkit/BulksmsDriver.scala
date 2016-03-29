package com.wix.sms.bulksms.testkit

import java.util.{List => JList}

import com.google.api.client.http.UrlEncodedParser
import com.wix.hoopoe.http.testkit.EmbeddedHttpProbe
import com.wix.sms.bulksms.model.{StatusCodes, _}
import com.wix.sms.bulksms.{BulksmsHelper, Credentials}
import com.wix.sms.model.Sender
import spray.http.{StatusCodes => HttpStatusCodes, _}

import scala.collection.JavaConversions._
import scala.collection.mutable

class BulksmsDriver(port: Int) {
  private val probe = new EmbeddedHttpProbe(port, EmbeddedHttpProbe.NotFoundHandler)
  private val responseParser = new ResponseParser

  def startProbe() {
    probe.doStart()
  }

  def stopProbe() {
    probe.doStop()
  }

  def resetProbe() {
    probe.handlers.clear()
  }

  def aSendFor(credentials: Credentials, sender: Sender, destPhone: String, text: String, routingGroup: String): SendCtx = {
    new SendCtx(
      credentials = credentials,
      sender = sender,
      destPhone = destPhone,
      text = text,
      routingGroup = routingGroup)
  }

  class SendCtx(credentials: Credentials, sender: Sender, destPhone: String, text: String, routingGroup: String) {
    private val expectedParams = BulksmsHelper.createRequestParams(
      sender = sender,
      destPhone = destPhone,
      text = text,
      credentials = credentials,
      routingGroup = routingGroup
    )

    def returns(msgId: String): Unit = {
      val response = new Response(
        code = StatusCodes.inProgress,
        description = "IN_PROGRESS",
        batchId = Some(msgId)
      )

      val responseText = responseParser.stringify(response)
      returnText(responseText)
    }

    def failsWith(code: String, description: String): Unit = {
      val response = new Response(
        code = code,
        description = description
      )

      val responseText = responseParser.stringify(response)
      returnText(responseText)
    }

    private def returnText(responseText: String): Unit = {
      probe.handlers += {
        case HttpRequest(
        HttpMethods.POST,
        Uri.Path("/submission/send_sms/2/2.0"),
        headers,
        entity,
        _) if isStubbedRequestEntity(entity) => HttpResponse(
          status = HttpStatusCodes.OK,
          entity = HttpEntity(ContentTypes.`text/plain`, responseText)
        )
      }
    }

    private def isStubbedRequestEntity(entity: HttpEntity): Boolean = {
      val requestParams = urlDecode(entity.asString)

      requestParams == expectedParams
    }

    private def urlDecode(str: String): Map[String, String] = {
      val params = mutable.LinkedHashMap[String, JList[String]]()
      UrlEncodedParser.parse(str, mutableMapAsJavaMap(params))
      params.mapValues( _.head ).toMap
    }
  }
}
