package com.example.andre.medicopaziente;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {


    private EditText passwordTxt, codiceFiscaleTxt;
    private Button btnLogIn, btnRegister;
    private String username, password, response;
    private ProgressDialog progressDialog;
    private CheckBox checkBoxMedico, checkBoxSalva;
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    protected static String tipoUtente;

    private Medico docProfile;
    private Paziente patProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        docProfile = new Medico();
        patProfile = new Paziente();

        codiceFiscaleTxt = (EditText) findViewById(R.id.email);
        passwordTxt = (EditText) findViewById(R.id.password);
        btnLogIn = (Button) findViewById(R.id.btnLogin);
        checkBoxMedico = (CheckBox) findViewById(R.id.cbMedico);
        checkBoxSalva = (CheckBox) findViewById(R.id.cbSave);
        btnRegister = (Button) findViewById(R.id.btnRegistrazione);

        setImageDimension();

        recuperaDatiPref();

        createPDFRicettaRossa();

        btnLogIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                //login(v);

                //modifiche per run application
                tipoUtente = "Paziente";
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        btnRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                registration(v);
            }
        });

    }

    private void setImageDimension() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
    }

    private void recuperaDatiPref() {
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        if (prefs.contains("codiceFiscale")) {
            codiceFiscaleTxt.setText(prefs.getString("codiceFiscale", ""));
        }
        if (prefs.contains("password")) {
            passwordTxt.setText(prefs.getString("password", ""));
        }
        if (prefs.contains("check")) {
            checkBoxMedico.setChecked(prefs.getBoolean("check", false));
        }
        if (prefs.contains("rememberMe")) {
            checkBoxSalva.setChecked(prefs.getBoolean("rememberMe", false));
        }

    }

    public void registration(View v) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Profilo");
        builder.setMessage("Quale tipo di profilo desidera registrare?");
        builder.setPositiveButton("Paziente", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                Intent newPage = new Intent(getBaseContext(), PazienteRegistration.class);
                startActivity(newPage);

            }
        });
        builder.setNegativeButton("Medico", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                Intent newPage = new Intent(getBaseContext(), MedicoRegistration.class);
                startActivity(newPage);

            }
        });
        builder.show();
    }

    public void login(View v) {
        if (!validate()) {
            return;
        }
        if (checkBoxMedico.isChecked()) {
            tipoUtente = "Medico";
        } else {
           tipoUtente = "Paziente";
        }
        new AsyncCallSoap().execute();
    }

    public boolean validate() {
        boolean valid = true;

        String codiceFiscale = codiceFiscaleTxt.getText().toString();
        String password = passwordTxt.getText().toString();

        if (codiceFiscale.isEmpty() || codiceFiscale.length() != 16) {
            codiceFiscaleTxt.setError("inserire un codice fiscale valido");
            valid = false;
        } else {
            codiceFiscaleTxt.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordTxt.setError("password compresa tra 4 e 10 caratteri");
            valid = false;
        } else {
            passwordTxt.setError(null);
        }

        return valid;
    }

    public class AsyncCallSoap extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MainActivity.this, "Attendere", "Autenticazione", true);
            username = codiceFiscaleTxt.getText().toString();
            password = passwordTxt.getText().toString();

        }

        @Override
        protected String doInBackground(String... params) {
            CallSoap CS = new CallSoap();
            switch (tipoUtente) {
                case "Medico":
                    //Cerco di fare il login sul web service
                    docProfile = CS.LoginMedico(username, password, tipoUtente);

                    //Se ci sono riuscito controlla la password e creo il db interno
                    if (docProfile != null) {
                        if (docProfile.getPassword().equals(password)) {
                            DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                            if (!db.createMedico(docProfile))
                            {

                                if(db.updateMedico(docProfile) == -1)
                                {
                                    System.out.println("SQLite error: Non è stato possibile creare e/o aggiornare la tabella medico");
                                }

                            }
                            db.closeDB();
                            response = "OK";
                        } else {
                            response = "KO";
                        }
                    }
                    //Se non ho effettuato il login con il web service provo ad eseguirlo con il db interno
                    else {
                        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                        docProfile = db.getMedico(username);
                        if (docProfile != null) {
                            if (docProfile.getPassword().equals(password)) {
                                response = "OK";
                            } else {
                                response = "KO";
                            }
                        } else {
                            response = "KO";
                        }
                    }
                    break;

                case "Paziente":
                    patProfile = CS.LoginPaziente(username, password, tipoUtente);
                    if (patProfile != null) {
                        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                        if (!db.createPaziente(patProfile))
                        {

                            if(db.updatePaziente(patProfile) == -1)
                            {
                                System.out.println("SQLite error: Non è stato possibile creare e/o aggiornare la tabella paziente");
                            }

                        }
                        db.closeDB();
                        response = "OK";
                    } else {
                        DatabaseHelper db = new DatabaseHelper(getApplicationContext());

                        patProfile = db.getPaziente(username);
                        if (patProfile != null) {
                            if (patProfile.getPassword().equals(password)) {
                                response = "OK";
                            } else {
                                response = "KO";
                            }
                        } else {
                            response = "KO";
                        }
                    }
                    break;

                default:
                    response = "Problema sconosciuto...";
                    break;
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();

            if (s.contains("OK")) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Operazione Eseguita");
                alertDialog.setMessage("Autenticazione effettuata");
                alertDialog.show();

                Thread background = new Thread() {
                    public void run() {
                        try {
                            sleep(500);
                            if (checkBoxSalva.isChecked()) {
                                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                editor.putString("codiceFiscale", codiceFiscaleTxt.getText().toString());
                                editor.putString("password", passwordTxt.getText().toString());
                                editor.putBoolean("check", checkBoxMedico.isChecked());
                                editor.putBoolean("rememberMe", checkBoxSalva.isChecked());

                                boolean committato = editor.commit();

                                if (!committato) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                    builder.setTitle("Attenzione");
                                    builder.setMessage("Non è stato possibile salvare i dati");
                                    builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();

                                        }
                                    });
                                }
                            }
                            Intent newPage = new Intent(getBaseContext(), Profilo.class);
                            if (tipoUtente.equals("Medico")) {

                                newPage.putExtra("Medico", docProfile);
                            } else if (tipoUtente.equals("Paziente")) {
                                newPage.putExtra("Paziente", patProfile);
                            }
                            finish();
                            startActivity(newPage);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                background.start();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Attenzione");
                builder.setMessage(s + " tre possibili problemi: rete KO, profilo inesistente, username o password sbagliati ");
                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
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

    public void createPDFRicettaRossa() {

        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "MedicoPaziente" +File.separator + "RicettaRossa.pdf");
        File dir = new File(Environment.getExternalStorageDirectory() + File.separator + "MedicoPaziente");
        if (!file.exists()) {
            dir.mkdirs();
            Document document = new Document(PageSize.A5.rotate());

            try {
                PdfWriter.getInstance(document,
                        new FileOutputStream(Environment.getExternalStorageDirectory() + File.separator + "MedicoPaziente" + File.separator + "RicettaRossa.pdf"));
                document.open();
                //Creo la bitmap dal drawable folder
                Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ricettarossa);
                //Scalo la bitmap nelle dimensioni della ricetta
                Bitmap bMapScaled = Bitmap.createScaledBitmap(largeIcon, (int) PageSize.A5.getHeight(), (int) PageSize.A5.getWidth(), true);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bMapScaled.compress(Bitmap.CompressFormat.PNG, 100, stream);

                byte[] imageInByte = stream.toByteArray();

                Image image = Image.getInstance(imageInByte);

                image.setAbsolutePosition(0, 0);

                document.add(image);

                document.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
