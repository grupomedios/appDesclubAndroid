package com.grupomedios.desclub.desclubapi.representations;

import java.io.Serializable;

/**
 * Created by jhoncruz on 28/05/15.
 */
public class NearByDiscountWithFakeCategoryRepresentation implements Serializable {

    private NearByDiscountRepresentation nearByDiscount;
    private FakeCategoryRepresentation category;

    public NearByDiscountWithFakeCategoryRepresentation(NearByDiscountRepresentation discount, FakeCategoryRepresentation category) {
        this.nearByDiscount = discount;
        this.category = category;
    }

    public NearByDiscountRepresentation getNearByDiscount() {
        return nearByDiscount;
    }

    public void setNearByDiscount(NearByDiscountRepresentation nearByDiscount) {
        this.nearByDiscount = nearByDiscount;
    }

    public FakeCategoryRepresentation getCategory() {
        return category;
    }

    public void setCategory(FakeCategoryRepresentation category) {
        this.category = category;
    }
}
