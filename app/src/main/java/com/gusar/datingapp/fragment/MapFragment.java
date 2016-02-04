package com.gusar.datingapp.fragment;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.gusar.datingapp.Constants;
import com.gusar.datingapp.R;
import com.gusar.datingapp.imagesdownloader.ImageLoader;
import com.gusar.datingapp.model.ModelPerson;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends DatingFragment {
    private GoogleMap map;
    private SupportMapFragment fragment;
    private static List<ModelPerson> PERSONS;
    ImageLoader loader;
    ImageView photo;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_maps, container, false);
        loader = new ImageLoader(getActivity());
        photo = (ImageView) v.findViewById(R.id.photooo);

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
        PERSONS = Constants.getPersons();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (map == null) {
            map = fragment.getMap();
            List<String> parseMap = new ArrayList<String>();

            for (ModelPerson mp: PERSONS) {
                Bitmap icon = loader.getBitmap(mp.getPhoto());
                icon = icon.createScaledBitmap(icon, 100, 100, true);
                BitmapDescriptor descriptor = BitmapDescriptorFactory.fromBitmap(icon);
                map.addMarker(new MarkerOptions().position(getLatLng(mp))
                        .icon(descriptor));
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