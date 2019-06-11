package com.example.hp.infotraficmobile.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TypeAlerte implements Serializable {
    @SerializedName("idType")
    private int idType;
    @SerializedName("nom")
    private String nom;
    @SerializedName("photo")
    private String photo;

    public int getIdType() {
        return idType;
    }

    public void setIdType(int idType) {
        this.idType = idType;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
