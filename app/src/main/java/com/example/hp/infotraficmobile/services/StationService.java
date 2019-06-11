package com.example.hp.infotraficmobile.services;


import com.example.hp.infotraficmobile.model.Station;

import java.util.List;

import retrofit2.Call;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;


public interface StationService {

    @GET("/stationService")
    public Call<List<Station>> getAll();

    @GET("/stationService/findByType")
    public Call<List<Station>> findByType(@Query("ids") int[] ids);
}
