package com.wix.sms.bulksms.model

/**
 * Response status codes.
 * @see <a href="http://developer.bulksms.com/eapi/submission/send_sms/#returns">Returns</a>
 */
object StatusCodes {
  /** A normal message submission, with no error encountered so far. */
  val inProgress = "0"
  val scheduled = "1"
  val internalFatalError = "22"
  val authenticationFailure = "23"
  val dataValidationFailed = "24"
  val insufficientCredits = "25"
  val upstreamCreditsNotAvailable = "26"
  val dailyQuotaExceeded = "27"
  val upstreamQuotaExceeded = "28"
  val temporarilyUnavilable = "40"
  val maximumBatchSizeExceeded = "201"
}
