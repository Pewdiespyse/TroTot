package com.team10.trotot.view.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.share.model.ShareOpenGraphAction;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.share.model.ShareOpenGraphObject;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.team10.trotot.R;
import com.team10.trotot.model.PREF_STRING;
import com.team10.trotot.model.basic_classes.Motel;
import com.team10.trotot.view.fragments.motel_detail_fragments.MotelDetailImageFragment;
import com.team10.trotot.view.fragments.motel_detail_fragments.MotelDetailInfoFragment;
import com.team10.trotot.view.fragments.motel_detail_fragments.MotelDetailMapFragment;
import com.team10.trotot.view.fragments.motel_detail_fragments.MotelDetailReviewFragment;
import com.team10.trotot.view.supports.PrefUtil;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static com.team10.trotot.model.FIREBASE_STRING.FIRE_BASE_STRING_MOTELS;
import static com.team10.trotot.model.FIREBASE_STRING.FIRE_BASE_STRING_USERS;
import static com.team10.trotot.model.FIREBASE_STRING.FIRE_BASE_STRING_USERS_LIKED_LIST;
import static com.team10.trotot.model.FIREBASE_STRING.FIRE_BASE_STRING_USERS_VIEWED_LIST;


public class MotelDetailActivity extends AppCompatActivity {

    // -------------------------------------------------------------------------------------- //
    // TODO sửa các config sau cho phù hợp nếu cần thiết
    public static final int MINIMUM_COMMENT_CHARACTER = 10;
    public static final int MINIMUM_REPLY_CHARACTER = 10;
    // -------------------------------------------------------------------------------------- //
    public static String motelID;
    public static String userID;

    private MotelDetailInfoFragment infoFragment;
    private MotelDetailImageFragment imageFragment;
    private MotelDetailReviewFragment reviewFragment;
    private MotelDetailMapFragment mapFragment;
    private FragmentPagerItemAdapter fragmentPagerItemAdapter;
    private ViewPager viewPager;
    private Menu myMenu;
    private Motel motel = null;
    private List<String> userViewedList;
    private List<String> userLikedList;
    private boolean isTriggerEvent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motel_detail);

