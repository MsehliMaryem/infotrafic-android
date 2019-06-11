package com.example.hp.infotraficmobile.services;

import com.example.hp.infotraficmobile.model.MessageResponse;
import com.example.hp.infotraficmobile.model.Alerte;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface AlerteService{

        @GET("/alerte/enable")
        public Call<List<Alerte>> getAll();

        @GET("/alerte/findByType")
        public Call<List<Alerte>> findByType(@Query("ids") int[] ids);
        @POST("/alerte")
        public  Call<MessageResponse> ajouter(@Body Alerte alerte);
        @PUT ("/alerte")
        public  Call<MessageResponse> modifier(@Body Alerte alerte);
        @DELETE("/alerte")
        public  Call<MessageResponse> supprimer(@Body Alerte alerte);
}
