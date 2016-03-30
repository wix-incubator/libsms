package com.wix.sms.cm.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Msg {
    /** Required. This is the sender name. The maximum length is 11 characters. Example: 'Example.com' */
    @XmlElement
    public String FROM;

    /**
     * Required. This is the destination mobile number.
     * Restrictions: this value should be in international format. A single mobile number per request. Example: '00972545320320'
     */
    @XmlElement
    public String TO;

    /**
     * Required. This is the message text.
     * Restrictions: the maximum length is 160 characters.
     */
    @XmlElement
    public String BODY;

    /**
     * You use the DCS (data coding scheme) paramater to indicate the type of message you are sending.
     * If you set DCS to '0' or do not include the parameter, the messages uses standard GSM encoding.
     * If DCS is set to '8' the message will be encoded using Unicode UCS2.
     */
    @XmlElement
    public String DCS;

    /**
     * Here you can include your message reference. This information will be returned in a status report so you can match
     * the message and it's status. It should be included in the XML when posting.
     * Restrictions: 1 - 32 alphanumeric characters.
     */
    @XmlElement
    public String REFERENCE;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Msg msg = (Msg) o;

        if (FROM != null ? !FROM.equals(msg.FROM) : msg.FROM != null) return false;
        if (TO != null ? !TO.equals(msg.TO) : msg.TO != null) return false;
        if (BODY != null ? !BODY.equals(msg.BODY) : msg.BODY != null) return false;
        if (DCS != null ? !DCS.equals(msg.DCS) : msg.DCS != null) return false;
        return !(REFERENCE != null ? !REFERENCE.equals(msg.REFERENCE) : msg.REFERENCE != null);

    }

    @Override
    public int hashCode() {
        int result = FROM != null ? FROM.hashCode() : 0;
        result = 31 * result + (TO != null ? TO.hashCode() : 0);
        result = 31 * result + (BODY != null ? BODY.hashCode() : 0);
        result = 31 * result + (DCS != null ? DCS.hashCode() : 0);
        result = 31 * result + (REFERENCE != null ? REFERENCE.hashCode() : 0);
        return result;
    }
}