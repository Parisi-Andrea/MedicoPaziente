package com.example.andre.medicopaziente;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class BasicDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public final static String EXTRA_PACK = "ID_";
    Manager util = new Manager(); //necessario??

    Toolbar toolbar;
    CircleImageView imageView;

    private Medico medico;
    private Paziente paziente;
    private Bitmap photo;
    //public String tipoUtente;
    //private TextView textViewNome, textViewCF;
    //private ProgressDialog progressDialog;
    //public String login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_drawer);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        Intent intent = getIntent();
        int id = intent.getIntExtra(EXTRA_PACK,0);
        navigationView.setCheckedItem(id);

        if (MainActivity.tipoUtente.equals("Medico")) {
            //medico = intent.getParcelableExtra("Medico");
            paziente = null;
            Menu drawermenu = navigationView.getMenu();
            drawermenu.removeItem(R.id.nav_request);
            drawermenu.getItem(3).setTitle("Informazioni Pazienti");
        }
        else {
            //paziente = intent.getParcelableExtra("Paziente");
            medico = null;
        }

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

                    if  (medico==null) util.saveImageInternalStorage(photo, paziente.getCodiceFiscale(),this);
                    else               util.saveImageInternalStorage(photo, medico.getCodiceFiscale(),this);

                    imageView.setImageBitmap(photo);

                    //new AsyncCallSoap().execute();
                }

                break;
            case 1: //GALLERY
                if (resultCode == RESULT_OK) {
                    try {
                        Uri selectedImage = imageReturnedIntent.getData();
                        photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

                        if(medico == null) {

                            File myDir = new File(Environment.getExternalStorageDirectory(), File.separator + "MedicoPaziente");
                            String fname = paziente.getCodiceFiscale() + ".png";
                            File file = new File(myDir, fname);

                            util.copyFile(new File(util.getPath(imageReturnedIntent.getData(),this)), file);

                        }
                        else
                        {
                            File myDir = new File(Environment.getExternalStorageDirectory(), File.separator + "MedicoPaziente");
                            String fname = medico.getCodiceFiscale() + ".png";
                            File file = new File(myDir, fname);


                            util.copyFile(new File(util.getPath(imageReturnedIntent.getData(), this)), file);

                        }

                        //scalo immagine per vederla in circleimageView
                        int nh = (int) ( photo.getHeight() * (512.0 / photo.getWidth()) );
                        photo = Bitmap.createScaledBitmap(photo, 512, nh, true);
                        imageView.setImageBitmap(photo);

                        //new AsyncCallSoap().execute();

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
            intent.putExtra(EXTRA_PACK,id);
            startActivity(intent);
        } else if (id == R.id.nav_wait) {
            //activity In Attesa
            getSupportActionBar().setTitle("In Attesa");
            Intent intent = new Intent(this, WaitingActivity.class);
            intent.putExtra(EXTRA_PACK,id);
            startActivity(intent);
        } else if (id == R.id.nav_history) {
            //activity Cronologia
            getSupportActionBar().setTitle("Cronologia");
            Intent intent = new Intent(this, HistoryActivity.class);
            intent.putExtra(EXTRA_PACK,id);
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


   /* public class AsyncCallSoap extends AsyncTask<Bitmap, Void, String> {

        @Override
        protected String doInBackground(Bitmap... params) {
            if(tipoUtente.equals("Paziente")){  util.saveImageDb(photo, paziente.getCodiceFiscale());}
            else if(tipoUtente.equals("Medico")){ util.saveImageDb(photo, medico.getCodiceFiscale());}
            return "OK";
        }

        @Override
        protected void onPreExecute()   {
            progressDialog = ProgressDialog.show(BasicDrawerActivity.this, "Attendere", "Salvataggio della foto...", true);
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
        }
    }*/
}
