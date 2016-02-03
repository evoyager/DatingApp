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
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gusar.datingapp.model.ModelPerson;

import org.testpackage.test_sdk.android.testlib.API;
import org.testpackage.test_sdk.android.testlib.interfaces.PersonsExtendedCallback;
import org.testpackage.test_sdk.android.testlib.interfaces.SuccessCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    private static final String TEST_FILE_NAME = "Universal Image Loader @#&=+-_.,!()~'%20.png";
    Fragment fr;
    String tag;
    static ProgressDialog mProgressDialog;
    List<ModelPerson> persons;
    Button btnGenerate;
    private static final int REQUEST_STORAGE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
            MainActivity.mProgressDialog.dismiss();
            Intent intent = new Intent(context, DatingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
