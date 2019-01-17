package com.rent.zona.baselib.network.encrypt;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;

import com.rent.zona.baselib.configs.LibConfigs;
import com.rent.zona.baselib.log.LibLogger;
import com.rent.zona.baselib.preference.BaseLibPrefrence;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class EncryptParam {
    public static final String TAG = "EncryptParam";
    public static final String ClientType_PRIVATE_KEY = "!@#$%^&*()_+{}|:?<>"; // 客户端加密私钥
    public static String timestamp;
    public static String hex;
    public static String key;
    public static String token;

    public static String getKey(Context context) {
        if(TextUtils.isEmpty(key)){
            key= BaseLibPrefrence.getHttpKey(context);
        }
        return key;
    }

    public static String getHex() {
        return hex;
    }

    private static boolean initKey() {
        if (!TextUtils.isEmpty(hex) && !TextUtils.isEmpty(token)) {
//
//            StringBuffer sb = new StringBuffer();
//            for (int i = 1; i < hex.length(); i++) {
//                if (i % 2 == 0) {
//                    sb.append(hex.charAt(i));
//                }
//            }
            int i = 1;
            StringBuilder sb = new StringBuilder();
            for (char c : hex.toCharArray()) {
                if (i % 2 == 0) {
                    sb.append(c);
                }
                i++;
            }
            try {
                String md5EvenHex = EnDecryptHelper.md5LowerCase(sb.toString());
                String md5Token = EnDecryptHelper.md5LowerCase(token + timestamp.substring(timestamp.length() - 4, timestamp.length()));
                key = md5EvenHex.substring(0, 4);//前4位
                key += md5EvenHex.substring(md5EvenHex.length() - 4, md5EvenHex.length());//后4位
                key += md5Token.substring(0, 8);
                return true;

            } catch (NoSuchAlgorithmException e) {
                if (LibConfigs.isDebugLog()) {
                    LibLogger.i(TAG, "hex偶数位md5失败");
                }
                e.printStackTrace();
                return false;
            }

        }
        return false;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        EncryptParam.setToken(token);
        initKey();
    }
    public static String getTimestamp() {
        return timestamp;
    }

    public static void setTimestamp(String timestamp) {
        EncryptParam.setTimestamp(timestamp);
        setHex(timestamp);
    }

    private static void setHex(String timestamp) {
        if (!TextUtils.isEmpty(timestamp)) {
            try {
                hex = EnDecryptHelper.md5LowerCase(timestamp + "houhan.com");
            } catch (NoSuchAlgorithmException e) {
                if (LibConfigs.isDebugLog()) {
                    LibLogger.i(TAG, "协商密码请求参数hex转化失败");
                }
                e.printStackTrace();
            }
        }
    }
    /*
       加密或解密数据的通用方法
       @param srcData
       待处理的数据
       @param key
       公钥或者私钥
       @param mode
       指定是加密还是解密，值为Cipher.ENCRYPT_MODE或者Cipher.DECRYPT_MODE

    */
    private static byte[] processData(byte[] srcData, Key key, int mode){

        //用来保存处理结果
        byte[] resultBytes = null;

        try {

            //构建Cipher对象，需要传入一个字符串，格式必须为"algorithm/mode/padding"或者"algorithm/",意为"算法/加密模式/填充方式"
            Cipher cipher = Cipher.getInstance("RSA/NONE/PKCS1Padding");
            //初始化Cipher，mode指定是加密还是解密，key为公钥或私钥
            cipher.init(mode,key);
            //处理数据
            resultBytes = cipher.doFinal(srcData);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return resultBytes;
    }
    /**
     *  使用公钥加密数据，结果用Base64转码
    */
    public static String encryptDataByPublicKey(byte[] srcData, PublicKey publicKey){

        byte[] resultBytes = processData(srcData,publicKey,Cipher.ENCRYPT_MODE);

        return Base64.encodeToString(resultBytes,Base64.DEFAULT);

    }
    /**
     * 使用私钥解密，返回解码数据
    */
    public static byte[] decryptDataByPrivate(String encryptedData, PrivateKey privateKey){

        byte[] bytes = Base64.decode(encryptedData,Base64.DEFAULT);

        return processData(bytes,privateKey,Cipher.DECRYPT_MODE);
    }

    /**
     *  使用私钥进行解密，解密数据转换为字符串，使用utf-8编码格式
     */
    public static String decryptedToStrByPrivate(String encryptedData, PrivateKey privateKey){
        return new String(decryptDataByPrivate(encryptedData,privateKey));
    }

    /**
     * 使用私钥解密，解密数据转换为字符串，并指定字符集
     */
    public static String decryptedToStrByPrivate(String encryptedData, PrivateKey privateKey,String charset){
        try {

            return new String(decryptDataByPrivate(encryptedData,privateKey),charset);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     *   使用私钥加密，结果用Base64转码
     */

    public static String encryptDataByPrivateKey(byte[] srcData,PrivateKey privateKey){

        byte[] resultBytes = processData(srcData,privateKey,Cipher.ENCRYPT_MODE);

        return Base64.encodeToString(resultBytes,Base64.DEFAULT);
    }
    /**
     * 使用公钥解密，返回解密数据
     */

    public static byte[] decryptDataByPublicKey(String encryptedData,PublicKey publicKey){

        byte[] bytes = Base64.decode(encryptedData,Base64.DEFAULT);

        return processData(bytes,publicKey,Cipher.DECRYPT_MODE);

    }

    /**
     * 使用公钥解密，结果转换为字符串，使用默认字符集utf-8
     */
    public static String decryptedToStrByPublicKey(String encryptedData,PublicKey publicKey){
        return new String(decryptDataByPublicKey(encryptedData,publicKey));
    }


    /**
     * 使用公钥解密，结果转换为字符串，使用指定字符集
     */

    public static String decryptedToStrByPublicKey(String encryptedData,PublicKey publicKey,String charset){
        try {

            return new String(decryptDataByPublicKey(encryptedData,publicKey),charset);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }
}
