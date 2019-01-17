package com.rent.zona.commponent.base;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.rent.zona.baselib.configs.LibConfigs;
import com.rent.zona.baselib.event.Event;
import com.rent.zona.baselib.log.LibLogger;
import com.rent.zona.baselib.network.RXHelper;
import com.rent.zona.baselib.network.httpbean.TResponse;
import com.rent.zona.baselib.rx.ObservableHelper;
import com.rent.zona.baselib.rx.ObservableTask;
import com.rent.zona.commponent.R;
import com.rent.zona.commponent.constant.ExtraConstant;
import com.rent.zona.commponent.helper.ActivityUIHelper;
import com.rent.zona.commponent.views.AppTitleBar;

import org.devio.takephoto.app.TakePhoto;
import org.devio.takephoto.app.TakePhotoImpl;
import org.devio.takephoto.model.InvokeParam;
import org.devio.takephoto.model.TContextWrap;
import org.devio.takephoto.model.TResult;
import org.devio.takephoto.permission.InvokeListener;
import org.devio.takephoto.permission.PermissionManager;
import org.devio.takephoto.permission.TakePhotoInvocationHandler;
import org.devio.takephoto.uitl.TakePhotoConfig;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;



public class BaseFragment extends Fragment implements TakePhoto.TakeResultListener, InvokeListener ,HandleBackInterface{
    private static final boolean DEBUG = LibConfigs.isDebugLog();
    private static final String TAG = "BaseFragment";
    private ActivityUIHelper mActivityHelper;
    private RXHelper mSubscribeHelper;

    public static final Map<String, String> sURL = new HashMap<>();
    private final Map<String, String> mURL = new HashMap<>();
    public AppTitleBar mTitleBar;
    private boolean canTakePhoto=false;//是否使用拍照或相册功能
    private InvokeParam invokeParam;//拍照需要使用的参数
    private TakePhoto takePhoto;//拍照需要使用的参数
    public BaseFragment() {
        mURL.putAll(sURL);
        sURL.clear();
    }

