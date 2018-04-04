package com.team10.trotot.view.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.team10.trotot.R;
import com.team10.trotot.model.basic_classes.News;
import com.team10.trotot.view.supports.GlideApp;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NewsDetailActivity extends AppCompatActivity implements ValueEventListener {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    News news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        Toolbar toolbar = findViewById(R.id.toolbar_news_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        String title = getIntent().getStringExtra("Title");
        String newsId = getIntent().getStringExtra("NewsId");

        getSupportActionBar().setTitle(title);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("news").child(newsId);

        news = new News();

        databaseReference.addValueEventListener(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        news = dataSnapshot.getValue(News.class);
        addControl(news);
    }

    private void addControl(News news) {
        TextView txtNewsTitle = findViewById(R.id.tv_news_title);
        TextView txtNewsTime = findViewById(R.id.tv_news_time);
        ImageView imgFeaturePhoto = findViewById(R.id.img_feature_photo);
        TextView txtNewsContent = findViewById(R.id.tv_news_content);

        txtNewsTitle.setText(news.getTitle());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis((long)news.getTimePost());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        txtNewsTime.setText(simpleDateFormat.format(calendar.getTime()));
        txtNewsContent.setText(news.getContent());

        //Load hình từ firebase dùng Glide
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("news").child(news.getNewsId() + ".png");
        GlideApp.with(NewsDetailActivity.this)
                .load(storageReference)
                .into(imgFeaturePhoto);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
