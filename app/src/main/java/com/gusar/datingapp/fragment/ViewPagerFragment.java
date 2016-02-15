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

import com.gusar.datingapp.view.SlidingTabLayout;

import com.gusar.datingapp.R;

/**
 * Created by igusar on 2/12/16.
 */
public class ViewPagerFragment extends Fragment {

    MyAdapter adapter;
    ViewPager mViewPager;
    private FragmentTabHost tabHost;
    private SlidingTabLayout mSlidingTabLayout;
    static final String LOG_TAG = "SlidingTabsBasicFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fr_viewpager, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


//        tabHost = new FragmentTabHost(getActivity());
//        tabHost.setup(getActivity(),getChildFragmentManager(),R.id.testtabhost1);
//
//        tabHost.addTab(tabHost.newTabSpec("tab_test1").setIndicator("Persons"), PersonsFragment.class, null);
//        tabHost.addTab(tabHost.newTabSpec("tab_test2").setIndicator("Maps"), MapFragment.class, null);

        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        adapter = new MyAdapter(getChildFragmentManager());
        mViewPager.setAdapter(adapter);
//        mViewPager.setAdapter(new SamplePagerAdapter());

        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);
    }


    class SamplePagerAdapter extends PagerAdapter {

        /**
         * @return the number of pages to display
         */
        @Override
        public int getCount() {
            return 10;
        }

        /**
         * @return true if the value returned from {@link #instantiateItem(ViewGroup, int)} is the
         * same object as the {@link View} added to the {@link ViewPager}.
         */
        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        // BEGIN_INCLUDE (pageradapter_getpagetitle)
        /**
         * Return the title of the item at {@code position}. This is important as what this method
         * returns is what is displayed in the {@link SlidingTabLayout}.
         * <p>
         * Here we construct one using the position value, but for real application the title should
         * refer to the item's contents.
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return "Item " + (position + 1);
        }
        // END_INCLUDE (pageradapter_getpagetitle)

        /**
         * Instantiate the {@link View} which should be displayed at {@code position}. Here we
         * inflate a layout from the apps resources and then change the text view to signify the position.
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // Inflate a new layout from our resources
            View view = getActivity().getLayoutInflater().inflate(R.layout.pager_item,
                    container, false);
            // Add the newly created View to the ViewPager
            container.addView(view);

            // Retrieve a TextView from the inflated View, and update it's text
            TextView title = (TextView) view.findViewById(R.id.item_title);
            title.setText(String.valueOf(position + 1));


            // Return the View
            return view;
        }

        /**
         * Destroy the item from the {@link ViewPager}. In our case this is simply removing the
         * {@link View}.
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    public static class MyAdapter extends FragmentPagerAdapter {


        public MyAdapter(FragmentManager fm) {
                super(fm);
            }

        @Override
        public Fragment getItem(int i) {
            switch(i) {
                case 0:
                    return new PersonsFragment();
                case 1:
                    return new MapFragment();
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

