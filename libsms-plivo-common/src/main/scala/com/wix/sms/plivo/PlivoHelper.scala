package com.wix.sms.plivo

import com.wix.sms.model.Sender
import com.wix.sms.plivo.model.SendMessageRequest

object PlivoHelper {
  def createSendMessageRequest(sender: Sender, destPhone: String, text: String): SendMessageRequest = {
    SendMessageRequest(
      src = sender.name.getOrElse(sender.phone.map { toPlivoPhone }.orNull),
      dst = toPlivoPhone(destPhone),
      text = text
    )
  }

  private def toPlivoPhone(phone: String): String = {
    phone.substring(1)
  }
}
