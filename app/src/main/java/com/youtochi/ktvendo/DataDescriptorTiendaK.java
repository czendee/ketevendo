package com.youtochi.ktvendo;

/**
 * Created by 813743 on 27/11/2017.
 */



import java.io.Serializable;


public class DataDescriptorTiendaK implements Serializable {
    private String id;


    private String internalId;
    private String name;
    private String tipo;
    private String periscope;
    private String facelive;
    private String status;
    private String lon;
    private String lat;

    public DataDescriptorTiendaK(String id, String internalId, String name, String tipo, String periscope, String facelive,String lon, String lat, String status) {
        super();
        this.id = id;
        this.internalId = internalId;
        this.name = name;
        this.tipo = tipo;
        this.periscope=periscope;
        this.facelive=facelive;
        this.lon=lon;
        this.lat=lat;
        this.status=status;
    }

    public DataDescriptorTiendaK(){

    super();
     }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInternalId() {
        return internalId;
    }

    public void setInternalId(String internalId) {
        this.internalId = internalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getPeriscope() {
        return periscope;
    }

    public void setPeriscope(String periscope) {
        this.periscope = periscope;
    }

    public String getFacelive() {
        return facelive;
    }

    public void setFacelive(String facelive) {
        this.facelive = facelive;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
}