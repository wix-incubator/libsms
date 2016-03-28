package com.wix.sms.clickatell.model

import org.specs2.mutable.SpecWithJUnit
import org.specs2.specification.Scope

class MessageRequestParserTest extends SpecWithJUnit {
  trait Ctx extends Scope {
    val someMessageRequest = MessageRequest(
      text = "some text",
      to = Seq("some to1", "some to2"),
      from = "some from"
    )

    val parser = new MessageRequestParser
  }

  "stringify and then parse" should {
    "yield an object similar to the original one" in new Ctx {
      val json = parser.stringify(someMessageRequest)
      parser.parse(json) must beEqualTo(someMessageRequest)
    }
  }
}
