package com.gusar.datingapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.gusar.datingapp.fragment.DatingFragment;
import com.gusar.datingapp.fragment.PersonsFragment;

/**
 * Created by igusar on 1/28/16.
 */
public class PersonsAdapter extends DatingAdapter {
    public PersonsAdapter(PersonsFragment personsFragment) {
        super(personsFragment);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }
}
