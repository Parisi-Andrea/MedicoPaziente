package com.example.andre.medicopaziente;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MedicoRegistration extends AppCompatActivity {

    private EditText txtCodiceFiscaleMedico,txtNomeMedico,txtCognomeMedico,txtEmailMedico,txtNTelMedico,txtPasswordMedico;
    private String codiceFiscale,nome,cognome,email,nTelefono,password;
    private Button btnregistrazione;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medico_registration);
        btnregistrazione = (Button) findViewById(R.id.btnSaveDataMedico);
        txtCodiceFiscaleMedico = (EditText) findViewById(R.id.registerCodFiscalMedico);
        txtNomeMedico = (EditText) findViewById(R.id.registerNameMedico);
        txtCognomeMedico = (EditText) findViewById(R.id.registerCognomeMedico);
        txtEmailMedico = (EditText) findViewById(R.id.registerEmailMedico);
        txtNTelMedico = (EditText) findViewById(R.id.registerTelefonoMedico);
        txtPasswordMedico = (EditText) findViewById(R.id.registerPasswordMedico);

        btnregistrazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validate())
                {
                    return;
                }

                new AsyncCallSoap().execute();
            }
        });
    }

    public class AsyncCallSoap extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            try
            {
                progressDialog = new ProgressDialog(MedicoRegistration.this);
                progressDialog.setIndeterminate(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setTitle("Attendere");
                progressDialog.setMessage("Registrazione in corso...");
                progressDialog.show();
            } catch (Exception ex)
            {
                ex.getMessage();
            }

        }

        @Override
        protected String doInBackground(String... params) {
            String response;
            CallSoap CS = new CallSoap();
            response = CS.MedicoRegistration(nome,cognome,email,password,codiceFiscale,nTelefono);
            return response;
        }

        @Override
        protected void onPostExecute(String s)
        {
            if(s.equals("1"))
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(MedicoRegistration.this);
                builder.setTitle("Operazione eseguita");
                builder.setMessage("Utente registrato correttamente");
                builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent newPage = new Intent(MedicoRegistration.this, MainActivity.class);

                        startActivity(newPage);
                    }
                });
                AlertDialog dialog= builder.create();
                progressDialog.dismiss();
                dialog.show();
            }
            else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(MedicoRegistration.this);
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
    private boolean validate()
    {
        boolean valid = true;

        nome = txtNomeMedico.getText().toString();
        cognome = txtCognomeMedico.getText().toString();
        password = txtPasswordMedico.getText().toString();
        codiceFiscale = txtCodiceFiscaleMedico.getText().toString();
        email = txtEmailMedico.getText().toString();
        nTelefono = txtNTelMedico.getText().toString();

        if(nome.isEmpty()) {
            txtNomeMedico.setError("Nome non inserito");
            valid = false;
        } else {
            txtNomeMedico.setError(null);
        }
        if(cognome.isEmpty()){
            txtCognomeMedico.setError("Cognome non inserito");
            valid = false;
        } else {
            txtCognomeMedico.setError(null);
        }
        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            txtPasswordMedico.setError("Password compresa tra 4 e 10 caratteri");
            valid = false;
        } else {
            txtPasswordMedico.setError(null);
        }
        if (codiceFiscale.isEmpty() || codiceFiscale.length()!=16 ) {
            txtCodiceFiscaleMedico.setError("Inserire un codice fiscale valido");
            valid = false;
        } else {
            txtCodiceFiscaleMedico.setError(null);
        }
        if(email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtEmailMedico.setError("Inserire una email valida");
            valid = false;
        } else {
            txtEmailMedico.setError(null);
        }
        if(nTelefono.length()!=10)
        {
            txtNTelMedico.setError("Errore inserire un numero di telefono valido");
            valid = false;
        }
        else
        {
            txtNTelMedico.setError(null);
        }
        return valid;
    }
}