package com.example.softbunch.cameraandgallery.utils;

import com.example.softbunch.cameraandgallery.contributer.upLoadImageFile;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by softbunch on 12/20/16.
 */
public interface APIService {


    @Multipart
    @POST("/miisecretory/API_audio_file_upload.php")
    Call<upLoadImageFile> getUploadFile(
            @Part MultipartBody.Part image,
            @Part("phone_number") RequestBody name,
            @Part("ext") RequestBody ext
    );

}
