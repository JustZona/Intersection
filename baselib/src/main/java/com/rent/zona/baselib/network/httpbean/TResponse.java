package com.rent.zona.baselib.network.httpbean;

import com.google.gson.annotations.SerializedName;


public class TResponse<Data> {

    @SerializedName("status")
    public String status;//状态码  1表示成功  用四位数的数字表示公共异常码  比如1001表示登陆异常
    @SerializedName("info")
    public String msg;//成功或失败提示信息
    @SerializedName("data")
    public Data data;//返回的数据实体 根据接口需求  返回json对象或json列表
//    /**
//     * 服务器时间和手机时间的误差
//     */
//    private static long deltaTime;
//
//    @SerializedName("RefreshTime")
//    public long refreshTime;
//
//
//    @SerializedName("ErrorCode")
//    public int errorCode;
//
//    @SerializedName("Message")
//    public String message;
//
//    @SerializedName("Location")
//    public String location;
//
//    @SerializedName("Data")
//    public Data data;

    public boolean isSuccess() {
        return "y".equals(status);
    }


//    public void setDeltaTime() {
//        if (refreshTime > 0) {
//            TResponse.deltaTime = refreshTime * 1000 - System.currentTimeMillis();
//        }
//    }

//    public static long currentTimeMillis() {
//        return System.currentTimeMillis() + deltaTime;
//    }
}
