package com.wix.sms.nexmo.model

import org.specs2.mutable.SpecWithJUnit
import org.specs2.specification.Scope

class ResponseParserTest extends SpecWithJUnit {
  trait Ctx extends Scope {
    val someResponse = Response(
      `message-count` = "1",
      messages = Seq(Message(
        status = Statuses.success,
        to = Some("some to")
      ))
    )

    val parser = new ResponseParser
  }

  "stringify and then parse" should {
    "yield an object similar to the original one" in new Ctx {
      val json = parser.stringify(someResponse)
      parser.parse(json) must beEqualTo(someResponse)
    }
  }
}
