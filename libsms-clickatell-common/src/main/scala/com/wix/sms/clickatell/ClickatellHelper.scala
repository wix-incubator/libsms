package com.wix.sms.clickatell

import com.wix.sms.clickatell.model.MessageRequest
import com.wix.sms.model.Sender

object ClickatellHelper {
  def createMessageRequest(sender: Sender, destPhone: String, text: String): MessageRequest = {
    MessageRequest(
      text = text,
      to = Seq(destPhone),
      from = sender.name.getOrElse(sender.phone.orNull)
    )
  }
}
