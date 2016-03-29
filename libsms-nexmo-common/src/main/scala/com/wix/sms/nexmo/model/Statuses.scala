package com.wix.sms.nexmo.model

/** @see <a href="https://docs.nexmo.com/api-ref/sms-api/response/status-codes">Response codes</a> */
object Statuses {
  /** The message was successfully accepted for delivery by Nexmo. */
  val success = "0"

  /** You have exceeded the submission capacity allowed on this account. Please wait and retry. */
  val throttled = "1"

  /** Your request is incomplete and missing some mandatory parameters. */
  val missingParams = "2"

  /** The value of one or more parameters is invalid. */
  val invalidParams = "3"

  /** The api_key / api_secret you supplied is either invalid or disabled. */
  val invalidCredentials = "4"

  /** There was an error processing your request in the Nexmo Cloud Communications Platform. */
  val internalError = "5"

  /**
   * The Nexmo Cloud Communications Platform was unable to process your request. For example, due to an unrecognized
   * prefix for the phone number.
   */
  val invalidMessage = "6"

  /** The number you are trying to submit to is blacklisted and may not receive messages. */
  val numberBarred = "7"

  /** The api_key you supplied is for an account that has been barred from submitting messages. */
  val partnerAccountBarred = "8"

  /** Your pre-paid account does not have sufficient credit to process this message. */
  val partnerQuotaExceeded = "9"

  /** This account is not provisioned for REST submission, you should use SMPP instead. */
  val accountNotEnabledForRest = "11"

  /** The length of udh and body was greater than 140 octets for a binary type SMS request. */
  val messageTooLong = "12"

  /** Message was not submitted because there was a communication failure. */
  val communicationFailed = "13"

  /** Message was not submitted due to a verification failure in the submitted signature. */
  val invalidSignature = "14"

  /**
   * Due to local regulations, the SenderID you set in from in the request was not accepted. See our information about
   * Country specific features and restrictions.
   */
  val invalidSenderAddress = "15"

  /** The value of ttl in your request was invalid. */
  val invalidTtl = "16"

  /** Your request makes use of a facility that is not enabled on your account. */
  val facilityNotAllowed = "19"

  /** The value of message-class in your request was out of range. Possible values are from 0 to 3 inclusive. */
  val invalidMessageClass = "20"

  /** You did not include https in the URL you set in callback. */
  val badCallbackUrl = "23"

  /**
   * The phone number you set in to is not in your pre-approved destination list. To send messages to this phone number,
   * add it using Nexmo Dashboard.
   */
  val nonWhiteListedDestination = "29"

  val invalidAccountCampaign = "101"

  /** You tried to send a message to a destination number that has opted out of your program. */
  val msisdnOptedOutForCampaign = "102"

  val invalidCampaignShortcode = "102"
  val invalidMsisdn = "103"
}
