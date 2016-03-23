package com.wix.sms.cellact.model

import org.specs2.mutable.SpecWithJUnit
import org.specs2.specification.Scope

class ResponseParserTest extends SpecWithJUnit {
  trait Ctx extends Scope {
    val parser = new ResponseParser
  }

  "stringify and then parse" should {
    val response = new Response
    response.BLMJ = "some blmj"
    response.RESULTCODE = "123"
    response.RESULTMESSAGE = "some resultmessage"

    "yield an object similar to the original one" in new Ctx {
      val xml = parser.stringify(response)
      parser.parse(xml) must beEqualTo(response)
    }
  }
}
