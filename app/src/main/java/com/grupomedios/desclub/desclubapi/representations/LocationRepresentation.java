package com.grupomedios.desclub.desclubapi.representations;

import java.io.Serializable;

/**
 * Created by jhoncruz on 28/05/15.
 */
public class LocationRepresentation implements Serializable {

    private String type;
    private Float[] coordinates;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Float[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Float[] coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public String toString() {
        return coordinates[0].toString() + " --- " + coordinates[1].toString();
    }
}
