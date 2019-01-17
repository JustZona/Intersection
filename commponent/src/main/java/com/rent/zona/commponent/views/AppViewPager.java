package com.rent.zona.commponent.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @name：
 * @author： liuyun
 * @createTime： 2017/7/3
 * @modifyTime： 2017/7/3
 * @explain：说明
 */


public class AppViewPager extends ViewPager {
    private boolean mViewTouchMode = false;

    public AppViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setViewTouchMode(boolean b) {
        if (b && !isFakeDragging()) {
            // System.out.println("beginFakeDrag!");
            beginFakeDrag();
        } else if (!b && isFakeDragging()) {
            // System.out.println("endFakeDrag!");
            endFakeDrag();
        }
        mViewTouchMode = b;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // System.out.println("mViewTouchMode is " + mViewTouchMode);
        if (mViewTouchMode) {
            return false;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean arrowScroll(int direction) {
        if (mViewTouchMode) return false;
        if (direction != FOCUS_LEFT && direction != FOCUS_RIGHT) return false;
        return super.arrowScroll(direction);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }


}
