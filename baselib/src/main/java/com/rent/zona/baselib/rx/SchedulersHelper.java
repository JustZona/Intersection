package com.rent.zona.baselib.rx;


import com.rent.zona.baselib.ThreedPool.ThreadManager;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;


public final class SchedulersHelper {
    private final Scheduler mUiScheduler;
    private final Scheduler mBkgThreadScheduler;

    private static final SchedulersHelper INSTANCE = new SchedulersHelper();

    private SchedulersHelper() {
        ThreadManager threadManager = ThreadManager.getInstance();
        mUiScheduler = Schedulers.from(threadManager.getUiThreadPoolExecutor());
        mBkgThreadScheduler = Schedulers.from(threadManager.getBkgThreadPoolExecutor());
    }

    public static Scheduler ui() {
        return INSTANCE.mUiScheduler;
    }

    public static Scheduler background() {
        return INSTANCE.mBkgThreadScheduler;
    }
}
