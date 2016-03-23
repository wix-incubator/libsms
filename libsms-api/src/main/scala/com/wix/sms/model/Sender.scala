package com.wix.sms.model

/**
 * An SMS sender.
 *
 * @param name    Alphanumeric sender-ID
 * @param phone   Phone number in E.164 format, e.g. +12125551234
 *
 * @note Alphanumeric sender-IDs are not allowed in some countries because of carrier regulations, e.g. US, Canada, Brazil.
 */
case class Sender(name: Option[String] = None, phone: Option[String] = None)
