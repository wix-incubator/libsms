package com.wix.sms.plivo.model

import org.specs2.mutable.SpecWithJUnit
import org.specs2.specification.Scope

class SendMessageRequestParserTest extends SpecWithJUnit {
  trait Ctx extends Scope {
    val someSendMessageRequest = SendMessageRequest(
      src = "some src",
      dst = "some dst",
      text = "some text"
    )

    val parser = new SendMessageRequestParser
  }

  "stringify and then parse" should {
    "yield an object similar to the original one" in new Ctx {
      val json = parser.stringify(someSendMessageRequest)
      parser.parse(json) must beEqualTo(someSendMessageRequest)
    }
  }
}
