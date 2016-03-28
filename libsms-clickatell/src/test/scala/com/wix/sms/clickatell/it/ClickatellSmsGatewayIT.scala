package com.wix.sms.clickatell.it

import com.google.api.client.http.javanet.NetHttpTransport
import com.wix.sms.SmsErrorException
import com.wix.sms.clickatell.testkit.ClickatellDriver
import com.wix.sms.clickatell.{ClickatellSmsGateway, Credentials}
import com.wix.sms.model.{Sender, SmsGateway}
import com.wix.sms.testkit.TwitterTryMatchers._
import org.specs2.mutable.SpecWithJUnit
import org.specs2.specification.Scope

class ClickatellSmsGatewayIT extends SpecWithJUnit {
  private val clickatellPort = 10005

  val driver = new ClickatellDriver(port = clickatellPort)
  step {
    driver.startProbe()
  }

  sequential

  trait Ctx extends Scope {
    val requestFactory = new NetHttpTransport().createRequestFactory()

    val someCredentials = Credentials(
      accessToken = "someAccessToken"
    )
    val someDestPhone = "+12125551234"
    val someSender = Sender(
      phone = Some("+12125554321")
    )
    val someText = "some text"
    val someMessageId = "someMessageId"

    val clickatell: SmsGateway = new ClickatellSmsGateway(
      requestFactory = requestFactory,
      endpoint = s"http://localhost:$clickatellPort/",
      credentials = someCredentials
    )

    driver.resetProbe()
  }

  "getId" should {
    "return the ID" in new Ctx {
      clickatell.getId must beEqualTo(ClickatellSmsGateway.id)
    }
  }

  "sendPlain" should {
    "successfully yield a message ID on valid request" in new Ctx {
      driver.aMessageFor(
        credentials = someCredentials,
        sender = someSender,
        destPhone = someDestPhone,
        text = someText
      ) returns(
          msgId = someMessageId
      )

      clickatell.sendPlain(
        sender = someSender,
        destPhone = someDestPhone,
        text = someText
      ) must beSuccessful(
        value = ===(someMessageId)
      )
    }

    "gracefully fail on error" in new Ctx {
      val someErrorCode = "some code"
      val someErrorDescription = "some description"

      driver.aMessageFor(
        credentials = someCredentials,
        sender = someSender,
        destPhone = someDestPhone,
        text = someText
      ) failsWith(
        code = someErrorCode,
        description = someErrorDescription
      )

      clickatell.sendPlain(
        sender = someSender,
        destPhone = someDestPhone,
        text = someText
      ) must beFailure[String, SmsErrorException](
        msg = contain(someErrorCode) and contain(someErrorDescription)
      )
    }

    "gracefully fail on invalid credentials" in new Ctx {
      driver.aMessageFor(
        credentials = someCredentials,
        sender = someSender,
        destPhone = someDestPhone,
        text = someText
      ) isUnauthorized()

      clickatell.sendPlain(
        sender = someSender,
        destPhone = someDestPhone,
        text = someText
      ) must beFailure[String, SmsErrorException](
        msg = contain("401") // "Unauthorized" HTTP status code
      )
    }
  }

  step {
    driver.stopProbe()
  }
}
