package com.example.hp.infotraficmobile.services;

import com.example.hp.infotraficmobile.model.Abonnee;
import com.example.hp.infotraficmobile.model.MessageResponse;
import com.example.hp.infotraficmobile.model.Personne;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginService {

    @POST("/login")
    public Call<Void> login(@Body Personne personne);
    @POST("/register/activateAccount")
    public Call<MessageResponse> activate(@Body Personne personne);
}
