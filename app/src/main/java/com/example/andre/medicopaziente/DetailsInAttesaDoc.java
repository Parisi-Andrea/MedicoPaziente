package com.example.andre.medicopaziente;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.damiano.myapplication.backend.messaging.Messaging;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Annalisa on 29/08/2016.
 */
public class DetailsInAttesaDoc extends AppCompatActivity {

    Utils u = new Utils();
    DatabaseHelper db = new DatabaseHelper(this);
    ProgressDialog progressDialog;
    Paziente paz = getpaziente();

    static final String descrizione_prescrizione = "Richiesta prescrizione farmaco: ";
    static final String descrizione_visita_spec = "Richiesta una visita specialistica in ";
    static final String descrizione_visita = "Richiesta una visita di controllo";

    TextView txttipo;
    TextView txtdescrizione;
    TextView txtdata;
    TextView txtstato;
    TextView txtfarmaco;
    TextView txtquant;
    TextView txtnote;
    ImageView imageView;
    TextView txtpaziente;
    ImageView pazienteimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        final Richiesta item_richiesta = intent.getParcelableExtra("ITEM_CLICKED");

        String tipo = item_richiesta.getTipo();
        String nome_farmaco = item_richiesta.getNome_farmaco();
        String data_richiesta = item_richiesta.getData_richiesta();
        String note_richiesta = item_richiesta.getNote_richiesta();
        int quantita = item_richiesta.getQuantita_farmaco();

