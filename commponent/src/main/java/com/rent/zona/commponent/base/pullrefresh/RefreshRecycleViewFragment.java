package com.rent.zona.commponent.base.pullrefresh;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.rent.zona.baselib.ThreedPool.ThreadManager;
import com.rent.zona.baselib.cache.CacheManagerFactory;
import com.rent.zona.baselib.cache.Query;
import com.rent.zona.baselib.configs.LibConfigs;
import com.rent.zona.baselib.log.LibLogger;
import com.rent.zona.baselib.network.httpbean.TaskException;
import com.rent.zona.baselib.utils.GsonFactory;
import com.rent.zona.commponent.base.pullrefresh.Adapter.PullToRefreshAdapter;
import com.rent.zona.commponent.R;
import com.rent.zona.commponent.base.BaseFragment;
import com.rent.zona.commponent.base.pullrefresh.Adapter.PullToRefreshRecycleAdapter;
import com.rent.zona.commponent.base.pullrefresh.bean.PageListDto;
import com.rent.zona.commponent.base.pullrefresh.header.DefaultClassicsHeader;
import com.rent.zona.commponent.helper.ActivityUIHelper;
import com.rent.zona.commponent.utils.CommonHandler;
import com.rent.zona.commponent.views.CommonEmptyView;
import com.rent.zona.commponent.views.EmptyRecyclerView;
import com.rent.zona.commponent.views.WebErrorView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

