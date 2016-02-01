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
import android.widget.ArrayAdapter;
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
import java.util.Arrays;
import java.util.List;

/**
 * Created by igusar on 2/1/16.
 */
public class DownloadImagesFragment extends Fragment {

    final int NUMBER_OF_PERSONS = Constants.getPersons().size();

    DownloadImagesAdapter diAdapter;
    private List<ModelPerson> modelpersons;

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

        List<String> list = new ArrayList<>();
        for (ModelPerson person: modelpersons) {
            list.add(person.getPhoto());
        }

        diAdapter = new DownloadImagesAdapter(getActivity(), R.layout.item_list_image, list);

        ListView lvMain = (ListView) rootView.findViewById(R.id.persons_list);
        lvMain.setAdapter(diAdapter);

        return rootView;
    }

    private class DownloadImagesAdapter extends ArrayAdapter<String> {

        Context ctx;
        List<String> mList;
        LayoutInflater mInflater;
        int mResource;


        public DownloadImagesAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);

            ctx = context;
            mList = objects;
            mResource = resource;
            mInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if(convertView == null) {
                view = mInflater.inflate(mResource, parent, false);
            } else {
                view = convertView;
            }

            ImageView imageView = (ImageView) view.findViewById(R.id.image);
            imageView.setTag(mList.get(position));
            new LoadImage(imageView).execute();

            return view;
        }
    }

    private class LoadImage extends AsyncTask<Object, Void, Bitmap> {

        private ImageView imv;
        private URL path;

        public LoadImage(ImageView imv) {
            this.imv = imv;
            try {
                this.path = new URL(imv.getTag().toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Bitmap doInBackground(Object... params) {
            Bitmap bitmap = null;

            try {
                bitmap = BitmapFactory.decodeStream(path.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bitmap;
        }


        @Override
        protected void onPostExecute(Bitmap result) {
//            if (!imv.getTag().equals(path)) {
//                return;
//            }

            if (result != null && imv != null) {
                imv.setImageBitmap(result);
            }
        }
    }

    private class Person {
        Bitmap image;

        public Person(Bitmap image) {
            this.image = image;
        }
    }

}
