package com.example.softbunch.cameraandgallery.contributer;

import com.google.gson.annotations.SerializedName;

/**
 * Created by softbunch on 12/20/16.
 */

public class upLoadImageFile {

    @SerializedName("upload")
    ImageListObject listObject;

    public ImageListObject getListObject() {
        return listObject;
    }
}
