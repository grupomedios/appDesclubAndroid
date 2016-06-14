package com.grupomedios.desclub.desclubapi.representations;

import java.io.Serializable;

/**
 * Created by jhoncruz on 28/05/15.
 */
public class FakeCategoryRepresentation extends CategoryRepresentation implements Serializable {

    private int image; //drawable
    private int color;

    public FakeCategoryRepresentation(String _id, String name, int image, int color) {
        super(_id, name);
        this.image = image;
        this.color = color;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
