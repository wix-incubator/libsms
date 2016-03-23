package com.wix.sms.cellact.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "RESPONSE")
public class Response {
    @XmlElement
    public String BLMJ;

    @XmlElement
    public String RESULTCODE;

    @XmlElement
    public String RESULTMESSAGE;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Response response = (Response) o;

        if (BLMJ != null ? !BLMJ.equals(response.BLMJ) : response.BLMJ != null) return false;
        if (RESULTCODE != null ? !RESULTCODE.equals(response.RESULTCODE) : response.RESULTCODE != null) return false;
        return !(RESULTMESSAGE != null ? !RESULTMESSAGE.equals(response.RESULTMESSAGE) : response.RESULTMESSAGE != null);
    }

    @Override
    public int hashCode() {
        int result = BLMJ != null ? BLMJ.hashCode() : 0;
        result = 31 * result + (RESULTCODE != null ? RESULTCODE.hashCode() : 0);
        result = 31 * result + (RESULTMESSAGE != null ? RESULTMESSAGE.hashCode() : 0);
        return result;
    }
}