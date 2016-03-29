package com.wix.sms.nexmo.model

import org.json4s.DefaultFormats
import org.json4s.native.Serialization

class ResponseParser {
  implicit val formats = DefaultFormats

  def stringify(obj: Response): String = {
    Serialization.write(obj)
  }

  def parse(json: String): Response = {
    Serialization.read[Response](json)
  }
}
