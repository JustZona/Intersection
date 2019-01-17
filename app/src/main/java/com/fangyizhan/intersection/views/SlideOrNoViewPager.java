package com.fangyizhan.intersection.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class SlideOrNoViewPager extends ViewPager {
    /**
     * 判断是否能够手动滑动.
     */
    private boolean isCanScroll=false;

    public SlideOrNoViewPager(@NonNull Context context) {
        super(context);
    }

    public SlideOrNoViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isCanScroll) {
            return super.onTouchEvent(ev);
        }else {
            return false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isCanScroll) {
            return super.onInterceptTouchEvent(ev);
        }else {
            return false;
        }
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item);
    }
    /**
     * 是否能够手动滑动
     */
    public void setCanScroll(boolean isCanScroll) {
        this.isCanScroll= isCanScroll;
    }
}
