package com.rent.zona.commponent.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import com.rent.zona.commponent.R;



public class MorseIndicator extends View implements ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private int mNormalSize, mSelectSize, mDistance;
    private int mNormalColor, mSelectColor, mVirtualColor;
    private Drawable mNormalDrawable, mSelectDrawable, mVirtualDrawable;
    private int mGravity;
    private float mRadius;
    private Paint mNormalPaint, mSelectPaint, mVirtualPaint;
    private RectF mRectF;
    private float mPositionOffset;
    private int mPosition;
    private boolean mAutoCarouse;
    private int mReallyCount;

    public MorseIndicator(Context context) {
        this(context, null);
    }

    public MorseIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MorseIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (isInEditMode()) {
            return;
        }
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MorseIndicator, defStyleAttr, 0);

        mNormalColor = typedArray.getColor(R.styleable.MorseIndicator_normalColor, Color.WHITE);
        mSelectColor = typedArray.getColor(R.styleable.MorseIndicator_selectColor, Color.WHITE);
        mVirtualColor = typedArray.getColor(R.styleable.MorseIndicator_virtualColor, Color.GRAY);

        mNormalSize = typedArray.getDimensionPixelOffset(R.styleable.MorseIndicator_normalSize, 6);
        mSelectSize = typedArray.getDimensionPixelSize(R.styleable.MorseIndicator_selectSize, 24);
        mDistance = typedArray.getDimensionPixelOffset(R.styleable.MorseIndicator_distanceSize, 30);

        mNormalDrawable = typedArray.getDrawable(R.styleable.MorseIndicator_normalDrawable);
        mSelectDrawable = typedArray.getDrawable(R.styleable.MorseIndicator_selectDrawable);
        mVirtualDrawable = typedArray.getDrawable(R.styleable.MorseIndicator_virtualDrawable);

        mGravity = typedArray.getInteger(R.styleable.MorseIndicator_android_gravity, Gravity.CENTER);

        typedArray.recycle();

        mRadius = mNormalSize / 2f;

        mNormalPaint = new Paint();
        mNormalPaint.setColor(mNormalColor);

        mSelectPaint = new Paint();
        mSelectPaint.setColor(mSelectColor);

        mVirtualPaint = new Paint();
        mVirtualPaint.setColor(mVirtualColor);

        mRectF = new RectF();
    }

    public void setViewPager(ViewPager viewPager) {
        if (viewPager != mViewPager) {
            mViewPager = viewPager;
        }
        if (viewPager != null) {
            if (viewPager.getAdapter() == null) {
                throw new IllegalStateException("ViewPager does not have adapter instance.");
            }
            mViewPager.addOnPageChangeListener(this);
            mPosition = mViewPager.getCurrentItem();

        } else {
            mViewPager.removeOnPageChangeListener(this);
        }
        invalidate();
    }

    public void setReallyCount(int reallyCount) {
        mReallyCount = reallyCount;
        mAutoCarouse = true;
    }

    public void setCurrentItem(int item) {
        mPosition = item;
        mViewPager.setCurrentItem(item);
        if (mAutoCarouse) {
            mPosition = item % mReallyCount;
        }
    }

    private int getCount() {
        if (mAutoCarouse) {
            return mReallyCount;
        }
        return mViewPager.getAdapter().getCount();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int modeW = MeasureSpec.getMode(widthMeasureSpec);
        int modeH = MeasureSpec.getMode(heightMeasureSpec);
        int sizeW = MeasureSpec.getSize(widthMeasureSpec);
        int sizeH = MeasureSpec.getSize(heightMeasureSpec);
        if (modeW == MeasureSpec.AT_MOST) {
            sizeW = getCount() * mDistance + getPaddingLeft() + getPaddingRight();
        }
        if (modeH == MeasureSpec.AT_MOST) {
            sizeH = mNormalSize + getPaddingTop() + getPaddingBottom();
        }
        setMeasuredDimension(sizeW, sizeH);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mViewPager != null) {
            int count = getCount();
            if (count > 1) {
                if (mNormalDrawable != null && mSelectDrawable != null) {
                    int centerX = getWidth() / 2;
                    int centerY = getHeight() / 2;
                    int normalWidth = mNormalDrawable.getIntrinsicWidth();
                    int normalHeight = mNormalDrawable.getIntrinsicHeight();
                    int selectWidth = mSelectDrawable.getIntrinsicWidth();
                    int selectHeight = mSelectDrawable.getIntrinsicHeight();
                    int x = centerX - mDistance * count / 2;
                    int y = centerY - normalHeight / 2;
                    for (int i = 0; i < count; i++) {
                        mNormalDrawable.setBounds(x + mDistance / 2 - normalWidth / 2, y, x + mDistance / 2 + normalWidth / 2, y + normalHeight);
                        mNormalDrawable.draw(canvas);
                        x += mDistance;
                    }
                    x = ((int) (centerX - mDistance * count / 2 + mDistance * (mPosition + mPositionOffset)));
                    y = centerY - selectHeight / 2;
                    mSelectDrawable.setBounds(x + mDistance / 2 - selectWidth / 2, y, x + mDistance / 2 + selectWidth / 2, y + selectHeight);
                    mSelectDrawable.draw(canvas);
                } else {
                    float center = getWidth() / 2f;
                    float x = center - mDistance * count / 2f;
                    float y = (getHeight() - mNormalSize) / 2f;
                    for (int i = 0; i < count; i++) {
                        mRectF.left = x + mDistance / 2f - mRadius;
                        mRectF.right = x + mDistance / 2f + mRadius;
                        mRectF.top = y;
                        mRectF.bottom = y + mNormalSize;
                        canvas.drawRoundRect(mRectF, mRadius, mRadius, mNormalPaint);
                        x += mDistance;
                    }
                    x = center - mDistance * count / 2f + mDistance * (mPosition + mPositionOffset);
                    mRectF.left = x + mDistance / 2f - mSelectSize / 2f;
                    mRectF.right = x + mDistance / 2f + mSelectSize / 2f;
                    mRectF.top = y;
                    mRectF.bottom = y + mNormalSize;
                    canvas.drawRoundRect(mRectF, mRadius, mRadius, mSelectPaint);
                }
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mPosition = position % getCount();
        mPositionOffset = positionOffset;
        invalidate();
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
