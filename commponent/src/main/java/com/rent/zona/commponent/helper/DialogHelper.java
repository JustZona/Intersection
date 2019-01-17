package com.rent.zona.commponent.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.rent.zona.baselib.log.LibLogger;
import com.rent.zona.commponent.R;
import com.rent.zona.commponent.dlg.AUProgressDialog;
import com.rent.zona.commponent.dlg.HriProgressDlgextends;
import com.rent.zona.commponent.utils.ActivityUtils;


/**
 * 对话框辅助类
 */
public class DialogHelper {
    /**
     * log tag
     */
    protected static final String TAG = "DialogHelper";
    private Activity mActivity;
    private AlertDialog mAlertDialog;

    public DialogHelper(Activity activity) {
        mActivity = activity;
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
    public void alert(final CharSequence title, final CharSequence msg, final CharSequence positive,
                      final DialogInterface.OnClickListener positiveListener,
                      final CharSequence negative, final DialogInterface.OnClickListener negativeListener) {
        alert(title, msg, positive, positiveListener, negative, negativeListener, false);
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
     * @param isCanceledOnTouchOutside 是否可以点击外围框
     */
    public void alert(final CharSequence title, final CharSequence msg, final CharSequence positive,
                      final DialogInterface.OnClickListener positiveListener,
                      final CharSequence negative, final DialogInterface.OnClickListener negativeListener, final Boolean isCanceledOnTouchOutside) {
        dismissProgressDialog();

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mActivity != null && !mActivity.isFinishing()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            mActivity);
                    if (title != null) {
                        builder.setTitle(title);
                    }
                    if (msg != null) {
                        builder.setMessage(msg);
                    }
                    if (positive != null) {
                        builder.setPositiveButton(positive, positiveListener);
                    }
                    if (negative != null) {
                        builder.setNegativeButton(negative, negativeListener);
                    }
                    mAlertDialog = builder.create();
                    mAlertDialog.setOnCancelListener(new OnCancelListener() {

                        @Override
                        public void onCancel(DialogInterface dialog) {
                            // TODO Auto-generated method stub

                        }
                    });
                    try {
                        mAlertDialog.show();
                        mAlertDialog
                                .setCanceledOnTouchOutside(isCanceledOnTouchOutside);
                        mAlertDialog.setCancelable(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                        mAlertDialog = null;
                    }
                }
            }
        });
    }

    /**
     * TOAST
     *
     * @param msg    消息
     * @param period should be Toast.LENGTH_LONG or Toast.LENGTH_SHORT.
     */
    public static Toast toast(final CharSequence msg, final int period, Context context) {
        Toast toast = new Toast(context);
        View view = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.common_toast, null);
        TextView tv = (TextView) view.findViewById(android.R.id.message);
        tv.setText(msg);
        toast.setView(view);
        if (ActivityUtils.isKeyboardShowing(context)) {
            toast.setGravity(Gravity.CENTER, 0, 0);
        }
        toast.setDuration(period);
        toast.show();
        return toast;
    }
    public static Toast toast(final CharSequence msg, Context context) {
        return toast(msg, Toast.LENGTH_SHORT, context);
    }
    /**
     * 显示对话框
     *
     * @param showProgressBar 是否显示圈圈
     * @param msg             对话框信息
     */
    public void showProgressDialog(boolean showProgressBar, CharSequence msg) {
        showProgressDialog(msg, true, null, showProgressBar);
    }

    /**
     * 显示进度对话框
     *
     * @param msg 消息
     */
    public void showProgressDialog(final CharSequence msg) {
        showProgressDialog(msg, false, null, true);
    }

    /**
     * 显示可取消的进度对话框
     *
     * @param msg 消息
     */
    public void showProgressDialog(final CharSequence msg, final boolean cancelable,
                                   final OnCancelListener cancelListener, final boolean showProgressBar) {
        dismissProgressDialog();

        mActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (mActivity == null || mActivity.isFinishing()) {
                    return;
                }

                mAlertDialog = new AUProgressDialog(mActivity);
                mAlertDialog.setMessage(msg);
                ((AUProgressDialog) mAlertDialog).setProgressVisiable(showProgressBar);
                mAlertDialog.setCancelable(cancelable);
                mAlertDialog.setOnCancelListener(cancelListener);

                mAlertDialog.show();
                mAlertDialog.setCanceledOnTouchOutside(false);
            }
        });
    }

    public void dismissProgressDialog() {
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mAlertDialog != null && mAlertDialog.isShowing()) {
                        try {
                            mAlertDialog.dismiss();
                        } catch (Exception e) {
                            LibLogger.w(TAG, "DialogHelper.dismissProgressDialog(): exception=" + e);
                        } finally {
                            mAlertDialog = null;
                        }
                    }
                }
            });
        }
    }

    public boolean isProgressDialogShown() {
        return (mAlertDialog != null && mAlertDialog.isShowing() && (mAlertDialog instanceof AUProgressDialog));
    }

    public void finish() {
        dismissProgressDialog();
        mActivity = null;
    }
    public void showHoriProgressDlg(int progress, final boolean cancelable){
        showHoriProgressDlg(progress,cancelable,null);
    }
    public void showHoriProgressDlg(int progress, final boolean cancelable,
                                    final OnCancelListener cancelListener){
        if(mAlertDialog!=null && mAlertDialog instanceof HriProgressDlgextends && mAlertDialog.isShowing()){
            mActivity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (mActivity == null || mActivity.isFinishing()) {
                        return;
                    }
                    ((HriProgressDlgextends) mAlertDialog).setProgress(progress);
                }
            });
        }else {
            dismissProgressDialog();
            mActivity.runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    if (mActivity == null || mActivity.isFinishing()) {
                        return;
                    }
                    try {
                        mAlertDialog = new HriProgressDlgextends(mActivity, progress);
//                    ((HriProgressDlgextends) mAlertDialog).setProgress(progress);
                        mAlertDialog.setCancelable(cancelable);
                        mAlertDialog.setOnCancelListener(cancelListener);

                        mAlertDialog.show();

                        mAlertDialog.setCanceledOnTouchOutside(false);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    private void setProgress(int progress){
        if(mAlertDialog!=null && mAlertDialog instanceof HriProgressDlgextends && mAlertDialog.isShowing()){
            mActivity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (mActivity == null || mActivity.isFinishing()) {
                        return;
                    }
                    ((HriProgressDlgextends) mAlertDialog).setProgress(progress);
                }
            });
        }
    }
    public Dialog getAlertDialg(){
        return  mAlertDialog;
    }
}
