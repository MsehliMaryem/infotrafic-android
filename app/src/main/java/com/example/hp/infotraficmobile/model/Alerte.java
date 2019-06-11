package com.example.hp.infotraficmobile.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class Alerte implements Serializable {
    @SerializedName("idAlerte")
    private long idAlerte;
  //  @SerializedName("dateAlerte")
   // private Local dateAlerte;
    @SerializedName("enabled")
    private boolean enabled;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;

    @SerializedName("typeAlerte")
    private TypeAlerte typeAlerte;
    @SerializedName("personne")
    private Personne personne;
    public long getIdAlerte() {
        return idAlerte;
    }

    public void setIdAlerte(long idAlerte) {
        this.idAlerte = idAlerte;
    }

   /*public Date getDateAlerte() {
        return dateAlerte;
    }

    public void setDateAlerte(Date dateAlerte) {
        this.dateAlerte = dateAlerte;
    }
*/
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    public TypeAlerte getTypeAlerte() {
        return typeAlerte;
    }

    public void setTypeAlerte(TypeAlerte typeAlerte) {
        this.typeAlerte = typeAlerte;
    }

    public Personne getPersonne() {
        return personne;
    }

    public void setPersonne(Personne personne) {
        this.personne = personne;
    }

    @Override
    public String toString() {
        return "Alerte{" +
                "idAlerte=" + idAlerte +
                ", enabled=" + enabled +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", typeAlerte=" + typeAlerte +
                '}';
    }
}
