package com.example.hp.infotraficmobile.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SignalAlerteId implements Serializable {
    @SerializedName("idPersonne")
    private long idPersonne;
    @SerializedName("idAlerte")
    private long idAlerte;

    public long getIdPersonne() {
        return idPersonne;
    }

    public void setIdPersonne(long idPersonne) {
        this.idPersonne = idPersonne;
    }

    public long getIdAlerte() {
        return idAlerte;
    }

    public void setIdAlerte(long idAlerte) {
        this.idAlerte = idAlerte;
    }
}
