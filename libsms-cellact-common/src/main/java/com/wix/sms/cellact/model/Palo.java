package com.wix.sms.cellact.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "PALO")
public class Palo {
    @XmlElement
    public Head HEAD;

    @XmlElement
    public Body BODY;

    @XmlElement
    public Optional OPTIONAL;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Palo palo = (Palo) o;

        if (HEAD != null ? !HEAD.equals(palo.HEAD) : palo.HEAD != null) return false;
        if (BODY != null ? !BODY.equals(palo.BODY) : palo.BODY != null) return false;
        return !(OPTIONAL != null ? !OPTIONAL.equals(palo.OPTIONAL) : palo.OPTIONAL != null);

    }

    @Override
    public int hashCode() {
        int result = HEAD != null ? HEAD.hashCode() : 0;
        result = 31 * result + (BODY != null ? BODY.hashCode() : 0);
        result = 31 * result + (OPTIONAL != null ? OPTIONAL.hashCode() : 0);
        return result;
    }
}