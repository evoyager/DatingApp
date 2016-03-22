package com.gusar.datingapp;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private boolean firstExecution = true;
    boolean activityIsDestroyed;
    private List<ModelPerson> persons1 = new ArrayList<ModelPerson>();
    private List<ModelPerson> persons2 = new ArrayList<ModelPerson>();

    private List<ModelPerson> persons = new ArrayList<ModelPerson>();
    public static int page_num = 0;
    public static final int NOTIFY_ID = 101;

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
        super.onDestroy();
        activityIsDestroyed = true;
        unSubscribeUpdatesAndCancelNotification();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unSubscribeUpdatesAndCancelNotification();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        clicked = false;
        firstLoading = false;
        btnGenerate.setVisibility(View.VISIBLE);
        mLoadingView.setVisibility(View.GONE);
        unSubscribeUpdatesAndCancelNotification();
    }

    private void unSubscribeUpdatesAndCancelNotification(){
        API.INSTANCE.unSubscribeUpdates();
        cancelNotification(NOTIFY_ID);
    }

    private void cancelNotification(int notify_id) {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(notify_id);
    }

    private static Map<Integer, Boolean> likedPersons = new HashMap<Integer, Boolean>();

    private static Map<Integer, Boolean> likedFromButtonPersons = new HashMap<Integer, Boolean>();

    public static boolean personIsLiked(Integer i) {
        return likedPersons.containsKey(i);
    }

    public static boolean personIsLikedFromButton(Integer i) {
        return likedFromButtonPersons.containsKey(i);
    }

    public static void removeIdOfLikedPerson(Integer i) {
        likedPersons.remove(i);
    }

    public static void removeIdOfLikedFromButtonPerson(Integer i) {
        likedFromButtonPersons.remove(i);
    }

    public static void addIdOfLikedPerson(Integer i) {
        likedPersons.put(i, true);
    }

    public static void addIdOfLikedFromButtonPerson(Integer i) {
        likedFromButtonPersons.put(i, true);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (clicked | firstLoading) {
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
        new ParseJSON(getApplicationContext()).execute();
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
            btnGenerate.setVisibility(View.VISIBLE);
            mLoadingView.setVisibility(View.GONE);
        }
    }

    private class ParseJSON extends AsyncTask<Void, Void, Void> {
        Context context;

        private ParseJSON(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            btnGenerate.setVisibility(View.GONE);
            mLoadingView.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            getPersons();

            return null;
        }

        private void getPersons() {

            API.INSTANCE.getPersons(page_num, new PersonsExtendedCallback() {
                @Override
                public void onResult(String json) {
                    if (json.equals("[]")) {
                        page_num = 0;
//                        getPersons();
                    } else {
                        Type listType = new TypeToken<ArrayList<ModelPerson>>() {
                        }.getType();
                        persons.addAll((List<ModelPerson>) new Gson().fromJson(json, listType));
                        page_num++;
                        getPersons();
                    }
                }

                @Override
                public void onFail(String reason) {
                }
            });

        }

        @Override
        protected void onPostExecute(Void args) {
            super.onPostExecute(args);
            firstLoading = false;
            mLoadingView.setVisibility(View.GONE);
//            page_num++;

            setTitle(R.string.app_name);
            firstExecution = false;
            fr = new ViewPagerFragment();
            tag = PersonsFragment.class.getSimpleName();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("persons", (ArrayList) persons);
                fr.setArguments(bundle);
                fm.beginTransaction()
                        .replace(android.R.id.content, fr, tag)
                        .addToBackStack("fr_image_list")
                        .commitAllowingStateLoss();
        }
    }
}


