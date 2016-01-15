package com.gusar.datingapp.adapter;

import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gusar.datingapp.fragment.DatingFragment;
import com.gusar.datingapp.model.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by evgeniy on 15.01.16.
 */
public abstract class DatingAdapter extends Adapter<ViewHolder> {

    List<Item> items = new ArrayList();
    DatingFragment datingFragment;

    protected class DatingViewHolder extends ViewHolder {
        protected TextView photo;

        public DatingViewHolder(View itemView, TextView photo) {
            super(itemView);
            this.photo = photo;
        }
    }

    public DatingAdapter(DatingFragment datingFragment) {
        this.datingFragment = datingFragment;
    }

    public Item getItem(int position) {
        return (Item) this.items.get(position);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public DatingFragment getDatingFragment() {return this.datingFragment;}
}
