package com.grupomedios.desclub.desclubapi.representations;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by jhoncruz on 28/05/15.
 */
public class BrandRepresentation implements Serializable {

    private String _id;
    private Integer brandId;
    private String name;
    private String logoSmall;
    private String logoBig;
    private Date validity_start;
    private Date validity_end;
    private String url;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoSmall() {
        return logoSmall;
    }

    public void setLogoSmall(String logoSmall) {
        this.logoSmall = logoSmall;
    }

    public String getLogoBig() {
        return logoBig;
    }

    public void setLogoBig(String logoBig) {
        this.logoBig = logoBig;
    }

    public Date getValidity_start() {
        return validity_start;
    }

    public void setValidity_start(Date validity_start) {
        this.validity_start = validity_start;
    }

    public Date getValidity_end() {
        return validity_end;
    }

    public void setValidity_end(Date validity_end) {
        this.validity_end = validity_end;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
