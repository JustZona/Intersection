package com.rent.zona.baselib.network;

import android.util.Log;

import com.rent.zona.baselib.configs.LibConfigs;
import com.rent.zona.baselib.network.interceptor.HttpLoggingInterceptor;
import com.rent.zona.baselib.network.progress.UIProgressResponseListener;
import com.rent.zona.baselib.rx.ObservableHelper;
import com.rent.zona.baselib.rx.OkHttpRxCall;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;


public class HttpClient {
    private static volatile OkHttpClient DEFAULT_OKHTTPCLIENT;

    private HttpClient() {

    }

    public static OkHttpClient defaultOkHttpClient() {
        if (DEFAULT_OKHTTPCLIENT == null) {
            synchronized (HttpClient.class) {
                if (DEFAULT_OKHTTPCLIENT == null) {
//                    DEFAULT_OKHTTPCLIENT = new OkHttpClient();
//                    DEFAULT_OKHTTPCLIENT.setConnectTimeout(30, TimeUnit.SECONDS);


                    HttpLoggingInterceptor.Level level = LibConfigs.isDebugLog()
                            ? HttpLoggingInterceptor.Level.BODY
                            : HttpLoggingInterceptor.Level.NONE;
                    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                    loggingInterceptor.setLevel(level);
//                    DEFAULT_OKHTTPCLIENT.networkInterceptors().add(loggingInterceptor);
                    DEFAULT_OKHTTPCLIENT=new OkHttpClient.Builder()
                            .connectTimeout(30, TimeUnit.SECONDS)
                            .addNetworkInterceptor(loggingInterceptor)
                            .build();
                }
            }
        }
        return DEFAULT_OKHTTPCLIENT;
    }


    public static Observable<String> download(String url, File dest) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        OkHttpRxCall rxCall = new OkHttpRxCall(request);

        return ObservableHelper.create(rxCall).flatMap(response -> {

            BufferedSink sink = null;
            BufferedSource source = null;
            try {
                source = response.body().source();
                sink = Okio.buffer(Okio.sink(dest));
                source.readAll(sink);
                String path = dest.getAbsolutePath();
                if (LibConfigs.isDebugLog()) {
                    Log.d("OkHttp", "file download success, source url: " + url + " dest path: " + path);
                }
                return Observable.just(path);
            } catch (IOException e) {
                dest.delete();
                return Observable.error(e);
            } finally {
                Util.closeQuietly(sink);
                Util.closeQuietly(source);
            }
        });
    }
    public static Observable<String> download(String url, File dest, UIProgressResponseListener progressResponseListener) {
        System.out.println("下载链接---》"+url);
        Request request = new Request.Builder()
                .url(url)
                .build();
        OkHttpClient okHttpClient = HttpClient.defaultOkHttpClient().newBuilder().build();
//       ProgressHelper.addProgressResponseListener(okHttpClient,progressResponseListener);

        OkHttpRxCall rxCall = new OkHttpRxCall(okHttpClient,request);

        return ObservableHelper.create(rxCall).flatMap(response -> {
            if(dest.exists()){
                dest.delete();
            }
            boolean success=false;
            BufferedSink sink = null;
            BufferedSource source = null;
            try {
                dest.createNewFile();
                source = response.body().source();
//               System.out.println("body--->"+response.body().string());
                sink = Okio.buffer(Okio.sink(dest));
                long contentLength=response.body().contentLength();
                long totalBytesRead = 0L;
                long read;
                while ((read=source.read(sink.buffer(),1024))!=-1){
                    totalBytesRead+=read;
                    progressResponseListener.onResponseProgress(totalBytesRead, contentLength, false);
                }
                source.readAll(sink);
                String path = dest.getAbsolutePath();

                if (LibConfigs.isDebugLog()) {
                    Log.d("OkHttp", "file download success, source url: " + url + " dest path: " + path);
                }
                success=true;
                return Observable.just(path);
            } catch (IOException e) {
                dest.delete();
                return Observable.error(e);
            } finally {
                Util.closeQuietly(sink);
                Util.closeQuietly(source);
                if(success){
                    progressResponseListener.onResponseProgress(100, 100, true);
                }
            }
        });
    }

}
