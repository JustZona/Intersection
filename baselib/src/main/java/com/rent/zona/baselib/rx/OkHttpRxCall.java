package com.rent.zona.baselib.rx;

import com.rent.zona.baselib.network.HttpClient;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpRxCall implements RxCall<Response> {

    final private Call mCall;
    final private Request mRequest;

    public OkHttpRxCall(Request request) {
        mRequest = request;
        OkHttpClient okHttpClient = HttpClient.defaultOkHttpClient().newBuilder().build();

        mCall = okHttpClient.newCall(request);
    }
    public OkHttpRxCall(OkHttpClient okHttpClient, Request request) {
        mRequest = request;
        mCall = okHttpClient.newCall(request);
    }
    @Override
    public Response execute() throws IOException {
        return mCall.execute();
    }

    @Override
    public void cancel() {
        mCall.cancel();
    }

    @Override
    public RxCall<Response> clone() {
        return new OkHttpRxCall(mRequest);
    }
}