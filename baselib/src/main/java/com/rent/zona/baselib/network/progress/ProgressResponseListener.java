package com.rent.zona.baselib.network.progress;

/**
 */


public interface ProgressResponseListener {
    void onResponseProgress(long bytesRead, long contentLength, boolean done);
}
