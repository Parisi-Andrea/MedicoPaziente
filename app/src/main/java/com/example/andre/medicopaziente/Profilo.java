package com.example.andre.medicopaziente;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;


public class Profilo extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ProgressDialog progressDialog;
    Toolbar toolbar;
    CircleImageView imageView;
    private TextView textViewNome, textViewCF;
    private ArrayList<Richiesta> richiesteMedicoList;
    private Medico medico;
    private Paziente paziente;
    private Bitmap photo;

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
                //new AsyncCallSoap().execute();
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

        //Setto le informazioni nel Drawer (nome,cognome, codice fiscale)
        if (!setUpInfoDrawer())
        {
            return false;
        }
        //Cerco di recuperare l'immagine profilo salvata nella memoria interna "CodiceFiscale.png"
        if(medico!=null)
        {
            photo = readImageFromInternalStore(medico.getCodiceFiscale());
            if (photo == null)
            {
                System.out.println("Medico: Errore lettura immagine");
            }
            else
            {
                imageView.setImageBitmap(photo);
            }
        } else
        {
            photo = readImageFromInternalStore(paziente.getCodiceFiscale());
            if (photo == null)
            {
                System.out.println("Paziente: Errore lettura immagine");
            }
            else
            {
                imageView.setImageBitmap(photo);
            }
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

                    photo = (Bitmap) imageReturnedIntent.getExtras().get("data");

                    if  (medico==null) saveImageInternalStorage(photo, paziente.getCodiceFiscale());
                    else               saveImageInternalStorage(photo, medico.getCodiceFiscale());

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

                            copyFile(new File(getPath(imageReturnedIntent.getData())), file);

                        }
                        else
                        {
                            File myDir = new File(Environment.getExternalStorageDirectory(), File.separator + "MedicoPaziente");
                            String fname = medico.getCodiceFiscale() + ".png";
                            File file = new File(myDir, fname);


                            copyFile(new File(getPath(imageReturnedIntent.getData())), file);

                        }

                        imageView.setImageBitmap(photo);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        startManagingCursor(cursor);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void copyFile(File sourceFile, File destFile) throws IOException {
        if (!sourceFile.exists()) {
            return;
        }

        FileChannel source = null;
        FileChannel destination = null;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }
    }

    public boolean saveImageDb(Bitmap bitmap,String codiceFiscale){
        try
        {
            byte[] byteArray;
            String encodedImage;

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArray = stream.toByteArray();
            encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

            CallSoap cs = new CallSoap();
            cs.InsertImage(encodedImage,codiceFiscale);

        } catch (Exception ex) {
            return false;
        }
        return true;
    }
    public boolean saveImageInternalStorage(Bitmap bitmap,String codiceFiscale){
        File myDir = new File( Environment.getExternalStorageDirectory(),File.separator+"MedicoPaziente");

        if(!myDir.mkdirs()) {System.out.println("myDir.mkdirs() return false");}

        String fname = codiceFiscale+".png";
        File file = new File (myDir, fname);
        if (file.exists ()) {
            if(!file.delete()) {System.out.println("file.delete() return false");}
        }
        try {
            FileOutputStream out = new FileOutputStream(file); //from here it goes to catch block
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            String[] paths = {file.toString()};
            String[] mimeTypes = {"/image/png"};
            MediaScannerConnection.scanFile(this, paths, mimeTypes, null);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Bitmap readImageFromInternalStore(String codiceFiscale) {
        Bitmap b;
        try {
            File f=new File(Environment.getExternalStorageDirectory()+File.separator+"MedicoPaziente", codiceFiscale+".png");
            b = BitmapFactory.decodeStream(new FileInputStream(f));
        }
        catch (FileNotFoundException e)
        {
            b = null;
            e.printStackTrace();
        }
        return b;
    }

    public boolean setUpInfoDrawer() {
        try {
            Intent intent = getIntent();
            String tipoUtente = intent.getStringExtra("tipoUtente");

            if (tipoUtente.equals("Medico")) {
                medico = intent.getParcelableExtra("Medico");
                textViewCF.setText(medico.getCodiceFiscale());
                textViewNome.setText("Dott. " + medico.getNome() + " " + medico.getCognome());
                paziente = null;
            }
            else {
                paziente = intent.getParcelableExtra("Paziente");
                textViewCF.setText(paziente.getCodiceFiscale());
                textViewNome.setText(paziente.getNome() + " " + paziente.getCognome());
                medico = null;
            }
        } catch (Exception ex) {

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

    public class AsyncCallSoap extends AsyncTask<Bitmap, Void, String> {


        @Override
        protected String doInBackground(Bitmap... params) {
            saveImageDb(photo,paziente.getCodiceFiscale());
            return "OK";
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(Profilo.this, "Waiting", "Send Request...", true);
        }




        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
        }
    }
}