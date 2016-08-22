package com.example.andre.medicopaziente;
import android.app.ProgressDialog;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import de.hdodenhof.circleimageview.CircleImageView;


public class Profilo extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ProgressDialog progressDialog;
    Toolbar toolbar;
    CircleImageView imageView;
    private TextView textViewNome, textViewCF;
    private Medico medico;
    private Paziente paziente;
    private Bitmap photo;
    String tipoUtente;

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
                //creaRicettaRossa(paziente);

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

        //Setto le informazioni nel Drawer (nome,cognome, codice fiscale,foto da db)
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
        } else //Se è paziente, se nel db non c'è la foto cerco di prenderla dalla memoria interna
        {
            if(paziente.getImage() == null) {
                photo = readImageFromInternalStore(paziente.getCodiceFiscale());
                if (photo == null) {
                    System.out.println("Paziente: Errore lettura immagine");
                } else {
                    imageView.setImageBitmap(photo);
                }
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

                    new AsyncCallSoap().execute();
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

                        //scalo immagine per vederla in circleimageView
                        int nh = (int) ( photo.getHeight() * (512.0 / photo.getWidth()) );
                        photo = Bitmap.createScaledBitmap(photo, 512, nh, true);
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

    public class AsyncCallSoap extends AsyncTask<Bitmap, Void, String> {


        @Override
        protected String doInBackground(Bitmap... params) {
            if(tipoUtente.equals("Paziente")){  saveImageDb(photo,paziente.getCodiceFiscale());}
            else if(tipoUtente.equals("Medico")){saveImageDb(photo,medico.getCodiceFiscale());}
            return "OK";
        }

        @Override
        protected void onPreExecute()   {
            progressDialog = ProgressDialog.show(Profilo.this, "Attendere", "Salvataggio della foto...", true);
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
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

    public void copyFile(File sourceFile, File destFile) throws IOException {
        if (!sourceFile.exists()) {
            return;
        }

        FileChannel source;
        FileChannel destination;
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
            cs.InsertImageToDB(encodedImage,codiceFiscale, tipoUtente);

        } catch (Exception ex) {
            return false;
        }
        return true;
    }
    public boolean saveImageInternalStorage(Bitmap bitmap,String codiceFiscale){
        File myDir = new File( Environment.getExternalStorageDirectory(),File.separator+"MedicoPaziente"+File.separator+codiceFiscale);

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
            File f=new File(Environment.getExternalStorageDirectory()+File.separator+"MedicoPaziente"+File.separator+codiceFiscale, codiceFiscale+".png");
            b = BitmapFactory.decodeStream(new FileInputStream(f));
        }
        catch (FileNotFoundException e)
        {
            b = null;
            e.printStackTrace();
        }
        return b;
    }
    public void stringToImageView(ImageView imageView, String encodedImage)   {
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imageView.setImageBitmap(decodedByte);
    }
    public Bitmap stringToBitmap(String encodedImage) {
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
    public boolean setUpInfoDrawer() {
        try {
            Intent intent = getIntent();
            tipoUtente = intent.getStringExtra("tipoUtente");

            if (tipoUtente.equals("Medico")) {
                medico = intent.getParcelableExtra("Medico");
                textViewCF.setText(medico.getCodiceFiscale());
                textViewNome.setText("Dott. " + medico.getNome() + " " + medico.getCognome());
                if(medico.getImage()!=null) {
                    stringToImageView(imageView,medico.getImage());
                    if(!saveImageInternalStorage(stringToBitmap(medico.getImage()),medico.getCodiceFiscale())) {
                        System.out.println("Errore salvataggio foto da db a locale");
                    }

                }
                paziente = null;
            }
            else {
                paziente = intent.getParcelableExtra("Paziente");
                textViewCF.setText(paziente.getCodiceFiscale());
                textViewNome.setText(paziente.getNome() + " " + paziente.getCognome());
                if(paziente.getImage()!=null) {
                    stringToImageView(imageView,paziente.getImage());
                    if(!saveImageInternalStorage(stringToBitmap(paziente.getImage()),paziente.getCodiceFiscale())) {
                        System.out.println("Errore salvataggio foto da db a locale");
                    }

                }
                File dir = new File(Environment.getExternalStorageDirectory() + File.separator + "MedicoPaziente" + File.separator + paziente.getCodiceFiscale());
                File dirInt = new File(Environment.getExternalStorageDirectory() + File.separator + "MedicoPaziente" + File.separator + paziente.getCodiceFiscale() + File.separator + "Ricette");
                if(!dir.exists())
                {
                    dir.mkdirs();
                }
                if(!dirInt.exists())
                {
                    dirInt.mkdirs();
                }
                medico = null;
            }
        } catch (Exception ex) {

            return false;
        }
        return true;
    }

    public void creaRicettaRossa(Paziente paziente) {
        System.out.println("PIPPO");
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");

            Date now = new Date();

            String fileName = formatter.format(now);

            String outName = fileName + ".pdf";
            String INPUTFILE = Environment.getExternalStorageDirectory() + File.separator + "MedicoPaziente" + File.separator + "RicettaRossa.pdf";
            String OUTPUTFILE = Environment.getExternalStorageDirectory() + File.separator + "MedicoPaziente" + File.separator + paziente.getCodiceFiscale() + File.separator + "Ricette" + File.separator + outName;
            //Create PdfReader instance.
            PdfReader pdfReader = new PdfReader(INPUTFILE);
            //Create PdfStamper instance.
            PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(OUTPUTFILE));

            PdfContentByte canvas = pdfStamper.getOverContent(1);

            ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(paziente.getCognome() + " " + paziente.getNome()), 20, PageSize.A5.getWidth() - 25, 0);
            ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(paziente.getResidenza()), 20, PageSize.A5.getWidth() - 45, 0);
            ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(paziente.getCodiceFiscale().replace("", "  ").trim()), PageSize.A5.getHeight() - 275, PageSize.A5.getWidth() - 95, 0);
            ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("100, 0"), 100, 0, 0);
            ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("100, 100"), 100, 100, 0);

            pdfStamper.close();
            pdfReader.close();

            System.out.println("PDF modified successfully.");
            Toast.makeText(getApplicationContext(), "PDF modified successfully.", Toast.LENGTH_LONG).show();

        } catch (Exception e) {

            Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
    }

}