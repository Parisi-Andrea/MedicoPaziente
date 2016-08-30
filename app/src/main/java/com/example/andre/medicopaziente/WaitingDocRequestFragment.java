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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Annalisa on 30/08/2016.
 */
public class WaitingDocRequestFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    ListView lista;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        lista = (ListView) v.findViewById(R.id.lista);
        //
        //creato arraylist momentaneo per simulare ritorno dalla query su db!
        //
        final ArrayList<Richiesta> returnfromDB = getRequests();
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
                if (returnfromDB.get(position).getTipo().equals("Prescrizione")) {
                    Intent intent = new Intent(lista.getContext(),RequestManagerFarmaco.class);
                    intent.putExtra("richiesta", returnfromDB.get(position));
                    intent.putExtra("Medico", ((WaitingActivity)getActivity()).medico);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(lista.getContext(), RequestManagerVisita.class);
                    intent.putExtra("richiesta", returnfromDB.get(position));
                    intent.putExtra("Medico", ((WaitingActivity)getActivity()).medico);
                    startActivity(intent);
                }
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
        String tipo,  nome_farmaco, stato, data_ora;
        static final String descrizione_prescrizione = "Richiesta prescrizione farmaco: ";
        static final String descrizione_visita_spec = "Richiesta una visita specialistica ";
        static final String descrizione_visita = "Richiesta una visita di controllo";

        public MyListAdapter(Context context, int layout, ArrayList<Richiesta> request){
            super(context, layout);
            richieste.addAll(request);
            notifyDataSetChanged();
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

            TextView descrizione = (TextView) v.findViewById(R.id.descrizione);
            ImageView img = (ImageView) v.findViewById(R.id.immagine);

            if (tipo.equals("Prescrizione")) {
                descrizione.setText(descrizione_prescrizione + nome_farmaco);
                img.setImageResource(R.drawable.pill_icon);
            } else if (tipo.equals("Visita di controllo")) {
                img.setImageResource(R.drawable.calendar);
                descrizione.setText(descrizione_visita);
            } else if (tipo.equals("Visita specialistica")) {
                img.setImageResource(R.drawable.calendar);
                descrizione.setText(descrizione_visita_spec+ nome_farmaco);
            }

            ImageView state = (ImageView) v.findViewById(R.id.state);
            state.setVisibility(View.GONE);
            return v;
        }
    }
 
    public ArrayList<Richiesta> getRequests(){
        DatabaseHelper db = new DatabaseHelper(getContext());
        ArrayList<Richiesta> array = db.getAttesaMedico(((WaitingActivity) getActivity()).medico.getCodiceFiscale());
        return array;
    }
}
