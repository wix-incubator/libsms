package com.wix.sms.cellact

import com.wix.sms.cellact.model._

import scala.collection.JavaConversions._

object CellactHelper {
  def createPalo(credentials: Credentials, sender: String, destPhone: String, text: String): Palo = {
    val palo = new Palo

    palo.HEAD = new Head
    palo.HEAD.FROM = credentials.company
    palo.HEAD.APP = new App
    palo.HEAD.APP.USER = credentials.username
    palo.HEAD.APP.PASSWORD = credentials.password
    palo.HEAD.CMD = Cmds.sendtextmt

    palo.BODY = new Body
    palo.BODY.CONTENT = text
    palo.BODY.DEST_LIST = new DestList
    palo.BODY.DEST_LIST.TO = Seq(destPhone)

    palo.OPTIONAL = new Optional
    palo.OPTIONAL.CALLBACK = sender

    palo
  }
}
