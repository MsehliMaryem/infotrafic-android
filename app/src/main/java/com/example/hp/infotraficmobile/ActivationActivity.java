package com.example.hp.infotraficmobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;
import com.example.hp.infotraficmobile.model.MessageResponse;
import com.example.hp.infotraficmobile.model.Personne;
import com.example.hp.infotraficmobile.services.LoginService;
import com.example.hp.infotraficmobile.utils.RetrofitUtil;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivationActivity extends AppCompatActivity {
    private EditText editCode;
    private Button btnActivate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation);
        editCode = findViewById(R.id.code);
        btnActivate = findViewById(R.id.btnActivate);


        btnActivate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {



                SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                String token = sharedPreferences.getString("token","");
                Gson gson = new Gson();


                JWT jwt = new JWT(token);
                Claim claim = jwt.getClaim("user");
                Personne personne = gson.fromJson(claim.asString(), Personne.class);
                personne.setCode(editCode.getText().toString());
                LoginService apiService = RetrofitUtil.getClient(getApplicationContext()).create(LoginService.class);
               Call<MessageResponse> call = apiService.activate(personne);

               call.enqueue(new Callback<MessageResponse>() {
                   @Override
                   public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {

                       Toast.makeText(getApplicationContext(),response.body().getMessage(), Toast.LENGTH_LONG).show();

                       if(response.body().isSuccess()){
                           startActivity(new Intent(getApplicationContext(), MainActivity.class));
                           finish();
                       }




                   }

                   @Override
                   public void onFailure(Call<MessageResponse> call, Throwable t) {
                       Toast.makeText(getApplicationContext(),"Erreur de connexion au serveur", Toast.LENGTH_LONG).show();
                   }
               });
            }
        });
    }
}
