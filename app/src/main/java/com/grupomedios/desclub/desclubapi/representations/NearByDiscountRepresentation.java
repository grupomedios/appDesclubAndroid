package com.grupomedios.desclub.desclubapi.representations;

import java.io.Serializable;

/**
 * Created by jhoncruz on 28/05/15.
 */
public class NearByDiscountRepresentation implements Serializable {

    private Double dis;
    private DiscountRepresentation discount;

    public Double getDis() {
        return dis;
    }

    public void setDis(Double dis) {
        this.dis = dis;
    }

    public DiscountRepresentation getDiscount() {
        return discount;
    }

    public void setDiscount(DiscountRepresentation discount) {
        this.discount = discount;
    }
}
