package com.grupomedios.desclub.desclubapi.representations;

import java.io.Serializable;

/**
 * Created by jhoncruz on 27/10/15.
 */
public class BancomerCorporateMembershipAdditionalDataRepresentation implements Serializable {

    private String policy;
    private String mobile;

    public BancomerCorporateMembershipAdditionalDataRepresentation(String policy, String mobile) {
        this.policy = policy;
        this.mobile = mobile;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
