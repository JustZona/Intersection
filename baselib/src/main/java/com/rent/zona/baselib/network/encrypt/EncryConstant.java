package com.rent.zona.baselib.network.encrypt;

public class EncryConstant {
    public static final String PREFIX_ENCTRY="prefix_enctry_";//需要加密的参数  key加上此前缀
//    public static final String WHOLE_JSON_PARAMS="whole_json_params_";//所有参数弄实体传输 加上此参数key  value随便传
    public static final String JSON_PARAMS="json_params_";//某个参数作为json传输  key加上此前缀 只有当所有参数作为实体传输时 实体中的某个参数为json对象时  才需要加
    public static final String JSON_PARAMS_ENCTRY="json_params_enctry_";//上同 但同时对这个json实体加密

}
