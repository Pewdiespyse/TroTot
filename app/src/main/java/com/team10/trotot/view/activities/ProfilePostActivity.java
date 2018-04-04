package com.team10.trotot.view.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.team10.trotot.R;
import com.team10.trotot.model.basic_classes.Motel;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProfilePostActivity extends AppCompatActivity implements OnMapReadyCallback {
    //Input
    private ImageView[] image = {null, null, null, null, null, null};
    private Bitmap[] bitmap = {null, null, null, null, null, null};
    private Boolean[] isUploadImage = {false, false, false, false, false, false};
    private EditText edMotelTitle;
    private EditText edMotelPrice;
    private EditText edMotelArea;
    private EditText edMotelPhone;
    private EditText edMotelTimeOpen;
    private EditText edMotelTimeClose;
    private Double lat = 10.762677d;
    private Double lng = 106.682569d;
    private EditText edMotelAddress;
    private EditText edMotelContent;
    private ImageButton imageButtonSetAddressMarker;
    //Output
    Motel motel;

    private Button selectImageButton;
    private int imageViewCurrent = 0;
    private static final String TAG = MapsCurrentPlaceActivity.class.getSimpleName();
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;

    // The entry points to the Places API.
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    // Used for selecting the current place.
    private static final int M_MAX_ENTRIES = 5;
    private String[] mLikelyPlaceNames;
    private String[] mLikelyPlaceAddresses;
    private String[] mLikelyPlaceAttributions;
    private LatLng[] mLikelyPlaceLatLngs;
    float zoom_ratio = 15;

    View.OnClickListener EventUploadImage = null;
    // number of images to select
    private static final int PICK_IMAGE = 100;
    Toolbar toolbar;

    String typeInput;

    private ScrollView mScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        setContentView(R.layout.activity_profile_post);
        addControls();
        addEvents();

        //Xử lý tách biệt edit và post
        typeInput = getIntent().getStringExtra("TypeInput");
        Toast.makeText(ProfilePostActivity.this, typeInput, Toast.LENGTH_SHORT).show();
        switch (typeInput) {
            case "Post":
                motel = null;
                break;

            case "Edit":
                motel = new Motel(getIntent().getStringExtra("motelId"), getIntent().getStringExtra("userId"), getIntent().getStringExtra("name"), Long.parseLong(getIntent().getStringExtra("price")), Double.parseDouble(getIntent().getStringExtra("area")), Double.parseDouble(getIntent().getStringExtra("evaluate")), Long.parseLong(getIntent().getStringExtra("timePost")), Long.parseLong(getIntent().getStringExtra("timeOpen")), Long.parseLong(getIntent().getStringExtra("timeClose")), getIntent().getStringExtra("phone"), getIntent().getStringExtra("status"), Double.parseDouble("longtitude"), Double.parseDouble(getIntent().getStringExtra("latitude")), getIntent().getStringExtra("address"), getIntent().getStringExtra("content"), getIntent().getStringArrayListExtra("photosId"));
                break;
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);
        mScrollView = (ScrollView) findViewById(R.id.sv_container);
        WorkaroundMapFragment mWorkMap = ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.post_map));
        mWorkMap.getMapAsync(this);
        ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.post_map)).setListener(new WorkaroundMapFragment.OnTouchListener() {
            @Override
            public void onTouch() {
                mScrollView.requestDisallowInterceptTouchEvent(true);
            }
        });
    }

    private void addEvents() {
        EventUploadImage = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImageFromGallery();
                switch (view.getId()) {
                    case R.id.imageView_post_image_1:
                        imageViewCurrent = 1;
                        break;
                    case R.id.imageView_post_image_2:
                        imageViewCurrent = 2;
                        break;
                    case R.id.imageView_post_image_3:
                        imageViewCurrent = 3;
                        break;
                    case R.id.imageView_post_image_4:
                        imageViewCurrent = 4;
                        break;
                    case R.id.imageView_post_image_5:
                        imageViewCurrent = 5;
                        break;
                    case R.id.imageView_post_image_6:
                        imageViewCurrent = 6;
                        break;
                }
            }
        };
        for (int i = 0; i < 6; i++) {
            image[i].setOnClickListener(EventUploadImage);
        }

        imageButtonSetAddressMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //edMotelAddress
                String text_search = edMotelAddress.getText().toString();
                Toast.makeText(ProfilePostActivity.this, "Set address marker", Toast.LENGTH_SHORT).show();
                if (text_search.matches("") || text_search == null) {
                    text_search = "Ho Chi Minh";
                }

                Geocoder gc = new Geocoder(ProfilePostActivity.this);
                try {
                    List<Address> list = gc.getFromLocationName(text_search, 1);
                    Address address = list.get(0);
                    double lat = address.getLatitude();
                    double lng = address.getLongitude();
                    LatLng zoom_ll = new LatLng(lat, lng);
                    mMap.clear();
                    zoom_ratio = mMap.getCameraPosition().zoom;
                    AddMarker(mMap, lat, lng, zoom_ratio, "Bạn ở đây!");
                } catch (IOException e) {
                    System.out.println("Got IOException");
                }

            }
        });
    }

    private void addControls() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_profile_post);
        image[0] = (ImageView) findViewById(R.id.imageView_post_image_1);
        image[1] = (ImageView) findViewById(R.id.imageView_post_image_2);
        image[2] = (ImageView) findViewById(R.id.imageView_post_image_3);
        image[3] = (ImageView) findViewById(R.id.imageView_post_image_4);
        image[4] = (ImageView) findViewById(R.id.imageView_post_image_5);
        image[5] = (ImageView) findViewById(R.id.imageView_post_image_6);

        edMotelTitle = findViewById(R.id.editText_post_title);
        edMotelPrice = findViewById(R.id.editText_post_price);
        edMotelArea = findViewById(R.id.editText_post_area);
        edMotelPhone = findViewById(R.id.editText_post_phone);
        edMotelTimeOpen = findViewById(R.id.editText_post_time_open);
        edMotelTimeClose = findViewById(R.id.editText_post_time_close);
        edMotelAddress = findViewById(R.id.editText_post_address);
        edMotelContent = findViewById(R.id.editText_post_content);

        imageButtonSetAddressMarker = findViewById(R.id.imgBtnSetAddressMarker);

        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Build the map.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.post_map);
