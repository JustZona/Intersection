package org.devio.takephoto.uitl;

import android.net.Uri;
import android.os.Environment;

import org.devio.takephoto.app.TakePhoto;
import org.devio.takephoto.compress.CompressConfig;
import org.devio.takephoto.model.CropOptions;
import org.devio.takephoto.model.LubanOptions;
import org.devio.takephoto.model.TakePhotoOptions;

import java.io.File;

public class TakePhotoConfig {
    public static void configTakePhotoOption(TakePhoto takePhoto) {
        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        builder.setWithOwnGallery(true);//ture使用自带的相册功能 即multipleimageselect相册  否则 系统的
        builder.setCorrectImage(true);//纠正拍照的照片旋转角度：
        takePhoto.setTakePhotoOptions(builder.create());

    }

    /**
     * 不压缩
     * @param takePhoto
     */
    public static void configNoCompress(TakePhoto takePhoto) {
        takePhoto.onEnableCompress(null, false);//不压缩
    }

    /**
     * 压缩 保存源文件，不使用自身压缩机制，展示压缩进度条，大小不超过150kb，宽度高度不超过800像素
     * @param takePhoto
     */
    public static void configCompress(TakePhoto takePhoto) {
        configCompress(takePhoto, true, false, true, 153600, 800, 800);
    }

    /**
     * @param takePhoto
     * @param enableRawFile      是否保存源文件
     * @param useCompressWithOwn 是否使用自身的压缩机制
     * @param showProgressBar    是否展示压缩进度条
     * @param maxSize            压缩后最大的文件大小 单位 B
     * @param width              压缩后宽度最大限制 单位像素
     * @param height             压缩后高度最大限制 单位像素
     */
    public static void configCompress(TakePhoto takePhoto, boolean enableRawFile, boolean useCompressWithOwn, boolean showProgressBar, int maxSize, int width, int height) {

        CompressConfig config;
        if (useCompressWithOwn) {
            config = new CompressConfig.Builder().setMaxSize(maxSize)
                    .setMaxPixel(width >= height ? width : height)
                    .enableReserveRaw(enableRawFile)
                    .create();
        } else {
            LubanOptions option = new LubanOptions.Builder().setMaxHeight(height).setMaxWidth(width).setMaxSize(maxSize).create();
            config = CompressConfig.ofLuban(option);
            config.enableReserveRaw(enableRawFile);
        }
        takePhoto.onEnableCompress(config, showProgressBar);
    }

    /**
     * 裁剪配置
     * @param isScale true 按 width/height比例方式裁剪  false 初始化width  height的大小裁剪
     * @param width
     * @param height
     * @return
     */
    public static CropOptions getCropOptions(boolean isScale, int width, int height) {

        CropOptions.Builder builder = new CropOptions.Builder();
        if (isScale) {
            builder.setAspectX(width).setAspectY(height);
        } else {
            builder.setOutputX(width).setOutputY(height);
        }
        builder.setWithOwnCrop(true);//是否使用自身 裁剪  否则使用第三方裁剪
        return builder.create();
    }

    /**
     * 从相册选择图片
     *
     * @param limit 最大选择张数
     */
    public static void picBySelect(TakePhoto takePhoto, int limit) {
        TakePhotoConfig.configCompress(takePhoto);//压缩配置
        TakePhotoConfig.configTakePhotoOption(takePhoto);//相册属性配置
        takePhoto.onPickMultiple(limit);//不经过裁剪
//        takePhoto.onPickMultipleWithCrop(limit, TakePhotoConfig.getCropOptions(true,600,600));//带裁剪
    }

    /**
     * 从相册选择一张图片
     *
     * @param
     */
    public static void picBySelect(TakePhoto takePhoto,int width,int height) {
        TakePhotoConfig.configCompress(takePhoto);//压缩配置
        TakePhotoConfig.configTakePhotoOption(takePhoto);//相册属性配置
//        takePhoto.onPickMultiple(1);//不经过裁剪
        takePhoto.onPickMultipleWithCrop(1, TakePhotoConfig.getCropOptions(true,width,height));//带裁剪
    }

    /**
     * 拍照
     */
    public static void picByTake(TakePhoto takePhoto) {
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        Uri imageUri = Uri.fromFile(file);
        TakePhotoConfig.configCompress(takePhoto);//压缩配置
        TakePhotoConfig.configTakePhotoOption(takePhoto);//相册属性配置
        takePhoto.onPickFromCapture(imageUri);
//        takePhoto.onPickFromCaptureWithCrop(imageUri, TakePhotoConfig.getCropOptions(true,600,600));//可裁剪
    }
}
