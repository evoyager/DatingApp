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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.gusar.datingapp.R;
import com.gusar.datingapp.adapter.MapAdapter;
import com.gusar.datingapp.model.ModelPerson;

public class MapFragment extends DatingFragment {
    private GoogleMap map;
    private SupportMapFragment fragment;
    OnPersonsListener onPersonsListener;

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
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        this.recyclerView = (RecyclerView) rootView.findViewById(R.id.rvMapDating);
        this.layoutManager = new LinearLayoutManager(getActivity());
        this.recyclerView.setLayoutManager(this.layoutManager);
        this.adapter = new MapAdapter(this);
        this.recyclerView.setAdapter(this.adapter);
        return rootView;
    }
//    static final LatLng HAMBURG = new LatLng(53.558, 9.927);
//    static final LatLng KIEL = new LatLng(53.551, 9.993);

//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View v = inflater.inflate(R.layout.fr_maps, container, false);
//
////        map = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
////        Marker hamburg = map.addMarker(new MarkerOptions().position(HAMBURG)
////        .title("Hamburg"));
////
////        map.moveCamera(CameraUpdateFactory.newLatLngZoom(HAMBURG, 15));
////        map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
//
//        return v;
//    }
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
////        FragmentManager fm = getChildFragmentManager();
//        FragmentManager fm = getActivity().getSupportFragmentManager();
//        fragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
//        if (fragment == null) {
//            fragment = SupportMapFragment.newInstance();
//            fm.beginTransaction().replace(R.id.map, fragment).commit();
//        }
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (map == null) {
//            map = fragment.getMap();
//            map.addMarker(new MarkerOptions().position(new LatLng(0, 0)));
//        }
//    }
}