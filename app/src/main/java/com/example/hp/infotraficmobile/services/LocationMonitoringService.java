package com.example.hp.infotraficmobile.services;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.hp.infotraficmobile.model.MessageResponse;
import com.example.hp.infotraficmobile.model.Personne;
import com.example.hp.infotraficmobile.model.Taxi;
import com.example.hp.infotraficmobile.utils.RetrofitUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationMonitoringService extends Service implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private Taxi taxi;
    public  static Location location ;
    public static final int LOCATION_INTERVAL = 10000;
    public static final int FASTEST_LOCATION_INTERVAL = 5000;
    public static final String ACTION_LOCATION_BROADCAST = LocationMonitoringService.class.getName() + "LocationBroadcast";
    public static final String EXTRA_LATITUDE = "extra_latitude";
    public static final String EXTRA_LONGITUDE = "extra_longitude";
    private static final String TAG = LocationMonitoringService.class.getSimpleName();
    GoogleApiClient mLocationClient;
    LocationRequest mLocationRequest = new LocationRequest();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mLocationClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        Toast.makeText(getApplicationContext(), "start service", Toast.LENGTH_LONG).show();
        mLocationRequest.setInterval(LOCATION_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_LOCATION_INTERVAL);


        int priority = LocationRequest.PRIORITY_HIGH_ACCURACY; //by default
        //PRIORITY_BALANCED_POWER_ACCURACY, PRIORITY_LOW_POWER, PRIORITY_NO_POWER are the other priority modes


        mLocationRequest.setPriority(priority);
        mLocationClient.connect();

        //Make it stick to the notification panel so it is less prone to get cancelled by the Operating System.
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*
     * LOCATION CALLBACKS
     */
    @Override
    public void onConnected(Bundle dataBundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            Log.d(TAG, "== Error On onConnected() Permission not granted");
            //Permission not granted by user so cancel the further execution.

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mLocationClient, mLocationRequest, this);

        Log.d(TAG, "Connected to Google API");
    }

    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Connection suspended");
    }

    //to get the location change
    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Location changed");


        if (location != null) {
            Log.d(TAG, "== location != null");
            this.location = location;
Toast.makeText(getApplicationContext(), location.getLatitude() +"***"+location.getLongitude(),Toast.LENGTH_LONG).show();
            //Send result to activities
            sendMessageToUI(location.getLatitude(),location.getLongitude());
        }
    }

    private void sendMessageToUI(double lat, double lng) {

        Log.d(TAG, "Sending info...");

       /* Intent intent = new Intent(ACTION_LOCATION_BROADCAST);
        intent.putExtra(EXTRA_LATITUDE, lat);
        intent.putExtra(EXTRA_LONGITUDE, lng);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);*/


        final Gson gson = new Gson();
        final SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);


        taxi = gson.fromJson(sharedPreferences.getString("taxi",""), Taxi.class);
        TaxiService taxiService = RetrofitUtil.getClient(getApplicationContext()).create(TaxiService.class);
        if(taxi == null){
           final Personne personne = gson.fromJson(sharedPreferences.getString("user",""), Personne.class);



           Call<Taxi> call = taxiService.findByChauffeur(personne.getId());

           call.enqueue(new Callback<Taxi>() {
               @Override
               public void onResponse(Call<Taxi> call, Response<Taxi> response) {
                   Taxi taxii = response.body();


                   if(taxii != null) {

                       SharedPreferences.Editor editor = sharedPreferences.edit();


                       editor.putString("taxi",  gson.toJson(taxii));
                       editor.commit();

                      taxi = taxii;
                   }

               }

               @Override
               public void onFailure(Call<Taxi> call, Throwable t) {

               }
           });
       }


       if(taxi != null) {
           taxi.setLatitude(lat);
           taxi.setLongitude(lng);
           Call<MessageResponse> call = taxiService.modifier(taxi);
           call.enqueue(new Callback<MessageResponse>() {
               @Override
               public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {

               }

               @Override
               public void onFailure(Call<MessageResponse> call, Throwable t) {

               }
           });

       }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Failed to connect to Google API");

    }
}