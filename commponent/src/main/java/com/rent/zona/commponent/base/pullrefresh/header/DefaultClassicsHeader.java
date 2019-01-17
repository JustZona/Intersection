package com.rent.zona.commponent.base.pullrefresh.header;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.scwang.smartrefresh.layout.header.ClassicsHeader;

public class DefaultClassicsHeader extends ClassicsHeader {
    public DefaultClassicsHeader(Context context) {
        super(context);
    }

    public DefaultClassicsHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DefaultClassicsHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public long getLastUpdeTime(){
        if (mLastTime == null ) {
            return  mShared.getLong(KEY_LAST_UPDATE_TIME, System.currentTimeMillis());
        }

        return mLastTime.getTime();
    }
}
