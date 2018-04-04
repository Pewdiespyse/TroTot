package com.team10.trotot.view.fragments.find;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.team10.trotot.R;
import com.team10.trotot.model.basic_classes.Motel;
import com.team10.trotot.view.activities.MotelDetailActivity;
import com.team10.trotot.view.fragments.BaseFragment;
import com.team10.trotot.view.supports.GlideApp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vinhkhang on 24/12/2017.
 */

public class TabMapFragment extends BaseFragment implements OnMapReadyCallback {
    private final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private Location myLocation = null;
    private String textSearch = "";
    private List<Motel> motelFound;
    private GoogleMap map;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_map, container, false);

        motelFound = new ArrayList<>();

        textSearch = getArguments().getString("valueTextSearch");

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setAllGesturesEnabled(true);
        map.getUiSettings().setZoomGesturesEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(true);
        map.getUiSettings().setRotateGesturesEnabled(true);
        map.getUiSettings().setIndoorLevelPickerEnabled(true);
        map.getUiSettings().setTiltGesturesEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setScrollGesturesEnabled(true);
        try {
            map.setMyLocationEnabled(true);
        } catch (SecurityException e) {
            System.out.print("Got securityException");
        }

        // search nearest motel to zoom
        querySearch(textSearch);

        updateMap();
    }

    private void zoom(GoogleMap myMap, LatLng ll, float zoom) {
        ((FindFragment) getParentFragment()).setLocation(ll);
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, zoom));
    }

    private void updateMap() {

        if (map == null) {
            logcat("map null");
            return;
        }

        // clear all marker
        map.clear();

        // set marker
        for (int i = 0; i < motelFound.size(); i++) {

            final Motel motel = motelFound.get(i);

            LatLng motel_ll = new LatLng(motel.getMapLatitude(), motel.getMapLongitude());

            map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    View v = getLayoutInflater().inflate(R.layout.marker_layout, null);

                    TextView tvName = v.findViewById(R.id.tv_item_favorite_motel_name);
                    TextView tvPrice = v.findViewById(R.id.tv_item_favorite_motel_price);
                    TextView tvRate = v.findViewById(R.id.tv_item_favorite_motel_rating);
                    ImageView img = v.findViewById(R.id.img_item_favorite_motel);

//                    for (Motel motel : motelFound) {
//                        if ((motel.getName()).equals(marker.getTitle())) {
                    tvName.setText(motel.getName());
                    tvPrice.setText(String.valueOf(motel.getPrice()));
                    tvRate.setText(String.valueOf(motel.getEvaluate()));
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("motels").child(motel.getPhotosId().get(0));
                    GlideApp.with(getContext()).load(storageReference).into(img);
//                        }
//                    }

                    return v;
                }
            });


            Marker m = map.addMarker(new MarkerOptions()
                    .title(motel.getName())
                    .snippet(motel.getAddress())
                    .position(motel_ll));
            m.setTag(motel.getMotelId());
            m.showInfoWindow();
        }

        // add event
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String motel_id = marker.getTag().toString();
                Intent intent = new Intent(getContext(), MotelDetailActivity.class);
                intent.putExtra("MotelID", motel_id);
                startActivity(intent);
            }
        });
    }

    private void logcat(String data) {
        Log.v("ahihi", data);
    }

    public void found(List<Motel> motels) {
        if (motels == null) {
            motelFound.clear();
        } else {
            motelFound.clear();
            motelFound.addAll(motels);
        }

        logcat("size " + motelFound.size());

        updateMap();
    }

    // zoom map to search location
    public void querySearch(String textSearch) {

        if (textSearch == null || textSearch.equals("")) {
            zoomMyLocation();
            return;
        }

        Geocoder gc = new Geocoder(getActivity());
        try {
            List<Address> list = gc.getFromLocationName(textSearch, 1);
            Address address = list.get(0);
            double lat = address.getLatitude();
            double lng = address.getLongitude();
            LatLng zoom_ll = new LatLng(lat, lng);
            zoom(map, zoom_ll, 13);

            // pass to parent then pass to child
            ((FindFragment) getParentFragment()).setLocation(zoom_ll);

        } catch (IOException e) {
            System.out.println("Got IOException");
        }
    }

    // zoom map to current location
    private void zoomMyLocation() {
        FusedLocationProviderClient fusedLocationProviderClient;
        if (ContextCompat.checkSelfPermission(this.getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(getContext(), "need 1 permission", Toast.LENGTH_SHORT).show();

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {
                ActivityCompat.requestPermissions(this.getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getActivity());
                try {
                    fusedLocationProviderClient.getLastLocation()
                            .addOnSuccessListener(this.getActivity(), new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    myLocation = location;
                                    if (myLocation != null) {
                                        zoom(map, new LatLng(location.getLatitude(), location.getLongitude()), 13);
                                    }
                                }
                            });
                } catch (SecurityException e) {
                    System.out.print("Got securityException.");
                }
            }
        } else {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getActivity());
            try {
                fusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(this.getActivity(), new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                myLocation = location;

                                if (myLocation != null) {
                                    zoom(map, new LatLng(location.getLatitude(), location.getLongitude()), 13);
                                }
                            }
                        });
            } catch (SecurityException e) {
                System.out.print("Got securityException.");
            }
        }
    }
}
