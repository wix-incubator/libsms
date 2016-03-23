package com.wix.sms.cellact.model

import java.io.{StringReader, StringWriter}
import javax.xml.bind.{JAXBContext, Marshaller}

class ResponseParser {
  val context = JAXBContext.newInstance(classOf[Response])
  val marshaller = context.createMarshaller()
  marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);

  val unmarshaller = context.createUnmarshaller()

  def stringify(obj: Response): String = {
    val writer = new StringWriter()
    try {
      marshaller.marshal(obj, writer)
    } finally {
      writer.close()
    }
    writer.toString
  }

  def parse(xml: String): Response = {
    val reader = new StringReader(xml)
    try {
      unmarshaller.unmarshal(reader).asInstanceOf[Response]
    } finally {
      reader.close()
    }
  }
}
