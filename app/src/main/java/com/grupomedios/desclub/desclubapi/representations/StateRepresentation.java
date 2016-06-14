package com.grupomedios.desclub.desclubapi.representations;

import java.io.Serializable;

/**
 * Created by jhoncruz on 28/05/15.
 */
public class StateRepresentation implements Serializable {

    private String _id;
    private Integer stateId;
    private String name;

    public StateRepresentation(String _id, Integer stateId, String name) {
        this._id = _id;
        this.stateId = stateId;
        this.name = name;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Integer getStateId() {
        return stateId;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
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
