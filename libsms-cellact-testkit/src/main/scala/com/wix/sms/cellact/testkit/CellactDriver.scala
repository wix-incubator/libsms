package com.wix.sms.cellact.testkit

import java.util.{List => JList}

import com.google.api.client.http.UrlEncodedParser
import com.wix.hoopoe.http.testkit.EmbeddedHttpProbe
import com.wix.sms.cellact.model._
import com.wix.sms.cellact.{CellactHelper, Credentials}
import spray.http._

import scala.collection.JavaConversions._
import scala.collection.mutable

class CellactDriver(port: Int) {
  private val probe = new EmbeddedHttpProbe(port, EmbeddedHttpProbe.NotFoundHandler)
  private val paloParser = new PaloParser
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

  def aSendPlainFor(credentials: Credentials, source: String, destPhone: String, text: String): SendPlainCtx = {
    new SendPlainCtx(
      credentials = credentials,
      source = source,
      destPhone = destPhone,
      text = text)
  }

  class SendPlainCtx(credentials: Credentials, source: String, destPhone: String, text: String) {
    private val expectedPalo = CellactHelper.createPalo(
      credentials = credentials,
      sender = source,
      destPhone = destPhone,
      text = text
    )

    def returns(msgId: String) = {
      val response = new Response
      response.RESULTCODE = ResultCodes.success
      response.RESULTMESSAGE = "Success"
      response.BLMJ = msgId

      val responseXml = responseParser.stringify(response)
      returnsXml(responseXml)
    }

    def failsWith(code: String, message: String) = {
      val response = new Response
      response.RESULTCODE = code
      response.RESULTMESSAGE = message

      val responseXml = responseParser.stringify(response)
      returnsXml(responseXml)
    }

    private def returnsXml(responseXml: String): Unit = {
      probe.handlers += {
        case HttpRequest(
        HttpMethods.POST,
        Uri.Path("/"),
        _,
        entity,
        _) if isStubbedRequestEntity(entity) =>
          HttpResponse(
            status = StatusCodes.OK,
            entity = HttpEntity(ContentType(MediaTypes.`text/xml`, HttpCharsets.`UTF-8`), responseXml))
      }
    }

    private def isStubbedRequestEntity(entity: HttpEntity): Boolean = {
      val requestParams = urlDecode(entity.asString)

      val requestXml = requestParams(Fields.xmlString)
      val palo = paloParser.parse(requestXml)

      palo == expectedPalo
    }

    private def urlDecode(str: String): Map[String, String] = {
      val params = mutable.LinkedHashMap[String, JList[String]]()
      UrlEncodedParser.parse(str, mutableMapAsJavaMap(params))
      params.mapValues( _(0) ).toMap
    }
  }
}
