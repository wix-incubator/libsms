package com.wix.sms.nexmo.it

import com.google.api.client.http.javanet.NetHttpTransport
import com.wix.sms.SmsErrorException
import com.wix.sms.model.{Sender, SmsGateway}
import com.wix.sms.nexmo.model.Statuses
import com.wix.sms.nexmo.testkit.NexmoDriver
import com.wix.sms.nexmo.{Credentials, NexmoSmsGateway}
import org.specs2.mutable.SpecWithJUnit
import org.specs2.specification.Scope

class NexmoSmsGatewayIT extends SpecWithJUnit {
  private val nexmoPort = 10011

  val driver = new NexmoDriver(port = nexmoPort)
  step {
    driver.startProbe()
  }

  sequential

  trait Ctx extends Scope {
    val requestFactory = new NetHttpTransport().createRequestFactory()

    val someCredentials = Credentials(
      apiKey = "someApiKey",
      apiSecret = "someApiSecret"
    )
    val someDestPhone = "+12125551234"
    val someSender = Sender(
      phone = Some("+12125554321")
    )
    val someText = "some text"
    val someMessageId = "someMessageId"

    val nexmo: SmsGateway = new NexmoSmsGateway(
      requestFactory = requestFactory,
      endpoint = s"http://localhost:$nexmoPort/",
      credentials = someCredentials
    )

    driver.resetProbe()
  }

  "getId" should {
    "return the ID" in new Ctx {
      nexmo.getId must beEqualTo(NexmoSmsGateway.id)
    }
  }

  "sendPlain" should {
    "successfully yield a message ID on valid request" in new Ctx {
      driver.anSmsFor(
        credentials = someCredentials,
        sender = someSender,
        destPhone = someDestPhone,
        text = someText
      ) returns(
          msgId = someMessageId
      )

      nexmo.sendPlain(
        sender = someSender,
        destPhone = someDestPhone,
        text = someText
      ) must beASuccessfulTry(
        check = ===(someMessageId)
      )
    }

    "gracefully fail on invalid credentials" in new Ctx {
      driver.anSmsFor(
        credentials = someCredentials,
        sender = someSender,
        destPhone = someDestPhone,
        text = someText
      ) isUnauthorized()

      nexmo.sendPlain(
        sender = someSender,
        destPhone = someDestPhone,
        text = someText
      ) must beAFailedTry.like {
        case e: SmsErrorException => e.message must contain(Statuses.invalidCredentials)
      }
    }
  }

  step {
    driver.stopProbe()
  }
}
