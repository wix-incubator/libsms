package com.wix.sms.cellact.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Body {
    @XmlElement
    public String CONTENT;

    @XmlElement
    public DestList DEST_LIST;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Body body = (Body) o;

        if (CONTENT != null ? !CONTENT.equals(body.CONTENT) : body.CONTENT != null) return false;
        return !(DEST_LIST != null ? !DEST_LIST.equals(body.DEST_LIST) : body.DEST_LIST != null);

    }

    @Override
    public int hashCode() {
        int result = CONTENT != null ? CONTENT.hashCode() : 0;
        result = 31 * result + (DEST_LIST != null ? DEST_LIST.hashCode() : 0);
        return result;
    }
}