/**
* recycleview 的上下拉刷新
* @author liuyun
* @date 2018/4/23 0023
*/
public abstract class RefreshRecycleViewFragment<D> extends BaseFragment implements CommonHandler.MessageHandler,OnRefreshListener,OnLoadMoreListener, WebErrorView.WebErrorViewClickListener{
    private static final int MSG_LOAD_CACHE_FINISHED = 1000;
    private static final int DEFAULT_COUNT_PER_PAGE = 20;
    protected SmartRefreshLayout mSmartRefreshLayout;
    protected EmptyRecyclerView mContentRv;
    protected ViewGroup mContentRoot;
    CommonEmptyView mEmptyView;
    private String mEmptyText;
    final protected CommonHandler mHandler = new CommonHandler(this);
    protected PullToRefreshRecycleAdapter mAdapter;
    boolean mListShown;
    final private Runnable mRequestFocus = new Runnable() {
        public void run() {
            mContentRv.focusableViewAvailable(mContentRv);
        }
    };
    private boolean mAutoRefresh = true;
    protected long mDefaultAutoRefreshTime = 0; // 5 * DateTimeUtils.MINUTE_MS
    private boolean isDefaultLoadCache = true;//是否默认加载缓存
//    protected String mPageKey;
    private int mEmptyResId = -1;//空数据时显示的图片
    protected int mCountPerPage = DEFAULT_COUNT_PER_PAGE;//每页条数
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int resId = getLayoutResId();
        if (resId < 0) {
            resId=R.layout.pull_refresh_recycle_layout;
        }
            View root = inflater.inflate(resId, container, false);
            mSmartRefreshLayout=root.findViewById(R.id.smart_refresh_layout);
        mContentRoot=root.findViewById(R.id.content_root);
        mContentRv=root.findViewById(R.id.content_rv);
        mEmptyView=root.findViewById(R.id.empty);
        mEmptyView.getErrorView().setErrorViewClickListener(this);
        RefreshHeader refreshHeader=forRefreshHeader();
        if(refreshHeader!=null){
            mSmartRefreshLayout.setRefreshHeader(refreshHeader);
        }
        RefreshFooter refreshFooter=forRefreshFooter();
        if(refreshFooter!=null){
            mSmartRefreshLayout.setRefreshFooter(refreshFooter);
        }
        mContentRv.setEmptyView(mEmptyView);
            return root;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (TextUtils.isEmpty(mEmptyText)) {
            mEmptyText = getString(R.string.cube_views_load_more_loaded_empty);
        }
        ensureList();
    }
    private void ensureList() {
        mListShown = true;
        RecyclerView.LayoutManager layoutManager=forLayoutManager();
        if(layoutManager!=null){
            mContentRv.setLayoutManager(layoutManager);
        }
        if(mContentRv.getItemDecorationCount()==0) {
            RecyclerView.ItemDecoration itemDecoration = forItemDecoration();
            if (itemDecoration != null) {
                mContentRv.addItemDecoration(itemDecoration);
            }
        }
        if (mAdapter != null) {
            PullToRefreshRecycleAdapter adapter = mAdapter;
            mAdapter = null;
            setListAdapter(adapter);
        }
        mHandler.post(mRequestFocus);
    }
    public void setAutoRefresh(boolean autoRefresh) {
        mAutoRefresh = autoRefresh;
    }
    public void setDefaultAutoRefreshTime(long time) {
        mDefaultAutoRefreshTime = time;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListAdapter(onCreateAdapter(getActivity()));
        if (mContentRv != null) {

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
        if (isDefaultLoadCache) {
            tryLoadCache();
        }
        mSmartRefreshLayout.setOnRefreshListener(this);
        mSmartRefreshLayout.setOnLoadMoreListener(this);
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
                        PageListDto<D> cacheData = GsonFactory.getDefault().
                                fromJson(cacheContent, new TypeToken<PageListDto<D>>() {}.getType());
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
     * 刷新
     * @param showPullAnim 是否展示动画
     * @param isFullRefresh 是否是满屏刷新 （满屏刷新 屏幕中心显示进度条）
     */
    public void triggerRefresh(boolean showPullAnim,boolean isFullRefresh) {
        if (showPullAnim && mSmartRefreshLayout != null) {
            if (mEmptyView != null && isFullRefresh) {
                showLoading();
                refresh(mCountPerPage);
            } else {
                mSmartRefreshLayout.autoRefresh();
            }
        } else {
            showLoading();
            refresh(mCountPerPage);
        }
    }
    /**
     * 展示加载动画
     */
    protected void showLoading() {
        //mEmptyView.setEmptyText(R.string.common_waiting);
        mEmptyView.setEmptyText(null);
        mEmptyView.showLoading();
        setListShown(false);
        mSmartRefreshLayout.setEnableRefresh(false);
    }
    /**
     * Provide the cursor for the list view.
     */
    public void setListAdapter(PullToRefreshRecycleAdapter adapter) {
        boolean hadAdapter = mAdapter != null;
        mAdapter = adapter;
        if (mContentRv != null) {
            //mList.setAdapter(adapter);
            mContentRv.setAdapter(adapter);
            if (!mListShown && !hadAdapter) {
                // The list was hidden, and previously didn't have an
                // adapter.  It is now time to show it.
                setListShown(true);
            }
        }
    }
    private void setListShown(boolean shown) {
        ensureList();

        if (mListShown == shown) {
            return;
        }
        mListShown = shown;
        if (mEmptyView == null) {
            mContentRv.setVisibility(shown ? View.VISIBLE : View.GONE);
        } else {
            if (shown) {
                mEmptyView.setVisibility(View.GONE);
                mContentRv.setVisibility(View.VISIBLE);
            } else {
                mEmptyView.setVisibility(View.VISIBLE);
                mContentRv.setVisibility(View.GONE);
            }
        }
    }
    public final RecyclerView getPullToRefreshRecyclerView() {
        return mContentRv;
    }
//    protected void setPageKey(PageListDto<D> listDto) {
//        mPageKey = listDto.getPageKey();
//        // Update load more container
//        //mLoadMoreContainer.loadMoreFinish(mAdapter.isEmpty(), !TextUtils.isEmpty(mPageKey));
//    }
    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_LOAD_CACHE_FINISHED: {
                PageListDto<D> cacheData = (PageListDto<D>) msg.obj;
                replaceAllItems(cacheData);
//                setPageKey(cacheData);
                onCacheLoaded(cacheData);
            }
            break;
            default:
                break;
        }
    }
    /**
     * 保存缓存数据
     * @param data
     */

    protected void trySaveCache(final PageListDto<D> data) {
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
    protected void showEmpty(String emptyText) {
        if(!TextUtils.isEmpty(emptyText)){
            mEmptyView.setEmptyText(emptyText);
        }else{
            mEmptyView.setEmptyText(mEmptyText);
        }
        mEmptyView.showEmpty(mEmptyResId);
    }
    public void onLoadSuccess(PageListDto<D> listDto) {
        if (listDto != null) {
            if (mSmartRefreshLayout.getState()==RefreshState.Refreshing) {
                replaceAllItems(listDto);
                trySaveCache(listDto);
            } else if (mSmartRefreshLayout.getState()==RefreshState.Loading) {
                addItems(listDto);
            }else{
                replaceAllItems(listDto);
                trySaveCache(listDto);
            }
//            setPageKey(listDto);
        }
        if(mAdapter.isDataEmpty()){
            showEmpty(null);
        }
        setListShown(true);
        onLoadFinished(false);
        mSmartRefreshLayout.setNoMoreData(listDto.hasNoMore());
    }
    public void onLoadFailed(Throwable throwable) {
//        mSmartRefreshLayout.setNoMoreData(false);
        if(mSmartRefreshLayout.getState()==RefreshState.Refreshing){
            mAdapter.clear();
        }
        onLoadFinished(true);
        if (mEmptyView != null && mAdapter.isDataEmpty()) {
            if (throwable != null && throwable instanceof TaskException) {
                showEmpty(((TaskException)throwable).desc);
            } else {
                mEmptyView.showError();
            }
            if (LibConfigs.DEBUG_LOG) {
                LibLogger.w("PullToRefreshBaseListFragment", "onLoadFailed ", throwable);
            }
        } else {
            ActivityUIHelper.showFailure(throwable, getActivity());
        }
    }
    protected void onLoadFinished(boolean hasError) {
        mSmartRefreshLayout.setEnableRefresh(true);
        if(mSmartRefreshLayout.getState()== RefreshState.Loading){
            mSmartRefreshLayout.finishLoadMore(!hasError?true:false);
//            mSmartRefreshLayout.finishLoadMore(0,!hasError?true:false,!hasMore);
        }
        if(mSmartRefreshLayout.getState()==RefreshState.Refreshing){
            mSmartRefreshLayout.finishRefresh(!hasError?true:false);
        }

    }
    @Override
    public void onRefreshClicked() {
        triggerRefresh(true,true);
    }

    private void replaceAllItems(PageListDto<D> items) {
        mAdapter.replaceAllItems(items);
    }
    private void addItems(PageListDto<D> items) {
        mAdapter.add(items);
    }
    protected PageListDto<D> obtainPageListDto( ){
        PageListDto<D> pageListDto=new PageListDto<>();
        pageListDto.setPageSize(mCountPerPage);
        pageListDto.setTotal(0);
        pageListDto.setPageIndex(getNextPageIndex());
        return pageListDto;
    }
    protected PageListDto<D> obtainPageListDto(  int total){
        PageListDto<D> pageListDto=new PageListDto<>();
        pageListDto.setPageSize(mCountPerPage);
        pageListDto.setTotal(total);
        pageListDto.setPageIndex(getNextPageIndex());
        return pageListDto;
    }
    @Override
    public void onDestroyView() {
        mHandler.removeCallbacksAndMessages(null);
        mSmartRefreshLayout = null;
        mListShown = false;
        mEmptyView = null;
        mContentRv = null;
        mAdapter = null;
        super.onDestroyView();
    }
    protected int getLayoutResId() {
        return -1;
    }
    /**
     * 如果不想使用统一的header 就重写此方法
     */
    protected RefreshHeader forRefreshHeader(){
        return null;
    }
    protected RefreshFooter forRefreshFooter(){
        return null;
    }
//    protected abstract void setListAdapter(RecyclerView recyclerView, PullToRefreshRecycleAdapter adapter);
    protected abstract PullToRefreshRecycleAdapter onCreateAdapter(Context context);
    protected String cacheKey() {
        return null;
    }
    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        refresh(mCountPerPage);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        int nextPageIndex = (mAdapter.getItemCount() + (mCountPerPage - 1)) / mCountPerPage;
        loadMore( nextPageIndex , mCountPerPage);
    }
    private int getNextPageIndex(){
        return (mAdapter.getItemCount() + (mCountPerPage - 1)) / mCountPerPage;
    }

    protected abstract void refresh(int pageSize);
    protected abstract void loadMore(int pageIndex, int pageSize);
    protected void onCacheLoaded(PageListDto<D> cacheData) {

    }
    protected  abstract RecyclerView.LayoutManager forLayoutManager();
    protected abstract RecyclerView.ItemDecoration forItemDecoration();

}
