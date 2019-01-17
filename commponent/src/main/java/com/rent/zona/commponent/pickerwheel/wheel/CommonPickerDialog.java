package com.rent.zona.commponent.pickerwheel.wheel;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.rent.zona.commponent.R;
import com.rent.zona.commponent.pickerwheel.CommonWheel;
import com.rent.zona.commponent.pickerwheel.bean.AbstractPickerBean;
import com.rent.zona.commponent.pickerwheel.config.PickerConfig;

import java.util.List;


public class CommonPickerDialog<T extends AbstractPickerBean> extends Dialog implements View.OnClickListener {

    public interface OnPickerResultListener<T> {
        void onPickerResult(List<T> resuilt);
    }

    PickerConfig mPickerConfig;
    private CommonWheel mCommonWheel;
    private View rootView; // 总的布局
    private View btnSubmit, btnCancel;
    private TextView mTimeTitle;
    private static final String TAG_SUBMIT = "submit";
    private static final String TAG_CANCEL = "cancel";

    private OnPickerResultListener mOnPickerResultListener;

    public CommonPickerDialog(Context context, PickerConfig pickerConfig) {
        super(context, R.style.MyWidget_ActionSheet);
        this.mPickerConfig = pickerConfig;
        setContentView(initView());

        Window win = getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0); // make dialog full screen
        WindowManager.LayoutParams wmlp = getWindow().getAttributes();
        wmlp.gravity = Gravity.BOTTOM;
        wmlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wmlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(wmlp);
        setCanceledOnTouchOutside(true);
    }

    View initView() {
        LayoutInflater mLayoutInflater = LayoutInflater.from(getContext());
        rootView = mLayoutInflater.inflate(R.layout.pw_time, null);
        btnSubmit = rootView.findViewById(R.id.btn_submit);
        btnSubmit.setTag(TAG_SUBMIT);
        btnCancel = rootView.findViewById(R.id.btn_cancel);
        btnCancel.setTag(TAG_CANCEL);
        mTimeTitle = (TextView) rootView.findViewById(R.id.time_title);
        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        FrameLayout wheelViewContaner = (FrameLayout) rootView.findViewById(R.id.wheelview_contaner);
        wheelViewContaner.removeAllViews();

        mCommonWheel = new CommonWheel(wheelViewContaner, mPickerConfig);
        return rootView;
    }

    public void setDataSource(List<T>... dataSources) {
        mCommonWheel.setDataSource(dataSources);
    }

    public void setOnUpdateNextDataListener(CommonWheel.OnUpdateNextDataListener listener) {
        mCommonWheel.setOnUpdateNextDataListener(listener);
    }

    public void setOnPickerResultListener(OnPickerResultListener listener) {
        this.mOnPickerResultListener = listener;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_cancel) {
            dismiss();
        } else if (i == R.id.btn_submit) {
            sureClicked();
        }
    }

    void sureClicked() {
        dismiss();
        if (mOnPickerResultListener != null) {
            mOnPickerResultListener.onPickerResult(mCommonWheel.getCurrentPickResult());
        }
    }

    public static CommonPickerDialog build(Context context) {
        PickerConfig pickerConfig = new PickerConfig();
        return new CommonPickerDialog(context, pickerConfig);
    }

    public void setTitleName(String mText) {
        mTimeTitle.setText(mText);
    }
}
