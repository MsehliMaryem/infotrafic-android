package com.example.hp.infotraficmobile;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsTraceTaxiActivity extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private int mInterval = 5000; // 5 seconds by default, can be changed later
    private Handler mHandler;

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

        mHandler = new Handler();
        startRepeatingTask();



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


    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                String matricule = sharedPreferences.getString("matricule","");


                mMap.clear();
                TaxiService apiService = RetrofitUtil.getClient(getActivity()).create(TaxiService.class);

                Call<Taxi> call = apiService.findByMatricule(matricule);


                call.enqueue(new Callback<Taxi>() {
                    @Override
                    public void onResponse(Call<Taxi> call, Response<Taxi> response) {

                        Taxi taxi = response.body();


                        try {


                            Bitmap bmImg = Ion.with(getContext())
                                    .load(RetrofitUtil.URL_SERVER + "upload/image?fileName=taxi.png" ).asBitmap().get();


                            LatLng sydney = new LatLng(taxi.getLatitude(), taxi.getLongitude());


                            Marker marker =  mMap.addMarker(new MarkerOptions().position(sydney).
                                    title(taxi.getChauffeurTaxi().getNom() +" "+taxi.getChauffeurTaxi().getPrenom() ).
                                    snippet(taxi.getMarque() + " " + taxi.getModele() +
                                            "\n "+taxi.getMatricule())
                                    .icon(BitmapDescriptorFactory.fromBitmap(bmImg)));


                            CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(getContext());
                            mMap.setInfoWindowAdapter(customInfoWindow);

                            marker.setTag(taxi);
                            // marker.showInfoWindow();
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(sydney.latitude, sydney.longitude), 12.0f));
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "Erreur de telechargement", Toast.LENGTH_LONG).show();

                            Log.e("erreur ", e.toString());
                            e.printStackTrace();
                        }



                    }

                    @Override
                    public void onFailure(Call<Taxi> call, Throwable t) {
                        Toast.makeText(getActivity(), "Erreur de connexion au serveur", Toast.LENGTH_LONG).show();

                    }
                });
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };




    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
    }






}
