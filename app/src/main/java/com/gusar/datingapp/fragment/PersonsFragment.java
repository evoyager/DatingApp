package com.gusar.datingapp.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.gusar.datingapp.Constants;
import com.gusar.datingapp.MatchActivity;
import com.gusar.datingapp.R;
import com.gusar.datingapp.db.ModelPersonsHolder;
import com.gusar.datingapp.imagesdownloader.ImageLoader;
import com.gusar.datingapp.interfaces.MyPersonsCallback;
import com.gusar.datingapp.model.ModelPerson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by igusar on 2/1/16.
 */
public class PersonsFragment extends Fragment {

    static final int ANIMATION_DURATION = 200;
    protected ListView listView;
    private List<ModelPerson> persons = new ArrayList<ModelPerson>();
    private ImageAdapter mMyAnimListAdapter;
    private ModelPerson currentPerson;
    ImageLoader imageLoader;
    int uiOptions;
    Button btnGenerate;
    Button btnLike;
    Button btnDislike;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ModelPersonsHolder personsHolder = new ModelPersonsHolder(getActivity());
        personsHolder.getPortionPersons(0, 10, new MyPersonsCallback() {
                             @Override
                             public void onResult(List<ModelPerson> personsFromHolder) {
                                 persons = personsFromHolder;
                             }
                         });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fr_download_images,  container, false);
        View listViewInfl = inflater.inflate(R.layout.listview_item,  container, false);

        btnLike = (Button) listViewInfl.findViewById(R.id.btnLike);
        btnDislike = (Button) listViewInfl.findViewById(R.id.btnDislike);

//        for (ModelPerson p: Constants.getPersons()) {
//            persons.add(p);
//        }

        mMyAnimListAdapter = new ImageAdapter(getActivity(), R.layout.listview_item, persons);
        listView = (ListView) rootView.findViewById(R.id.listview);
        (listView).setAdapter(mMyAnimListAdapter);

        return rootView;
    }

    private void deleteCell(final View v, final int index) {
        Animation.AnimationListener al = new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                persons.remove(index);

                ViewHolder vh = (ViewHolder)v.getTag();
                vh.needInflate = true;

                mMyAnimListAdapter.notifyDataSetChanged();

                if(persons.size() == 0) {
                    getActivity().onBackPressed();
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

    public class ImageAdapter extends ArrayAdapter<ModelPerson> {

        private LayoutInflater inflater;
        private int resId;

        public ImageAdapter(Context context, int textViewResourceId, List<ModelPerson> objects) {
            super(context, textViewResourceId, objects);

            this.resId = textViewResourceId;
            this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            imageLoader = new ImageLoader(context);
        }

        @Override
        public int getCount()
        {
            return persons.size();
        }

        @Override
        public ModelPerson getItem (int pos){
            return persons.get(pos);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final View view;
            final ViewHolder holder;
            ImageView photo;

            if (convertView == null) {
                view = inflater.inflate(R.layout.listview_item, parent, false);
//                setViewHolder(view, position);
            }
            else if (((ViewHolder)convertView.getTag()).needInflate) {
                view = inflater.inflate(R.layout.listview_item, parent, false);
//                setViewHolder(view, position);
            }
            else {
                view = convertView;
            }

            setViewHolder(view, position);

            holder = (ViewHolder) view.getTag();
            currentPerson = persons.get(position);
//            if (Constants.isLike(currentPerson.getId())) {
//                holder.heart.setVisibility(View.VISIBLE);
//            }

//            holder.btnDislike.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ViewHolder holderr = (ViewHolder) view.getTag();
//                    ModelPerson currentPersonn = persons.get(position);
//                    Constants.changeLikeStatus(currentPersonn.getId(), false);
//                    holderr.heart.setVisibility(View.GONE);
//                    deleteCell(view, position);
//                }
//            });
//            holder.btnLike.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ViewHolder holderr = (ViewHolder) view.getTag();
//                    ModelPerson currentPersonn = persons.get(position);
//                    Constants.changeLikeStatus(currentPersonn.getId(), true);
////                    Intent intent = new Intent(getActivity(), MatchActivity.class);
////                    intent.putExtra("url", currentPerson.getPhoto());
//                    holderr.heart.setVisibility(View.VISIBLE);
//                    deleteCell(view, position);
////                    startActivity(intent);
//                }
//            });
            photo = (ImageView) view.findViewById(R.id.photo);
            imageLoader.DisplayImage(currentPerson.getPhoto(), photo);

            return view;
        }

        private void setViewHolder(View view, int pos) {
            ViewHolder vh = new ViewHolder();
            vh.image = (ImageView) view.findViewById(R.id.image);
            vh.heart = (ImageView) view.findViewById(R.id.heart);
            vh.btnLike = (Button) view.findViewById(R.id.btnLike);
            vh.btnDislike = (Button) view.findViewById(R.id.btnDislike);

            vh.needInflate = false;

//            if (Constants.isLike(persons.get(pos).getId())) {
//                vh.heart.setVisibility(View.VISIBLE);
//            } else vh.heart.setVisibility(View.GONE);

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
}
