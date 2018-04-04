package com.team10.trotot.view.activities;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.team10.trotot.R;

public class ProfileAboutActivity extends AppCompatActivity {

    TextView textViewAboutAppStore;
    TextView textViewAboutGroupDev;
    TextView textViewAboutAppPolicy;
    TextView textViewAboutAppWebSite;
    Toolbar  toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_about);
        toolbar = findViewById(R.id.toolbar_profile_about);
        setSupportActionBar(toolbar);
        addControls();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        addEvents();

    }

    private void addEvents() {
        textViewAboutAppStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://play.google.com/store/apps"));
                startActivity(intent);
            }
        });
        textViewAboutGroupDev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileAboutActivity.this,ProfileTeamActivity.class);
                startActivity(intent);
            }
        });
        textViewAboutAppWebSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://pdspyse.wixsite.com/trotot"));
                startActivity(intent);
            }
        });
        textViewAboutAppPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://pdspyse.wixsite.com/trotot"));
                startActivity(intent);
            }
        });
    }

    private void addControls() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_profile_about);

        textViewAboutAppStore = (TextView)findViewById(R.id.textView_about_app_store) ;
        textViewAboutAppPolicy = (TextView)findViewById(R.id.textView_about_policy) ;
        textViewAboutAppWebSite = (TextView)findViewById(R.id.textView_about_website) ;
        textViewAboutGroupDev = (TextView)findViewById(R.id.textView_about_group_dev) ;
        textViewAboutAppStore.setPaintFlags(textViewAboutAppStore.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        textViewAboutAppPolicy.setPaintFlags(textViewAboutAppPolicy.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        textViewAboutAppWebSite.setPaintFlags(textViewAboutAppWebSite.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        textViewAboutGroupDev.setPaintFlags(textViewAboutGroupDev.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
    }

}
