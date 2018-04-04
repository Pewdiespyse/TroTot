package com.team10.trotot.view.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.team10.trotot.R;
import com.team10.trotot.model.basic_classes.Motel;
import com.team10.trotot.view.supports.GlideApp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by lehuu on 08-Nov-17.
 */

public class MotelListAdapter extends ArrayAdapter<Motel> {

    Context context;
    int resource;
    List<Motel> objects;

    public MotelListAdapter(@NonNull Context context, int resource, @NonNull List<Motel> objects) {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        this.objects = objects;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rowView = convertView;
        ViewHolder viewHolder;
        if(rowView == null){
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.item_motel_listview, null);

            viewHolder = new ViewHolder();
            viewHolder.motel = (TextView) rowView.findViewById(R.id.tv_item_favorite_motel_name);
            viewHolder.price = (TextView) rowView.findViewById(R.id.tv_item_favorite_motel_price);
            viewHolder.time = (TextView) rowView.findViewById(R.id.tv_time);
            viewHolder.evaluate = (TextView) rowView.findViewById(R.id.tv_evaluate);
            viewHolder.address = (TextView) rowView.findViewById(R.id.tv_address);
            viewHolder.img = (ImageView) rowView.findViewById(R.id.img_item_favorite_motel);

            rowView.setTag(viewHolder);
        } else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Motel motel = objects.get(position);
        viewHolder.motel.setText(motel.getName());
        viewHolder.price.setText("" + motel.getPrice());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis((long)motel.getTimePost());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        viewHolder.time.setText(simpleDateFormat.format(calendar.getTime()));
        viewHolder.evaluate.setText("" + motel.getEvaluate());
        viewHolder.address.setText("Địa chỉ: " + motel.getAddress());
        //Load hình từ firebase dùng Glide
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("motels").child(motel.getPhotosId().get(0));
        GlideApp.with(getContext())
                .load(storageReference)
                .into(viewHolder.img);

        return rowView;
    }

    static class ViewHolder {
        ImageView img;
        TextView motel;
        TextView price;
        TextView time;
        TextView evaluate;
        TextView address;
    }
}
