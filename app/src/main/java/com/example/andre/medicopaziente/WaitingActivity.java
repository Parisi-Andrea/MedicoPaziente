package com.example.andre.medicopaziente;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

public class WaitingActivity extends BasicDrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(MainActivity.tipoUtente.equals("Medico"))
            transaction.add(R.id.frag_container, new WaitingDocRequestFragment());
        else {
            transaction.add(R.id.frag_container, new WaitingRequestFragment());
        }
        transaction.commit();
    }
}