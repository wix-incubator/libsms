package com.wix.sms.cellact.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Head {
    @XmlElement
    public String FROM;

    @XmlElement
    public App APP;

    @XmlElement
    public String CMD;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Head head = (Head) o;

        if (FROM != null ? !FROM.equals(head.FROM) : head.FROM != null) return false;
        if (APP != null ? !APP.equals(head.APP) : head.APP != null) return false;
        return !(CMD != null ? !CMD.equals(head.CMD) : head.CMD != null);

    }

    @Override
    public int hashCode() {
        int result = FROM != null ? FROM.hashCode() : 0;
        result = 31 * result + (APP != null ? APP.hashCode() : 0);
        result = 31 * result + (CMD != null ? CMD.hashCode() : 0);
        return result;
    }
}