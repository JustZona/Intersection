package com.rent.zona.commponent.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2018/1/22.
 */

public class AutoTextSizeView extends android.support.v7.widget.AppCompatTextView {

    private boolean isUseAutoSize=true;
    private float textSize;
    public AutoTextSizeView(Context context) {
        super(context);
    }

    public AutoTextSizeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoTextSizeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public boolean isUseAutoSize() {
        return isUseAutoSize;
    }

    public void setUseAutoSize(boolean useAutoSize) {
        isUseAutoSize = useAutoSize;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(isUseAutoSize) {
            int wide = MeasureSpec.getSize(widthMeasureSpec);
            int wideMode = MeasureSpec.getMode(widthMeasureSpec);
            if (wideMode == MeasureSpec.AT_MOST || wideMode == MeasureSpec.EXACTLY) {
                if(textSize>0){
                    TextPaint textPaint = getPaint();
                    textPaint.setTextSize(textSize);
                }
                changeTextSize(wide);
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    private void changeTextSize(int wide){
        TextPaint textPaint = getPaint();
        if(textSize==0){
            textSize=textPaint.getTextSize();
        }
        float textPaintWidth ;
        if(!TextUtils.isEmpty(getText().toString())){
            textPaintWidth = textPaint.measureText(getText().toString());
        }else if(!TextUtils.isEmpty(getHint())){
            textPaintWidth = textPaint.measureText(getHint().toString());
        }else{
            return;
        }
        float needSize=getPaddingLeft()+getPaddingRight()+textPaintWidth;
        if(needSize>wide){
            textPaint.setTextSize(textPaint.getTextSize()-2);
//            setTextSize(TypedValue.COMPLEX_UNIT_SP, DensityUtils.px2sp(getContext(),getTextSize()-1));
            changeTextSize(wide);
        }


    }

}
