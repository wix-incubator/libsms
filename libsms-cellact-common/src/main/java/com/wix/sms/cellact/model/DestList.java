package com.wix.sms.cellact.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class DestList {
    @XmlElement
    public List<String> TO;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DestList destList = (DestList) o;

        return !(TO != null ? !TO.equals(destList.TO) : destList.TO != null);

    }

    @Override
    public int hashCode() {
        return TO != null ? TO.hashCode() : 0;
    }
}