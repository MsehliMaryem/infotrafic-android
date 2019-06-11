package com.example.hp.infotraficmobile.services;

import com.example.hp.infotraficmobile.model.DemandeTaxi;
import com.example.hp.infotraficmobile.model.MessageResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface DemandeTaxiService {



    @GET("/demandetaxi/findByAbonnee/{id}")
    public Call<List<DemandeTaxi>> getAllByAbonne(@Path("id") long id);
    @GET("/demandetaxi/findByTaxi/{matricule}")
    public Call<List<DemandeTaxi>> getAllByTaxi(@Path("matricule") String matricule);
    @GET("/demandetaxi/findWaiting/{matricule}")
    public Call<List<DemandeTaxi>> getWaitingByTaxi(@Path("matricule") String matricule);
    @POST("/demandetaxi")
    public Call<MessageResponse> ajouter(@Body DemandeTaxi demandeTaxi);
    @PUT("/demandetaxi")
    public Call<MessageResponse> annuler(@Body DemandeTaxi demandeTaxi);

    @PUT("/demandetaxi/reclame")
    public Call<MessageResponse> reclamer(@Body DemandeTaxi demandeTaxi);
}
