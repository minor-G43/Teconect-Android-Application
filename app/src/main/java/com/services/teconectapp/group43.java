package com.services.teconectapp;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface group43
{
     //@GET("info")
     //Call<Info> getInfo();

     @POST("auth/register")
     Call<ResponseBack> createInfo(@Body Info info);

     @POST("auth/login/{token}")
     Call<login_response> user_Register(@Body login_request loginRequest,@Path("token") String token);

     @POST("auth/login")
     Call<login_response> user_login(@Body login_request loginRequest);

     @POST("auth/forgotpassword")
     Call<forget_password_response> change_password(@Body email_for_forget_password entering_email);

     @POST("auth/fetchprofile/{token}")
     Call<profile_response> getProfile(@Path("token") String token);

     @POST("auth/createproject/{token}")
     Call<create_project_response> createProject(@Body create_project createProject,@Path("token") String token);

     @POST("auth/fetchproject/{token}")
     Call<fetch_project_response> fetchProject(@Path("token") String token);

     @POST("auth/userlist/{token}")
     Call<userlist_response> fetchUserList(@Path("token") String token);
}
