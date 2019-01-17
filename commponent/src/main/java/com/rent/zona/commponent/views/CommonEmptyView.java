package com.rent.zona.commponent.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rent.zona.commponent.R;
import com.wang.avi.AVLoadingIndicatorView;


public class CommonEmptyView extends RelativeLayout {

    private LinearLayout mEmptyContainer;
    private ImageView mEmptyImage;
    //    private AVLoadingIndicatorView mLoadingView;
    private ImageView mLoadingView;
    private TextView mEmptyText;
    private Button mOperationBtn;
    private WebErrorView mErrorView;
    private boolean mAttached;
    private View mCustomEmptyView;

    public CommonEmptyView(Context context) {
        super(context);
        initView();
    }

    public CommonEmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        setAttrs(context, attrs);
    }

    public CommonEmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        setAttrs(context, attrs);
    }

    public void setOnButtonClickListener(OnClickListener l) {
        mOperationBtn.setOnClickListener(l);
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_common_empty, this, true);
        mEmptyContainer = (LinearLayout) findViewById(R.id.empty_container);
        mEmptyImage = (ImageView) findViewById(R.id.empty_image);
        mLoadingView = findViewById(R.id.loading);
        mEmptyText = (TextView) findViewById(R.id.empty_text);
        mOperationBtn = (Button) findViewById(R.id.empty_operation);
        mErrorView = (WebErrorView) findViewById(R.id.error);
    }

    private void setAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CommonEmptyView);
        if (a.hasValue(R.styleable.CommonEmptyView_image_background)) {
            int resId = a.getResourceId(R.styleable.CommonEmptyView_image_background, R.drawable.ic_default_empty);
            mEmptyImage.setBackgroundResource(resId);
        }
        if (a.hasValue(R.styleable.CommonEmptyView_emptyText)) {
            String text = a.getString(R.styleable.CommonEmptyView_emptyText);
            mEmptyText.setText(text);
        }
        if (a.hasValue(R.styleable.CommonEmptyView_buttonText)) {
            String clickText = a.getString(R.styleable.CommonEmptyView_buttonText);
            mOperationBtn.setText(clickText);
        }
        if (a.hasValue(R.styleable.CommonEmptyView_isTextVisible)) {
            boolean isVisible = a.getBoolean(R.styleable.CommonEmptyView_isTextVisible, true);
            setViewVisibility(isVisible, mEmptyText);
        }
        if (a.hasValue(R.styleable.CommonEmptyView_isButtonVisible)) {
            boolean isVisible = a.getBoolean(R.styleable.CommonEmptyView_isButtonVisible, true);
            setViewVisibility(isVisible, mOperationBtn);
        }
