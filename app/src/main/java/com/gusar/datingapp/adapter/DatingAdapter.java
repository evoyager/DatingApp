package com.gusar.datingapp.adapter;

import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;

import com.gusar.datingapp.fragment.DatingFragment;

/**
 * Created by evgeniy on 15.01.16.
 */
public abstract class DatingAdapter extends Adapter<ViewHolder> {

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
