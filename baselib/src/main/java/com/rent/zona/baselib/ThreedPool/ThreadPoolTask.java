package com.rent.zona.baselib.ThreedPool;

import android.os.Process;
import android.os.SystemClock;
import com.rent.zona.baselib.configs.LibConfigs;
import com.rent.zona.baselib.log.LibLogger;

import java.util.concurrent.FutureTask;

public class ThreadPoolTask extends FutureTask<Void> implements Comparable<ThreadPoolTask> {
    private static final String TAG = "ThreadPoolTask";

    public static final int PRIORITY_HIGH = 4;
    public static final int PRIORITY_NORMAL = 5;
    public static final int PRIORITY_LOW = 6;

    private static final long UI_TASK_RUNNING_TIME_WARNING = 1000 * 5; // 5 second

    private int mPriority = PRIORITY_NORMAL;
    private long mQueuedTime;
    private boolean mUiTask;
    private String mCallStack;

    /**
     * Same to {@link #ThreadPoolTask(java.lang.Runnable, boolean, int)} with {@link #PRIORITY_NORMAL}
     *
     * @param target
     */
    public ThreadPoolTask(Runnable target, boolean uiTask) {
        super(target, null);
        mUiTask = uiTask;
        saveCallStack();
    }

    /**
     * Construct a ThreadPoolTask object with specified target and priority.
     *
     * @param target
     * @param priority One of the priorities {@link #PRIORITY_NORMAL}, {@link #PRIORITY_LOW}
     *                 or {@link #PRIORITY_HIGH}
     */
    public ThreadPoolTask(Runnable target, boolean uiTask, int priority) {
        super(target, null);
        mPriority = priority;
        mUiTask = uiTask;
        saveCallStack();
    }

    private void saveCallStack() {
        if (LibConfigs.isDebugLog() && mUiTask) {
            mCallStack = getCallstack();
        }
    }
    /**
     * For debug purpose
     */
    public static String getCallstack() {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("Thread name: ").append(Thread.currentThread().getName()).append('\n');
        for (StackTraceElement element : stack) {
            strBuilder.append("\tat ").append(element).append('\n');
        }
        return strBuilder.toString();
    }
    int getPriority() {
        return mPriority;
    }

    void updateQueuedTime(long queuedTime) {
        mQueuedTime = queuedTime;
    }

    @Override
    public void run() {
        if (!mUiTask) {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        }
        long startTime = SystemClock.elapsedRealtime();
        super.run();
        long timeUsed = SystemClock.elapsedRealtime() - startTime;
        if (LibConfigs.isDebugLog() && mUiTask && timeUsed > UI_TASK_RUNNING_TIME_WARNING) {
            LibLogger.e(TAG, "heavy UI task found: " + timeUsed);
            LibLogger.w(TAG, mCallStack);
        }
    }

    @Override
    public int compareTo(ThreadPoolTask another) {
        if (mPriority < another.mPriority) {
            return -1;
        } else if (mPriority > another.mPriority) {
            return 1;
        } else {
            if (mQueuedTime < another.mQueuedTime) {
                return -1;
            } else if (mQueuedTime > another.mQueuedTime) {
                return 1;
            }
            return 0;
        }
    }
}
