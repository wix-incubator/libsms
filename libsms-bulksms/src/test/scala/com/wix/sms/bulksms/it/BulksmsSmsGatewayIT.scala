package com.wix.sms.bulksms.it

import com.google.api.client.http.javanet.NetHttpTransport
import com.wix.sms.SmsErrorException
import com.wix.sms.bulksms.model.{RoutingGroups, StatusCodes}
import com.wix.sms.bulksms.testkit.BulksmsDriver
import com.wix.sms.bulksms.{BulksmsSmsGateway, Credentials}
import com.wix.sms.model.{Sender, SmsGateway}
import org.specs2.mutable.SpecWithJUnit
import org.specs2.specification.Scope

class BulksmsSmsGatewayIT extends SpecWithJUnit {
  private val bulksmsPort = 10004

  val driver = new BulksmsDriver(port = bulksmsPort)
  step {
    driver.startProbe()
  }

  sequential

  trait Ctx extends Scope {
    val requestFactory = new NetHttpTransport().createRequestFactory()

    val someRoutingGroup = RoutingGroups.standard
    val someCredentials = Credentials(
      username = "some username",
      password = "some password"
    )
    val someDestPhone = "+12125551234"
    val someSender = Sender(
      phone = Some("+12125554321")
    )
    val someText = "some text"
    val someMessageId = "someMessageId"

    val bulksms: SmsGateway = new BulksmsSmsGateway(
      requestFactory = requestFactory,
      endpoint = s"http://localhost:$bulksmsPort/",
      credentials = someCredentials,
      routingGroup = someRoutingGroup
    )

    driver.resetProbe()
  }

  "getId" should {
    "return the ID" in new Ctx {
      bulksms.getId must beEqualTo(BulksmsSmsGateway.id)
    }
  }

  "sendPlain" should {
    "successfully yield a message ID on valid request" in new Ctx {
      driver.aSendFor(
        credentials = someCredentials,
        sender = someSender,
        destPhone = someDestPhone,
        text = someText,
        routingGroup = someRoutingGroup
      ) returns(
          msgId = someMessageId
      )

      bulksms.sendPlain(
        sender = someSender,
        destPhone = someDestPhone,
        text = someText
      ) must beASuccessfulTry(
        check = ===(someMessageId)
      )
    }

    "gracefully fail on insufficient credit" in new Ctx {
      val insufficientCreditsDescription = "FAILED_USERCREDITS"
      driver.aSendFor(
        credentials = someCredentials,
        sender = someSender,
        destPhone = someDestPhone,
        text = someText,
        routingGroup = someRoutingGroup
      ) failsWith (
        code = StatusCodes.insufficientCredits,
        description = insufficientCreditsDescription
      )

      bulksms.sendPlain(
        sender = someSender,
        destPhone = someDestPhone,
        text = someText
      ) must beAFailedTry.like {
        case e: SmsErrorException => e.message must (contain(StatusCodes.insufficientCredits) and contain(insufficientCreditsDescription))
      }
    }
  }

  step {
    driver.stopProbe()
  }
}
