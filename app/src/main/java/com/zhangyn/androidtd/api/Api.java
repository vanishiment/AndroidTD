package com.zhangyn.androidtd.api;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Api {

  @POST("tokens")
  @FormUrlEncoded
  @Headers("Auth-Type:Basic")
  Observable<User> login(@Field("account") String account,@Field("password") String password);

  @GET("/user/{uid}")
  Observable<User> user(@Path("uid") long uid);

}
