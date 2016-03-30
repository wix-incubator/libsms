package com.wix.sms.cm.model

import java.io.{StringReader, StringWriter}
import javax.xml.bind.{JAXBContext, Marshaller}

class MessagesParser {
  val context = JAXBContext.newInstance(classOf[Messages])
  val marshaller = context.createMarshaller()
  marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true)

  val unmarshaller = context.createUnmarshaller()

  def stringify(obj: Messages): String = {
    val writer = new StringWriter()
    try {
      marshaller.marshal(obj, writer)
    } finally {
      writer.close()
    }
    writer.toString
  }

  def parse(xml: String): Messages = {
    val reader = new StringReader(xml)
    try {
      unmarshaller.unmarshal(reader).asInstanceOf[Messages]
    } finally {
      reader.close()
    }
  }
}
