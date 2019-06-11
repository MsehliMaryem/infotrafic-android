package com.example.hp.infotraficmobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.hp.infotraficmobile.model.Abonnee;
import com.example.hp.infotraficmobile.model.ChauffeurTaxi;
import com.example.hp.infotraficmobile.model.MessageResponse;
import com.example.hp.infotraficmobile.services.RegisterService;
import com.example.hp.infotraficmobile.utils.RetrofitUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText editNom, editPrenom, editEmail, editTel, editLogin, editPwd, editConfPwd,editNumPermis;
    private Button btnSubscribe;
    private RadioButton radioABonnee, radioChauffeur;
    private RadioGroup radioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        editNom = findViewById(R.id.editNom);
        editPrenom= findViewById(R.id.editPrenom);
        editEmail=findViewById(R.id.editEmail);
        editLogin=findViewById(R.id.editLogin);
        editTel=findViewById(R.id.editTel);
        editPwd=findViewById(R.id.editPassword);
        editConfPwd=findViewById(R.id.editConfirmPassword);
        editNumPermis = findViewById(R.id.editNumPermis);
        btnSubscribe=findViewById(R.id.btnSubscribe);
        radioABonnee = findViewById(R.id.radioAbonne);
        radioChauffeur = findViewById(R.id.radioChauffeur);

        editNumPermis.setVisibility(View.GONE);
        radioABonnee.setChecked(true);
         radioGroup = findViewById(R.id.radioGroup);

         radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(RadioGroup group, int checkedId) {
                 if(checkedId == R.id.radioAbonne){
                     editNumPermis.setVisibility(View.GONE);
                 }else{
                     editNumPermis.setVisibility(View.VISIBLE);
                 }
             }
         });

        btnSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean cancel = false;
                View focusView = null;
                String nom =editNom.getText().toString();
                String prenom =editPrenom.getText().toString();
                String email=editEmail.getText().toString();
                String telephone=editTel.getText().toString();
                String login=editLogin.getText().toString();
                String pwd=editPwd.getText().toString();
                String confPwd = editConfPwd.getText().toString();
                String numPermis = editNumPermis.getText().toString();
                if (TextUtils.isEmpty(nom) ) {
                    editNom.setError("Nom réquis");
                    focusView = editNom;
                    cancel = true;
                }

              else  if (TextUtils.isEmpty(prenom) ) {
                    editPrenom.setError("Prenom réquis");
                    focusView = editPrenom;
                    cancel = true;
                }
                else     if (TextUtils.isEmpty(email) ) {
                    editEmail.setError("Email réquis");
                    focusView = editEmail;
                    cancel = true;
                }
                else    if (TextUtils.isEmpty(telephone) ) {
                    editTel.setError("Telephone réquis");
                    focusView = editTel;
                    cancel = true;
                }
                else  if (TextUtils.isEmpty(login) ) {
                    editLogin.setError("Login réquis");
                    focusView = editLogin;
                    cancel = true;
                }
                else   if (TextUtils.isEmpty(pwd) ) {
                    editPwd.setError("mot de passe réquis");
                    focusView = editPwd;
                    cancel = true;
                }
                else     if (TextUtils.isEmpty(confPwd) ) {
                    editConfPwd.setError("confirmer mot de passe réquis");
                    focusView = editConfPwd;
                    cancel = true;
                }

                else    if (!pwd.equals(confPwd) ) {
                    editConfPwd.setError("Les mot de passe ne sont pas identiques");
                    focusView = editConfPwd;
                    cancel = true;
                }
                else  if(radioChauffeur.isChecked()){
                    if (TextUtils.isEmpty(numPermis) ) {
                        editNumPermis.setError("Numéro permis réquis");
                        focusView = editNumPermis;
                        cancel = true;
                    }
                }

                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                } else {

                    if(radioABonnee.isChecked()) {
                        Abonnee abonnee = new Abonnee();
                        abonnee.setNom(nom);
                        abonnee.setPrenom(prenom);
                        abonnee.setEmail(email);
                        abonnee.setTelephone(telephone);
                        abonnee.setLogin(login);
                        abonnee.setPassword(pwd);
                        registerAbonnee(abonnee);

                    }
                    else {
                        ChauffeurTaxi chauffeurTaxi= new ChauffeurTaxi();
                        chauffeurTaxi.setNom(nom);
                        chauffeurTaxi.setPrenom(prenom);
                        chauffeurTaxi.setEmail(email);
                        chauffeurTaxi.setTelephone(telephone);
                        chauffeurTaxi.setLogin(login);
                        chauffeurTaxi.setPassword(pwd);
                        chauffeurTaxi.setNumPermis(Long.parseLong(numPermis));
                        registerChauffeur(chauffeurTaxi);
                }
            }

    }});


}



  private void  registerAbonnee(Abonnee abonnee){

      RegisterService apiService = RetrofitUtil.getClient(getApplicationContext()).create(RegisterService.class);

    Call<MessageResponse> call=    apiService.registerAbonne(abonnee);

      call.enqueue(new Callback<MessageResponse>() {
          @Override
          public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {


              Toast.makeText(getApplicationContext(),response.body().getMessage(), Toast.LENGTH_LONG).show();
              if(response.body().isSuccess()){
                  startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                  finish();
              }
          }

          @Override
          public void onFailure(Call<MessageResponse> call, Throwable t) {
              Toast.makeText(getApplicationContext(),"Erreur de connexion au serveur", Toast.LENGTH_LONG).show();
          }
      });
    }



    private void  registerChauffeur(ChauffeurTaxi chauffeurTaxi){

        RegisterService apiService = RetrofitUtil.getClient(getApplicationContext()).create(RegisterService.class);

        Call<MessageResponse> call=    apiService.registerChauffeur(chauffeurTaxi);

        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {


                Toast.makeText(getApplicationContext(),response.body().getMessage(), Toast.LENGTH_LONG).show();
            if(response.body().isSuccess()){
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Erreur de connexion au serveur", Toast.LENGTH_LONG).show();
            }
        });
    }
}
