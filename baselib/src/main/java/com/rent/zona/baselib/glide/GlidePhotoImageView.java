package com.rent.zona.baselib.glide;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;

import com.bumptech.glide.request.RequestOptions;
import com.rent.zona.baselib.glide.progress.OnGlideImageViewListener;
import com.rent.zona.baselib.glide.progress.OnProgressListener;
import com.rent.zona.baselib.view.photoview.PhotoView;

public class GlidePhotoImageView extends PhotoView {
    private GlideImageLoader mImageLoader;

    public GlidePhotoImageView(Context context) {
        this(context, null);
    }

    public GlidePhotoImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GlidePhotoImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mImageLoader = GlideImageLoader.create(this);
    }

    public GlideImageLoader getImageLoader() {
        if (mImageLoader == null) {
            mImageLoader = GlideImageLoader.create(this);
        }
        return mImageLoader;
    }

    public String getImageUrl() {
        return getImageLoader().getImageUrl();
    }

    public RequestOptions requestOptions(int placeholderResId) {
        return getImageLoader().requestOptions(placeholderResId);
    }

    public RequestOptions circleRequestOptions(int placeholderResId) {
        return getImageLoader().circleRequestOptions(placeholderResId);
    }

    public GlidePhotoImageView load(int resId, RequestOptions options) {
        getImageLoader().load(resId, options);
        return this;
    }

    public GlidePhotoImageView load(Uri uri, RequestOptions options) {
        getImageLoader().load(uri, options);
        return this;
    }

    public GlidePhotoImageView load(String url, RequestOptions options) {
        getImageLoader().load(url, options);
        return this;
    }

    public GlidePhotoImageView loadImage(String url, int placeholderResId) {
        getImageLoader().loadImage(url, placeholderResId);
        return this;
    }

    public GlidePhotoImageView loadLocalImage(@DrawableRes int resId, int placeholderResId) {
        getImageLoader().loadLocalImage(resId, placeholderResId);
        return this;
    }

    public GlidePhotoImageView loadLocalImage(String localPath, int placeholderResId) {
        getImageLoader().loadLocalImage(localPath, placeholderResId);
        return this;
    }

    public GlidePhotoImageView loadCircleImage(String url, int placeholderResId) {
        getImageLoader().loadCircleImage(url, placeholderResId);
        return this;
    }

    public GlidePhotoImageView loadLocalCircleImage(int resId, int placeholderResId) {
        getImageLoader().loadLocalCircleImage(resId, placeholderResId);
        return this;
    }

    public GlidePhotoImageView loadLocalCircleImage(String localPath, int placeholderResId) {
        getImageLoader().loadLocalCircleImage(localPath, placeholderResId);
        return this;
    }

    public GlidePhotoImageView listener(OnGlideImageViewListener listener) {
        getImageLoader().setOnGlideImageViewListener(getImageUrl(), listener);
        return this;
    }

    public GlidePhotoImageView listener(OnProgressListener listener) {
        getImageLoader().setOnProgressListener(getImageUrl(), listener);
        return this;
    }
}
