package com.example.hp.infotraficmobile.services;

import com.example.hp.infotraficmobile.model.Abonnee;
import com.example.hp.infotraficmobile.model.MessageResponse;
import com.example.hp.infotraficmobile.model.SignalAlerte;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SignalService {

    @POST("/signal")
    public Call<MessageResponse> signaler(@Body SignalAlerte signalAlerte);
}
