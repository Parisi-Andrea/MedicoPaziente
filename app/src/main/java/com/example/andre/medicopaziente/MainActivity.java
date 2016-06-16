package com.example.andre.medicopaziente;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

public class MainActivity extends AppCompatActivity {


    private EditText passwordTxt;
    private EditText codiceFiscaleTxt;
    private ImageView imageView;
    private Button btnLogIn;
    private Button btnRegister;
    private String username;
    private String password;
    private String response;
    private ProgressDialog progressDialog;
    private CheckBox checkBoxMedico,checkBoxSalva;
    String tipoUtente;
    String codiceFiscaleExtra;
    String nomeExtra;
    String cognomeExtra;
    String dataNascitaExtra;
    String luogoNascitaExtra;
    String residenzaExtra;
    String EmailExtra;
    String nTelExtra;
    String passwordExtra;
    public static final String MY_PREFS_NAME = "MyPrefsFile";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        codiceFiscaleTxt = (EditText) findViewById(R.id.email);
        passwordTxt = (EditText) findViewById(R.id.password);
        imageView = (ImageView) findViewById(R.id.imageViewMainActivity);
        imageView.setImageResource(R.drawable.logomedium);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        btnLogIn = (Button) findViewById(R.id.btnLogin);
        checkBoxMedico = (CheckBox) findViewById(R.id.cbMedico);
        checkBoxSalva = (CheckBox) findViewById(R.id.cbSave);
        btnRegister = (Button) findViewById(R.id.btnRegistrazione);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        if (prefs.contains("codiceFiscale"))
        {
            codiceFiscaleTxt.setText(prefs.getString("codiceFiscale", ""));
        }
        if (prefs.contains("password"))
        {
            passwordTxt.setText(prefs.getString("password", ""));
        }
        if (prefs.contains("check"))
        {
            checkBoxMedico.setChecked(prefs.getBoolean("check", false));
        }
        if (prefs.contains("rememberMe"))
        {
            checkBoxSalva.setChecked(prefs.getBoolean("rememberMe", false));
        }



        btnLogIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                username = codiceFiscaleTxt.getText().toString();

                if(username.length()!=16)
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("ATTENZIONE");
                    alertDialog.setMessage("Formato codice fiscale non corretto");
                    alertDialog.show();
                    return;
                }

                if(checkBoxMedico.isChecked())
                {
                    tipoUtente = "Medico";
                }
                else
                {
                    tipoUtente = "Paziente";
                }
                new AsyncCallSoap().execute();


            }
        });

        btnRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar.make(v, "Working progress",Snackbar.LENGTH_LONG);
                snackbar.show();


                Intent newPage = new Intent(getBaseContext(), Registration.class);
                finish();
                startActivity(newPage);


            }
        });

    }

    public class AsyncCallSoap extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MainActivity.this,"Waiting","Authentication",true);
            username = codiceFiscaleTxt.getText().toString();
            password = passwordTxt.getText().toString();

        }
        @Override
        protected String doInBackground(String... params) {
            CallSoap CS = new CallSoap();
            response = CS.Login(username,password,tipoUtente);
            return  response;

        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();

            if(s.contains("OK")) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Operazione Eseguita");
                alertDialog.setMessage(s);
                alertDialog.show();
                if(tipoUtente.equals("Paziente")) {
                    codiceFiscaleExtra = Utility.GetFieldCSV(s, 1, ".");
                    nomeExtra = Utility.GetFieldCSV(s, 2, ".");
                    cognomeExtra = Utility.GetFieldCSV(s, 3, ".");
                    dataNascitaExtra = Utility.GetFieldCSV(s, 4, ".");
                    luogoNascitaExtra = Utility.GetFieldCSV(s, 5, ".");
                    residenzaExtra = Utility.GetFieldCSV(s, 6, ".");
                    EmailExtra = Utility.GetFieldCSV(s, 7, ".") + "." + Utility.GetFieldCSV(s, 8, ".");

                    //ATTENZIONE EMAIL CON CARATTERI '.'

                    nTelExtra = Utility.GetFieldCSV(s, 9, ".");
                    passwordExtra = Utility.GetFieldCSV(s, 10, ".");


                }
                else if(tipoUtente.equals("Medico"))
                {
                    codiceFiscaleExtra = Utility.GetFieldCSV(s, 1, ".");
                    nomeExtra = Utility.GetFieldCSV(s, 2, ".");
                    cognomeExtra = Utility.GetFieldCSV(s, 3, ".");
                    EmailExtra = Utility.GetFieldCSV(s, 4, ".") + "." + Utility.GetFieldCSV(s, 5, ".");
                    nTelExtra = Utility.GetFieldCSV(s, 6, ".");
                    passwordExtra = Utility.GetFieldCSV(s, 7, ".");
                }
                Thread background = new Thread() {
                    public  void run () {
                        try {

                            sleep(1500);

                            if(checkBoxSalva.isChecked()) {
                                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                editor.putString("codiceFiscale", codiceFiscaleExtra);
                                editor.putString("password", passwordExtra);
                                editor.putBoolean("check", checkBoxMedico.isChecked());
                                editor.putBoolean("rememberMe",checkBoxSalva.isChecked());
                                boolean committato = editor.commit();
                                if(!committato)
                                {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                    builder.setTitle("Attenzione");
                                    builder.setMessage("Non è stato possibile salvare i dati");
                                    builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();

                                        }
                                    });
                                    AlertDialog dialog = builder.create();
                                }
                            }
                            if(tipoUtente.equals("Paziente"))
                            {
                                Intent newPage = new Intent(getBaseContext(), Profilo.class);
                                newPage.putExtra("codiceFiscale", codiceFiscaleExtra);
                                newPage.putExtra("nome", nomeExtra);
                                newPage.putExtra("cognome", cognomeExtra);
                                newPage.putExtra("dataNascita", dataNascitaExtra);
                                newPage.putExtra("luogoNascita", luogoNascitaExtra);
                                newPage.putExtra("residenza", residenzaExtra);
                                newPage.putExtra("email", EmailExtra);
                                newPage.putExtra("nTel", nTelExtra);
                                newPage.putExtra("password", passwordExtra);
                                newPage.putExtra("tipoUtente",tipoUtente);

                                finish();
                                startActivity(newPage);
                            }
                            else if(tipoUtente.equals("Medico"))
                            {
                                Intent newPage = new Intent(getBaseContext(), Profilo.class);
                                newPage.putExtra("CodiceFiscale", codiceFiscaleExtra);
                                newPage.putExtra("nome", nomeExtra);
                                newPage.putExtra("cognome", cognomeExtra);
                                newPage.putExtra("email", EmailExtra);
                                newPage.putExtra("nTel", nTelExtra);
                                newPage.putExtra("password", passwordExtra);
                                newPage.putExtra("tipoUtente",tipoUtente);

                                finish();
                                startActivity(newPage);
                            }

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                background.start();


            }
            else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Attenzione");
                    builder.setMessage(s);
                    builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
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
}
