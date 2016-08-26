package com.example.andre.medicopaziente;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Annalisa on 16/08/2016.
 */
public class WaitingRequestFragment extends Fragment {

    ListView lista;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list,container,false);
        lista = (ListView) v.findViewById(R.id.lista);
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


        return v;
    }

    class MyListAdapter extends ArrayAdapter<String> {
        //array paralleli momentanei per simulare dati DB
        private List<String> descriptioList = new ArrayList<>();
        private List<String> typeList = new ArrayList<>();

        public MyListAdapter(Context context, int re){
            super(context, re);
        }

        //metodo di aggiunta dati momentaneo
        public void setData() {
            descriptioList.add("Hai richiesto al medico la prescrizione di...");
            descriptioList.add("Hai richiesto al medico la visita specialistica in ...");
            descriptioList.add("Hai richiesto al medico la visita specialistica in ...");
            typeList.add("Prescrizione");
            typeList.add("Visita");
            typeList.add("Visita");
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return descriptioList.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.history_element, parent, false);
            TextView textView1 = (TextView) v.findViewById(R.id.descrizione);
            textView1.setText(descriptioList.get(position));
            TextView textView2 = (TextView) v.findViewById(R.id.tipo);
            textView2.setText(typeList.get(position));
            ImageView img = (ImageView) v.findViewById(R.id.immagine);
            if(typeList.get(position)=="Prescrizione")
                img.setImageResource(R.drawable.pill_icon);
            else if(typeList.get(position)=="Visita")
                img.setImageResource(R.drawable.calendar);
            return v;
        }
    }
}
