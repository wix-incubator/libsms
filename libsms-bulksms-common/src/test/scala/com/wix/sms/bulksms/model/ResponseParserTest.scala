package com.wix.sms.bulksms.model

import org.specs2.mutable.SpecWithJUnit
import org.specs2.specification.Scope

class ResponseParserTest extends SpecWithJUnit {
  trait Ctx extends Scope {
    val someResponse = Response(
      code = "some code",
      description = "some description",
      batchId = Some("some batch ID")
    )

    val parser = new ResponseParser
  }

  "stringify and then parse" should {
    "yield an object similar to the original one" in new Ctx {
      val str = parser.stringify(someResponse)
      parser.parse(str) must beEqualTo(someResponse)
    }
  }
}
