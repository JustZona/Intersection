package com.fangyizhan.intersection.views;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by Zona on 2018/4/8.
 */

public class WelcomeSkipButton extends Button {
    /**
     * 暂停时间.
     * 毫秒.
     */
    private int pauseTime = 4000;

    /**
     * 间隔时间.
     */
    private int interval = 1000;

    private Handler handler;

    private OnchangeTimerTask onchangeTimerTask;

    private int time;

    private CountDownTimer countDownTimer;

    public WelcomeSkipButton(Context context) {
        super(context);
        init();
    }

    public WelcomeSkipButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WelcomeSkipButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (onchangeTimerTask != null) {
                    time = pauseTime / interval;
                    onchangeTimerTask.changeTime(time);
                    pauseTime = pauseTime - interval;
                }
            }
        };
    }

    public void startTimerTask() {
        countDownTimer = new CountDownTimer(pauseTime, interval) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (onchangeTimerTask != null) {
                    onchangeTimerTask.changeTime((int) (millisUntilFinished / 1000) - 1);
                }
            }

            @Override
            public void onFinish() {
                if (onchangeTimerTask != null) {
                    onchangeTimerTask.onFinish();
                }
            }
        }.start();
    }

    public void stop() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = null;
    }

    /**
     * 设置暂停时间.
     *
     * @param pauseTime
     */
    public void setPauseTime(int pauseTime) {
        this.pauseTime = pauseTime;
    }

    /**
     * 设置间隔时间
     *
     * @param interval
     */
    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void setOnchangeTimerTask(OnchangeTimerTask onchangeTimerTask) {
        this.onchangeTimerTask = onchangeTimerTask;
    }

    /**
     * 定时监听.
     */
    public interface OnchangeTimerTask {
        public void changeTime(int time);

        public void onFinish();
    }
}
