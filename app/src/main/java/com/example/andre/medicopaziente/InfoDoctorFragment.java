package com.example.andre.medicopaziente;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Annalisa on 27/08/2016.
 */
public class InfoDoctorFragment extends Fragment {

    ListView lista;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_info, container, false);

        lista = (ListView) v.findViewById(R.id.list_info);

        //
        //medico momentaneo
        //
        Medico returnfromDB = riempiMedico();
        MyInfoListAdapter adapter = new MyInfoListAdapter(v.getContext(), R.layout.item_listinfo, returnfromDB);
        lista.setAdapter(adapter);
        return v;
    }


    public class MyInfoListAdapter extends ArrayAdapter<String> {

        Medico medico;
        ArrayList<String> description = new ArrayList<>();
        ArrayList<String> dati_medico = new ArrayList<>();
         String nome;
         String cognome;
         String email;
         String nTel;
         String ambulatorio;
         String orario;


        public MyInfoListAdapter(Context context, int res, Medico medicoDB) {
            super(context, res);
            medico = medicoDB;
            setdata();
        }


        public void setdata() {
            description.add("immagine");
            description.add("Nome  Cognome");
            description.add("Email");
            description.add("Telefono");
            description.add("Ambulatorio");
            description.add("Orario");
            nome = medico.getNome();
            cognome = medico.getCognome();
            email = medico.getEmail();
            nTel = medico.getNTel();
            ambulatorio = medico.getAmbulatorio();
            orario = medico.getOrario();

            dati_medico.add("immagine");
            dati_medico.add(nome + " " + cognome);
            dati_medico.add(email);
            dati_medico.add(nTel);
            dati_medico.add(ambulatorio);
            dati_medico.add(orario);
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
                txt2.setText(dati_medico.get(position));

            }
            return v;
        }
    }

    public Medico riempiMedico() {
        DatabaseHelper db =new DatabaseHelper(getContext());
        Medico res = db.getMedico(((InfoActivity)getActivity()).paziente.getMedico());

        return res;
    }
}
