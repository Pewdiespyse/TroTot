package com.team10.trotot.view.fragments.favorite;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.team10.trotot.R;
import com.team10.trotot.model.PREF_STRING;
import com.team10.trotot.model.basic_classes.Motel;
import com.team10.trotot.view.activities.MotelDetailActivity;
import com.team10.trotot.view.fragments.BaseFragment;
import com.team10.trotot.view.supports.GlideApp;
import com.team10.trotot.view.supports.PrefUtil;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.team10.trotot.model.FIREBASE_STRING.FIRE_BASE_STRING_MOTELS;
import static com.team10.trotot.model.FIREBASE_STRING.FIRE_BASE_STRING_USERS;
import static com.team10.trotot.model.FIREBASE_STRING.FIRE_BASE_STRING_USERS_VIEWED_LIST;

/**
 * Created by vinhkhang on 04/12/2017.
 */

public class TabHistoryFragment extends BaseFragment {

    String userID = FavoriteFragment.userID;
    String FIRE_BASE_STRING_USERS_MOTEL_LIST = FIRE_BASE_STRING_USERS_VIEWED_LIST;

    Paint p = new Paint();

    MotelAdapter motelAdapter;
    List<Motel> motelListAll;
    List<String> motelListLoved;

    RecyclerView motelRecyclerView;

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_love, container, false);

        motelListLoved = new ArrayList<>();
        motelListAll = new ArrayList<>();

        motelRecyclerView = view.findViewById(R.id.rv_favorite_love_motel);

        motelRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        motelRecyclerView.setItemAnimator(new DefaultItemAnimator());

        motelAdapter = new MotelAdapter();
        motelRecyclerView.setAdapter(motelAdapter);

        initSwipe();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // get motels
        FirebaseDatabase.getInstance().getReference().child(FIRE_BASE_STRING_MOTELS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                motelListAll = new ArrayList<>();
                if (dataSnapshot.hasChildren()) {
                    Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                    Motel motel;
                    while (iterator.hasNext()) {
                        motel = iterator.next().getValue(Motel.class);
                        motelListAll.add(motel);
                    }

                    // get loved list
                    logcat("userID " + userID);
                    if (userID.equals("")) {
                        Set<String> data = PrefUtil.getStringSet(getContext(), PREF_STRING.HISTORY_MOTEL_ID);

                        if (data != null) {
                            logcat("fragment his data " + data.toString());
                            motelListLoved = new ArrayList<>(data);
                        }
                        motelAdapter.notifyDataSetChanged();
                    } else {
                        FirebaseDatabase.getInstance().getReference().child(FIRE_BASE_STRING_USERS).child(userID).child(FIRE_BASE_STRING_USERS_MOTEL_LIST).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                motelListLoved.clear();
                                if (dataSnapshot.hasChildren()) {
                                    Iterator<DataSnapshot> result = dataSnapshot.getChildren().iterator();
                                    while (result.hasNext()) {
                                        motelListLoved.add(result.next().getValue(String.class));
                                    }
                                }

//                                Collections.reverse(motelListLoved);
                                Collections.sort(motelListLoved);


                                motelAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void initSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.LEFT) {
                    motelListLoved.remove(viewHolder.getAdapterPosition());
                    motelAdapter.notifyDataSetChanged();

                    // update
                    if (userID.equals("")) {
                        PrefUtil.set(getApplicationContext(), PREF_STRING.LOVE_MOTEL_ID, new HashSet<String>(motelListLoved));
                    } else {
                        FirebaseDatabase.getInstance().getReference().child(FIRE_BASE_STRING_USERS).child(userID).child(FIRE_BASE_STRING_USERS_MOTEL_LIST).setValue(motelListLoved);
                    }
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    p.setColor(Color.parseColor("#CFD8DC"));
                    icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_delete_orange);

                    // left
                    if (dX > 0) {
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, p);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width / 2, (float) itemView.getTop() + width, (float) itemView.getLeft() + 3 * width / 2, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    } else {
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 3 * width / 2, (float) itemView.getTop() + width, (float) itemView.getRight() - width / 2, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(motelRecyclerView);
    }

    private void logcat(String data) {
        Log.v("ahihi", data);
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
            String motelID = motelListLoved.get(position);

            Motel motel = null;

            logcat("binding " + position + "  " + motelID);

            for (Motel item : motelListAll) {
                if (item.getMotelId().equals(motelID)) {
                    motel = item;
                    break;
                }
            }

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

                holder.ID = motelID;
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
            return motelListLoved.size();
        }

        //  @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
            logcat("direction " + direction);
            logcat("position " + position);
            if (viewHolder instanceof MotelAdapter.MotelViewHolder) {
                // get the removed item name to display it in snack bar
                String motelID = motelListLoved.get(viewHolder.getAdapterPosition());

                int deletedIndex = viewHolder.getAdapterPosition();

                logcat("delete " + motelID);
                Toast.makeText(getContext(), "Delete " + motelID, Toast.LENGTH_SHORT).show();
            } else {
                logcat("delete " + null);
            }
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
                        intent.putExtra("UserID", userID);
                        intent.putExtra("MotelID", ID);
                        startActivity(intent);
                    }
                });
            }
        }
    }
}
