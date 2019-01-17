package com.rent.zona.commponent.views;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.rent.zona.commponent.R;


public class WebErrorView extends FrameLayout implements View.OnClickListener {
    private WebErrorViewClickListener mViewClickListener;

    public interface WebErrorViewClickListener {
        void onRefreshClicked();
    }

    public WebErrorView(Context context) {
        super(context);
    }

    public WebErrorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WebErrorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupViews(context);
    }

    private void setupViews(Context context) {
        LayoutInflater.from(context).inflate(R.layout.web_error_layout, this, true);
        findViewById(R.id.network_settings).setOnClickListener(this);
        findViewById(R.id.refresh).setOnClickListener(this);
    }

    public void setErrorViewClickListener(WebErrorViewClickListener l) {
        mViewClickListener = l;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.network_settings) {
            Intent intent = null;
            //判断手机系统的版本  即API大于10 就是3.0或以上版本
            if (android.os.Build.VERSION.SDK_INT > 10) {
                intent = new Intent(Settings.ACTION_SETTINGS);
            } else {
                intent = new Intent();
                ComponentName component = new ComponentName("com.android.settings", "com.android.settings.Settings");
                intent.setComponent(component);
                intent.setAction("android.intent.action.VIEW");
            }
            getContext().startActivity(intent);
        } else if (id == R.id.refresh) {
            if (mViewClickListener != null) {
                mViewClickListener.onRefreshClicked();
            }
        }
    }
}
