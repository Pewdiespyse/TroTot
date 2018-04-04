package com.team10.trotot.view.fragments.find;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team10.trotot.R;
import com.team10.trotot.model.BUNDLE_STRING;
import com.team10.trotot.model.basic_classes.Motel;
import com.team10.trotot.view.activities.FilterOptionsActivity;
import com.team10.trotot.view.activities.MainActivity;
import com.team10.trotot.view.adapters.SectionPageAdapter;
import com.team10.trotot.view.fragments.BaseFragment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.team10.trotot.model.FIREBASE_STRING.FIRE_BASE_STRING_MOTELS;

/**
 * Created by vinhkhang on 15/10/2017.
 */

public class FindFragment extends BaseFragment {
    private static final String TAG = "FavoriteFragment";
    private final int REQUEST_CODE = 1;
    private ProgressDialog progressDialog;
    private SectionPageAdapter adapter;
    private ViewPager mViewPager;
    private LatLng latLngSearch = null;
    private String textSearch = "";
    private int radius = -1, priceMin, priceMax, areaMin, areaMax, ratingMin, ratingMax;
    private List<Motel> motelListAll;

    public static FindFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt(ARGS_INSTANCE, instance);
        FindFragment fragment = new FindFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find, container, false);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_fragment_find);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setLogo(R.drawable.ic_bottom_bar_search);
        mViewPager = view.findViewById(R.id.container);
        setupViewPager(mViewPager);
        TabLayout tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
//        progressDialog.show();

        motelListAll = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child(FIRE_BASE_STRING_MOTELS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                motelListAll = new ArrayList<>();
                if (dataSnapshot.hasChildren()) {
                    Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                    Motel motel;
                    while (iterator.hasNext()) {
                        motel = iterator.next().getValue(Motel.class);
                        motelListAll.add(motel);
                    }
                }
                Log.v("ahihi", "motelListAll size " + motelListAll.size());
                searchIt();
//                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void setLocation(LatLng latLng) {
        // search
        latLngSearch = latLng;
        Log.v("ahihi ", "ssd" + latLng);
        searchIt();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            radius = data.getIntExtra(BUNDLE_STRING.FILTER_RADIUS.getName(), 100);
            priceMin = data.getIntExtra(BUNDLE_STRING.FILTER_PRICE_MIN.getName(), 100000);
            priceMax = data.getIntExtra(BUNDLE_STRING.FILTER_PRICE_MAX.getName(), 10000000);
            areaMin = data.getIntExtra(BUNDLE_STRING.FILTER_AREA_MIN.getName(), 1);
            areaMax = data.getIntExtra(BUNDLE_STRING.FILTER_AREA_MAX.getName(), 100);
            ratingMin = data.getIntExtra(BUNDLE_STRING.FILTER_RATING_MIN.getName(), 1);
            ratingMax = data.getIntExtra(BUNDLE_STRING.FILTER_RATING_MAX.getName(), 5);
            searchIt();
        }
    }


    private void searchIt() {
        List<Motel> result = new ArrayList<>();
        if (latLngSearch == null) {
            Toast.makeText(getContext(), getString(R.string.filter_options_location) + " : null", Toast.LENGTH_SHORT).show();
            result.addAll(motelListAll);
        } else if (radius == -1) {
            Toast.makeText(getContext(), getString(R.string.filter_options_radius) + " : null", Toast.LENGTH_SHORT).show();
            result.addAll(motelListAll);
        } else {


            for (Motel motel : motelListAll) {
                int count = 0;

                Location loc1 = new Location(LocationManager.GPS_PROVIDER);
                Location loc2 = new Location(LocationManager.GPS_PROVIDER);

                loc1.setLatitude(latLngSearch.latitude);
                loc1.setLongitude(latLngSearch.longitude);

                loc2.setLatitude(motel.getMapLatitude());
                loc2.setLongitude(motel.getMapLongitude());

                if (loc1.distanceTo(loc2) <= radius) {
                    count++;
                }

                if (priceMin <= motel.getPrice() && motel.getPrice() <= priceMax) {
                    count++;
                }

                if (areaMin <= motel.getArea() && motel.getArea() <= areaMax) {
                    count++;
                }

                if (ratingMin <= motel.getEvaluate() && motel.getEvaluate() <= ratingMax) {
                    count++;
                }

                if (count >= 4) {
                    result.add(motel);
                }
            }
        }

        Toast.makeText(getContext(), "" + result.size(), Toast.LENGTH_SHORT).show();
        ((TabMapFragment) getChildFragmentManager().getFragments().get(0)).found(result);
        ((TabFoundFragment) getChildFragmentManager().getFragments().get(1)).found(result);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_find, menu);
        MenuItem itemSearch = menu.findItem(R.id.itFindSearch);
        MenuItem itemFilter = menu.findItem(R.id.itFindFilter);

        SearchView searchView = (SearchView) itemSearch.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ((TabMapFragment) getChildFragmentManager().getFragments().get(0)).querySearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.itFindSearch:
                break;
            case R.id.itFindFilter:
                Intent intent = new Intent(getContext(), FilterOptionsActivity.class);
                intent.putExtra(BUNDLE_STRING.FILTER_SHOW_MAP.getName(), false);
                startActivityForResult(intent, REQUEST_CODE);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new SectionPageAdapter(getChildFragmentManager());
        String valueTextSearch = getArguments().getString("valueTextSearch");
        textSearch = valueTextSearch;
        TabMapFragment tabMapFragment = new TabMapFragment();
        Bundle bundle = new Bundle();
        bundle.putString("valueTextSearch", valueTextSearch);
        tabMapFragment.setArguments(bundle);
        adapter.addFragment(tabMapFragment, "Bản đồ");

        TabFoundFragment tabFoundFragment = new TabFoundFragment();
        adapter.addFragment(tabFoundFragment, "Phòng trọ");

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    MainActivity.mBottomBar.getShySettings().showBar();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }
}
