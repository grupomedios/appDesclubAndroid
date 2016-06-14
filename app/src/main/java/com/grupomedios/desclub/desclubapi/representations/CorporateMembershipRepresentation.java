package com.grupomedios.desclub.desclubapi.representations;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by jhoncruz on 8/07/15.
 */
public class CorporateMembershipRepresentation implements Serializable {

    private String _id;
    private String name;
    private String email;
    private String firstName;
    private String lastName1;
    private String lastName2;
    private String membershipNumber;
    private boolean alreadyUsed;
    private CorporateRepresentation corporate;
    private CorporateMembershipAdditionalDataRepresentation additionalData;
    private Date validThru;
    private Date created;


    public CorporateMembershipRepresentation(String email) {
        this.email = email;
    }

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName1() {
        return lastName1;
    }

    public void setLastName1(String lastName1) {
        this.lastName1 = lastName1;
    }

    public String getLastName2() {
        return lastName2;
    }

    public void setLastName2(String lastName2) {
        this.lastName2 = lastName2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMembershipNumber() {
        return membershipNumber;
    }

    public void setMembershipNumber(String membershipNumber) {
        this.membershipNumber = membershipNumber;
    }

    public boolean isAlreadyUsed() {
        return alreadyUsed;
    }

    public void setAlreadyUsed(boolean alreadyUsed) {
        this.alreadyUsed = alreadyUsed;
    }

    public CorporateRepresentation getCorporate() {
        return corporate;
    }

    public void setCorporate(CorporateRepresentation corporate) {
        this.corporate = corporate;
    }

    public CorporateMembershipAdditionalDataRepresentation getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(CorporateMembershipAdditionalDataRepresentation additionalData) {
        this.additionalData = additionalData;
    }

    public Date getValidThru() {
        return validThru;
    }

    public void setValidThru(Date validThru) {
        this.validThru = validThru;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
