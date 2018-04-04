package com.team10.trotot.view.fragments.motel_detail_fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.team10.trotot.R;
import com.team10.trotot.model.basic_classes.Motel;
import com.team10.trotot.view.activities.MotelDetailActivity;
import com.team10.trotot.view.activities.MotelDetailImageSlideActivity;
import com.team10.trotot.view.supports.GlideApp;

import java.util.ArrayList;
import java.util.Iterator;

import static com.team10.trotot.model.FIREBASE_STRING.FIRE_BASE_STRING_MOTELS;

/**
 * Created by vinhkhang on 16/10/2017.
 */

public class MotelDetailImageFragment extends Fragment {

    private String motelID = MotelDetailActivity.motelID;

    private RecyclerView rvImage;
    private ArrayList<String> listImage;
    private ImageAdapter imageAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_motel_detail_image, container, false);

        listImage = new ArrayList<>();

        rvImage = view.findViewById(R.id.rv_motel_detail_image);
        rvImage.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvImage.setItemAnimator(new DefaultItemAnimator());

        imageAdapter = new ImageAdapter(getContext());
        rvImage.setAdapter(imageAdapter);

        updateUI();

        return view;
    }

    public void updateUI() {

//        listImage.clear();
//        listImage.add("https://images.unsplash.com/photo-1483356256511-b48749959172?w=1050");
//        listImage.add("https://images.unsplash.com/photo-1504893524553-b855bce32c67?w=634");
//        listImage.add("https://images.unsplash.com/photo-1494023120489-e26d4967e173?w=1050");
//        listImage.add("https://images.unsplash.com/photo-1468779065891-103dac4a7c48?w=1189");
//        listImage.add("https://images.unsplash.com/photo-1496154077138-22d8a3b92e8b?w=1050");
//        listImage.add("https://images.unsplash.com/photo-1484061263732-b8b0a5ae7db6?w=750");
//        listImage.add("https://images.unsplash.com/photo-1498061390976-2b09e3889580?w=800");
//        listImage.add("https://images.unsplash.com/photo-1505576391880-b3f9d713dc4f?w=800");


        FirebaseDatabase.getInstance().getReference().child(FIRE_BASE_STRING_MOTELS).orderByKey().equalTo(motelID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listImage = new ArrayList<>();
                if (dataSnapshot.hasChildren()) {
                    Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                    Motel motel = iterator.next().getValue(Motel.class);

                    if (motel != null) {
                        listImage.addAll(motel.getPhotosId());
                        imageAdapter.notifyDataSetChanged();
                    }

                    if (listImage.size() == 0) {
                        Toast.makeText(getContext(), "No image found", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

        Context context;

        ImageAdapter(Context context) {
            this.context = context;
        }

        @Override
        public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater li = LayoutInflater.from(parent.getContext());
            View itemView = li.inflate(R.layout.item_fragment_motel_detail_image, parent, false);

            return new ImageViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ImageViewHolder holder, int position) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("motels").child(listImage.get(position));
            GlideApp.with(getContext())
                    .load(storageReference)
                    .error(R.drawable.load_room)
                    .into(holder.img);
        }

        @Override
        public int getItemCount() {
            return listImage.size();
        }

        class ImageViewHolder extends RecyclerView.ViewHolder {

            ImageView img;

            ImageViewHolder(View itemView) {
                super(itemView);
                img = itemView.findViewById(R.id.img_motel_detail_image);

                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, MotelDetailImageSlideActivity.class);

                        intent.putExtra("list", listImage);
                        intent.putExtra("index", getAdapterPosition());

                        context.startActivity(intent);
                    }
                });
            }
        }

    }
}

