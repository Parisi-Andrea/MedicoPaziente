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
import java.util.List;

/**
 * Created by Annalisa on 27/08/2016.
 */
public class InfoPatFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    ListView lista;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        lista = (ListView) v.findViewById(R.id.lista);
        MyDataAdapter adapter = new MyDataAdapter(v.getContext(), R.layout.item_listinfo_pat);
        adapter.setData();
        lista.setAdapter(adapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(lista.getContext(), DetailsActivity.class);
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

    class MyDataAdapter extends ArrayAdapter<String> {
        //array paralleli momentanei per simulare dati DB
        private List<String> pazienti = new ArrayList<>();
        private List<String> cf = new ArrayList<>();

        public MyDataAdapter(Context context, int resource) {
            super(context, resource);
        }

        //metodo di aggiunta dati momentaneo
        public void setData() {
            pazienti.add("Mario Rossi");
            pazienti.add("Alberta Verdi");
            pazienti.add("Giacomo Bianchi");
            cf.add("NLSFLP94T45L378G");
            cf.add("NLSFLP94T45L378G");
            cf.add("NLSFLP94T45L378G");
            notifyDataSetChanged();
        }


        @Override
        public int getCount() {
            return pazienti.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.history_element, parent, false);

            TextView textView1 = (TextView) v.findViewById(R.id.descrizione);
            textView1.setText(pazienti.get(position));
            TextView textView2 = (TextView) v.findViewById(R.id.tipo);
            textView2.setText(cf.get(position));

            ImageView img = (ImageView) v.findViewById(R.id.imgpaziente);
            img.setImageResource(R.drawable.immagine1);
            return v;
        }
    }
}
