package com.wix.sms.cellact.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Optional {
    @XmlElement
    public String CALLBACK;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Optional optional = (Optional) o;

        return !(CALLBACK != null ? !CALLBACK.equals(optional.CALLBACK) : optional.CALLBACK != null);

    }

    @Override
    public int hashCode() {
        return CALLBACK != null ? CALLBACK.hashCode() : 0;
    }
}