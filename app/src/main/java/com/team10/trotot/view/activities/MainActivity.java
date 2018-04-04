package com.team10.trotot.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ncapdevi.fragnav.FragNavController;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;
import com.team10.trotot.R;
import com.team10.trotot.model.PREF_STRING;
import com.team10.trotot.view.fragments.BaseFragment;
import com.team10.trotot.view.fragments.favorite.FavoriteFragment;
import com.team10.trotot.view.fragments.find.FindFragment;
import com.team10.trotot.view.fragments.notification.NotificationFragment;
import com.team10.trotot.view.fragments.profile.ProfileFragment;
import com.team10.trotot.view.supports.PrefUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.team10.trotot.model.FIREBASE_STRING.FIRE_BASE_STRING_USERS;
import static com.team10.trotot.model.FIREBASE_STRING.FIRE_BASE_STRING_USERS_LIKED_LIST;
import static com.team10.trotot.model.FIREBASE_STRING.FIRE_BASE_STRING_USERS_VIEWED_LIST;

/**
 * Created by vinhkhang on 15/10/2017.
 */

public class MainActivity extends AppCompatActivity implements BaseFragment.FragmentNavigation, FragNavController.TransactionListener, FragNavController.RootFragmentListener {

    // Todo
    // sửa thành rỗng ("") khi mới sử dụng ứng dụng chưa đăng nhập
    // sửa thành ID thực khi đã đăng nhập
    // hiện tại đang để "3uuEChU5waTjSlPzDyX8hH7Atol1" để test
    // WARNING :
    //          không sử dụng null
    //          không gán ngược từ những tác nhân khác MainActivity và LogIn
    public static String USER_ID = "3uuEChU5waTjSlPzDyX8hH7Atol1";
    public static BottomBar mBottomBar;
    private final int INDEX_HOME = FragNavController.TAB1;
    private final int INDEX_FAVORITE = FragNavController.TAB2;
    private final int INDEX_NOTIFICATION = FragNavController.TAB3;
    private final int INDEX_PROFILE = FragNavController.TAB4;
    private boolean isLoadFavorite = false;
    private boolean isLoadHistory = false;
    private Toolbar mToolBar;
    private FragNavController mNavController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fresco.initialize(this);

        //toolBar
        mToolBar = findViewById(R.id.toolBar);
        setSupportActionBar(mToolBar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        //bottomBar
        mBottomBar = findViewById(R.id.bottomBar);

        //exit login and go to profile
        if (ProfileAuthenticationActivity.isLogin) {
            mBottomBar.selectTabAtPosition(INDEX_PROFILE);
        } else
            mBottomBar.selectTabAtPosition(INDEX_HOME);

        mNavController = FragNavController.newBuilder(savedInstanceState, getSupportFragmentManager(), R.id.contentContainer)
                .transactionListener(this)
                .rootFragmentListener(this, 4)
                .build();

        mBottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_home:
                        mNavController.switchTab(INDEX_HOME);
                        break;
                    case R.id.tab_favorite:
                        mNavController.switchTab(INDEX_FAVORITE);
                        break;
                    case R.id.tab_notification:
                        mNavController.switchTab(INDEX_NOTIFICATION);
                        break;
                    case R.id.tab_profile:
                        boolean isLogin = ProfileAuthenticationActivity.isLogin;
                        if (isLogin == true) {
                            mNavController.switchTab(INDEX_PROFILE);
                        } else {
                            Intent intent = new Intent(MainActivity.this, ProfileAuthenticationActivity.class);
                            startActivity(intent);
                        }
                        break;
                }
            }
        });


        mBottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
//                mNavController.clearStack();
            }
        });

