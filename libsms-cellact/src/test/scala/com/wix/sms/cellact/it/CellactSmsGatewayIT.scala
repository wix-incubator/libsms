package com.wix.sms.cellact.it

import com.google.api.client.http.javanet.NetHttpTransport
import com.wix.sms.SmsErrorException
import com.wix.sms.cellact.testkit.CellactDriver
import com.wix.sms.cellact.{CellactSmsGateway, Credentials}
import com.wix.sms.model.{Sender, SmsGateway}
import org.specs2.mutable.SpecWithJUnit
import org.specs2.specification.Scope

import com.wix.sms.testkit.TwitterTryMatchers._

class CellactSmsGatewayIT extends SpecWithJUnit {
  private val cellactPort = 10007

  val driver = new CellactDriver(port = cellactPort)
  step {
    driver.startProbe()
  }

  sequential

  trait Ctx extends Scope {
    val requestFactory = new NetHttpTransport().createRequestFactory()

    val someCredentials = Credentials(
      company = "some company",
      username = "some username",
      password = "some password"
    )
    val someDestPhone = "+12125551234"
    val someSenderPhone = "+12125554321"
    val someText = "some text"
    val someMessageId = "someMessageId"

    val cellact: SmsGateway = new CellactSmsGateway(
      requestFactory = requestFactory,
      endpoint = s"http://localhost:$cellactPort/",
      credentials = someCredentials
    )

    driver.resetProbe()
  }

  "getId" should {
    "return the ID" in new Ctx {
      cellact.getId must beEqualTo(CellactSmsGateway.id)
    }
  }

  "sendPlain" should {
    "successfully yield a message ID on valid request" in new Ctx {
      driver.aSendPlainFor(
        credentials = someCredentials,
        source = someSenderPhone,
        destPhone = someDestPhone,
        text = someText
      ) returns(
          msgId = someMessageId
      )

      cellact.sendPlain(
        sender = Sender(
          phone = Some(someSenderPhone)
        ),
        destPhone = someDestPhone,
        text = someText
      ) must beSuccessful(
        value = ===(someMessageId)
      )
    }

    "gracefully fail on error" in new Ctx {
      val someReturnCode = "some code"
      val someReturnMessage = "some message"

      driver.aSendPlainFor(
        credentials = someCredentials,
        source = someSenderPhone,
        destPhone = someDestPhone,
        text = someText
      ) failsWith(
        code = someReturnCode,
        message = someReturnMessage
      )

      cellact.sendPlain(
        sender = Sender(
          phone = Some(someSenderPhone)
        ),
        destPhone = someDestPhone,
        text = someText
      ) must beFailure[String, SmsErrorException](
        msg = contain(someReturnCode) and contain(someReturnMessage)
      )
    }
  }

  step {
    driver.stopProbe()
  }
}
