package com.gusar.datingapp;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gusar.datingapp.fragment.MapFragment;
import com.gusar.datingapp.fragment.PageFragment;
import com.gusar.datingapp.fragment.PersonsFragment;
import com.gusar.datingapp.fragment.ViewPagerFragment;
import com.gusar.datingapp.model.ModelPerson;

import org.testpackage.test_sdk.android.testlib.API;
import org.testpackage.test_sdk.android.testlib.interfaces.PersonsExtendedCallback;
import org.testpackage.test_sdk.android.testlib.interfaces.SuccessCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    private static final String TEST_FILE_NAME = "Universal Image Loader @#&=+-_.,!()~'%20.png";
    Fragment fr;
    String tag;
    static ProgressDialog mProgressDialog;
    List<ModelPerson> persons;
    Button btnGenerate;
    private static final int REQUEST_STORAGE = 0;
//    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    ViewPager mViewPager;
    static final int PAGE_COUNT = 2;
    static final String TAG = "myLogs";
    Button genBtn;

//    ViewPager pager;
//    PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        genBtn = (Button)findViewById(R.id.btnGenerate);

        btnGenerate = (Button) findViewById(R.id.btnGenerate);
        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_STORAGE);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        new DownloadJSON(getApplicationContext()).execute();
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {

    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        btnGenerate.setVisibility(View.VISIBLE);

    }

    private class DownloadJSON extends AsyncTask<Void, Void, Void> {
        Context context;
        private DownloadJSON(Context context) {
            this.context = context.getApplicationContext();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setTitle("Parsing data from JSON through testlib API");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            API.INSTANCE.init(getApplicationContext());
            API.INSTANCE.refreshPersons(new SuccessCallback() {
                @Override
                public void onSuccess() {}
            });

            getPersons();

            return null;
        }

         private void getPersons() {
             API.INSTANCE.getPersons(Constants.getPageNum(), new PersonsExtendedCallback() {
                 @Override
                 public void onResult(String json) {
                     if (json.equals("[]")) {
                         Constants.setPageNum(0);
                         getPersons();
                     } else {
                         Type listType = new TypeToken<ArrayList<ModelPerson>>() {
                         }.getType();
                         Constants.setPersons((List<ModelPerson>) new Gson().fromJson(json, listType));
                     }
                 }
                 @Override
                 public void onFail(String reason) {}
             });
         }

        @Override
        protected void onPostExecute(Void args) {
            mProgressDialog.dismiss();
            super.onPostExecute(args);

            setTitle(R.string.app_name);
            fr = new ViewPagerFragment();
            tag = PersonsFragment.class.getSimpleName();
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, fr, tag)
                    .addToBackStack("fr_image_list")
                    .commit();

            btnGenerate.setVisibility(View.GONE);
//
//            mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
//
//            final ActionBar actionBar = getActionBar();
//
//            actionBar.setHomeButtonEnabled(false);
//
//            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//
//            mViewPager = (ViewPager) findViewById(R.id.pager);
//            mViewPager.setAdapter(mAppSectionsPagerAdapter);
//            mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
//                @Override
//                public void onPageSelected(int position) {
//                    actionBar.setSelectedNavigationItem(position);
//                }
//            });
//
//            for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
//                actionBar.addTab(
//                        actionBar.newTab()
//                                .setText(mAppSectionsPagerAdapter.getPageTitle(i))
//                                .setTabListener(MainActivity.this));
//            }

//            ===========================================================================

//            pager = (ViewPager) findViewById(R.id.pager);
//            pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
//            pager.setAdapter(pagerAdapter);
//
//            pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//
//                @Override
//                public void onPageSelected(int position) {
//                    Log.d(TAG, "onPageSelected, position = " + position);
//                }
//
//                @Override
//                public void onPageScrolled(int position, float positionOffset,
//                                           int positionOffsetPixels) {
//                }
//
//                @Override
//                public void onPageScrollStateChanged(int state) {
//                }
//            });
        }

//        private class MyFragmentPagerAdapter extends FragmentPagerAdapter {
//
//            public MyFragmentPagerAdapter(FragmentManager fm) {
//                super(fm);
//            }
//
//            @Override
//            public Fragment getItem(int position) {
//                return PageFragment.newInstance(position);
//            }
//
//            @Override
//            public int getCount() {
//                return PAGE_COUNT;
//            }
//        }
    }

//    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {
//
//
//        public AppSectionsPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int i) {
//            switch(i) {
//                case 0:
//                    return new PersonsFragment();
//                default:
//                    return new MapFragment();
//            }
//        }
//
//        @Override
//        public int getCount() {
//            return 2;
//        }
//
//        @Override
//        public CharSequence getPageTitle(int i) {
//            switch(i) {
//                case 0:
//                    return "Persons";
//                default:
//                    return "Map";
//            }
//        }
//    }
}


