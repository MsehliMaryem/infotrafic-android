package com.example.hp.infotraficmobile;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.infotraficmobile.model.Alerte;
import com.example.hp.infotraficmobile.model.MessageResponse;
import com.example.hp.infotraficmobile.model.Personne;
import com.example.hp.infotraficmobile.model.SignalAlerte;
import com.example.hp.infotraficmobile.model.SignalAlerteId;
import com.example.hp.infotraficmobile.model.Station;
import com.example.hp.infotraficmobile.model.TypeAlerte;
import com.example.hp.infotraficmobile.model.TypeStation;
import com.example.hp.infotraficmobile.services.AlerteService;
import com.example.hp.infotraficmobile.services.SignalService;
import com.example.hp.infotraficmobile.services.StationService;
import com.example.hp.infotraficmobile.services.TypeAlerteService;
import com.example.hp.infotraficmobile.services.TypeStationService;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private int idTypeAleryte = 0;
    private Bitmap bitmap = null;
    List<Integer> typeAlerteList = new ArrayList<>();
    List<Integer> typeStationsList = new ArrayList<>();

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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        enableMyLocationIfPermitted();
        getAllAlerteAndStation();


        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                openDialogAlerte(latLng.latitude,latLng.longitude);
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(marker.getSnippet().equals("Alerte")){
                    Float zIndex = marker.getZIndex();
                    int id = zIndex.intValue();
                    openSignalDialog(id);
                }
                return false;
            }
        });


    }

    private void openDialog() {

        typeAlerteList = new ArrayList<>();
         typeStationsList = new ArrayList<>();


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
         View view = inflater.inflate(R.layout.layout_dialog, null);
        final LinearLayout line1 = view.findViewById(R.id.line1);
       final LinearLayout line2 = view.findViewById(R.id.line2);


        TypeStationService typeStationService = RetrofitUtil.getClient(getActivity()).create(TypeStationService.class);

        Call<List<TypeStation>> typeStationCall = typeStationService.getAll();



        typeStationCall.enqueue(new Callback<List<TypeStation>>() {
            @Override
            public void onResponse(Call<List<TypeStation>> call, Response<List<TypeStation>> response) {
                List<TypeStation> list = response.body();
                for(TypeStation typeStation: list) {
                    CheckBox ch = new CheckBox(getContext());
                    ch.setText(typeStation.getNomType());
                    ch.setTag(typeStation);
                    ch.setId( typeStation.getCode());
                    line1.addView(ch);
                    ch.setOnCheckedChangeListener(handleCheckTypeStation(ch));
                }

            }

            @Override
            public void onFailure(Call<List<TypeStation>> call, Throwable t) {
                Toast.makeText(getActivity(), "Erreur de telechargement", Toast.LENGTH_LONG).show();

            }
        });

        final TypeAlerteService typeAlerteService = RetrofitUtil.getClient(getActivity()).create(TypeAlerteService.class);

        Call<List<TypeAlerte>> typeAlerteServiceAll = typeAlerteService.getAll();



        typeAlerteServiceAll.enqueue(new Callback<List<TypeAlerte>>() {
            @Override
            public void onResponse(Call<List<TypeAlerte>> call, Response<List<TypeAlerte>> response) {
                List<TypeAlerte> list = response.body();
                for(TypeAlerte typeStation: list) {
                    CheckBox ch = new CheckBox(getContext());
                    ch.setText(typeStation.getNom());
                    ch.setTag(typeStation);
                    ch.setId( typeStation.getIdType());
                    line2.addView(ch);
                    ch.setOnCheckedChangeListener(handleCheckTypeAlerte(ch));
                }

            }

            @Override
            public void onFailure(Call<List<TypeAlerte>> call, Throwable t) {
                Toast.makeText(getActivity(), "Erreur de telechargement", Toast.LENGTH_LONG).show();

            }
        });

        builder.setView(view)
                .setTitle("Recherche Avancée")
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mMap.clear();
                        if(!typeAlerteList.isEmpty()){
                            getAlerte();
                        }

                        if(!typeStationsList.isEmpty()){
                            getStation();
                        }
                        if(typeStationsList.isEmpty() && typeAlerteList.isEmpty()){
                            getAllAlerteAndStation();
                        }
                    }
                });

         builder.create();
         builder.show();

    }

    private void getAlerte() {

        AlerteService apiService = RetrofitUtil.getClient(getActivity()).create(AlerteService.class);

        Call<List<Alerte>> call = apiService.findByType(toIntArray(typeAlerteList));


        call.enqueue(new Callback<List<Alerte>>() {
            @Override
            public void onResponse(Call<List<Alerte>> call, Response<List<Alerte>> response) {

                List<Alerte> list = response.body();

                    for (Alerte stat : list) {
                        try {


                            Bitmap bmImg = Ion.with(getContext())
                                    .load(RetrofitUtil.URL_SERVER + "upload/image?fileName=" + stat.getTypeAlerte().getPhoto()).asBitmap().get();


                            LatLng sydney = new LatLng(stat.getLatitude(), stat.getLongitude());
                            mMap.addMarker(new MarkerOptions().zIndex(stat.getIdAlerte()).snippet("Alerte").position(sydney).title(stat.getTypeAlerte().getNom())
                                    .icon(BitmapDescriptorFactory.fromBitmap(bmImg)));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "Erreur de telechargement", Toast.LENGTH_LONG).show();

                            Log.e("erreur ", e.toString());
                            e.printStackTrace();
                        }
                    }


            }

            @Override
            public void onFailure(Call<List<Alerte>> call, Throwable t) {
                Toast.makeText(getActivity(), "Erreur de connexion au serveur", Toast.LENGTH_LONG).show();

            }
        });

    }

    private void getStation() {


        StationService apiService = RetrofitUtil.getClient(getActivity()).create(StationService.class);

        Call<List<Station>> call = apiService.findByType(toIntArray(typeStationsList));


        call.enqueue(new Callback<List<Station>>() {
            @Override
            public void onResponse(Call<List<Station>> call, Response<List<Station>> response) {

                List<Station> list = response.body();

    for (Station stat : list) {
        try {


            Bitmap bmImg = Ion.with(getContext())
                    .load(RetrofitUtil.URL_SERVER + "upload/image?fileName=" + stat.getTypeStation().getPhoto()).asBitmap().get();


            LatLng sydney = new LatLng(stat.getLatitude(), stat.getLongitude());
            mMap.addMarker(new MarkerOptions().zIndex(stat.getCode()).snippet("Station")
                    .position(sydney).title(stat.getNom())
                    .icon(BitmapDescriptorFactory.fromBitmap(bmImg)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Erreur de telechargement", Toast.LENGTH_LONG).show();

            Log.e("erreur ", e.toString());
            e.printStackTrace();
        }
    }
}



            @Override
            public void onFailure(Call<List<Station>> call, Throwable t) {
                Toast.makeText(getActivity(), "Erreur de connexion au serveur", Toast.LENGTH_LONG).show();

            }
        });



    }


    private CompoundButton.OnCheckedChangeListener handleCheckTypeStation (final CheckBox chk)
    {
        return new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if(isChecked){
                 typeStationsList.add(   ((TypeStation)chk.getTag()).getCode());

                }
                else
                {

                    typeStationsList.remove( ((TypeStation)chk.getTag()).getCode());

                }
            }
        };
    }


    private CompoundButton.OnCheckedChangeListener handleCheckTypeAlerte (final CheckBox chk)
    {
        return new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if(isChecked){
                   typeAlerteList.add(((TypeAlerte)chk.getTag()).getIdType());

                }
                else
                {
                    typeAlerteList.remove(((TypeAlerte)chk.getTag()).getIdType());

                }
            }
        };
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
   private int[] toIntArray(List<Integer> list)  {
        int[] ret = new int[list.size()];
        int i = 0;
        for (Integer e : list)
            ret[i++] = e;
        return ret;
    }


  private void  getAllAlerteAndStation() {
    mMap.clear();
      StationService apiService = RetrofitUtil.getClient(getActivity()).create(StationService.class);

      Call<List<Station>> call = apiService.getAll();


      call.enqueue(new Callback<List<Station>>() {
          @Override
          public void onResponse(Call<List<Station>> call, Response<List<Station>> response) {

              List<Station> list = response.body();

              for (Station stat : list) {
                  try {


                      Bitmap bmImg = Ion.with(getContext())
                              .load(RetrofitUtil.URL_SERVER + "upload/image?fileName=" + stat.getTypeStation().getPhoto()).asBitmap().get();


                      LatLng sydney = new LatLng(stat.getLatitude(), stat.getLongitude());


                      mMap.addMarker(new MarkerOptions().zIndex(stat.getCode()).snippet("Station").position(sydney).title(stat.getNom())
                              .icon(BitmapDescriptorFactory.fromBitmap(bmImg)));
                      mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                  } catch (Exception e) {
                      Toast.makeText(getActivity(), "Erreur de telechargement", Toast.LENGTH_LONG).show();

                      Log.e("erreur ", e.toString());
                      e.printStackTrace();
                  }
              }


          }

          @Override
          public void onFailure(Call<List<Station>> call, Throwable t) {
              Toast.makeText(getActivity(), "Erreur de connexion au serveur", Toast.LENGTH_LONG).show();

          }
      });


      AlerteService alerteService = RetrofitUtil.getClient(getActivity()).create(AlerteService.class);

      Call<List<Alerte>> callAlert = alerteService.getAll();


      callAlert.enqueue(new Callback<List<Alerte>>() {
          @Override
          public void onResponse(Call<List<Alerte>> call, Response<List<Alerte>> response) {

              List<Alerte> list = response.body();

              for (Alerte stat : list) {
                  try {


                      Bitmap bmImg = Ion.with(getContext())
                              .load(RetrofitUtil.URL_SERVER + "upload/image?fileName=" + stat.getTypeAlerte().getPhoto()).asBitmap().get();


                      LatLng sydney = new LatLng(stat.getLatitude(), stat.getLongitude());
                      mMap.addMarker(new MarkerOptions().zIndex(stat.getIdAlerte()).snippet("Alerte").position(sydney).title(stat.getTypeAlerte().getNom())
                              .icon(BitmapDescriptorFactory.fromBitmap(bmImg)));
                      mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                  } catch (Exception e) {
                      Toast.makeText(getActivity(), "Erreur de telechargement", Toast.LENGTH_LONG).show();

                      Log.e("erreur ", e.toString());
                      e.printStackTrace();
                  }
              }


          }

          @Override
          public void onFailure(Call<List<Alerte>> call, Throwable t) {
              Toast.makeText(getActivity(), "Erreur de connexion au serveur", Toast.LENGTH_LONG).show();
              Log.e("errreurr ***", t.getMessage());
          }
      });


  }



    private void openDialogAlerte(final double latitude, final double longitude) {

      //  typeAlerteList = new ArrayList<>();
      //  typeStationsList = new ArrayList<>();


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_alerte, null);
        final LinearLayout line1 = view.findViewById(R.id.line1);




        final TypeAlerteService typeAlerteService = RetrofitUtil.getClient(getActivity()).create(TypeAlerteService.class);

        Call<List<TypeAlerte>> typeAlerteServiceAll = typeAlerteService.getAll();



        typeAlerteServiceAll.enqueue(new Callback<List<TypeAlerte>>() {
            @Override
            public void onResponse(Call<List<TypeAlerte>> call, Response<List<TypeAlerte>> response) {
                List<TypeAlerte> list = response.body();
                RadioGroup radioGroup = new RadioGroup(getContext());
                for(TypeAlerte typeStation: list) {
                    RadioButton ch = new RadioButton(getContext());
                    radioGroup.addView(ch);
                    ch.setText(typeStation.getNom());
                    ch.setTag(typeStation);
                    ch.setId( typeStation.getIdType());


                }

                line1.addView(radioGroup);
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        idTypeAleryte = checkedId;
                    }
                });
            }

            @Override
            public void onFailure(Call<List<TypeAlerte>> call, Throwable t) {
                Toast.makeText(getActivity(), "Erreur de telechargement", Toast.LENGTH_LONG).show();

            }
        });

        builder.setView(view)
                .setTitle("Ajouter une alerte")
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        AlerteService alerteService = RetrofitUtil.getClient(getContext()).create(AlerteService.class);

                      TypeAlerte typeAlerte = new TypeAlerte();
                      typeAlerte.setIdType(idTypeAleryte);
                        Alerte alerte = new Alerte();
                      alerte.setTypeAlerte(typeAlerte);
                      alerte.setLatitude(latitude);
                      alerte.setLongitude(longitude);

                        Gson gson = new Gson();

                        SharedPreferences sharedPreferences = getContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                        Personne personne = gson.fromJson(sharedPreferences.getString("user",""), Personne.class);
                      alerte.setPersonne(personne);

                        Call<MessageResponse> call = alerteService.ajouter(alerte);

                        call.enqueue(new Callback<MessageResponse>() {
                            @Override
                            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {



                            if(response.body().isSuccess()){
                                getAllAlerteAndStation();
                            }
                                Toast.makeText(getContext(), response.body().getMessage(),Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(Call<MessageResponse> call, Throwable t) {
                                Toast.makeText(getActivity(), "Erreur de telechargement", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });

        builder.create();
        builder.show();

    }



    private void openSignalDialog(final int idAlerte) {

        //  typeAlerteList = new ArrayList<>();
        //  typeStationsList = new ArrayList<>();


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_signal, null);
        final EditText editText = view.findViewById(R.id.txtCause);









        builder.setView(view)
                .setTitle("Signaler une alerte")
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {



                        Gson gson = new Gson();

                        SharedPreferences sharedPreferences = getContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                        Personne personne = gson.fromJson(sharedPreferences.getString("user",""), Personne.class);

                        SignalService signalService = RetrofitUtil.getClient(getActivity()).create(SignalService.class);
                        SignalAlerte signalAlerte = new SignalAlerte();
                        SignalAlerteId id = new SignalAlerteId();
                        id.setIdAlerte(idAlerte);
                        id.setIdPersonne(personne.getId());
                        signalAlerte.setCause(editText.getText().toString());
                        signalAlerte.setId(id);
                    Call<MessageResponse> call = signalService.signaler(signalAlerte);

                        call.enqueue(new Callback<MessageResponse>() {
                            @Override
                            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {

                                Toast.makeText(getContext(), response.body().getMessage(),Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(Call<MessageResponse> call, Throwable t) {
                                Toast.makeText(getActivity(), "Erreur de telechargement", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });

        builder.create();
        builder.show();

    }


}
