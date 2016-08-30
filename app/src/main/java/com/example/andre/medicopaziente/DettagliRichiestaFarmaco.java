package com.example.andre.medicopaziente;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;


public class DettagliRichiestaFarmaco extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private Toolbar toolbar;
    TextView richiedente;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettagli_richiesta_farmaco);

        richiedente = (TextView) findViewById(R.id.richiedente);
        richiedente.setText("-");

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
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        Paziente paziente = db.getPaziente(richiesta.cf_paziente);
        if(paziente!=null) {
            richiedente.setText(paziente.getNome() + " " + paziente.getCognome());
        }else {
            try {
                new AsyncCallSoapInfoPaziente().execute(richiesta.getCf_paziente());
            } catch (Exception e) {
            }
        }


        TextView cf_richiedente = (TextView) findViewById(R.id.cf_richiedente);
        cf_richiedente.setText(richiesta.getCf_paziente());

        TextView stato = (TextView) findViewById(R.id.stato);
        if(richiesta.getStato().equals("A")){
            stato.setText("IN ATTESA");
        } else if(richiesta.getStato().equals("C")){
            stato.setText("ACCETTATA");
        } else{
            stato.setText("RIFIUTATA");
        }

        TextView dataRichiesta = (TextView) findViewById(R.id.requestDate);
        dataRichiesta.setText(richiesta.getData_richiesta());

        TextView medicinaRichiesta = (TextView) findViewById(R.id.medicine_requested);
        medicinaRichiesta.setText(richiesta.getNome_farmaco());

        TextView medicineAmount = (TextView) findViewById(R.id.medicine_amount);
        medicineAmount.setText(String.valueOf(richiesta.getQuantita_farmaco()));

        final TextView noteFarmaco = (TextView) findViewById(R.id.request_notes);
        noteFarmaco.setText(richiesta.getNote_richiesta());
        noteFarmaco.setMovementMethod(new ScrollingMovementMethod());
        noteFarmaco.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                noteFarmaco.getParent().requestDisallowInterceptTouchEvent(true);

                return false;
            }
        });

        TextView dataRisposta = (TextView) findViewById(R.id.data_risposta);
        dataRisposta.setText(richiesta.getData_risposta());
        if (richiesta.getStato().equals("A")){
            dataRisposta.setText("-");
        }
        final TextView noteRisposta = (TextView) findViewById(R.id.note_risposta);
        noteRisposta.setText(richiesta.getNote_risposta());
        noteRisposta.setMovementMethod(new ScrollingMovementMethod());
        noteRisposta.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                noteRisposta.getParent().requestDisallowInterceptTouchEvent(true);

                return false;
            }
        });

        if (richiesta.getStato().equals("A")){
            noteRisposta.setText("-");
        }

    }
    public class AsyncCallSoapInfoPaziente extends AsyncTask<String, Void, Paziente> {
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(DettagliRichiestaFarmaco.this, "Waiting", "Recupero dati..", true);
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

                AlertDialog.Builder builder = new AlertDialog.Builder(DettagliRichiestaFarmaco.this);
                builder.setTitle("Errore");
                builder.setMessage("Connessione non disponibile");
                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog= builder.create();
                dialog.show();
            } else {
                richiedente.setText(s.getNome() + " " + s.getCognome());
            }
        }
    }
}
