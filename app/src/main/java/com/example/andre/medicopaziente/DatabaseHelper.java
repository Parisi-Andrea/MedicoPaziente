package com.example.andre.medicopaziente;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by andre on 30/06/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "MedicoPaziente.db";

    // Table Names
    private static final String TABLE_MEDICO = "medico";
    private static final String TABLE_PAZIENTE = "paziente";
    private static final String TABLE_RICHIESTA = "richiesta";
    // Common column names
    private static final String KEY_ID = "cf";
    private static final String KEY_NOME = "nome";
    private static final String KEY_COGNOME = "cognome";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_NTEL = "n_tel";
    private static final String KEY_PASSWORD = "password";

    private static final String KEY_AMBULATORIO_MEDICO = "ambulatorio";
    private static final String KEY_ORARIO_MEDICO = "orario";

    private static final String KEY_DATA_NASCITA_PAZIENTE = "data_nascita";
    private static final String KEY_LUOGO_NASCITA_PAZIENTE = "luogo_nascita";
    private static final String KEY_RESIDENZA_PAZIENTE = "residenza";
    private static final String KEY_CF_MEDICO = "medico";

    private static final String KEY_ID_RICHIESTA = "id";
    private static final String KEY_STATO_RICHIESTA = "stato";
    private static final String KEY_TIPO_RICHIESTA = "tipo";
    private static final String KEY_DATA_RICHIESTA = "data_richiesta";
    private static final String KEY_NOTE_RICHIESTA = "note_richiesta";
    private static final String KEY_NOME_FARMACO = "nome_farmaco";
    private static final String KEY_QUANTITA_FARMACO = "quantita_farmaco";
    private static final String KEY_DATA_RISPOSTA_RICHIESTA = "data_risposta";
    private static final String KEY_NOTE_RISPOSTA_RICHIESTA = "note_risposta";
    private static final String KEY_CF_PAZIENTE_RICHIESTA = "cf_paziente";
    private static final String KEY_CF_MEDICO_RICHIESTA = "cf_medico";


    private static final String KEY_IMAGE = "image";

    private static final String KEY_CREATED_AT = "created_at";


    private static final String CREATE_TABLE_MEDICO = "CREATE TABLE IF NOT EXISTS "
            + TABLE_MEDICO + "("
            +  KEY_ID + " TEXT PRIMARY KEY,"
            +  KEY_NOME + " TEXT,"
            + KEY_COGNOME + " TEXT,"
            + KEY_EMAIL + " TEXT,"
            + KEY_NTEL + " TEXT,"
            + KEY_PASSWORD + " TEXT,"
            + KEY_AMBULATORIO_MEDICO + " TEXT,"
            + KEY_ORARIO_MEDICO + " TEXT,"
            + KEY_IMAGE +" TEXT" + ");";

    private static final String CREATE_TABLE_PAZIENTE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_PAZIENTE + "("
            +  KEY_ID + " TEXT PRIMARY KEY NOT NULL,"
            +  KEY_NOME + " TEXT,"
            + KEY_COGNOME + " TEXT,"
            + KEY_DATA_NASCITA_PAZIENTE + " TEXT,"
            + KEY_LUOGO_NASCITA_PAZIENTE + " TEXT,"
            + KEY_RESIDENZA_PAZIENTE + " TEXT,"
            + KEY_EMAIL + " TEXT,"
            + KEY_NTEL + " TEXT,"
            + KEY_PASSWORD + " TEXT,"
            + KEY_CF_MEDICO + " TEXT REFERENCES "+ TABLE_MEDICO+" ("+KEY_ID+"),"
            + KEY_IMAGE + " TEXT"+ ");";

    private static final String CREATE_TABLE_RICHIESTA = "CREATE TABLE IF NOT EXISTS "
            + TABLE_RICHIESTA + "("
            + KEY_ID_RICHIESTA + " INTEGER PRIMARY KEY NOT NULL,"
            + KEY_STATO_RICHIESTA + " TEXT,"
            + KEY_TIPO_RICHIESTA + " TEXT,"
            + KEY_DATA_RICHIESTA + " TEXT,"
            + KEY_NOTE_RICHIESTA + " TEXT,"
            + KEY_NOME_FARMACO + " TEXT,"
            + KEY_QUANTITA_FARMACO + " INTEGER,"
            + KEY_DATA_RISPOSTA_RICHIESTA + " TEXT,"
            + KEY_NOTE_RISPOSTA_RICHIESTA + " TEXT,"
            + KEY_CF_PAZIENTE_RICHIESTA + " TEXT REFERENCES "+ TABLE_PAZIENTE+" ("+KEY_ID+"),"
            + KEY_CF_MEDICO_RICHIESTA + " TEXT REFERENCES "+ TABLE_MEDICO+" ("+KEY_ID+")"
            + ");";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MEDICO);
        db.execSQL(CREATE_TABLE_PAZIENTE);
        db.execSQL(CREATE_TABLE_RICHIESTA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDICO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAZIENTE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RICHIESTA);

        // create new tables
        onCreate(db);
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public boolean createRequest(ArrayList<Richiesta> richiestaArrayList) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            for (int i = 0; i < richiestaArrayList.size(); i++) {
                Richiesta richiesta = richiestaArrayList.get(i);
                ContentValues values = new ContentValues();

                values.put(KEY_ID_RICHIESTA, richiesta.getIdRichiesta());
                values.put(KEY_STATO_RICHIESTA, richiesta.getStato());
                values.put(KEY_TIPO_RICHIESTA, richiesta.getTipo());
                values.put(KEY_DATA_RICHIESTA, richiesta.getData_richiesta());
                values.put(KEY_NOTE_RICHIESTA, richiesta.getNote_richiesta());
                values.put(KEY_NOME_FARMACO, richiesta.getNome_farmaco());
                values.put(KEY_QUANTITA_FARMACO, richiesta.getQuantita_farmaco());
                values.put(KEY_DATA_RISPOSTA_RICHIESTA, richiesta.getData_risposta());
                values.put(KEY_NOTE_RISPOSTA_RICHIESTA, richiesta.getNote_risposta());
                values.put(KEY_CF_PAZIENTE_RICHIESTA, richiesta.getCf_paziente());
                values.put(KEY_CF_MEDICO_RICHIESTA, richiesta.getCf_medico());

                String a = String.valueOf(db.insert(TABLE_RICHIESTA,null,values));
                if(a.equals("-1")) {
                    return false;
                }
            }

        } catch (SQLiteException s)
        {
            s.getMessage();
            return false;
        }

        return true;
    }
    public boolean createPaziente(Paziente paziente) {
        try
        {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_ID, paziente.getCodiceFiscale());
            values.put(KEY_NOME, paziente.getNome());
            values.put(KEY_COGNOME, paziente.getCognome());
            values.put(KEY_DATA_NASCITA_PAZIENTE,paziente.getDataNascita());
            values.put(KEY_LUOGO_NASCITA_PAZIENTE,paziente.getLuogoNascita());
            values.put(KEY_RESIDENZA_PAZIENTE,paziente.getResidenza());
            values.put(KEY_PASSWORD, paziente.getPassword());
            values.put(KEY_EMAIL,paziente.getEmail());
            values.put(KEY_NTEL,paziente.getNTel());
            values.put(KEY_CF_MEDICO,paziente.getMedico());
            values.put(KEY_IMAGE,paziente.getImage());
            String i = String.valueOf(db.insert(TABLE_PAZIENTE,null,values));
            if(i.equals("-1")) {
                return false;
            }


        } catch (SQLiteException e)
        {
            e.getMessage();
            return false;
        }
        return true;
    }

    public boolean createPazienti(ArrayList<Paziente> pazientiArrayList) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            for (int i = 0; i < pazientiArrayList.size(); i++) {
                Paziente paziente = pazientiArrayList.get(i);

                ContentValues values = new ContentValues();
                values.put(KEY_ID, paziente.getCodiceFiscale());
                values.put(KEY_NOME, paziente.getNome());
                values.put(KEY_COGNOME, paziente.getCognome());
                values.put(KEY_DATA_NASCITA_PAZIENTE,paziente.getDataNascita());
                values.put(KEY_LUOGO_NASCITA_PAZIENTE,paziente.getLuogoNascita());
                values.put(KEY_RESIDENZA_PAZIENTE,paziente.getResidenza());
                values.put(KEY_PASSWORD, paziente.getPassword());
                values.put(KEY_EMAIL,paziente.getEmail());
                values.put(KEY_NTEL,paziente.getNTel());
                values.put(KEY_CF_MEDICO,paziente.getMedico());
                values.put(KEY_IMAGE,paziente.getImage());
                String a = String.valueOf(db.insert(TABLE_PAZIENTE,null,values));
                if(a.equals("-1")) {
                    return false;
                }
            }

        } catch (SQLiteException s)
        {
            s.getMessage();
            return false;
        }

        return true;
    }

    public boolean createMedico(Medico medico) {
        try
        {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_ID, medico.getCodiceFiscale());
            values.put(KEY_NOME, medico.getNome());
            values.put(KEY_COGNOME, medico.getCognome());
            values.put(KEY_EMAIL, medico.getEmail());
            values.put(KEY_NTEL, medico.getNTel());
            values.put(KEY_PASSWORD, medico.getPassword());
            values.put(KEY_AMBULATORIO_MEDICO, medico.getAmbulatorio());
            values.put(KEY_ORARIO_MEDICO, medico.getOrario());
            values.put(KEY_IMAGE,medico.getImage());

            String i = String.valueOf(db.insert(TABLE_MEDICO,null,values));
            if(i.equals("-1")) {
                return false;
            }
            Log.e(LOG, i);
            int s = 10;
        } catch (SQLiteException e)
        {
            e.getMessage();
            return false;
        }
        return true;
    }
    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
    public Paziente getPaziente(String codiceFiscale) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "Select * FROM " + TABLE_PAZIENTE + " WHERE "
                +   KEY_ID + " =  \"" + codiceFiscale + "\"";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if(c.getCount()<=0)
        {
            return null;
        }
        if(c!=null) {
            c.moveToFirst();
            Paziente paziente = new Paziente();

            paziente.setCodiceFiscale(c.getString(c.getColumnIndex(KEY_ID)));
            paziente.setNome(c.getString(c.getColumnIndex(KEY_NOME)));
            paziente.setCognome(c.getString(c.getColumnIndex(KEY_COGNOME)));
            paziente.setEmail(c.getString(c.getColumnIndex(KEY_EMAIL)));
            paziente.setNTel(c.getString(c.getColumnIndex(KEY_NTEL)));
            paziente.setPassword(c.getString(c.getColumnIndex(KEY_PASSWORD)));
            paziente.setDataNascita(c.getString(c.getColumnIndex(KEY_DATA_NASCITA_PAZIENTE)));
            paziente.setLuogoNascita(c.getString(c.getColumnIndex(KEY_LUOGO_NASCITA_PAZIENTE)));
            paziente.setResidenza(c.getString(c.getColumnIndex(KEY_RESIDENZA_PAZIENTE)));
            paziente.setMedico(c.getString(c.getColumnIndex(KEY_CF_MEDICO)));
            paziente.setImage(c.getString(c.getColumnIndex(KEY_IMAGE)));

            return paziente;
        }
        else {
            return null;
        }
    }
    public Medico getMedico(String codiceFiscale) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "Select * FROM " + TABLE_MEDICO + " WHERE "
                +   KEY_ID + " =  \"" + codiceFiscale + "\"";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery,null);

        if(c.getCount()<=0)
        {
            return null;
        }
        if(c!=null) {
            c.moveToFirst();
            Medico medico = new Medico();
            medico.setCodiceFiscale(c.getString(c.getColumnIndex(KEY_ID)));
            medico.setNome(c.getString(c.getColumnIndex(KEY_NOME)));
            medico.setCognome(c.getString(c.getColumnIndex(KEY_COGNOME)));
            medico.setEmail(c.getString(c.getColumnIndex(KEY_EMAIL)));
            medico.setNTel(c.getString(c.getColumnIndex(KEY_NTEL)));
            medico.setPassword(c.getString(c.getColumnIndex(KEY_PASSWORD)));
            medico.setAmbulatorio(c.getString(c.getColumnIndex(KEY_AMBULATORIO_MEDICO)));
            medico.setOrario(c.getString(c.getColumnIndex(KEY_ORARIO_MEDICO)));
            medico.setImage(c.getString(c.getColumnIndex(KEY_IMAGE)));

            return medico;
        }
        else {
            return null;
        }
    }

    public int updateMedico(Medico medico) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_ID, medico.getCodiceFiscale());
            values.put(KEY_NOME, medico.getNome());
            values.put(KEY_COGNOME, medico.getCognome());
            values.put(KEY_EMAIL, medico.getEmail());
            values.put(KEY_NTEL, medico.getNTel());
            values.put(KEY_PASSWORD, medico.getPassword());
            values.put(KEY_AMBULATORIO_MEDICO, medico.getAmbulatorio());
            values.put(KEY_ORARIO_MEDICO, medico.getOrario());
            values.put(KEY_IMAGE, medico.getImage());

            int a = db.update(TABLE_MEDICO, values, KEY_ID + "='" + medico.getCodiceFiscale()+"'", null);
            return a;
        } catch (SQLiteException s)
        {
            s.getMessage();
            return -1;
        } catch (Exception e)
        {
            e.getMessage();
            return -1;
        }
    }

    public int updatePaziente(Paziente paziente) {
        try
        {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_ID, paziente.getCodiceFiscale());
            values.put(KEY_NOME, paziente.getNome());
            values.put(KEY_COGNOME, paziente.getCognome());
            values.put(KEY_DATA_NASCITA_PAZIENTE,paziente.getDataNascita());
            values.put(KEY_LUOGO_NASCITA_PAZIENTE,paziente.getLuogoNascita());
            values.put(KEY_RESIDENZA_PAZIENTE,paziente.getResidenza());
            values.put(KEY_PASSWORD, paziente.getPassword());
            values.put(KEY_EMAIL,paziente.getEmail());
            values.put(KEY_NTEL,paziente.getNTel());
            values.put(KEY_CF_MEDICO,paziente.getMedico());
            values.put(KEY_IMAGE,paziente.getImage());

            int a = db.update(TABLE_PAZIENTE, values, KEY_ID + "='" + paziente.getCodiceFiscale()+"'",null);
            return a;
        } catch (SQLiteException s)
        {
            s.getMessage();
            return -1;
        } catch (Exception e)
        {
            e.getMessage();
            return -1;
        }
    }

    public Richiesta getRichiesta(Integer idRichiesta) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "Select * FROM " + TABLE_RICHIESTA + " WHERE "
                +   KEY_ID_RICHIESTA + " =  " + idRichiesta;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery,null);

        if(c.getCount()<=0)
        {
            return null;
        }
        if(c!=null) {
            c.moveToFirst();
            Richiesta richiesta = new Richiesta();


            richiesta.setIdRichiesta(c.getInt(c.getColumnIndex(KEY_ID_RICHIESTA)));
            richiesta.setStato(c.getString(c.getColumnIndex(KEY_STATO_RICHIESTA)));
            richiesta.setTipo(c.getString(c.getColumnIndex(KEY_TIPO_RICHIESTA)));
            richiesta.setData_richiesta(c.getString(c.getColumnIndex(KEY_DATA_RICHIESTA)));
            richiesta.setNote_richiesta(c.getString(c.getColumnIndex(KEY_NOTE_RICHIESTA)));
            richiesta.setNome_farmaco(c.getString(c.getColumnIndex(KEY_NOME_FARMACO)));
            richiesta.setQuantita_farmaco(c.getInt(c.getColumnIndex(KEY_QUANTITA_FARMACO)));
            richiesta.setData_risposta(c.getString(c.getColumnIndex(KEY_DATA_RISPOSTA_RICHIESTA)));
            richiesta.setNote_risposta(c.getString(c.getColumnIndex(KEY_NOTE_RISPOSTA_RICHIESTA)));
            richiesta.setCf_paziente(c.getString(c.getColumnIndex(KEY_CF_PAZIENTE_RICHIESTA)));
            richiesta.setCf_medico(c.getString(c.getColumnIndex(KEY_CF_MEDICO_RICHIESTA)));

            return richiesta;
        }
        else {
            return null;
        }
    }

    public ArrayList<Richiesta> getAllPaziente(String cfPaziente) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "Select * FROM " + TABLE_RICHIESTA + " WHERE "
                +   KEY_CF_PAZIENTE_RICHIESTA + " =  \"" + cfPaziente + "\" ORDER BY " + KEY_DATA_RICHIESTA + " DESC";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery,null);

        if(c.getCount()<=0)
        {
            return null;
        }
        if(c!=null) {
            ArrayList<Richiesta> res= new ArrayList<Richiesta>();
            c.moveToFirst();
            do {
                Richiesta richiesta = new Richiesta();


                richiesta.setIdRichiesta(c.getInt(c.getColumnIndex(KEY_ID_RICHIESTA)));
                richiesta.setStato(c.getString(c.getColumnIndex(KEY_STATO_RICHIESTA)));
                richiesta.setTipo(c.getString(c.getColumnIndex(KEY_TIPO_RICHIESTA)));
                richiesta.setData_richiesta(c.getString(c.getColumnIndex(KEY_DATA_RICHIESTA)));
                richiesta.setNote_richiesta(c.getString(c.getColumnIndex(KEY_NOTE_RICHIESTA)));
                richiesta.setNome_farmaco(c.getString(c.getColumnIndex(KEY_NOME_FARMACO)));
                richiesta.setQuantita_farmaco(c.getInt(c.getColumnIndex(KEY_QUANTITA_FARMACO)));
                richiesta.setData_risposta(c.getString(c.getColumnIndex(KEY_DATA_RISPOSTA_RICHIESTA)));
                richiesta.setNote_risposta(c.getString(c.getColumnIndex(KEY_NOTE_RISPOSTA_RICHIESTA)));
                richiesta.setCf_paziente(c.getString(c.getColumnIndex(KEY_CF_PAZIENTE_RICHIESTA)));
                richiesta.setCf_medico(c.getString(c.getColumnIndex(KEY_CF_MEDICO_RICHIESTA)));

                res.add(richiesta);
            }while(c.moveToNext());
            return res;
        }
        else {
            return null;
        }
    }

    public ArrayList<Richiesta> getAllMedico(String cfMedico) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "Select * FROM " + TABLE_RICHIESTA + " WHERE "
                +   KEY_CF_MEDICO_RICHIESTA + " =  \"" + cfMedico + "\" ORDER BY " + KEY_DATA_RICHIESTA + " DESC";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery,null);

        if(c.getCount()<=0)
        {
            return null;
        }
        if(c!=null) {
            ArrayList<Richiesta> res= new ArrayList<Richiesta>();
            c.moveToFirst();
            do {
                Richiesta richiesta = new Richiesta();


                richiesta.setIdRichiesta(c.getInt(c.getColumnIndex(KEY_ID_RICHIESTA)));
                richiesta.setStato(c.getString(c.getColumnIndex(KEY_STATO_RICHIESTA)));
                richiesta.setTipo(c.getString(c.getColumnIndex(KEY_TIPO_RICHIESTA)));
                richiesta.setData_richiesta(c.getString(c.getColumnIndex(KEY_DATA_RICHIESTA)));
                richiesta.setNote_richiesta(c.getString(c.getColumnIndex(KEY_NOTE_RICHIESTA)));
                richiesta.setNome_farmaco(c.getString(c.getColumnIndex(KEY_NOME_FARMACO)));
                richiesta.setQuantita_farmaco(c.getInt(c.getColumnIndex(KEY_QUANTITA_FARMACO)));
                richiesta.setData_risposta(c.getString(c.getColumnIndex(KEY_DATA_RISPOSTA_RICHIESTA)));
                richiesta.setNote_risposta(c.getString(c.getColumnIndex(KEY_NOTE_RISPOSTA_RICHIESTA)));
                richiesta.setCf_paziente(c.getString(c.getColumnIndex(KEY_CF_PAZIENTE_RICHIESTA)));
                richiesta.setCf_medico(c.getString(c.getColumnIndex(KEY_CF_MEDICO_RICHIESTA)));

                res.add(richiesta);
            }while(c.moveToNext());
            return res;
        }
        else {
            return null;
        }
    }

    public ArrayList<Richiesta> getAttesaPaziente(String cfPaziente) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "Select * FROM " + TABLE_RICHIESTA + " WHERE "
                +   KEY_CF_PAZIENTE_RICHIESTA + " =  \"" + cfPaziente + "\" AND "
                + KEY_STATO_RICHIESTA + " =  \"" + "A" + "\" ORDER BY " + KEY_DATA_RICHIESTA + " DESC";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery,null);

        if(c.getCount()<=0)
        {
            return null;
        }
        if(c!=null) {
            ArrayList<Richiesta> res= new ArrayList<Richiesta>();
            c.moveToFirst();
            do {
                Richiesta richiesta = new Richiesta();


                richiesta.setIdRichiesta(c.getInt(c.getColumnIndex(KEY_ID_RICHIESTA)));
                richiesta.setStato(c.getString(c.getColumnIndex(KEY_STATO_RICHIESTA)));
                richiesta.setTipo(c.getString(c.getColumnIndex(KEY_TIPO_RICHIESTA)));
                richiesta.setData_richiesta(c.getString(c.getColumnIndex(KEY_DATA_RICHIESTA)));
                richiesta.setNote_richiesta(c.getString(c.getColumnIndex(KEY_NOTE_RICHIESTA)));
                richiesta.setNome_farmaco(c.getString(c.getColumnIndex(KEY_NOME_FARMACO)));
                richiesta.setQuantita_farmaco(c.getInt(c.getColumnIndex(KEY_QUANTITA_FARMACO)));
                richiesta.setData_risposta(c.getString(c.getColumnIndex(KEY_DATA_RISPOSTA_RICHIESTA)));
                richiesta.setNote_risposta(c.getString(c.getColumnIndex(KEY_NOTE_RISPOSTA_RICHIESTA)));
                richiesta.setCf_paziente(c.getString(c.getColumnIndex(KEY_CF_PAZIENTE_RICHIESTA)));
                richiesta.setCf_medico(c.getString(c.getColumnIndex(KEY_CF_MEDICO_RICHIESTA)));

                res.add(richiesta);
            }while(c.moveToNext());
            return res;
        }
        else {
            return null;
        }
    }

    public ArrayList<Richiesta> getCompletatePaziente(String cfPaziente) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "Select * FROM " + TABLE_RICHIESTA + " WHERE "
                +   KEY_CF_PAZIENTE_RICHIESTA + " =  \"" + cfPaziente + "\" AND "
                + KEY_STATO_RICHIESTA + " =  \"" + "C" + "\" ORDER BY " + KEY_DATA_RISPOSTA_RICHIESTA + " DESC";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery,null);

        if(c.getCount()<=0)
        {
            return null;
        }
        if(c!=null) {
            ArrayList<Richiesta> res= new ArrayList<Richiesta>();
            c.moveToFirst();
            do {
                Richiesta richiesta = new Richiesta();


                richiesta.setIdRichiesta(c.getInt(c.getColumnIndex(KEY_ID_RICHIESTA)));
                richiesta.setStato(c.getString(c.getColumnIndex(KEY_STATO_RICHIESTA)));
                richiesta.setTipo(c.getString(c.getColumnIndex(KEY_TIPO_RICHIESTA)));
                richiesta.setData_richiesta(c.getString(c.getColumnIndex(KEY_DATA_RICHIESTA)));
                richiesta.setNote_richiesta(c.getString(c.getColumnIndex(KEY_NOTE_RICHIESTA)));
                richiesta.setNome_farmaco(c.getString(c.getColumnIndex(KEY_NOME_FARMACO)));
                richiesta.setQuantita_farmaco(c.getInt(c.getColumnIndex(KEY_QUANTITA_FARMACO)));
                richiesta.setData_risposta(c.getString(c.getColumnIndex(KEY_DATA_RISPOSTA_RICHIESTA)));
                richiesta.setNote_risposta(c.getString(c.getColumnIndex(KEY_NOTE_RISPOSTA_RICHIESTA)));
                richiesta.setCf_paziente(c.getString(c.getColumnIndex(KEY_CF_PAZIENTE_RICHIESTA)));
                richiesta.setCf_medico(c.getString(c.getColumnIndex(KEY_CF_MEDICO_RICHIESTA)));

                res.add(richiesta);
            }while(c.moveToNext());
            return res;
        }
        else {
            return null;
        }
    }

    public ArrayList<Richiesta> getRifiutatePaziente(String cfPaziente) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "Select * FROM " + TABLE_RICHIESTA + " WHERE "
                +   KEY_CF_PAZIENTE_RICHIESTA + " =  \"" + cfPaziente + "\" AND "
                + KEY_STATO_RICHIESTA + " =  \"" + "R" + "\" ORDER BY " + KEY_DATA_RISPOSTA_RICHIESTA + " DESC";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery,null);

        if(c.getCount()<=0)
        {
            return null;
        }
        if(c!=null) {
            ArrayList<Richiesta> res= new ArrayList<Richiesta>();
            c.moveToFirst();
            do {
                Richiesta richiesta = new Richiesta();


                richiesta.setIdRichiesta(c.getInt(c.getColumnIndex(KEY_ID_RICHIESTA)));
                richiesta.setStato(c.getString(c.getColumnIndex(KEY_STATO_RICHIESTA)));
                richiesta.setTipo(c.getString(c.getColumnIndex(KEY_TIPO_RICHIESTA)));
                richiesta.setData_richiesta(c.getString(c.getColumnIndex(KEY_DATA_RICHIESTA)));
                richiesta.setNote_richiesta(c.getString(c.getColumnIndex(KEY_NOTE_RICHIESTA)));
                richiesta.setNome_farmaco(c.getString(c.getColumnIndex(KEY_NOME_FARMACO)));
                richiesta.setQuantita_farmaco(c.getInt(c.getColumnIndex(KEY_QUANTITA_FARMACO)));
                richiesta.setData_risposta(c.getString(c.getColumnIndex(KEY_DATA_RISPOSTA_RICHIESTA)));
                richiesta.setNote_risposta(c.getString(c.getColumnIndex(KEY_NOTE_RISPOSTA_RICHIESTA)));
                richiesta.setCf_paziente(c.getString(c.getColumnIndex(KEY_CF_PAZIENTE_RICHIESTA)));
                richiesta.setCf_medico(c.getString(c.getColumnIndex(KEY_CF_MEDICO_RICHIESTA)));

                res.add(richiesta);
            }while(c.moveToNext());
            return res;
        }
        else {
            return null;
        }
    }

    public ArrayList<Richiesta> getAttesaMedico(String cfMedico) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "Select * FROM " + TABLE_RICHIESTA + " WHERE "
                +   KEY_CF_MEDICO_RICHIESTA + " =  \"" + cfMedico + "\" AND "
                + KEY_STATO_RICHIESTA + " =  \"" + "A" + "\" ORDER BY " + KEY_DATA_RICHIESTA + " DESC";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery,null);

        if(c.getCount()<=0)
        {
            return null;
        }
        if(c!=null) {
            ArrayList<Richiesta> res= new ArrayList<Richiesta>();
            c.moveToFirst();
            do {
                Richiesta richiesta = new Richiesta();


                richiesta.setIdRichiesta(c.getInt(c.getColumnIndex(KEY_ID_RICHIESTA)));
                richiesta.setStato(c.getString(c.getColumnIndex(KEY_STATO_RICHIESTA)));
                richiesta.setTipo(c.getString(c.getColumnIndex(KEY_TIPO_RICHIESTA)));
                richiesta.setData_richiesta(c.getString(c.getColumnIndex(KEY_DATA_RICHIESTA)));
                richiesta.setNote_richiesta(c.getString(c.getColumnIndex(KEY_NOTE_RICHIESTA)));
                richiesta.setNome_farmaco(c.getString(c.getColumnIndex(KEY_NOME_FARMACO)));
                richiesta.setQuantita_farmaco(c.getInt(c.getColumnIndex(KEY_QUANTITA_FARMACO)));
                richiesta.setData_risposta(c.getString(c.getColumnIndex(KEY_DATA_RISPOSTA_RICHIESTA)));
                richiesta.setNote_risposta(c.getString(c.getColumnIndex(KEY_NOTE_RISPOSTA_RICHIESTA)));
                richiesta.setCf_paziente(c.getString(c.getColumnIndex(KEY_CF_PAZIENTE_RICHIESTA)));
                richiesta.setCf_medico(c.getString(c.getColumnIndex(KEY_CF_MEDICO_RICHIESTA)));

                res.add(richiesta);
            }while(c.moveToNext());
            return res;
        }
        else {
            return null;
        }
    }

    public ArrayList<Richiesta> getCompletateMedico(String cfMedico) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "Select * FROM " + TABLE_RICHIESTA + " WHERE "
                +   KEY_CF_MEDICO_RICHIESTA + " =  \"" + cfMedico + "\" AND "
                + KEY_STATO_RICHIESTA + " =  \"" + "C" + "\" ORDER BY " + KEY_DATA_RISPOSTA_RICHIESTA + " DESC";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery,null);

        if(c.getCount()<=0)
        {
            return null;
        }
        if(c!=null) {
            ArrayList<Richiesta> res= new ArrayList<Richiesta>();
            c.moveToFirst();
            do {
                Richiesta richiesta = new Richiesta();


                richiesta.setIdRichiesta(c.getInt(c.getColumnIndex(KEY_ID_RICHIESTA)));
                richiesta.setStato(c.getString(c.getColumnIndex(KEY_STATO_RICHIESTA)));
                richiesta.setTipo(c.getString(c.getColumnIndex(KEY_TIPO_RICHIESTA)));
                richiesta.setData_richiesta(c.getString(c.getColumnIndex(KEY_DATA_RICHIESTA)));
                richiesta.setNote_richiesta(c.getString(c.getColumnIndex(KEY_NOTE_RICHIESTA)));
                richiesta.setNome_farmaco(c.getString(c.getColumnIndex(KEY_NOME_FARMACO)));
                richiesta.setQuantita_farmaco(c.getInt(c.getColumnIndex(KEY_QUANTITA_FARMACO)));
                richiesta.setData_risposta(c.getString(c.getColumnIndex(KEY_DATA_RISPOSTA_RICHIESTA)));
                richiesta.setNote_risposta(c.getString(c.getColumnIndex(KEY_NOTE_RISPOSTA_RICHIESTA)));
                richiesta.setCf_paziente(c.getString(c.getColumnIndex(KEY_CF_PAZIENTE_RICHIESTA)));
                richiesta.setCf_medico(c.getString(c.getColumnIndex(KEY_CF_MEDICO_RICHIESTA)));

                res.add(richiesta);
            }while(c.moveToNext());
            return res;
        }
        else {
            return null;
        }
    }

    public ArrayList<Richiesta> getRifiutateMedico(String cfMedico) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "Select * FROM " + TABLE_RICHIESTA + " WHERE "
                +   KEY_CF_MEDICO_RICHIESTA + " =  \"" + cfMedico + "\" AND "
                + KEY_STATO_RICHIESTA + " =  \"" + "R" + "\" ORDER BY " + KEY_DATA_RISPOSTA_RICHIESTA + " DESC";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery,null);

        if(c.getCount()<=0)
        {
            return null;
        }
        if(c!=null) {
            ArrayList<Richiesta> res= new ArrayList<Richiesta>();
            c.moveToFirst();
            do {
                Richiesta richiesta = new Richiesta();


                richiesta.setIdRichiesta(c.getInt(c.getColumnIndex(KEY_ID_RICHIESTA)));
                richiesta.setStato(c.getString(c.getColumnIndex(KEY_STATO_RICHIESTA)));
                richiesta.setTipo(c.getString(c.getColumnIndex(KEY_TIPO_RICHIESTA)));
                richiesta.setData_richiesta(c.getString(c.getColumnIndex(KEY_DATA_RICHIESTA)));
                richiesta.setNote_richiesta(c.getString(c.getColumnIndex(KEY_NOTE_RICHIESTA)));
                richiesta.setNome_farmaco(c.getString(c.getColumnIndex(KEY_NOME_FARMACO)));
                richiesta.setQuantita_farmaco(c.getInt(c.getColumnIndex(KEY_QUANTITA_FARMACO)));
                richiesta.setData_risposta(c.getString(c.getColumnIndex(KEY_DATA_RISPOSTA_RICHIESTA)));
                richiesta.setNote_risposta(c.getString(c.getColumnIndex(KEY_NOTE_RISPOSTA_RICHIESTA)));
                richiesta.setCf_paziente(c.getString(c.getColumnIndex(KEY_CF_PAZIENTE_RICHIESTA)));
                richiesta.setCf_medico(c.getString(c.getColumnIndex(KEY_CF_MEDICO_RICHIESTA)));

                res.add(richiesta);
            }while(c.moveToNext());
            return res;
        }
        else {
            return null;
        }
    }
    public ArrayList<Richiesta> getTipoRichieste(String cfPaziente, String tipo) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "Select * FROM " + TABLE_RICHIESTA + " WHERE "
                +   KEY_CF_PAZIENTE_RICHIESTA + " =  \"" + cfPaziente + "\" AND "
                +   KEY_TIPO_RICHIESTA + " =  \"" + tipo + "\" ORDER BY " + KEY_DATA_RICHIESTA + " DESC";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery,null);

        if(c.getCount()<=0)
        {
            return null;
        }
        if(c!=null) {
            ArrayList<Richiesta> res= new ArrayList<Richiesta> ();
            c.moveToFirst();
            do {
                Richiesta richiesta = new Richiesta();


                richiesta.setIdRichiesta(c.getInt(c.getColumnIndex(KEY_ID_RICHIESTA)));
                richiesta.setStato(c.getString(c.getColumnIndex(KEY_STATO_RICHIESTA)));
                richiesta.setTipo(c.getString(c.getColumnIndex(KEY_TIPO_RICHIESTA)));
                richiesta.setData_richiesta(c.getString(c.getColumnIndex(KEY_DATA_RICHIESTA)));
                richiesta.setNote_richiesta(c.getString(c.getColumnIndex(KEY_NOTE_RICHIESTA)));
                richiesta.setNome_farmaco(c.getString(c.getColumnIndex(KEY_NOME_FARMACO)));
                richiesta.setQuantita_farmaco(c.getInt(c.getColumnIndex(KEY_QUANTITA_FARMACO)));
                richiesta.setData_risposta(c.getString(c.getColumnIndex(KEY_DATA_RISPOSTA_RICHIESTA)));
                richiesta.setNote_risposta(c.getString(c.getColumnIndex(KEY_NOTE_RISPOSTA_RICHIESTA)));
                richiesta.setCf_paziente(c.getString(c.getColumnIndex(KEY_CF_PAZIENTE_RICHIESTA)));
                richiesta.setCf_medico(c.getString(c.getColumnIndex(KEY_CF_MEDICO_RICHIESTA)));

                res.add(richiesta);
            }while(c.moveToNext());
            return res;
        }
        else {
            return null;
        }
    }
    public ArrayList<Paziente> getPazientiMedico(String cfMedico) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "Select * FROM " + TABLE_PAZIENTE + " WHERE "
                +   KEY_CF_MEDICO + " =  \"" + cfMedico + "\"";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery,null);

        if(c.getCount()<=0)
        {
            return null;
        }
        if(c!=null) {
            ArrayList<Paziente> res= new ArrayList<>();
            c.moveToFirst();
            do {
                Paziente paziente = new Paziente();

                paziente.setCodiceFiscale(c.getString(c.getColumnIndex(KEY_ID)));
                paziente.setNome(c.getString(c.getColumnIndex(KEY_NOME)));
                paziente.setCognome(c.getString(c.getColumnIndex(KEY_COGNOME)));
                paziente.setEmail(c.getString(c.getColumnIndex(KEY_EMAIL)));
                paziente.setNTel(c.getString(c.getColumnIndex(KEY_NTEL)));
                paziente.setPassword(c.getString(c.getColumnIndex(KEY_PASSWORD)));
                paziente.setDataNascita(c.getString(c.getColumnIndex(KEY_DATA_NASCITA_PAZIENTE)));
                paziente.setLuogoNascita(c.getString(c.getColumnIndex(KEY_LUOGO_NASCITA_PAZIENTE)));
                paziente.setResidenza(c.getString(c.getColumnIndex(KEY_RESIDENZA_PAZIENTE)));
                paziente.setMedico(c.getString(c.getColumnIndex(KEY_CF_MEDICO)));
                paziente.setImage(c.getString(c.getColumnIndex(KEY_IMAGE)));

                res.add(paziente);
            }while(c.moveToNext());
            return res;
        }
        else {
            return null;
        }
    }

}
