/*      __ __ _____  __                                              *\
**     / // // /_/ |/ /          Wix                                 **
**    / // // / /|   /           (c) 2006-2014, Wix LTD.             **
**   / // // / //   |            http://www.wix.com/                 **
**   \__/|__/_//_/| |                                                **
\*                |/                                                 */
package com.wix.sms.model

import com.twitter.util.Try

/**
 * The Fax trait, which allows sending fax documents and querying their status.
 * Fax providers should subclass and implement this trait.
 */
trait SmsGateway {
  /** @return the SMS gateway's unique ID. */
  def getId: String

  /**
   * Sends a single plain (non-unicode) SMS.
   * @param sender      The SMS source.
   * @param destPhone   Destination phone number in E.164 format, e.g. +12125551234
   * @param text        The text to send.
   * @return a gateway-specific unique message identifier on success (or empty string if no such id exists), or any kind
   *         of SmsException on error
   */
  def sendPlain(sender: Sender, destPhone: String, text: String): Try[String]
}
