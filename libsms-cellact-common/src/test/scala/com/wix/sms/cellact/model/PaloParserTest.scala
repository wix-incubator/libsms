package com.wix.sms.cellact.model

import org.specs2.mutable.SpecWithJUnit
import org.specs2.specification.Scope

import scala.collection.JavaConversions._

class PaloParserTest extends SpecWithJUnit {
  trait Ctx extends Scope {
    val parser = new PaloParser
  }

  "stringify and then parse" should {
    val palo = new Palo

    palo.HEAD = new Head
    palo.HEAD.FROM = "some from"
    palo.HEAD.APP = new App
    palo.HEAD.APP.USER = "some user"
    palo.HEAD.APP.PASSWORD = "some password"
    palo.HEAD.CMD = "some cmd"

    palo.BODY = new Body
    palo.BODY.CONTENT = "some content"
    palo.BODY.DEST_LIST = new DestList
    palo.BODY.DEST_LIST.TO = Seq("some to 1", "some to 2")

    palo.OPTIONAL = new Optional
    palo.OPTIONAL.CALLBACK = "some callback"

    "yield an object similar to the original one" in new Ctx {
      val xml = parser.stringify(palo)
      parser.parse(xml) must beEqualTo(palo)
    }
  }
}
