package com.wix.sms.cm.it

import com.google.api.client.http.javanet.NetHttpTransport
import com.wix.sms.SmsErrorException
import com.wix.sms.cm.testkit.CmDriver
import com.wix.sms.cm.{CmSmsGateway, Credentials}
import com.wix.sms.model.{Sender, SmsGateway}
import org.specs2.mutable.SpecWithJUnit
import org.specs2.specification.Scope

class CmSmsGatewayIT extends SpecWithJUnit {
  private val cmPort = 10017

  val driver = new CmDriver(port = cmPort)
  step {
    driver.startProbe()
  }

  sequential

  trait Ctx extends Scope {
    val requestFactory = new NetHttpTransport().createRequestFactory()

    val someCredentials = Credentials(
      productToken = "some product token"
    )
    val someDestPhone = "+12125551234"
    val someSender = Sender(
      name = Some("Example")
    )
    val someText = "some text"

    val cm: SmsGateway = new CmSmsGateway(
      requestFactory = requestFactory,
      endpoint = s"http://localhost:$cmPort/",
      credentials = someCredentials
    )

    driver.resetProbe()
  }

  "getId" should {
    "return the ID" in new Ctx {
      cm.getId must beEqualTo(CmSmsGateway.id)
    }
  }

  "sendPlain" should {
    "successfully yield a message ID on valid request" in new Ctx {
      driver.aMessageFor(
        credentials = someCredentials,
        sender = someSender,
        destPhone = someDestPhone,
        text = someText
      ) succeeds()

      cm.sendPlain(
        sender = someSender,
        destPhone = someDestPhone,
        text = someText
      ) must beASuccessfulTry(
        check = ===("")
      )
    }

    "gracefully fail on invalid credentials" in new Ctx {
      driver.aMessageFor(
        credentials = someCredentials,
        sender = someSender,
        destPhone = someDestPhone,
        text = someText
      ) isUnauthorized()

      cm.sendPlain(
        sender = someSender,
        destPhone = someDestPhone,
        text = someText
      ) must beAFailedTry(
        check = beAnInstanceOf[SmsErrorException]
      )
    }

    "gracefully fail on generic error" in new Ctx {
      val someError = "some error"

      driver.aMessageFor(
        credentials = someCredentials,
        sender = someSender,
        destPhone = someDestPhone,
        text = someText
      ) failsWith(
        error = someError
      )

      cm.sendPlain(
        sender = someSender,
        destPhone = someDestPhone,
        text = someText
      ) must beAFailedTry.like{
        case e: SmsErrorException => e.message must beEqualTo(someError)
      }
    }
  }

  step {
    driver.stopProbe()
  }
}
