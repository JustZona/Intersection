package com.rent.zona.commponent.views;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class W2HLinearLayout extends LinearLayout{
    public W2HLinearLayout(Context context) {
        super(context);
    }

    public W2HLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public W2HLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public W2HLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

            int modeW = MeasureSpec.getMode(widthMeasureSpec);
            int sizeW = MeasureSpec.getSize(widthMeasureSpec);
            int sizeH = MeasureSpec.getSize(heightMeasureSpec);
            if(modeW== MeasureSpec.EXACTLY){
                sizeH = sizeW ;
            }
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(sizeW, MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(sizeH, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
