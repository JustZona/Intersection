package com.rent.zona.commponent.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;


public class CommonHandler extends Handler {
    public interface MessageHandler {
        void handleMessage(Message msg);
    }

    private WeakReference<MessageHandler> mMessageHandler;

    public CommonHandler(MessageHandler msgHandler) {
        mMessageHandler = new WeakReference<>(msgHandler);
    }

    public CommonHandler(Looper looper, MessageHandler msgHandler) {
        super(looper);
        mMessageHandler = new WeakReference<>(msgHandler);
    }

    @Override
    public void handleMessage(Message msg) {
        MessageHandler realHandler = mMessageHandler.get();
        if (realHandler != null) {
            realHandler.handleMessage(msg);
        }
    }
}
