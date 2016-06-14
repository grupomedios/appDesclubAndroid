package com.grupomedios.desclub.desclubapi.representations;

import java.io.Serializable;

/**
 * Created by jhoncruz on 28/05/15.
 */
public class DiscountRepresentation implements Serializable {

    private String _id;

    private BranchRepresentation branch;

    private BrandRepresentation brand;

    private SubcategoryRepresentation subcategory;

    private CategoryRepresentation category;

    private String cash;
    private String card;
    private String promo;

    private String restriction;

    private LocationRepresentation location;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public BranchRepresentation getBranch() {
        return branch;
    }

    public void setBranch(BranchRepresentation branch) {
        this.branch = branch;
    }

    public BrandRepresentation getBrand() {
        return brand;
    }

    public void setBrand(BrandRepresentation brand) {
        this.brand = brand;
    }

    public SubcategoryRepresentation getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(SubcategoryRepresentation subcategory) {
        this.subcategory = subcategory;
    }

    public CategoryRepresentation getCategory() {
        return category;
    }

    public void setCategory(CategoryRepresentation category) {
        this.category = category;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getPromo() {
        return promo;
    }

    public void setPromo(String promo) {
        this.promo = promo;
    }

    public String getRestriction() {
        return restriction;
    }

    public void setRestriction(String restriction) {
        this.restriction = restriction;
    }

    public LocationRepresentation getLocation() {
        return location;
    }

    public void setLocation(LocationRepresentation location) {
        this.location = location;
    }
}
