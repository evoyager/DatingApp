package com.gusar.datingapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.gusar.datingapp.fragment.MapFragment;
import com.gusar.datingapp.fragment.PersonsFragment;

/**
 * Created by igusar on 1/27/16.
 */
public class TabAdapter extends FragmentStatePagerAdapter {
    public static final int PERSONS_FRAGMENT_POSITION = 0;
    public static final int MAP_FRAGMENT_POSITION = 1;
    private PersonsFragment personsFragment = new PersonsFragment();
    private MapFragment mapFragment = new MapFragment();
    private int numberOfTabs;

    public TabAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case PERSONS_FRAGMENT_POSITION:
                return this.personsFragment;
            case MAP_FRAGMENT_POSITION:
                return this.mapFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return this.numberOfTabs;
    }
}
