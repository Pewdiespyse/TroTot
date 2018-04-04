package com.team10.trotot.view.fragments.favorite;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.FirebaseDatabase;
import com.team10.trotot.R;
import com.team10.trotot.model.PREF_STRING;
import com.team10.trotot.view.activities.MainActivity;
import com.team10.trotot.view.adapters.SectionPageAdapter;
import com.team10.trotot.view.fragments.BaseFragment;
import com.team10.trotot.view.supports.PrefUtil;

import java.util.HashSet;

import static com.team10.trotot.model.FIREBASE_STRING.FIRE_BASE_STRING_USERS;
import static com.team10.trotot.model.FIREBASE_STRING.FIRE_BASE_STRING_USERS_LIKED_LIST;
import static com.team10.trotot.model.FIREBASE_STRING.FIRE_BASE_STRING_USERS_VIEWED_LIST;

/**
 * Created by vinhkhang on 15/10/2017.
 */

public class FavoriteFragment extends BaseFragment {

    public static String userID = MainActivity.USER_ID;
    private ViewPager viewPager;

    public static FavoriteFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt(ARGS_INSTANCE, instance);
        FavoriteFragment fragment = new FavoriteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_fragment_favorite);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setLogo(R.drawable.ic_bottom_bar_favorite);

        viewPager = (ViewPager) view.findViewById(R.id.container);
        SectionPageAdapter adapter = new SectionPageAdapter(getChildFragmentManager());
        adapter.addFragment(new TabLoveFragment(), getString(R.string.favorite_tab_love));
        adapter.addFragment(new TabHistoryFragment(), getString(R.string.favorite_tab_history));
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_favorite, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menuFavoriteDelete: {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle(getString(R.string.favorite_tool_bar_clear_all_confirm));

                if (viewPager.getCurrentItem() == 0) {
                    // favorite
                    alertDialog.setMessage(getString(R.string.favorite_tool_bar_clear_all_confirm_love));
                    alertDialog.setPositiveButton(getString(R.string.favorite_tool_bar_clear_all_confirm_yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // delete

                            PrefUtil.set(getContext(), PREF_STRING.LOVE_MOTEL_ID, new HashSet<String>());

                            if (!userID.equals("")) {
                                FirebaseDatabase.getInstance().getReference().child(FIRE_BASE_STRING_USERS).child(userID).child(FIRE_BASE_STRING_USERS_LIKED_LIST).removeValue();
                            }
                        }
                    });
                } else {
                    // history
                    alertDialog.setMessage(getString(R.string.favorite_tool_bar_clear_all_confirm_history));
                    alertDialog.setPositiveButton(getString(R.string.favorite_tool_bar_clear_all_confirm_yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // delete

                            PrefUtil.set(getContext(), PREF_STRING.HISTORY_MOTEL_ID, new HashSet<String>());

                            if (!userID.equals("")) {
                                FirebaseDatabase.getInstance().getReference().child(FIRE_BASE_STRING_USERS).child(userID).child(FIRE_BASE_STRING_USERS_VIEWED_LIST).removeValue();
                            }
                        }
                    });
                }

                alertDialog.setNegativeButton(getString(R.string.favorite_tool_bar_clear_all_confirm_no), null);
                alertDialog.show();
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }
}
