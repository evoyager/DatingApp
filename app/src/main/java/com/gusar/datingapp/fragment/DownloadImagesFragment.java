package com.gusar.datingapp.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gusar.datingapp.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by igusar on 2/1/16.
 */
public class DownloadImagesFragment extends Fragment {

    ProgressBar progress;
    ImageView[] targetImage = new ImageView[3];
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

        targetImage[0] = (ImageView) rootView.findViewById(R.id.target0);
        targetImage[1] = (ImageView) rootView.findViewById(R.id.target1);
        targetImage[2] = (ImageView) rootView.findViewById(R.id.target2);

        progress = (ProgressBar) rootView.findViewById(R.id.progress);

        String urlImage0 = "http://developer.alexanderklimov.ru/android/images/pinkhellokitty.jpg";
        String urlImage1 = "http://developer.alexanderklimov.ru/android/images/keyboard-cat.jpg";
        String urlImage2 = "http://developer.alexanderklimov.ru/android/images/cat-tips.jpg";

        URL myURL0, myURL1, myURL2;

        try {
            myURL0 = new URL(urlImage0);
            myURL1 = new URL(urlImage1);
            myURL2 = new URL(urlImage2);
            new MyAsyncTask(targetImage, progress).execute(myURL0, myURL1, myURL2);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return rootView;
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
}
