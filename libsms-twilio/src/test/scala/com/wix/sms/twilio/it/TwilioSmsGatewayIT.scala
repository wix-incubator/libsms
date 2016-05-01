package com.wix.sms.twilio.it

import com.google.api.client.http.javanet.NetHttpTransport
import com.wix.sms.SmsErrorException
import com.wix.sms.model.{Sender, SmsGateway}
import com.wix.sms.twilio.testkit.TwilioDriver
import com.wix.sms.twilio.{Credentials, TwilioSmsGateway}
import org.specs2.mutable.SpecWithJUnit
import org.specs2.specification.Scope

class TwilioSmsGatewayIT extends SpecWithJUnit {
  private val twilioPort = 10008

  val driver = new TwilioDriver(port = twilioPort)
  step {
    driver.startProbe()
  }

  sequential

  trait Ctx extends Scope {
    val requestFactory = new NetHttpTransport().createRequestFactory()

    val someCredentials = Credentials(
      accountSid = "someAccountSid",
      authToken = "someAuthToken"
    )
    val someDestPhone = "+12125551234"
    val someSender = Sender(
      phone = Some("+12125554321")
    )
    val someText = "some text"
    val someMessageId = "someMessageId"

    val twilio: SmsGateway = new TwilioSmsGateway(
      requestFactory = requestFactory,
      endpoint = s"http://localhost:$twilioPort/",
      credentials = someCredentials
    )

    driver.resetProbe()
  }

  "getId" should {
    "return the ID" in new Ctx {
      twilio.getId must beEqualTo(TwilioSmsGateway.id)
    }
  }

  "sendPlain" should {
    "successfully yield a message ID on valid request" in new Ctx {
      driver.aSendMessageFor(
        credentials = someCredentials,
        sender = someSender,
        destPhone = someDestPhone,
        text = someText
      ) returns(
          msgId = someMessageId
      )

      twilio.sendPlain(
        sender = someSender,
        destPhone = someDestPhone,
        text = someText
      ) must beASuccessfulTry(
        check = ===(someMessageId)
      )
    }

    "gracefully fail on error" in new Ctx {
      val someCode = "some code"
      val someMessage = "some message"

      driver.aSendMessageFor(
        credentials = someCredentials,
        sender = someSender,
        destPhone = someDestPhone,
        text = someText
      ) failsWith(
        code = someCode,
        message = someMessage
      )

      twilio.sendPlain(
        sender = someSender,
        destPhone = someDestPhone,
        text = someText
      ) must beAFailedTry.like {
        case e: SmsErrorException => e.message must (contain(someCode) and contain(someMessage))
      }
    }
  }

  step {
    driver.stopProbe()
  }
}
