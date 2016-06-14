package com.grupomedios.desclub.desclubapi.representations;

import java.io.Serializable;

/**
 * Created by jhoncruz on 28/05/15.
 */
public class SubcategoryRepresentation implements Serializable {

    private String _id;
    private Integer subcategoryId;
    private String name;
    private String category;

    public SubcategoryRepresentation(String _id, Integer subcategoryId, String name, String category) {
        this._id = _id;
        this.subcategoryId = subcategoryId;
        this.name = name;
        this.category = category;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Integer getSubcategoryId() {
        return subcategoryId;
    }

    public void setSubcategoryId(Integer subcategoryId) {
        this.subcategoryId = subcategoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SubcategoryRepresentation)) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        SubcategoryRepresentation objToCompare = (SubcategoryRepresentation) obj;

        if (this.getSubcategoryId().equals(objToCompare.getSubcategoryId())) {
            return true;
        }

        return false;

    }
}
