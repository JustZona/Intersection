package com.rent.zona.baselib.network.service;



import com.rent.zona.baselib.network.httpbean.TResponse;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface UserService {
    @FormUrlEncoded
    @POST("/apphosting/appLogin/login.action")
    Observable<TResponse<Object>> login(
            @Field("loginPhone") String loginPhone,
            @Field("loginPwd") String loginPwd);
}