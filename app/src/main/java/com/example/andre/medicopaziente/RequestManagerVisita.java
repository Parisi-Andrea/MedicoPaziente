package com.example.andre.medicopaziente;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.damiano.myapplication.backend.messaging.Messaging;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class RequestManagerVisita extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private Toolbar toolbar;
    Paziente paziente;
    ImageView image;
    TextView nome_paziente;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if( getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_request_manager_visita);
        }else{
            setContentView(R.layout.activity_request_manager_visita_land);
        }
        nome_paziente = (TextView) findViewById(R.id.patient_name);
        image = (ImageView) findViewById(R.id.photo);


        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        final Richiesta richiesta = intent.getParcelableExtra("richiesta");

        // guardo se le informazioni del paziente sono nel db interno, altrimenti guardo nel server remoto
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        paziente = db.getPaziente(richiesta.cf_paziente);
        if(paziente==null) {
            try {
                new AsyncCallSoap().execute(richiesta.getCf_paziente());
            } catch (Exception e) {
            }
        }

        if(paziente!=null && paziente.getCodiceFiscale()!=null) {
            String fotoPaziente = paziente.getImage();
            Utils utils = new Utils();
            Bitmap photo = utils.stringToBitmap(fotoPaziente);
            image.setImageBitmap(photo);
        }

        ImageView dettagliPaziente = (ImageView) findViewById(R.id.info_button);
        dettagliPaziente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(paziente!=null && paziente.getCodiceFiscale()!=null) {
                    Intent intent = new Intent(v.getContext(), DettagliPaziente.class);
                    intent.putExtra("Paziente", paziente);
                    startActivity(intent);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RequestManagerVisita.this);
                    builder.setTitle("Errore");
                    builder.setMessage("Dettagli paziente non disponibili");
                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog dialog= builder.create();
                    dialog.show();
                }
            }
        });

        if(paziente!=null && paziente.getCodiceFiscale()!=null) {
            nome_paziente.setText(paziente.getNome() + " " + paziente.getCognome());
        }

        TextView cf_paziente = (TextView) findViewById(R.id.patient_id);
        cf_paziente.setText(richiesta.getCf_paziente());

        TextView dataRichiesta = (TextView) findViewById(R.id.requestDate);
        dataRichiesta.setText(richiesta.getData_richiesta());

        TextView tipoVisitaRichiesta = (TextView) findViewById(R.id.tipo_visita_richiesta);
        tipoVisitaRichiesta.setText(richiesta.getNome_farmaco());

        TextView nomeVisitaSpecialistica = (TextView) findViewById(R.id.nome_visita_specialistica);
        nomeVisitaSpecialistica.setText(String.valueOf(richiesta.getQuantita_farmaco()));

        TextView patientId = (TextView) findViewById(R.id.patient_id);
        patientId.setText(richiesta.getCf_paziente());

        final TextView noteVisita = (TextView) findViewById(R.id.request_notes);
        noteVisita.setText(richiesta.getNote_richiesta());
        noteVisita.setMovementMethod(new ScrollingMovementMethod());
        noteVisita.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                noteVisita.getParent().requestDisallowInterceptTouchEvent(true);

                return false;
            }
        });

        if(richiesta.getTipo().equals("C"/*visita di controllo*/)){
            findViewById(R.id.layout_specialistica1).setVisibility(View.GONE); // rimozione campo per il nome della visita specialistica
            findViewById(R.id.layout_specialistica2).setVisibility(View.GONE);
            findViewById(R.id.break1).setVisibility(View.GONE);
            findViewById(R.id.break2).setVisibility(View.GONE);
        }

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        Button accetta = (Button)findViewById(R.id.accetta_richiesta);
        accetta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText edittext = new EditText(RequestManagerVisita.this);
                alert.setMessage("Aggiungi una nota per il paziente");
                alert.setTitle("Richiesta accettata");

                alert.setView(edittext);

                alert.setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                        String note_risposta = edittext.getText().toString();
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd 'alle' HH:mm:ss");
                        final String formattedDate = df.format(c.getTime());

                        new AsyncSendResponseCallSoap().execute(Integer.toString(richiesta.getIdRichiesta()), formattedDate, note_risposta, "C" /*accettata*/, richiesta.getCf_paziente(), richiesta.getNome_farmaco(), richiesta.getTipo());
                    }
                });

                alert.setNegativeButton("Cancella", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        return;
                    }
                });

                alert.show();

            }
        });
        Button rifiuta = (Button)findViewById(R.id.rifiuta_richiesta);
        rifiuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText edittext = new EditText(RequestManagerVisita.this);
                alert.setMessage("Aggiungi una nota per il paziente");
                alert.setTitle("Richiesta rifiutata");

                alert.setView(edittext);

                alert.setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String note_risposta = edittext.getText().toString();
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd 'alle' HH:mm:ss");
                        final String formattedDate = df.format(c.getTime());

                        new AsyncSendResponseCallSoap().execute(Integer.toString(richiesta.getIdRichiesta()), formattedDate, note_risposta, "R" /*rifiutata*/, richiesta.getCf_paziente(), richiesta.getNome_farmaco(), richiesta.getTipo());

                    }
                });

                alert.setNegativeButton("Cancella", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        return;
                    }
                });

                alert.show();

            }
        });

    }
    public class AsyncCallSoap extends AsyncTask<String, Void, Paziente> {
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(RequestManagerVisita.this,"Waiting","Recupero dati..",true);
        }

        @Override
        protected Paziente doInBackground(String... params) {
            Paziente response;
            CallSoap CS = new CallSoap();
            response = CS.GetPazienteInfo(params[0]);
            return response;
        }

        @Override
        protected void onPostExecute(Paziente s)
        {
            progressDialog.dismiss();
            if(s.getCodiceFiscale()==null){

                AlertDialog.Builder builder = new AlertDialog.Builder(RequestManagerVisita.this);
                builder.setTitle("Connessione non disponibile");
                builder.setMessage("Impossibile recuperare i dati");
                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog= builder.create();
                dialog.show();
            }else {
                paziente = s;
                String fotoPaziente = s.getImage();
                Utils utils = new Utils();
                Bitmap photo = utils.stringToBitmap(fotoPaziente);
                image.setImageBitmap(photo);
                nome_paziente.setText(s.getNome() + " " + s.getCognome());
            }
        }

    }

    public class AsyncSendResponseCallSoap extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            try
            {
                progressDialog = new ProgressDialog(RequestManagerVisita.this);
                progressDialog.setIndeterminate(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setTitle("Attendere");
                progressDialog.setMessage("Invio risposta in corso...");
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
            response = CS.Risposta(Integer.parseInt(params[0]),params[1],params[2],params[3]);
            response="1";
            if(response.equals("1")){
                try {
                    String dest = params[4] + "m2p";
                    dest += String.format("%09d", Integer.parseInt(params[0]));
                    String temp = "La tua rischiesta per la ";
                    if(params[6].equals("S")){
                        temp += "visita specialistica " + params[5] + " è stata ";
                    } else{
                        temp += "visita di controllo " + params[5] + " è stata ";
                    }
                    if (params[3].equals("C")){
                        temp += "accettata";
                    }else{
                        temp += "rifiutata";
                    }
                    String message = dest.concat(temp);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(RequestManagerVisita.this);
                builder.setTitle("Operazione eseguita");
                builder.setMessage("Risposta inviata");
                builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent newPage = new Intent(getBaseContext(), MainActivity.class);
                        finish();
                        startActivity(newPage);
                    }
                });
                AlertDialog dialog= builder.create();
                progressDialog.dismiss();
                dialog.show();
            }
            else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(RequestManagerVisita.this);
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
