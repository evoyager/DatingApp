package com.gusar.datingapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.gusar.datingapp.R;

/**
 * @author evoyager
 */


public class MapFragment extends android.support.v4.app.Fragment {
    GoogleMap m_googleMap;
    StreetViewPanorama m_StreetView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fr_maps, container, false);
        createMapView();
        createStreetView();

        m_googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (m_StreetView != null) {
                    Fragment mapView = getFragmentManager().findFragmentById(R.id.mapView);
                    getFragmentManager().beginTransaction().hide(mapView).commit();

                    m_StreetView.setPosition(latLng);
                }
            }
        });

        return rootView;
    }

    private void createStreetView() {
        m_StreetView = ((SupportStreetViewPanoramaFragment)
                getFragmentManager().findFragmentById(R.id.streetView))
                .getStreetViewPanorama();
    }

    private void createMapView() {
        try {
            if(null == m_googleMap) {
                m_googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                        R.id.mapView)).getMap();

                if(null == m_googleMap) {
                    Toast.makeText(getApplicationContext(),
                            "Error creating map", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (NullPointerException exception) {
            Log.e("mapApp", exception.toString());
        }
    }

    private void addMaker() {
        if (null != m_googleMap) {
            m_googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(0, 0))
                                .title("Marker")
                                 .draggable(true)
            );
        }
    }
}
