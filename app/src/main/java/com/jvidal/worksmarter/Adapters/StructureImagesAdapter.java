package com.jvidal.worksmarter.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.jvidal.worksmarter.Models.ImagesURLModel;
import com.jvidal.worksmarter.R;
import com.mostafaaryan.transitionalimageview.TransitionalImageView;
import com.mostafaaryan.transitionalimageview.model.TransitionalImage;

import java.util.ArrayList;

public class StructureImagesAdapter extends BaseAdapter {
    Context context; //context

    ArrayList<ImagesURLModel> items;
    LayoutInflater inflter;

    public StructureImagesAdapter(Context context, ArrayList<ImagesURLModel> items) {
        this.context = context;
        this.items = items;
        inflter = (LayoutInflater.from((context)));

    }

    @Override
    public int getCount() {
        return items.size();
    }


    @Override
    public Object getItem(int position) {
        return items.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflter.inflate(R.layout.list_images, parent, false);

        final TransitionalImageView transitionalImageView = (TransitionalImageView) convertView.findViewById(R.id.transitional_image);
        Glide.with(context).asBitmap().apply(new RequestOptions().override(400, 200)).load(items.get(position).getUrls()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {

                TransitionalImage transitionalImage = new TransitionalImage.Builder()
                        .duration(500)
                        /*.backgroundColor(ContextCompat.getColor(, R.color.colorAccent))*/
                        /*.image(R.drawable.sample_image)*/
                        .image(bitmap)
                        .create();
                transitionalImageView.setTransitionalImage(transitionalImage);
            }
        });

        return convertView;


    }
}