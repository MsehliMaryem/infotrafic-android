package com.example.hp.infotraficmobile.services;

import com.example.hp.infotraficmobile.model.TypeAlerte;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TypeAlerteService {
    @GET("/typeAlerte")
    public Call<List<TypeAlerte>> getAll();
}