//        if (a.hasValue(R.styleable.CommonEmptyView_image_margin_top)) {
//            int marginTop = (int) a.getDimension(R.styleable.CommonEmptyView_image_margin_top, R.dimen.empty_view_margin_top);
//            setMarginTop(marginTop);
//        }
        if (a.hasValue(R.styleable.CommonEmptyView_isWrapContent)) {
            boolean isWrapContent = a.getBoolean(R.styleable.CommonEmptyView_isWrapContent, false);
            setWrapContent(isWrapContent);
        }

        a.recycle();
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
    }

    private void startAnimation() {
        if (mErrorView.getVisibility() != VISIBLE) {
            Drawable backGround = mEmptyImage.getBackground();
            if (backGround != null && backGround instanceof AnimationDrawable) {
                ((AnimationDrawable) backGround).start();
            }
        }
    }

    private void stopAnimation() {
        if (mEmptyImage == null) {
            return;
        }
        Drawable backGround = mEmptyImage.getBackground();
        if (backGround != null && backGround instanceof AnimationDrawable) {
            ((AnimationDrawable) backGround).stop();
        }
    }

    public void setWrapContent(boolean isWrapContent) {
        LayoutParams emptyParams = (LayoutParams) mEmptyContainer.getLayoutParams();
        LayoutParams errorParams = (LayoutParams) mErrorView.getLayoutParams();
        if (isWrapContent) {
            if (emptyParams != null && errorParams != null) {
                emptyParams.height = LayoutParams.WRAP_CONTENT;
                errorParams.height = LayoutParams.WRAP_CONTENT;
            }
        } else {
            if (emptyParams != null && errorParams != null) {
                emptyParams.height = LayoutParams.MATCH_PARENT;
                errorParams.height = LayoutParams.MATCH_PARENT;
            }
        }
    }

    public void setEmptyView(View view) {
        mEmptyContainer.removeAllViews();
        mEmptyContainer.addView(view);
        mEmptyContainer.setVisibility(View.GONE);
    }

    public void setMarginTop(int marginTop) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mEmptyImage.getLayoutParams();
        params.addRule(CENTER_IN_PARENT, 0);
        params.addRule(CENTER_HORIZONTAL);
        params.topMargin = marginTop;
        mEmptyImage.setLayoutParams(params);
    }

    public void setMarginTopResource(int resId) {
        int marginTop = (int) getContext().getResources().getDimension(resId);
        setMarginTop(marginTop);
    }

    public void setErrorMarginTop(int marginTop) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mErrorView.getLayoutParams();
        params.topMargin = marginTop;
        mErrorView.setLayoutParams(params);
    }

    public void setCenter() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mEmptyImage.getLayoutParams();
        params.addRule(CENTER_IN_PARENT);
        params.addRule(CENTER_HORIZONTAL, 0);
        params.topMargin = 0;
        mEmptyImage.setLayoutParams(params);
    }

    private void setViewVisibility(boolean visible, View view) {
        if (visible) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    public void setShowLoading(boolean show) {
        if (show) {
            mEmptyImage.setVisibility(GONE);
            mLoadingView.setVisibility(VISIBLE);
            Glide.with(getContext()).load(R.drawable.loading).apply(new RequestOptions().centerInside()).into(mLoadingView);
//            mLoadingView.show();
        } else {
            mEmptyImage.setVisibility(VISIBLE);
            mLoadingView.setVisibility(GONE);
            mLoadingView.setImageDrawable(null);
//            mLoadingView.hide();
        }
    }

    public void setEmptyImageBackgroundResource(int resId) {
        //mEmptyImage.setBackgroundDrawable(null);
        if (mAttached) {
            stopAnimation();
            mEmptyImage.setBackgroundResource(resId);
            if (getVisibility() == View.VISIBLE) {
                startAnimation();
            }
        } else {
            mEmptyImage.setBackgroundResource(resId);
        }
    }

    public void setEmptyTextVisibility(int visibility) {
        mEmptyText.setVisibility(visibility);
    }

    public void setEmptyText(String text) {
        mEmptyText.setText(text);
    }

    public void setEmptyText(int resId) {
        mEmptyText.setText(resId);
    }

    public void setEmptyTextColor(int color) {
        mEmptyText.setTextColor(color);
    }

    public void setButtonText(int resId) {
        mOperationBtn.setText(resId);
    }

    public void setButtonText(CharSequence text) {
        mOperationBtn.setText(text);
    }

    public void setButtonVisibility(int visibility) {
        mOperationBtn.setVisibility(visibility);
    }

    public Button getOperationButton() {
        return mOperationBtn;
    }

    public TextView getEmptyText() {
        return mEmptyText;
    }

    public void setCustomEmptyView(View customView) {
        if (mCustomEmptyView != null) {
            this.removeView(customView);
        }
        if (customView != null) {
            this.addView(customView, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
        }
        mCustomEmptyView = customView;
        if (mCustomEmptyView != null) {
            mCustomEmptyView.setVisibility(View.GONE);
        }
    }

    public void showError() {
        stopAnimation();
        mEmptyContainer.setVisibility(GONE);
        if (mCustomEmptyView != null) {
            mCustomEmptyView.setVisibility(GONE);
        }
        mErrorView.setVisibility(VISIBLE);
        setVisibility(View.VISIBLE);
    }

    public void showLoading() {
        showLoading(null);
    }

    public void showLoading(String indicatorName) {
        mEmptyContainer.setVisibility(VISIBLE);
        if (mCustomEmptyView != null) {
            mCustomEmptyView.setVisibility(View.GONE);
        }
        mOperationBtn.setVisibility(View.GONE);
        mErrorView.setVisibility(GONE);
        setVisibility(View.VISIBLE);
//        setEmptyImageBackgroundResource(resId != -1 ? resId : R.anim.default_loading_anim);
//        if (!TextUtils.isEmpty(indicatorName)) {
//            mLoadingView.setIndicator(indicatorName);
//        }
        setShowLoading(true);

    }

    public void showEmpty() {
        showEmpty(-1);
    }

    public void showEmpty(int resId) {
        mErrorView.setVisibility(GONE);
        setVisibility(View.VISIBLE);
        if (mCustomEmptyView != null) {
            mEmptyContainer.setVisibility(GONE);
            mCustomEmptyView.setVisibility(View.VISIBLE);
            stopAnimation();
        } else {
            mEmptyContainer.setVisibility(VISIBLE);
            setEmptyImageBackgroundResource(resId != -1 ? resId : R.drawable.ic_default_empty);
        }
        setShowLoading(false);
    }

    public WebErrorView getErrorView() {
        return mErrorView;
    }

    @Override
    public void setVisibility(int visibility) {
        if (getVisibility() != visibility) {
            super.setVisibility(visibility);
            if (visibility == GONE || visibility == INVISIBLE) {
                stopAnimation();
            } else {
                startAnimation();
            }
        }
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        // let's be nice with the UI thread
        if (visibility == GONE || visibility == INVISIBLE) {
            stopAnimation();
        } else {
            startAnimation();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (getVisibility() == VISIBLE) {
            startAnimation();
        }
        mAttached = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        stopAnimation();
        // This should come after stopAnimation(), otherwise an invalidate message remains in the
        // queue, which can prevent the entire view hierarchy from being GC'ed during a rotation
        super.onDetachedFromWindow();

        mAttached = false;
    }

}
