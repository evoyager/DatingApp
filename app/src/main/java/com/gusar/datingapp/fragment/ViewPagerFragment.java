package com.gusar.datingapp.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gusar.datingapp.MainActivity;
import com.gusar.datingapp.R;

/**
 * Created by igusar on 2/12/16.
 */
public class ViewPagerFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_viewpager, container, false);

        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.pager);
        mViewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

        return view;
    }

//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//
//    }

        public static class MyAdapter extends FragmentPagerAdapter {


        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch(i) {
                case 0:
                    return new PersonsFragment();
                default:
                    return new MapFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int i) {
            switch(i) {
                case 0:
                    return "Persons";
                default:
                    return "Map";
            }
        }
    }

}

