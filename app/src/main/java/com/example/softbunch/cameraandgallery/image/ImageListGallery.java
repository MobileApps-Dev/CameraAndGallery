package com.example.softbunch.cameraandgallery.image;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.softbunch.cameraandgallery.adapter.ImageGalleryAdapter;
import com.example.softbunch.cameraandgallery.model.M_imageList;
import com.example.softbunch.cameraandgallery.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by softbunch on 25/11/15.
 */
public class ImageListGallery extends AppCompatActivity {
    ImageGalleryAdapter imageListAdapter;
    ProgressDialog progressDialog;
    ArrayList<M_imageList> galleryImagesDetails;
    String bucketName;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_photo_activity);

        galleryImagesDetails = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.photo_folder_list);
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        try {
            bucketName = getIntent().getExtras().getString("BUCKETNAME");
        } catch (Exception e) {
            e.printStackTrace();
        }
        /** Asyntask **/
        try {
            /** Display Image from Image Folder **/
            new AsynImageLoad().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public List<M_imageList> getVideoDetails() {
        Cursor cursor = null;
        ArrayList<M_imageList> imageLists = new ArrayList<M_imageList>();
        System.out.println("Pohot " + MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        String searchParams = null;
        String bucket = bucketName;
        searchParams = "bucket_display_name = \"" + bucket + "\"";

        try {
            cursor = getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null,
                    searchParams,
                    null,
                    orderBy + " DESC");

            String[] proj = {
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.DISPLAY_NAME,
            };

            int imageId = cursor.getColumnIndex(MediaStore.Images.Media._ID);
            int imageData = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            int imageName = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);

            cursor.moveToFirst();
            int count = cursor.getCount();
            do {

                Bitmap bitmap = null;
                String imageid = cursor.getString(imageId);
                String imagedata = cursor.getString(imageData);
                String imagename = cursor.getString(imageName);

                M_imageList m_imageListnew = new M_imageList();
                m_imageListnew.setimageData(imagedata);
                m_imageListnew.setimageName(imagename);
                imageLists.add(m_imageListnew);
            } while (cursor.moveToNext());

        } catch (Exception e) {
            e.printStackTrace();
        }
        galleryImagesDetails = imageLists;
        return imageLists;
    }


    /**
     * Asyn Task
     **/
    private class AsynImageLoad extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ImageListGallery.this);
            progressDialog.setMessage("Loading Images Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            getVideoDetails();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            imageListAdapter = new ImageGalleryAdapter(ImageListGallery.this, galleryImagesDetails);
            recyclerView.setAdapter(imageListAdapter);
            progressDialog.dismiss();
        }
    }
}