    protected void finishWithAnim(int leftAnim, int rightAnim) {
        Activity activity = getActivity();
        activity.finish();
        activity.overridePendingTransition(leftAnim, rightAnim);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if(getArguments()!=null){
            canTakePhoto=getArguments().getBoolean(ExtraConstant.EXTRA_CAN_TAKE_PHOTO,canTakePhoto());
        }else{
            canTakePhoto=canTakePhoto();
        }

        if(canTakePhoto){
            getTakePhoto().onCreate(savedInstanceState);
        }
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(canTakePhoto) {
            getTakePhoto().onSaveInstanceState(outState);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(canTakePhoto) {
            getTakePhoto().onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    /**
     * 从相册选择图片
     * @param limit 最大选择张数
     */
    protected void picBySelect(int limit){
        TakePhotoConfig.picBySelect(takePhoto,limit);
    }

    /**
     * 拍照
     */
    protected void picByTake(){
        TakePhotoConfig.picByTake(takePhoto);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(canTakePhoto) {
            PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
            PermissionManager.handlePermissionsResult(getActivity(), type, invokeParam, this);
        }
    }
    @Override
    public void takeSuccess(TResult result) {
        Log.i(TAG, "takeSuccess：" + result.getImage().getCompressPath());
    }

    @Override
    public void takeFail(TResult result, String msg) {
        Log.i(TAG, "takeFail:" + msg);
    }

    @Override
    public void takeCancel() {
        Log.i(TAG, getResources().getString(R.string.msg_operation_canceled));
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }
    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    public String getTAG(){
        return this.getClass().getSimpleName();
    }
    @Override
    public void onPause() {
        super.onPause();
    }

    protected boolean canTakePhoto(){
        return false;
    }
    @Override
    public void onDestroyView() {
        cancelTask();
        unsubscribeEvent();
        super.onDestroyView();
    }
    public ActivityUIHelper getActivityHelper() {
        if (mActivityHelper == null) {
            mActivityHelper = new ActivityUIHelper(getActivity());
        }
        return mActivityHelper;
    }
    @Override
    public void onDestroy() {
        if (DEBUG) LibLogger.d(TAG, "==== onDestroy: " + getClass().getSimpleName());
        if (mActivityHelper != null) {
            mActivityHelper.finish();
            mActivityHelper = null;
        }

        super.onDestroy();
    }

    public void showWaitingProgress() {
        showProgress(getText(R.string.common_waiting));
    }

    public void showWaitingProgress(DialogInterface.OnCancelListener cancelListener) {
        showProgress(getText(R.string.common_waiting), cancelListener);
    }

    public void showProgress(final CharSequence msg) {
        if (mActivityHelper == null) {
            mActivityHelper = new ActivityUIHelper(getActivity());
        }
        mActivityHelper.showProgress(msg, true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                cancelTask();
            }
        });
    }

    public void showProgress(final CharSequence msg, DialogInterface.OnCancelListener cancelListener) {
        if (mActivityHelper == null) {
            mActivityHelper = new ActivityUIHelper(getActivity());
        }
        mActivityHelper.showProgress(msg, (cancelListener != null), cancelListener);
    }

    public void dismissProgress() {
        if (mActivityHelper != null) {
            mActivityHelper.dismissProgress();
        }
    }

    protected void cancelTask() {
        if (mSubscribeHelper != null) {
            mSubscribeHelper.unsubscribeTask();
        }
    }

    protected RXHelper getSubscribeHelper() {
        if (mSubscribeHelper == null) {
            mSubscribeHelper = new RXHelper();
        }

        return mSubscribeHelper;
    }

    /**
     * 异步执行轻量级任务, 耗时不超过5秒，请注意：生命周期结束时会取消该操作
     *
     * @param task
     * @param onUiThread
     * @param onError
     * @param <T>
     * @return
     */
    public <T> Disposable executeUITask(ObservableTask<T> task,
                                        final Consumer<? super T> onUiThread,
                                        final Consumer<Throwable> onError) {
        return getSubscribeHelper().executeUITask(ObservableHelper.create(task), onUiThread,
                onError != null ? onError : this::defaultRetrofitErrorHandle);
    }

    /**
     * 异步执行轻量级任务, 耗时不超过5秒，请注意：生命周期结束时会取消该操作
     *
     * @param observable
     * @param onNext
     * @param onError
     * @param <T>
     * @return
     */
    public <T> Disposable executeUITask(Observable<T> observable,
                                          final Consumer<? super T> onNext,
                                          final Consumer<Throwable> onError) {
        return getSubscribeHelper().executeUITask(observable, onNext,
                onError != null ? onError : this::defaultRetrofitErrorHandle);
    }

    /**
     * 异步执行后台任务, 任务耗时有可能比较长，请注意：生命周期结束时会取消该操作
     *
     * @param observable
     * @param onNext
     * @param onError
     * @param <T>
     * @return
     */
    public <T> Disposable executeBkgTask(Observable<T> observable,
                                           final Consumer<? super T> onNext,
                                           final Consumer<Throwable> onError) {
        return getSubscribeHelper().executeBkgTask(observable, onNext,
                onError != null ? onError : this::defaultRetrofitErrorHandle);
    }

    /**
     * 发送特斯联请求接口，请注意：生命周期结束时会取消该操作
     *
     * @param observable
     * @param onNext
     * @param <T>
     * @return
     */
    public <T> Disposable sendRequest(Observable<TResponse<T>> observable,
                                      final Consumer<TResponse<? super T>> onNext) {
        return sendRequest(observable, onNext, null);
    }
    /**
     * 发送特斯联请求接口，请注意：生命周期结束时会取消该操作
     *
     * @param observable
     * @param onNext
     * @param onError
     * @param <T>
     * @return
     */
    public <T> Disposable sendRequest(Observable<TResponse<T>> observable,
                                        final Consumer<TResponse<? super T>> onNext,
                                        final Consumer<Throwable> onError) {

        return getSubscribeHelper().execute(observable, onNext,
                onError != null ? onError : this::defaultRetrofitErrorHandle);

    }


    protected <E extends Event> Disposable subscribeEvent(
            Class<E> eventClass, final Consumer<? super E> onEvent) {
        return getSubscribeHelper().observeEvent(eventClass, onEvent, AndroidSchedulers.mainThread());
    }

    protected <E extends Event> Disposable subscribeEvent(
            Class<E> eventClass,
            final Consumer<? super E> onEvent,
            Scheduler scheduler) {
        return getSubscribeHelper().observeEvent(eventClass, onEvent, scheduler);
    }

    protected void unsubscribeEvent(Disposable d) {
        getSubscribeHelper().unsubscribeEvent(d);
    }

    public void defaultRetrofitErrorHandle(Throwable t) {
        dismissProgress();
        ActivityUIHelper.showFailure(t, getActivity());
    }

    public AppTitleBar getTitleBar() {
        AppTitleBar titleBar = null;
        if (getActivity() instanceof TitleBarFragmentActivity) {
            titleBar = ((TitleBarFragmentActivity) getActivity()).getTitleBar();
        }
        return titleBar;
    }

    private void unsubscribeEvent() {
        if (mSubscribeHelper != null) {
            mSubscribeHelper.unsubscribeEvent();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    /**
     * @return 返回统计使用的页面标题
     */
    protected String getStatsTitle() {
        return null;
    }


    private String getStatsTag() {
        String tag = getStatsTitle();
        if (TextUtils.isEmpty(tag)) {


            if (getTitleBar() == null || TextUtils.isEmpty(getTitleBar().getTitle())) {
                tag = getClass().getSimpleName();
            } else {
                tag = getTitleBar().getTitle().toString();
            }
        }
        return tag;
    }



    public void setTitle(String title){
        AppTitleBar appTitleBar= (AppTitleBar) getView().findViewById(R.id.titlebar);
        if(appTitleBar!=null){
            appTitleBar.setTitle(title);
        }
    }
    public AppTitleBar getTitleView(){
        return  (AppTitleBar) getView().findViewById(R.id.titlebar);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTitleBar= (AppTitleBar) getView().findViewById(R.id.titlebar);
        setBack(true);
    }


    public void setBack(boolean isCanBack){
        View backView=getView().findViewById(R.id.back);
        if(backView!=null){
            if(isCanBack){
                backView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            back();
                        }
                    });
            }else{
                backView.setOnClickListener(null);
            }
        }

    }
    public void back(){
        getActivity().finish();

    }

    @Override
    public boolean onBackPressed() {
        return HandleBackUtil.handleBackPress(this);
    }
}
