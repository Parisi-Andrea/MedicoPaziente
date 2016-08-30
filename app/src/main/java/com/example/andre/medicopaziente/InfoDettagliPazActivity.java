package com.example.andre.medicopaziente;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Annalisa on 30/08/2016.
 */
public class InfoDettagliPazActivity extends AppCompatActivity {

    ListView lista;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_paziente);
        lista = (ListView) findViewById(R.id.list_info);
        //
        //paziente momentaneo
        //
        Intent intent = getIntent();
        Paziente returnfromDB = intent.getParcelableExtra("InfoPaziente");
        MyInfoListAdapter adapter = new MyInfoListAdapter(this, R.layout.item_listinfo, returnfromDB);
        lista.setAdapter(adapter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }

            });

        }
    }


    public class MyInfoListAdapter extends ArrayAdapter<String> {

        Paziente paziente;
        ArrayList<String> description = new ArrayList<>();
        ArrayList<String> dati = new ArrayList<>();
        String immagine;
        String nome;
        String cognome;
        String datanascita;
        String luogonascita;
        String residenza;
        String email;
        String nTel;


        public MyInfoListAdapter(Context context, int res, Paziente pazDB) {
            super(context, res);
            paziente = pazDB;
            setdata();
        }


        public void setdata() {
            description.add("immagine");
            description.add("Nome  Cognome");
            description.add("Email");
            description.add("Telefono");
            description.add("Residenza");
            description.add("Data Nascita");
            description.add("Luogo Nascita");
            immagine = paziente.getImage();
            nome = paziente.getNome();
            cognome = paziente.getCognome();
            email = paziente.getEmail();
            nTel = paziente.getNTel();
            datanascita = paziente.getDataNascita();
            luogonascita = paziente.getLuogoNascita();
            residenza = paziente.getResidenza();

            dati.add(immagine);
            dati.add(nome+cognome);
            dati.add(email);
            dati.add(nTel);
            dati.add(residenza);
            dati.add(datanascita);
            dati.add(luogonascita);

        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }


        @Override
        public int getCount() {
            return description.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v;
            if (position == 0) {
                v = inflater.inflate(R.layout.item_listinfo_image, parent, false);
                ImageView image = (ImageView) v.findViewById(R.id.img);
                //manca il ritorno dell'immagine questo è statico
                image.setImageResource(R.drawable.ali_connors);
            } else {
                v = inflater.inflate(R.layout.item_listinfo, parent, false);
                TextView txt1 = (TextView) v.findViewById(R.id.description);
                txt1.setText(description.get(position));
                TextView txt2 = (TextView) v.findViewById(R.id.details);
                txt2.setText(dati.get(position));

            }
            return v;
        }
    }

    public Paziente riempiPaziente() {
        Paziente elemento = new Paziente();
        elemento.setCodiceFiscale("MRORSS94T05E378A");
        elemento.setNome("Mario");
        elemento.setCognome("Rossi");
        elemento.setDataNascita("05/12/1994");
        elemento.setLuogoNascita("Trento");
        elemento.setResidenza("via paludi, 104");
        elemento.setEmail("mario.rossi@mail.it");
        elemento.setNTel("0461 961361");
        elemento.setMedico("FLPNLS94T45L378G");
        return elemento;
    }
}
