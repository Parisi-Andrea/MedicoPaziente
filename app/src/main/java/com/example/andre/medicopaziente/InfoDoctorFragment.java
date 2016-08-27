package com.example.andre.medicopaziente;

import android.app.ProgressDialog;
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
 * Created by Annalisa on 27/08/2016.
 */
public class InfoDoctorFragment extends Fragment {

    ListView lista;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_info_medico,container,false);

        lista = (ListView) v.findViewById(R.id.list_info);
        MyInfoListAdapter adapter= new MyInfoListAdapter(v.getContext(), R.layout.item_listinfo);
        adapter.setdata();
        lista.setAdapter(adapter);
        return v;
    }

    public class MyInfoListAdapter extends ArrayAdapter<String> {

        ArrayList<String> description = new ArrayList<>();
        ArrayList<String> items = new ArrayList<>();


        public MyInfoListAdapter(Context context, int res){
            super(context,res);

        }

        public void setdata(){
            description.add("immagine");
            description.add("Nome  Cognome");
            description.add("Telefono");
            description.add("Ambulatorio");
            description.add("Orario");

            items.add("immagine");
            items.add("Mario Rossi");
            items.add("0461 961361");
            items.add("Rosti - via le man dal cul, 2");
            items.add("Lun - Ven : 9.00 - 15.00");
            notifyDataSetChanged();
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
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v;
            if(position==0){
               v = inflater.inflate(R.layout.item_listinfo_image, parent, false);
                ImageView image = (ImageView) v.findViewById(R.id.img);
                image.setImageResource(R.drawable.ali_connors);
            }
            else {
                v = inflater.inflate(R.layout.item_listinfo, parent, false);
                TextView txt1 = (TextView) v.findViewById(R.id.description);
                txt1.setText(description.get(position));
                TextView txt2 = (TextView) v.findViewById(R.id.details);
                txt2.setText(items.get(position));
            }
            return v;
        }
    }
}
