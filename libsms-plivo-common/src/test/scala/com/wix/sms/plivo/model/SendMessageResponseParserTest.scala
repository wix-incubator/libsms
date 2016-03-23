package com.wix.sms.plivo.model

import org.specs2.mutable.SpecWithJUnit
import org.specs2.specification.Scope

class SendMessageResponseParserTest extends SpecWithJUnit {
  trait Ctx extends Scope {
    val someSendMessageResponse = SendMessageResponse(
      api_id = "some api id",
      error = Some("some error"),
      message_uuid = Some(Seq("1", "2"))
    )

    val parser = new SendMessageResponseParser
  }

  "stringify and then parse" should {
    "yield an object similar to the original one" in new Ctx {
      val json = parser.stringify(someSendMessageResponse)
      parser.parse(json) must beEqualTo(someSendMessageResponse)
    }
  }
}
