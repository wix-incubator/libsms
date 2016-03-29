package com.wix.sms.nexmo.testkit

import java.util.{List => JList}

import com.google.api.client.http.UrlEncodedParser
import com.wix.hoopoe.http.testkit.EmbeddedHttpProbe
import com.wix.sms.model.Sender
import com.wix.sms.nexmo.model.{Message, Response, ResponseParser, Statuses}
import com.wix.sms.nexmo.{Credentials, NexmoHelper}
import spray.http._

import scala.collection.JavaConversions._
import scala.collection.mutable

class NexmoDriver(port: Int) {
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

  def anSmsFor(credentials: Credentials, sender: Sender, destPhone: String, text: String): SmsCtx = {
    new SmsCtx(
      credentials = credentials,
      sender = sender,
      destPhone = destPhone,
      text = text)
  }

  class SmsCtx(credentials: Credentials, sender: Sender, destPhone: String, text: String) {
    private val expectedParams = NexmoHelper.createRequestParams(
      sender = sender,
      destPhone = destPhone,
      text = text,
      credentials = credentials
    )

    def returns(msgId: String): Unit = {
      val response = Response(
        `message-count` = "1",
        messages = Seq(Message(
          status = Statuses.success,
          to = Some(destPhone.substring(1)),
          `message-id` = Some(msgId),
          `remaining-balance` = Some("9.07060000"),
          `message-price` = Some("0.01040000"),
          network = Some("42501")
        ))
      )

      val responseJson = responseParser.stringify(response)
      returnJson(responseJson)
    }

    def failsWith(status: String, errorText: String): Unit = {
      val response = new Response(
        `message-count` = "1",
        messages = Seq(Message(
          status = status,
          `error-text` = Some(errorText)
        ))
      )

      val responseJson = responseParser.stringify(response)
      returnJson(responseJson)
    }

    def isUnauthorized(): Unit = {
      failsWith(
        status = Statuses.invalidCredentials,
        errorText = "Bad Credentials"
      )
    }

    private def returnJson(responseJson: String): Unit = {
      probe.handlers += {
        case HttpRequest(
        HttpMethods.POST,
        Uri.Path("/sms/json"),
        _,
        entity,
        _) if isStubbedRequestEntity(entity) => HttpResponse(
          status = StatusCodes.BadRequest,
          entity = HttpEntity(ContentTypes.`application/json`, responseJson)
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
