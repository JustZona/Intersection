package com.rent.zona.baselib.ThreedPool;


import com.rent.zona.baselib.configs.LibConfigs;
import com.rent.zona.baselib.log.LibLogger;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Scheduler;


public class ThreadManager {

    private static final String TAG = "AppThreadPool";
    private static Scheduler sScheduler;
    private WeakHashMap<Object, List<WeakReference<FutureTask<?>>>> futureTasks;

    private static class SingletonHolder {
        public static final ThreadManager INSTANCE = new ThreadManager();
    }

    private static class PoolThreadFactory implements ThreadFactory {
        private boolean mUiTask;
        private AtomicInteger mCount = new AtomicInteger(1);

        public PoolThreadFactory(boolean uiTask) {
            mUiTask = uiTask;
        }

        @Override
        public Thread newThread(Runnable r) {
            if (mUiTask) {
                return new Thread(r, "UiThreadPool#" + mCount.getAndIncrement());
            } else {
                return new Thread(r, "BkgThreadPool#" + mCount.getAndIncrement());
            }
        }
    }

    private ThreadPoolExecutor mUiThreadPoolExecutor;
    private ThreadPoolExecutor mBkgThreadPoolExecutor;

    public ThreadPoolExecutor getUiThreadPoolExecutor() {
        return mUiThreadPoolExecutor;
    }

    public ThreadPoolExecutor getBkgThreadPoolExecutor() {
        return mBkgThreadPoolExecutor;
    }


    public static ThreadManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private ThreadManager() {
//        int uiThreadsCount = getUiInitialThreadPoolSize();
//        int bkgThreadsCount = 1;
//
//        mUiThreadPoolExecutor = new ThreadPoolExecutor(uiThreadsCount, uiThreadsCount, 1,
//                TimeUnit.SECONDS, new PriorityBlockingQueue<Runnable>(), new PoolThreadFactory(true));
//        mBkgThreadPoolExecutor = new ThreadPoolExecutor(bkgThreadsCount, bkgThreadsCount, 1,
//                TimeUnit.SECONDS, new PriorityBlockingQueue<Runnable>(), new PoolThreadFactory(false));
//
//        futureTasks = new WeakHashMap<Object, List<WeakReference<FutureTask<?>>>>();

        int cpuCores = Runtime.getRuntime().availableProcessors();
        int uiPoolSize = Math.max(4, cpuCores / 2); // at least 4, or (cores/2)

        mUiThreadPoolExecutor = new ThreadPoolExecutor(uiPoolSize, uiPoolSize, 1,
                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new PoolThreadFactory(true));

        int bkgPoolSize = Math.max(4, cpuCores / 2); // at least 4, or (cores/2)
        mBkgThreadPoolExecutor = new ThreadPoolExecutor(bkgPoolSize, bkgPoolSize, 1,
                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new PoolThreadFactory(false));

        futureTasks = new WeakHashMap<Object, List<WeakReference<FutureTask<?>>>>();
    }

    private int getUiInitialThreadPoolSize() {
        int cpuCores = Runtime.getRuntime().availableProcessors();
        return Math.max(2, cpuCores / 2); // at least 2, or (cores/2)
    }

    /**
     * After the app started, we can make more threads to execute background jobs.
     */
    public void enlargePoolSize() {
        int cpuCores = Runtime.getRuntime().availableProcessors();
        int uiPoolSize = Math.max(4, cpuCores / 2); // at least 4, or (cores/2)
        mUiThreadPoolExecutor.setCorePoolSize(uiPoolSize);
        mUiThreadPoolExecutor.setMaximumPoolSize(uiPoolSize);

        int bkgPoolSize = Math.max(4, cpuCores / 2); // at least 4, or (cores/2)
        mBkgThreadPoolExecutor.setCorePoolSize(bkgPoolSize);
        mBkgThreadPoolExecutor.setMaximumPoolSize(bkgPoolSize);
    }

    /**
     * Add a new UI-related task with the priority {@link ThreadPoolTask#PRIORITY_NORMAL}.
     *
     * @param job The task to be execute
     */
    public ThreadPoolTask addUiTask(Runnable job) {
        ThreadPoolTask task = new ThreadPoolTask(job, true);
        task.updateQueuedTime(System.currentTimeMillis());
        mUiThreadPoolExecutor.execute(task);
        return task;
    }

    /**
     * Add a new UI-related task with specified priority.
     *
     * @param job      The task to be execute
     * @param priority One of the priorities {@link ThreadPoolTask#PRIORITY_NORMAL},
     *                 {@link ThreadPoolTask#PRIORITY_LOW} or {@link ThreadPoolTask#PRIORITY_HIGH}
     */
    public ThreadPoolTask addUiTask(Runnable job, int priority) {
        ThreadPoolTask task = new ThreadPoolTask(job, true, priority);
        task.updateQueuedTime(System.currentTimeMillis());
        mUiThreadPoolExecutor.execute(task);
        return task;
    }

    /**
     * Add a new non-UI-related task with the priority {@link ThreadPoolTask#PRIORITY_NORMAL}.
     *
     * @param job The task to be execute
     */
    public ThreadPoolTask addBkgTask(Runnable job) {
        ThreadPoolTask task = new ThreadPoolTask(job, false);
        task.updateQueuedTime(System.currentTimeMillis());
        mBkgThreadPoolExecutor.execute(task);
        return task;
    }

