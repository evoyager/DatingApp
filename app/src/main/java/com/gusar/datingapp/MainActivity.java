package com.gusar.datingapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gusar.datingapp.loader.ImageLoader;

import org.testpackage.test_sdk.android.testlib.API;
import org.testpackage.test_sdk.android.testlib.interfaces.PersonsExtendedCallback;
import org.testpackage.test_sdk.android.testlib.interfaces.SuccessCallback;
import org.testpackage.test_sdk.android.testlib.model.Person;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    ImageView ivPersons;
    ImageLoader imageLoader;
    ProgressDialog mProgressDialog;
    ArrayList<HashMap<String, String>> arraylist;
    TextView tvJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvJson = (TextView) findViewById(R.id.tvJson);
        
        API.INSTANCE.init(getApplicationContext());
        API.INSTANCE.refreshPersons(new SuccessCallback() {
            @Override
            public void onSuccess() {}
        });
        API.INSTANCE.getPersons(0, new PersonsExtendedCallback() {
            @Override
            public void onResult(String json) {
                Type listType = new TypeToken<ArrayList<Person>>() {
                }.getType();
                List<Person> persons = new Gson().fromJson(json, listType);
                tvJson.setText(persons.get(0).getStatus());
//                    Type listType = new TypeToken<ArrayList<Person>>() {
//                    }.getType();
//                    List<Person> persons = new Gson().fromJson(json, listType);
            }

            @Override
            public void onFail(String reason) {
                tvJson.setText(reason);
            }
        });

        setContentView(R.layout.activity_home);



//        new DownloadJSON().execute();


//        ivPersons = (ImageView) findViewById(R.id.tvPersons);
//
//        API.INSTANCE.init(getApplicationContext());
//        API.INSTANCE.refreshPersons(new SuccessCallback() {
//            @Override
//            public void onSuccess() {
//            }
//        });

//        API.INSTANCE.getPersons(0, new PersonsExtendedCallback() {
//
//            @Override
//            public void onResult(String json) {
//                Type listType = new TypeToken<ArrayList<Person>>() {
//                }.getType();
//                List<Person> persons = new Gson().fromJson(json, listType);
////                ivPersons.setText(persons.get(0).getStatus());
////                ivPersons.setImageURI(Uri.parse(persons.get(0).getPhoto()));
////                ivPersons.setImageURI(Uri.parse("http://cs313217.vk.me/v313217800/436c/DO1w-2mKStQ.jpg"));
//
////                String url = "http://cs313217.vk.me/v313217800/436c/DO1w-2mKStQ.jpg";
//
//                String imgUrl = persons.get(0).getPhoto();
//
//                Context context;
//                context = MainActivity.this;
//                imageLoader = new ImageLoader(context);
//                imageLoader.DisplayImage(imgUrl, ivPersons);
//            }
//
//            @Override
//            public void onFail(String reason) {
//            }
//        });
    }

    private class DownloadJSON extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setTitle("Dating App");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            arraylist = new ArrayList<HashMap<String, String>>();
            API.INSTANCE.refreshPersons(new SuccessCallback() {
                @Override
                public void onSuccess() {}
            });
            API.INSTANCE.getPersons(0, new PersonsExtendedCallback() {
                @Override
                public void onResult(String json) {
                    tvJson.setText(json);
//                    Type listType = new TypeToken<ArrayList<Person>>() {
//                    }.getType();
//                    List<Person> persons = new Gson().fromJson(json, listType);
                }

                @Override
                public void onFail(String reason) {
                }
            });

            return null;
        }
    }


//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent i = new Intent(MainActivity.this, HomeActivity.class);
//                startActivity(i);
//                finish();
//            }
//        }, 2*1000);
}
