package com.example.softbunch.cameraandgallery.docsfile;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.example.softbunch.cameraandgallery.R;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.MediaColumns.DATA;
import static android.provider.MediaStore.MediaColumns.DATE_ADDED;
import static android.provider.MediaStore.MediaColumns.MIME_TYPE;
import static android.provider.MediaStore.MediaColumns.SIZE;
import static android.provider.MediaStore.MediaColumns.TITLE;

/**
 * Created by softbunch on 11/29/16.
 */

public class DocumentFolder extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<M_Files> documentsDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_photo_activity);

        recyclerView = (RecyclerView) findViewById(R.id.photo_folder_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        documentsDetails = new ArrayList<>();

        try {
            /** Load Image Folder **/
            new AsyncFilesLoad().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class AsyncFilesLoad extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                getDocsFiles();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                DocumentAdapter  documentAdapter = new DocumentAdapter(DocumentFolder.this, documentsDetails);
                recyclerView.setAdapter(documentAdapter);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public List<M_Files> getDocsFiles() {
        Uri uri;
        Cursor cursor;
        ArrayList<M_Files> listOfAllDocs = new ArrayList<>();

        String[] selectionArgs = new String[]{".pdf", ".ppt", ".pptx", ".xlsx", ".xls", ".doc", ".docx", ".txt"};

        String[] PROJECTION_BUCKET = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Files.FileColumns.MIME_TYPE,
                MediaStore.Files.FileColumns.SIZE,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Files.FileColumns.TITLE
        };

        cursor = getContentResolver().query(MediaStore.Files.getContentUri("external"),
                PROJECTION_BUCKET,
                null,
                null,
                MediaStore.Files.FileColumns.DATE_ADDED + " DESC");

        if (cursor.moveToFirst()) {
            String filesId;
            String filesData;
            String filesMimeType;
            String filesSize;
            String filesDate;
            String filesTitle;


            int fileIdColumn = cursor.getColumnIndex(_ID);
            int fileData = cursor.getColumnIndex(DATA);
            int fileMimeType = cursor.getColumnIndex(MIME_TYPE);
            int fileSize = cursor.getColumnIndex(SIZE);
            int fileDate = cursor.getColumnIndex(DATE_ADDED);
            int fileTitle = cursor.getColumnIndex(TITLE);


            do {
                // Get the field values
                filesId = cursor.getString(fileIdColumn);
                filesData = cursor.getString(fileData);
                filesMimeType = cursor.getString(fileMimeType);
                filesSize = cursor.getString(fileSize);
                filesDate = cursor.getString(fileDate);
                filesTitle = cursor.getString(fileTitle);


                if (filesData != null && contains(selectionArgs, filesData)) {
                    M_Files mFiles = new M_Files();

                    if (filesMimeType != null && !TextUtils.isEmpty(filesMimeType))
                        mFiles.setFilesMimeType(filesMimeType);
                    else {
                        mFiles.setFilesMimeType("");
                    }


                    mFiles.setFilesId(filesId);
                    mFiles.setFilesData(filesData);
                    mFiles.setFilesSize(filesSize);
                    mFiles.setFilesDate(filesDate);
                    mFiles.setFilesTitle(filesTitle);

                    /** Add Folder in list **/
                    listOfAllDocs.add(mFiles);
                }


            } while (cursor.moveToNext());
        }
        documentsDetails =listOfAllDocs;

        return listOfAllDocs;
    }

    boolean contains(String[] types, String path) {
        for (String string : types) {
            if (path.endsWith(string))
                return true;
        }
        return false;
    }
}
