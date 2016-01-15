package com.gusar.datingapp.adapter;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gusar.datingapp.R;
import com.gusar.datingapp.fragment.PersonsFragment;
import com.gusar.datingapp.model.Item;
import com.gusar.datingapp.model.ModelPerson;

/**
 * Created by evgeniy on 15.01.16.
 */
public class PersonsAdapter extends DatingAdapter {
    public PersonsAdapter(PersonsFragment personsFragment) {
        super(personsFragment);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.model_dating, viewGroup, false);
        return new DatingViewHolder(v, (TextView) v.findViewById(R.id.tvPersons));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Item item = (Item) this.items.get(position);
        final Resources resources = viewHolder.itemView.getResources();
        if (item.isPerson()) {
            viewHolder.itemView.setEnabled(true);
            final ModelPerson person = (ModelPerson) item;
            final DatingViewHolder datingViewHolder = (DatingViewHolder) viewHolder;
            final View itemView = datingViewHolder.itemView;
            datingViewHolder.photo.setText(person.getPhoto());
            itemView.setBackgroundColor(resources.getColor(R.color.gray_50));

        }
    }
}
