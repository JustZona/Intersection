package com.fangyizhan.intersection.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.fangyizhan.intersection.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/8/20.
 */

public class MyCustomDialog extends Dialog {


    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.info_tv)
    TextView infoTv;
    @BindView(R.id.cancel_tv)
    TextView cancelTv;
    @BindView(R.id.sure_tv)
    TextView sureTv;
    @BindView(R.id.ll_common_dialog_double)
    LinearLayout llCommonDialogDouble;

    public MyCustomDialog(Context context) {
        super(context, R.style.CustomDialogStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(false);  // 是否可以撤销
        setContentView(R.layout.my_custom_dialog);
        ButterKnife.bind(this);
    }

    /**
     * 设置右键文字和点击事件
     *
     * @param rightStr      文字
     * @param clickListener 点击事件
     */
    public void setRightButton(String rightStr, View.OnClickListener clickListener) {
        sureTv.setOnClickListener(clickListener);
        sureTv.setText(rightStr);
    }

    /**
     * 设置左键文字和点击事件
     *
     * @param leftStr       文字
     * @param clickListener 点击事件
     */
    public void setLeftButton(String leftStr, View.OnClickListener clickListener) {
        cancelTv.setOnClickListener(clickListener);
        cancelTv.setText(leftStr);
    }

    /**
     * 设置内容
     *
     * @param str 内容
     */
    public void setINfoText(String str) {
        infoTv.setText(Html.fromHtml(str));
        infoTv.setVisibility(View.VISIBLE);
    }

    /**
     * 设置标题
     *
     * @param str 内容
     */
    public void setTitleText(String str) {
        titleTv.setText(Html.fromHtml(str));
        titleTv.setVisibility(View.VISIBLE);
    }


    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);
    }

    /**
     * 是否只有一个按钮
     */
    public void isSingleButton(boolean single){
        cancelTv.setVisibility(View.GONE);
    }

}