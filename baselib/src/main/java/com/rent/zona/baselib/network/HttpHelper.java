package com.rent.zona.baselib.network;

import android.content.Context;
import android.text.TextUtils;

import com.rent.zona.baselib.configs.LibConfigs;
import com.rent.zona.baselib.network.interceptor.RequestSignInterceptor;
import com.rent.zona.baselib.network.service.ServiceFactory;
import com.rent.zona.baselib.rx.ObservableHelper;
import com.rent.zona.baselib.rx.OkHttpRxCall;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class HttpHelper {

//    public static Observable<Response> upload(String url, File file, final MediaType contentType, Map<String, String> apiParams) {
//
//        Request request = getRequest(url, file, contentType, apiParams);
//
//        return ObservableHelper.create(new OkHttpRxCall(request));
//    }

//    private static Request getRequest(String url, File file, MediaType contentType, Map<String, String> apiParams) {
//        if (!file.exists()) {
//            throw new IllegalArgumentException(file.getAbsolutePath() + " is not exists");
//        }
//        if (file.isDirectory()) {
//            throw new IllegalArgumentException(file.getAbsolutePath() + " is a  Directory");
//        }
//        Context context= LibConfigs.getAppContext();
//
////        MultipartBuilder builder = new MultipartBuilder()
////                .type(MultipartBuilder.FORM)
////                .addFormDataPart("File", file.getName(), RequestBody.create(contentType, file));
//        MultipartBody.Builder builder=new MultipartBody.Builder();
//        builder.setType(MultipartBody.FORM).addFormDataPart("File", file.getName(), RequestBody.create(contentType, file));
//        HashMap<String, Object> params = RequestSignInterceptor.getExtraParams();
//        if (apiParams != null) {
//            for (Map.Entry<String, String> entry : apiParams.entrySet()) {
//                String key = entry.getKey();
//                String value = entry.getValue();
//                if (TextUtils.isEmpty(key)) continue;
//                if (value == null) value = "";
//                params.put(key, value);
//            }
//        }
//
//        String signdata = RequestSignInterceptor.signParams(null, params);
//
//        for (Map.Entry<String, Object> entry : params.entrySet()) {
//            if (!ServiceFactory.EXCLUDE_FIELD.contains(entry.getKey())) {
//                builder.addFormDataPart(entry.getKey(), entry.getValue().toString());
//            }
//        }
//
//        builder.addFormDataPart("signdata", signdata);
//
//        return new Request.Builder()
//                .header("User-Agent", "android")
//                .url(url)
//                .post(builder.build())
//                .build();
//    }

}
