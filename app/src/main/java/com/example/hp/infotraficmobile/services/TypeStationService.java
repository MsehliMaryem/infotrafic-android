package com.example.hp.infotraficmobile.services;

import com.example.hp.infotraficmobile.model.TypeStation;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TypeStationService {
    @GET("/typeStation")
    public Call<List<TypeStation>> getAll();
}
