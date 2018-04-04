package com.team10.trotot.view.fragments.find;

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

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.team10.trotot.R;
import com.team10.trotot.model.basic_classes.Motel;
import com.team10.trotot.view.activities.MotelDetailActivity;
import com.team10.trotot.view.fragments.BaseFragment;
import com.team10.trotot.view.supports.GlideApp;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by vinhkhang on 04/12/2017.
 */

public class TabFoundFragment extends BaseFragment {
    MotelAdapter motelAdapter;
    List<Motel> motelFound;

    RecyclerView motelRecyclerView;

    public void found(List<Motel> motels) {
        if (motels == null) {
            motelFound.clear();
        } else {
            motelFound.clear();
            motelFound.addAll(motels);
        }

        motelAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_motel, container, false);

        motelFound = new ArrayList<>();

        motelRecyclerView = view.findViewById(R.id.rv_find_motel);

        motelRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        motelRecyclerView.setItemAnimator(new DefaultItemAnimator());

        motelAdapter = new MotelAdapter();
        motelRecyclerView.setAdapter(motelAdapter);

        return view;
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

            Motel motel = motelFound.get(position);

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
            return motelFound.size();
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
                        intent.putExtra("MotelID", ID);
                        startActivity(intent);
                    }
                });
            }
        }
    }


}
