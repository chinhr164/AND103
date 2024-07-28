package com.zr.retrofitapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiServices{

  public static String BASE_URL="http://10.0.2.2:3000/api";

  @GET("/student")
  Call<List<Student>> getAll();  //Lấy danh sách dữ liệu

  @POST("/student")
  Call<List<Student>> postData(@Body Student student); //Thêm đối tượng mới

  @PUT("/student/{id}")
  Call<List<Student>> putData(@Path("id") String id, @Body Student student); //Sửa 1 đối tượng có sẵn

  @DELETE("/student/{id}")
  Call<List<Student>> deleteData(@Path("id") String id); //Xoá 1 đối tượng
}
