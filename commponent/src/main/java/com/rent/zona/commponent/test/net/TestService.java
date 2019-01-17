package com.rent.zona.commponent.test.net;

import com.rent.zona.baselib.network.httpbean.TResponse;
import com.rent.zona.baselib.network.service.ServiceFactory;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface TestService {
    @FormUrlEncoded
    @POST("/apphosting/appLogin/login.action")
    Observable<TResponse<Object>> login(
            @Field("loginPhone") String loginPhone,
            @Field("loginPwd") String loginPwd);
    @POST(ServiceFactory.BASE_UPLOAD_URL + "/FileUpload/MendUpload")
    @Multipart
    Observable<TResponse<String>> mendUpload(@Part("myfile\"; filename=\"image.png\" ") RequestBody file);

    @POST(ServiceFactory.BASE_UPLOAD_URL + "/FileUpload/PhotoUrl")
    @Multipart
    Observable<TResponse<String>> PhotoUpload(
            @Part("myfile\"; filename=\"image.png\" ") RequestBody file,
            @Part("userid") String uid
    );

}