    /**
     * Add a new non-UI-related task with specified priority.
     *
     * @param job      The task to be execute
     * @param priority One of the priorities {@link ThreadPoolTask#PRIORITY_NORMAL},
     *                 {@link ThreadPoolTask#PRIORITY_LOW} or {@link ThreadPoolTask#PRIORITY_HIGH}
     */
    public ThreadPoolTask addBkgTask(Runnable job, int priority) {
        ThreadPoolTask task = new ThreadPoolTask(job, false, priority);
        task.updateQueuedTime(System.currentTimeMillis());
        mBkgThreadPoolExecutor.execute(task);
        return task;
    }

    public void execute(Runnable job, Object contextKey) {
        execute(job, ThreadPoolTask.PRIORITY_NORMAL, contextKey);
    }

    public void execute(Runnable job, int priority, Object contextKey) {
        ThreadPoolTask task = addBkgTask(job, priority);
        addRequestReference(task, contextKey);
    }

    public void execute(ThreadPoolTask task, Object contextKey) {
        task.updateQueuedTime(System.currentTimeMillis());
        mBkgThreadPoolExecutor.execute(task);
        addRequestReference(task, contextKey);
    }

    private void addRequestReference(FutureTask<Void> futureTask, Object contextKey) {
        if (contextKey != null) {
            List<WeakReference<FutureTask<?>>> requestList;
            synchronized (futureTasks) {
                requestList = futureTasks.get(contextKey);
                if (requestList == null) {
                    requestList = Collections.synchronizedList(new LinkedList<WeakReference<FutureTask<?>>>());
                    futureTasks.put(contextKey, requestList);
                }
            }
            Iterator<WeakReference<FutureTask<?>>> iterator = requestList.iterator();
            while (iterator.hasNext()) {
                FutureTask<?> fTask = iterator.next().get();
                if (fTask == null || fTask.isDone() || fTask.isCancelled()) {
                    iterator.remove();
                }
            }
            requestList.add(new WeakReference<FutureTask<?>>(futureTask));
        }
    }

    public void executeUiTask(Runnable job, Object contextKey) {
        executeUiTask(job, ThreadPoolTask.PRIORITY_NORMAL, contextKey);
    }

    public void executeUiTask(Runnable job, int priority, Object contextKey) {
        ThreadPoolTask task = addUiTask(job, priority);
        addRequestReference(task, contextKey);
    }

    public void executeUiTask(ThreadPoolTask task, Object contextKey) {
        task.updateQueuedTime(System.currentTimeMillis());
        mUiThreadPoolExecutor.execute(task);
        addRequestReference(task, contextKey);
    }

    public void cancelTask(Object contextKey, boolean mayInterruptIfRunning) {
        List<WeakReference<FutureTask<?>>> requestList = futureTasks
                .get(contextKey);
        if (requestList != null) {
            for (WeakReference<FutureTask<?>> requestRef : requestList) {
                Future<?> request = requestRef.get();
                if (request != null) {
                    request.cancel(mayInterruptIfRunning);
                }
            }
            futureTasks.remove(contextKey);
        }
    }

    public void cancelAllTask(boolean mayInterruptIfRunning) {
        Set<Object> keys = futureTasks.keySet();
        for (Object contextKey : keys) {
            List<WeakReference<FutureTask<?>>> requestList = futureTasks
                    .get(contextKey);
            if (requestList != null) {
                for (WeakReference<FutureTask<?>> requestRef : requestList) {
                    Future<?> request = requestRef.get();
                    if (request != null) {
                        request.cancel(mayInterruptIfRunning);
                    }
                }
            }
        }
        futureTasks.clear();
    }

    public void dumpState(String logPrefix) {
        if (LibConfigs.isDebugLog()) {
            LibLogger.d(TAG, logPrefix + "-UiTaskPool, PoolCoreSize: " + mBkgThreadPoolExecutor.getCorePoolSize()
                    + ", ActiveThreadCount: " + mBkgThreadPoolExecutor.getActiveCount()
                    + ", CompletedTaskCount: " + mBkgThreadPoolExecutor.getCompletedTaskCount()
                    + ", CurPoolSize:" + mBkgThreadPoolExecutor.getPoolSize()
                    + ", ScheduledTaskCount: " + mBkgThreadPoolExecutor.getTaskCount()
                    + ", QueueSize: " + mBkgThreadPoolExecutor.getQueue().size());
            LibLogger.d(TAG, logPrefix + "-BkgTaskPool, PoolCoreSize: " + mBkgThreadPoolExecutor.getCorePoolSize()
                    + ", ActiveThreadCount: " + mBkgThreadPoolExecutor.getActiveCount()
                    + ", CompletedTaskCount: " + mBkgThreadPoolExecutor.getCompletedTaskCount()
                    + ", CurPoolSize:" + mBkgThreadPoolExecutor.getPoolSize()
                    + ", ScheduledTaskCount: " + mBkgThreadPoolExecutor.getTaskCount()
                    + ", QueueSize: " + mBkgThreadPoolExecutor.getQueue().size());
        }
    }
}