//        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile_post, menu);
        //showCurrentPlace();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(R.id.menu_profile_post_send == id)
        {
            postMotelOnFirebase();
            Toast.makeText(this, "Đã đăng phòng trọ " + motel.getName() + "!", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
    /**
     * Opens dialog picker, so the user can select image from the gallery. The
     * result is returned in the method <code>onActivityResult()</code>
     */
    public void selectImageFromGallery() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    /**
     * Retrives the result returned from selecting image, by invoking the method
     * <code>selectImageFromGallery()</code>
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        switch (imageViewCurrent) {
                            case 1:
                                bitmap[0] = decodeUri(selectedImage);
                                image[0].setImageBitmap(bitmap[0]);
                                isUploadImage[0] = true;
                                break;
                            case 2:
                                bitmap[1] = decodeUri(selectedImage);
                                image[1].setImageBitmap(bitmap[1]);
                                isUploadImage[1] = true;
                                break;
                            case 3:
                                bitmap[2] = decodeUri(selectedImage);
                                image[2].setImageBitmap(bitmap[2]);
                                isUploadImage[2] = true;
                                break;
                            case 4:
                                bitmap[3] = decodeUri(selectedImage);
                                image[3].setImageBitmap(bitmap[3]);
                                isUploadImage[3] = true;
                                break;
                            case 5:
                                bitmap[4] = decodeUri(selectedImage);
                                image[4].setImageBitmap(bitmap[4]);
                                isUploadImage[4] = true;
                                break;
                            case 6:
                                bitmap[5] = decodeUri(selectedImage);
                                image[5].setImageBitmap(bitmap[5]);
                                isUploadImage[5] = true;
                                break;
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }

    }

    private void postMotelOnFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("motels");
        List<String> photosId = new ArrayList<>();
        String randomMotelId = databaseReference.push().getKey();
        //Nhận biết có up hình lên imageview chưa để còn up lên firebase
        for (int i = 0; i < 6; i++) {
            if (isUploadImage[i])
                photosId.add(randomMotelId + "_" + i + ".png");
        }
        switch (typeInput) {
            case "Edit":
                //Xóa hình cũ đi lấy hình mới nếu upload hình
                for (int i = 0; i < 6; i++) {
                    if (isUploadImage[i]) {
                        FirebaseStorage.getInstance().getReference().child("motels").child(motel.getMotelId()).child(i + ".png").delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });
                    }
                }
                motel = new Motel(getIntent().getStringExtra("motelId"), ProfileAuthenticationActivity.PROFILE.getUserId(), edMotelTitle.getText().toString(), Long.parseLong(edMotelPrice.getText().toString()), Double.parseDouble(edMotelArea.getText().toString()), 0, System.currentTimeMillis(), Long.parseLong(edMotelTimeOpen.getText().toString()), Long.parseLong(edMotelTimeClose.getText().toString()), edMotelPhone.getText().toString(), "Normal", lng, lat, edMotelAddress.getText().toString(), edMotelContent.getText().toString(), photosId);
                break;
            case "Post":
                motel = new Motel(randomMotelId, "3uuEChU5waTjSlPzDyX8hH7Atol1", edMotelTitle.getText().toString(), Long.parseLong(edMotelPrice.getText().toString()), Double.parseDouble(edMotelArea.getText().toString()), 0, System.currentTimeMillis(), Long.parseLong(edMotelTimeOpen.getText().toString()), Long.parseLong(edMotelTimeClose.getText().toString()), edMotelPhone.getText().toString(), "Normal", lng, lat, edMotelAddress.getText().toString(), edMotelContent.getText().toString(), photosId);
                break;
        }
        for (int i = 0; i < 6; i++) {
            if (isUploadImage[i]) {
                upBitMapOnStorage(bitmap[i], motel.getMotelId() + "_" + i + ".png"); //id_0.png for example
            }
        }
        databaseReference.child(motel.getMotelId()).setValue(motel);
        Intent intent = new Intent(ProfilePostActivity.this, MainActivity.class);
        startActivity(intent);
    }

    //Up lên storage
    private void upBitMapOnStorage(Bitmap bitmap, String imgName) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();
        //push hình bằng byte data
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("motels").child(imgName);
        storageReference.putBytes(data);
    }

    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);
        // The new size we want to scale to
        final int REQUIRED_SIZE = 960;
        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE
                    || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }
        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //get coordinate here
                //latLng.longitude
                //latLng.latitude
                lat = latLng.latitude;
                lng = latLng.latitude;
                mMap.clear();
                zoom_ratio = mMap.getCameraPosition().zoom;
                AddMarker(mMap, latLng.latitude, latLng.longitude, zoom_ratio, "Bạn ở đây!");
            }
        });

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

    }
    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    //funtion add a marker with title to google map
    public  void AddMarker(GoogleMap map, double lat, double lng, float z, String title){
        LatLng ll = new LatLng(lat,lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll,z);
        map.moveCamera(update);
        MarkerOptions option=new MarkerOptions();
        option.title(title);
        //  option.snippet("You are here.");
        option.position(ll);
        Marker currentMarker= map.addMarker(option);
        currentMarker.showInfoWindow();
    }


    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getLocationPermission() {
    /*
     * Request location permission, so that we can get the location of the
     * device. The result of the permission request is handled by a callback,
     * onRequestPermissionsResult.
     */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }
    /**
     * Prompts the user to select the current place from a list of likely places, and shows the
     * current place on the map - provided the user has granted location permission.
     */
    private void showCurrentPlace() {
        if (mMap == null) {
            return;
        }

        if (mLocationPermissionGranted) {
            // Get the likely places - that is, the businesses and other points of interest that
            // are the best match for the device's current location.
            @SuppressWarnings("MissingPermission") final
            Task<PlaceLikelihoodBufferResponse> placeResult =
                    mPlaceDetectionClient.getCurrentPlace(null);
            placeResult.addOnCompleteListener
                    (new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
                        @Override
                        public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();

                                // Set the count, handling cases where less than 5 entries are returned.
                                int count;
                                if (likelyPlaces.getCount() < M_MAX_ENTRIES) {
                                    count = likelyPlaces.getCount();
                                } else {
                                    count = M_MAX_ENTRIES;
                                }

                                int i = 0;
                                mLikelyPlaceNames = new String[count];
                                mLikelyPlaceAddresses = new String[count];
                                mLikelyPlaceAttributions = new String[count];
                                mLikelyPlaceLatLngs = new LatLng[count];

                                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                                    // Build a list of likely places to show the user.
                                    mLikelyPlaceNames[i] = (String) placeLikelihood.getPlace().getName();
                                    mLikelyPlaceAddresses[i] = (String) placeLikelihood.getPlace()
                                            .getAddress();
                                    mLikelyPlaceAttributions[i] = (String) placeLikelihood.getPlace()
                                            .getAttributions();
                                    mLikelyPlaceLatLngs[i] = placeLikelihood.getPlace().getLatLng();

                                    i++;
                                    if (i > (count - 1)) {
                                        break;
                                    }
                                }

                                // Release the place likelihood buffer, to avoid memory leaks.
                                likelyPlaces.release();

                                // Show a dialog offering the user the list of likely places, and add a
                                // marker at the selected place.
                                openPlacesDialog();

                            } else {
                                Log.e(TAG, "Exception: %s", task.getException());
                            }
                        }
                    });
        } else {
            // The user has not granted permission.
            Log.i(TAG, "The user did not grant location permission.");

            // Add a default marker, because the user hasn't selected a place.
            mMap.addMarker(new MarkerOptions()
                    .title("tt")
                    .position(mDefaultLocation)
                    .snippet("snn"));

            // Prompt the user for permission.
            getLocationPermission();
        }
    }
    /**
     * Displays a form allowing the user to select a place from a list of likely places.
     */
    private void openPlacesDialog() {
        // Ask the user to choose the place where they are now.
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // The "which" argument contains the position of the selected item.
                LatLng markerLatLng = mLikelyPlaceLatLngs[which];
                String markerSnippet = mLikelyPlaceAddresses[which];
                if (mLikelyPlaceAttributions[which] != null) {
                    markerSnippet = markerSnippet + "\n" + mLikelyPlaceAttributions[which];
                }

                // Add a marker for the selected place, with an info window
                // showing information about that place.
                mMap.addMarker(new MarkerOptions()
                        .title(mLikelyPlaceNames[which])
                        .position(markerLatLng)
                        .snippet(markerSnippet));

                // Position the map's camera at the location of the marker.
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,
                        DEFAULT_ZOOM));
            }
        };

        // Display the dialog.
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("dasd")
                .setItems(mLikelyPlaceNames, listener)
                .show();
    }
}
