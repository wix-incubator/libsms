/*      __ __ _____  __                                              *\
**     / // // /_/ |/ /          Wix                                 **
**    / // // / /|   /           (c) 2006-2014, Wix LTD.             **
**   / // // / //   |            http://www.wix.com/                 **
**   \__/|__/_//_/| |                                                **
\*                |/                                                 */
package com.wix.sms.model

import scala.util.Try

/** The Outgoing SMS gateway SPI. */
trait SmsGateway {
  /**
    * Sends a single plain (non-Unicode) SMS.
    *
    * @param sender      The SMS source.
    * @param destPhone   Destination phone number in E.164 format, e.g. +12125551234
    * @param text        The plain (non-Unicode) text to send.
    * @return a gateway-specific unique message identifier on success (or empty string if no such id exists),
    *         or any kind of SmsException on error
    *
    * @see <a href="https://en.wikipedia.org/wiki/Short_Message_Service#Message_size">Message size</a>
    */
  def sendPlain(sender: Sender, destPhone: String, text: String): Try[String]

  /**
    * Sends a single Unicode SMS.
    *
    * @param sender      The SMS source.
    * @param destPhone   Destination phone number in E.164 format, e.g. +12125551234
    * @param text        The Unicode text to send.
    * @return a gateway-specific unique message identifier on success (or empty string if no such id exists),
    *         or any kind of SmsException on error
    *
    * @see <a href="https://en.wikipedia.org/wiki/Short_Message_Service#Message_size">Message size</a>
    */
  def sendUnicode(sender: Sender, destPhone: String, text: String): Try[String]
}
