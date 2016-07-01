package com.example.andre.medicopaziente;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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


    private static final String KEY_CREATED_AT = "created_at";


    private static final String CREATE_TABLE_MEDICO = "CREATE TABLE "
            + TABLE_MEDICO + "("
            +  KEY_ID + " TEXT PRIMARY KEY,"
            +  KEY_NOME + " TEXT,"
            + KEY_COGNOME + " TEXT,"
            + KEY_EMAIL + " TEXT,"
            + KEY_NTEL + " TEXT,"
            + KEY_PASSWORD + " TEXT,"
            + KEY_AMBULATORIO_MEDICO + " TEXT,"
            + KEY_ORARIO_MEDICO + " TEXT" + ");";

    private static final String CREATE_TABLE_PAZIENTE = "CREATE TABLE "
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
            + KEY_CF_MEDICO + " TEXT REFERENCES "+ TABLE_MEDICO+" ("+KEY_ID+")"
            + ");";

    private static final String CREATE_TABLE_RICHIESTA = "CREATE TABLE "
            + TABLE_RICHIESTA + "("
            + KEY_ID_RICHIESTA + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
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

            long a = db.insert(TABLE_PAZIENTE,null,values);
            Log.e(LOG,a+"");
            int s = 10;

        } catch (SQLiteException e)
        {
            e.getMessage();
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

            long a = db.insert(TABLE_MEDICO,null,values);
            Log.e(LOG,a+"");
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

        Cursor c = db.rawQuery(selectQuery,null);

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

            return medico;
        }
        else {
            return null;
        }
    }

    public int updateMedico(Medico medico)
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

        return db.update(TABLE_MEDICO, values, KEY_ID + "=" + medico.getCodiceFiscale(),null);
    }
}
