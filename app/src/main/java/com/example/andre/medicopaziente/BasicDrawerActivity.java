package com.example.andre.medicopaziente;

import android.app.Activity;
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
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class BasicDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public final static String EXTRA_PACK = "ID_";

    private ProgressDialog progressDialog;
    Toolbar toolbar;
    CircleImageView imageView;

    private Medico medico;
    private Paziente paziente;
    private Bitmap photo;

    private Utils utils = new Utils();
    private TextView textViewNome, textViewCF;

    public String login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_drawer);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //
        //da rimettere!
        //
        //new AsyncCallSoapRichieste().execute();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (!Utils.isConnectedViaWifi(this)) {
            if (!Utils.executePingWebService("192.168.173.1")) {

                Utils.createSnackBar(this, "Non connesso! Dati non aggiornati", Snackbar.LENGTH_LONG, Color.RED);
                fab.setVisibility(View.GONE);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = getIntent();
        int id = intent.getIntExtra(EXTRA_PACK, 0);
        navigationView.setCheckedItem(id);

        if (MainActivity.tipoUtente.equals("Medico")) {
            medico = intent.getParcelableExtra("Medico");
            paziente = null;
            Menu drawermenu = navigationView.getMenu();
            drawermenu.removeItem(R.id.nav_request);
            drawermenu.getItem(3).setTitle("Informazioni Pazienti");
        } else {
            paziente = intent.getParcelableExtra("Paziente");
            medico = null;
        }
        //
        //tolto per non chiamare dati del db per l'header del drawer!
        //
        //setNavigationview();
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
                        int nh = (int) (photo.getHeight() * (512.0 / photo.getWidth()));// su profilo è 200.0/
                        photo = Bitmap.createScaledBitmap(photo, 512, nh, true);//su profilo è 200
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

        int id = item.getItemId();

        if (id == R.id.nav_home) {
            //activity Home
            getSupportActionBar().setTitle("Home");
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra(EXTRA_PACK, id);
            startActivity(intent);
        } else if (id == R.id.nav_wait) {
            //activity In Attesa
            getSupportActionBar().setTitle("In Attesa");
            Intent intent = new Intent(this, WaitingActivity.class);
            intent.putExtra(EXTRA_PACK, id);
            startActivity(intent);
        } else if (id == R.id.nav_history) {
            //activity Cronologia
            getSupportActionBar().setTitle("Cronologia");
            Intent intent = new Intent(this, HistoryActivity.class);
            intent.putExtra(EXTRA_PACK, id);
            intent.putExtra("Medico", medico);
            intent.putExtra("Paziente", paziente);
            startActivity(intent);
        } else if (id == R.id.nav_info) {
            //activity Info Dottore
            getSupportActionBar().setTitle("Dottore");

        } else if (id == R.id.nav_request) {
            //activity Invia Richiesta


        } else if (id == R.id.nav_user) {
            /*Intent intent = new Intent(this, UserActivity.class);
            startActivity(intent);*/
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
            progressDialog = ProgressDialog.show(BasicDrawerActivity.this, "Attendere", "Salvataggio della foto...", true);
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
        }
    }

    public class AsyncCallSoapRichieste extends AsyncTask<String, Void, ArrayList<Richiesta>> {

        @Override
        protected ArrayList<Richiesta> doInBackground(String... params) {
            CallSoap cs = new CallSoap();
            return cs.GetPazienteRequest(paziente.getCodiceFiscale());

        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(BasicDrawerActivity.this, "Attendere", "Aggiornamento richieste...", true);
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

    public void setNavigationview() {

        imageView = (CircleImageView) findViewById(R.id.imageViewClickable);
        textViewNome = (TextView) findViewById(R.id.textNome);
        textViewCF = (TextView) findViewById(R.id.textCodiceFiscale);

        //Setto le informazioni nel Drawer (nome,cognome, codice fiscale,foto da db)
        if (!utils.setUpInfoDrawer(BasicDrawerActivity.this, medico, paziente, textViewCF, textViewNome, imageView)) {
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