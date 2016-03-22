package com.gusar.datingapp.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.gusar.datingapp.MainActivity;
import com.gusar.datingapp.MatchActivity;
import com.gusar.datingapp.R;
import com.gusar.datingapp.imagesdownloader.ImageLoader;
import com.gusar.datingapp.model.ModelPerson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by igusar on 2/1/16.
 */
public class PersonsFragment extends Fragment {

    protected ListView listView;
    private List<ModelPerson> persons = new ArrayList<ModelPerson>();
    public static ImageAdapter mMyAnimListAdapter;
    ImageLoader imageLoader;
    Button btnLike;
    Button btnDislike;
    private LayoutInflater inflater;
    NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
    View view;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inboxStyle.addLine("Removed persons: ");

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fr_download_images, container, false);
        View listViewInfl = inflater.inflate(R.layout.listview_item, container, false);
        final Context c = getActivity();

        persons = getArguments().getParcelableArrayList("persons");

        btnLike = (Button) listViewInfl.findViewById(R.id.btnLike);
        btnDislike = (Button) listViewInfl.findViewById(R.id.btnDislike);

        mMyAnimListAdapter = new ImageAdapter(getActivity(), R.layout.listview_item, persons);
        listView = (ListView) rootView.findViewById(R.id.listview);
        (listView).setAdapter(mMyAnimListAdapter);

        return rootView;
    }

    protected void removeListItem(View rowView, final int positon) {
        Animation out = AnimationUtils.makeOutAnimation(getActivity(), true);
        rowView.startAnimation(out);
        rowView.setVisibility(View.INVISIBLE);
        Handler handle = new Handler(Looper.getMainLooper());
        handle.postDelayed(new Runnable() {

            public void run() {
                try {
                    persons.remove(positon);
                } catch (IndexOutOfBoundsException e){}
                mMyAnimListAdapter.notifyDataSetChanged();
                if (persons.size() == 0) {
                    try {
                        getActivity().onBackPressed();
                    } catch (NullPointerException e){}
                }
            }
        }, 300);
    }

    public class ImageAdapter extends ArrayAdapter<ModelPerson> {
        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            if (persons.size() == 0) {
                Context c = getActivity();
            }
        }

        private int resId;
        Context context;

        public ImageAdapter(Context context, int textViewResourceId, List<ModelPerson> objects) {
            super(context, textViewResourceId, objects);
            this.context = context;
            this.resId = textViewResourceId;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            imageLoader = new ImageLoader(context);
        }

        @Override
        public int getCount() {
            return persons.size();
        }

        @Override
        public ModelPerson getItem(int pos) {
            return persons.get(pos);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final ViewHolder holder;
            ImageView photo;
            ModelPerson currentPerson = persons.get(position);
            view = inflater.inflate(R.layout.listview_item, null, false);

            setViewHolder(view, position);

            holder = (ViewHolder) view.getTag();

            final View finalView = view;
            holder.btnDislike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewHolder holderr = (ViewHolder) finalView.getTag();
                    final ModelPerson currentPersonn = persons.get(position);
                    MainActivity.removeIdOfLikedPerson(currentPersonn.getId());
                    MainActivity.removeIdOfLikedFromButtonPerson(currentPersonn.getId());
                    holderr.heart.setVisibility(View.GONE);
                    removeListItem(finalView, position);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MapFragment.markers.get(currentPersonn.getId()).remove();
                        }
                    });
                }
            });
            holder.btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewHolder holderr = (ViewHolder) finalView.getTag();
                    final ModelPerson currentPersonn = persons.get(position);
                    if (MainActivity.personIsLiked(persons.get(position).getId())) {
                        Intent intent = new Intent(getActivity(), MatchActivity.class);
                        intent.putExtra("url", currentPersonn.getPhoto());
                        startActivity(intent);
                    }
                    MainActivity.addIdOfLikedPerson(currentPersonn.getId());
                    MainActivity.addIdOfLikedFromButtonPerson(currentPersonn.getId());

                    holderr.heart.setVisibility(View.VISIBLE);
                    removeListItem(finalView, position);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MapFragment.markers.get(currentPersonn.getId()).remove();
                        }
                    });
                }
            });
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

            if ((persons.get(pos).getStatus().equals("like")) || (MainActivity.personIsLiked(persons.get(pos).getId()))) {
                vh.heart.setVisibility(View.VISIBLE);
            } else vh.heart.setVisibility(View.GONE);

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
