package com.gusar.datingapp;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ViewAnimator;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gusar.datingapp.fragment.PersonsFragment;
import com.gusar.datingapp.fragment.ViewPagerFragment;
import com.gusar.datingapp.model.ModelPerson;
import org.testpackage.test_sdk.android.testlib.API;
import org.testpackage.test_sdk.android.testlib.interfaces.PersonsExtendedCallback;
import org.testpackage.test_sdk.android.testlib.interfaces.SuccessCallback;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity {

    private static final String TEST_FILE_NAME = "Universal Image Loader @#&=+-_.,!()~'%20.png";
    Fragment fr;
    String tag;
    static ProgressDialog mProgressDialog;
    List<ModelPerson> persons;
    Button btnGenerate;
    private static final int REQUEST_STORAGE = 0;
    ViewPager mViewPager;
    static final int PAGE_COUNT = 2;
    static final String TAG = "myLogs";
    Button genBtn;

    Toolbar toolbar;

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
    public void onPause(){

        super.onPause();
        if(mProgressDialog != null)
            mProgressDialog.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        new DownloadJSON(getApplicationContext()).execute();
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
        }
    }
}


