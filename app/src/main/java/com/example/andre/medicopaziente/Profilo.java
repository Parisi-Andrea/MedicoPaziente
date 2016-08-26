package com.example.andre.medicopaziente;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class Profilo extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ProgressDialog progressDialog = null;
    Toolbar toolbar = null;
    CircleImageView imageView = null;
    private TextView textViewNome, textViewCF = null;
    private Medico medico = null;
    private Paziente paziente = null;
    private Bitmap photo = null;
    private Utils utils = new Utils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");

        new AsyncCallSoapRichieste().execute();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        if (!Utils.isConnectedViaWifi(this)) {
            if (!Utils.executePingWebService("192.168.173.1")) {

                Utils.createSnackBar(this, "Warning: I dati possono non essere aggiornati", Snackbar.LENGTH_INDEFINITE, Color.RED);
                fab.setVisibility(View.GONE);
            }
        }

        Intent intent = getIntent();

        if (MainActivity.tipoUtente.equals("Medico")) {
            medico = intent.getParcelableExtra("Medico");
            paziente = null;
        } else {
            paziente = intent.getParcelableExtra("Paziente");
            medico = null;
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creaRicettaRossa(paziente);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        setNavigationview();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case 0: //CAMERA
                if (resultCode == RESULT_OK) {

                    photo = (Bitmap) imageReturnedIntent.getExtras().get("data");

                    if (medico == null)
                        utils.saveImageInternalStorage(photo, paziente.getCodiceFiscale(), this);
                    else utils.saveImageInternalStorage(photo, medico.getCodiceFiscale(), this);

                    imageView.setImageBitmap(photo);

                    new AsyncCallSoap().execute();
                }

                break;
            case 1: //GALLERY
                if (resultCode == RESULT_OK) {
                    try {
                        Uri selectedImage = imageReturnedIntent.getData();
                        photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

                        if (medico == null) {

                            File myDir = new File(Environment.getExternalStorageDirectory(), File.separator + "MedicoPaziente" + File.separator + paziente.getCodiceFiscale());
                            String fname = paziente.getCodiceFiscale() + ".png";
                            File file = new File(myDir, fname);

                            utils.copyFile(new File(utils.getPath(imageReturnedIntent.getData(), this)), file);

                        } else {
                            File myDir = new File(Environment.getExternalStorageDirectory(), File.separator + "MedicoPaziente" + File.separator + medico.getCodiceFiscale());
                            String fname = medico.getCodiceFiscale() + ".png";
                            File file = new File(myDir, fname);


                            utils.copyFile(new File(utils.getPath(imageReturnedIntent.getData(), this)), file);

                        }

                        //scalo immagine per vederla in circleimageView
                        int nh = (int) (photo.getHeight() * (200.0 / photo.getWidth()));
                        photo = Bitmap.createScaledBitmap(photo, 200, nh, true);
                        imageView.setImageBitmap(photo);

                        new AsyncCallSoap().execute();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment = new Fragment();

        int id = item.getItemId();

        if (id == R.id.nav_home) {
            getSupportActionBar().setTitle("Home");
            fragment = new HomeFragment();
        } else if (id == R.id.nav_wait) {
            getSupportActionBar().setTitle("In Attesa");
            fragment = new WaitFragment();
        } else if (id == R.id.nav_response) {
            getSupportActionBar().setTitle("Risposte");
        } else if (id == R.id.nav_complete) {
            getSupportActionBar().setTitle("Completate");
        } else if (id == R.id.nav_setting) {
            getSupportActionBar().setTitle("Setting");
        }
        transaction.replace(R.id.flFragments, fragment);
        transaction.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class AsyncCallSoapRichieste extends AsyncTask<String, Void, ArrayList<Richiesta>> {

        @Override
        protected ArrayList<Richiesta> doInBackground(String... params) {
            CallSoap cs = new CallSoap();
            return cs.GetPazienteRequest(paziente.getCodiceFiscale());

        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(Profilo.this, "Attendere", "Aggiornamento richieste...", true);
        }

        @Override
        protected void onPostExecute(ArrayList<Richiesta> s) {
            DatabaseHelper db = new DatabaseHelper(getApplicationContext());
            if (!db.createRequest(s)) {
                int a = 10;
                a++;
                System.out.println(a);
            }
            progressDialog.dismiss();

        }
    }

    public class AsyncCallSoap extends AsyncTask<Bitmap, Void, String> {

        @Override
        protected String doInBackground(Bitmap... params) {

            if (MainActivity.tipoUtente.equals("Paziente")) {
                utils.saveImageDb(photo, paziente.getCodiceFiscale(), MainActivity.tipoUtente);
            } else if (MainActivity.tipoUtente.equals("Medico")) {
                utils.saveImageDb(photo, medico.getCodiceFiscale(), MainActivity.tipoUtente);
            }
            return "OK";

        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(Profilo.this, "Attendere", "Salvataggio della foto...", true);
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
        }
    }

    public void setNavigationview() {

        imageView = (CircleImageView) findViewById(R.id.imageViewClickable);
        textViewNome = (TextView) findViewById(R.id.textNome);
        textViewCF = (TextView) findViewById(R.id.textCodiceFiscale);

        //Setto le informazioni nel Drawer (nome,cognome, codice fiscale,foto da db)
        if (!utils.setUpInfoDrawer(Profilo.this, medico, paziente, textViewCF, textViewNome, imageView)) {
            AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
            alertDialog.setTitle("Errore");
            alertDialog.setMessage("Info drawer non settate!");
            alertDialog.show();
        }
        //Cerco di recuperare l'immagine profilo salvata nella memoria interna "CodiceFiscale.png"
        if (medico != null) {
            photo = utils.readImageFromInternalStore(medico.getCodiceFiscale());
            if (photo == null) {
                System.out.println("Medico: Errore lettura immagine");
            } else {
                imageView.setImageBitmap(photo);
            }
        } else //Se è paziente, se nel db non c'è la foto cerco di prenderla dalla memoria interna
        {
            if (paziente.getImage() == null) {
                photo = utils.readImageFromInternalStore(paziente.getCodiceFiscale());
                if (photo == null) {
                    System.out.println("Paziente: Errore lettura immagine");
                } else {
                    imageView.setImageBitmap(photo);
                }
            }
        }


    }
}