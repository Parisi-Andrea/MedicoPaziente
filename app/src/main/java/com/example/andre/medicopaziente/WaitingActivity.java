package com.example.andre.medicopaziente;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

public class WaitingActivity extends BasicDrawerActivity {

    private Myadapter adapter;

    private ViewPager myviewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewGroup content = (ViewGroup) findViewById(R.id.frag_container);
        getLayoutInflater().inflate(R.layout.activity_waiting, content, true);
        adapter = new Myadapter(getSupportFragmentManager());
        myviewpager = (ViewPager) findViewById(R.id.container);
        myviewpager.setAdapter(adapter);
    }


    public class Myadapter extends FragmentPagerAdapter {

        public Myadapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return new WaitingRequestFragment();
        }

        @Override
        public int getCount() {
            // pagine totali nel tablayout finale
            return 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }
}

