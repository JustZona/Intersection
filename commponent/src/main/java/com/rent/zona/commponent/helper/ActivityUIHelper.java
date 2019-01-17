package com.rent.zona.commponent.helper;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;
import com.rent.zona.baselib.configs.LibConfigs;
import com.rent.zona.baselib.network.httpbean.TaskException;
import com.rent.zona.commponent.R;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

/**
 * Activity辅助类
 */
public class ActivityUIHelper {
    final static String TAG = "ActivityUIHelper";
    /**
     * 对应的Activity
     */
    private Activity mActivity;

    /**
     * 对话框帮助类
     */
    private DialogHelper mDialogHelper;

    public ActivityUIHelper(Activity activity) {
        mActivity = activity;

        mDialogHelper = new DialogHelper(mActivity);
    }

    public void finish() {
        mActivity = null;
        mDialogHelper.finish();
    }

    /**
     * 弹对话框
     *
     * @param title            标题
     * @param msg              消息
     * @param positive         确定
     * @param positiveListener 确定回调
     * @param negative         否定
     * @param negativeListener 否定回调
     */
    public void alert(CharSequence title, CharSequence msg, CharSequence positive,
                      DialogInterface.OnClickListener positiveListener, CharSequence negative,
                      DialogInterface.OnClickListener negativeListener) {
        mDialogHelper.alert(title, msg, positive, positiveListener, negative,
                negativeListener);
    }

    /**
     * 弹对话框
     *
     * @param title                    标题
     * @param msg                      消息
     * @param positive                 确定
     * @param positiveListener         确定回调
     * @param negative                 否定
     * @param negativeListener         否定回调
     * @param isCanceledOnTouchOutside 外部是否可点取消
     */
    public void alert(CharSequence title, CharSequence msg, CharSequence positive,
                      DialogInterface.OnClickListener positiveListener, CharSequence negative,
                      DialogInterface.OnClickListener negativeListener,
                      Boolean isCanceledOnTouchOutside) {
        mDialogHelper.alert(title, msg, positive, positiveListener, negative,
                negativeListener, isCanceledOnTouchOutside);
    }

    public static Toast toast(CharSequence msg, Context context) {
        return DialogHelper.toast(msg, Toast.LENGTH_SHORT, context);
    }

    /**
     * TOAST
     *
     * @param msg    消息
     * @param period should be Toast.LENGTH_LONG or Toast.LENGTH_SHORT.
     */
    public static void toast(CharSequence msg, int period, Context context) {
        DialogHelper.toast(msg, period, context);
    }

    public void showWaitingProgress() {
        mDialogHelper.showProgressDialog(mActivity.getText(R.string.common_waiting));
    }

    public void showWaitingProgress(final boolean cancelable, final OnCancelListener cancelListener) {
        mDialogHelper.showProgressDialog(mActivity.getText(R.string.common_waiting),
                cancelable, cancelListener, true);
    }

    public void showHoriProgress(int progress,boolean cancelable) {
        mDialogHelper.showHoriProgressDlg(progress,cancelable,null);
    }
    /**
     * 显示进度对话框
     *
     * @param msg 消息
     */
    public void showProgress(CharSequence msg) {
        mDialogHelper.showProgressDialog(msg);
    }
    public void setDlgCancelable(boolean cancelable){
        if(mDialogHelper.getAlertDialg()!=null ){
            mDialogHelper.getAlertDialg().setCancelable(cancelable);
        }
    }
    /**
     * 显示可取消的进度对话框
     *
     * @param msg 消息
     */
    public void showProgress(final CharSequence msg, final boolean cancelable,
                             final OnCancelListener cancelListener) {
        mDialogHelper.showProgressDialog(msg, cancelable, cancelListener, true);
    }


    public void dismissProgress() {
        mDialogHelper.dismissProgressDialog();
    }

    public boolean isProgressDialogShown() {
        return mDialogHelper.isProgressDialogShown();
    }

    public static void showFailure(Throwable throwable, Context context) {
        CharSequence errMsg = null;
        if (throwable != null) {
            if (throwable instanceof TaskException) {
                TaskException e= (TaskException) throwable;

                errMsg = ((TaskException) throwable).desc;
            } else if (throwable instanceof SocketTimeoutException || throwable instanceof TimeoutException) {
                errMsg = context.getText(R.string.timeout_exception);
            } else if (throwable instanceof IOException) {
                errMsg = context.getText(R.string.network_error);
            } else if (LibConfigs.isDebugLog() && throwable instanceof JsonSyntaxException) {
                errMsg = context.getText(R.string.json_exception);
            }
        }

        if (errMsg == null) {
            errMsg = context.getText(R.string.default_request_error);
        }
        toast(errMsg, context);
        if (LibConfigs.isDebugLog()) {
            Log.w(TAG, "subscribe error ", throwable);
        }
    }

}
