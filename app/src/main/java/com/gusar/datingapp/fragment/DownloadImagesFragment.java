package com.gusar.datingapp.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gusar.datingapp.Constants;
import com.gusar.datingapp.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by igusar on 2/1/16.
 */
public class DownloadImagesFragment extends Fragment {

    ProgressBar progress;
    final int NUMBER_OF_PERSONS = Constants.getPersons().size();
    ImageView[] targetImage = new ImageView[NUMBER_OF_PERSONS];
    int[] images = new int[NUMBER_OF_PERSONS];


    ArrayList<Person> persons = new ArrayList<Person>();
    DownloadImagesAdapter diAdapter;

    final String LOG_TAG = "myLogs";

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(LOG_TAG, "Fragment1 onAttach");
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "Fragment1 onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fr_download_images, null);
        for (int i = 0; i < NUMBER_OF_PERSONS; i++)
            images[i] = R.drawable.ic_launcher;
        images[0] = R.drawable.a;
        images[1] = R.drawable.b;
        images[2] = R.drawable.c;
        images[3] = R.drawable.d;
        images[4] = R.drawable.e;
        images[5] = R.drawable.f;
        images[6] = R.drawable.g;
        images[7] = R.drawable.h;
        images[8] = R.drawable.a;
        images[9] = R.drawable.b;


        fillData();
        diAdapter = new DownloadImagesAdapter(getActivity(), persons);

        ListView lvMain = (ListView) rootView.findViewById(R.id.persons_list);
        lvMain.setAdapter(diAdapter);

        return rootView;
    }

    private void fillData() {
        for (int i = 0; i < NUMBER_OF_PERSONS; i++) {
            persons.add(new Person(images[i]));
        }
    }

    private class MyAsyncTask extends AsyncTask<URL, Integer, Void> {

        ImageView[] aIV;
        Bitmap[] aBM;
        ProgressBar progressBar;

        public MyAsyncTask(ImageView[] iv, ProgressBar pb) {
            aBM = new Bitmap[iv.length];

            aIV = new ImageView[iv.length];
            for (int i = 0; i < iv.length; i++)
                aIV[i] = iv[i];

            progressBar = pb;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(URL... urls) {
            if(urls.length > 0) {
                for (int i = 0; i < urls.length; i++) {
                    URL networkUrl = urls[i];

                    try {
                        aBM[i] = BitmapFactory.decodeStream(networkUrl.openConnection().getInputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    publishProgress(i);

                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Toast.makeText(getActivity(), "Загрузка завершена", Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if (values.length > 0) {
                for (int i = 0; i < values.length; i++) {
                    aIV[values[i]].setImageBitmap(aBM[values[i]]);
                    progressBar.setProgress((values[i]+1) * 33 + 1);
                }
            }
        }
    }

    private class DownloadImagesAdapter extends BaseAdapter {
        Context ctx;
        LayoutInflater lInflater;
        ArrayList<Person> persons;

        public DownloadImagesAdapter(Context context, ArrayList<Person> persons) {
            ctx = context;
            this.persons = persons;
            lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return persons.size();
        }

        @Override
        public Object getItem(int position) {
            return persons.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = lInflater.inflate(R.layout.item_list_image, parent, false);
            }

            Person p = getPerson(position);
            ((ImageView) view.findViewById(R.id.image)).setImageResource(p.image);


            return view;
        }

        Person getPerson(int position) {
            return ((Person) getItem(position));
        }
    }

    private class Person {
        int image;

        public Person(int image) {
            this.image = image;
        }
    }
}
