package com.example.softbunch.cameraandgallery.image;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.softbunch.cameraandgallery.R;
import com.example.softbunch.cameraandgallery.adapter.ImageFolderAdapter;
import com.example.softbunch.cameraandgallery.model.M_ImageFolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by softbunch on 7/14/16.
 */
public class ImageFolder extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView txt_empty;
    private static final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 123;
    ArrayList<M_ImageFolder> galleryImageFolder;
    ProgressDialog progressDialog;
    com.example.softbunch.cameraandgallery.adapter.ImageFolderAdapter ImageFolderAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_photo_activity);

        galleryImageFolder = new ArrayList<>();

        txt_empty = (TextView) findViewById(R.id.txt_empty);

        recyclerView = (RecyclerView) findViewById(R.id.photo_folder_list);
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        /** Asyntask **/
        try {
            /** Load Image Folder **/
            new AsynImageLoad().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Load Image Folder From Mobile
     **/
    private class AsynImageLoad extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                progressDialog = new ProgressDialog(ImageFolder.this);
                progressDialog.setMessage("Loading Images Please wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                getImageFolderDetails();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                progressDialog.dismiss();
                if (galleryImageFolder.size() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    txt_empty.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    txt_empty.setVisibility(View.GONE);
                    ImageFolderAdapter = new ImageFolderAdapter(ImageFolder.this, galleryImageFolder);
                    recyclerView.setAdapter(ImageFolderAdapter);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public List<M_ImageFolder> getImageFolderDetails() {
        Uri uri;
        Cursor cursor;
        ArrayList<M_ImageFolder> listOfAllImages = new ArrayList<M_ImageFolder>();
        try {
            String[] PROJECTION_BUCKET = {
                    MediaStore.Images.ImageColumns.BUCKET_ID,
                    MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.ImageColumns.DATE_TAKEN,
                    MediaStore.Images.ImageColumns.DATA};

            String BUCKET_GROUP_BY = "1) GROUP BY 1,(2";
            String BUCKET_ORDER_BY = "MAX(datetaken) DESC";
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

            cursor = getContentResolver().query(
                    uri,
                    PROJECTION_BUCKET,
                    BUCKET_GROUP_BY,
                    null,
                    BUCKET_ORDER_BY
            );

            if (cursor.moveToFirst()) {
                String bucket;
                String date;
                String data;
                String bucketId;

                int bucketIdColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);
                int bucketColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
                int dateColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
                int dataColumn1 = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                do {
                    // Get the field values
                    bucketId = cursor.getString(bucketIdColumn);
                    bucket = cursor.getString(bucketColumn);
                    date = cursor.getString(dateColumn);
                    data = cursor.getString(dataColumn1);

                    M_ImageFolder m_imageFolder = new M_ImageFolder();
                    m_imageFolder.setAbsolutePathOfImage(data);
                    m_imageFolder.setAbsolutePathOfImageFolder(bucket);
                    m_imageFolder.setBucketId(bucketId);
                    m_imageFolder.setTotalCount(photoCountByAlbum(bucket));

                    /** Add Folder in list **/
                    listOfAllImages.add(m_imageFolder);

                    // Do something with the values.
                    Log.i("ListingImages", " bucket=" + bucket
                            + "  date_taken=" + date
                            + "  _data=" + data);

                } while (cursor.moveToNext());
            }

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        galleryImageFolder = listOfAllImages;
        return listOfAllImages;
    }

    /**
     * Count Images In folder
     **/
    private int photoCountByAlbum(String bucketName) {
        try {
            final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
            String searchParams = null;
            String bucket = bucketName;
            searchParams = "bucket_display_name = \"" + bucket + "\"";

            Cursor mPhotoCursor = getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
                    searchParams, null, orderBy + " DESC");

            if (mPhotoCursor.getCount() > 0) {
                return mPhotoCursor.getCount();
            }
            mPhotoCursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
