package com.gusar.datingapp.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.gusar.datingapp.Constants;
import com.gusar.datingapp.R;
import com.gusar.datingapp.adapter.MapAdapter;
import com.gusar.datingapp.model.ModelPerson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapFragment extends DatingFragment {
    private GoogleMap map;
    private SupportMapFragment fragment;
    OnPersonsListener onPersonsListener;

    private static List<ModelPerson> PERSONS;

    public interface OnPersonsListener {
        void onPersons(ModelPerson modelPerson);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.onPersonsListener = (OnPersonsListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnPersonsListener");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_maps, container, false);
        PERSONS = Constants.getPersons();

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        FragmentManager fm = getChildFragmentManager();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, fragment).commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (map == null) {
            map = fragment.getMap();
            List<String> parseMap = new ArrayList<String>();
            for (ModelPerson mp: PERSONS) {
                map.addMarker(new MarkerOptions().position(getLatLng(mp)));
                parseMap.add(getLatLng(mp).toString());
            }
        }
        zoomCamera();
    }

    public void zoomCamera() {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(getLatLng(PERSONS.get(0)))
                .zoom(12)
                .bearing(45)
                .tilt(20)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        map.animateCamera(cameraUpdate);
    }

    private LatLng getLatLng(ModelPerson person) {
        String[] splitLocation = person.getLocation().split(",");
        Double lat = Double.parseDouble(splitLocation[0]);
        Double lon = Double.parseDouble(splitLocation[1]);
        return new LatLng(lat, lon);
    }
}