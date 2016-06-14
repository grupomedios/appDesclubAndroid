package com.grupomedios.desclub.desclubapi.representations;

import java.io.Serializable;

/**
 * Created by jhoncruz on 28/05/15.
 */
public class CategoryRepresentation implements Serializable {

    private String _id;
    private Integer categoryId;
    private String originalName;
    private String name;

    public CategoryRepresentation(String _id, String name) {
        this._id = _id;
        this.name = name;
    }

    public CategoryRepresentation(String _id, String name, Integer categoryId) {
        this._id = _id;
        this.name = name;
        this.categoryId = categoryId;
    }

    public CategoryRepresentation(String _id, Integer categoryId, String originalName, String name) {
        this._id = _id;
        this.categoryId = categoryId;
        this.originalName = originalName;
        this.name = name;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
