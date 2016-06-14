package com.grupomedios.desclub.desclubapi.representations;

import java.io.Serializable;

/**
 * Created by jhoncruz on 27/10/15.
 */
public class CorporateMembershipAdditionalDataRepresentation implements Serializable {

    private BancomerCorporateMembershipAdditionalDataRepresentation bancomer;

    public CorporateMembershipAdditionalDataRepresentation(BancomerCorporateMembershipAdditionalDataRepresentation bancomer) {
        this.bancomer = bancomer;
    }

    public BancomerCorporateMembershipAdditionalDataRepresentation getBancomer() {
        return bancomer;
    }

    public void setBancomer(BancomerCorporateMembershipAdditionalDataRepresentation bancomer) {
        this.bancomer = bancomer;
    }
}
