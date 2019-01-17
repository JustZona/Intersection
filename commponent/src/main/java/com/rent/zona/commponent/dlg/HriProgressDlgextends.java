package com.rent.zona.commponent.dlg;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.rent.zona.commponent.R;
import com.rent.zona.commponent.utils.DensityUtils;
import com.rent.zona.commponent.views.NumberProgressBar;

/**
 * @name：
 * @author： liuyun
 * @phone： 15723310658
 * @createTime： 2017/9/15
 * @modifyTime： 2017/9/15
 * @explain：说明
 */


public class HriProgressDlgextends extends ProgressDialog {
    private NumberProgressBar mProgressView;
    private TextView mMessageView;
    private CharSequence mMessage;
    private boolean mIndeterminate;
    private boolean mProgressVisiable;
    private int mProgress;

    public HriProgressDlgextends(Context context, int progress) {
//        super(context/*,R.style.Float*/);
        super(context, R.style.loading_dialog);
        this.mProgress=progress;
    }

    public HriProgressDlgextends(Context context, int theme, int progress) {
        super(context,/*, R.style.Float*/theme);
        this.mProgress=progress;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_hori_progress_dlg);
        mProgressView = (NumberProgressBar) findViewById(android.R.id.progress);
        mMessageView = (TextView) findViewById(R.id.message);

//        if (mMessage != null) {
//            setMessage(mMessage);
//        }

//        setMessageAndView();
//        setIndeterminate(mIndeterminate);
        mProgressView.setVisibility( View.VISIBLE );
        mProgressView.setProgress(mProgress);
        int width= (int)(DensityUtils.getScreenWidth(getContext())*0.8f);
        getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    public void setProgress(int progress){
        mProgressView.setProgress(progress);
    }
//    private void setMessageAndView() {
//        if (mMessageView != null) {
//            mMessageView.setText(mMessage);
//        }
//
//        if (TextUtils.isEmpty(mMessage)) {
//            mMessageView.setVisibility(View.GONE);
//        }
//        if (mProgress != null) {
//            mProgress.setVisibility(mProgressVisiable ? View.VISIBLE : View.GONE);
//        }
//    }

    @Override
    public void setMessage(CharSequence message) {
        mMessage = message;
    }

    public void setProgressVisiable(boolean progressVisiable) {
        mProgressVisiable = progressVisiable;
    }


//    public void setIndeterminate(boolean indeterminate) {
//        if (mProgress != null) {
//            mProgress.setIndeterminate(indeterminate);
//        } else {
//            mIndeterminate = indeterminate;
//        }
//    }
}
