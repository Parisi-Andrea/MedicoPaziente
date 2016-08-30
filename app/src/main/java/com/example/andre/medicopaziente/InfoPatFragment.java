package com.example.andre.medicopaziente;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Annalisa on 27/08/2016.
 */
public class InfoPatFragment extends Fragment{

    ListView lista;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.activity_info, container, false);
        lista = (ListView) v.findViewById(R.id.list_info);

    
        final ArrayList<Paziente> returnfromDB = riempi();
        //
        //l'ultimo paramentro = array list da passare all'adapter!
        //nelle altre historyfragment non c'è!
        //

        MyDataAdapter adapter = new MyDataAdapter(v.getContext(), R.layout.item_listinfo_pat, returnfromDB);
        lista.setAdapter(adapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), InfoDettagliPazActivity.class);
                intent.putExtra("Paziente",returnfromDB.get(position) );
                startActivity(intent);
            }
        });
        return v;
    }

    class MyDataAdapter extends ArrayAdapter<Paziente> {
        //array paralleli momentanei per simulare dati DB
        ArrayList<Paziente> pazienti = new ArrayList<>();

        public MyDataAdapter(Context context, int resource, ArrayList<Paziente> pazFromDB) {
            super(context, resource);
            pazienti.addAll(pazFromDB);
        }


        @Override
        public int getCount() {
            return pazienti.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.item_listinfo_pat, parent, false);

            TextView textView1 = (TextView) v.findViewById(R.id.nome_cognome);
            textView1.setText(pazienti.get(position).getNome()+ " " +pazienti.get(position).getCognome());
            TextView textView2 = (TextView) v.findViewById(R.id.cf);
            textView2.setText(pazienti.get(position).getCodiceFiscale());

            CircleImageView img = (CircleImageView) v.findViewById(R.id.imgpaziente);
            //manca il ritorno dell'immagine questo è statico
            img.setImageResource(R.drawable.immagine1);
            return v;
        }
    }


    public ArrayList<Paziente> riempi(){
        DatabaseHelper db = new DatabaseHelper(getContext());
        ArrayList<Paziente> array = db.getPazientiMedico(((InfoActivity)getActivity()).medico.getCodiceFiscale());
        if(array!=null) {
            return array;
        }else{
            return new ArrayList<Paziente>();
        }
    }
}
