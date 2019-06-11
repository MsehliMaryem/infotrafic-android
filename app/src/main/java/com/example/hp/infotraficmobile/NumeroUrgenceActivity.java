package com.example.hp.infotraficmobile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.hp.infotraficmobile.model.MessageResponse;
import com.example.hp.infotraficmobile.model.NumeroUrgence;
import com.example.hp.infotraficmobile.services.NumeroUrgenceService;
import com.example.hp.infotraficmobile.services.RegisterService;
import com.example.hp.infotraficmobile.utils.RetrofitUtil;

import java.util.List;

import retrofit2.Call;

public class NumeroUrgenceActivity extends AppCompatActivity {
    private EditText numUrgence ;
    private Button btnAPPel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numero_urgence);
        numUrgence=(EditText) findViewById(R.id.edittext);
        btnAPPel=(Button)findViewById(R.id.button);
        btnAPPel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+numUrgence.getText().toString())));
            }
        });
    }
    NumeroUrgenceService apiService = RetrofitUtil.getClient(getApplicationContext()).create(NumeroUrgenceService.class);

    Call<List<NumeroUrgence>> call = apiService.getAll();
}
