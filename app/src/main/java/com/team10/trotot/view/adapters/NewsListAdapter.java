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
import com.team10.trotot.model.basic_classes.News;
import com.team10.trotot.view.supports.GlideApp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by lehuu on 08-Nov-17.
 */

public class NewsListAdapter extends ArrayAdapter<News> {

    Context context;
    int resource;
    List<News> objects;

    public NewsListAdapter(@NonNull Context context, int resource, @NonNull List<News> objects) {
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
            rowView = inflater.inflate(R.layout.item_news_listview, null);

            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) rowView.findViewById(R.id.tv_news_title);
            viewHolder.content = (TextView) rowView.findViewById(R.id.tv_news_content);
            viewHolder.img = (ImageView) rowView.findViewById(R.id.img_item_news_photo);
            viewHolder.time = (TextView) rowView.findViewById(R.id.tv_news_time);

            rowView.setTag(viewHolder);
        } else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        News news = objects.get(position);
        viewHolder.title.setText(news.getTitle());
        viewHolder.content.setText(news.getContent());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis((long)news.getTimePost());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        viewHolder.time.setText("Ngày: " + simpleDateFormat.format(calendar.getTime()));

        //Load hình từ firebase dùng Glide
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("news").child(news.getNewsId() + ".png");
        GlideApp.with(getContext())
                .load(storageReference)
                .into(viewHolder.img);

        return rowView;
    }

    static class ViewHolder {
        ImageView img;
        TextView title;
        TextView content;
        TextView time;
    }
}
