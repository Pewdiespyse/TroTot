package com.team10.trotot.view.fragments.motel_detail_fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team10.trotot.R;
import com.team10.trotot.model.basic_classes.Motel;
import com.team10.trotot.view.activities.MotelDetailActivity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import static com.team10.trotot.model.FIREBASE_STRING.FIRE_BASE_STRING_MOTELS;


/**
 * Created by vinhkhang on 16/10/2017.
 */

public class MotelDetailInfoFragment extends Fragment {

    private String motelID = MotelDetailActivity.motelID;
    private Motel motel = null;

    private TextView tvAddress;
    private TextView tvArea;
    private TextView tvDetail;
    private TextView tvName;
    private TextView tvPhone;
    private TextView tvPrice;
    private TextView tvOpenTime;
    private TextView tvCloseTime;
    private TextView tvPostTime;

    private ImageView imgSMS, imgCall;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_motel_detail_info, container, false);

        tvAddress = view.findViewById(R.id.tv_motel_detail_information_address_detail);
        tvArea = view.findViewById(R.id.tv_motel_detail_information_area_detail);
        tvDetail = view.findViewById(R.id.tv_motel_detail_information_detail_detail);
        tvName = view.findViewById(R.id.tv_motel_detail_information_name_detail);
        tvPhone = view.findViewById(R.id.tv_motel_detail_information_phone_detail);
        tvPrice = view.findViewById(R.id.tv_motel_detail_information_price_detail);
        tvOpenTime = view.findViewById(R.id.tv_motel_detail_information_open_time_detail);
        tvCloseTime = view.findViewById(R.id.tv_motel_detail_information_close_time_detail);
        tvPostTime = view.findViewById(R.id.tv_motel_detail_information_post_time_detail);

        imgSMS = view.findViewById(R.id.img_motel_detail_information_phone_sms);
        imgCall = view.findViewById(R.id.img_motel_detail_information_phone_call);

        updateUI();

        return view;
    }

    private void updateUI() {

        FirebaseDatabase.getInstance().getReference().child(FIRE_BASE_STRING_MOTELS).orderByKey().equalTo(motelID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                    motel = iterator.next().getValue(Motel.class);

                    if (motel != null) {
                        tvAddress.setText(motel.getAddress());
                        tvArea.setText(String.valueOf(motel.getArea()));
                        tvDetail.setText(motel.getMotelDetail());
                        tvName.setText(motel.getName());
                        tvPhone.setText(motel.getPhone());

                        imgSMS.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", motel.getPhone(), null));
                                startActivity(intent);
                            }
                        });

                        imgCall.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", motel.getPhone(), null));
                                startActivity(intent);
                            }
                        });

                        tvPrice.setText(String.valueOf(motel.getPrice()));
                        tvOpenTime.setText(String.valueOf(motel.getTimeOpen()));
                        tvCloseTime.setText(String.valueOf(motel.getTimeClose()));

                        Timestamp timestamp = new Timestamp(motel.getTimePost());
                        Date date = new Date(timestamp.getTime());
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

                        tvPostTime.setText(simpleDateFormat.format(date));
                    } else {
                        tvAddress.setText(getText(R.string.home_no_data));
                        tvArea.setText(getText(R.string.home_no_data));
                        tvDetail.setText(getText(R.string.home_no_data));
                        tvName.setText(getText(R.string.home_no_data));
                        tvPhone.setText(getText(R.string.home_no_data));
                        tvPrice.setText(getText(R.string.home_no_data));
                        tvOpenTime.setText(getText(R.string.home_no_data));
                        tvCloseTime.setText(getText(R.string.home_no_data));
                        tvPostTime.setText(getText(R.string.home_no_data));

                        imgSMS.setVisibility(View.GONE);
                        imgCall.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

}
