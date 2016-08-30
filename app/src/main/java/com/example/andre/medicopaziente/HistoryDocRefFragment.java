package com.example.andre.medicopaziente;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;

/**
 * Created by Annalisa on 28/08/2016.
 */
public class HistoryDocRefFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    ListView lista;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        lista = (ListView) v.findViewById(R.id.lista);


        final ArrayList<Richiesta> requestsfromDB = getRichieste();
        //
        //l'ultimo paramentro = array list da passare all'adapter!
        //
        final MyListAdapter adapter = new MyListAdapter(v.getContext(), R.layout.history_element, requestsfromDB);
        lista.setAdapter(adapter);
        //

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(lista.getContext(), DetailsDocActivity.class);
                intent.putExtra("ITEM_CLICKED", requestsfromDB.get(position));
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
        //versione per dati DB
        private ArrayList<Richiesta> richieste = new ArrayList<>();
        String tipo, nome_farmaco, stato, data_ora, nome_cognomePaziente, cf_paziente;

        static final String descrizione_prescrizione = "Richiesta prescrizione farmaco: ";
        static final String descrizione_visita_spec = "Richiesta una visita specialistica: ";
        static final String descrizione_visita = "Richiesta una visita di controllo";

        public MyListAdapter(Context context, int layout, ArrayList<Richiesta> request) {
            super(context, layout);
            richieste.addAll(request);
        }

        @Override
        public int getCount() {
            return richieste.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.history_element, parent, false);
            TextView titolo = (TextView) v.findViewById(R.id.tipo);
            TextView descrizione = (TextView) v.findViewById(R.id.descrizione);
            ImageView img = (ImageView) v.findViewById(R.id.immagine);

            DatabaseHelper db = new DatabaseHelper(getContext());
            Paziente paziente = db.getPaziente(richieste.get(position).getCf_paziente());
            if (paziente == null) {
                titolo.setVisibility(View.GONE);
                descrizione.setVisibility(View.GONE);
                img.setVisibility(View.GONE);
            } else {
                tipo = richieste.get(position).getTipo();
                nome_farmaco = richieste.get(position).getNome_farmaco();
                stato = richieste.get(position).getStato();
                data_ora = richieste.get(position).getData_richiesta();
                if(paziente!=null) {
                    nome_cognomePaziente = paziente.getNome() + " " + paziente.getCognome();
                    titolo.setText(nome_cognomePaziente + " - " + cf_paziente);
                } else{
                    titolo.setText(" -- ");
                }

                cf_paziente = richieste.get(position).getCf_paziente();

                if (tipo.equals("Prescrizione")) {
                    descrizione.setText(descrizione_prescrizione + nome_farmaco);
                    img.setImageResource(R.drawable.pill_icon);
                } else if (tipo.equals("Visita di controllo")) {
                    img.setImageResource(R.drawable.calendar);
                    descrizione.setText(descrizione_visita);
                } else if (tipo.equals("Visita specialistica")) {
                    img.setImageResource(R.drawable.calendar);
                    descrizione.setText(descrizione_visita_spec + nome_farmaco);
                }
            }
            ImageView state = (ImageView) v.findViewById(R.id.state);
            state.setVisibility(View.GONE);
            return v;

        }
    }


    public ArrayList<Richiesta> getRichieste(){
        DatabaseHelper db = new DatabaseHelper(getContext());
        ArrayList<Richiesta> array = db.getRifiutateMedico(((HistoryActivity) getActivity()).medico.getCodiceFiscale());
        if(array!=null) {
            return array;
        }else{
            return new ArrayList<Richiesta>();
        }    }
}
