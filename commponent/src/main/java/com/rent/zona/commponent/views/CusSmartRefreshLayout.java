package com.rent.zona.commponent.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.constant.RefreshState;

import static android.view.MotionEvent.obtain;

public class CusSmartRefreshLayout extends SmartRefreshLayout{
    Runnable refreshRunable;
    public CusSmartRefreshLayout(Context context) {
        super(context);
    }

    public CusSmartRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CusSmartRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void refresh(){
        reset();
        autoRefresh(0);
    }
    public void reset(){
        removeCallbacks(refreshRunable);
        if (mState == RefreshState.Refreshing && mRefreshHeader != null && mRefreshContent != null) {
            System.out.println("-------------reset()");
            boolean success=true;
            notifyStateChanged(RefreshState.RefreshFinish);
            @SuppressLint("RestrictedApi")
            int startDelay = mRefreshHeader.onFinish(CusSmartRefreshLayout.this, success);
            startDelay=0;
            if (mOnMultiPurposeListener != null && mRefreshHeader instanceof RefreshHeader) {
                mOnMultiPurposeListener.onHeaderFinish((RefreshHeader) mRefreshHeader, success);
            }
            if (startDelay < Integer.MAX_VALUE) {
                if (mIsBeingDragged || mNestedInProgress) {
                    if (mIsBeingDragged) {
                        mTouchY = mLastTouchY;
                        mTouchSpinner = 0;
                        mIsBeingDragged = false;
                    }
                    long time = System.currentTimeMillis();
                    super.dispatchTouchEvent(obtain(time, time, MotionEvent.ACTION_DOWN, mLastTouchX, mLastTouchY + mSpinner - mTouchSlop * 2, 0));
                    super.dispatchTouchEvent(obtain(time, time, MotionEvent.ACTION_MOVE, mLastTouchX, mLastTouchY + mSpinner, 0));
                    if (mNestedInProgress) {
                        mTotalUnconsumed = 0;
                    }
                }
                if (mSpinner > 0) {
                    notifyStateChanged(RefreshState.None);
                } else if (mSpinner < 0) {
                    animSpinner(0, startDelay, mReboundInterpolator, mReboundDuration);
                } else {
                    mKernel.moveSpinner(0, false);
                    resetStatus();
                }
            }
        }
    }
    /**
     * 自动刷新
     * @return 是否成功
     */
    public boolean autoRefresh(int delayed) {
        return autoRefresh(0, mReboundDuration, 1f * ((mHeaderMaxDragRate/2 + 0.5f) * mHeaderHeight) / (mHeaderHeight == 0 ? 1 : mHeaderHeight));
    }
    /**
     * 自动刷新
     * @param delayed 开始延时
     * @param duration 拖拽动画持续时间
     * @param dragRate 拉拽的高度比率（要求 ≥ 1 ）
     * @return 是否成功
     */
    @Override
    public boolean autoRefresh(int delayed, final int duration, final float dragRate) {
        if (mState == RefreshState.None && isEnableRefresh()) {
            if (reboundAnimator != null) {
                reboundAnimator.cancel();
            }
            refreshRunable = new Runnable() {
                @Override
                public void run() {
                    reboundAnimator = ValueAnimator.ofInt(mSpinner, (int) (mHeaderHeight * dragRate));
                    reboundAnimator.setDuration(duration);
                    reboundAnimator.setInterpolator(new DecelerateInterpolator());
                    reboundAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            mKernel.moveSpinner((int) animation.getAnimatedValue(), true);
                        }
                    });
                    reboundAnimator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            final View thisView = CusSmartRefreshLayout.this;
                            mLastTouchX = thisView.getMeasuredWidth() / 2;
                            mKernel.setState(RefreshState.PullDownToRefresh);
                        }
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            reboundAnimator = null;
                            if (mState != RefreshState.ReleaseToRefresh) {
                                mKernel.setState(RefreshState.ReleaseToRefresh);
                            }
                            overSpinner();
                        }
                    });
                    reboundAnimator.start();
                    System.out.println("----------------11-autoRefresh");
                }
            };
            if (delayed > 0) {
                reboundAnimator = new ValueAnimator();
                postDelayed(refreshRunable, delayed);
            } else {
                refreshRunable.run();
            }
            return true;
        } else {
            return false;
        }
    }
}
