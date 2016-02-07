package com.gusar.datingapp.fragment;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import static java.lang.Math.min;

public class MapFragment extends DatingFragment {
    private GoogleMap map;
    private SupportMapFragment fragment;
    private static List<ModelPerson> PERSONS;
    ImageLoader loader;
    ImageView photo;
    Bitmap icon;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_maps, container, false);
        loader = new ImageLoader(getActivity());
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
            final List<String> parseMap = new ArrayList<String>();

            for (final ModelPerson mp: PERSONS) {
                Executor executor = Executors.newSingleThreadExecutor();
                executor.execute(new Runnable() { public void run() {
                    icon = loader.getBitmap(mp.getPhoto());
                    icon = icon.createScaledBitmap(icon, 100, 100, true);
                    icon = circleBitmap(icon);
                    final BitmapDescriptor descriptor = BitmapDescriptorFactory.fromBitmap(icon);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable(){
                                     @Override
                                     public void run() {
                                         map.addMarker(new MarkerOptions().position(getLatLng(mp))
                                                 .icon(descriptor));
                                     }
                                 });

                    parseMap.add(getLatLng(mp).toString());
                } });
            }
        }
        zoomCamera();
    }

    private Bitmap circleBitmap(Bitmap b) {
        Bitmap bitmap = b;

        Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        BitmapShader shader = new BitmapShader (bitmap,  Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(shader);
        paint.setAntiAlias(true);
        Canvas c = new Canvas(circleBitmap);
        c.drawCircle(bitmap.getWidth()/2, bitmap.getHeight()/2, min(bitmap.getWidth(), bitmap.getHeight())/2, paint);
        return circleBitmap;
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