package com.example.hp.infotraficmobile;

import android.Manifest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
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
import android.widget.Toast;

import com.example.hp.infotraficmobile.adapters.CustomInfoWindowGoogleMap;
import com.example.hp.infotraficmobile.model.Abonnee;
import com.example.hp.infotraficmobile.model.DemandeTaxi;
import com.example.hp.infotraficmobile.model.MessageResponse;
import com.example.hp.infotraficmobile.model.Personne;
import com.example.hp.infotraficmobile.model.Taxi;
import com.example.hp.infotraficmobile.services.DemandeTaxiService;
import com.example.hp.infotraficmobile.services.LocationMonitoringService;
import com.example.hp.infotraficmobile.services.TaxiService;
import com.example.hp.infotraficmobile.utils.RetrofitUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsTaxiActivity extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_maps, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);

        fab.setVisibility(View.GONE);


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        enableMyLocationIfPermitted();






        mMap.clear();
        TaxiService apiService = RetrofitUtil.getClient(getActivity()).create(TaxiService.class);

        Call<List<Taxi>> call = apiService.findDisponible();


        call.enqueue(new Callback<List<Taxi>>() {
            @Override
            public void onResponse(Call<List<Taxi>> call, Response<List<Taxi>> response) {

                List<Taxi> list = response.body();

                for (Taxi stat : list) {
                    try {


                        Bitmap bmImg = Ion.with(getContext())
                                .load(RetrofitUtil.URL_SERVER + "upload/image?fileName=taxi.png" ).asBitmap().get();


                        LatLng sydney = new LatLng(stat.getLatitude(), stat.getLongitude());


                       Marker marker =  mMap.addMarker(new MarkerOptions().position(sydney).
                                title(stat.getChauffeurTaxi().getNom() +" "+stat.getChauffeurTaxi().getPrenom() ).
                                snippet(stat.getMarque() + " " + stat.getModele() +
                                        "\n "+stat.getMatricule())
                                .icon(BitmapDescriptorFactory.fromBitmap(bmImg)));


                        CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(getContext());
                        mMap.setInfoWindowAdapter(customInfoWindow);

                        marker.setTag(stat);
                       // marker.showInfoWindow();
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Erreur de telechargement", Toast.LENGTH_LONG).show();

                        Log.e("erreur ", e.toString());
                        e.printStackTrace();
                    }
                }


            }

            @Override
            public void onFailure(Call<List<Taxi>> call, Throwable t) {
                Toast.makeText(getActivity(), "Erreur de connexion au serveur", Toast.LENGTH_LONG).show();

            }
        });
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(final Marker marker) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder
                        .setMessage("Vous etes sur de valider votre demande ")
                        .setPositiveButton("Confirmer",  new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                Taxi taxi = (Taxi) marker.getTag();
                                demanderTaxi(taxi);

                            }
                        })
                        .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        });

    }

    private void enableMyLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }
    }




   private void demanderTaxi(Taxi taxi) {


       Gson gson = new Gson();
       SharedPreferences sharedPreferences = getContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
       Personne personne = gson.fromJson(sharedPreferences.getString("user",""), Personne.class);

       DemandeTaxi demandeTaxi = new DemandeTaxi();
       demandeTaxi.setTaxi(taxi);

       Abonnee abonnee = new Abonnee();
       abonnee.setId(personne.getId());
       demandeTaxi.setAbonnee(abonnee);
      Location location = LocationMonitoringService.location;
      if(location == null){
          demandeTaxi.setLatitude(0);
          demandeTaxi.setLongitude(0);
      }else {

          demandeTaxi.setLatitude(LocationMonitoringService.location.getLatitude());
          demandeTaxi.setLongitude(LocationMonitoringService.location.getLongitude());
      }

       DemandeTaxiService apiService = RetrofitUtil.getClient(getActivity()).create(DemandeTaxiService.class);

 Call<MessageResponse> call = apiService.ajouter(demandeTaxi);
 call.enqueue(new Callback<MessageResponse>() {
     @Override
     public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {

         Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();

     }

     @Override
     public void onFailure(Call<MessageResponse> call, Throwable t) {
         Toast.makeText(getActivity(), "Erreur de connexion au serveur", Toast.LENGTH_LONG).show();

     }
 });
   }






}
