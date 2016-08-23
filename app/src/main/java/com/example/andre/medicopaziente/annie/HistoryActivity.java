package com.example.andre.medicopaziente.annie;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.example.andre.medicopaziente.R;


public class HistoryActivity extends BasicDrawerActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Myadapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_history);
        TabLayout tab = (TabLayout) findViewById(R.id.tabs);
        tab.setVisibility(View.VISIBLE);

        ViewGroup content = (ViewGroup) findViewById(R.id.frag_container);
        getLayoutInflater().inflate(R.layout.activity_history, content, true);

        mSectionsPagerAdapter = new Myadapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tab.setupWithViewPager(mViewPager);

    }

    public class Myadapter extends FragmentPagerAdapter {

        public Myadapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new HistoryAllFragment();
                case 1:
                    return new HistoryApprFragment();
                case 2:
                    return new HistoryRefFragment();
            }
            return new HistoryAllFragment();
            //return new RequestFragment();
        }

        @Override
        public int getCount() {
            // pagine totali nel tablayout finale
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "All";
                case 1:
                    return "Approvate";
                case 2:
                    return "Rifiutate";
            }
            return null;
        }
    }
}
