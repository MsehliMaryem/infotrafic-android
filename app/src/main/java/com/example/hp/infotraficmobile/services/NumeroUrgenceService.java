package com.example.hp.infotraficmobile.services;

import com.example.hp.infotraficmobile.model.NumeroUrgence;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NumeroUrgenceService {
    @GET("/numerourgence")
    public Call<List<NumeroUrgence>> getAll();
}
