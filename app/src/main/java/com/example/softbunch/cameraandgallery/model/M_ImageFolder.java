package com.example.softbunch.cameraandgallery.model;

/**
 * Created by softbunch on 7/14/16.
 */
public class M_ImageFolder {
    private String absolutePathOfImage;
    private String absolutePathOfImageFolder;
    private String bucketId;
    private int totalCount;



    public String getAbsolutePathOfImage() {
        return absolutePathOfImage;
    }

    public void setAbsolutePathOfImage(String absolutePathOfImage) {
        this.absolutePathOfImage = absolutePathOfImage;
    }

    public String getAbsolutePathOfImageFolder() {
        return absolutePathOfImageFolder;
    }

    public void setAbsolutePathOfImageFolder(String absolutePathOfImageFolder) {
        this.absolutePathOfImageFolder = absolutePathOfImageFolder;
    }

    public String getBucketId() {
        return bucketId;
    }

    public void setBucketId(String bucketId) {
        this.bucketId = bucketId;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
