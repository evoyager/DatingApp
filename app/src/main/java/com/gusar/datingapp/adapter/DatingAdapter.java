package com.gusar.datingapp.adapter;

import android.support.v7.widget.RecyclerView;

import com.gusar.datingapp.fragment.DatingFragment;

/**
 * Created by igusar on 1/28/16.
 */
public abstract class DatingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    DatingFragment datingFragment;

    public DatingAdapter(DatingFragment datingFragment) {
        this.datingFragment = datingFragment;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public DatingFragment getDatingFragment() {return this.datingFragment;}
}
