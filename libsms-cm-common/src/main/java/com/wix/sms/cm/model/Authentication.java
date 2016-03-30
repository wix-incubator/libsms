package com.wix.sms.cm.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Authentication {
    /**
     * Required. This is the product token that was sent to you by email.
     * Example: '12345678-1234-1234-1234-123456789abc'
     */
    @XmlElement
    public String PRODUCTTOKEN;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Authentication that = (Authentication) o;

        return !(PRODUCTTOKEN != null ? !PRODUCTTOKEN.equals(that.PRODUCTTOKEN) : that.PRODUCTTOKEN != null);

    }

    @Override
    public int hashCode() {
        return PRODUCTTOKEN != null ? PRODUCTTOKEN.hashCode() : 0;
    }
}