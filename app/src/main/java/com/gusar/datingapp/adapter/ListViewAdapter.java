package com.gusar.datingapp.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.maps.GoogleMap;
import com.gusar.datingapp.Constants;
import com.gusar.datingapp.MyCell;
import com.gusar.datingapp.R;
import com.gusar.datingapp.imagesdownloader.ImageLoader;
import com.gusar.datingapp.model.ModelPerson;

public class ListViewAdapter extends ArrayAdapter<MyCell> {


    // Declare Variables
    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    ImageLoader imageLoader;
    private GoogleMap map;
    private ModelPerson currentPerson;
    private List<ModelPerson> persons = Constants.getPersons();
    private static List<MyCell> mAnimList = new ArrayList<MyCell>();
    static final int ANIMATION_DURATION = 200;
    ListView listview;
    private int resId;

    public ListViewAdapter(Context context, int textViewResourceId, List<MyCell> objects) {
        super(context, textViewResourceId, objects);
        resId = textViewResourceId;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.context = context;
        imageLoader = new ImageLoader(context);
        for (int i = 0; i < persons.size(); i++) {
            MyCell cell = new MyCell();
            mAnimList.add(cell);
        }
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView photo;
        final View view;
        final ViewHolder holder;


        if (convertView == null) {
            view = inflater.inflate(R.layout.listview_item, parent, false);
            setViewHolder(view);
        } else
            view = convertView;

        holder = (ViewHolder) view.getTag();
        currentPerson = persons.get(position);
        if (Constants.isLiked(currentPerson.getId()))
            holder.heart.setVisibility(View.VISIBLE);

        holder.btnDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.changeLikeStatus(persons.get(position).getId(), false);
                holder.heart.setVisibility(View.INVISIBLE);
                deleteCell(view, position);
            }
        });

        holder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.changeLikeStatus(persons.get(position).getId(), true);
                holder.heart.setVisibility(View.VISIBLE);
                deleteCell(view, position);
            }
        });

        // Locate the ImageView in listview_item.xml
        photo = (ImageView) view.findViewById(R.id.photo);

        imageLoader.DisplayImage(currentPerson.getPhoto(), photo);
        return view;
    }

    private void setViewHolder(View view) {
        ViewHolder vh = new ViewHolder();
        vh.image = (ImageView) view.findViewById(R.id.image);
        vh.heart = (ImageView) view.findViewById(R.id.heart);
        vh.btnLike = (Button) view.findViewById(R.id.btnLike);
        vh.btnDislike = (Button) view.findViewById(R.id.btnDislike);
        vh.needInflate = false;
        view.setTag(vh);
    }

    private class ViewHolder {
        public boolean needInflate;
        ImageView image;
        ImageView heart;
        Button btnDislike;
        Button btnLike;
    }

    private void deleteCell(final View v, final int index) {
        Animation.AnimationListener al = new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                mAnimList.remove(index);
                persons.remove(index);

                ViewHolder vh = (ViewHolder)v.getTag();
                vh.needInflate = true;

                notifyDataSetChanged();

//                if(mAnimList.size() == 0) {
//                    getFragmentManager().popBackStack();
//                }
            }

            @Override public void onAnimationRepeat(Animation animation) {}
            @Override public void onAnimationStart(Animation animation) {}
        };
        collapse(v, al);
    }

    private void collapse(final View v, Animation.AnimationListener al) {
        final int initialHeight = v.getMeasuredHeight();

        Animation anim = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                }
                else {
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        if (al!=null) {
            anim.setAnimationListener(al);
        }
        anim.setDuration(ANIMATION_DURATION);
        v.startAnimation(anim);
    }
}
