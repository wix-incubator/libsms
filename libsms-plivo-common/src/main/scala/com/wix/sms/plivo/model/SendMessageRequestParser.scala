package com.wix.sms.plivo.model

import org.json4s.DefaultFormats
import org.json4s.native.Serialization

class SendMessageRequestParser {
  implicit val formats = DefaultFormats

  def stringify(obj: SendMessageRequest): String = {
    Serialization.write(obj)
  }

  def parse(json: String): SendMessageRequest = {
    Serialization.read[SendMessageRequest](json)
  }
}
