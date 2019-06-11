package com.example.hp.infotraficmobile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.hp.infotraficmobile.adapters.MyClaimAdapter;
import com.example.hp.infotraficmobile.model.ChauffeurTaxi;
import com.example.hp.infotraficmobile.model.DemandeTaxi;
import com.example.hp.infotraficmobile.model.MessageResponse;
import com.example.hp.infotraficmobile.model.Personne;
import com.example.hp.infotraficmobile.model.Taxi;
import com.example.hp.infotraficmobile.model.TypeAlerte;
import com.example.hp.infotraficmobile.model.TypeStation;
import com.example.hp.infotraficmobile.services.DemandeTaxiService;
import com.example.hp.infotraficmobile.services.TaxiService;
import com.example.hp.infotraficmobile.services.TypeAlerteService;
import com.example.hp.infotraficmobile.services.TypeStationService;
import com.example.hp.infotraficmobile.utils.RetrofitUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyClaimActivity extends Fragment {
    private  View view;
    private RadioGroup radioGroup;
     private DemandeTaxi demandeTaxi;
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = view.findViewById(R.id.listView);

        registerForContextMenu(listView);


       getDemande();


    }

    private void getDemande() {
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        final Personne personne = gson.fromJson(sharedPreferences.getString("user",""), Personne.class);


        DemandeTaxiService demandeTaxiService = RetrofitUtil.getClient(getContext()).create(DemandeTaxiService.class);
        Call<List<DemandeTaxi>> call= demandeTaxiService.getAllByAbonne(personne.getId());

        call.enqueue(new Callback<List<DemandeTaxi>>() {
            @Override
            public void onResponse(Call<List<DemandeTaxi>> call, Response<List<DemandeTaxi>> response) {
                MyClaimAdapter adapter = new MyClaimAdapter(getContext(), response.body());

                listView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<List<DemandeTaxi>> call, Throwable t) {
                t.printStackTrace();
                Log.e("erreur", t.toString());
                Toast.makeText(getActivity(), "Erreur de connexion", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.listView) {
            ListView lv = (ListView) v;
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
             demandeTaxi = (DemandeTaxi) lv.getItemAtPosition(acmi.position);
            menu.setHeaderTitle("Opérations");

            if(demandeTaxi.getEtat().equals("En attente")){
                menu.add(0,1,0,"Annuler");
            }
            if(demandeTaxi.getEtat().equals("Confirmée")) {
                menu.add(0,2,0,"Suivre mon taxi");

            }
            menu.add(0,3,0,"Réclamation");


        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

                annulerDemande(demandeTaxi);
                return true;
            case 2 :



                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("matricule", demandeTaxi.getTaxi().getMatricule());
                editor.commit();

                Fragment fragment = new MapsTraceTaxiActivity();
                if (fragment != null) {
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.commit();
                }
return  true;
            case 3 :

                openDialogueReclamation();
                return  true;
            default:
                return super.onContextItemSelected(item);
        }

    }

    private void openDialogueReclamation() {




        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
         view = inflater.inflate(R.layout.layout_dialog_reclamation, null);


         radioGroup = view.findViewById(R.id.radioGroup);
       RadioButton radLost = view.findViewById(R.id.radLost);
        RadioButton radMauvaise = view.findViewById(R.id.radMauvaise);
        RadioButton radPrix = view.findViewById(R.id.radPrix);
        RadioButton radOther= view.findViewById(R.id.radOther);
        final EditText editText = view.findViewById(R.id.txtCause);

        radLost.setChecked(true);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            int checked;
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                checked = checkedId;
                if (checkedId == R.id.radOther) {
                    //some code
                    editText.setVisibility(View.VISIBLE);

                } else  {
                    editText.setVisibility(View.GONE);
                }
            }
        });

        radLost.setChecked(true);



        builder.setView(view)
                .setTitle("Cause")
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Réclamer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                      String cause;
                      int selectedId = radioGroup.getCheckedRadioButtonId();
                        // find the radiobutton by returned id
                        RadioButton radioButton = (RadioButton) view.findViewById(selectedId);

                        if(radioButton.getId() == R.id.radOther) {
                            cause = editText.getText().toString();
                        }else {
                            cause=    radioButton.getText().toString();
                        }

                        demandeTaxi.setReclamation(cause);

                   DemandeTaxiService demandeTaxiService = RetrofitUtil.getClient(getContext()).create(DemandeTaxiService.class);

                        Call<MessageResponse> call = demandeTaxiService.reclamer(demandeTaxi);

                        call.enqueue(new Callback<MessageResponse>() {
                            @Override
                            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(Call<MessageResponse> call, Throwable t) {
                                Toast.makeText(getActivity(), "Erreur de connexion", Toast.LENGTH_LONG).show();
                            }
                        });



                    }
                });

        builder.create();
        builder.show();

    }
    private void annulerDemande(final DemandeTaxi demandeTaxi) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder
                .setMessage("Vous etes sur d' annuler  votre demande ")
                .setPositiveButton("Confirmer",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        DemandeTaxiService demandeTaxiService = RetrofitUtil.getClient(getContext()).create(DemandeTaxiService.class);
                        Call<MessageResponse> call = demandeTaxiService.annuler(demandeTaxi);
                        call.enqueue(new Callback<MessageResponse>() {
                            @Override
                            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                                getDemande();

                            }

                            @Override
                            public void onFailure(Call<MessageResponse> call, Throwable t) {
                                Toast.makeText(getActivity(), "Erreur de connexion", Toast.LENGTH_LONG).show();
                            }
                        });

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


}
