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


public class DettagliMedico extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private Toolbar toolbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettagli_medico);

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
        final Medico medico = intent.getParcelableExtra("medico");

        String fotoMedico = medico.getImage();
        if(fotoMedico!=null) {
            Utils utils = new Utils();
            Bitmap photo = utils.stringToBitmap(fotoMedico);
            ImageView userImage = (ImageView) findViewById(R.id.userimage);
            userImage.setImageBitmap(photo);
        }

        TextView cf_medico = (TextView) findViewById(R.id.cf);
        cf_medico.setText(medico.getCodiceFiscale());

        TextView nomeMedico = (TextView) findViewById(R.id.name);
        nomeMedico.setText(medico.getNome() + " " + medico.getCognome());

        TextView email = (TextView) findViewById(R.id.text_email);
        email.setText(medico.getEmail());

        final EditText edit_email = (EditText) findViewById(R.id.edit_email);
        edit_email.setText(medico.getEmail());

        TextView tel = (TextView) findViewById(R.id.text_tel);
        tel.setText(medico.getNTel());

        final EditText edit_tel = (EditText) findViewById(R.id.edit_tel);
        edit_tel.setText(medico.getNTel());

        TextView text_ambulatorio = (TextView) findViewById(R.id.text_ambulatorio);
        text_ambulatorio.setText(medico.getAmbulatorio());

        final EditText edit_ambulatorio = (EditText) findViewById(R.id.edit_ambulatorio);
        edit_ambulatorio.setText(medico.getAmbulatorio());

        TextView text_orario = (TextView) findViewById(R.id.text_orario);
        text_orario.setText(medico.getOrario());

        final EditText edit_orario = (EditText) findViewById(R.id.edit_orario);
        edit_orario.setText(medico.getOrario());
        edit_orario.setHorizontallyScrolling(false);
        edit_orario.setMaxLines(5);



        Button modifica = (Button)findViewById(R.id.buttonModifica);
        modifica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.text_email).setVisibility(View.GONE);
                findViewById(R.id.text_tel).setVisibility(View.GONE);
                findViewById(R.id.text_orario).setVisibility(View.GONE);
                findViewById(R.id.text_ambulatorio).setVisibility(View.GONE);
                findViewById(R.id.edit_email).setVisibility(View.VISIBLE);
                findViewById(R.id.edit_tel).setVisibility(View.VISIBLE);
                findViewById(R.id.edit_orario).setVisibility(View.VISIBLE);
                findViewById(R.id.edit_ambulatorio).setVisibility(View.VISIBLE);

                findViewById(R.id.no_modifica).setVisibility(View.GONE);
                findViewById(R.id.modifica).setVisibility(View.VISIBLE);
            }
        });

        Button annulla = (Button)findViewById(R.id.btn_annulla);
        annulla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.text_email).setVisibility(View.VISIBLE);
                findViewById(R.id.text_tel).setVisibility(View.VISIBLE);
                findViewById(R.id.text_ambulatorio).setVisibility(View.VISIBLE);
                findViewById(R.id.text_orario).setVisibility(View.VISIBLE);
                edit_email.setVisibility(View.GONE);
                edit_email.setText(((TextView) findViewById(R.id.text_email)).getText());
                edit_tel.setVisibility(View.GONE);
                edit_tel.setText(((TextView) findViewById(R.id.text_tel)).getText());
                edit_ambulatorio.setVisibility(View.GONE);
                edit_ambulatorio.setText(((TextView) findViewById(R.id.text_ambulatorio)).getText());
                edit_orario.setVisibility(View.GONE);
                edit_orario.setText(((TextView) findViewById(R.id.text_orario)).getText());

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
                new AsyncCallSoap().execute(medico.getCodiceFiscale(), "medico", edit_email.getText().toString(), edit_tel.getText().toString(), medico.getPassword(), edit_ambulatorio.getText().toString(),edit_orario.getText().toString());

            }
        });

    }

    public class AsyncCallSoap extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            try
            {
                progressDialog = new ProgressDialog(DettagliMedico.this);
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
            response = CS.UpdateProfilo(params[0], params[1], null, params[2], params[3], params[4], params[5], params[6]);
            response="1";
            return response;
        }

        @Override
        protected void onPostExecute(String s)
        {
            if(s.equals("1"))
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(DettagliMedico.this);
                builder.setTitle("Operazione eseguita");
                builder.setMessage("Dati modificati correttamente");
                builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent newPage = new Intent(DettagliMedico.this, MainActivity.class);
                        DettagliMedico.this.finish();
                        startActivity(newPage);
                    }
                });
                AlertDialog dialog= builder.create();
                progressDialog.dismiss();
                dialog.show();
            }
            else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(DettagliMedico.this);
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