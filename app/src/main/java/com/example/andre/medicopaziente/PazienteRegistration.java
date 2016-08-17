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

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class PazienteRegistration extends AppCompatActivity {

    private Button btnregistrazione;
    private String nome,cognome,password,indirizzo,codiceFiscale,medico,email,dataNascita,luogoNascita,telefono;
    private Integer giorno,mese,anno;
    private EditText txtNome,txtCognome,txtPassword,txtIndirizzo,txtCodiceFiscale,txtMedico,txtEmail,txtDay,txtMonth,txtYear,txtLuogoNascita,txtTelefono;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paziente_registration);
        btnregistrazione = (Button) findViewById(R.id.btnSaveData);
        txtNome = (EditText) findViewById(R.id.registerName);
        txtCognome = (EditText) findViewById(R.id.registerCognome);
        txtPassword = (EditText) findViewById(R.id.registerPassword);
        txtIndirizzo = (EditText) findViewById(R.id.registerIndirizzo);
        txtCodiceFiscale = (EditText) findViewById(R.id.registerCodFiscal);
        txtEmail = (EditText) findViewById(R.id.registerEmail);
        txtMedico = (EditText) findViewById(R.id.registerMedico);
        txtDay = (EditText) findViewById(R.id.registerDay);
        txtMonth = (EditText) findViewById(R.id.registerMonth);
        txtYear = (EditText) findViewById(R.id.registerYear);
        txtLuogoNascita = (EditText) findViewById(R.id.registerLuogo);
        txtTelefono = (EditText) findViewById(R.id.registerTelefono);


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
                progressDialog = new ProgressDialog(PazienteRegistration.this);
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
            response = CS.PazienteRegistration(nome,cognome,email,password,indirizzo,codiceFiscale,medico,dataNascita,luogoNascita,telefono);
            return response;
        }

        @Override
        protected void onPostExecute(String s)
        {
            if(s.equals("1"))
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(PazienteRegistration.this);
                builder.setTitle("Operazione eseguita");
                builder.setMessage("Utente registrato correttamente");
                builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent newPage = new Intent(PazienteRegistration.this, MainActivity.class);

                        startActivity(newPage);
                    }
                });
                AlertDialog dialog= builder.create();
                progressDialog.dismiss();
                dialog.show();
            }
            else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(PazienteRegistration.this);
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


    public static boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

    private boolean validate()
    {
        boolean valid = true;

        nome = txtNome.getText().toString();
        cognome = txtCognome.getText().toString();
        password = txtPassword.getText().toString();
        indirizzo = txtIndirizzo.getText().toString();
        codiceFiscale = txtCodiceFiscale.getText().toString();
        email = txtEmail.getText().toString();
        medico = txtMedico.getText().toString();
        giorno = Integer.parseInt(txtDay.getText().toString());
        mese = Integer.parseInt(txtMonth.getText().toString());
        anno = Integer.parseInt(txtYear.getText().toString());
        dataNascita = anno+"-"+mese+"-"+giorno;
        luogoNascita = txtLuogoNascita.getText().toString();
        telefono = txtTelefono.getText().toString();

        if(nome.isEmpty()) {
            txtNome.setError("Nome non inserito");
            valid = false;
        } else {
            txtNome.setError(null);
        }
        if(cognome.isEmpty()){
            txtCognome.setError("Cognome non inserito");
            valid = false;
        } else {
            txtCognome.setError(null);
        }
        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            txtPassword.setError("Password compresa tra 4 e 10 caratteri");
            valid = false;
        } else {
            txtPassword.setError(null);
        }
        if(indirizzo.isEmpty()) {
            txtIndirizzo.setError("Indirizzo non inserito");
            valid = false;
        } else {
            txtIndirizzo.setError(null);
        }
        if (codiceFiscale.isEmpty() || codiceFiscale.length()!=16 ) {
            txtCodiceFiscale.setError("Inserire un codice fiscale valido");
            valid = false;
        } else {
            txtCodiceFiscale.setError(null);
        }
        if(email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtEmail.setError("Inserire una email valida");
            valid = false;
        } else {
            txtEmail.setError(null);
        }
        if(medico.isEmpty() || medico.length()!=16)
        {
            txtMedico.setError("Inserire il codice fiscale del proprio medico");
            valid = false;
        }
        else
        {
            txtMedico.setError(null);
        }
        if(!isValidDate(dataNascita)  )
        {
            txtDay.setError("Errore la data inserita non è corretta");
            valid = false;
        }
        else
        {
            txtDay.setError(null);
        }
        if(luogoNascita.isEmpty())
        {
            txtLuogoNascita.setError("Errore inserire luogo di nascita");
            valid = false;
        }
        else
        {
            txtLuogoNascita.setError(null);
        }
        if(telefono.length()!=10)
        {
            txtTelefono.setError("Errore inserire un numero di telefono valido");
            valid = false;
        }
        else
        {
            txtTelefono.setError(null);
        }
        return valid;
    }
}
