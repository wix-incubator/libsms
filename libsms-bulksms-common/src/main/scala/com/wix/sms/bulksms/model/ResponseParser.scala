package com.wix.sms.bulksms.model

class ResponseParser {
  def stringify(obj: Response): String = {
    obj.batchId match {
      case Some(batchId) => s"${obj.code}|${obj.description}|$batchId\n"
      case None => s"${obj.code}|${obj.description}\n"
    }
  }

  def parse(str: String): Response = {
    val parts = str.trim.split("""\|""")
    parts.length match {
      case 2 => Response(code = parts(0), description = parts(1))
      case 3 => Response(code = parts(0), description = parts(1), batchId = Some(parts(2)))
      case _ => throw new IllegalArgumentException(s"Unexpected response format: $str")
    }
  }
}
