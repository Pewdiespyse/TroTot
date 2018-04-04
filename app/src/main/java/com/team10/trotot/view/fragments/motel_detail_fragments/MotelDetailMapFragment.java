package com.team10.trotot.view.fragments.motel_detail_fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team10.trotot.R;
import com.team10.trotot.model.basic_classes.Motel;
import com.team10.trotot.view.activities.MotelDetailActivity;

import java.util.Iterator;

import static com.team10.trotot.model.FIREBASE_STRING.FIRE_BASE_STRING_MOTELS;

/**
 * Created by vinhkhang on 17/11/2017.
 */

public class MotelDetailMapFragment extends Fragment implements OnMapReadyCallback {

    private String motelID = MotelDetailActivity.motelID;
    private double longitude = -1;
    private double latitude = -1;
    private GoogleMap myMap;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_motel_detail_map, container, false);

        Button btnOpenMapApp = view.findViewById(R.id.btn_motel_detail_map);
        btnOpenMapApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strUri = "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude + " (" + "TroTot" + ")";
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

                startActivity(intent);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fm_motel_detail_map);
        mapFragment.getMapAsync(this);

        FirebaseDatabase.getInstance().getReference().child(FIRE_BASE_STRING_MOTELS).orderByKey().equalTo(motelID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChildren()) {
                    Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                    Motel motel = iterator.next().getValue(Motel.class);

                    latitude = motel.getMapLatitude();
                    longitude = motel.getMapLongitude();

                    updateUI();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return view;
    }

    private void updateUI() {
        if (latitude != -1 && longitude != -1 && myMap != null) {
            myMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)));
            myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));
            myMap.animateCamera(CameraUpdateFactory.zoomIn());
            myMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;
        myMap.getUiSettings().setAllGesturesEnabled(true);
        myMap.getUiSettings().setZoomGesturesEnabled(true);
        myMap.getUiSettings().setZoomControlsEnabled(true);
        myMap.getUiSettings().setCompassEnabled(true);
        myMap.getUiSettings().setMapToolbarEnabled(true);
        myMap.getUiSettings().setRotateGesturesEnabled(true);
        myMap.getUiSettings().setIndoorLevelPickerEnabled(true);
        myMap.getUiSettings().setTiltGesturesEnabled(true);
        myMap.getUiSettings().setMyLocationButtonEnabled(true);
        myMap.getUiSettings().setScrollGesturesEnabled(true);

        if (latitude != -1 && longitude != -1) {
            updateUI();
        }
    }
}