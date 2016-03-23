package com.wix.sms.plivo.model

/** @see <a href="https://www.plivo.com/docs/api/message/#send-a-message">Send a Message</a> */
case class SendMessageResponse(api_id: String,
                               error: Option[String] = None,
                               message_uuid: Option[Seq[String]] = None)