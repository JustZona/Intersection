package com.rent.zona.commponent.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;

/**
 * @name：
 * @author： liuyun
 * @createTime： 2017/7/31
 * @modifyTime： 2017/7/31
 * @explain：说明
 */


public class AutoSizeWideImageView extends ImageView {
    private float downX;
    private float downY;
    private long downTime;
    private AutoSizeHeightImageView.OnRegionClickListener mListener;

    public AutoSizeWideImageView(Context context) {
        this(context, null);
    }

    public AutoSizeWideImageView(Context context, AttributeSet attr) {
        super(context, attr);
    }

    public void setOnRegionClickListener(AutoSizeHeightImageView.OnRegionClickListener listener) {
        mListener = listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable drawable = getDrawable();
        if (drawable != null) {
            int intrinsicWidth = drawable.getIntrinsicWidth();
            int intrinsicHeight = drawable.getIntrinsicHeight();
            int modeW = MeasureSpec.getMode(widthMeasureSpec);
            int modeH = MeasureSpec.getMode(heightMeasureSpec);
            int sizeW = MeasureSpec.getSize(widthMeasureSpec);
            int sizeH = MeasureSpec.getSize(heightMeasureSpec);

            if(modeH== MeasureSpec.EXACTLY){
                sizeW= sizeH * intrinsicWidth / intrinsicHeight;
            }

//            if (modeW != MeasureSpec.EXACTLY) {
//                sizeW = getResources().getDisplayMetrics().widthPixels;
//            }
//            sizeH = sizeW * intrinsicHeight / intrinsicWidth;
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(sizeW, MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(sizeH, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (!isEnabled() || isClickable() || isLongClickable()) {
            super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downTime = SystemClock.uptimeMillis();
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                float upX = event.getX();
                float upY = event.getY();
                long upTime = SystemClock.uptimeMillis();
                float deltaX = upX - downX;
                float deltaY = upY - downY;
                int touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
                if ((deltaX * deltaX + deltaY * deltaY) < touchSlop * touchSlop) {
                    if (upTime - downTime < 500) {
                        onClick();
                        if (mListener != null) {
                            mListener.onRegionClick(this, (downX + upX) / 2, (downY + upY) / 2);
                        }
                    } else {
                        onLongClick();
                    }
                }
                break;
        }
        return true;
    }

    private void onClick() {

    }

    private void onLongClick() {

    }

    public interface OnRegionClickListener {

        void onRegionClick(View view, float x, float y);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
