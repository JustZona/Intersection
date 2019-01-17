package com.rent.zona.commponent.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.rent.zona.baselib.configs.LibConfigs;
import com.rent.zona.baselib.event.Event;
import com.rent.zona.baselib.network.RXHelper;
import com.rent.zona.baselib.network.httpbean.TResponse;
import com.rent.zona.baselib.rx.ObservableHelper;
import com.rent.zona.baselib.rx.ObservableTask;
import com.rent.zona.baselib.utils.GlobalStateMgr;
import com.rent.zona.commponent.R;
import com.rent.zona.commponent.constant.ExtraConstant;
import com.rent.zona.commponent.helper.ActivityUIHelper;
import com.rent.zona.commponent.utils.StatusBarUtil;

import org.devio.takephoto.app.TakePhoto;
import org.devio.takephoto.app.TakePhotoImpl;
import org.devio.takephoto.model.InvokeParam;
import org.devio.takephoto.model.TContextWrap;
import org.devio.takephoto.model.TResult;
import org.devio.takephoto.permission.InvokeListener;
import org.devio.takephoto.permission.PermissionManager;
import org.devio.takephoto.permission.TakePhotoInvocationHandler;
import org.devio.takephoto.uitl.TakePhotoConfig;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.Utils;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityBase;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;


/**
 * TakePhoto.TakeResultListener, InvokeListener 都是拍照或从相册选择照片需要用到的
 */
public class BaseActivity extends AppCompatActivity implements SwipeBackActivityBase, TakePhoto.TakeResultListener, InvokeListener {

    private static final boolean DEBUG = LibConfigs.isDebugLog();
    private static final String TAG = "BaseActivity";

    private static WeakHashMap<String, WeakReference<BaseActivity>> sInstanceMap =
            new WeakHashMap<String, WeakReference<BaseActivity>>();

    private ActivityUIHelper mActivityHelper;
    private RXHelper mSubscribeHelper;

    private boolean mHasAnimation = false;
    protected String mActivityName;

    public static final Map<String, String> sURL = new HashMap<>();
    private final Map<String, String> mURL = new HashMap<>();

