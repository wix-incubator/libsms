package com.wix.sms.cellact.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class App {
    @XmlAttribute(name = "USER")
    public String USER;

    @XmlAttribute(name = "PASSWORD")
    public String PASSWORD;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        App app = (App) o;

        if (USER != null ? !USER.equals(app.USER) : app.USER != null) return false;
        return !(PASSWORD != null ? !PASSWORD.equals(app.PASSWORD) : app.PASSWORD != null);

    }

    @Override
    public int hashCode() {
        int result = USER != null ? USER.hashCode() : 0;
        result = 31 * result + (PASSWORD != null ? PASSWORD.hashCode() : 0);
        return result;
    }
}