package com.gusar.datingapp;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.gusar.datingapp.adapter.TabAdapter;
import com.gusar.datingapp.fragment.DownloadImagesFragment;
import com.gusar.datingapp.fragment.MapFragment;
import com.gusar.datingapp.model.ModelPerson;

/**
 * @author evoyager
 */
public class DatingActivity extends ActionBarActivity  {
    private static final String STATE_POSITION = "STATE_POSITION";
    private ViewPager viewPager;
    TabAdapter tabAdapter;
    FragmentManager fragmentManager;
    Fragment personsFragment;
    Fragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_dating);
        this.fragmentManager = getSupportFragmentManager();

        setUI();

//
//        fragmentManager = getSupportFragmentManager();
//
//        int pagerPosition = savedInstanceState == null ? 0 : savedInstanceState.getInt(STATE_POSITION);
//
//        pager = (ViewPager) findViewById(R.id.pager);
//        pager.setAdapter(new DatingAdapter(getSupportFragmentManager()));
//        pager.setCurrentItem(pagerPosition);
    }

    private void setUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.persons));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.map));
        viewPager = (ViewPager) findViewById(R.id.pager);
        this.tabAdapter = new TabAdapter(this.fragmentManager, 2);
        viewPager.setAdapter(this.tabAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//        this.personsFragment = (PersonsFragment) this.tabAdapter.getItem(0);
        this.personsFragment = (DownloadImagesFragment) this.tabAdapter.getItem(0);
        this.mapFragment = (MapFragment) this.tabAdapter.getItem(1);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
