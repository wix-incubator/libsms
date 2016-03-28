package com.wix.sms.clickatell.model

import org.specs2.mutable.SpecWithJUnit
import org.specs2.specification.Scope

class MessageResponseParserTest extends SpecWithJUnit {
  trait Ctx extends Scope {
    val someMessageResponse = MessageResponse(
      data = Some(Data(
        message = Seq(Message(
          accepted = true,
          to = "some to",
          apiMessageId = "some api message ID"
        ))
      ))
    )

    val parser = new MessageResponseParser
  }

  "stringify and then parse" should {
    "yield an object similar to the original one" in new Ctx {
      val json = parser.stringify(someMessageResponse)
      parser.parse(json) must beEqualTo(someMessageResponse)
    }
  }
}
