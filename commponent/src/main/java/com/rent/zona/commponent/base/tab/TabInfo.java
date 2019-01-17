package com.rent.zona.commponent.base.tab;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

import java.lang.reflect.Constructor;

/**
 * @name：
 * @author： liuyun
 * @phone： 15723310658
 * @createTime： 2017/7/3
 * @modifyTime： 2017/7/3
 * @explain：说明
 */


public class TabInfo implements Parcelable {

    public static final Creator<TabInfo> CREATOR = new Creator<TabInfo>() {
        public TabInfo createFromParcel(Parcel p) {
            return new TabInfo(p);
        }

        public TabInfo[] newArray(int size) {
            return new TabInfo[size];
        }
    };
    private int mId;
    private int mIcon;
    private String mName = null;
    private Fragment mFragment = null;
    private boolean mNotifyChange = false;
    @SuppressWarnings("rawtypes")
    private Class mFragmentClass = null;
    private Bundle mArguments = null;
    private boolean mFirstLoad = true;
    private boolean hasTip=false;//是否有tip 有就显示一个红色的点 没有不显示
    private int tipCount=0; //tip的个数 大于0 就是显示  否则不予显示 hasTip和tipCount是完全两个独立属性 互相不干扰

    @SuppressWarnings("rawtypes")
    public TabInfo(int id, String name, Class clazz) {
        this(id, name, 0, clazz);
    }

//    @SuppressWarnings("rawtypes")
//    public TabInfo(int id, String name, boolean hasTips, Class clazz) {
//        this(id, name, 0, clazz);
//        this.mHasTips = hasTips;
//    }

    @SuppressWarnings("rawtypes")
    public TabInfo(int id, String name, int iconid, Class clazz) {
        super();

        this.mName = name;
        this.mId = id;
        mIcon = iconid;
        mFragmentClass = clazz;
    }
    public TabInfo(Parcel p) {
        this.mId = p.readInt();
        this.mName = p.readString();
        this.mIcon = p.readInt();
        this.mNotifyChange = p.readInt() == 1;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public int getIcon() {
        return mIcon;
    }

    public void setIcon(int iconId) {
        mIcon = iconId;
    }

    @SuppressWarnings({
            "rawtypes", "unchecked"
    })
    public Fragment createFragment() {
        if (mFragment == null) {
            Constructor constructor;
            try {
                constructor = mFragmentClass.getConstructor();
                mFragment = (Fragment) constructor.newInstance();
                if (mArguments != null) {
                    mFragment.setArguments(mArguments);
                    mArguments = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mFragment;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel p, int flags) {
        p.writeInt(mId);
        p.writeString(mName);
        p.writeInt(mIcon);
        p.writeInt(mNotifyChange ? 1 : 0);
    }

    public Fragment getFragment() {
        return mFragment;
    }

    public void setFragment(Fragment f) {
        mFragment = f;
    }

//    public boolean getHasTips() {
//        return mHasTips;
//    }
//
//    public void setHasTips(boolean has) {
//        mHasTips = has;
//    }

    public TabInfo setArguments(Bundle args) {
        mArguments = args;
        return this;
    }

    public boolean isFirstLoad() {
        return mFirstLoad;
    }

    public void setFirstLoad(boolean firstLoad) {
        if (firstLoad != this.mFirstLoad) {
            this.mFirstLoad = firstLoad;
        }
    }

    public boolean isHasTip() {
        return hasTip;
    }

    public void setHasTip(boolean hasTip) {
        this.hasTip = hasTip;
    }

    public int getTipCount() {
        return tipCount;
    }

    public void setTipCount(int tipCount) {
        this.tipCount = tipCount;
    }
}
