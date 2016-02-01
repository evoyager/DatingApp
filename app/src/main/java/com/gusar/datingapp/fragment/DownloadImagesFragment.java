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
import com.gusar.datingapp.model.ModelPerson;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by igusar on 2/1/16.
 */
public class DownloadImagesFragment extends Fragment {

    ProgressBar progress;

    final int NUMBER_OF_PERSONS = Constants.getPersons().size();
    ImageView[] targetImage = new ImageView[NUMBER_OF_PERSONS];
    int[] images = new int[NUMBER_OF_PERSONS];
    Bitmap[] aBM = new Bitmap[NUMBER_OF_PERSONS];

    ArrayList<Person> persons = new ArrayList<Person>();
    DownloadImagesAdapter diAdapter;
    private List<ModelPerson> modelpersons;
    String[] strImages = new String[NUMBER_OF_PERSONS];
    URL[] urlImages = new URL[NUMBER_OF_PERSONS];

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
        modelpersons = Constants.getPersons();

        fillData();
        diAdapter = new DownloadImagesAdapter(getActivity(), persons);

        ListView lvMain = (ListView) rootView.findViewById(R.id.persons_list);
        lvMain.setAdapter(diAdapter);

        return rootView;
    }

    private void fillData() {

        for (int i = 0; i < NUMBER_OF_PERSONS; i++) {
            strImages[i] = modelpersons.get(i).getPhoto();
            try {
                urlImages[i] = new URL(strImages[i]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        new MyAsyncTask().execute(urlImages);
    }

    private class MyAsyncTask extends AsyncTask<URL, Integer, Void> {

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
                        persons.add(new Person(aBM[i]));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

//                    publishProgress(i);

//                    try {
//                        Thread.sleep(1500);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Toast.makeText(getActivity(), "Загрузка завершена", Toast.LENGTH_LONG).show();
        }

//        @Override
//        protected void onProgressUpdate(Integer... values) {
//            if (values.length > 0) {
//                for (int i = 0; i < values.length; i++) {
//                    aIV[values[i]].setImageBitmap(aBM[values[i]]);
//                    progressBar.setProgress((values[i]+1) * 33 + 1);
//                }
//            }
//        }
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
//            ((ImageView) view.findViewById(R.id.image)).setImageResource(p.image);

            ((ImageView) view.findViewById(R.id.image)).setImageBitmap(aBM[position]);
            return view;
        }

        Person getPerson(int position) {
            return ((Person) getItem(position));
        }
    }

    private class Person {
        Bitmap image;

        public Person(Bitmap image) {
            this.image = image;
        }
    }
}
