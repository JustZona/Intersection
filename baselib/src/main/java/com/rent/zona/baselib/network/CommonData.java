package com.rent.zona.baselib.network;

public class CommonData {
    private static boolean login=false;
    private static String userid;
    private static String token;

    public static boolean isLogin() {
        return login;
    }

    public static void setLogin(boolean login) {
        CommonData.login = login;
    }

    public static String getUserid() {
        return userid;
    }

    public static void setUserid(String userid) {
        CommonData.userid = userid;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        CommonData.token = token;
    }
}
