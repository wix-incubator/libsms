package com.wix.sms.clickatell.model

case class MessageRequest(text: String, to: Seq[String], from: String)
