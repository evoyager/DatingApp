package com.gusar.datingapp.fragment;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gusar.datingapp.MainActivity;
import com.gusar.datingapp.R;
import com.gusar.datingapp.imagesdownloader.ImageLoader;
import com.gusar.datingapp.model.ModelPerson;

import org.testpackage.test_sdk.android.testlib.API;
import org.testpackage.test_sdk.android.testlib.services.UpdateService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    Map<Integer, Boolean> idsOfRemovedPersons = new HashMap<Integer, Boolean>();
    Context context;
    private LayoutInflater inflater;

    private List<String> events = new ArrayList<>();
    String removedMsg = "Removed persons: ";
    String ids = "", longRemovedMsg = "";
    int numberOfAllerts = 0;
    NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
    View view;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        notifyRemovedStatus(1);

        final FragmentActivity fa = getActivity();
        inboxStyle.addLine("Removed persons: ");

        subscribeUpdates(getActivity());
    }

    private void subscribeUpdates(final Context context) {
        this.context = context;

        API.INSTANCE.subscribeUpdates(new UpdateService.UpdateServiceListener() {

            @Override
            public void onChanges(String person) {

                ModelPerson changedPerson = new Gson().fromJson(person, ModelPerson.class);
                final int idOfChangedPerson = changedPerson.getId();

                ModelPerson oldPerson = null;
                int index = 0;
                for (ModelPerson p : persons) {
                    if (p.getId() == idOfChangedPerson) {
                        oldPerson = p;
                        index = persons.indexOf(p);

                        persons.set(persons.indexOf(oldPerson), changedPerson);
                        if (changedPerson.getStatus().equals("removed")) {
                            persons.remove(index);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mMyAnimListAdapter.notifyDataSetChanged();
                                    if (persons.size() == 0) {
                                        getActivity().onBackPressed();
                                    }
                                }
                            });
                            notifyRemovedStatus(idOfChangedPerson);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        subscribeUpdates(getActivity());
    }

    public void notifyRemovedStatus(int idOfChangedPerson) {

//        Intent notificationIntent = new Intent(Intent.ACTION_VIEW);
//        PendingIntent contentIntent = PendingIntent.getActivity(getActivity(), 0, notificationIntent, 0);
//        final String GROUP_KEY_STATUS = "group_key_status";
        numberOfAllerts++;
        ids += idOfChangedPerson + "; ";
        longRemovedMsg = removedMsg + "[" + numberOfAllerts + "] - " + ids;
        Notification notification = new NotificationCompat.Builder(getActivity())
                .setCategory(Notification.CATEGORY_PROMO)
                .setSmallIcon(R.drawable.heart)
                .setContentTitle("DatingApp")
                .setContentText(longRemovedMsg)
                .setAutoCancel(true)
//                .setContentIntent(contentIntent)
                .setPriority(Notification.PRIORITY_HIGH)
                .setVibrate(new long[]{100})
//                .setGroup(GROUP_KEY_STATUS)
                .build();

        NotificationManager notificationManager =
                (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(MainActivity.NOTIFY_ID, notification);
    }

    public void showToastInIntentService(final String sText) {
        final Context MyContext = getActivity();
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast toast1 = Toast.makeText(MyContext, sText, Toast.LENGTH_SHORT);
                toast1.show();
            }
        });
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
        }, 500);
    }

    private void deleteCell(final View v, final int index) {
        Animation.AnimationListener al = new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                persons.remove(index);

                ViewHolder vh = (ViewHolder) v.getTag();
                vh.needInflate = true;

                mMyAnimListAdapter.notifyDataSetChanged();

                closeFragmentIfPersonsListIsEmpty();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }
        };
        collapse(v, al);
    }

    private void closeFragmentIfPersonsListIsEmpty() {
        if (persons.size() == 0) {
            getActivity().onBackPressed();
        }
    }

    private void collapse(final View v, Animation.AnimationListener al) {
        final int initialHeight = v.getMeasuredHeight();

        Animation anim = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        if (al != null) {
            anim.setAnimationListener(al);
        }
        anim.setDuration(ANIMATION_DURATION);
        v.startAnimation(anim);
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
                    ModelPerson currentPersonn = persons.get(position);
                    MainActivity.removeIdOfLikedPerson(currentPersonn.getId());
                    holderr.heart.setVisibility(View.GONE);
//                    deleteCell(finalView, position);
                    removeListItem(finalView, position);
                }
            });
            holder.btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewHolder holderr = (ViewHolder) finalView.getTag();
                    ModelPerson currentPersonn = persons.get(position);
                    MainActivity.addIdOfLikedPerson(currentPersonn.getId());
//                        Intent intent = new Intent(getActivity(), MatchActivity.class);
//                        intent.putExtra("url", currentPerson.getPhoto());
                    holderr.heart.setVisibility(View.VISIBLE);
//                    deleteCell(finalView, position);
                    removeListItem(finalView, position);
//                        startActivity(intent);
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
