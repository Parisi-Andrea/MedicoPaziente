package com.example.andre.medicopaziente;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Annalisa on 26/08/2016.
 */
public class InfoActivity extends BasicDrawerActivity {

    public Medico medico;
    public Paziente paziente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intent = getIntent();
        if(MainActivity.tipoUtente.equals("Paziente")) {
            paziente = intent.getParcelableExtra("Paziente");
        } else {
            medico = intent.getParcelableExtra("Medico");
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if(MainActivity.tipoUtente.equals("Medico"))
            transaction.add(R.id.frag_container, new InfoPatFragment());
        else {
            transaction.add(R.id.frag_container, new InfoDoctorFragment());
        }
        transaction.commit();

    }


}