        if (tipo.equals("Prescrizione")) {
            setContentView(R.layout.details_doc_prescrizione);

            txttipo = (TextView) findViewById(R.id.titolo_dett);
            txtdescrizione = (TextView) findViewById(R.id.descrizione_dett);
            txtdata = (TextView) findViewById(R.id.data_ora_dett);
            txtstato = (TextView) findViewById(R.id.stato_richiesta_dett);
            txtfarmaco = (TextView) findViewById(R.id.farmaco_dett);
            txtquant = (TextView) findViewById(R.id.quant_farmaco_dett);
            txtnote = (TextView) findViewById(R.id.note_richiesta_dett);
            imageView = (ImageView) findViewById(R.id.immagine_dett);

            imageView.setImageResource(R.drawable.pill_icon);
            txttipo.setText(tipo);
            txtdescrizione.setText(descrizione_prescrizione + nome_farmaco);
            txtdata.setText(data_richiesta);
            txtstato.setText("In Attesa");
            txtfarmaco.setText(nome_farmaco);
            txtquant.setText("" + quantita);
            if (note_richiesta == null || note_richiesta.equals(""))
                txtnote.setText("- -");
            else
                txtnote.setText(note_richiesta);

        } else {
            setContentView(R.layout.details_doc_visita);

            txttipo = (TextView) findViewById(R.id.titolo_dett);
            txtdescrizione = (TextView) findViewById(R.id.descrizione_dett);
            txtdata = (TextView) findViewById(R.id.data_ora_dett);
            txtstato = (TextView) findViewById(R.id.stato_richiesta_dett);
            txtnote = (TextView) findViewById(R.id.note_richiesta_dett);
            imageView = (ImageView) findViewById(R.id.immagine_dett);

            imageView.setImageResource(R.drawable.calendar);
            txttipo.setText(tipo);
            if (tipo.equals("Visita di controllo"))
                txtdescrizione.setText(descrizione_visita);
            else
                txtdescrizione.setText(descrizione_visita_spec + nome_farmaco);
            txtdata.setText(data_richiesta);
            txtstato.setText("In Attesa");
            if (note_richiesta == null || note_richiesta.equals(""))
                txtnote.setText("- -");
            else
                txtnote.setText(note_richiesta);

        }
         txtpaziente = (TextView) findViewById(R.id.paziente);
         pazienteimg = (ImageView) findViewById(R.id.paziente_img);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageButton btninfo = (ImageButton) findViewById(R.id.btninfo);
        assert btninfo != null;
        btninfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(DetailsInAttesaDoc.this, InfoDettagliPazActivity.class);
                intent1.putExtra("Paziente", paz);
                startActivity(intent1);
            }
        });

        ViewGroup content = (ViewGroup) findViewById(R.id.riepilogo_richiesta);
        //getLayoutInflater().inflate(R.layout.doctor_button, content, true);
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        Button accetta = (Button) findViewById(R.id.accetta_richiesta);
        accetta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText edittext = new EditText(DetailsInAttesaDoc.this);
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

                        new AsyncSendResponseCallSoap().execute(Integer.toString(item_richiesta.getIdRichiesta()),
                                formattedDate, note_risposta, "C", item_richiesta.getCf_paziente(),
                                item_richiesta.getNome_farmaco());
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
        Button rifiuta = (Button) findViewById(R.id.rifiuta_richiesta);
        rifiuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText edittext = new EditText(DetailsInAttesaDoc.this);
                alert.setMessage("Aggiungi una nota per il paziente");
                alert.setTitle("Richiesta rifiutata");

                alert.setView(edittext);

                alert.setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String note_risposta = edittext.getText().toString();
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd 'alle' HH:mm:ss");
                        final String formattedDate = df.format(c.getTime());

                        new AsyncSendResponseCallSoap().execute(Integer.toString(item_richiesta.getIdRichiesta()),
                                formattedDate, note_risposta, "R", item_richiesta.getCf_paziente(),
                                item_richiesta.getNome_farmaco());

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
            progressDialog = ProgressDialog.show(DetailsInAttesaDoc.this, "Waiting", "Recupero dati..", true);
        }

        @Override
        protected Paziente doInBackground(String... params) {
            Paziente response;
            CallSoap CS = new CallSoap();
            response = CS.GetPazienteInfo(params[0]);
            return response;
        }

        @Override
        protected void onPostExecute(Paziente s) {
            progressDialog.dismiss();
            if (s.getCodiceFiscale() == null) {

                AlertDialog.Builder builder = new AlertDialog.Builder(DetailsInAttesaDoc.this);
                builder.setTitle("Errore");
                builder.setMessage("Connessione non disponibile");
                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                paz = s;
                String fotoPaziente = s.getImage();
                Utils utils = new Utils();
                Bitmap photo = utils.stringToBitmap(fotoPaziente);
                pazienteimg.setImageBitmap(photo);
                txtpaziente.setText(s.getNome() + " " + s.getCognome());
            }
        }

    }


    public class AsyncSendResponseCallSoap extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            try {
                progressDialog = new ProgressDialog(DetailsInAttesaDoc.this);
                progressDialog.setIndeterminate(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setTitle("Attendere");
                progressDialog.setMessage("Invio risposta in corso...");
                progressDialog.show();
            } catch (Exception ex) {
                ex.getMessage().toString();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String response;
            CallSoap CS = new CallSoap();
            response = CS.Risposta(Integer.parseInt(params[0]), params[1], params[2], params[3]);
            if (response.equals("1")) {
                try {
                    String dest = params[4] + "m2p";
                    dest += String.format("%09d", Integer.parseInt(params[0]));
                    String temp = "La tua rischiesta per il farmaco " + params[5] + " è stata ";
                    if (params[3].equals("C")) {
                        temp += "accettata";
                    } else {
                        temp += "rifiutata";
                    }
                    String message = dest.concat(temp);
                    Messaging.Builder builder = new Messaging.Builder(AndroidHttp.newCompatibleTransport(),
                            new AndroidJsonFactory(), null)
                            .setRootUrl("https://pazientemedico.appspot.com/_ah/api/");
                    Messaging messaging = builder.build();
                    try {
                        messaging.sendMessage(message).execute();
                    } catch (Exception e) {
                        Log.e("MedicoPaziente", "Exception :", e);
                    }
                } catch (Exception e) {
                    Log.e("MedicoPaziente", "Exception :", e);
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals("1")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailsInAttesaDoc.this);
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
                AlertDialog dialog = builder.create();
                progressDialog.dismiss();
                dialog.show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailsInAttesaDoc.this);
                builder.setTitle("Errore");
                builder.setMessage("Operazione non eseguita, riprovare...");
                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                progressDialog.dismiss();
                dialog.show();
            }


        }

    }

    public Paziente getpaziente() {
        Paziente elemento = new Paziente();
        elemento.setCodiceFiscale("NLSFLP94T45L378G");
        elemento.setNome("Lia");
        elemento.setCognome("Filippi");
        elemento.setDataNascita("05/12/1994");
        elemento.setLuogoNascita("Trento");
        elemento.setResidenza("via paludi, 42");
        elemento.setEmail("annalisa.filippi@mail.it");
        elemento.setNTel("0461 961361");

        return elemento;
    }
}
