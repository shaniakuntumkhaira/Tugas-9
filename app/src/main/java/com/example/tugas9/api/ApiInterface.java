package com.example.tugas9.api;


import com.example.tugas9.model.login.LoginData;
import com.example.tugas9.model.register.RegisterData;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("login.php")
    Call<LoginData> loginResponse(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("register.php")
    Call<RegisterData> RegisterResponse(
            @Field("username") String username,
            @Field("password") String password,
            @Field("name") String name
    );
}
