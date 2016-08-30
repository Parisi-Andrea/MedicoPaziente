package com.example.andre.medicopaziente;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
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
 * Created by andre on 24/08/2016.
 */
public class Utils {

    public static boolean executePingWebService(String ip){
        System.out.println("executeCommand");
        Runtime runtime = Runtime.getRuntime();
        try
        {
            Process  mIpAddrProcess = runtime.exec("/system/bin/ping -c 1 "+ip);
            int mExitValue = mIpAddrProcess.waitFor();
            System.out.println(" mExitValue "+mExitValue);
            if(mExitValue==0){
                return true;
            }else{
                return false;
            }
        }
        catch (InterruptedException ignore)
        {
            ignore.printStackTrace();
            System.out.println(" Exception:"+ignore);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.out.println(" Exception:"+e);
        }
        return false;
    }

    public static boolean isConnectedViaWifi(Activity activity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return mWifi.isConnected();
    }

    public void creaRicettaRossa(Paziente paziente, Activity activity) {
        //System.out.println("PIPPO");
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
            Toast.makeText(activity.getApplicationContext(), "PDF modified successfully.", Toast.LENGTH_LONG).show();

        } catch (Exception e) {

            Toast.makeText(activity.getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
        return;
    }

    public boolean setUpInfoDrawer(Activity activity, Medico medico, Paziente paziente, TextView textViewCF,
                                   TextView textViewNome, ImageView imageView) {
        try {
            if(medico != null) {
                textViewCF.setText(medico.getCodiceFiscale());
                textViewNome.setText("Dott. " + medico.getNome() + " " + medico.getCognome());
                if(medico.getImage()!=null) {
                    stringToImageView(imageView,medico.getImage());
                    if(!saveImageInternalStorage(stringToBitmap(medico.getImage()),medico.getCodiceFiscale(),activity)) {
                        System.out.println("Errore salvataggio foto da db a locale");
                    }

                }
                paziente = null;
            }
            else {
                textViewCF.setText(paziente.getCodiceFiscale());
                textViewNome.setText(paziente.getNome() + " " + paziente.getCognome());
                if(paziente.getImage()!=null) {
                    stringToImageView(imageView,paziente.getImage());
                    if(!saveImageInternalStorage(stringToBitmap(paziente.getImage()),paziente.getCodiceFiscale(),activity)) {
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

    public void stringToImageView(ImageView imageView, String encodedImage)   {
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imageView.setImageBitmap(decodedByte);
    }

    public Bitmap stringToBitmap(String encodedImage) {
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
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

    public boolean saveImageInternalStorage(Bitmap bitmap,String codiceFiscale,Activity activity){
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
            MediaScannerConnection.scanFile(activity.getBaseContext(), paths, mimeTypes, null);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean saveImageDb(Bitmap bitmap,String codiceFiscale,String tipoUtente){
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

    public String getPath(Uri uri, Activity activity) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
        activity.startManagingCursor(cursor);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public static void createSnackBar(Activity activity,String s, int length,int color) {
        Snackbar snackbar = Snackbar
                .make(activity.getWindow().getDecorView().findViewById(android.R.id.content), s, length);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }

    public void creaRicettaRossa(Paziente paziente, Activity activity,Richiesta richiesta) {
        try {
            /*SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");

            Date now = new Date();

            String fileName = formatter.format(now);*/
            String fileName = richiesta.getData_risposta().replace(" ","_").replace("/","_");
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
            ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(richiesta.getNome_farmaco()), 25, PageSize.A5.getWidth() - 155, 0);
            ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(paziente.getMedico()), PageSize.A5.getHeight() - 160, PageSize.A5.getWidth() - 250, 0);
            ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(String.valueOf(richiesta.getQuantita_farmaco()).replace("", "  ").trim()), 55, PageSize.A5.getWidth() - 252, 0);
            ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(richiesta.getData_risposta().substring(2,10).replace("/","").replace("","   ")),PageSize.A5.getHeight() - 292,PageSize.A5.getWidth() - 255, 0);
            pdfStamper.close();
            pdfReader.close();

            System.out.println("PDF modified successfully.");
            Toast.makeText(activity.getApplicationContext(), "Ricetta scaricata correttamente.", Toast.LENGTH_LONG).show();

        } catch (Exception e) {

            Toast.makeText(activity.getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
        return;
    }
}
