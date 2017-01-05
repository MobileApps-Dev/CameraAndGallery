package com.example.softbunch.cameraandgallery.model;

import android.graphics.Bitmap;

/**
 * Created by softbunch on 24/11/15.
 */
public class M_imageList {

    private String imageName;
    private String imageData;
    private Bitmap bitmap;


    public String getimageName() {
        return imageName;
    }

    public void setimageName(String imageName) {
        this.imageName = imageName;
    }

    public String getimageData() {
        return imageData;
    }

    public void setimageData(String imageData) {
        this.imageData = imageData;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }


}
