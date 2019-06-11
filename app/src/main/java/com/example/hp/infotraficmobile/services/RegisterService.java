package com.example.hp.infotraficmobile.services;

import com.example.hp.infotraficmobile.model.Abonnee;
import com.example.hp.infotraficmobile.model.ChauffeurTaxi;
import com.example.hp.infotraficmobile.model.MessageResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RegisterService {


    @POST("/register/abonnee")
    public Call<MessageResponse> registerAbonne(@Body Abonnee abonnee);
    @POST("/register/chauffeur")
    public Call<MessageResponse> registerChauffeur(@Body ChauffeurTaxi chauffeurTaxi);

}