    private SwipeBackActivityHelper mHelper;//滑动返回
    private boolean canSwipeBack = true;//是否能侧滑finish
    private boolean canTakePhoto = false;//是否使用拍照或相册功能
    private InvokeParam invokeParam;//拍照需要使用的参数
    private TakePhoto takePhoto;//拍照需要使用的参数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setStatusBar();
        MyActivityManager.getInstance().addActivity(this);
        canTakePhoto = getIntent().getBooleanExtra(ExtraConstant.EXTRA_CAN_TAKE_PHOTO, canTakePhoto());
        if (canTakePhoto) {
            getTakePhoto().onCreate(savedInstanceState);
        }
        super.onCreate(savedInstanceState);
        if (DEBUG) Log.d(TAG, "==== onCreate: " + getClass().getSimpleName());
        canSwipeBack = getIntent().getBooleanExtra(ExtraConstant.EXTRA_CAN_SWIPE_BACK, canSwipeBack());
        if (canSwipeBack) {
            mHelper = new SwipeBackActivityHelper(this);
            mHelper.onActivityCreate();
            getSwipeBackLayout().setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);//EDGE_LEFT EDGE_RIGHT EDGE_BOTTOM EDGE_ALL
        }

        if (mActivityName != null) {
            BaseActivity instance = getSingleActivity();
            if (instance != null) {
                instance.finish();
            }
            synchronized (sInstanceMap) {
                sInstanceMap.put(mActivityName, new WeakReference<BaseActivity>(this));
            }
        }

        try {
            mHasAnimation = getIntent().getBooleanExtra(ExtraConstant.EXTRA_HAS_ANIM, true);
        } catch (Exception e) {
            // ignore
            e.printStackTrace();
        }

        mURL.putAll(sURL);
        sURL.clear();
    }

    protected boolean canSwipeBack() {
        return true;
    }

    protected boolean canTakePhoto() {
        return false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (canTakePhoto) {
            getTakePhoto().onSaveInstanceState(outState);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (canTakePhoto) {
            getTakePhoto().onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 从相册选择图片
     *
     * @param limit 最大选择张数
     */
    protected void picBySelect(int limit) {
        TakePhotoConfig.picBySelect(takePhoto, limit);
    }

    /**
     * 从相册选择一张图片
     *
     * @param
     */
    protected void picBySelect(int width,int height) {
        TakePhotoConfig.picBySelect(takePhoto, width,height);
    }


    /**
     * 拍照
     */
    protected void picByTake() {
        TakePhotoConfig.picByTake(takePhoto);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (canTakePhoto) {
            PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
            PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
        }
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

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (canSwipeBack) {
            mHelper.onPostCreate();
        }
    }

    public void setStatusBar() {
//        StatusBarUtil.transparencyBar(this);
//        StatusBarUtil.setStatusBarColor(this, R.color.common_title_bg_color);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//5.0
            StatusBarUtil.transparencyBar(this);
        }
        StatusBarUtil.statusBarLightMode(this, true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {//5.0
            StatusBarUtil.setStatusBarColor(this, R.color.common_10_percent_transparent);
        }
    }

    private BaseActivity getSingleActivity() {
        if (mActivityName != null) {
            WeakReference<BaseActivity> ref = sInstanceMap.get(mActivityName);
            if (ref != null) {
                return ref.get();
            }
        }

        return null;
    }

    @Override
    protected void onDestroy() {
        if (DEBUG) Log.d(TAG, "==== onDestroy: " + getClass().getSimpleName());
        cancelTask();
        unsubscribeEvent();
        if (mActivityHelper != null) {
            mActivityHelper.finish();
            mActivityHelper = null;
        }
        super.onDestroy();
        MyActivityManager.getInstance().finishActivity(this);
        if (mActivityName != null) {
            BaseActivity instance = getSingleActivity();
            if (instance == this) {
                synchronized (sInstanceMap) {
                    sInstanceMap.remove(mActivityName);
                }
            }
        }
    }

    public ActivityUIHelper getActivityHelper() {
        if (mActivityHelper == null) {
            mActivityHelper = new ActivityUIHelper(this);
        }
        return mActivityHelper;
    }

    @Override
    protected void onResume() {
        super.onResume();

        GlobalStateMgr.setCurrentTopActivity(this);
        GlobalStateMgr.setCurrentTopActivityName(mActivityName);
        GlobalStateMgr.setIsForeGround(true);

    }

    @Override
    protected void onPause() {
        super.onPause();

        GlobalStateMgr.setCurrentTopActivity(null);
    }

    @Override
    protected void onStop() {
        if (GlobalStateMgr.getCurrentTopActivity() == null) {
            GlobalStateMgr.setIsForeGround(false);
        }

        super.onStop();
    }

    @Override
    public void finish() {
        super.finish();
        if (mHasAnimation) {
            overridePendingTransition(R.anim.app_slide_hold, R.anim.app_slide_right_out);
        }
    }

    public void startActivityWithAnim(Intent intent) {
        intent.putExtra(ExtraConstant.EXTRA_HAS_ANIM, true);
        startActivity(intent);
        overridePendingTransition(R.anim.app_slide_right_in, R.anim.app_slide_hold);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        if (mHasAnimation) {
            overridePendingTransition(R.anim.app_slide_right_in, R.anim.app_slide_hold);
        }
    }

    public void startActivityForResultWithAnim(Intent intent, int requestCode) {
        intent.putExtra(ExtraConstant.EXTRA_HAS_ANIM, true);
        startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.app_slide_right_in, R.anim.app_slide_hold);
    }

    public void overridePendingTransition(String leftAnim, String rightAnim) {
        String packageName = getPackageName();
        int left = getResources().getIdentifier(leftAnim, "anim", packageName);
        int right = getResources().getIdentifier(rightAnim, "anim", packageName);
        overridePendingTransition(left, right);
    }

    public void showWaitingProgress() {
        showProgress(getText(R.string.common_waiting));
    }

    public void showWaitingProgress(DialogInterface.OnCancelListener cancelListener) {
        showProgress(getText(R.string.common_waiting), cancelListener);
    }

    public void showProgress(final CharSequence msg) {
        if (mActivityHelper == null) {
            mActivityHelper = new ActivityUIHelper(this);
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
            mActivityHelper = new ActivityUIHelper(this);
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
                                      final Consumer<TResponse<? super T>> onNext
                                      ) {
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
    public  <T> Disposable sendRequest(Observable<TResponse<T>> observable,
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

    protected void defaultRetrofitErrorHandle(Throwable t) {
        dismissProgress();
        ActivityUIHelper.showFailure(t, this);
    }

    private void unsubscribeEvent() {
        if (mSubscribeHelper != null) {
            mSubscribeHelper.unsubscribeEvent();
        }
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }

    /**
     * 点击空白区域隐藏键盘
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction()==MotionEvent.ACTION_DOWN){//把操作放在用户点击的时候
            View currentFocus = getCurrentFocus();//得到当前页面的焦点
            if (isShouldHideKeyboard(currentFocus,ev)){//判断用户点击的是否是输入框以外的区域
                hideKeyboard(currentFocus.getWindowToken());//收起键盘
            }
        }
        return super.dispatchTouchEvent(ev);
    }
    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {  //判断得到的焦点控件是否包含EditText
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],    //得到输入框在屏幕中上下左右的位置
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击位置如果是EditText的区域，忽略它，不收起键盘。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
