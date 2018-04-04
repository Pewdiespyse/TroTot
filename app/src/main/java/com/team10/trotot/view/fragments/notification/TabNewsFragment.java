package com.team10.trotot.view.fragments.notification;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.team10.trotot.R;
import com.team10.trotot.model.basic_classes.News;
import com.team10.trotot.view.activities.NewsDetailActivity;
import com.team10.trotot.view.fragments.BaseFragment;
import com.team10.trotot.view.supports.GlideApp;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Created by vinhkhang on 06/12/2017.
 */

public class TabNewsFragment extends BaseFragment {

    final String NEWS_FIREABSE_DATABASE_STRING = "news";
    final String NEWS_FIREABSE_STORAGE_STRING = "news";

    NewsAdapter newsAdapter;
    List<News> newsList;

    RecyclerView newsRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification_news, container, false);

        newsList = new ArrayList<>();

        newsRecyclerView = view.findViewById(R.id.rv_notification_news);

        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        newsRecyclerView.setItemAnimator(new DefaultItemAnimator());

        newsAdapter = new NewsAdapter();
        newsRecyclerView.setAdapter(newsAdapter);

        FirebaseDatabase.getInstance().getReference().child(NEWS_FIREABSE_DATABASE_STRING).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                newsList = new ArrayList<>();
                if (dataSnapshot.hasChildren()) {
                    Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                    News news;
                    while (iterator.hasNext()) {
                        news = iterator.next().getValue(News.class);
                        newsList.add(news);
                    }
                }
                Collections.reverse(newsList);
                newsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }


    class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

        public NewsAdapter() {
        }

        @Override
        public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater li = LayoutInflater.from(parent.getContext());
            View itemView = li.inflate(R.layout.item_news_listview, parent, false);
            return new NewsViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final NewsViewHolder holder, int position) {
            final News news = newsList.get(position);

            if (news != null) {

                holder.title.setText(news.getTitle());
                holder.content.setText(news.getContent());

                Timestamp timestamp = new Timestamp(news.getTimePost());
                Date date = new Date(timestamp.getTime());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

                holder.time.setText(simpleDateFormat.format(date));

//                FirebaseStorage.getInstance().getReference().child(NEWS_FIREABSE_STORAGE_STRING + "/" + news.getNewsId() + ".png").getDownloadUrl()
//                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri uri) {
//                                Picasso.with(getContext()).load(uri.toString()).into(holder.img);
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                holder.img.setImageResource(R.drawable.load_image);
//                            }
//                        });

                //Load hình từ firebase dùng Glide
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(NEWS_FIREABSE_STORAGE_STRING).child(news.getNewsId() + ".png");
                GlideApp.with(getContext())
                        .load(storageReference)
                        .into(holder.img);

                holder.ID = news.getNewsId();
            } else {
                holder.title.setText(getString(R.string.home_no_data));
                holder.content.setText(getString(R.string.home_no_data));
                holder.time.setText(getString(R.string.home_no_data));
            }
        }

        @Override
        public int getItemCount() {
            return newsList.size();
        }

        class NewsViewHolder extends RecyclerView.ViewHolder {

            String ID;
            ImageView img;
            TextView title;
            TextView content;
            TextView time;

            public NewsViewHolder(View itemView) {
                super(itemView);

                img = itemView.findViewById(R.id.img_item_news_photo);
                title = itemView.findViewById(R.id.tv_news_title);
                content = itemView.findViewById(R.id.tv_news_content);
                time = itemView.findViewById(R.id.tv_news_time);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), NewsDetailActivity.class);
                        intent.putExtra("Title", title.getText());
                        intent.putExtra("NewsId", ID);
                        startActivity(intent);
                    }
                });
            }
        }
    }
}
