package com.rent.zona.commponent.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.rent.zona.baselib.configs.LibConfigs;
import com.rent.zona.baselib.event.Event;
import com.rent.zona.baselib.network.RXHelper;
import com.rent.zona.baselib.network.httpbean.TResponse;
import com.rent.zona.baselib.rx.ObservableHelper;
import com.rent.zona.baselib.rx.ObservableTask;
import com.rent.zona.baselib.utils.GlobalStateMgr;
import com.rent.zona.commponent.R;
import com.rent.zona.commponent.constant.ExtraConstant;
import com.rent.zona.commponent.constants.CommonIntentExtra;
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


public class BaseFragmentActivity extends FragmentActivity  implements SwipeBackActivityBase ,TakePhoto.TakeResultListener, InvokeListener {
    public static final String DEFAULT_FRAGMENT_TAG = "DefaultFragment";

    private static final boolean DEBUG = LibConfigs.isDebugLog();
    private static final String TAG = "BaseFragmentActivity";
    private static final String SELECT_TAB = "select_tab";

    private boolean hasStatusBar;

    private static WeakHashMap<String, WeakReference<BaseFragmentActivity>> sInstanceMap =
            new WeakHashMap<String, WeakReference<BaseFragmentActivity>>();

    private RXHelper mSubscribeHelper;
    private SwipeBackActivityHelper mHelper;//滑动返回
    private boolean canSwipeBack=true;
    private boolean canTakePhoto=false;//是否使用拍照或相册功能
    private InvokeParam invokeParam;//拍照需要使用的参数
    private TakePhoto takePhoto;//拍照需要使用的参数
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    private ActivityUIHelper mActivityHelper;

