package com.example.hp.infotraficmobile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.hp.infotraficmobile.R;
import com.example.hp.infotraficmobile.model.DemandeTaxi;

import java.text.SimpleDateFormat;
import java.util.List;

public class MyClaimAdapter extends BaseAdapter {
    private  List<DemandeTaxi> demandeTaxiList;
    private Context context;

    public MyClaimAdapter( Context context,List<DemandeTaxi> demandeTaxiList) {
        this.demandeTaxiList = demandeTaxiList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return demandeTaxiList.size();
    }

    @Override
    public Object getItem(int position) {
        return demandeTaxiList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.claim_items, parent, false);
        }

        // get current item to be displayed
        DemandeTaxi currentItem = (DemandeTaxi) getItem(position);

        // get the TextView for item name and item description
        TextView textTaxi = (TextView)
                convertView.findViewById(R.id.txtTaxi);


        TextView textEtat = (TextView)
                convertView.findViewById(R.id.txtEtat);

        TextView textDate = (TextView)
                convertView.findViewById(R.id.txtDate);
        TextView textMatricule = (TextView)
                convertView.findViewById(R.id.txtMatricule);

        //sets the text for item name and item description from the current item object
        textMatricule.setText(currentItem.getTaxi().getMatricule());
        textEtat.setText(currentItem.getEtat());
        textTaxi.setText(currentItem.getTaxi().getMarque() +" "+ currentItem.getTaxi().getModele());


        textDate.setText(currentItem.getDate());
        // returns the view for the current row
        return convertView;
    }
}
