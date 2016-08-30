package com.example.andre.medicopaziente;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.damiano.myapplication.backend.messaging.Messaging;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Damiano on 23/06/2016.
 */
public class VisitaSendRequest extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private EditText visitaSpecialistica;

    private ProgressDialog progressDialog;
    private Toolbar toolbar;
    private Paziente paziente;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_request_visita);

        toolbar = (Toolbar) findViewById(R.id.farmaco_request_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        final Spinner spinner = (Spinner) findViewById(R.id.visita_spinner);
        spinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.visita_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        visitaSpecialistica = (EditText)findViewById(R.id.textVisitaSpecialistica);
        final EditText noteVisita = (EditText) findViewById(R.id.note_farmaco);
        noteVisita.setHorizontallyScrolling(false);
        noteVisita.setMaxLines(5);
        Button sendRequest = (Button)findViewById(R.id.sendRequest);

        Intent intent = getIntent();
        paziente = intent.getExtras().getParcelable("Paziente");
        final String cf_paziente = paziente.getCodiceFiscale();
        final String cf_medico = paziente.getMedico();;

        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (visitaSpecialistica.getText().toString().length()==0) {
                    AlertDialog alertDialog = new AlertDialog.Builder(VisitaSendRequest.this).create();
                    alertDialog.setTitle("ATTENZIONE");
                    alertDialog.setMessage("Specificare il nome della visita specialistica da prescrivere...");
                    alertDialog.show();
                    return;
                }
                final String nomeCompletoVisita;
                if (spinner.getSelectedItem().toString().equals("Visita specialistica")){
                    nomeCompletoVisita = "Visita specialistica: " + visitaSpecialistica.getText().toString();
                }else{
                    nomeCompletoVisita = "Visita di controllo";
                }
                String tipo_richiesta;
                if (spinner.getSelectedItem().equals("Visita specialistica")){
                    tipo_richiesta = "Visita specialistica";
                } else {
                    tipo_richiesta = "Visita di controllo";
                }
                final String notes = noteVisita.getText().toString();
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd 'alle' HH:mm:ss");
                final String formattedDate = df.format(c.getTime());

                new AsyncCallSoap().execute(formattedDate,tipo_richiesta, notes, nomeCompletoVisita, "1", cf_paziente, cf_medico);
            }
        });
    }


    public class AsyncCallSoap extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            try
            {
                progressDialog = new ProgressDialog(VisitaSendRequest.this);
                progressDialog.setIndeterminate(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setTitle("Attendere");
                progressDialog.setMessage("Invio richiesta in corso...");
                progressDialog.show();
            } catch (Exception ex)
            {
                ex.getMessage().toString();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String response;
            CallSoap CS = new CallSoap();
            response = CS.Richiesta(params[0],params[1],params[2],params[3],Integer.parseInt(params[4]),params[5],params[6]);
            if(response.equals("1")){
                try {
                    String dest = params[6] + "p2m";
                    String visita = params[3];
                    String paziente = params[5];
                    String message = dest.concat(visita);
                    message = message.concat(" richiesta dal paziente: " + paziente);
                    Messaging.Builder builder = new Messaging.Builder(AndroidHttp.newCompatibleTransport(),
                            new AndroidJsonFactory(), null)
                            .setRootUrl("https://pazientemedico.appspot.com/_ah/api/");
                    Messaging messaging = builder.build();
                    try {
                        messaging.sendMessage(message).execute();
                    }catch(Exception e){
                        Log.e("MedicoPaziente", "Exception :", e);
                    }
                } catch (Exception e) {
                    Log.e("MedicoPaziente", "Exception :", e);
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s)
        {
            if(s.equals("1"))
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(VisitaSendRequest.this);
                boolean isSpecialistica;
                if (visitaSpecialistica.getVisibility()==View.VISIBLE){
                    isSpecialistica=true;
                }else{
                    isSpecialistica=false;
                }
                builder.setTitle("Operazione eseguita");
                if(isSpecialistica) {
                    builder.setMessage("Richiesta inviata. Ricorda che la ricetta originale della visita specialistica deve essere comunque ritirata manualmente presso il proprio medico per poter avere accesso alle prestazioni.");
                }else{
                    builder.setMessage("Richiesta inviata");
                }
                builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent newPage = new Intent(VisitaSendRequest.this, HomeActivity.class);
                        newPage.putExtra("Paziente", paziente);
                        VisitaSendRequest.this.finish();
                        startActivity(newPage);
                    }
                });
                AlertDialog dialog= builder.create();
                progressDialog.dismiss();
                dialog.show();
            }
            else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(VisitaSendRequest.this);
                builder.setTitle("Errore");
                builder.setMessage("Operazione non eseguita, riprovare...");
                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog= builder.create();
                progressDialog.dismiss();
                dialog.show();
            }


        }

    }
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        String item = (String)parent.getItemAtPosition(pos);
        if(item.equals("Visita specialistica")){
            visitaSpecialistica.setVisibility(View.VISIBLE);
        }else{
            visitaSpecialistica.setVisibility(View.GONE);
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
    }

}

