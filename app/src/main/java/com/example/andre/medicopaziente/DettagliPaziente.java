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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class DettagliPaziente extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private Toolbar toolbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettagli_paziente);

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
        final Paziente paziente = intent.getParcelableExtra("Paziente");

        if (!MainActivity.tipoUtente.equals("Paziente")){
            findViewById(R.id.no_modifica).setVisibility(View.GONE);
        }

        String fotoPaziente = paziente.getImage();
        if(fotoPaziente!=null) {
            Utils utils = new Utils();
            Bitmap photo = utils.stringToBitmap(fotoPaziente);
            ImageView userImage = (ImageView) findViewById(R.id.userimage);
            userImage.setImageBitmap(photo);
        }

        TextView cf_richiedente = (TextView) findViewById(R.id.cf);
        cf_richiedente.setText(paziente.getCodiceFiscale());

        TextView nomePaziente = (TextView) findViewById(R.id.name);
        nomePaziente.setText(paziente.getNome() + " " + paziente.getCognome());

        TextView dataNascita = (TextView) findViewById(R.id.birthday);
        dataNascita.setText(paziente.getDataNascita());

        TextView luogoNascita = (TextView) findViewById(R.id.luogo_nascita);
        luogoNascita.setText(paziente.getLuogoNascita());

        TextView email = (TextView) findViewById(R.id.text_email);
        email.setText(paziente.getEmail());

        final EditText edit_email = (EditText) findViewById(R.id.edit_email);
        edit_email.setText(paziente.getEmail());

        TextView tel = (TextView) findViewById(R.id.text_tel);
        tel.setText(paziente.getNTel());

        final EditText edit_tel = (EditText) findViewById(R.id.edit_tel);
        edit_tel.setText(paziente.getNTel());

        TextView residenza = (TextView) findViewById(R.id.text_residenza);
        residenza.setText(paziente.getResidenza());

        final EditText edit_residenza = (EditText) findViewById(R.id.edit_residenza);
        edit_residenza.setText(paziente.getResidenza());


        Button modifica = (Button)findViewById(R.id.buttonModifica);
        modifica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.text_email).setVisibility(View.GONE);
                findViewById(R.id.text_residenza).setVisibility(View.GONE);
                findViewById(R.id.text_tel).setVisibility(View.GONE);
                findViewById(R.id.edit_email).setVisibility(View.VISIBLE);
                findViewById(R.id.edit_residenza).setVisibility(View.VISIBLE);
                findViewById(R.id.edit_tel).setVisibility(View.VISIBLE);

                findViewById(R.id.no_modifica).setVisibility(View.GONE);
                findViewById(R.id.modifica).setVisibility(View.VISIBLE);
            }
        });

        Button annulla = (Button)findViewById(R.id.btn_annulla);
        annulla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.text_email).setVisibility(View.VISIBLE);
                findViewById(R.id.text_residenza).setVisibility(View.VISIBLE);
                findViewById(R.id.text_tel).setVisibility(View.VISIBLE);
                edit_email.setVisibility(View.GONE);
                edit_email.setText(((TextView) findViewById(R.id.text_email)).getText());
                edit_residenza.setVisibility(View.GONE);
                edit_residenza.setText(((TextView) findViewById(R.id.text_residenza)).getText());
                edit_tel.setVisibility(View.GONE);
                edit_tel.setText(((TextView) findViewById(R.id.text_tel)).getText());

                findViewById(R.id.no_modifica).setVisibility(View.VISIBLE);
                findViewById(R.id.modifica).setVisibility(View.GONE);
            }
        });
        Button btnok = (Button)findViewById(R.id.btnok);
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.no_modifica).setVisibility(View.GONE);
                findViewById(R.id.modifica).setVisibility(View.VISIBLE);

                new AsyncCallSoap().execute(paziente.getCodiceFiscale(),"Paziente",edit_residenza.getText().toString(), edit_email.getText().toString(), edit_tel.getText().toString(), paziente.getPassword());

            }
        });

    }

    public class AsyncCallSoap extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            try
            {
                progressDialog = new ProgressDialog(DettagliPaziente.this);
                progressDialog.setIndeterminate(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setTitle("Attendere");
                progressDialog.setMessage("Modifica dati in corso...");
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
            response = CS.UpdateProfilo(params[0], params[1], params[2], params[3], params[4], params[5], null, null);
            return response;
        }

        @Override
        protected void onPostExecute(String s)
        {
            if(s.equals("1"))
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(DettagliPaziente.this);
                builder.setTitle("Operazione eseguita");
                builder.setMessage("Dati modificati correttamente");
                builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent newPage = new Intent(DettagliPaziente.this, HomeActivity.class);
                        DettagliPaziente.this.finish();
                        startActivity(newPage);
                    }
                });
                AlertDialog dialog= builder.create();
                progressDialog.dismiss();
                dialog.show();
            }
            else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(DettagliPaziente.this);
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