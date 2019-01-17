package com.fangyizhan.intersection.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fangyizhan.intersection.R;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home3, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
//        tmdIv1.getBackground().setAlpha(25);
//        tmdIv.getBackground().setAlpha(1);
        return rootView;
    }

    private void initView() {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) settingIv.getLayoutParams();
        params.setMargins(0, StatusBarUtil.getStatusBarHeight(getContext()), DensityUtil.dp2px(10), 0);
        settingIv.setLayoutParams(params);

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
