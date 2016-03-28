package com.wix.sms.clickatell.model

case class MessageResponse(error: Option[Error] = None, data: Option[Data] = None)