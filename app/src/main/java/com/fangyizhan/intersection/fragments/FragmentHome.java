package com.fangyizhan.intersection.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fangyizhan.intersection.R;
import com.fangyizhan.intersection.activities.SettingActivity;
import com.fangyizhan.intersection.views.CircleImageView;
import com.rent.zona.commponent.base.BaseFragment;
import com.rent.zona.commponent.utils.StatusBarUtil;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FragmentHome extends BaseFragment {


     @BindView(R.id.head_picture_iv)
     CircleImageView headPictureIv;
     @BindView(R.id.name_tv)
     TextView nameTv;
     @BindView(R.id.tab_layout_linear)
     LinearLayout tabLayoutLinear;
     @BindView(R.id.setting_iv)
     ImageView settingIv;
     @BindView(R.id.dynamic_iv)
     ImageView dynamicIv;
     @BindView(R.id.linear1)
     LinearLayout linear1;
     @BindView(R.id.distribution_iv)
     ImageView distributionIv;
     @BindView(R.id.recent_visitors_iv)
     ImageView recentVisitorsIv;
     @BindView(R.id.linear2)
     LinearLayout linear2;
     @BindView(R.id.square_iv)
     ImageView squareIv;
     @BindView(R.id.linear3)
     LinearLayout linear3;
    Unbinder unbinder;
    @BindView(R.id.homebg11_iv)
    ImageView homebg11Iv;
    @BindView(R.id.homebg6_iv)
    ImageView homebg6Iv;
    @BindView(R.id.boy_rl)
    RelativeLayout boyRl;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    private void initView() {
        //沉浸式状态栏高度
        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) settingIv.getLayoutParams();
        params1.setMargins(0, StatusBarUtil.getStatusBarHeight(getContext()), DensityUtil.dp2px(10), 0);
        settingIv.setLayoutParams(params1);
        //获取手机屏幕高度
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();//单位是px
        int height = wm.getDefaultDisplay().getHeight();
        //设置白色倒月牙距离上方的位置 weight42 +交叉的高度
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) homebg11Iv.getLayoutParams();
        params.setMargins(0, (int) (height * 0.42 - DensityUtil.dp2px(56)), 0, 0);
        homebg11Iv.setLayoutParams(params);

        FrameLayout.LayoutParams params2 = (FrameLayout.LayoutParams) homebg6Iv.getLayoutParams();
        params2.height = (int) (height * 0.41);//宽度比weight42 小一点（交叉的空间大一点）
        homebg6Iv.setLayoutParams(params2);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.head_picture_iv, R.id.setting_iv, R.id.dynamic_iv, R.id.distribution_iv, R.id.recent_visitors_iv, R.id.square_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_picture_iv:
                //头像
                break;
            case R.id.setting_iv:
                //设置
                SettingActivity.launch(getContext());
                break;
            case R.id.dynamic_iv:
                //动态
                break;
            case R.id.distribution_iv:
                //包分配
                break;
            case R.id.recent_visitors_iv:
                //最近访客
                break;
            case R.id.square_iv:
                //广场
                break;
        }
    }
}
