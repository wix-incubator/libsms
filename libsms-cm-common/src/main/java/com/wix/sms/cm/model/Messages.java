package com.wix.sms.cm.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "MESSAGES")
public class Messages {
    @XmlElement
    public Authentication AUTHENTICATION;

    /**
     * Required. The msg-tag signals the start of a message and should comprise of at least a from, to and body-tag.
     * One HTTP-call can support up to 1000 msg elements.
     */
    @XmlElement
    public Msg MSG;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Messages messages = (Messages) o;

        if (AUTHENTICATION != null ? !AUTHENTICATION.equals(messages.AUTHENTICATION) : messages.AUTHENTICATION != null)
            return false;
        return !(MSG != null ? !MSG.equals(messages.MSG) : messages.MSG != null);

    }

    @Override
    public int hashCode() {
        int result = AUTHENTICATION != null ? AUTHENTICATION.hashCode() : 0;
        result = 31 * result + (MSG != null ? MSG.hashCode() : 0);
        return result;
    }
}