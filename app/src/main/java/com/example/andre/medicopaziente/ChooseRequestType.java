package com.example.andre.medicopaziente;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class ChooseRequestType extends AppCompatActivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_request_type);

        toolbar = (Toolbar) findViewById(R.id.request_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final Paziente paziente;
        Bundle bundle = getIntent().getExtras();
        paziente = bundle.getParcelable("Paziente");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Button farmacoButton = (Button)findViewById(R.id.farmacoButton);
        farmacoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FarmacoSendRequest.class);
                intent.putExtra("Paziente", paziente);
                startActivity(intent);
            }
        });
        Button visitaButton = (Button)findViewById(R.id.visitaButton);
        visitaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), VisitaSendRequest.class);
                intent.putExtra("Paziente", paziente);
                startActivity(intent);
            }
        });
    }
}
