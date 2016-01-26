package com.gusar.datingapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.gusar.datingapp.fragment.MapFragment;
import com.gusar.datingapp.fragment.PersonsFragment;

/**
 * @author evoyager
 */
public class DatingActivity extends FragmentActivity {
    private static final String STATE_POSITION = "STATE_POSITION";
    private ViewPager pager;

    public static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_dating);

        fragmentManager = getSupportFragmentManager();

        int pagerPosition = savedInstanceState == null ? 0 : savedInstanceState.getInt(STATE_POSITION);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new DatingAdapter(getSupportFragmentManager()));
        pager.setCurrentItem(pagerPosition);
    }

    private class DatingAdapter extends FragmentPagerAdapter {
        Fragment personsFragment;
        Fragment mapFragment;

        public DatingAdapter(FragmentManager fm) {
            super(fm);
            personsFragment = new PersonsFragment();
            mapFragment = new MapFragment();
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return personsFragment;
                case 1:
                    return mapFragment;
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Persons";
                case 1:
                    return "Map";
                default:
                    return null;
            }
        }
    }
}
