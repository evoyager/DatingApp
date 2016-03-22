package com.gusar.datingapp.fragment;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.gusar.datingapp.MainActivity;
import com.gusar.datingapp.R;
import com.gusar.datingapp.model.ModelPerson;
import com.gusar.datingapp.view.SlidingTabLayout;

import org.testpackage.test_sdk.android.testlib.API;
import org.testpackage.test_sdk.android.testlib.services.UpdateService;

import java.util.ArrayList;

/**
 * Created by igusar on 2/12/16.
 */
public class ViewPagerFragment extends Fragment {

    MyAdapter adapter;
    ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLayout;
    private static ArrayList<ModelPerson> persons = new ArrayList<ModelPerson>();
    String ids = "", longRemovedMsg = "";
    int numberOfAllerts = 0;
    String removedMsg = "Removed persons: ";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            persons = bundle.getParcelableArrayList("persons");
        }

        return inflater.inflate(R.layout.fr_viewpager, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        adapter = new MyAdapter(getChildFragmentManager());
        mViewPager.setAdapter(adapter);

        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setViewPager(mViewPager);

        subscribeUpdates();
    }

    private void subscribeUpdates() {

        API.INSTANCE.subscribeUpdates(new UpdateService.UpdateServiceListener() {

            @Override
            public void onChanges(final String person) {

                final ModelPerson changedPerson = new Gson().fromJson(person, ModelPerson.class);
                final int idOfChangedPerson = changedPerson.getId();

                ModelPerson oldPerson = null;
                int index = 0;
                for (final ModelPerson p : persons) {
                    if (p.getId() == idOfChangedPerson) {
                        oldPerson = p;
                        index = persons.indexOf(p);

                        persons.set(persons.indexOf(oldPerson), changedPerson);

                        final int finalIndex = index;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    MapFragment.markers.get(p.getId()).setPosition(getLatLng(changedPerson));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                if (changedPerson.getStatus().equals("removed")) {
                                    persons.remove(finalIndex);
                                    notifyRemovedStatus(idOfChangedPerson);
                                    PersonsFragment.mMyAnimListAdapter.notifyDataSetChanged();
                                        MapFragment.markers.get(p.getId()).remove();
                                        if (persons.size() == 0) {
                                            getActivity().onBackPressed();
                                        }
                                } else if ((changedPerson.getStatus().equals("like"))&&(MainActivity.personIsLikedFromButton(changedPerson.getId()))) {
                                    notifyLikedStatus();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    private LatLng getLatLng(ModelPerson person) {
        String[] splitLocation = person.getLocation().split(",");
        Double lat = Double.parseDouble(splitLocation[0]);
        Double lon = Double.parseDouble(splitLocation[1]);
        return new LatLng(lat, lon);
    }

    public void notifyRemovedStatus(int idOfChangedPerson) {

        numberOfAllerts++;
        ids += idOfChangedPerson + "; ";
        longRemovedMsg = removedMsg + "[" + numberOfAllerts + "] - " + ids;
        Notification notification = new NotificationCompat.Builder(getActivity())
                .setCategory(Notification.CATEGORY_PROMO)
                .setSmallIcon(R.drawable.heart)
                .setContentTitle("DatingApp")
                .setContentText(longRemovedMsg)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setVibrate(new long[]{100})
                .build();

        NotificationManager notificationManager =
                (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(MainActivity.NOTIFY_ID, notification);
    }

    public void notifyLikedStatus() {
        Notification notification = new NotificationCompat.Builder(getActivity())
                .setCategory(Notification.CATEGORY_PROMO)
                .setSmallIcon(R.drawable.heart)
                .setContentTitle("DatingApp")
                .setContentText("This is MATCH")
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setVibrate(new long[]{100})
                .build();

        NotificationManager notificationManager =
                (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(MainActivity.NOTIFY_ID, notification);

        Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(500);
    }

    public static class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
                super(fm);
            }

        @Override
        public Fragment getItem(int i) {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("persons", persons);
            Fragment personsFragment = new PersonsFragment();
            Fragment mapFragment = new MapFragment();
            personsFragment.setArguments(bundle);
            mapFragment.setArguments(bundle);
            switch(i) {
                case 0:
                    return personsFragment;
                case 1:
                    return mapFragment;
                }
            return null;
        }

        @Override
        public int getCount() {
                return 2;
            }

        @Override
        public CharSequence getPageTitle(int i) {
            switch(i) {
                case 0:
                    return "Persons";
                default:
                    return "Map";
            }
        }
    }
}

