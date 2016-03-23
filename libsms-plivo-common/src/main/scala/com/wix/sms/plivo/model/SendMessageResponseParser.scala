package com.wix.sms.plivo.model

import org.json4s.DefaultFormats
import org.json4s.native.Serialization

class SendMessageResponseParser {
  implicit val formats = DefaultFormats

  def stringify(obj: SendMessageResponse): String = {
    Serialization.write(obj)
  }

  def parse(json: String): SendMessageResponse = {
    Serialization.read[SendMessageResponse](json)
  }
}
