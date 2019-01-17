package com.rent.zona.commponent.base.pullrefresh;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.gson.reflect.TypeToken;
import com.rent.zona.baselib.ThreedPool.ThreadManager;
import com.rent.zona.baselib.cache.CacheManagerFactory;
import com.rent.zona.baselib.cache.Query;
import com.rent.zona.baselib.configs.LibConfigs;
import com.rent.zona.baselib.log.LibLogger;
import com.rent.zona.baselib.network.httpbean.TaskException;
import com.rent.zona.baselib.utils.GsonFactory;
import com.rent.zona.commponent.R;
import com.rent.zona.commponent.base.BaseFragment;
import com.rent.zona.commponent.base.pullrefresh.header.DefaultClassicsHeader;
import com.rent.zona.commponent.helper.ActivityUIHelper;
import com.rent.zona.commponent.utils.CommonHandler;
import com.rent.zona.commponent.views.CommonEmptyView;
import com.rent.zona.commponent.views.WebErrorView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

/**
 * 下拉布局  布局中一定要有id smart_refresh_layout @android:id/content empty
 *  D缓存数据对象
 * @param
 */
public abstract class PllToRefreshBaseFragment<D> extends BaseFragment implements CommonHandler.MessageHandler, WebErrorView.WebErrorViewClickListener
          ,OnRefreshListener{
    private static final int MSG_LOAD_CACHE_FINISHED = 1000;//缓存数据加载完成
    protected SmartRefreshLayout mSmartRefreshLayout;
    protected CommonEmptyView mEmptyView;
    private String mEmptyText;
    protected ViewGroup mContentRootView;
//    boolean mContentShown;
    private boolean mAutoRefresh = true;
    protected long mDefaultAutoRefreshTime = 0; // 刷新间隔时间  单位毫秒  当下次进入这个页面没有超过这个时间 则加载缓存数据  否则刷新数据
    private boolean isDefaultLoadCache = true;//是否默认加载缓存
    final protected CommonHandler mHandler = new CommonHandler(this);
    final private Runnable mRequestFocus = new Runnable() {
        public void run() {
            mContentRootView.focusableViewAvailable(mContentRootView);
        }
    };
    private int mEmptyResId = -1;//空数据时显示的图片
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int resId = getLayoutResId();
        if (resId < 0) {
            resId = R.layout.pull_refresh_layout;
        }
        View root = inflater.inflate(resId, container, false);
        mSmartRefreshLayout=root.findViewById(R.id.smart_refresh_layout);
        if(getContentView()!=-1){
            ViewGroup contentView = (ViewGroup) root.findViewById(R.id.content_root);
            if(contentView!=null){
                contentView.addView(inflater.inflate(getContentView(),null),0);
            }
        }
        mEmptyView=root.findViewById(R.id.empty);
        if (mEmptyView == null && useDefaultEmptyView()) {
            mEmptyView = createDefaultEmptyView();
            showLoading();
        }else {
            mEmptyView.getErrorView().setErrorViewClickListener(this);
            mEmptyView.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(mEmptyText)) {
                mEmptyView.setEmptyText(mEmptyText);
            }
        }
        mSmartRefreshLayout.setEnableLoadMore(false);
        mSmartRefreshLayout.setOnRefreshListener(this);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (TextUtils.isEmpty(mEmptyText)) {
            mEmptyText = getString(R.string.cube_views_load_more_loaded_empty);
        }
        ensureContent();
        if (mSmartRefreshLayout != null) {
            RefreshHeader refreshHeader=forRefreshHeader();
            if(refreshHeader!=null){
                mSmartRefreshLayout.setRefreshHeader(refreshHeader);
            }
            //加载缓存数据
            long lastUpdateTime = 0;
            if(mSmartRefreshLayout.getRefreshHeader()!=null && mSmartRefreshLayout.getRefreshHeader() instanceof DefaultClassicsHeader){
                lastUpdateTime=((DefaultClassicsHeader)mSmartRefreshLayout.getRefreshHeader()).getLastUpdeTime();
            }
            if (mAutoRefresh &&
                    (lastUpdateTime == 0 || (System.currentTimeMillis() - lastUpdateTime > mDefaultAutoRefreshTime))) {
                mHandler.postDelayed(() -> triggerRefresh(true,true), 0);
            }
        }
        if (isDefaultLoadCache) {
            tryLoadCache();
        }
    }
    /**
     * 刷新
     * @param showPullAnim 是否展示动画
     * @param isFullRefresh 是否是满屏刷新 （满屏刷新 屏幕中心显示进度条）
     */
    public void triggerRefresh(boolean showPullAnim,boolean isFullRefresh) {
        if (showPullAnim && mSmartRefreshLayout != null) {
            if (mEmptyView != null && isFullRefresh) {
                showLoading();
                refresh();
            } else {
                mSmartRefreshLayout.autoRefresh();
            }
        } else {
            showLoading();
            refresh();
        }
    }
    //读取cache
    protected void tryLoadCache() {
        final String cacheKey = cacheKey();
        if (!TextUtils.isEmpty(cacheKey)) {
            ThreadManager.getInstance().executeUiTask(new Runnable() {
                @Override
                public void run() {
                    Query<String> query = new Query<String>(CacheManagerFactory.getDefault(), cacheKey);
                    String cacheContent = query.querySync();
                    if (cacheContent != null) {
                        D cacheData = GsonFactory.getDefault().
                                fromJson(cacheContent, new TypeToken<D>(){}.getType());
                        if (cacheData != null) {
                            Message msg = Message.obtain(mHandler, MSG_LOAD_CACHE_FINISHED, cacheData);
                            mHandler.sendMessage(msg);
                        }
                    }
                }
            }, this);
        }
    }



    /**
     * 保存缓存数据
     * @param data
     */
    protected void trySaveCache(final D data) {
        final String cacheKey = cacheKey();
        if (!TextUtils.isEmpty(cacheKey)) {
            ThreadManager.getInstance().addUiTask(new Runnable() {
                @Override
                public void run() {
                    String cacheContent = GsonFactory.getDefault().toJson(data);//异步
                    CacheManagerFactory.getDefault().setCacheDataSync(cacheKey, cacheContent);//同步
                }
            });
        }
    }
    protected void updateEmptyViewShown() {
        if (mEmptyView != null) {
            if (!isDataEmpty()){
                mEmptyView.setVisibility(View.GONE);
            }else{
                showEmpty();
            }
        }
    }
    protected void showEmpty() {
        showEmpty(null);
    }
    protected void showEmpty(String emptyText) {
        if(!TextUtils.isEmpty(emptyText)){
            mEmptyView.setEmptyText(emptyText);
        }else{
            mEmptyView.setEmptyText(mEmptyText);
        }
        mEmptyView.showEmpty(mEmptyResId);
    }
    /**
     * shown true展示内容 false 展示emptyview
     * @param shown
     */
    protected void setContentShown(boolean shown) {
        ensureContent();

//        if(mContentView.isShown()==shown){
//            return;
//        }
        View contentView=mContentRootView.getChildAt(0);
        if (mEmptyView == null) {
            contentView.setVisibility(shown ? View.VISIBLE : View.GONE);
        } else {
            if (shown) {
                mEmptyView.setVisibility(View.GONE);
                contentView.setVisibility(View.VISIBLE);
            } else {
                mEmptyView.setVisibility(View.VISIBLE);
                contentView.setVisibility(View.GONE);
            }
        }
    }
    /**
     * contentview 初始化
     */
    private void ensureContent() {
        if (mContentRootView != null) {
            return;
        }
        View root = getView();
        if (root == null) {
            throw new IllegalStateException("Content view not yet created");
        }

        ViewGroup contentView = (ViewGroup) root.findViewById(R.id.content_root);
        if (contentView == null) {
            throw new RuntimeException(
                    "Your content must have a ListView whose id attribute is " +
                            "'android.R.id.list'");
        }

        mContentRootView = (ViewGroup) contentView;

        mHandler.post(mRequestFocus);
    }


    /**
     * 展示加载动画
     */
    protected void showLoading() {
        //mEmptyView.setEmptyText(R.string.common_waiting);
        mEmptyView.setEmptyText(null);
        mEmptyView.showLoading();
        setContentShown(false);
        mSmartRefreshLayout.setEnableRefresh(false);
    }
    private CommonEmptyView createDefaultEmptyView() {
        if(mContentRootView!=null) {
            CommonEmptyView emptyView = new CommonEmptyView(getActivity());
            emptyView.setId(R.id.empty);
            emptyView.getErrorView().setErrorViewClickListener(this);
            emptyView.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(mEmptyText)) {
                emptyView.setEmptyText(mEmptyText);
            }

            mContentRootView.addView(emptyView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            return emptyView;

        }
        return null;
    }
    public void onLoadSuccess(D listDto) {
        if (listDto != null) {
                trySaveCache(listDto);
        }
        updateEmptyViewShown();
        setContentShown(true);
        onLoadFinished(false);
    }

    public void onLoadFailed(Throwable throwable) {
        onLoadFinished(true);
        if (mEmptyView != null ) {
            if (throwable != null && throwable instanceof TaskException) {
                showEmpty(((TaskException)throwable).desc);
            } else {
                mEmptyView.showError();
            }
            if (LibConfigs.DEBUG_LOG) {
                LibLogger.w("PullToRefreshBaseListFragment", "onLoadFailed ", throwable);
            }
            setContentShown(false);
        } else {
            ActivityUIHelper.showFailure(throwable, getActivity());
        }
    }
    protected void onLoadFinished(boolean hasError) {
        mSmartRefreshLayout.setEnableRefresh(true);
        if(mSmartRefreshLayout.getState()== RefreshState.Loading){
            mSmartRefreshLayout.finishLoadMore(!hasError?true:false);
        }
        if(mSmartRefreshLayout.getState()==RefreshState.Refreshing){
            mSmartRefreshLayout.finishRefresh(!hasError?true:false);
        }

    }
    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_LOAD_CACHE_FINISHED: {
                D cacheData = (D) msg.obj;
                onCacheLoaded(cacheData);
            }
            break;
            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        mHandler.removeCallbacksAndMessages(null);
        mContentRootView = null;
        mEmptyView = null;
        mSmartRefreshLayout = null;
        super.onDestroyView();
    }
    protected boolean useDefaultEmptyView() {
        return true;
    }
    @Override
    public void onRefreshClicked() {
        triggerRefresh(true,true);
    }

    /**
     * 判断是否是空数据
     * @return
     */
    protected  boolean isDataEmpty(){
        return false;
    };
    /**
     * 布局文件layout  用默认的 返回-1
     * @return
     */
    protected abstract int getLayoutResId();

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        refresh();
    }
    protected abstract void refresh();
    /**
     * 如果使用缓存数据功能 请重写此方法 数据缓存key
     * @return
     */
    protected String cacheKey(){
        return null;
    }
    /**
     * 如果不想使用统一的header 就重写此方法
     */
    protected RefreshHeader forRefreshHeader(){
        return null;
    }
    /**
     * 如果使用缓存数据功能 请重写此方法 数据缓存key
     * @return
     */
    protected void onCacheLoaded(D cacheData){};
    /**
     * 内容view   如果使用默认的布局  需重写此方法 以获取内容布局
     * @return
     */
    public int getContentView(){
        return -1;
    }
}
