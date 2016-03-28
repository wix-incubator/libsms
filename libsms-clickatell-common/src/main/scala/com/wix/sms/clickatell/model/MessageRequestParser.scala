package com.wix.sms.clickatell.model

import org.json4s.DefaultFormats
import org.json4s.native.Serialization

class MessageRequestParser {
  implicit val formats = DefaultFormats

  def stringify(obj: MessageRequest): String = {
    Serialization.write(obj)
  }

  def parse(json: String): MessageRequest = {
    Serialization.read[MessageRequest](json)
  }
}
