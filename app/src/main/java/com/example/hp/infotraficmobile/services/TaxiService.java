package com.example.hp.infotraficmobile.services;

import com.example.hp.infotraficmobile.model.Alerte;
import com.example.hp.infotraficmobile.model.MessageResponse;
import com.example.hp.infotraficmobile.model.Taxi;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TaxiService {



    @GET("/taxi/findByChauffeur/{idChauffeur}")
    public Call<Taxi> findByChauffeur(@Path("idChauffeur") long id);

    @GET("/taxi/findByMatricule/{matricule}")
    public Call<Taxi> findByMatricule(@Path("matricule") String matricule);
    @POST("/taxi")
    public  Call<MessageResponse> ajouter(@Body Taxi taxi);
    @PUT("/taxi")
    public  Call<MessageResponse> modifier(@Body Taxi taxi);
    @GET("/taxi")
    public  Call<List<Taxi>> findDisponible();
}
