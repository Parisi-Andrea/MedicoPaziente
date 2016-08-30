package com.example.andre.medicopaziente;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

public class WaitingActivity extends BasicDrawerActivity {

    public Paziente paziente;
    public Medico medico;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        paziente=intent.getParcelableExtra("Paziente");
        medico=intent.getParcelableExtra("Medico");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.add(R.id.frag_container, new WaitingRequestFragment());
        transaction.commit();
    }
}