//        Fresco.initialize(this);

        // -------------------------------------------------------------------------------------- //
        motelID = getIntent().getStringExtra("MotelID");
        if (motelID == null) {
            Toast.makeText(getApplicationContext(), "NULL ID", Toast.LENGTH_SHORT).show();
            finish();
        }

        // use before create child fragment (before not sync)
        userID = PrefUtil.getString(getApplicationContext(), PREF_STRING.USER_ID);

        logcat("motel " + motelID);
        logcat("user " + userID);

        // -------------------------------------------------------------------------------------- //

        Toolbar toolbar = findViewById(R.id.tb_motel_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        FragmentPagerItems pages = new FragmentPagerItems(this);
        pages.add(FragmentPagerItem.of(getString(R.string.home_model_detail_information), MotelDetailInfoFragment.class));
        pages.add(FragmentPagerItem.of(getString(R.string.home_model_detail_image), MotelDetailImageFragment.class));
        pages.add(FragmentPagerItem.of(getString(R.string.home_model_detail_review), MotelDetailReviewFragment.class));
        pages.add(FragmentPagerItem.of(getString(R.string.home_model_detail_map), MotelDetailMapFragment.class));

        fragmentPagerItemAdapter = new FragmentPagerItemAdapter(getSupportFragmentManager(), pages);

        viewPager = findViewById(R.id.vp_motel_detail);
        viewPager.setAdapter(fragmentPagerItemAdapter);
        viewPager.setOffscreenPageLimit(4);

        SmartTabLayout smartTabLayout = findViewById(R.id.stl_motel_detail);
        smartTabLayout.setViewPager(viewPager);

        userViewedList = new ArrayList<>();
        userLikedList = new ArrayList<>();

        isTriggerEvent = true;

        loadData();
    }

    private void logcat(String data) {
        Log.v("ahihi", data);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        isTriggerEvent = false;
    }

    private void loadData() {

        // not signed in
        if (userID.equals("")) {
            Set<String> data;

            data = PrefUtil.getStringSet(getApplicationContext(), PREF_STRING.HISTORY_MOTEL_ID);
            if (data != null) {
                userViewedList = new ArrayList<>(data);
            }

            if (!userViewedList.contains(motelID)) {
                userViewedList.add(motelID);
            }
            PrefUtil.set(getApplicationContext(), PREF_STRING.HISTORY_MOTEL_ID, new HashSet<String>(userViewedList));

            data = PrefUtil.getStringSet(getApplicationContext(), PREF_STRING.HISTORY_MOTEL_ID);

            logcat("activity data " + data.toString());
//            logcat("data " + data.size());
//
//            List<String> al = new ArrayList<>(data);

//            for (String item : al) {
//                logcat("item " + item);
//            }

            data = PrefUtil.getStringSet(getApplicationContext(), PREF_STRING.LOVE_MOTEL_ID);
            if (data != null) {
                userLikedList = new ArrayList<>(data);
            }
            if (myMenu != null) {
                if (userLikedList.contains(motelID)) {
                    myMenu.findItem(R.id.menu_motel_detail_favorite).setIcon(R.drawable.ic_action_favorite);
                } else {
                    myMenu.findItem(R.id.menu_motel_detail_favorite).setIcon(R.drawable.ic_action_favorite_empty);
                }
            }
            return;
        }

        // get viewed list
        FirebaseDatabase.getInstance().getReference().child(FIRE_BASE_STRING_USERS).child(userID).child(FIRE_BASE_STRING_USERS_VIEWED_LIST).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userViewedList.clear();
                if (dataSnapshot.hasChildren()) {
                    Iterator<DataSnapshot> result = dataSnapshot.getChildren().iterator();
                    while (result.hasNext()) {
                        userViewedList.add(result.next().getValue(String.class));
                    }
                }

                if (!userViewedList.contains(motelID) && isTriggerEvent) {
                    userViewedList.add(motelID);
                    FirebaseDatabase.getInstance().getReference().child(FIRE_BASE_STRING_USERS).child(userID).child(FIRE_BASE_STRING_USERS_VIEWED_LIST).setValue(userViewedList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // get liked list
        FirebaseDatabase.getInstance().getReference().child(FIRE_BASE_STRING_USERS).child(userID).child(FIRE_BASE_STRING_USERS_LIKED_LIST).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userLikedList.clear();
                if (dataSnapshot.hasChildren()) {
                    Iterator<DataSnapshot> result = dataSnapshot.getChildren().iterator();
                    while (result.hasNext()) {
                        userLikedList.add(result.next().getValue(String.class));
                    }
                }

                if (myMenu != null) {
                    if (userLikedList.contains(motelID)) {
                        myMenu.findItem(R.id.menu_motel_detail_favorite).setIcon(R.drawable.ic_action_favorite);
                    } else {
                        myMenu.findItem(R.id.menu_motel_detail_favorite).setIcon(R.drawable.ic_action_favorite_empty);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_motel_detail, menu);
        MenuItem itemShare = menu.findItem(R.id.menu_motel_detail_share);
        this.myMenu = menu;
        if (userLikedList.contains(motelID)) {
            this.myMenu.findItem(R.id.menu_motel_detail_favorite).setIcon(R.drawable.ic_action_favorite);
        } else {
            this.myMenu.findItem(R.id.menu_motel_detail_favorite).setIcon(R.drawable.ic_action_favorite_empty);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_motel_detail_share:


//                if(ShareDialog.canShow(ShareLinkContent.class)) {
//                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
//                            .setContentTitle("abcdefg")
//                            .setContentDescription("Your Description")
//                            .setImageUrl(Uri.parse("http://image.khaigiang.vn/school/files/212/thumbnail/600x400/554554khtnlarge.jpg"))
//                            .build();
//                    shareDialog.show(linkContent);
//                }

                FirebaseDatabase.getInstance().getReference().child(FIRE_BASE_STRING_MOTELS).orderByKey().equalTo(motelID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChildren()) {
                            Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                            motel = iterator.next().getValue(Motel.class);

                            Timestamp timestamp = new Timestamp(motel.getTimePost());
                            Date date = new Date(timestamp.getTime());
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

                            ShareDialog shareDialog = new ShareDialog(MotelDetailActivity.this);

                            ShareOpenGraphObject object = new ShareOpenGraphObject.Builder()
                                    .putString("fb:app_id", "127919827911885")
                                    .putString("og:type", "article")
                                    .putString("og:title", motel.getName())
                                    .putString("og:description", "Thời gian đăng bài: " + simpleDateFormat.format(date) + ", địa chỉ: " + motel.getAddress() + ", liên hệ: " + motel.getPhone())
                                    .putString("og:image", motel.getPhotosId().get(0))
                                    .build();

                            ShareOpenGraphAction action = new ShareOpenGraphAction.Builder()
                                    .setActionType("news.publishes")
                                    .putObject("article", object)
                                    .build();

                            ShareOpenGraphContent content = new ShareOpenGraphContent.Builder()
                                    .setPreviewPropertyName("article")
                                    .setAction(action)
                                    .build();

                            shareDialog.show(content);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                return true;
            case R.id.menu_motel_detail_favorite:

                if (userLikedList.contains(motelID)) {
                    userLikedList.remove(motelID);
                } else {
                    userLikedList.add(motelID);
                }

                PrefUtil.set(getApplicationContext(), PREF_STRING.LOVE_MOTEL_ID, new HashSet<String>(userLikedList));

                if (!userID.equals("")) {
                    FirebaseDatabase.getInstance().getReference().child(FIRE_BASE_STRING_USERS).child(userID).child(FIRE_BASE_STRING_USERS_LIKED_LIST).setValue(userLikedList);
                } else {
                    if (userLikedList.contains(motelID)) {
                        this.myMenu.findItem(R.id.menu_motel_detail_favorite).setIcon(R.drawable.ic_action_favorite);
                    } else {
                        this.myMenu.findItem(R.id.menu_motel_detail_favorite).setIcon(R.drawable.ic_action_favorite_empty);
                    }
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}




