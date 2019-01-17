package com.rent.zona.commponent.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class ScreenTabView extends LinearLayout {
    public ScreenTabView(Context context) {
        super(context);
    }

    public ScreenTabView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ScreenTabView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ScreenTabView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

}
