package com.example.hp.infotraficmobile;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hp.infotraficmobile.model.ChauffeurTaxi;
import com.example.hp.infotraficmobile.model.MessageResponse;
import com.example.hp.infotraficmobile.model.Personne;
import com.example.hp.infotraficmobile.model.Taxi;
import com.example.hp.infotraficmobile.services.TaxiService;
import com.example.hp.infotraficmobile.utils.RetrofitUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaxiActivity extends Fragment {

    private GoogleMap mMap;
    private EditText editMatricule,editMarque, editModel, editNum;
    private Button btnAdd, btnEdit;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_taxi, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        editMatricule = view.findViewById(R.id.editMatricule);
        editNum = view.findViewById(R.id.editNum);
        editMarque = view.findViewById(R.id.editMarque);
        editModel = view.findViewById(R.id.editModel);

        btnAdd = view.findViewById(R.id.btnAdd);
        btnEdit = view.findViewById(R.id.btnEdit);

        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        final Personne personne = gson.fromJson(sharedPreferences.getString("user",""), Personne.class);

        TaxiService taxiService = RetrofitUtil.getClient(getContext()).create(TaxiService.class);

       Call<Taxi> call = taxiService.findByChauffeur(personne.getId());

       call.enqueue(new Callback<Taxi>() {
           @Override
           public void onResponse(Call<Taxi> call, Response<Taxi> response) {
               Taxi taxi = response.body();


                    if(taxi == null) {
                        btnAdd.setVisibility(View.VISIBLE);
                        btnEdit.setVisibility(View.GONE);
                    }else {
                        btnAdd.setVisibility(View.GONE);
                        btnEdit.setVisibility(View.VISIBLE);
                        editMarque.setText(taxi.getMarque());
                        editModel.setText(taxi.getModele());
                        editMatricule.setText(taxi.getMatricule());
                        editNum.setText(taxi.getNumTaxi());
                    }

           }

           @Override
           public void onFailure(Call<Taxi> call, Throwable t) {
               Toast.makeText(getActivity(), "Erreur de connexion", Toast.LENGTH_LONG).show();
           }
       });


       btnAdd.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Taxi taxi = new Taxi();
               ChauffeurTaxi chauffeurTaxi = new ChauffeurTaxi();
               chauffeurTaxi.setId(personne.getId());
               taxi.setChauffeurTaxi(chauffeurTaxi);
               taxi.setMatricule(editMatricule.getText().toString());
               taxi.setNumTaxi(editNum.getText().toString());
               taxi.setMarque(editMarque.getText().toString());
               taxi.setModele(editModel.getText().toString());
               addTaxi(taxi);
           }
       });


        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Taxi taxi = new Taxi();
                ChauffeurTaxi chauffeurTaxi = new ChauffeurTaxi();
                chauffeurTaxi.setId(personne.getId());
                taxi.setChauffeurTaxi(chauffeurTaxi);
                taxi.setMatricule(editMatricule.getText().toString());
                taxi.setNumTaxi(editNum.getText().toString());
                taxi.setMarque(editMarque.getText().toString());
                taxi.setModele(editModel.getText().toString());
                updateTaxi(taxi);
            }
        });
    }



private  void addTaxi(Taxi taxi){
    TaxiService taxiService = RetrofitUtil.getClient(getContext()).create(TaxiService.class);

    Call<MessageResponse> call = taxiService.ajouter(taxi);
    call.enqueue(new Callback<MessageResponse>() {
        @Override
        public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
            Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();
            if(response.body().isSuccess()){
                btnAdd.setVisibility(View.GONE);
                btnEdit.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onFailure(Call<MessageResponse> call, Throwable t) {
            Toast.makeText(getActivity(), "Erreur de connexion", Toast.LENGTH_LONG).show();
        }
    });
}


    private  void updateTaxi(Taxi taxi){
        TaxiService taxiService = RetrofitUtil.getClient(getContext()).create(TaxiService.class);

        Call<MessageResponse> call = taxiService.modifier(taxi);
        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                if(response.body().isSuccess()){
                    btnAdd.setVisibility(View.GONE);
                    btnEdit.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Erreur de connexion", Toast.LENGTH_LONG).show();
            }
        });
    }





}
