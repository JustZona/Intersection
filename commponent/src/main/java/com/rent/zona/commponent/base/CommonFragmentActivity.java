package com.rent.zona.commponent.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;

import com.rent.zona.commponent.constants.CommonIntentExtra;
import com.rent.zona.commponent.views.OnBackStackListener;


public class CommonFragmentActivity extends BaseFragmentActivity {
    protected Fragment mFragment;
    private boolean mDisableAnim;


    public static Intent createIntent(Context context, Bundle fragParams, Class<? extends Fragment> fragCls) {
        return createIntent(context, null, fragParams, fragCls);
    }

    public static Intent createIntent(Context context, String activityName, Bundle fragParams, Class<? extends Fragment> fragCls) {
        Intent intent = new Intent(context, CommonFragmentActivity.class);
        if (activityName != null) {
            intent.putExtra(CommonIntentExtra.EXTRA_ACTIVITY_NAME, activityName);
        }
        intent.putExtra(CommonIntentExtra.EXTRA_FRAGMENT_CLASS, fragCls);

        if (fragParams != null) {
            intent.putExtras(fragParams);
        }

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivityName = getIntent().getStringExtra(CommonIntentExtra.EXTRA_ACTIVITY_NAME);

        super.onCreate(savedInstanceState);
        @SuppressWarnings("unchecked")
        Class<? extends Fragment> fragCls = (Class<? extends Fragment>) getIntent().getSerializableExtra(
                CommonIntentExtra.EXTRA_FRAGMENT_CLASS);
        mFragment = loadFragment(fragCls);
        Bundle fragParams = getIntent().getExtras();
        if (fragParams != null && mFragment.getArguments() == null) {
            mFragment.setArguments(fragParams);
        }

        mDisableAnim = getIntent().getBooleanExtra(CommonIntentExtra.EXTRA_DISABLE_ANIM, false);
    }




    public Fragment getFragment() {
        return mFragment;
    }

    @Override
    public void onBackPressed() {
        if (mFragment != null) {
            if (mFragment instanceof OnBackStackListener) {
                if (((OnBackStackListener) mFragment).onBackStack()) {
                    return;
                }
            }
        }
        super.onBackPressed();
    }

    @Override
    public void finish() {
        super.finish();
        if (mDisableAnim) {
            // remove activity transition animation
            overridePendingTransition(0, 0);
        }
    }

    @Override
    protected void onDestroy() {
        mFragment = null;
        super.onDestroy();
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
}
