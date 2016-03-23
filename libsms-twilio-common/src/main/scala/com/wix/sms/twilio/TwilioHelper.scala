package com.wix.sms.twilio

import com.wix.sms.model.Sender
import com.wix.sms.twilio.model.Fields

object TwilioHelper {
  def createRequestParams(sender: Sender, destPhone: String, text: String): Map[String, String] = {
    Map(
      Fields.to -> destPhone,
      Fields.from -> sender.name.getOrElse(sender.phone.orNull),
      Fields.body -> text
    )
  }
}
