package com.rent.zona.baselib.network.interceptor;

import android.content.Context;
import android.content.Entity;
import android.text.TextUtils;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.rent.zona.baselib.configs.LibConfigs;
import com.rent.zona.baselib.log.LibLogger;
import com.rent.zona.baselib.network.CommonData;
import com.rent.zona.baselib.network.encrypt.EncryConstant;
import com.rent.zona.baselib.network.encrypt.EncryptParam;
import com.rent.zona.baselib.network.encrypt.RSAUtil;
import com.rent.zona.baselib.network.service.ServiceFactory;
import com.rent.zona.baselib.utils.DeviceUtil;
import com.rent.zona.baselib.utils.GsonFactory;
import com.rent.zona.baselib.utils.MD5;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

public class RequestSignInterceptor implements Interceptor {
    private static final String TAG = "OkHttp";
    private static boolean DEBUG = LibConfigs.DEBUG_LOG;

    public static HashMap<String, String> getExtraParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("appid",ServiceFactory.APP_ID);
//        if (!TextUtils.isEmpty(CommonData.getToken())&& !TextUtils.isEmpty(CommonData.getUserid())) {
//            params.put("auth", CommonData.getUserid() + "_" + CommonData.getToken());
//            params.put("timestamp",System.currentTimeMillis()+"");
//            params.put("nonc",new Random(1000000).nextInt()+"");
//            params.put("userId",CommonData.getUserid());
//        }
        return params;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        Request signedRequest = null;
        if (shouldInterceptRequest(request)) {
            if (DEBUG) {
                LibLogger.d(TAG, "--> Interceptor");
            }
            RequestBody body = signRequestBody(request);
            signedRequest = request.newBuilder()
                    .header("Content-Length", String.valueOf(body.contentLength()))
                    .header("User-Agent", "android")
                    .method(request.method(), body)
                    .build();

            if (signedRequest != null) {

//                if (DEBUG) {
//                    Buffer buffer = new Buffer();
//                    signedRequest.body().writeTo(buffer);
//                    String requestBody = buffer.readUtf8();
//                    LibLogger.d(TAG, "signedRequest body " + requestBody);
//
//                    Headers headers = signedRequest.headers();
//                    for (int i = 0, count = headers.size(); i < count; i++) {
//                        LibLogger.d(TAG, headers.name(i) + ": " + headers.value(i));
//                    }
//                    buffer.close();
//                }
                request = signedRequest;
            }
            if (DEBUG) {
                LibLogger.d(TAG, "<-- Interceptor");
            }
        }


//     Response response;
//        try {
//            response = chain.proceed(request);
//        } catch (IOException e) {
//            throw e;
//        }
//        ResponseBody body = response.body();
//        MediaType contentType = null;
//        InputStream responseStream = null;
//        if (body != null) {
//            contentType = body.contentType();
//            responseStream = body.byteStream();
//        }
//        response = response.newBuilder()
//                .body(ResponseBody.create(contentType,"{\"status\":\"y\",\"info\":\"登录密码错误\",\"data\":NULL}"))
//                .build();
//        return response;
        return chain.proceed(request);
    }

    private boolean shouldInterceptRequest(Request request) {
        URL baseUrl;
        try {
            baseUrl = new URL(ServiceFactory.BASE_URL);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        String scheme = request.url().scheme();
        String host = request.url().host();
        String method = request.method();
        String contentType = request.header("Content-Type");
        int port = request.url().port();
        boolean intercept = baseUrl.getProtocol().equalsIgnoreCase(scheme)
                && baseUrl.getHost().equalsIgnoreCase(host)
                && (baseUrl.getPort() == -1 ? baseUrl.getDefaultPort() : baseUrl.getPort()) == port
                && "POST".equalsIgnoreCase(method)
                && "application/x-www-form-urlencoded".equalsIgnoreCase(contentType);
        if (!intercept && DEBUG) {
            LibLogger.d(TAG, String.format("No Intercept %s  %s Content-Type: %s", request.url().toString(), method, contentType));
        }
        return intercept;
    }

    private RequestBody signRequestBody(Request request) throws IOException {

        HashMap<String, String> params = getExtraParams();
        FormBody.Builder builder = new FormBody.Builder();

        Buffer buffer = new Buffer();
        request.body().writeTo(buffer);
        String s = buffer.readUtf8();
        buffer.close();
        String[] kvs = s.split("&");
        for (String kv : kvs) {
            String[] split = kv.split("=");
            String v = "";
            if (split.length == 2) {
                v = split[1];
            }
            v = URLDecoder.decode(v, "UTF-8");
            params.put(split[0], v);

        }
        params.put("sign", getSign(params));
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!ServiceFactory.EXCLUDE_FIELD.contains(entry.getKey())) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        return builder.build();
    }

    /**
     * 生成签名sign
     */

    public static String getSign(Map<String, String> map) {
        StringBuffer sb = new StringBuffer();
        //签名步骤一：参数按字典序排序
        //Map<String, String> parameterMap = new HashMap<String, String>();

        List<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String, String>>(map.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
            //升序排序
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }

        });
        for (int i = 0; i < list.size(); i++) {
            Map.Entry<String, String> entry = list.get(i);
            String key1 = entry.getKey();
            String value = entry.getValue();
            if (null != value && !"".equals(value) && !"0".equals(value)&& !"sign".equals(key1) && !"key".equals(key1)) {
                sb.append(key1 + "=" + value + "&");
            }

        }

        //拼接成StringA
        sb.deleteCharAt(sb.length() - 1);
        sb.append(ServiceFactory.APP_KEY);//StringA最后加上key
        String sign = getMessageDigest(sb.toString().getBytes()).toUpperCase();//进行MD5加密 然后转成大写
        return sign;

    }
    /**
     * MD5加密
     *
     * @param buffer
     * @return
     */
    public static final String getMessageDigest(byte[] buffer) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(buffer);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
}