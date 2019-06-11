package com.example.hp.infotraficmobile.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Taxi implements Serializable {
    @SerializedName("matricule")
    private String matricule;
    @SerializedName("numTaxi")
    private String numTaxi;
    @SerializedName("marque")
    private String marque;
    @SerializedName("modele")
    private String modele;
    @SerializedName("nbplace")
    private int nbplace;
    @SerializedName("disponibilite")
    private boolean disponibilite;
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("chauffeurTaxi")
    private ChauffeurTaxi chauffeurTaxi;

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getNumTaxi() {
        return numTaxi;
    }

    public void setNumTaxi(String numTaxi) {
        this.numTaxi = numTaxi;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public int getNbplace() {
        return nbplace;
    }

    public void setNbplace(int nbplace) {
        this.nbplace = nbplace;
    }

    public boolean isDisponibilite() {
        return disponibilite;
    }

    public void setDisponibilite(boolean disponibilite) {
        this.disponibilite = disponibilite;
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


    public ChauffeurTaxi getChauffeurTaxi() {
        return chauffeurTaxi;
    }

    public void setChauffeurTaxi(ChauffeurTaxi chauffeurTaxi) {
        this.chauffeurTaxi = chauffeurTaxi;
    }
}
