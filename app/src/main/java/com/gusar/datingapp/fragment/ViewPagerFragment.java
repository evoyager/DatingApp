package com.gusar.datingapp.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gusar.datingapp.model.ModelPerson;
import com.gusar.datingapp.view.SlidingTabLayout;

import com.gusar.datingapp.R;

import org.testpackage.test_sdk.android.testlib.API;
import org.testpackage.test_sdk.android.testlib.services.UpdateService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by igusar on 2/12/16.
 */
public class ViewPagerFragment extends Fragment {

    MyAdapter adapter;
    ViewPager mViewPager;
    private FragmentTabHost tabHost;
    private SlidingTabLayout mSlidingTabLayout;
    static final String LOG_TAG = "SlidingTabsBasicFragment";
    private static ArrayList<ModelPerson> persons = new ArrayList<ModelPerson>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            persons = bundle.getParcelableArrayList("persons");
        }

        return inflater.inflate(R.layout.fr_viewpager, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        adapter = new MyAdapter(getChildFragmentManager());
        mViewPager.setAdapter(adapter);

        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setViewPager(mViewPager);
    }

    public static class MyAdapter extends FragmentPagerAdapter {


        public MyAdapter(FragmentManager fm) {
                super(fm);
            }

        @Override
        public Fragment getItem(int i) {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("persons", persons);
            Fragment personsFragment = new PersonsFragment();
            Fragment mapFragment = new MapFragment();
            personsFragment.setArguments(bundle);
            mapFragment.setArguments(bundle);
            switch(i) {
                case 0:
                    return personsFragment;
                case 1:
                    return mapFragment;
                }
            return null;
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

