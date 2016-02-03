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
import com.gusar.datingapp.adapter.ListViewAdapter;
import com.gusar.datingapp.model.ModelPerson;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by igusar on 2/1/16.
 */
public class DownloadImagesFragment extends Fragment {

    ListView listview;
    ListViewAdapter adapter;
    ArrayList<HashMap<String, String>> arraylist;
    private List<ModelPerson> persons;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fr_download_images, null);

        persons = Constants.getPersons();
        listview = (ListView) rootView.findViewById(R.id.listview);
        arraylist = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < persons.size(); i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put("photo", persons.get(i).getPhoto());
            arraylist.add(map);
        }

        adapter = new ListViewAdapter(getActivity(), arraylist);
        listview.setAdapter(adapter);

        return rootView;
    }
}
