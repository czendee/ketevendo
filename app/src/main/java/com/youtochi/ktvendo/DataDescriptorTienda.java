package com.youtochi.ktvendo;

/**
 * Created by 813743 on 27/11/2017.
 */



import java.io.Serializable;


public class DataDescriptorTienda implements Serializable {
    private String id;


    private String internalId;
    private String name;
    private String tipo;
    private String manada;
    private String posLon;
    private String posLat;
    private String rentaTiempo;
    private String rentaCosto;
    //    private String estaSiendoUtilizado;
//    private String estaSiendoUtilizadoEsteUsuario;
    private String transmite;
    private String transmiteCanal;

    public String getTransmite() {
        return transmite;
    }

    public void setTransmite(String transmite) {
        this.transmite = transmite;
    }

    public String getTransmiteCanal() {
        return transmiteCanal;
    }

    public void setTransmiteCanal(String transmiteCanal) {
        this.transmiteCanal = transmiteCanal;
    }



    public DataDescriptorTienda(String id, String internalId, String name, String tipo, String manada, String porLon, String posAlt, String rentaTiempo, String rentaCosto, String transmite, String transmiteCanal) {
        super();
        this.id = id;
        this.internalId = internalId;
        this.name = name;
        this.tipo = tipo;
        this.manada = manada;
        this.posLon = porLon;
        this.posLat = posAlt;
        this.rentaTiempo = rentaTiempo;
        this.rentaCosto = rentaCosto;
        this.transmite = transmite;
        this.transmiteCanal = transmiteCanal;
    }

    public DataDescriptorTienda(){

    super();
     }
    // get and set methods

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getManada() {
        return manada;
    }

    public void setManada(String manada) {
        this.manada = manada;
    }

    public String getPosLon() {
        return posLon;
    }

    public void setPosLon(String posLon) {
        this.posLon = posLon;
    }

    public String getPosLat() {
        return posLat;
    }

    public void setPosLat(String posAlt) {
        this.posLat = posAlt;
    }

    public String getRentaTiempo() {
        return rentaTiempo;
    }

    public void setRentaTiempo(String rentaTiempo) {
        this.rentaTiempo = rentaTiempo;
    }

    public String getRentaCosto() {
        return rentaCosto;
    }

    public void setRentaCosto(String rentaCosto) {
        this.rentaCosto = rentaCosto;
    }
    public String getInternalId() {
        return internalId;
    }

    public void setInternalId(String internalId) {
        this.internalId = internalId;
    }
}