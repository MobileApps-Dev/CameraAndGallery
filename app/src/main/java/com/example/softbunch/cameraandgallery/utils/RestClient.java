package com.example.softbunch.cameraandgallery.utils;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by softbunch on 12/20/16.
 */

public class RestClient {

    Retrofit retrofit;
    APIService service;

    public RestClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        // set your desired log level
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://miitel.com")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        service = retrofit.create(APIService.class);
    }

    public APIService getService() {
        return service;
    }


}
