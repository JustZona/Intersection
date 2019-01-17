package com.rent.zona.commponent.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.rent.zona.commponent.R;
import com.rent.zona.commponent.constants.CommonIntentExtra;
import com.rent.zona.commponent.views.AppTitleBar;
import com.rent.zona.commponent.views.OnBackStackListener;


public class TitleBarFragmentActivity extends CommonFragmentActivity implements OnBackStackListener {
    protected AppTitleBar mTitleBar;

    public static Intent createIntent(Context context, String title, Bundle fragParams, Class<? extends Fragment> fragCls) {
        return createIntent(context, title, null, fragParams, fragCls);
    }

    public static Intent createIntent(Context context, String title, String activityName, Bundle fragParams, Class<? extends Fragment> fragCls) {
        Intent intent = new Intent(context, TitleBarFragmentActivity.class);
        intent.putExtra(CommonIntentExtra.EXTRA_ACTIVITY_NAME, TextUtils.isEmpty(activityName) ? fragCls.getName()
                : activityName);
        intent.putExtra(CommonIntentExtra.EXTRA_FRAGMENT_CLASS, fragCls);
        intent.putExtra(CommonIntentExtra.EXTRA_TITLE, title);

        if (fragParams != null) {
            intent.putExtras(fragParams);
        }

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitleBar = (AppTitleBar) findViewById(R.id.titlebar);
        if (mTitleBar != null) {
            mTitleBar.setBackListener(this);
            mTitleBar.setTitle(getIntent().getStringExtra(CommonIntentExtra.EXTRA_TITLE));
        }
    }

    public AppTitleBar getTitleBar() {
        return mTitleBar;
    }

    @Override
    protected int getMainViewResId() {
        return R.layout.activity_fragment;
    }

    @Override
    public boolean onBackStack() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (getFragment() instanceof BaseFragment) {
            BaseFragment fragment = (BaseFragment) getFragment();
            boolean r = fragment.onKeyDown(keyCode, event);
            if (r) {
                return r;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
