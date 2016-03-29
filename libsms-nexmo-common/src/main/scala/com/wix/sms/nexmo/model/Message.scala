package com.wix.sms.nexmo.model

case class Message(status: String,
                   `error-text`: Option[String] = None,
                   to: Option[String] = None,
                   `message-id`: Option[String] = None,
                   `remaining-balance`: Option[String] = None,
                   `message-price`: Option[String] = None,
                   network: Option[String] = None)