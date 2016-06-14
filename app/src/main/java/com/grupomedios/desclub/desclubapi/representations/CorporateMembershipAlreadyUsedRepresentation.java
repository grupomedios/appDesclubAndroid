package com.grupomedios.desclub.desclubapi.representations;

import java.io.Serializable;

/**
 * Created by jhoncruz on 9/07/15.
 */
public class CorporateMembershipAlreadyUsedRepresentation implements Serializable {

    private Boolean alreadyUsed;

    public CorporateMembershipAlreadyUsedRepresentation(Boolean alreadyUsed) {
        this.alreadyUsed = alreadyUsed;
    }

    public Boolean getAlreadyUsed() {
        return alreadyUsed;
    }

    public void setAlreadyUsed(Boolean alreadyUsed) {
        this.alreadyUsed = alreadyUsed;
    }
}
