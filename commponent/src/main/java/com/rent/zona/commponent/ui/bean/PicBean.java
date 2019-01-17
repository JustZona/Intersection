package com.rent.zona.commponent.ui.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class PicBean implements Parcelable{
    private String imageUrl;
    private String imageUrlThumbnail;

    public PicBean(String imageUrl, String imageUrlThumbnail) {
        this.imageUrl = imageUrl;
        this.imageUrlThumbnail = imageUrlThumbnail;
    }

    protected PicBean(Parcel in) {
        imageUrl = in.readString();
        imageUrlThumbnail = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageUrl);
        dest.writeString(imageUrlThumbnail);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PicBean> CREATOR = new Creator<PicBean>() {
        @Override
        public PicBean createFromParcel(Parcel in) {
            return new PicBean(in);
        }

        @Override
        public PicBean[] newArray(int size) {
            return new PicBean[size];
        }
    };

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrlThumbnail() {
        return imageUrlThumbnail;
    }

    public void setImageUrlThumbnail(String imageUrlThumbnail) {
        this.imageUrlThumbnail = imageUrlThumbnail;
    }

    @Override
    public String toString() {
        return  imageUrl;
    }
}
