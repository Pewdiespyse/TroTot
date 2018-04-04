package com.team10.trotot.view.fragments.notification;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.team10.trotot.R;
import com.team10.trotot.model.BUNDLE_STRING;
import com.team10.trotot.model.PREF_STRING;
import com.team10.trotot.model.basic_classes.Motel;
import com.team10.trotot.view.activities.FilterOptionsActivity;
import com.team10.trotot.view.activities.MainActivity;
import com.team10.trotot.view.activities.MotelDetailActivity;
import com.team10.trotot.view.fragments.BaseFragment;
import com.team10.trotot.view.supports.GlideApp;
import com.team10.trotot.view.supports.PrefUtil;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static com.team10.trotot.model.FIREBASE_STRING.FIRE_BASE_STRING_MOTELS;

/**
 * Created by vinhkhang on 04/12/2017.
 */

public class TabNewMotelFragment extends BaseFragment {

    private final int REQUEST_CODE = 28;
    private int MAX_MOTEL_ITEM = 20;
    private String userID = MainActivity.USER_ID;
    private MotelAdapter motelAdapter;
    private List<Motel> motelList;
    private RecyclerView motelRecyclerView;

    private boolean showNotification;
    private long notiTime;
    private LatLng latLng;
    private String mapName;
    private int radius, priceMin, priceMax, areaMin, areaMax, ratingMin, ratingMax;

    public void openFilterOptionsActivity() {
        Intent intent = new Intent(getContext(), FilterOptionsActivity.class);
        intent.putExtra(BUNDLE_STRING.FILTER_FROM_NOTIFICATION.getName(), true);
        intent.putExtra(BUNDLE_STRING.FILTER_LOCATION_NAME.getName(), mapName);
        intent.putExtra(BUNDLE_STRING.FILTER_LATITUDE.getName(), (float) latLng.latitude);
        intent.putExtra(BUNDLE_STRING.FILTER_LONGITUDE.getName(), (float) latLng.longitude);
        intent.putExtra(BUNDLE_STRING.FILTER_RADIUS.getName(), radius);
        intent.putExtra(BUNDLE_STRING.FILTER_PRICE_MIN.getName(), priceMin);
        intent.putExtra(BUNDLE_STRING.FILTER_PRICE_MAX.getName(), priceMax);
        intent.putExtra(BUNDLE_STRING.FILTER_AREA_MIN.getName(), areaMin);
        intent.putExtra(BUNDLE_STRING.FILTER_AREA_MAX.getName(), areaMax);
        intent.putExtra(BUNDLE_STRING.FILTER_RATING_MIN.getName(), ratingMin);
        intent.putExtra(BUNDLE_STRING.FILTER_RATING_MAX.getName(), ratingMax);

        logcat("start " + ratingMin);
        logcat("start " + ratingMax);

        startActivityForResult(intent, REQUEST_CODE);
    }

    public void changeNotificationStatus(boolean isOn) {

        // turn off
        if (showNotification && !isOn) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
            alertDialog.setTitle(getString(R.string.notification_clear_all_confirm_title));
            alertDialog.setCancelable(false);
            alertDialog.setMessage(getString(R.string.notification_clear_all_confirm_message));
            alertDialog.setPositiveButton(getString(R.string.notification_clear_all_confirm_yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(getContext(), getString(R.string.notification_turn_off), Toast.LENGTH_LONG).show();
                    showNotification = false;
                    saveData();
                    motelList.clear();
                    motelAdapter.notifyDataSetChanged();
                }
            });

            alertDialog.setNegativeButton(getString(R.string.notification_clear_all_confirm_no), null);
            alertDialog.show();

