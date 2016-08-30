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
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.example.damiano.myapplication.backend.messaging.Messaging;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Damiano on 23/06/2016.
 */
public class FarmacoSendRequest extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private Toolbar toolbar;
    private Paziente paziente;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_request_farmaco);

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

        final NumberPicker medicineAmount = (NumberPicker) findViewById(R.id.medicine_number_picker);
        medicineAmount.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        medicineAmount.setMinValue(1);
        medicineAmount.setMaxValue(10);
        medicineAmount.setWrapSelectorWheel(true);

        final EditText medicineName = (EditText)findViewById(R.id.textFarmaco);
        final EditText noteFarmaco = (EditText) findViewById(R.id.note_farmaco);
        noteFarmaco.setHorizontallyScrolling(false);
        noteFarmaco.setMaxLines(5);
        Button sendRequest = (Button)findViewById(R.id.sendRequest);

        Intent intent = getIntent();
        paziente = intent.getExtras().getParcelable("Paziente");
        final String cf_paziente = paziente.getCodiceFiscale();
        final String cf_medico = paziente.getMedico();;

        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (medicineName.getText().toString().length()==0) {
                    AlertDialog alertDialog = new AlertDialog.Builder(FarmacoSendRequest.this).create();
                    alertDialog.setTitle("ATTENZIONE");
                    alertDialog.setMessage("Specificare il nome completo del medicinale da prescrivere");
                    alertDialog.show();
                    return;
                }
                final Integer medicineNum = medicineAmount.getValue();
                final String medicine = medicineName.getText().toString();
                final String notes = noteFarmaco.getText().toString();
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd 'alle' HH:mm:ss");
                final String formattedDate = df.format(c.getTime());

                new AsyncCallSoap().execute(formattedDate,"Prescrizione", notes, medicine, medicineNum.toString(), cf_paziente, cf_medico);
            }
        });


    }


    public class AsyncCallSoap extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            try
            {
                progressDialog = new ProgressDialog(FarmacoSendRequest.this);
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
                    String dest = params[6] + "p2m"; //"p2m" stands for "patient to medical doctor", to identfy the type of message
                    String farmaco = params[3];
                    String paziente = params[5];
                    String message = dest.concat(farmaco);
                    message = message.concat(" richiesto dal paziente: " + paziente);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(FarmacoSendRequest.this);
                builder.setTitle("Operazione eseguita");
                builder.setMessage("Richiesta inviata");
                builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent newPage = new Intent(FarmacoSendRequest.this, HomeActivity.class);
                        newPage.putExtra("Paziente", paziente);
                        FarmacoSendRequest.this.finish();
                        startActivity(newPage);
                    }
                });
                AlertDialog dialog= builder.create();
                progressDialog.dismiss();
                dialog.show();
            }
            else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(FarmacoSendRequest.this);
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

}
