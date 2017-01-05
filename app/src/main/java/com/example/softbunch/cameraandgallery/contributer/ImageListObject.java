package com.example.softbunch.cameraandgallery.contributer;

import com.google.gson.annotations.SerializedName;

/**
 * Created by softbunch on 12/20/16.
 */
public class ImageListObject {

    @SerializedName("status")
    String status;

    @SerializedName("message")
    String message;

    @SerializedName("url")
    String url;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getUrl() {
        return url;
    }
}
