/*      __ __ _____  __                                              *\
**     / // // /_/ |/ /          Wix                                 **
**    / // // / /|   /           (c) 2006-2014, Wix LTD.             **
**   / // // / //   |            http://www.wix.com/                 **
**   \__/|__/_//_/| |                                                **
\*                |/                                                 */
package com.wix.sms

/** An exception raised by an SMS gateway. */
class SmsException(message: String = null, cause: Throwable = null) extends RuntimeException(message, cause)

/** An exception raised when a communication error happens, e.g. connection is down. Common practice is to retry. */
case class CommunicationException(message: String = null,
                                  cause: Throwable = null) extends SmsException(message, cause)

/** An exception raised when an unrecoverable error happens, e.g. unexpected protocol. */
case class SmsErrorException(message: String = null,
                             cause: Throwable = null) extends SmsException(message, cause)