            return;
        }

        // turn on
        if (!showNotification && isOn) {
            notiTime = System.currentTimeMillis();

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
            alertDialog.setTitle(getString(R.string.notification_clear_all_confirm_title));
            alertDialog.setCancelable(false);
            alertDialog.setMessage(getString(R.string.notification_clear_all_confirm_message));
            alertDialog.setPositiveButton(getString(R.string.notification_clear_all_confirm_yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(getContext(), getString(R.string.notification_turn_on), Toast.LENGTH_LONG).show();
                    showNotification = true;
                    saveData();
                    motelList.clear();
                    motelAdapter.notifyDataSetChanged();
                }
            });

            alertDialog.setNegativeButton(getString(R.string.notification_clear_all_confirm_no), null);
            alertDialog.show();

            return;
        }
    }

    private void saveData() {
        PrefUtil.set(getContext(), PREF_STRING.NOTIFICATION_STATUS, showNotification);
        PrefUtil.set(getContext(), PREF_STRING.NOTIFICATION_TIME, notiTime);
        PrefUtil.set(getContext(), PREF_STRING.NOTIFICATION_LOCATION_NAME, mapName);
        PrefUtil.set(getContext(), PREF_STRING.NOTIFICATION_LATITUDE, (float) latLng.latitude);
        PrefUtil.set(getContext(), PREF_STRING.NOTIFICATION_LONGITUDE, (float) latLng.longitude);
        PrefUtil.set(getContext(), PREF_STRING.NOTIFICATION_RADIUS, radius);
        PrefUtil.set(getContext(), PREF_STRING.NOTIFICATION_PRICE_MIN, priceMin);
        PrefUtil.set(getContext(), PREF_STRING.NOTIFICATION_PRICE_MAX, priceMax);
        PrefUtil.set(getContext(), PREF_STRING.NOTIFICATION_AREA_MIN, areaMin);
        PrefUtil.set(getContext(), PREF_STRING.NOTIFICATION_AREA_MAX, areaMax);
        PrefUtil.set(getContext(), PREF_STRING.NOTIFICATION_RATING_MIN, ratingMin);
        PrefUtil.set(getContext(), PREF_STRING.NOTIFICATION_RATING_MAX, ratingMax);
    }

    private void loadData() {
        showNotification = PrefUtil.getBoolean(getContext(), PREF_STRING.NOTIFICATION_STATUS);
        notiTime = PrefUtil.getLong(getContext(), PREF_STRING.NOTIFICATION_TIME);
        radius = PrefUtil.getInt(getContext(), PREF_STRING.NOTIFICATION_RADIUS);
        mapName = PrefUtil.getString(getContext(), PREF_STRING.NOTIFICATION_LOCATION_NAME);
        latLng = new LatLng(PrefUtil.getFloat(getContext(), PREF_STRING.NOTIFICATION_LATITUDE), PrefUtil.getFloat(getContext(), PREF_STRING.NOTIFICATION_LONGITUDE));
        priceMin = PrefUtil.getInt(getContext(), PREF_STRING.NOTIFICATION_PRICE_MIN);
        priceMax = PrefUtil.getInt(getContext(), PREF_STRING.NOTIFICATION_PRICE_MAX);
        areaMin = PrefUtil.getInt(getContext(), PREF_STRING.NOTIFICATION_AREA_MIN);
        areaMax = PrefUtil.getInt(getContext(), PREF_STRING.NOTIFICATION_AREA_MAX);
        ratingMin = PrefUtil.getInt(getContext(), PREF_STRING.NOTIFICATION_RATING_MIN);
        ratingMax = PrefUtil.getInt(getContext(), PREF_STRING.NOTIFICATION_RATING_MAX);
    }

    private void loadList() {
        // get motels
        FirebaseDatabase.getInstance().getReference().child(FIRE_BASE_STRING_MOTELS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                motelList = new ArrayList<>();

                if (!showNotification) {
                    motelList.clear();
                    motelAdapter.notifyDataSetChanged();
                    return;
                }

                if (dataSnapshot.hasChildren()) {
                    Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                    Motel motel;
                    logcat("Motel ---------------------------------- ");
                    while (iterator.hasNext()) {

                        int count = 0;
                        motel = iterator.next().getValue(Motel.class);

                        Location loc1 = new Location(LocationManager.GPS_PROVIDER);
                        Location loc2 = new Location(LocationManager.GPS_PROVIDER);

                        loc1.setLatitude(latLng.latitude);
                        loc1.setLongitude(latLng.longitude);

                        loc2.setLatitude(motel.getMapLatitude());
                        loc2.setLongitude(motel.getMapLongitude());

                        if (loc1.distanceTo(loc2) <= radius) {
                            count++;
                        }

//                        logcat("Motel " + motel.getMotelId() + " : " + loc1.distanceTo(loc2) + " ... " + radius);

                        if (priceMin <= motel.getPrice() && motel.getPrice() <= priceMax) {
                            count++;
                        }

                        if (areaMin <= motel.getArea() && motel.getArea() <= areaMax) {
                            count++;
                        }

                        if (ratingMin <= motel.getEvaluate() && motel.getEvaluate() <= ratingMax) {
                            count++;
                        }

                        if (motel.getTimePost() >= notiTime) {
                            count++;
                        }

//                        logcat("Motel " + motel.getMotelId() + " : " + motel.getTimePost() + " ... " + notiTime);
                        logcat("Motel " + motel.getMotelId() + " : " + count);
                        if (count >= 5) {
                            motelList.add(motel);
                        }
                    }
                }

//                Collections.reverse(motelList);
                Collections.sort(motelList, new Comparator<Motel>() {
                    @Override
                    public int compare(Motel t1, Motel t2) {
                        return Long.compare(t1.getTimePost(), t2.getTimePost());
                    }
                });

                while (motelList.size() > MAX_MOTEL_ITEM) {
                    motelList.remove(motelList.size() - 1);
                }

                motelAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
            alertDialog.setTitle(getString(R.string.notification_clear_all_confirm_title));
            alertDialog.setCancelable(false);
            alertDialog.setMessage(getString(R.string.notification_clear_all_confirm_message));
            alertDialog.setPositiveButton(getString(R.string.notification_clear_all_confirm_yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(getContext(), getString(R.string.notification_turn_on), Toast.LENGTH_LONG).show();
                    showNotification = true;

                    notiTime = System.currentTimeMillis();
                    mapName = data.getStringExtra(BUNDLE_STRING.FILTER_LOCATION_NAME.getName());
                    latLng = new LatLng(data.getFloatExtra(BUNDLE_STRING.FILTER_LATITUDE.getName(), -1), data.getFloatExtra(BUNDLE_STRING.FILTER_LONGITUDE.getName(), -1));
                    radius = data.getIntExtra(BUNDLE_STRING.FILTER_RADIUS.getName(), 100);
                    priceMin = data.getIntExtra(BUNDLE_STRING.FILTER_PRICE_MIN.getName(), 100000);
                    priceMax = data.getIntExtra(BUNDLE_STRING.FILTER_PRICE_MAX.getName(), 10000000);
                    areaMin = data.getIntExtra(BUNDLE_STRING.FILTER_AREA_MIN.getName(), 1);
                    areaMax = data.getIntExtra(BUNDLE_STRING.FILTER_AREA_MAX.getName(), 100);
                    ratingMin = data.getIntExtra(BUNDLE_STRING.FILTER_RATING_MIN.getName(), 1);
                    ratingMax = data.getIntExtra(BUNDLE_STRING.FILTER_RATING_MAX.getName(), 5);
                    logcat("onResult radius " + radius);
                    logcat("onResult name   " + mapName);
                    logcat("onResult time   " + notiTime);
                    logcat("onResult latlng " + latLng);
                    logcat("onResult price  " + priceMin + " " + priceMax);
                    logcat("onResult area   " + areaMin + " " + areaMax);
                    logcat("onResult rating " + ratingMin + " " + ratingMax);

                    saveData();
                    loadList();
                }
            });

            alertDialog.setNegativeButton(getString(R.string.notification_clear_all_confirm_no), null);
            alertDialog.show();
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification_motel, container, false);

        motelList = new ArrayList<>();

        motelRecyclerView = view.findViewById(R.id.rv_notification_motel);

        motelRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        motelRecyclerView.setItemAnimator(new DefaultItemAnimator());

        motelAdapter = new MotelAdapter();
        motelRecyclerView.setAdapter(motelAdapter);

        loadData();
        loadList();

        return view;
    }

    private void logcat(String data) {
        Log.v("ahihi", data);
    }

    class MotelAdapter extends RecyclerView.Adapter<MotelAdapter.MotelViewHolder> {

        public MotelAdapter() {
        }

        @Override
        public MotelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater li = LayoutInflater.from(parent.getContext());
            View itemView = li.inflate(R.layout.item_fragment_favorite_motel, parent, false);
            return new MotelViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MotelViewHolder holder, int position) {
            Motel motel = motelList.get(position);

            if (motel != null) {
                holder.name.setText(motel.getName());
                holder.price.setText(String.valueOf(motel.getPrice()));

                Timestamp timestamp = new Timestamp(motel.getTimePost());
                Date date = new Date(timestamp.getTime());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

                holder.time.setText(simpleDateFormat.format(date));
                holder.rating.setText(String.valueOf(motel.getEvaluate()));
                holder.address.setText(motel.getAddress());
                //Load hình từ firebase dùng Glide
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("motels").child(motel.getPhotosId().get(0));
                GlideApp.with(getContext())
                        .load(storageReference)
                        .into(holder.img);

                holder.ID = motel.getMotelId();
            } else {
                holder.name.setText(getString(R.string.home_no_data));
                holder.price.setText(getString(R.string.home_no_data));
                holder.time.setText(getString(R.string.home_no_data));
                holder.rating.setText(getString(R.string.home_no_data));
                holder.address.setText(getString(R.string.home_no_data));
            }
        }

        @Override
        public int getItemCount() {
            return motelList.size();
        }

        class MotelViewHolder extends RecyclerView.ViewHolder {

            String ID;
            ImageView img;
            TextView name;
            TextView price;
            TextView time;
            TextView rating;
            TextView address;

            public MotelViewHolder(View itemView) {
                super(itemView);

                img = itemView.findViewById(R.id.img_item_favorite_motel);
                name = itemView.findViewById(R.id.tv_item_favorite_motel_name);
                price = itemView.findViewById(R.id.tv_item_favorite_motel_price);
                time = itemView.findViewById(R.id.tv_item_favorite_motel_time);
                rating = itemView.findViewById(R.id.tv_item_favorite_motel_rating);
                address = itemView.findViewById(R.id.tv_item_favorite_motel_address);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), MotelDetailActivity.class);
                        intent.putExtra("UserID", userID);
                        intent.putExtra("MotelID", ID);
                        startActivity(intent);
                    }
                });
            }
        }
    }
}

