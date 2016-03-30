package com.wix.sms.cm.model

import org.specs2.mutable.SpecWithJUnit
import org.specs2.specification.Scope

class MessagesParserTest extends SpecWithJUnit {
  trait Ctx extends Scope {
    val parser = new MessagesParser
  }

  "stringify and then parse" should {
    val messages = new Messages

    messages.AUTHENTICATION = new Authentication
    messages.AUTHENTICATION.PRODUCTTOKEN = "some product token"

    messages.MSG = new Msg
    messages.MSG.FROM = "some from"
    messages.MSG.TO = "some to"
    messages.MSG.BODY = "some text"
    messages.MSG.DCS = Dcs.gsm

    "yield an object similar to the original one" in new Ctx {
      val xml = parser.stringify(messages)
      parser.parse(xml) must beEqualTo(messages)
    }
  }
}
