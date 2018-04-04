package com.team10.trotot.view.activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.team10.trotot.R;
import com.team10.trotot.model.BUNDLE_STRING;

import java.io.IOException;
import java.util.List;

public class FilterOptionsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final int RADIUS_MIN = 1;       // 100 m
    private final int RADIUS_MAX = 100;     // 10.000 m = 10 km
    private final int RADIUS_UNIT = 100;    // 100 m
    private final int RADIUS_STEP = 1;      // 500 m

    private final int PRICE_MIN = 1;        // 100.000
    private final int PRICE_MAX = 100;      // 10.000.000
    private final int PRICE_UNIT = 100000;  // 100.000
    private final int PRICE_STEP = 1;       // 200.000

    private final int AREA_MIN = 0;         // 10 m2
    private final int AREA_MAX = 100;       // 100 m2
    private final int AREA_UNIT = 1;        // 1 m2
    private final int AREA_STEP = 5;        // 5 m2

    private final int RATING_MIN = 0;       // 1 star
    private final int RATING_MAX = 5;       // 5 star
    private final int RATING_UNIT = 1;      // 1
    private final int RATING_STEP = 1;      // 1

    private float zoomRatio = 15;

    //    private TextView tvMapName;
    private GoogleMap myMap = null;

    private LinearLayout llMapAll;
    private EditText etLocation;
    private ImageButton btnLocation;
    //    private FrameLayout flMapTouch;
    private CrystalSeekbar sbRadius;
    private CrystalRangeSeekbar sbPrice, sbArea, sbRating;
    private TextView tvRadius, tvPriceMin, tvPriceMax, tvAreaMin, tvAreaMax, tvRatingMin, tvRatingMax;

    private boolean showMap = true;
    private String mapName = "";
    private LatLng mapLatLng = null;
    private int radius, priceMin, priceMax, areaMin, areaMax, ratingMin, ratingMax;
    private int PLACE_PICKER_REQUEST = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_options);

        Toolbar toolbar = findViewById(R.id.toolbar_filter_options);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.filter_options_title));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map_filter_options);
//        mapFragment.getMapAsync(this);

        WorkaroundMapFragment mapFragment = ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_filter_options));
        mapFragment.getMapAsync(this);
        ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_filter_options)).setListener(new WorkaroundMapFragment.OnTouchListener() {
            @Override
            public void onTouch() {
                ((ScrollView) findViewById(R.id.sv_filter_options)).requestDisallowInterceptTouchEvent(true);
            }
        });

//        tvMapName = findViewById(R.id.tv_filter_options_location);
        etLocation = findViewById(R.id.et_filter_options_location);
        btnLocation = findViewById(R.id.btn_filter_options_location);
        llMapAll = findViewById(R.id.ll_filter_options_map_all);
//        flMapTouch = findViewById(R.id.fl_filter_options_map_touch);
        tvRadius = findViewById(R.id.tv_filter_options_radius);
        sbRadius = findViewById(R.id.sb_filter_options_radius);
        tvPriceMin = findViewById(R.id.tv_filter_options_price_min);
        tvPriceMax = findViewById(R.id.tv_filter_options_price_max);
        sbPrice = findViewById(R.id.sb_filter_options_price);
        tvAreaMin = findViewById(R.id.tv_filter_options_area_min);
        tvAreaMax = findViewById(R.id.tv_filter_options_area_max);
        sbArea = findViewById(R.id.sb_filter_options_area);
        tvRatingMin = findViewById(R.id.tv_filter_options_rating_min);
        tvRatingMax = findViewById(R.id.tv_filter_options_rating_max);
        sbRating = findViewById(R.id.sb_filter_options_rating);

        // TODO pass from another event
        showMap = getIntent().getBooleanExtra(BUNDLE_STRING.FILTER_SHOW_MAP.getName(), true);
        mapName = getIntent().getStringExtra(BUNDLE_STRING.FILTER_LOCATION_NAME.getName());
        mapLatLng = new LatLng(getIntent().getFloatExtra(BUNDLE_STRING.FILTER_LATITUDE.getName(), 106.6738673f), getIntent().getFloatExtra(BUNDLE_STRING.FILTER_LONGITUDE.getName(), 10.7575444f));
        radius = getIntent().getIntExtra(BUNDLE_STRING.FILTER_RADIUS.getName(), RADIUS_MIN);
        priceMin = getIntent().getIntExtra(BUNDLE_STRING.FILTER_PRICE_MIN.getName(), PRICE_MIN);
        priceMax = getIntent().getIntExtra(BUNDLE_STRING.FILTER_PRICE_MAX.getName(), PRICE_MAX);
        areaMin = getIntent().getIntExtra(BUNDLE_STRING.FILTER_AREA_MIN.getName(), AREA_MIN);
        areaMax = getIntent().getIntExtra(BUNDLE_STRING.FILTER_AREA_MAX.getName(), AREA_MAX);
        ratingMin = getIntent().getIntExtra(BUNDLE_STRING.FILTER_RATING_MIN.getName(), RATING_MIN);
        ratingMax = getIntent().getIntExtra(BUNDLE_STRING.FILTER_RATING_MAX.getName(), RATING_MAX);

        logcat("create " + ratingMin);
        logcat("create " + ratingMax);

        if (!showMap) {
            llMapAll.setVisibility(View.GONE);
        }

