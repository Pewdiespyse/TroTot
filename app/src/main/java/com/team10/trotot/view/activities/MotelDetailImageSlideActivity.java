package com.team10.trotot.view.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.team10.trotot.R;
import com.team10.trotot.view.supports.GlideApp;

import java.util.ArrayList;

public class MotelDetailImageSlideActivity extends AppCompatActivity {

    ArrayList<String> imgList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motel_detail_image_slide);

        imgList = (ArrayList<String>) getIntent().getSerializableExtra("list");

        MyPagerAdapter mAdapter;
        ViewPager viewPager = findViewById(R.id.vp_motel_detail_image_slide);
        mAdapter = new MyPagerAdapter(this);
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(getIntent().getIntExtra("index", 0));
    }


    class MyPagerAdapter extends PagerAdapter {

        Context context;
        LayoutInflater layoutInflater;

        public MyPagerAdapter(Context _context) {
            context = _context;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View itemView = layoutInflater.inflate(R.layout.fragment_motel_detail_image_slide, container, false);
            ImageView img = itemView.findViewById(R.id.img_motel_detail_image_slide);

            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("motels").child(imgList.get(position));
            GlideApp.with(context)
                    .load(storageReference)
                    .error(R.drawable.load_room)
                    .into(img);

//            ZoomableDraweeView zoomableDraweeView = itemView.findViewById(R.id.img_motel_detail_image_slide);
//            zoomableDraweeView.setAllowTouchInterceptionWhileZoomed(mAllowSwipingWhileZoomed);
//            // needed for double tap to zoom
//            zoomableDraweeView.setIsLongpressEnabled(false);
//            zoomableDraweeView.setTapListener(new DoubleTapGestureListener(zoomableDraweeView));
//            DraweeController controller = Fresco.newDraweeControllerBuilder()
//                    .setUri(imgList.get(position))
//                    .setCallerContext("ZoomableApp-MyPagerAdapter")
//                    .build();
//            zoomableDraweeView.setController(controller);

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((FrameLayout) object);
        }

        @Override
        public int getCount() {
            return imgList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View arg0, @NonNull Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            // We want to create a new view when we call notifyDataSetChanged() to have the correct behavior
            return POSITION_NONE;
        }
    }
}
