package com.wix.sms.twilio.model

/** @see <a href="https://www.twilio.com/docs/api/rest/response">Twilio Docs: Twilio's response to your REST API requests</a> */
case class SmsResponse(status: Option[String] = None,
                       message: Option[String] = None,
                       code: Option[String] = None,
                       more_info: Option[String] = None,
                       sid: Option[String] = None,
                       date_created: Option[String] = None,
                       date_updated: Option[String] = None,
                       date_sent: Option[String] = None,
                       account_sid: Option[String] = None,
                       to: Option[String] = None,
                       from: Option[String] = None,
                       body: Option[String] = None,
                       flags: Option[Seq[String]] = None,
                       api_version: Option[String] = None,
                       price: Option[Double] = None,
                       uri: Option[String] = None)