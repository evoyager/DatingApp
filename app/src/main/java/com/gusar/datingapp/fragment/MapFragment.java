package com.gusar.datingapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

public class MapFragment extends Fragment {
    private GoogleMap map;
    private SupportMapFragment fragment;
//    static final LatLng HAMBURG = new LatLng(53.558, 9.927);
//    static final LatLng KIEL = new LatLng(53.551, 9.993);

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_maps, container, false);

//        map = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
//        Marker hamburg = map.addMarker(new MarkerOptions().position(HAMBURG)
//        .title("Hamburg"));
//
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(HAMBURG, 15));
//        map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
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
            map.addMarker(new MarkerOptions().position(new LatLng(0, 0)));
        }
    }
}