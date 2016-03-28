package com.wix.sms.clickatell.model

import org.json4s.DefaultFormats
import org.json4s.native.Serialization

class MessageResponseParser {
  implicit val formats = DefaultFormats

  def stringify(obj: MessageResponse): String = {
    Serialization.write(obj)
  }

  def parse(json: String): MessageResponse = {
    Serialization.read[MessageResponse](json)
  }
}
