package com.rent.zona.commponent.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.widget.Button;

import com.rent.zona.commponent.R;
import com.rent.zona.commponent.utils.ResourceUtils;


public class CommonButton extends Button {
    int radius;
    int enableColor;
    int unEnableColor;
    int strokeColor;
    int unEnableStrokeColor;
    int pressColor;
    int strokeWidth;

    public CommonButton(Context context) {
        this(context, null, 0);
    }

    public CommonButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CommonButton, defStyleAttr, 0);

        Drawable drawable = a.getDrawable(R.styleable.CommonButton_button_click_selector);
        if (drawable != null) {
            setBackgroundDrawable(drawable);
        } else {
            radius = a.getDimensionPixelSize(R.styleable.CommonButton_button_radius, 0);
            enableColor = a.getColor(R.styleable.CommonButton_button_enable_solid_color, -1);
            unEnableColor = a.getColor(R.styleable.CommonButton_button_unenable_solid_color, -1);
            strokeColor = a.getColor(R.styleable.CommonButton_button_stroke_color, -1);
            unEnableStrokeColor = a.getColor(R.styleable.CommonButton_button_unenable_stroke_color, -1);
            pressColor = a.getColor(R.styleable.CommonButton_button_press_color, -1);
            strokeWidth = a.getDimensionPixelSize(R.styleable.CommonButton_button_stroke_width, 0);

            if (pressColor == -1) {
                pressColor = ResourceUtils.adjustAlpha(enableColor, 0.6f);
            }
            createBg();
//            setBackgroundDrawable(createSelector(enableColor, unEnableColor, pressColor, strokeColor, strokeWidth, radius));
        }
        a.recycle();
    }

    private StateListDrawable createSelector(int enableColor, int unEnableColor, int unEnableStrokeColorColor, int pressColor, int strokeColor, int strokeWidth, int radius) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{-android.R.attr.state_enabled},
                ResourceUtils.createShape(unEnableColor, unEnableStrokeColorColor, strokeWidth, radius));
        drawable.addState(new int[]{android.R.attr.state_selected},
                ResourceUtils.createShape(pressColor, strokeColor, strokeWidth, radius));
        drawable.addState(new int[]{android.R.attr.state_pressed},
                ResourceUtils.createShape(pressColor, strokeColor, strokeWidth, radius));
        drawable.addState(new int[]{android.R.attr.state_focused},
                ResourceUtils.createShape(pressColor, strokeColor, strokeWidth, radius));
        drawable.addState(new int[]{android.R.attr.state_enabled},
                ResourceUtils.createShape(enableColor, strokeColor, strokeWidth, radius));
        return drawable;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable[] drawables = getCompoundDrawables();
        if (drawables != null) {
            Drawable drawableLeft = drawables[0];
            Drawable drawableRight = drawables[2];
            if (drawableLeft != null || drawableRight != null) {
                float textWidth = getPaint().measureText(getText().toString());
                int drawablePadding = getCompoundDrawablePadding();
                int drawableWidth = 0;
                if (drawableLeft != null)
                    drawableWidth = drawableLeft.getIntrinsicWidth();
                else if (drawableRight != null) {
                    drawableWidth = drawableRight.getIntrinsicWidth();
                }
                float bodyWidth = textWidth + drawableWidth + drawablePadding + getPaddingLeft() + getPaddingRight();
                canvas.translate((getWidth() - bodyWidth) / 2, 0);
            }
        }

        super.onDraw(canvas);

    }


    public void setRadius(int radius) {
        this.radius = radius;
        createBg();
    }

    public void setEnableColor(int enableColor) {
        this.enableColor = enableColor;
        createBg();
    }

    public void setUnEnableColor(int unEnableColor) {
        this.unEnableColor = unEnableColor;
        createBg();
    }

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
        createBg();
    }

    public void setPressColor(int pressColor) {
        if(pressColor>0) {
            this.pressColor = pressColor;
        }else {
            this.pressColor = ResourceUtils.adjustAlpha(enableColor, 0.6f);
        }
        createBg();
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
        createBg();
    }
    private void createBg(){
        setBackgroundDrawable(createSelector(enableColor, unEnableColor,unEnableStrokeColor, pressColor, strokeColor, strokeWidth, radius));
    }
}
