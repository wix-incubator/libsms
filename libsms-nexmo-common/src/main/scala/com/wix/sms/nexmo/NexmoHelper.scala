package com.wix.sms.nexmo

import com.wix.sms.model.Sender
import com.wix.sms.nexmo.model.Fields

object NexmoHelper {
  def createRequestParams(credentials: Credentials, sender: Sender, destPhone: String, text: String): Map[String, String] = {
    Map(
      Fields.apiKey -> credentials.apiKey,
      Fields.apiSecret -> credentials.apiSecret,
      Fields.to -> destPhone,
      Fields.text -> text,
      Fields.from -> sender.name.getOrElse(sender.phone.orNull)
    )
  }
}
