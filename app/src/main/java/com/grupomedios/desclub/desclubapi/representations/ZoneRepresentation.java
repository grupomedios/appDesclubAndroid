package com.grupomedios.desclub.desclubapi.representations;

import java.io.Serializable;

/**
 * Created by jhoncruz on 28/05/15.
 */
public class ZoneRepresentation implements Serializable {

    private String _id;
    private Integer zoneId;
    private Integer stateId;
    private String name;

    public ZoneRepresentation(String _id, Integer zoneId, String name) {
        this._id = _id;
        this.zoneId = zoneId;
        this.name = name;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Integer getZoneId() {
        return zoneId;
    }

    public void setZoneId(Integer zoneId) {
        this.zoneId = zoneId;
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
