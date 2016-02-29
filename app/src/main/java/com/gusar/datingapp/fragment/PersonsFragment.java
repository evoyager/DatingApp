package com.gusar.datingapp.fragment;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gusar.datingapp.MainActivity;
import com.gusar.datingapp.MatchActivity;
import com.gusar.datingapp.R;
import com.gusar.datingapp.imagesdownloader.ImageLoader;
import com.gusar.datingapp.model.ModelPerson;
import com.gusar.datingapp.service.MyService;

import org.testpackage.test_sdk.android.testlib.API;
import org.testpackage.test_sdk.android.testlib.services.UpdateService;

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
    Button btnLike;
    Button btnDislike;
    private static final int NOTIFY_ID = 101;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        notifyRemovedStatus();
        Toast.makeText(getActivity(), "CHANGED", Toast.LENGTH_SHORT).show();
        API.INSTANCE.subscribeUpdates(new UpdateService.UpdateServiceListener(){

            @Override
            public void onChanges(String person) {
                ModelPerson changedPerson = new Gson().fromJson(person, ModelPerson.class);
                changedPerson.getId();
                ModelPerson oldPerson = null;
                for (ModelPerson p: persons) {
                    if (p.getId() == changedPerson.getId())
                        oldPerson = p;
                }

                persons.set(persons.indexOf(oldPerson), changedPerson);
                Toast.makeText(getActivity(), "CHANGED", Toast.LENGTH_SHORT).show();
                notifyRemovedStatus();
//                Toast.makeText(getActivity(), "CHANGED", Toast.LENGTH_SHORT).show();
                if (changedPerson.getStatus().equals("removed")) {
                }
//                    showToast("Person removed");
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fr_download_images,  container, false);
        View listViewInfl = inflater.inflate(R.layout.listview_item,  container, false);
        final Context c = getActivity();
        notifyRemovedStatus();

        persons = getArguments().getParcelableArrayList("persons");
        final Context context = getActivity();

        btnLike = (Button) listViewInfl.findViewById(R.id.btnLike);
        btnDislike = (Button) listViewInfl.findViewById(R.id.btnDislike);

        mMyAnimListAdapter = new ImageAdapter(getActivity(), R.layout.listview_item, persons);
        listView = (ListView) rootView.findViewById(R.id.listview);
        (listView).setAdapter(mMyAnimListAdapter);

        return rootView;
    }

    public void notifyRemovedStatus() {
        Intent notificationIntent = new Intent(Intent.ACTION_VIEW);
        notificationIntent.setData(Uri.parse("http://google.com"));
        PendingIntent contentIntent = PendingIntent.getActivity(getActivity(), 0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(getActivity())
                .setCategory(Notification.CATEGORY_PROMO)
                .setSmallIcon(R.drawable.heart)
                .setContentTitle("DatingApp")
                .setContentText("Person removed")
                .setAutoCancel(true)
//                .addAction(android.R.drawable.ic_menu_view, "View details", contentIntent)
                .setContentIntent(contentIntent)
                .setPriority(Notification.PRIORITY_HIGH)
                .setVibrate(new long[]{1000})
                .build();
        NotificationManager notificationManager =
                (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFY_ID, notification);
    }

    public void showToast(String s) {
        Toast toast = Toast.makeText(getActivity().getApplicationContext(),s,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
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
            }
            else if (((ViewHolder)convertView.getTag()).needInflate) {
                view = inflater.inflate(R.layout.listview_item, parent, false);
            }
            else {
                view = convertView;
            }

            setViewHolder(view, position);

            holder = (ViewHolder) view.getTag();
            currentPerson = persons.get(position);

            if (currentPerson.getStatus().equals("removed")) {
                view.setVisibility(View.GONE);
                deleteCell(view, position);
            }

            else {
                holder.btnDislike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewHolder holderr = (ViewHolder) view.getTag();
                        ModelPerson currentPersonn = persons.get(position);
                        MainActivity.removeIdOfLikedPerson(currentPersonn.getId());
                        holderr.heart.setVisibility(View.GONE);
                        deleteCell(view, position);
//                        Toast.makeText(getActivity(), "Click!", Toast.LENGTH_SHORT).show();
                    }
                });
                holder.btnLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewHolder holderr = (ViewHolder) view.getTag();
                        ModelPerson currentPersonn = persons.get(position);
                        MainActivity.addIdOfLikedPerson(currentPersonn.getId());
//                        Intent intent = new Intent(getActivity(), MatchActivity.class);
//                        intent.putExtra("url", currentPerson.getPhoto());
                        holderr.heart.setVisibility(View.VISIBLE);
                        deleteCell(view, position);
//                        startActivity(intent);
//                        Toast.makeText(getActivity(), "Click!", Toast.LENGTH_SHORT).show();
                    }
                });
                photo = (ImageView) view.findViewById(R.id.photo);
                imageLoader.DisplayImage(currentPerson.getPhoto(), photo);
            }

            return view;
        }

        private void setViewHolder(View view, int pos) {
            ViewHolder vh = new ViewHolder();
            vh.image = (ImageView) view.findViewById(R.id.image);
            vh.heart = (ImageView) view.findViewById(R.id.heart);
            vh.btnLike = (Button) view.findViewById(R.id.btnLike);
            vh.btnDislike = (Button) view.findViewById(R.id.btnDislike);

            vh.needInflate = false;

            if ((persons.get(pos).getStatus().equals("like"))||(MainActivity.personIsLiked(persons.get(pos).getId()))) {
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
