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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list,container,false);
        lista = (ListView) v.findViewById(R.id.lista_richieste);
        MyListAdapter adapter = new MyListAdapter(v.getContext(), R.layout.history_element);
        adapter.setData();
        lista.setAdapter(adapter);
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
        //array paralleli momentanei per simulare dati DB
        private List<String> descriptionList = new ArrayList<>();
        private List<String> typeList = new ArrayList<>();
        private List<String> statereq = new ArrayList<>();

        public MyListAdapter(Context context, int re){
            super(context, re);
        }

        //metodo di aggiunta dati momentaneo
        public void setData() {
            descriptionList.add("Hai richiesto al medico la prescrizione di...");
            descriptionList.add("Hai richiesto al medico la visita specialistica in ...");
            descriptionList.add("Hai richiesto al medico la prescrizione di...");
            descriptionList.add("Hai richiesto al medico la visita specialistica in ...");
            typeList.add("Prescrizione");
            typeList.add("Visita");
            typeList.add("Prescrizione");
            typeList.add("Visita");
            statereq.add("a");
            statereq.add("r");
            statereq.add("r");
            statereq.add("a");
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return descriptionList.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.history_element, parent, false);

            TextView textView1 = (TextView) v.findViewById(R.id.descrizione);
            textView1.setText(descriptionList.get(position));
            TextView textView2 = (TextView) v.findViewById(R.id.tipo);
            textView2.setText(typeList.get(position));

            ImageView img = (ImageView) v.findViewById(R.id.immagine);
            if(typeList.get(position)=="Prescrizione")
                img.setImageResource(R.drawable.pill_icon);
            else if(typeList.get(position)=="Visita")
                img.setImageResource(R.drawable.calendar);

            /*ImageView state = (ImageView) v.findViewById(R.id.state);
            if(statereq.get(position)== "a")
                state.setImageResource(R.drawable.ic_thumb_up_black_24dp);
            else if(statereq.get(position)=="r")
                state.setImageResource(R.drawable.ic_thumb_down_black_24dp);
*/
            return v;
        }
    }
}
