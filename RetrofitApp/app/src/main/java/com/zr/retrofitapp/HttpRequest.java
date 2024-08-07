package com.zr.retrofitapp;

import static com.zr.retrofitapp.ApiServices.BASE_URL;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpRequest{
  private ApiServices services;

  public HttpRequest(){
    services=new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServices.class);
  }

  public ApiServices callAPI(){
    return services;
  }
}
