package com.rent.zona.baselib.network.service;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.rent.zona.baselib.configs.LibConfigs;
import com.rent.zona.baselib.log.LibLogger;
import com.rent.zona.baselib.network.HttpClient;
import com.rent.zona.baselib.network.interceptor.RequestSignInterceptor;
import com.rent.zona.baselib.utils.GsonFactory;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class ServiceFactory {
    public static final String BASE_URL;//基本请求的域名
    public static final String BASE_UPLOAD_URL="http://192.168.1.233:8080";//上传文件的域名
    public static final String EMPTY_POST_VOID_FIELD = "empty_post_void_filed";
    /**
     * 平台授权接口可用的appid
     */
    public static final String APP_ID = "muhxi9gpsgkowhrd";
    /**
     * 秘钥
     */
    public static final String APP_KEY = "cacfcc014d8b6606";
    public static final List<String> EXCLUDE_FIELD = Arrays.asList(EMPTY_POST_VOID_FIELD);

    private static ServiceFactory sInstance;

    private Retrofit mRetrofit;
    private UserService mUserService;

    static {
        switch (LibConfigs.getOnlineServer()) {
            case SERVER_ENV_TEST: {//区域网测
                BASE_URL = "http://www.fangonezhan.com";//https://023sec.com
            }
            break;
            case SERVER_ENV_INSIDE: {//内网测 或公司内部人员测
                BASE_URL = "http://dev.fangonezhan.com";
            }
            break;
            default: {//发布版本
                BASE_URL = "https://www.fangonezhan.com";
            }
            break;
        }

    }

    public static ServiceFactory getInstance() {
        if (sInstance == null) {
            synchronized (ServiceFactory.class) {
                sInstance = new ServiceFactory();
            }
        }
        return sInstance;
    }

    private ServiceFactory() {
        createSignRetrofit();
    }

    private void createSignRetrofit() {
        RequestSignInterceptor requestSignInterceptor = new RequestSignInterceptor();
        OkHttpClient.Builder builder= HttpClient.defaultOkHttpClient().newBuilder();
        builder.addNetworkInterceptor(requestSignInterceptor);
        if (LibConfigs.isAddFacebookStetho()) {
            try {
                builder.addNetworkInterceptor(new StethoInterceptor());
            } catch (Exception e) {
                LibLogger.w("OkHttp", "", e);
            }
        }

        OkHttpClient okHttpClient =builder.build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
//                .addConverterFactory(AppGsonConverterFactory.create(GsonFactory.getDefault()))
                .addConverterFactory(GsonConverterFactory.create(GsonFactory.getDefault()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

    }
    public UserService userService() {
        if (mUserService == null) {
            mUserService = mRetrofit.create(UserService.class);
        }
        return mUserService;
    }

    public <T> T forService(Class<T> serviceClass){
        return mRetrofit.create(serviceClass);
    }

    public static String getLinkUrl(String apiUrl, Map<String,String> params){
        String url=BASE_URL+apiUrl;
        // 添加url参数
        if (params != null) {
            Iterator<String> it = params.keySet().iterator();
            StringBuffer sb = null;
            while (it.hasNext()) {
                String key = it.next();
                String value = params.get(key);
                if (sb == null) {
                    sb = new StringBuffer();
                    sb.append("?");
                } else {
                    sb.append("&");
                }
                sb.append(key);
                sb.append("=");
                sb.append(value);
            }
            url += sb.toString();
        }
        return url;
    }



}
