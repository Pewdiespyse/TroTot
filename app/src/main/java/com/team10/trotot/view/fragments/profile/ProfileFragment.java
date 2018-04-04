package com.team10.trotot.view.fragments.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.team10.trotot.R;
import com.team10.trotot.model.PREF_STRING;
import com.team10.trotot.view.activities.MainActivity;
import com.team10.trotot.view.activities.ProfileAboutActivity;
import com.team10.trotot.view.activities.ProfileAuthenticationActivity;
import com.team10.trotot.view.activities.ProfileFeedbackActivity;
import com.team10.trotot.view.activities.ProfilePostActivity;
import com.team10.trotot.view.activities.ProfileUserActivity;
import com.team10.trotot.view.fragments.BaseFragment;
import com.team10.trotot.view.supports.PrefUtil;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by vinhkhang on 15/10/2017.
 */

public class ProfileFragment extends BaseFragment {

    private Button btnUser;
    private Button btnPost;
    private Button btnAbout;
    private Button btnFeedback;
    private TextView textViewName;
    private TextView textViewEmail;
    private CircleImageView circleImageView;
    public static ProfileFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt(ARGS_INSTANCE, instance);
        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.title_fragment_profile);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setLogo(R.drawable.ic_bottom_bar_profile);
//        String text = getIntent().getStringExtra("extra_text");
//        if (intent != null) {
        addControls(view);
        addEvents(view);
        return view;
    }

    private void addEvents(View view) {

        //process user
        btnUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ProfileUserActivity.class);
                getContext().startActivity(intent);
            }
        });

        //process post
        btnPost.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ProfilePostActivity.class);
                intent.putExtra("TypeInput", "Post");
                getContext().startActivity(intent);
            }
        });

        //process about
        btnAbout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ProfileAboutActivity.class);
                getContext().startActivity(intent);
            }
        });


        //process feedback
        btnFeedback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ProfileFeedbackActivity.class);
                getContext().startActivity(intent);
            }
        });
    }

    private void addControls(View view) {
        btnUser = view.findViewById(R.id.button_profile_user);
        btnPost = view.findViewById(R.id.button_profile_post);
        btnAbout = view.findViewById(R.id.button_profile_about);
        btnFeedback = view.findViewById(R.id.button_profile_feedback);
        textViewName = view.findViewById(R.id.textView_profile_name);
        textViewEmail = view.findViewById(R.id.textView_profile_email);
        circleImageView = view.findViewById(R.id.imageView_profile_avatar);

        String avatarUrl = ProfileAuthenticationActivity.PROFILE.getAvatarUrl();
        if (avatarUrl != "")
            Picasso.with(getContext()).load(avatarUrl).placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar).into(circleImageView);
        else
            Picasso.with(getContext()).load(R.drawable.default_avatar).into(circleImageView);
        //Load hình từ firebase dùng Glide
//        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("users").child(ProfileAuthenticationActivity.PROFILE.getUserId() + ".png");
//        if (storageReference != null) {
//            GlideApp.with(this)
//                    .load(storageReference)
//                    .into(circleImageView);
//        }
        textViewName.setText(ProfileAuthenticationActivity.PROFILE.getName());
        textViewEmail.setText(ProfileAuthenticationActivity.PROFILE.getEmail());

    }

    @Override
    public void onStart() {
        super.onStart();
        String id = PrefUtil.getString(getContext(), PREF_STRING.USER_ID);
        if (!id.equals("")) {
            MainActivity.USER_ID = id;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        PrefUtil.set(getContext(), PREF_STRING.USER_ID, MainActivity.USER_ID);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile, menu);
        MenuItem itemSignOut = menu.findItem(R.id.itProfileSignOut);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.itProfileSignOut:
                ProfileAuthenticationActivity.LogOut();
                Toast.makeText(getContext(), getString(R.string.profile_logout_success), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), ProfileAuthenticationActivity.class);
                getContext().startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
