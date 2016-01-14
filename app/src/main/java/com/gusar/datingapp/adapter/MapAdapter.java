package com.gusar.datingapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gusar.datingapp.R;
import com.gusar.datingapp.fragment.MapFragment;

/**
 * Created by evgeniy on 15.01.16.
 */
public class MapAdapter extends DatingAdapter {
    public MapAdapter(MapFragment mapFragment) {
        super(mapFragment);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }
}
