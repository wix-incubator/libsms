package com.wix.sms.twilio.model

object Fields {
  /** The destination phone number. Format with a '+' and country code e.g., +16175551212 (E.164 format). */
  val to = "To"

  /**
   * A Twilio phone number (in E.164 format) or alphanumeric sender ID enabled for the type of message you wish to send.
   * Phone numbers or short codes purchased from Twilio work here. You cannot (for example) spoof messages from your own
   * cell phone number.
   */
  val from = "From"

  /** The text of the message you want to send, limited to 1600 characters. */
  val body = "Body"
}
