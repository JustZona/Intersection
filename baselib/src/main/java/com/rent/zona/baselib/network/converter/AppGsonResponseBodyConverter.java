package com.rent.zona.baselib.network.converter;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.rent.zona.baselib.configs.LibConfigs;
import com.rent.zona.baselib.log.LibLogger;
import com.rent.zona.baselib.network.encrypt.EnDecryptHelper;
import com.rent.zona.baselib.network.encrypt.EncryptParam;

import java.io.IOException;
import java.io.StringReader;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * @name：
 * @author： liuyun
 * @phone： 15723310658
 * @createTime： 2017/8/12
 * @modifyTime： 2017/8/12
 * @explain：说明
 */


public class AppGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    AppGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
//        JsonReader jsonReader = gson.newJsonReader(value.charStream());

//        try {
//            return adapter.read(jsonReader);
//        } finally {
//            value.close();
//        }
        String response=value.string();
        JsonObject returnData = new JsonParser().parse(response).getAsJsonObject();
        if(returnData.has("encrypt") && returnData.get("encrypt").getAsBoolean() && returnData.has("data")&& !returnData.get("data").isJsonObject()){
            String data=returnData.get("data").getAsString();
            try {
                if(LibConfigs.isDebugLog()){
                    LibLogger.i("解密前",data);
                }
                data= EnDecryptHelper.aesDecryptString(data, EncryptParam.getKey(LibConfigs.getAppContext()));
                returnData.remove("data");
                returnData.add("data", new JsonParser().parse(data));
                if(LibConfigs.isDebugLog()){
                    LibLogger.i("解密后",data);
                }
            } catch (Exception e) {//
                if(LibConfigs.isDebugLog()) {
                    LibLogger.e("解密异常","返回网络数据解密异常");
                    LibLogger.e("原始数据",response.toString());
                    e.printStackTrace();
                }
                throw new IOException(e);
            }
        }
        if(returnData.has("code") && returnData.get("code").getAsInt()!=0){
            returnData.remove("data");
        }
        LibLogger.di("返回数据",returnData.toString());
        try {
            return adapter.read(gson.newJsonReader(new StringReader(returnData.toString())));
        } finally {
            value.close();
        }

    }
}
