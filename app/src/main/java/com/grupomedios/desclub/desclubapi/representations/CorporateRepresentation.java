package com.grupomedios.desclub.desclubapi.representations;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by jhoncruz on 8/07/15.
 */
public class CorporateRepresentation implements Serializable {

    private String _id;
    private String name;
    private String membershipPrefix;
    private Date created;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMembershipPrefix() {
        return membershipPrefix;
    }

    public void setMembershipPrefix(String membershipPrefix) {
        this.membershipPrefix = membershipPrefix;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
