package com.grupomedios.desclub.desclubapi.representations;

import com.grupomedios.desclub.desclubandroid.BuildConfig;

import java.io.Serializable;

/**
 * Created by jhoncruz on 27/10/15.
 */
public class CorporateMembershipInputRepresentation implements Serializable {

    private String name;
    private String email;
    private String corporate;
    private CorporateMembershipAdditionalDataRepresentation additionalData;

    public CorporateMembershipInputRepresentation(String name, String email, String policy, String mobile) {
        this.name = name;
        this.email = email;
        BancomerCorporateMembershipAdditionalDataRepresentation bancomer = new BancomerCorporateMembershipAdditionalDataRepresentation(policy, mobile);
        this.additionalData = new CorporateMembershipAdditionalDataRepresentation(bancomer);
        this.corporate = BuildConfig.DESCLUB_CORPORATE;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCorporate() {
        return corporate;
    }

    public void setCorporate(String corporate) {
        this.corporate = corporate;
    }

    public CorporateMembershipAdditionalDataRepresentation getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(CorporateMembershipAdditionalDataRepresentation additionalData) {
        this.additionalData = additionalData;
    }
}
