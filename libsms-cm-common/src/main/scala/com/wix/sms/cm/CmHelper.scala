package com.wix.sms.cm

import com.wix.sms.cm.model.{Authentication, Dcs, Messages, Msg}
import com.wix.sms.model.Sender

object CmHelper {
  def createMessages(credentials: Credentials, sender: Sender, destPhone: String, text: String): Messages = {
    val messages = new Messages

    messages.AUTHENTICATION = new Authentication
    messages.AUTHENTICATION.PRODUCTTOKEN = credentials.productToken

    messages.MSG = new Msg
    messages.MSG.FROM = sender.name.getOrElse(sender.phone.orNull)
    messages.MSG.TO = toCmPhone(destPhone)
    messages.MSG.BODY = text
    messages.MSG.DCS = Dcs.gsm

    messages
  }

  private def toCmPhone(phone: String): String = {
    s"00${phone.substring(1)}"
  }
}
