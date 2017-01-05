package com.example.softbunch.cameraandgallery.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.softbunch.cameraandgallery.R;
import com.example.softbunch.cameraandgallery.image.ImageListGallery;
import com.example.softbunch.cameraandgallery.model.M_imageList;

import java.util.ArrayList;

/**
 * Created by softbunch on 9/30/16.
 */

public class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.MyViewHolder> {

    ImageListGallery context;
    ArrayList<M_imageList> galleryPhotoDetails;
    String photofilename;

    public ImageGalleryAdapter(ImageListGallery context, ArrayList<M_imageList> galleryPhotoDetails) {
        this.context = context;
        this.galleryPhotoDetails = galleryPhotoDetails;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = null;
        try {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_photo_activity_row, parent, false);
            holder = new MyViewHolder(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(ImageGalleryAdapter.MyViewHolder holder, int position) {
        try {
            M_imageList imageListItem = galleryPhotoDetails.get(position);
            holder.setData(imageListItem, position);
            holder.setLinster();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return galleryPhotoDetails.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imagethumbnail;
        TextView txtfileName;

        M_imageList imageListItem;
        int position;

        public MyViewHolder(View itemView) {
            super(itemView);
            imagethumbnail = (ImageView) itemView.findViewById(R.id.image_preview1);
        }

        public void setData(M_imageList imageListItem, int position) {
            try {
                Glide.with(context)
                        .load(imageListItem.getimageData())
                        .into(imagethumbnail);
                this.position = position;
                this.imageListItem = imageListItem;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void setLinster() {
            imagethumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        photofilename = imageListItem.getimageData();
                        Intent i = new Intent();
                        i.putExtra("image_path", photofilename);
                        context.setResult(Activity.RESULT_OK, i);
                        context.finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
