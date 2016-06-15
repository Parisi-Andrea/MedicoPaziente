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
import android.widget.TextView;

import org.w3c.dom.Text;

public class Registration extends AppCompatActivity {

    private Button btnregistrazione;

    private String nome;
    private String cognome;
    private String password;
    private String indirizzo;
    private String codiceFiscale;
    private String medico;
    private String email;

    private TextView txtNome;
    private TextView txtCognome;
    private TextView txtPassword;
    private TextView txtIndirizzo;
    private TextView txtCodiceFiscale;
    private TextView txtMedico;
    private TextView txtEmail;


    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        btnregistrazione = (Button) findViewById(R.id.btnSaveData);
        txtNome = (TextView) findViewById(R.id.registerName);
        txtCognome = (TextView) findViewById(R.id.registerCognome);
        txtPassword = (TextView) findViewById(R.id.registerPassword);
        txtIndirizzo = (TextView) findViewById(R.id.registerIndirizzo);
        txtCodiceFiscale = (TextView) findViewById(R.id.registerCodFiscal);
        txtEmail = (TextView) findViewById(R.id.registerEmail);


        btnregistrazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncCallSoap().execute();
            }
        });
    }


    public class AsyncCallSoap extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            try
            {
                progressDialog = new ProgressDialog(Registration.this);
                progressDialog.setIndeterminate(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setTitle("Attendere");
                progressDialog.setMessage("Registrazione in corso...");
                progressDialog.show();
            } catch (Exception ex)
            {
                ex.getMessage().toString();
            }
            nome = txtNome.getText().toString();
            cognome = txtCognome.getText().toString();
            password = txtPassword.getText().toString();
            indirizzo = txtIndirizzo.getText().toString();
            codiceFiscale = txtCodiceFiscale.getText().toString();
            email = txtEmail.getText().toString();
        }

        @Override
        protected String doInBackground(String... params) {
            String response;
            CallSoap CS = new CallSoap();
            response = CS.Registration(nome,cognome,email,password,indirizzo,codiceFiscale,0);
            return response;
        }

        @Override
        protected void onPostExecute(String s)
        {
            if(s.equals("1"))
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(Registration.this);
                builder.setTitle("Operazione eseguita");
                builder.setMessage("Utente registrato correttamente, in attesa che venga confermato dal medico...");
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
                AlertDialog.Builder builder = new AlertDialog.Builder(Registration.this);
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
