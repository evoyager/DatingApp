package com.gusar.datingapp;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gusar.datingapp.fragment.ImageListFragment;
import com.gusar.datingapp.model.ModelPerson;
import com.nostra13.universalimageloader.utils.L;

import org.testpackage.test_sdk.android.testlib.API;
import org.testpackage.test_sdk.android.testlib.interfaces.PersonsExtendedCallback;
import org.testpackage.test_sdk.android.testlib.interfaces.SuccessCallback;
import org.testpackage.test_sdk.android.testlib.model.Person;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity {

    private static final String TEST_FILE_NAME = "Universal Image Loader @#&=+-_.,!()~'%20.png";
    Fragment fr;
    String tag;
    ProgressDialog mProgressDialog;
    List<ModelPerson> persons;
    Button btnGenerate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnGenerate = (Button) findViewById(R.id.btnGenerate);
        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File testImageOnSdCard = new File("/mnt/sdcard", TEST_FILE_NAME);
                if (!testImageOnSdCard.exists()) {
                    copyTestImageToSdCard(testImageOnSdCard);
                }
                new DownloadJSON().execute();
            }
        });
    }

     // DownloadJSON AsyncTask
    private class DownloadJSON extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(MainActivity.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Parsing data from JSON through testlib API");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
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
//                    persons = new Gson().fromJson(json, listType);
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

            setTitle(R.string.app_name);
            fr = new ImageListFragment();
            tag = ImageListFragment.class.getSimpleName();
            getSupportFragmentManager().beginTransaction().replace(android.R.id.content, fr, tag).commit();
        }

    }

    private void copyTestImageToSdCard(final File testImageOnSdCard) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream is = getAssets().open(TEST_FILE_NAME);
                    FileOutputStream fos = new FileOutputStream(testImageOnSdCard);
                    byte[] buffer = new byte[8192];
                    int read;
                    try {
                        while((read = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, read);
                        }
                    } finally {
                        fos.flush();
                        fos.close();
                        is.close();
                    }
                } catch (IOException e) {
                    L.w("Can't copy test image onto SD card");
                }
            }
        }).start();
    }
}
