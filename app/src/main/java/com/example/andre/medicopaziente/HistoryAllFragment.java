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
        ArrayList<Richiesta> returnfromDB = riempi();
        //
        //l'ultimo paramentro = array list da passare all'adapter!
        //nelle altre historyfragment non c'è!
        //
        MyListAdapter adapter = new MyListAdapter(v.getContext(), R.layout.history_element, returnfromDB);
        lista.setAdapter(adapter);
        //

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(lista.getContext(),DetailsActivity.class);
                intent.putExtra("ITEM_CLICKED", position);
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


    class MyListAdapter extends ArrayAdapter<String> {

        //
        //DEVO IMPLEMENTARE GETITEM PER RITORNARE L'ELEMENTO DA INVIARE TRAMITE INTENT A DetailsActivity
        //


        //
        //se guardi nelle altre classi historyfragment sono diverse! ma questo dovrebbe essere
        //il codice da usare con le chiamate al DB dove ritorna un Arraylist
        //
        //versione per dati DB
        private ArrayList<Richiesta> richieste = new ArrayList<>();
        String tipo,  nome_farmaco;
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

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.history_element, parent, false);

            TextView textView2 = (TextView) v.findViewById(R.id.tipo);
            textView2.setText(tipo);

            TextView textView1 = (TextView) v.findViewById(R.id.descrizione);
            ImageView img = (ImageView) v.findViewById(R.id.immagine);

            textView1.setText(descrizione_prescrizione + nome_farmaco);


            if(tipo.equals("Prescrizione")) {
                textView1.setText(descrizione_prescrizione + nome_farmaco);
                img.setImageResource(R.drawable.pill_icon);
            }
            else if(tipo.equals("Visita")) {
                img.setImageResource(R.drawable.calendar);
            }

            /*ImageView state = (ImageView) v.findViewById(R.id.state);
            if(statereq.get(position)== "a")
                state.setImageResource(R.drawable.ic_thumb_up_black_24dp);
            else if(statereq.get(position)=="r")
                state.setImageResource(R.drawable.ic_thumb_down_black_24dp);*/
            return v;
        }
    }

    //
    //è un copia incolla della funzione in BasicDrawer! quindi è da modificare!
    //
    /*public class AsyncCallSoapRichieste extends AsyncTask<String, Void, ArrayList<Richiesta>> {

        @Override
        protected ArrayList<Richiesta> doInBackground(String... params) {
            CallSoap cs = new CallSoap();
            return cs.GetPazienteRequest(paziente.getCodiceFiscale());

        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getContext(), "Attendere", "Aggiornamento richieste...", true);
        }

        @Override
        protected void onPostExecute(ArrayList<Richiesta> s) {
            DatabaseHelper db = new DatabaseHelper(getApplicationContext());
            if (!db.createRequest(s)) {
                int a = 10;
                a++;
                System.out.println(a);
            }
            progressDialog.dismiss();

        }
    }*/


    //
    //funione momentanea per arraylist sopra!
    //
    public ArrayList<Richiesta> riempi(){
        ArrayList<Richiesta> array = new ArrayList<>();
        Richiesta elemento = new Richiesta();
        elemento.setIdRichiesta(1);
        elemento.setStato("A");
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
}
