package com.example.andre.medicopaziente;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class Profilo extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ProgressDialog progressDialog;
    private String response;
    Toolbar toolbar;
    CircleImageView imageView;
    private String codiceFiscaleIntent, medicoIntent, nomeIntent, cognomeIntent, nomeCompletoIntent;
    private TextView textViewNome, textViewCF;
    private ArrayList<Richiesta> richiesteMedicoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncCallSoap().execute();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profilo, menu);
        imageView = (CircleImageView) findViewById(R.id.imageViewClickable);
        textViewNome = (TextView) findViewById(R.id.textNome);
        textViewCF = (TextView) findViewById(R.id.textCodiceFiscale);

        Intent intent = getIntent();
        String tipoUtente = intent.getStringExtra("tipoUtente");

        codiceFiscaleIntent = intent.getStringExtra("codiceFiscale");
        nomeIntent = intent.getStringExtra("nome");
        cognomeIntent = intent.getStringExtra("cognome");
        nomeCompletoIntent = nomeIntent + " " + cognomeIntent;

        if (tipoUtente.equals("Paziente")) {
            textViewNome.setText(nomeCompletoIntent);
            textViewCF.setText(codiceFiscaleIntent);
            medicoIntent = intent.getStringExtra("medico");
        } else if (tipoUtente.equals("Medico")) {
            textViewNome.setText("Dottor: " + nomeCompletoIntent);
            textViewCF.setText(codiceFiscaleIntent);
        }
        if (!readImageFromInternalStore(codiceFiscaleIntent)) {
            System.out.println("Errore lettura immagine");
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(Profilo.this);
                builder.setTitle("Foto profilo");
                builder.setMessage("");
                builder.setPositiveButton("CAMERA", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {

                        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takePicture, 0);


                    }
                });
                builder.setNegativeButton("GALLERIA", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {

                        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, 1);//one can be replaced with any action code

                    }
                });
                builder.show();

            }
        });

        return true;
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
                    Bitmap photo = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    imageView.setImageBitmap(photo);
                    if (!saveImageToInternalStorage(photo, codiceFiscaleIntent)) {
                        System.out.println("Errore nel salvare la foto da camera!");
                    }
                }

                break;
            case 1: //GALLERY
                if (resultCode == RESULT_OK) {
                    try {
                        Uri selectedImage = imageReturnedIntent.getData();
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                        if (!saveImageToInternalStorage(bitmap, codiceFiscaleIntent)) {
                            System.out.println("Errore nel salvare la foto da galleria!");
                        }
                        imageView.setImageURI(selectedImage);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    public boolean readImageFromInternalStore(String codiceFiscale) {
        boolean element = false;
        try {
            Bitmap bitmapA;
            FileInputStream fis = new FileInputStream (new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + codiceFiscale + ".jpeg"));

            bitmapA = BitmapFactory.decodeStream(fis);
            imageView.setImageBitmap(bitmapA);
            fis.close();
            element = true;

        } catch (FileNotFoundException e) {
            element = false;
            e.printStackTrace();
        } catch (IOException e) {
            element = false;
            e.printStackTrace();
        }
        return element;
    }

    public boolean saveImageToInternalStorage(Bitmap image, String codiceFiscale) {

        try {
            // Use the compress method on the Bitmap object to write image to
            // the OutputStream
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), codiceFiscale + ".jpeg");

            FileOutputStream fos = new FileOutputStream(file);

        } catch (Exception e) {
            System.out.println(e.getMessage().toString());
            Log.e("saveToInternalStorage()", e.getMessage());
            return false;
        }

        return true;
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

    public class AsyncCallSoap extends AsyncTask<Medico, Void, Medico> {

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(Profilo.this, "Waiting", "Send Request...", true);
            richiesteMedicoList = new ArrayList<>();
        }
        @Override
        protected Medico doInBackground(Medico... params) {
            CallSoap CS = new CallSoap();
            Medico medico = CS.GetMedicoInfo(medicoIntent);
            return medico;
        }


        @Override
        protected void onPostExecute(Medico s) {
            progressDialog.dismiss();


        }
    }
}
