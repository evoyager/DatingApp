package com.gusar.datingapp.fragment;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.InflateException;
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
import com.gusar.datingapp.R;
import com.gusar.datingapp.imagesdownloader.ImageLoader;
import com.gusar.datingapp.model.ModelPerson;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import static java.lang.Math.min;

public class MapFragment extends Fragment {
    private GoogleMap map;
    private SupportMapFragment fragment;
    private static List<ModelPerson> PERSONS;
    ImageLoader loader;
    ImageView photo;
    Bitmap icon;
    View rootView;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.fr_maps, container, false);
        loader = new ImageLoader(getActivity());
        PERSONS = getArguments().getParcelableArrayList("persons");

        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {
            rootView = inflater.inflate(R.layout.fr_maps, container, false);
        } catch (InflateException e) {
        }

        return rootView;
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
            final List<String> parseMap = new ArrayList<String>();

            for (final ModelPerson mp: PERSONS) {
                Executor executor = Executors.newSingleThreadExecutor();
                executor.execute(new Runnable() { public void run() {
                    icon = loader.getBitmap(mp.getPhoto());
//                    try {
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
//                    } catch (NullPointerException e){}
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
        try {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(getLatLng(PERSONS.get(0)))
                    .zoom(12)
                    .bearing(45)
                    .tilt(20)
                    .build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
            map.animateCamera(cameraUpdate);
        } catch (IndexOutOfBoundsException e) {}
    }

    private LatLng getLatLng(ModelPerson person) {
        String[] splitLocation = person.getLocation().split(",");
        Double lat = Double.parseDouble(splitLocation[0]);
        Double lon = Double.parseDouble(splitLocation[1]);
        return new LatLng(lat, lon);
    }
}