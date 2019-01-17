package com.rent.zona.baselib.glide;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;

import com.bumptech.glide.request.RequestOptions;
import com.rent.zona.baselib.glide.progress.OnGlideImageViewListener;
import com.rent.zona.baselib.glide.progress.OnProgressListener;

public class GlideShapeImageView extends ShapeImageView {

    private GlideImageLoader mImageLoader;

    public GlideShapeImageView(Context context) {
        this(context, null);
    }

    public GlideShapeImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GlideShapeImageView(Context context, AttributeSet attrs, int defStyleAttr) {
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

    public GlideShapeImageView load(int resId, RequestOptions options) {
        getImageLoader().load(resId, options);
        return this;
    }

    public GlideShapeImageView load(Uri uri, RequestOptions options) {
        getImageLoader().load(uri, options);
        return this;
    }

    public GlideShapeImageView load(String url, RequestOptions options) {
        getImageLoader().load(url, options);
        return this;
    }

    public GlideShapeImageView loadImage(String url, int placeholderResId) {
        getImageLoader().loadImage(url, placeholderResId);
        return this;
    }

    public GlideShapeImageView loadLocalImage(@DrawableRes int resId, int placeholderResId) {
        getImageLoader().loadLocalImage(resId, placeholderResId);
        return this;
    }

    public GlideShapeImageView loadLocalImage(String localPath, int placeholderResId) {
        getImageLoader().loadLocalImage(localPath, placeholderResId);
        return this;
    }

    public GlideShapeImageView loadCircleImage(String url, int placeholderResId) {
        setShapeType(ShapeType.CIRCLE);
        getImageLoader().loadCircleImage(url, placeholderResId);
        return this;
    }

    public GlideShapeImageView loadLocalCircleImage(int resId, int placeholderResId) {
        setShapeType(ShapeType.CIRCLE);
        getImageLoader().loadLocalCircleImage(resId, placeholderResId);
        return this;
    }

    public GlideShapeImageView loadLocalCircleImage(String localPath, int placeholderResId) {
        setShapeType(ShapeType.CIRCLE);
        getImageLoader().loadLocalCircleImage(localPath, placeholderResId);
        return this;
    }

    public GlideShapeImageView listener(OnGlideImageViewListener listener) {
        getImageLoader().setOnGlideImageViewListener(getImageUrl(), listener);
        return this;
    }

    public GlideShapeImageView listener(OnProgressListener listener) {
        getImageLoader().setOnProgressListener(getImageUrl(), listener);
        return this;
    }
}