//        flMapTouch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//
//                    if (mapLatLng != null) {
////                        double dis = 100;
////                    LatLng southwest = SphericalUtil.computeOffset(mapLatLng, dis * Math.sqrt(2.0), 225);
////                    LatLng northeast = SphericalUtil.computeOffset(mapLatLng, dis * Math.sqrt(2.0), 45);
////                    LatLngBounds latLngBounds = new LatLngBounds(southwest, northeast);
//                        LatLngBounds latLngBounds = new LatLngBounds(mapLatLng, mapLatLng);
//                        builder.setLatLngBounds(latLngBounds);
//                    }
//
//                    startActivityForResult(builder.build(FilterOptionsActivity.this), PLACE_PICKER_REQUEST);
//                } catch (GooglePlayServicesNotAvailableException e) {
//                    e.printStackTrace();
//                } catch (GooglePlayServicesRepairableException e) {
//                    e.printStackTrace();
//                }
//            }
//        });


        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (myMap == null) {
                    Toast.makeText(FilterOptionsActivity.this, "..........", Toast.LENGTH_LONG).show();
                    return;
                }

                String textSearch = etLocation.getText().toString();
                if (textSearch.matches("")) {
                    textSearch = "Ho Chi Minh";
                }

                Geocoder gc = new Geocoder(FilterOptionsActivity.this);
                try {
                    List<Address> list = gc.getFromLocationName(textSearch, 1);
                    Address address = list.get(0);
                    double lat = address.getLatitude();
                    double lng = address.getLongitude();
                    LatLng searchLatLng = new LatLng(lat, lng);

                    mapName = address.getFeatureName();
                    mapLatLng = searchLatLng;

                    myMap.clear();
                    zoomRatio = myMap.getCameraPosition().zoom;

                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(searchLatLng, zoomRatio);
                    myMap.moveCamera(update);
                    MarkerOptions option = new MarkerOptions();
                    option.position(searchLatLng);
                    myMap.addMarker(option).showInfoWindow();
                } catch (IOException e) {
                    System.out.println("Got IOException");
                }
            }
        });

        sbRadius.setMinValue(RADIUS_MIN);
        sbRadius.setMaxValue(RADIUS_MAX);
        sbRadius.setSteps(RADIUS_STEP);
        sbRadius.setMinStartValue(radius / RADIUS_UNIT).apply();
        sbRadius.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                radius = value.intValue() * RADIUS_UNIT;
                String text = radius + " " + getString(R.string.filter_options_radius_unit);
                tvRadius.setText(text);
            }
        });

        sbPrice.setMinValue(PRICE_MIN);
        sbPrice.setMaxValue(PRICE_MAX);
        sbPrice.setSteps(PRICE_STEP);
        sbPrice.setGap(10 * PRICE_STEP);
        sbPrice.setMinStartValue(priceMin / PRICE_UNIT);
        sbPrice.setMaxStartValue(priceMax / PRICE_UNIT).apply();
        sbPrice.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                priceMin = minValue.intValue() * PRICE_UNIT;
                priceMax = maxValue.intValue() * PRICE_UNIT;

                String priceMinText = priceMin + " " + getString(R.string.filter_options_price_unit);
                String priceMaxText = priceMax + " " + getString(R.string.filter_options_price_unit);

                tvPriceMin.setText(priceMinText);
                tvPriceMax.setText(priceMaxText);
            }
        });

        sbArea.setMinValue(AREA_MIN);
        sbArea.setMaxValue(AREA_MAX);
        sbArea.setSteps(AREA_STEP);
        sbArea.setGap(4 * AREA_STEP);
        sbArea.setMinStartValue(areaMin / AREA_UNIT);
        sbArea.setMaxStartValue(areaMax / AREA_UNIT).apply();
        sbArea.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                areaMin = minValue.intValue() * AREA_UNIT;
                areaMax = maxValue.intValue() * AREA_UNIT;

                String areaMinText = areaMin + " " + getString(R.string.filter_options_area_unit);
                String areaMaxText = areaMax + " " + getString(R.string.filter_options_area_unit);

                tvAreaMin.setText(areaMinText);
                tvAreaMax.setText(areaMaxText);
            }
        });

        sbRating.setMinValue(RATING_MIN);
        sbRating.setMaxValue(RATING_MAX);
        sbRating.setSteps(RATING_STEP);