//        BottomBarTab nearby = mBottomBar.getTabWithId(R.id.tab_notification);
//        nearby.setBadgeCount(5);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        String id = PrefUtil.getString(getApplicationContext(), PREF_STRING.USER_ID);
        if (!id.equals("")) {
            USER_ID = id;
        }

        // sync local shared pref to firebase when signed
        if (!USER_ID.equals("")) {
            syncData(USER_ID);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        PrefUtil.set(getApplicationContext(), PREF_STRING.USER_ID, USER_ID);
    }

    @Override
    public void onBackPressed() {
        if (!mNavController.isRootFragment()) {
            mNavController.popFragment();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mNavController != null) {
            mNavController.onSaveInstanceState(outState);
        }
    }

    @Override
    public void pushFragment(Fragment fragment) {
        if (mNavController != null) {
            mNavController.pushFragment(fragment);
        }
    }

    @Override
    public void onTabTransaction(Fragment fragment, int index) {
        // If we have a backstack, show the back button
        if (getSupportActionBar() != null && mNavController != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(!mNavController.isRootFragment());
        }
    }

    @Override
    public void onFragmentTransaction(Fragment fragment, FragNavController.TransactionType transactionType) {
        // do fragmentty stuff. Maybe change title, I'm not going to tell you how to live your life
        // If we have a backstack, show the back button
        if (getSupportActionBar() != null && mNavController != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(!mNavController.isRootFragment());
        }
    }

    @Override
    public Fragment getRootFragment(int index) {
        switch (index) {
            case INDEX_HOME:
                Intent i = getIntent();
                String valueTextSearch = i.getExtras().getString("valueTextSearch", "");
                Bundle bundle = new Bundle();
                bundle.putString("valueTextSearch", valueTextSearch);
                FindFragment obj = FindFragment.newInstance(0);
                obj.setArguments(bundle);
                return obj;
            case INDEX_FAVORITE:
                return FavoriteFragment.newInstance(0);
            case INDEX_NOTIFICATION:
                return NotificationFragment.newInstance(0);
            case INDEX_PROFILE:
                return ProfileFragment.newInstance(0);
        }
        throw new IllegalStateException("ahihi, Need to send an valid index");
    }

    private void syncData(final String id) {

        isLoadFavorite = true;
        isLoadHistory = true;

        // get viewed list
        FirebaseDatabase.getInstance().getReference().child(FIRE_BASE_STRING_USERS).child(id).child(FIRE_BASE_STRING_USERS_VIEWED_LIST).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!isLoadHistory) {
                    return;
                }

                List<String> userList = new ArrayList<>();

                // get firebase
                if (dataSnapshot.hasChildren()) {
                    Iterator<DataSnapshot> result = dataSnapshot.getChildren().iterator();
                    while (result.hasNext()) {
                        userList.add(result.next().getValue(String.class));
                    }
                }

                // combine with local
                userList.addAll(PrefUtil.getStringSet(getApplicationContext(), PREF_STRING.HISTORY_MOTEL_ID));

//                Log.v("ahihi", "userList 1" + userList.size());
//                Log.v("ahihi", "userList 1" + userList);

                // remove duplicate
                Set<String> hs = new HashSet<>();
                hs.addAll(userList);
                userList.clear();
                userList.addAll(hs);

//                Log.v("ahihi", "userList 2" + userList.size());
//                Log.v("ahihi", "userList 2" + userList);

                // push to firebase
                FirebaseDatabase.getInstance().getReference().child(FIRE_BASE_STRING_USERS).child(id).child(FIRE_BASE_STRING_USERS_VIEWED_LIST).setValue(userList);

                // save to local empty
                PrefUtil.set(getApplicationContext(), PREF_STRING.HISTORY_MOTEL_ID, new HashSet<String>());

                isLoadHistory = false;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // get liked list
        FirebaseDatabase.getInstance().getReference().child(FIRE_BASE_STRING_USERS).child(id).child(FIRE_BASE_STRING_USERS_LIKED_LIST).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!isLoadFavorite) {
                    return;
                }

                List<String> userList = new ArrayList<>();

                // get firebase
                if (dataSnapshot.hasChildren()) {
                    Iterator<DataSnapshot> result = dataSnapshot.getChildren().iterator();
                    while (result.hasNext()) {
                        userList.add(result.next().getValue(String.class));
                    }
                }

                // combine with local
                userList.addAll(PrefUtil.getStringSet(getApplicationContext(), PREF_STRING.LOVE_MOTEL_ID));

                // remove duplicate
                Set<String> hs = new HashSet<>();
                hs.addAll(userList);
                userList.clear();
                userList.addAll(hs);

                // push to firebase
                FirebaseDatabase.getInstance().getReference().child(FIRE_BASE_STRING_USERS).child(id).child(FIRE_BASE_STRING_USERS_LIKED_LIST).setValue(userList);

                // save to local empty
                PrefUtil.set(getApplicationContext(), PREF_STRING.LOVE_MOTEL_ID, new HashSet<String>());

                isLoadFavorite = false;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}


