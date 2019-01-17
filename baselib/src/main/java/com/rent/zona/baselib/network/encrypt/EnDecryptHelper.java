package com.rent.zona.baselib.network.encrypt;

import android.text.TextUtils;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @name：加解密
 * @author： liuyun
 * @phone： 15723310658
 * @createTime： 2017/8/8
 * @modifyTime： 2017/8/8
 * @explain：说明
 */


public class EnDecryptHelper {

    /**
     * AES_128_CBC 加密
     * @param content 明文
     * @param key
     * @param initialVector 初始向量
     * @return
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws UnsupportedEncodingException
     * @throws InvalidAlgorithmParameterException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static String encryptAES(String content, String key, String initialVector) throws Exception {


        try {
            byte[] byteContent = content.getBytes("UTF-8");

            // 注意，为了能与 iOS 统一
            // 这里的 key 不可以使用 KeyGenerator、SecureRandom、SecretKey 生成
            byte[] enCodeFormat = key.getBytes();
            SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");

            byte[] initParam = initialVector.getBytes();
            IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);

            // 指定加密的算法、工作模式和填充方式
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

            byte[] encryptedBytes = cipher.doFinal(byteContent);

            return Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
        } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException | NoSuchPaddingException | BadPaddingException | UnsupportedEncodingException | IllegalBlockSizeException e) {
            throw e;
        }
    }



    private static final String IV_STRING = "a6fb84c82ca840e2";//初始向量
    private static final String charset = "UTF-8";
    private static final String KEY="61db18011e434ac3";

    public static String aesEncryptString(String content) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
                return aesEncryptString(content,KEY);
    }
    public static String aesDecryptString(String content) throws Exception {

        return aesDecryptString(content,KEY);
    }

    public static String aesEncryptString(String content, String key) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        if(TextUtils.isEmpty(content) || TextUtils.isEmpty(key)){
            return "";
        }
        byte[] contentBytes = content.getBytes(charset);
        byte[] keyBytes = key.getBytes(charset);
        byte[] encryptedBytes = aesEncryptBytes(contentBytes, keyBytes);
//        return Base64.encodeToString(encryptedBytes,Base64.NO_WRAP);
        return new String(Base64.encode(encryptedBytes, Base64.NO_WRAP), "UTF-8");
    }

    public static String aesDecryptString(String content, String key) throws Exception {

        byte[] encryptedBytes = Base64.decode(content, Base64.DEFAULT);
        byte[] keyBytes = key.getBytes(charset);
        byte[] decryptedBytes = aesDecryptBytes(encryptedBytes, keyBytes);
        return new String(decryptedBytes, charset);
    }

    public static byte[] aesEncryptBytes(byte[] contentBytes, byte[] keyBytes) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        return cipherOperation(contentBytes, keyBytes, Cipher.ENCRYPT_MODE);
    }

    public static byte[] aesDecryptBytes(byte[] contentBytes, byte[] keyBytes) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        return cipherOperation(contentBytes, keyBytes, Cipher.DECRYPT_MODE);
    }

    private static byte[] cipherOperation(byte[] contentBytes, byte[] keyBytes, int mode) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");

        byte[] initParam = IV_STRING.getBytes(charset);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(mode, secretKey, ivParameterSpec);

        return cipher.doFinal(contentBytes);
    }


    public static String md5(String string) throws NoSuchAlgorithmException {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        md5 = MessageDigest.getInstance("MD5");
        byte[] bytes = md5.digest(string.getBytes());
        String result = "";
        for (byte b : bytes) {
            String temp = Integer.toHexString(b & 0xff);
            if (temp.length() == 1) {
                temp = "0" + temp;
            }
            result += temp;
        }
        return result;
    }
    public static String md5UpperCase(String string) throws NoSuchAlgorithmException {
        String result=md5(string);
        if(!TextUtils.isEmpty(result)){
            return result.toUpperCase();
        }
        return null;
    }
    public static String md5LowerCase(String string) throws NoSuchAlgorithmException {
        String result=md5(string);
        if(!TextUtils.isEmpty(result)){
            return result.toLowerCase();
        }
        return null;
    }







    PublicKey publicKey;
    PrivateKey privateKey;
    /*
       产生密钥对
       @param keyLength
       密钥长度，小于1024长度的密钥已经被证实是不安全的，通常设置为1024或者2048，建议2048
    */
    public static KeyPair generateRSAKeyPair(int keyLength){
        KeyPair keyPair = null;
        try {

            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            //设置密钥长度
            keyPairGenerator.initialize(keyLength);
            //产生密钥对
            keyPair = keyPairGenerator.generateKeyPair();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return keyPair;
    }
    public void initKey(){
        KeyPair keyPair = generateRSAKeyPair(2048);
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();

    }

}
