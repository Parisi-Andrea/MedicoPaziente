package com.example.andre.medicopaziente;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Annalisa on 26/08/2016.
 */
public class InfoActivity extends BasicDrawerActivity {

    ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup content = (ViewGroup) findViewById(R.id.frag_container);
        getLayoutInflater().inflate(R.layout.activity_info_medico, content, true);


        lista = (ListView) findViewById(R.id.list_info);
        MyInfoListAdapter adapter= new MyInfoListAdapter(this, R.layout.item_listinfo);
        lista.setAdapter(adapter);

    }

    public class MyInfoListAdapter extends ArrayAdapter<String> {

        ArrayList<String> description = new ArrayList<>();
        ArrayList<String> items = new ArrayList<>();


        public MyInfoListAdapter(Context context, int res){
            super(context,res);
            description.add("Nome  Cognome");
            description.add("Telefono");
            description.add("Ambulatorio");
            description.add("Orario");

            items.add("Mario Rossi");
            items.add("0461 961361");
            items.add("Rosti - via le man dal cul, 2");
            items.add("Lun - Ven : 9.00 - 15.00");
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.history_element, parent, false);

            TextView txt1 = (TextView) v.findViewById(R.id.description);
            txt1.setText(description.get(position));
            TextView txt2 = (TextView) v.findViewById(R.id.details);
            txt2.setText(items.get(position));

            return v;
        }
    }
}
