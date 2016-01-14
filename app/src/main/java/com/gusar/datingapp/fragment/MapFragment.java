package com.gusar.datingapp.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gusar.datingapp.R;
import com.gusar.datingapp.adapter.MapAdapter;
import com.gusar.datingapp.adapter.PersonsAdapter;
import com.gusar.datingapp.model.ModelDating;

/**
 * Created by evgeniy on 14.01.16.
 */
public class MapFragment extends DatingFragment {
    OnPersonsListener onPersonsListener;

    public interface OnPersonsListener {
        void onPersons(ModelDating modelDating);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.onPersonsListener = (OnPersonsListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnPersonsListener");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        this.recyclerView = (RecyclerView) rootView.findViewById(R.id.rvMapDating);
        this.layoutManager = new LinearLayoutManager(getActivity());
        this.recyclerView.setLayoutManager(this.layoutManager);
        this.adapter = new MapAdapter(this);
        this.recyclerView.setAdapter(this.adapter);
        return rootView;
    }
}
