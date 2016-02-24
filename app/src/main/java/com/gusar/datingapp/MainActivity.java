package com.gusar.datingapp;

import android.Manifest;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gusar.datingapp.db.ModelPersonsHolder;
import com.gusar.datingapp.db.MyDatabaseHelper;
import com.gusar.datingapp.fragment.PersonsFragment;
import com.gusar.datingapp.fragment.ViewPagerFragment;
import com.gusar.datingapp.interfaces.MyPersonsCallback;
import com.gusar.datingapp.model.ModelPerson;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import org.testpackage.test_sdk.android.testlib.API;
import org.testpackage.test_sdk.android.testlib.interfaces.PersonsCallback;
import org.testpackage.test_sdk.android.testlib.interfaces.PersonsExtendedCallback;
import org.testpackage.test_sdk.android.testlib.interfaces.SuccessCallback;
import org.testpackage.test_sdk.android.testlib.model.Person;
import org.testpackage.test_sdk.android.testlib.util.Utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    Fragment fr;
    String tag;
    static Button btnGenerate;
    private static final int REQUEST_STORAGE = 0;
    private static View mLoadingView;
    private static FragmentManager fm;
    private static boolean clicked;
    private static boolean firstLoading = true;
    private static boolean restored = false;
    private static boolean firstExecution = true;
    boolean activityIsDestroyed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnGenerate = (Button) findViewById(R.id.btnGenerate);
        mLoadingView = findViewById(R.id.loading_spinner);

        fm = getSupportFragmentManager();

        if (firstLoading & !restored) {
            new InitializeData().execute();
        }

        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked = true;
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_STORAGE);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();  // Always call the superclass
        activityIsDestroyed = true;
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (clicked|firstLoading) {
            mLoadingView.setVisibility(View.VISIBLE);
            btnGenerate.setVisibility(View.GONE);
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        restored = true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        new DownloadJSON(getApplicationContext()).execute();
        setTitle(R.string.app_name);
        firstExecution = false;
        fr = new ViewPagerFragment();
        tag = PersonsFragment.class.getSimpleName();
        if (!activityIsDestroyed)
            fm.beginTransaction()
                    .replace(android.R.id.content, fr, tag)
                    .addToBackStack("fr_image_list")
                    .commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        clicked = false;
        firstLoading = false;
        btnGenerate.setVisibility(View.VISIBLE);
        mLoadingView.setVisibility(View.GONE);
    }

    private class InitializeData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            btnGenerate.setVisibility(View.GONE);
            mLoadingView.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            API.INSTANCE.init(getApplicationContext());
            API.INSTANCE.refreshPersons(new SuccessCallback() {
                @Override
                public void onSuccess() {
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            super.onPostExecute(args);
            if (firstExecution)
                new ParseJSON(getApplicationContext()).execute();
        }
    }

    private class ParseJSON extends AsyncTask<Void, Void, Void> {
        Context context;
        private ParseJSON(Context context) {
//            this.context = context.getApplicationContext();
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            getPersons();

            return null;
        }

        protected List<ModelPerson> persons = null;

        private void getPersons() {

            if (firstExecution) {
                API.INSTANCE.getPersons(Constants.getPageNum(), new PersonsExtendedCallback() {
                    @Override
                    public void onResult(String json) {
                        if (json.equals("[]")) {
                            Constants.setPageNum(0);
                            getPersons();
                        } else {
                            Type listType = new TypeToken<ArrayList<ModelPerson>>() {}.getType();
                            persons = (List<ModelPerson>) new Gson().fromJson(json, listType);

                            final ModelPersonsHolder personsHolder = new ModelPersonsHolder(context);
                            personsHolder.savePersons(persons);
//                         Constants.setPersons(persons);
                        }
                    }
                    @Override
                    public void onFail(String reason) {}
                });
            }
        }

        @Override
        protected void onPostExecute(Void args) {
            super.onPostExecute(args);
            firstLoading = false;
            btnGenerate.setVisibility(View.VISIBLE);
            mLoadingView.setVisibility(View.GONE);

        }
    }
}