    protected String mCurrent;
    private boolean mHasAnimation = false;
    protected String mActivityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        hasStatusBar=getIntent().getBooleanExtra(CommonIntentExtra.EXTRA_HAS_STATUS_BAR,true);
        if (DEBUG) Log.d(TAG, "==== onCreate: " + getClass().getSimpleName());
        canTakePhoto=getIntent().getBooleanExtra(ExtraConstant.EXTRA_CAN_TAKE_PHOTO,canTakePhoto());
        if(canTakePhoto){
            getTakePhoto().onCreate(savedInstanceState);
        }
        super.onCreate(savedInstanceState);
        canSwipeBack=getIntent().getBooleanExtra(ExtraConstant.EXTRA_CAN_SWIPE_BACK,canSwipeBack());
        if(canSwipeBack){
            mHelper = new SwipeBackActivityHelper(this);
            mHelper.onActivityCreate();
            getSwipeBackLayout().setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);//EDGE_LEFT EDGE_RIGHT EDGE_BOTTOM EDGE_ALL
        }
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setStatusBar();
        int resId = getMainViewResId();
        if (resId >= 0) {
            setContentView(resId);
        } else {
            setContentView(createContentView(this), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
        }

        if (mActivityName != null) {
            BaseFragmentActivity instance = getSingleActivity();
            if (instance != null) {
                instance.finish();
            }
            synchronized (sInstanceMap) {
                sInstanceMap.put(mActivityName, new WeakReference<BaseFragmentActivity>(this));
            }
        }

        try {
            mHasAnimation = getIntent().getBooleanExtra(ExtraConstant.EXTRA_HAS_ANIM, true);
        } catch (Exception e) {
            // ignore
            e.printStackTrace();
        }

        if (savedInstanceState != null) {
            mCurrent = savedInstanceState.getString(SELECT_TAB);
        } else {
            mCurrent = getDefaultFragmentTag();
        }
    }
    protected boolean canSwipeBack(){
        return true;
    }
    protected boolean canTakePhoto(){
        return false;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        if(mHasAnimation){
            overridePendingTransition(R.anim.app_slide_right_in, R.anim.app_slide_hold);
        }
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
        if(canSwipeBack) {
            mHelper.onPostCreate();
        }
    }
    public ActivityUIHelper getActivityHelper() {
        if(mActivityHelper==null){
            mActivityHelper=new ActivityUIHelper(this);
        }
        return mActivityHelper;
    }
    public boolean hasStatusBar(){
        return hasStatusBar;
    }
    public void setStatusBar() {
        if(!hasStatusBar){
//            StatusBarUtil.transparencyBar(this);
            StatusBarUtil.statusBarLightMode(this,true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&  Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {//5.0
                StatusBarUtil.setStatusBarColor(this,R.color.common_60_percent_transparent);
            }

        }else{
//            StatusBarUtil.setStatusBarColor(this, R.color.common_title_bg_color);
//            StatusBarUtil.statusBarLightMode(this,false);
            if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.M) {//5.0
                StatusBarUtil.transparencyBar(this);
            }
            StatusBarUtil.statusBarLightMode(this,true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&  Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {//5.0
                StatusBarUtil.setStatusBarColor(this,R.color.common_10_percent_transparent);
            }
        }
    }

    protected View createContentView(Context context) {
        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);

        if(hasStatusBar ) {
            rootLayout.setClipToPadding(true);
            ViewCompat.setFitsSystemWindows(rootLayout, true);
        }

        FrameLayout content = new FrameLayout(context);
        content.setId(android.R.id.custom);
        content.setBackgroundResource(R.color.common_bg);
        rootLayout.addView(content, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        return rootLayout;
    }

    protected View getDefaultFragmentContainer() {
        return findViewById(android.R.id.custom);
    }

    private BaseFragmentActivity getSingleActivity() {
        if (mActivityName != null) {
            WeakReference<BaseFragmentActivity> ref = sInstanceMap.get(mActivityName);
            if (ref != null) {
                return ref.get();
            }
        }
        return null;
    }

    public void finishActivity(String ActivityName){
        if (ActivityName != null) {
            WeakReference<BaseFragmentActivity> ref = sInstanceMap.get(ActivityName);
            if (ref != null && ref.get()!=null) {
                ref.get().finish();
            }
        }
    }
    protected String getDefaultFragmentTag() {
        return DEFAULT_FRAGMENT_TAG;
    }

    protected Fragment loadFragment(Class<? extends Fragment> clazz) {
        return loadFragment(android.R.id.custom, clazz);
    }

    protected Fragment loadFragment(int resid, Class<? extends Fragment> clazz) {
        return loadFragment(resid, getDefaultFragmentTag(), clazz);
    }

    protected Fragment loadFragment(int resid, String tag, Class<? extends Fragment> clazz) {
        mCurrent = tag;

        boolean init = false;
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            try {
                init = true;
                fragment = clazz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (tag.equals(getDefaultFragmentTag())) {
            if (!init) ft.setCustomAnimations(R.anim.app_slide_hold, R.anim.app_slide_right_out);
        } else {
            ft.setCustomAnimations(R.anim.app_slide_right_in, R.anim.app_slide_hold);
        }
        ft.replace(resid, fragment, tag);
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();

        return fragment;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(SELECT_TAB, mCurrent);
        if(canTakePhoto) {
            getTakePhoto().onSaveInstanceState(outState);
        }
        super.onSaveInstanceState(outState);
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
    protected void onDestroy() {
        if (DEBUG) Log.d(TAG, "==== onDestroy: " + getClass().getSimpleName());
        unsubscribeEvent();
        cancelTask();
        super.onDestroy();
        MyActivityManager.getInstance().finishActivity(this);
        if (mActivityName != null) {
            BaseFragmentActivity instance = getSingleActivity();
            if (instance == this) {
                synchronized (sInstanceMap) {
                    sInstanceMap.remove(mActivityName);
                }
            }
        }
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
    public void onBackPressed() {
        finish();
    }

    public void overridePendingTransition(String leftAnim, String rightAnim) {
        String packageName = getPackageName();
        int left = getResources().getIdentifier(leftAnim, "anim", packageName);
        int right = getResources().getIdentifier(rightAnim, "anim", packageName);
        overridePendingTransition(left, right);
    }

    protected int getMainViewResId() {
        return -1;
    }


    protected RXHelper getSubscribeHelper() {
        if (mSubscribeHelper == null) {
            mSubscribeHelper = new RXHelper();
        }

        return mSubscribeHelper;
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

    private void unsubscribeEvent() {
        if (mSubscribeHelper != null) {
            mSubscribeHelper.unsubscribeEvent();
        }
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

    public void defaultRetrofitErrorHandle(Throwable t) {
        dismissProgress();
        ActivityUIHelper.showFailure(t, this);
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

}
