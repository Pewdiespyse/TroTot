package com.team10.trotot.view.fragments.notification;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.team10.trotot.R;
import com.team10.trotot.view.adapters.SectionPageAdapter;
import com.team10.trotot.view.fragments.BaseFragment;

/**
 * Created by vinhkhang on 15/10/2017.
 */

public class NotificationFragment extends BaseFragment {

    private ViewPager viewPager;

    public static NotificationFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt(ARGS_INSTANCE, instance);
        NotificationFragment fragment = new NotificationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_fragment_notification);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setLogo(R.drawable.ic_bottom_bar_notification);

        viewPager = view.findViewById(R.id.container);

        SectionPageAdapter adapter = new SectionPageAdapter(getChildFragmentManager());
        adapter.addFragment(new TabNewMotelFragment(), getString(R.string.notification_new_motel));
        adapter.addFragment(new TabNewsFragment(), getString(R.string.notification_new_news));
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // add event here
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_notification, menu);
        MenuItem itemFilter = menu.findItem(R.id.itNotificationFilter);
        MenuItem itemSetting = menu.findItem(R.id.itNotificationSetting);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.itNotificationFilter:
                ((TabNewMotelFragment) getChildFragmentManager().getFragments().get(0)).openFilterOptionsActivity();
                break;
            case R.id.itNotificationSetting:
                break;
            case R.id.subMenuNotificationOn:
                ((TabNewMotelFragment) getChildFragmentManager().getFragments().get(0)).changeNotificationStatus(true);
                break;
            case R.id.subMenuNotificationOff:
                ((TabNewMotelFragment) getChildFragmentManager().getFragments().get(0)).changeNotificationStatus(false);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
