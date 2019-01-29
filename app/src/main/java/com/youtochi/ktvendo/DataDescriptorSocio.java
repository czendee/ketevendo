package com.youtochi.ktvendo;

/**
 * Created by 813743 on 27/11/2017.
 */



import java.io.Serializable;


public class DataDescriptorSocio implements Serializable {
    private String id;


    private String internalId;
    private String name;
    private String mobile;
    private String pin;
    private String status;




    public DataDescriptorSocio(String id, String internalId, String name, String mobile, String pin, String status) {
        super();
        this.id = id;
        this.internalId = internalId;
        this.name = name;
        this.mobile = mobile;
        this.pin = pin;
        this.status =status;
    }

    public DataDescriptorSocio(){

    super();
     }
    // get and set methods


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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}