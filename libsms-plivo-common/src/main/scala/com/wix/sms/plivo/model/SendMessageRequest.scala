package com.wix.sms.plivo.model

/** @see <a href="https://www.plivo.com/docs/api/message/#send-a-message">Send a Message</a> */
case class SendMessageRequest(src: String, dst: String, text: String)