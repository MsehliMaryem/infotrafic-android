package com.example.hp.infotraficmobile.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hp.infotraficmobile.R;
import com.example.hp.infotraficmobile.model.Taxi;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public CustomInfoWindowGoogleMap(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.map_custom_infowindow, null);

        TextView name = view.findViewById(R.id.txtName);
        TextView taxi = view.findViewById(R.id.txtTaxi);



        name.setText(marker.getTitle());
        taxi.setText(marker.getSnippet());

        Taxi infoWindowData = (Taxi) marker.getTag();




        return view;
    }
}