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
import android.util.DisplayMetrics;
import android.util.Log;
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


    private EditText passwordTxt,codiceFiscaleTxt;
    private ImageView imageView;
    private Button btnLogIn,btnRegister;
    private String username,password,response;
    private ProgressDialog progressDialog;
    private CheckBox checkBoxMedico,checkBoxSalva;
    String tipoUtente,codiceFiscaleExtra,nomeExtra,medicoExtra,cognomeExtra,dataNascitaExtra,luogoNascitaExtra,residenzaExtra,EmailExtra,nTelExtra,passwordExtra;
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    private Medico test1;
    private Paziente test2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        test1 = new Medico();
        test2 = new Paziente();
        codiceFiscaleTxt = (EditText) findViewById(R.id.email);
        passwordTxt = (EditText) findViewById(R.id.password);
        imageView = (ImageView) findViewById(R.id.imageViewMainActivity);
        btnLogIn = (Button) findViewById(R.id.btnLogin);
        checkBoxMedico = (CheckBox) findViewById(R.id.cbMedico);
        checkBoxSalva = (CheckBox) findViewById(R.id.cbSave);
        btnRegister = (Button) findViewById(R.id.btnRegistrazione);

        setImageDimension();

        recuperaDatiPref();

        btnLogIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                login(v);

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

//        int height = displaymetrics.heightPixels;
//        int width = displaymetrics.widthPixels;
//
//        imageView.setImageResource(R.drawable.logomedium);
//
//        imageView.requestLayout();
//
//        imageView.getLayoutParams().height = (height / 3);
//        imageView.getLayoutParams().width = (width / 3)+(width/6);
//
//        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    private void recuperaDatiPref() {

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

    }
    public void registration(View v) {
//        Snackbar snackbar = Snackbar.make(v, "Working progress",Snackbar.LENGTH_LONG);
//        snackbar.show();


        Intent newPage = new Intent(getBaseContext(), Registration.class);
        //finish();
        startActivity(newPage);
    }

    public void login(View v) {

        if (!validate()) {
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

    public boolean validate() {
        boolean valid = true;

        String codiceFiscale = codiceFiscaleTxt.getText().toString();
        String password = passwordTxt.getText().toString();

        //!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

        if (codiceFiscale.isEmpty() || codiceFiscale.length()!=16 ) {
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
            if(tipoUtente.equals("Medico")) {

                //Cerco di fare il login sul web service

                test1 = CS.LoginMedico(username, password, tipoUtente);

                //Se ci sono riuscito controlla la password e creo il db
                if(test1 != null) {
                    if(test1.getPassword().equals(password))
                    {
                        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                        db.createMedico(test1);
                        db.closeDB();
                        response = "OK";
                    }
                    else
                    {
                        response = "KO";
                    }


                }
                //Se non ho effettuato il login con il web service provo ad eseguirlo con il db interno
                else if(test1 == null)
                {
                    DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                    test1 = db.getMedico(username);
                    if(test1 != null)
                    {
                        if(test1.getPassword().equals(password))
                        {
                            response = "OK";
                        }
                        else
                        {
                            response = "KO";
                        }
                    }
                    else
                    {
                        response = "KO";
                    }
                }
            } else if(tipoUtente.equals("Paziente")) {
                test2 = CS.LoginPaziente(username,password,tipoUtente);
                if(test2 != null) {
                    DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                    db.createPaziente(test2);
                    db.closeDB();
                    response = "OK";
                }
                else if(test2 == null)
                {
                    DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                    test2 = db.getPaziente(username);
                    if(test2 != null)
                    {
                        if(test2.getPassword().equals(password))
                        {
                            response = "OK";
                        }
                        else
                        {
                            response = "KO";
                        }
                    }
                    else
                    {
                        response = "KO";
                    }
                }
            }
            else
            {
                response = "Problema sconosciuto...";
            }
            return  response;

        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();

            if(s.contains("OK")) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Operazione Eseguita");
                alertDialog.setMessage("Autenticazione effettuata");
                alertDialog.show();

                Thread background = new Thread() {
                    public  void run () {
                        try {

                            sleep(1500);

                            if(checkBoxSalva.isChecked()) {
                                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                editor.putString("codiceFiscale", codiceFiscaleTxt.getText().toString());
                                editor.putString("password", passwordTxt.getText().toString());
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
                            Intent newPage = new Intent(getBaseContext(), Profilo.class);
                            if(tipoUtente.equals("Medico")) {

                                newPage.putExtra("Medico", test1);
                            }
                            else if(tipoUtente.equals("Paziente"))
                            {
                                newPage.putExtra("Paziente",test2);
                            }

                            newPage.putExtra("tipoUtente", tipoUtente);
                            finish();
                            startActivity(newPage);


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
                    builder.setMessage(s+" tre possibili problemi: rete KO, profilo inesistente, username & password sbagliati ");
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
}
