package com.gusar.datingapp;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.gusar.datingapp.adapter.TabAdapter;
import com.gusar.datingapp.fragment.DatingFragment;
import com.gusar.datingapp.fragment.MapFragment;
import com.gusar.datingapp.fragment.PersonsFragment;
import com.gusar.datingapp.model.ModelPerson;

import org.testpackage.test_sdk.android.testlib.API;
import org.testpackage.test_sdk.android.testlib.db.PersonsHolder;
import org.testpackage.test_sdk.android.testlib.interfaces.PersonsExtendedCallback;
import org.testpackage.test_sdk.android.testlib.interfaces.SuccessCallback;

public class HomeActivity extends ActionBarActivity implements PersonsFragment.OnMapListener, MapFragment.OnPersonsListener {

    ViewPager viewPager;
    TabAdapter tabAdapter;
    FragmentManager fragmentManager;
    DatingFragment personsFragment;
    DatingFragment mapFragment;
    PersonsHolder personsHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        this.fragmentManager = getSupportFragmentManager();

        setUI();

        API.INSTANCE.init(getApplicationContext());
        API.INSTANCE.refreshPersons(new SuccessCallback() {
            @Override
            public void onSuccess(){}
        });

        API.INSTANCE.getPersons(0, new PersonsExtendedCallback() {

            @Override
            public void onResult(String json) {

            }

            @Override
            public void onFail(String reason) {

            }
        });

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
        this.personsFragment = (PersonsFragment) this.tabAdapter.getItem(0);
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

    @Override
    public void onMap(ModelPerson modelPerson) {

    }

    @Override
    public void onPersons(ModelPerson modelPerson) {

    }
}
