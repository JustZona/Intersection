package com.rent.zona.baselib.network.encrypt;

import android.util.Base64;

import com.rent.zona.baselib.configs.LibConfigs;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSAUtil {
    //构建Cipher实例时所传入的的字符串，默认为"RSA/NONE/PKCS1Padding"
//    private static String sTransform = "RSA/NONE/PKCS1Padding";
//    private static String sTransform = "RSA/None/PKCS1Padding";
//    private static String sTransform = "RSA/ECB/NoPadding";
    private static String sTransform = "RSA/ECB/PKCS1Padding";

    //进行Base64转码时的flag设置，默认为Base64.DEFAULT
    private static int sBase64Mode = Base64.DEFAULT;

    private static PublicKey mPublicKey;
    private static PrivateKey mSignPrivateKey;

    //初始化方法，设置参数
    public static void init(String transform,int base64Mode){
        sTransform = transform;
        sBase64Mode = base64Mode;
    }
    static {
//        KeyPair keyPair=generateRSAKeyPair(2048);
//        mPublicKey=keyPair.getPublic();
//
//        mSignPrivateKey=keyPair.getPrivate();
        //字符串公钥，可以直接保存在客户端
        String PUBLIC_KEY_STR = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDWnb1t7lOeAlRcJTDoa44jR0OlrhrpJCI6YJtXmwIgzLYkbSGexRGmoH85JONMqBCN5bVuY0aW463i/oGDOAmz1EiOMgqRPzBZYGTSi+1VghJKlF+DmN3vU8jp7YAZQNny21GPYXiRMMZBpzqsv2GRoVw3K/vd7qo8Pj5yJJ0weQIDAQAB";

        //字符串密钥，通常保存在服务器，这里为了方便演示，直接保存在客户端
         String PRIVATE_KEY_STR = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALqGYE7RGXHfjWvZF7tpaTO624dmfDDNiMB7LYnhYTaSym3l7x3VOCr8Ove0dNv5Vh0qbrZBVPFOgWQpOr26wvlJ4VkWMlB5a8UHkcDn9oMjCaSi/owWCXEQtTOsqjTpgXaUPhDcUT6E9uPFAgLkuhnUgCTgcQ1K2Khx8l9QFFmDAgMBAAECgYEAhdCrvT/D+m8APXQk034/TC5tGkmLJVP/HOAB/p4jqQxMj+Va09NJFYWZJGZhRAMwJYJwpO5f6zKAH4R4sO8Qup2Az+iAgMgTp16yAcKO1Egva9w4LQIM+eTo9a62vYQWewonzWtt7taWqXiW/EU3CmOBXk+MYYrG9wO9NIysNFECQQD4lyjKvqPv4YwQMgMHY7LtHxKyCrhbGAj6gvO8J3Zs1SpMzXgx5eaAnXE744ZyxAbtcw/r4gpWrOBmSY1y3o9JAkEAwBWiAMdyXIfQP89hVWwGcO3XmVhES62D44AMP3r5GgSc0M78JafnwgXpkFpQlJgagCUcZhtjz6keCDTLTFzGawJBAJZB3+E0fCjZnuyHHjeQNq2HE6jUO/yruXmaP7Lh833zFt9XbqhSexIQmEoHy9OA02MhImWEupULZ5PML5hYGCkCQGqqWKkIGFiLzrfwiENwVm9VlFkCLAK+WzP1QlfqCPRKxW4q0thyUf9rWxUfIgoyIN0yPn/xC88gQZ4a+x3vkvsCQQDgFWWc44UcBjy2X//3IcCYCHCXmF6wn/s1k5VRVAzlmUfq01ck8tjiHKxlGN3ZdYA7OlqaYXdiP/6rUfoakd7w";
        mPublicKey = RSAUtil.keyStrToPublicKey(PUBLIC_KEY_STR);
        //获取私钥
        mSignPrivateKey = RSAUtil.keyStrToPrivate(PRIVATE_KEY_STR);
    }

    public static PrivateKey getSignPrivateKey() {
        return mSignPrivateKey;
    }

    /**
     * 产生密钥对
     * @param keyLength
     * 密钥长度，小于1024长度的密钥已经被证实是不安全的，通常设置为1024或者2048，建议2048
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

    /**
     * 加密或解密数据的通用方法
     * @param srcData
     * 待处理的数据
     * @param key
     * 公钥或者私钥
     * @param mode
     * 指定是加密还是解密，值为Cipher.ENCRYPT_MODE或者Cipher.DECRYPT_MODE
     */
    private static byte[] processData(byte[] srcData, Key key, int mode){

        //用来保存处理结果
        byte[] resultBytes = null;

        try {

            //获取Cipher实例
            Cipher cipher = Cipher.getInstance(sTransform);
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
     * 使用公钥加密数据，结果用Base64转码
     */
    public static String encryptDataByPublicKey(byte[] srcData, PublicKey publicKey){

        byte[] resultBytes = processData(srcData,publicKey,Cipher.ENCRYPT_MODE);

        return Base64.encodeToString(resultBytes,sBase64Mode);

    }
    /**
     * 使用公钥加密数据，结果用Base64转码
     */
    public static String encryptDataByPublicKey(byte[] srcData){

        byte[] resultBytes = processData(srcData,mPublicKey,Cipher.ENCRYPT_MODE);

        return Base64.encodeToString(resultBytes,sBase64Mode);

    }
    public static String encryptDataByPublicKey(String srcData){

        byte[] resultBytes = processData(srcData.getBytes(),mPublicKey,Cipher.ENCRYPT_MODE);
        return Base64.encodeToString(resultBytes,sBase64Mode);

    }
    /**
     * 使用私钥解密，返回解码数据
     */
    public static byte[] decryptDataByPrivate(String encryptedData, PrivateKey privateKey){

        byte[] bytes = Base64.decode(encryptedData,sBase64Mode);

        return processData(bytes,privateKey,Cipher.DECRYPT_MODE);
    }

    /**
     * 使用私钥进行解密，解密数据转换为字符串，使用utf-8编码格式
     */
    public static String decryptedToStrByPrivate(String encryptedData, PrivateKey privateKey){
        return new String(decryptDataByPrivate(encryptedData,privateKey));
    }
    public static String decryptedToStrByPrivate(String encryptedData){
        return new String(decryptDataByPrivate(encryptedData,mSignPrivateKey));
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
     * 使用私钥加密，结果用Base64转码
     */
    public static String encryptDataByPrivateKey(byte[] srcData,PrivateKey privateKey){

        byte[] resultBytes = processData(srcData,privateKey,Cipher.ENCRYPT_MODE);

        return Base64.encodeToString(resultBytes,sBase64Mode);
    }
    public static String encryptDataByPrivateKey(byte[] srcData){

        byte[] resultBytes = processData(srcData,mSignPrivateKey,Cipher.ENCRYPT_MODE);

        return Base64.encodeToString(resultBytes,sBase64Mode);
    }
    public static String encryptDataByPrivateKey(String srcData){

        byte[] resultBytes = processData(srcData.getBytes(),mSignPrivateKey,Cipher.ENCRYPT_MODE);

        return Base64.encodeToString(resultBytes,sBase64Mode);
    }
    public static String encryptDataByPrivateKey(String srcData,PrivateKey privateKey){

        byte[] resultBytes = processData(srcData.getBytes(),privateKey,Cipher.ENCRYPT_MODE);

        return Base64.encodeToString(resultBytes,sBase64Mode);
    }
    /**
     * 使用公钥解密，返回解密数据
     */

    public static byte[] decryptDataByPublicKey(String encryptedData,PublicKey publicKey){

        byte[] bytes = Base64.decode(encryptedData,sBase64Mode);

        return processData(bytes,publicKey,Cipher.DECRYPT_MODE);

    }

    /**
     * 使用公钥解密，结果转换为字符串，使用默认字符集utf-8
     */
    public static String decryptedToStrByPublicKey(String encryptedData,PublicKey publicKey){
        return new String(decryptDataByPublicKey(encryptedData,publicKey));
    }
    public static String decryptedToStrByPublicKey(String encryptedData){
        return new String(decryptDataByPublicKey(encryptedData,mPublicKey));
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




    /**
     * 将字符串形式的公钥转换为公钥对象
     */

    public static PublicKey keyStrToPublicKey(String publicKeyStr){

        PublicKey publicKey = null;

        byte[] keyBytes = Base64.decode(publicKeyStr,sBase64Mode);

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            publicKey = keyFactory.generatePublic(keySpec);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return publicKey;

    }

    /**
     * 将字符串形式的私钥，转换为私钥对象
     */

    public static PrivateKey keyStrToPrivate(String privateKeyStr){

        PrivateKey privateKey = null;

        byte[] keyBytes = Base64.decode(privateKeyStr,sBase64Mode);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);

        try {

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            privateKey = keyFactory.generatePrivate(keySpec);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return privateKey;

    }

    public static String sign(String sign, PrivateKey privateKey) {
        Signature signature = null;
        try {
            signature = Signature.getInstance("SHA1withRSA");
            signature.initSign(privateKey);//设置私钥
            signature.update(sign.getBytes("UTF-8"));
//            return new String(signature.sign(),"UTF-8");
            return  new String(Base64.encode(signature.sign(), Base64.NO_WRAP), "UTF-8");
        } catch (NoSuchAlgorithmException e) {
            if(LibConfigs.isDebugLog()){
                e.printStackTrace();
            }
        } catch (InvalidKeyException e) {
            if(LibConfigs.isDebugLog()){
                e.printStackTrace();
            }
        } catch (SignatureException e) {
            if(LibConfigs.isDebugLog()){
                e.printStackTrace();
            }
        } catch (UnsupportedEncodingException e) {
            if(LibConfigs.isDebugLog()){
                e.printStackTrace();
            }
        }
        return null;
    }
}
