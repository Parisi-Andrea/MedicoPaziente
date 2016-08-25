package com.example.andre.medicopaziente;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.View;
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

/**
 * Created by Annalisa on 23/08/2016.
 */
public class Manager {


    public String getPath(Uri uri, Activity activity) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
        activity.startManagingCursor(cursor);
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

    public boolean saveImageDb(Bitmap bitmap, String codiceFiscale){
        try
        {
            byte[] byteArray;
            String encodedImage;

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArray = stream.toByteArray();
            encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

            CallSoap cs = new CallSoap();
            cs.InsertImageToDB(encodedImage,codiceFiscale, MainActivity.tipoUtente);

        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public boolean saveImageInternalStorage(Bitmap bitmap,String codiceFiscale, Context context){
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
            MediaScannerConnection.scanFile(context, paths, mimeTypes, null);
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

    public boolean setUpInfoDrawer(Activity activity,Context context, Medico medico, Paziente paziente,
                                   TextView textViewCF, TextView textViewNome, ImageView imageView) {
        try {
            Intent intent = activity.getIntent();
            MainActivity.tipoUtente = (intent.getStringExtra("tipoUtente"));

            if (MainActivity.tipoUtente.equals("Medico")) {
                medico = intent.getParcelableExtra("Medico");
                textViewCF.setText(medico.getCodiceFiscale());
                textViewNome.setText("Dott. " + medico.getNome() + " " + medico.getCognome());
                if(medico.getImage()!=null) {
                    stringToImageView(imageView,medico.getImage());
                    if(!saveImageInternalStorage(stringToBitmap(medico.getImage()),medico.getCodiceFiscale(),context)) {
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
                    if(!saveImageInternalStorage(stringToBitmap(paziente.getImage()),paziente.getCodiceFiscale(),context)) {
                        System.out.println("Errore salvataggio foto da db a locale");
                    }

                }
                File dir = new File(Environment.getExternalStorageDirectory() + File.separator
                        + "MedicoPaziente" + File.separator + paziente.getCodiceFiscale());
                File dirInt = new File(Environment.getExternalStorageDirectory() + File.separator
                        + "MedicoPaziente" + File.separator + paziente.getCodiceFiscale() + File.separator + "Ricette");
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

    public void setNavigationview(final Activity activity, final Context context,
                                  ImageView imageView, TextView textViewNome, TextView textViewCF,
                                  Medico medico, Paziente paziente, Bitmap photo){

        imageView = (CircleImageView) activity.findViewById(R.id.imageViewClickable);
        textViewNome = (TextView) activity.findViewById(R.id.textNome);
        textViewCF = (TextView) activity.findViewById(R.id.textCodiceFiscale);

        //Setto le informazioni nel Drawer (nome,cognome, codice fiscale,foto da db)
        if (!setUpInfoDrawer(activity,context,medico,paziente,textViewCF,textViewNome,imageView))
        {
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Errore");
            alertDialog.setMessage("Info drawer non settate!");
            alertDialog.show();
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


    }

    public void creaRicettaRossa(Paziente paziente, Context applicationContext) {
        System.out.println("PIPPO");
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");

            Date now = new Date();

            String fileName = formatter.format(now);

            String outName = fileName + ".pdf";
            String INPUTFILE = Environment.getExternalStorageDirectory() + File.separator
                    + "MedicoPaziente" + File.separator + "RicettaRossa.pdf";

            String OUTPUTFILE = Environment.getExternalStorageDirectory() + File.separator
                    + "MedicoPaziente" + File.separator + paziente.getCodiceFiscale()
                    + File.separator + "Ricette" + File.separator + outName;

            //Create PdfReader instance.
            PdfReader pdfReader = new PdfReader(INPUTFILE);

            //Create PdfStamper instance.
            PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(OUTPUTFILE));

            PdfContentByte canvas = pdfStamper.getOverContent(1);

            ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(paziente.getCognome()
                    + " " + paziente.getNome()), 20, PageSize.A5.getWidth() - 25, 0);

            ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(paziente.getResidenza()),
                    20, PageSize.A5.getWidth() - 45, 0);

            ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT,
                    new Phrase(paziente.getCodiceFiscale().replace("", "  ").trim()),
                    PageSize.A5.getHeight() - 275, PageSize.A5.getWidth() - 95, 0);

            ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("100, 0"), 100, 0, 0);
            ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("100, 100"), 100, 100, 0);

            pdfStamper.close();
            pdfReader.close();

            System.out.println("PDF modified successfully.");
            Toast.makeText(applicationContext, "PDF modified successfully.", Toast.LENGTH_LONG).show();

        } catch (Exception e) {

            Toast.makeText(applicationContext, e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
    }


}
