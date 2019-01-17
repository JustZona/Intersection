package com.rent.zona.commponent.dlg;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rent.zona.commponent.R;
import com.wang.avi.AVLoadingIndicatorView;


public class AUProgressDialog extends ProgressDialog {
//    private ProgressBar mProgress;
    AVLoadingIndicatorView mLoadingView;
    private TextView mMessageView;
    private CharSequence mMessage;
    private boolean mIndeterminate;
    private boolean mProgressVisiable;

    public AUProgressDialog(Context context) {
//        super(context/*,R.style.Float*/);
        super(context, R.style.loading_dialog);
    }

    public AUProgressDialog(Context context, int theme) {
        super(context,/*, R.style.Float*/theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_progress_dialog);
//        mProgress = (ProgressBar) findViewById(android.R.id.progress);
        mLoadingView=findViewById(R.id.avi_progress);
        mMessageView = (TextView) findViewById(R.id.message);

//        if (mMessage != null) {
//            setMessage(mMessage);
//        }

        setMessageAndView();
        setIndeterminate(mIndeterminate);
    }

    private void setMessageAndView() {
        if (mMessageView != null) {
            mMessageView.setText(mMessage);
        }

        if (TextUtils.isEmpty(mMessage)) {
            mMessageView.setVisibility(View.GONE);
        }
        if (mLoadingView != null) {
            mLoadingView.setVisibility(mProgressVisiable ? View.VISIBLE : View.GONE);
        }
        showLoading();
    }

    @Override
    public void setMessage(CharSequence message) {
        mMessage = message;
    }

    public void setProgressVisiable(boolean progressVisiable) {
        mProgressVisiable = progressVisiable;
    }


    public void showLoading(){
        if(mLoadingView.isShown()){
            mLoadingView.show();
        }
    }
//    public void setIndeterminate(boolean indeterminate) {
//        if (mProgress != null) {
//            mProgress.setIndeterminate(indeterminate);
//        } else {
//            mIndeterminate = indeterminate;
//        }
//    }

    @Override
    public void onDetachedFromWindow() {
        mLoadingView.hide();
        super.onDetachedFromWindow();
    }
}
