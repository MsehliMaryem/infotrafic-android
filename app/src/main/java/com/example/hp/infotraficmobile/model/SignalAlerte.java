package com.example.hp.infotraficmobile.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class SignalAlerte implements Serializable {
    @SerializedName("id")
    private SignalAlerteId id;

    @SerializedName("dateSignal")
    private Date dateSignal;
    @SerializedName("cause")
    private String cause;

    public SignalAlerteId getId() {
        return id;
    }

    public void setId(SignalAlerteId id) {
        this.id = id;
    }

    public Date getDateSignal() {
        return dateSignal;
    }

    public void setDateSignal(Date dateSignal) {
        this.dateSignal = dateSignal;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }
}