//        sbRating.setGap(3 * RATING_STEP);
        sbRating.setMinStartValue(ratingMin / RATING_UNIT);
        sbRating.setMaxStartValue(ratingMax / RATING_UNIT).apply();
        sbRating.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                ratingMin = minValue.intValue() * RATING_UNIT;
                ratingMax = maxValue.intValue() * RATING_UNIT;

                String ratingMinText = ratingMin + " " + getString(R.string.filter_options_rating_unit);
                String ratingMaxText = ratingMax + " " + getString(R.string.filter_options_rating_unit);

                tvRatingMin.setText(ratingMinText);
                tvRatingMax.setText(ratingMaxText);
            }
        });

        if (myMap != null) {
//            tvMapName.setText(mapName);
            myMap.clear();
            myMap.addMarker(new MarkerOptions().position(mapLatLng).title(" " + mapName + " "));
            myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapLatLng, 15));
            myMap.animateCamera(CameraUpdateFactory.zoomIn());
            myMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        }
    }

    private void logcat(String data) {
        Log.v("ahihi", data);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {
            Place place = PlacePicker.getPlace(this, data);
            Toast.makeText(this, place.getName() + " : " + place.getAddress(), Toast.LENGTH_LONG).show();

            mapLatLng = place.getLatLng();
            mapName = place.getName().toString();

            if (myMap != null) {
//                tvMapName.setText(mapName);
                etLocation.setText(mapName);
                myMap.clear();
                myMap.addMarker(new MarkerOptions().position(mapLatLng).title(" " + mapName + " "));
                myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapLatLng, 15));
                myMap.animateCamera(CameraUpdateFactory.zoomIn());
                myMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filter_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (R.id.menu_filter_options_apply == id) {
            // TODO return data back, implement onActivityResult to catch data
            // look example at NotificationFragment.java
            Intent intent = getIntent();
            try {
                intent.putExtra(BUNDLE_STRING.FILTER_LOCATION_NAME.getName(), String.valueOf(mapName));
                intent.putExtra(BUNDLE_STRING.FILTER_LATITUDE.getName(), (float) mapLatLng.latitude);
                intent.putExtra(BUNDLE_STRING.FILTER_LONGITUDE.getName(), (float) mapLatLng.longitude);
                intent.putExtra(BUNDLE_STRING.FILTER_RADIUS.getName(), radius);
                intent.putExtra(BUNDLE_STRING.FILTER_PRICE_MIN.getName(), priceMin);
                intent.putExtra(BUNDLE_STRING.FILTER_PRICE_MAX.getName(), priceMax);
                intent.putExtra(BUNDLE_STRING.FILTER_AREA_MIN.getName(), areaMin);
                intent.putExtra(BUNDLE_STRING.FILTER_AREA_MAX.getName(), areaMax);
                intent.putExtra(BUNDLE_STRING.FILTER_RATING_MIN.getName(), ratingMin);
                intent.putExtra(BUNDLE_STRING.FILTER_RATING_MAX.getName(), ratingMax);
            } catch (NullPointerException e) {
                finish();
                Toast.makeText(getApplicationContext(), "...", Toast.LENGTH_SHORT).show();
                return false;
            }
            setResult(RESULT_OK, intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;

        if (mapLatLng != null && mapName != null) {
//            tvMapName.setText(mapName);
            etLocation.setText(mapName);
            myMap.addMarker(new MarkerOptions().position(mapLatLng).title(mapName));
            myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapLatLng, 15));
            myMap.animateCamera(CameraUpdateFactory.zoomIn());
            myMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        }
    }
}

