package com.wix.sms.bulksms

import com.wix.sms.bulksms.model.{Booleans, Fields}
import com.wix.sms.model.Sender

object BulksmsHelper {
  def createRequestParams(credentials: Credentials, sender: Sender, destPhone: String, text: String, routingGroup: String): Map[String, String] = {
    Map(
      Fields.username -> credentials.username,
      Fields.password -> credentials.password,
      Fields.message -> text,
      Fields.msisdn -> destPhone,
      Fields.sender -> sender.name.getOrElse(sender.phone.orNull),
      Fields.routing_group -> routingGroup,
      Fields.repliable -> Booleans.`false`
    )
  }

}
