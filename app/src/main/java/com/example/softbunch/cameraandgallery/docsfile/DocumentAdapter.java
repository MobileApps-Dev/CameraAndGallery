package com.example.softbunch.cameraandgallery.docsfile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.softbunch.cameraandgallery.R;

import java.util.ArrayList;

/**
 * Created by softbunch on 9/30/16.
 */

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.MyViewHolder> {

    Context context;
    ArrayList<M_Files> documentDetails;

    public DocumentAdapter(Context context, ArrayList<M_Files> documentDetails) {
        this.context = context;
        this.documentDetails = documentDetails;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = null;
        try {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doc_layout, parent, false);
            holder = new MyViewHolder(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(DocumentAdapter.MyViewHolder holder, int position) {
        try {
            M_Files mFiles = documentDetails.get(position);
            holder.setData(mFiles, position);
            holder.setLinster();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return documentDetails.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imagethumbnail;
        TextView txtfileName,txtFileSize;
        int position;
        M_Files mFiles;

        public MyViewHolder(View itemView) {
            super(itemView);

            imagethumbnail = (ImageView) itemView.findViewById(R.id.file_iv);
            txtfileName = (TextView) itemView.findViewById(R.id.file_name_tv);
            txtFileSize = (TextView) itemView.findViewById(R.id.file_size_tv);
        }

        public void setData(M_Files mFiles, int position) {

            /** Display Image Folder Name **/
            txtfileName.setText(mFiles.getFilesTitle());
            txtFileSize.setText(Formatter.formatShortFileSize(context, Long.parseLong(mFiles.getFilesSize())));


            imagethumbnail.setImageResource(mFiles.getTypeDrawable());

            this.position = position;
            this.mFiles = mFiles;

        }

        public void setLinster() {
//            imagethumbnail.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    try {
//                        Intent intent = new Intent(context, ImageListGallery.class);
//                        intent.putExtra("BUCKETNAME", model.getAbsolutePathOfImageFolder());
//                        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
//                        context.startActivity(intent);
//                        ((Activity) context).finish();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });

        }
    }
}
