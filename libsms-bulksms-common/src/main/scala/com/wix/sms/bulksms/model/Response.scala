package com.wix.sms.bulksms.model

case class Response(code: String, description: String, batchId: Option[String] = None)