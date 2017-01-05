package com.example.softbunch.cameraandgallery.adapter;

import android.app.Activity;
import android.content.Context;
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
import com.example.softbunch.cameraandgallery.model.M_ImageFolder;

import java.util.ArrayList;

/**
 * Created by softbunch on 9/30/16.
 */

public class ImageFolderAdapter extends RecyclerView.Adapter<ImageFolderAdapter.MyViewHolder> {

    Context context;
    ArrayList<M_ImageFolder> imageFolderDetails;

    public ImageFolderAdapter(Context context, ArrayList<M_ImageFolder> imageFolderDetails) {
        this.context = context;
        this.imageFolderDetails = imageFolderDetails;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = null;
        try {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_folder_row, parent, false);
            holder = new MyViewHolder(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(ImageFolderAdapter.MyViewHolder holder, int position) {
        try {
            M_ImageFolder model = imageFolderDetails.get(position);
            holder.setData(model, position);
            holder.setLinster();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return imageFolderDetails.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imagethumbnail;
        TextView txtfileName;
        int position;
        M_ImageFolder model;

        public MyViewHolder(View itemView) {
            super(itemView);

            imagethumbnail = (ImageView) itemView.findViewById(R.id.img_folder);
            txtfileName = (TextView) itemView.findViewById(R.id.txt_folder_name);
        }

        public void setData(M_ImageFolder model, int position) {

            /** Display Image Folder Thumbnail **/
            Glide.with(context)
                    .load(model.getAbsolutePathOfImage())
                    .into(imagethumbnail);

            /** Display Image Folder Name **/
            txtfileName.setText(
                    model.getAbsolutePathOfImageFolder() +
                            "(" +
                            model.getTotalCount()
                            + ")"
            );
            this.position = position;
            this.model = model;

        }

        public void setLinster() {
            imagethumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(context, ImageListGallery.class);
                        intent.putExtra("BUCKETNAME", model.getAbsolutePathOfImageFolder());
                        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                        context.startActivity(intent);
                        ((Activity) context).finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }
}
