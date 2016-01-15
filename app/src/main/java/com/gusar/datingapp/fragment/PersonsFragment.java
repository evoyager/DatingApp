package com.gusar.datingapp.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gusar.datingapp.R;
import com.gusar.datingapp.adapter.PersonsAdapter;
import com.gusar.datingapp.model.ModelPerson;

/**
 * Created by evgeniy on 14.01.16.
 */
public class PersonsFragment extends DatingFragment {
    OnMapListener onMapListener;

    public interface OnMapListener {
        void onMap(ModelPerson modelPerson);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.onMapListener = (OnMapListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnMapListener");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_persons, container, false);
        this.recyclerView = (RecyclerView) rootView.findViewById(R.id.rvPersonsDating);
        this.layoutManager = new LinearLayoutManager(getActivity());
        this.recyclerView.setLayoutManager(this.layoutManager);
        this.adapter = new PersonsAdapter(this);
        this.recyclerView.setAdapter(this.adapter);
        return rootView;
    }
}
