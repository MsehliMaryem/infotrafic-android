package com.example.hp.infotraficmobile.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Station implements Serializable {

   @SerializedName("code")
    private long code;
   @SerializedName("nom")
    private String nom;
   @SerializedName("longitude")
    private double longitude;
   @SerializedName("latitude")
    private double latitude;
    @SerializedName("typeStation")
    private TypeStation typeStation;

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public TypeStation getTypeStation() {
        return typeStation;
    }

    public void setTypeStation(TypeStation typeStation) {
        this.typeStation = typeStation;
    }
}
