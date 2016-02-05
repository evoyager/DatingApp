package com.gusar.datingapp.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.gusar.datingapp.Constants;
import com.gusar.datingapp.R;
import com.gusar.datingapp.imagesdownloader.ImageLoader;
import com.gusar.datingapp.model.ModelPerson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by igusar on 2/1/16.
 */
public class DownloadImagesFragment extends Fragment {

    static final int ANIMATION_DURATION = 200;
    protected ListView listView;
    private static List<MyCell> mAnimList = new ArrayList<MyCell>();
    private List<ModelPerson> persons;
    private ImageAdapter mMyAnimListAdapter;
    private ModelPerson currentPerson;
    ImageLoader imageLoader;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fr_download_images,  container, false);

        persons = Constants.getPersons();

        for (int i = 0; i < persons.size(); i++) {
            MyCell cell = new MyCell();
            mAnimList.add(cell);
        }

        mMyAnimListAdapter = new ImageAdapter(getActivity(), R.layout.listview_item, mAnimList);
        listView = (ListView) rootView.findViewById(R.id.listview);
        (listView).setAdapter(mMyAnimListAdapter);

        return rootView;
    }

    private void deleteCell(final View v, final int index) {
        Animation.AnimationListener al = new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                mAnimList.remove(index);
                persons.remove(index);

                ViewHolder vh = (ViewHolder)v.getTag();
                vh.needInflate = true;

                mMyAnimListAdapter.notifyDataSetChanged();

                if(mAnimList.size() == 0) {
                    getActivity().finish();
                }
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

    @Override
    public void onResume() {
        super.onResume();
    }

    public class ImageAdapter extends ArrayAdapter<MyCell> {

        private LayoutInflater inflater;
        private int resId;

        public ImageAdapter(Context context, int textViewResourceId, List<MyCell> objects) {
            super(context, textViewResourceId, objects);

            this.resId = textViewResourceId;
            this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            imageLoader = new ImageLoader(context);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final View view;
            final ViewHolder holder;
            ImageView photo;

            if (convertView == null) {
                view = inflater.inflate(R.layout.listview_item, parent, false);
                setViewHolder(view);
            }
            else if (((ViewHolder)convertView.getTag()).needInflate) {
                view = inflater.inflate(R.layout.listview_item, parent, false);
                setViewHolder(view);
            }
            else {
                view = convertView;
            }

            holder = (ViewHolder) view.getTag();
            currentPerson = persons.get(position);
            if (Constants.isLiked(currentPerson.getId())) {
                holder.heart.setVisibility(View.VISIBLE);
            }

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
    }

    private class ViewHolder {
        public boolean needInflate;
        ImageView image;
        ImageView heart;
        Button btnDislike;
        Button btnLike;
    }

    private static class MyCell {
    }
}
