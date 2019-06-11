package com.example.hp.infotraficmobile.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ChauffeurTaxi extends Personne implements Serializable {
    @SerializedName("numPermis")
        private long numPermis;


        public long getNumPermis() {
            return numPermis;
        }

        public void setNumPermis(long numPermis) {
            this.numPermis = numPermis;
        }



    }

