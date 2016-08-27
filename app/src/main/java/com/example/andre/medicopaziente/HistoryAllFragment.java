package com.example.andre.medicopaziente;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.drm.DrmStore;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.andre.medicopaziente.R;
import com.example.andre.medicopaziente.DetailsActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Annalisa on 16/08/2016.
 */
public class HistoryAllFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    ListView lista;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list,container,false);
        lista = (ListView) v.findViewById(R.id.lista);

        //
        //creato arraylist momentaneo per simulare ritorno dalla query su db!
        //
        final ArrayList<Richiesta> returnfromDB = riempi();
        returnfromDB.addAll(riempi2());
        //
        //l'ultimo paramentro = array list da passare all'adapter!
        //nelle altre historyfragment non c'è!
        //
        final MyListAdapter adapter = new MyListAdapter(v.getContext(), R.layout.history_element, returnfromDB);
        lista.setAdapter(adapter);
        //

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(lista.getContext(),DetailsActivity.class);

                intent.putExtra("ITEM_CLICKED", returnfromDB.get(position));
                startActivity(intent);
            }
        });
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        return v;
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }


    class MyListAdapter extends ArrayAdapter<Richiesta> {

        //
        //DEVO IMPLEMENTARE GETITEM PER RITORNARE L'ELEMENTO DA INVIARE TRAMITE INTENT A DetailsActivity
        //


        //
        //se guardi nelle altre classi historyfragment sono diverse! ma questo dovrebbe essere
        //il codice da usare con le chiamate al DB dove ritorna un Arraylist
        //
        //versione per dati DB
        private ArrayList<Richiesta> richieste = new ArrayList<>();
        String tipo,  nome_farmaco, stato, data_ora;
        final String descrizione_prescrizione= "Al medico è stato richiesto il farmaco: ";
        final String descrizione_visita= "Al medico è stata richiesta una visita ";

        public MyListAdapter(Context context, int layout, ArrayList<Richiesta> request){
            super(context, layout);
            richieste.addAll(request);
        }

        @Override
        public int getCount() {
            return richieste.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            tipo = richieste.get(position).getTipo();
            nome_farmaco = richieste.get(position).getNome_farmaco();
            stato = richieste.get(position).getStato();
            data_ora = richieste.get(position).getData_richiesta();

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.history_element, parent, false);

            TextView textView2 = (TextView) v.findViewById(R.id.tipo);
            textView2.setText(tipo);

            TextView textView1 = (TextView) v.findViewById(R.id.descrizione);

            ImageView img = (ImageView) v.findViewById(R.id.immagine);
            if(tipo.equals("Prescrizione")) {
                textView1.setText(descrizione_prescrizione + nome_farmaco);
                img.setImageResource(R.drawable.pill_icon);
            }
            else if(tipo.equals("Visita")) {
                img.setImageResource(R.drawable.calendar);
                textView1.setText(descrizione_visita);
            }

            ImageView state = (ImageView) v.findViewById(R.id.state);
            if(stato=="C")
                state.setImageResource(R.mipmap.ic_state_complete);
            else if(stato=="R")
                state.setImageResource(R.mipmap.ic_state_refused);
            return v;
        }
    }

    //
    //funione momentanea per arraylist sopra!
    //
    public ArrayList<Richiesta> riempi(){
        ArrayList<Richiesta> array = new ArrayList<>();
        Richiesta elemento = new Richiesta();
        elemento.setIdRichiesta(1);
        elemento.setStato("C");
        elemento.setTipo("Prescrizione");
        elemento.setData_richiesta("2012/12/12 alle 12:00 ");
        elemento.setNome_farmaco("Brufen");
        elemento.setQuantita_farmaco(2);
        array.add(elemento);
        elemento.setIdRichiesta(2);
        array.add(elemento);
        elemento.setIdRichiesta(3);
        array.add(elemento);
        elemento.setIdRichiesta(4);
        array.add(elemento);

        return array;
    }
    public ArrayList<Richiesta> riempi2(){
        ArrayList<Richiesta> array = new ArrayList<>();
        Richiesta elemento = new Richiesta();
        elemento.setIdRichiesta(1);
        elemento.setStato("R");
        elemento.setTipo("Visita");
        elemento.setData_richiesta("2012/12/12 alle 14:00 ");
        elemento.setNote_richiesta("Specialistica dermatologica presso LAB1");
        array.add(elemento);
        elemento.setIdRichiesta(2);
        array.add(elemento);
        elemento.setIdRichiesta(3);
        array.add(elemento);
        elemento.setIdRichiesta(4);
        array.add(elemento);

        return array;
    }
}
