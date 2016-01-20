package com.gusar.datingapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Transformation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.gusar.datingapp.Constants;
import com.gusar.datingapp.MainActivity;
import com.gusar.datingapp.R;
import com.gusar.datingapp.model.ModelPerson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static com.gusar.datingapp.DatingApplication.initImageLoader;

/**
 * @author evoyager
 */
public class ImageListFragment extends Fragment {

    static final int ANIMATION_DURATION = 200;
    protected ListView listView;
    private static List<MyCell> mAnimList = new ArrayList<MyCell>();
    private List<ModelPerson> persons;
    private ImageAdapter mMyAnimListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fr_image_list, container, false);
        persons = Constants.getPersons();

        for (int i = 0; i < persons.size(); i++) {
            MyCell cell = new MyCell();
            mAnimList.add(cell);
        }

        mMyAnimListAdapter = new ImageAdapter(getActivity(), R.layout.item_list_image, mAnimList);
        listView = (ListView) rootView.findViewById(android.R.id.list);
        (listView).setAdapter(mMyAnimListAdapter);

        return rootView;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        setHasOptionsMenu(true);
        inflater.inflate(R.menu.main_menu, menu);
    }

    private void deleteCell(final View v, final int index) {
        AnimationListener al = new AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                mAnimList.remove(index);
                persons.remove(index);

                ViewHolder vh = (ViewHolder)v.getTag();
                vh.needInflate = true;

                mMyAnimListAdapter.notifyDataSetChanged();

                if(mAnimList.size() == 0) {
                    Intent i = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                    startActivity(i);
                }
            }

            @Override public void onAnimationRepeat(Animation animation) {}
            @Override public void onAnimationStart(Animation animation) {}
        };
        collapse(v, al);
    }

    private void collapse(final View v, AnimationListener al) {
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
        applyScrollListener();
    }

    private void applyScrollListener() {
        listView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), false, false));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AnimateFirstDisplayListener.displayedImages.clear();
    }

    public class ImageAdapter extends ArrayAdapter<MyCell> {

        private LayoutInflater inflater;
        private int resId;
        private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
        private DisplayImageOptions options;

        public ImageAdapter(Context context, int textViewResourceId, List<MyCell> objects) {
            super(context, textViewResourceId, objects);
            initImageLoader(context);

//            inflater = LayoutInflater.from(context);
            this.resId = textViewResourceId;
            this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.ic_stub)
                    .showImageForEmptyUri(R.drawable.ic_empty)
                    .showImageOnFail(R.drawable.ic_error)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .displayer(new CircleBitmapDisplayer(Color.WHITE, 5))
                    .build();
        }

//        @Override
//        public int getCount() {
//            return persons.size();
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final View view;
            ViewHolder holder;

            if (convertView == null) {
                view = inflater.inflate(R.layout.item_list_image, parent, false);
                setViewHolder(view);
            }
            else if (((ViewHolder)convertView.getTag()).needInflate) {
                view = inflater.inflate(R.layout.item_list_image, parent, false);
                setViewHolder(view);
            }
            else {
                view = convertView;
            }

            holder = (ViewHolder) view.getTag();
            ImageLoader.getInstance().displayImage(persons.get(position).getPhoto(), holder.image, options, animateFirstListener);
            holder.btnDislike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteCell(view, position);
                }
            });
            holder.btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteCell(view, position);
                }
            });

            return view;
        }

        private void setViewHolder(View view) {
            ViewHolder vh = new ViewHolder();
            vh.image = (ImageView) view.findViewById(R.id.image);
            vh.btnLike = (Button) view.findViewById(R.id.btnLike);
            vh.btnDislike = (Button) view.findViewById(R.id.btnDislike);
            vh.needInflate = false;
            view.setTag(vh);
        }
    }

    private class ViewHolder {
        public boolean needInflate;
        ImageView image;
        Button btnDislike;
        Button btnLike;
    }

    private static class MyCell {
    }

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

}
