package com.wix.sms.cellact.model

import java.io.{StringReader, StringWriter}
import javax.xml.bind.{JAXBContext, Marshaller}

class PaloParser {
  val context = JAXBContext.newInstance(classOf[Palo])
  val marshaller = context.createMarshaller()
  marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);

  val unmarshaller = context.createUnmarshaller()

  def stringify(obj: Palo): String = {
    val writer = new StringWriter()
    try {
      marshaller.marshal(obj, writer)
    } finally {
      writer.close()
    }
    writer.toString
  }

  def parse(xml: String): Palo = {
    val reader = new StringReader(xml)
    try {
      unmarshaller.unmarshal(reader).asInstanceOf[Palo]
    } finally {
      reader.close()
    }
  }
}
