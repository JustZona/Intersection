package com.fangyizhan.intersection.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.scwang.smartrefresh.layout.util.DensityUtil;

public class MyToggleBtn extends View implements View.OnClickListener{
    /**
     * 最小高度
     */
    private float miniHeight;
    /**
     * 最小宽度
     */
    private float miniWidth;
    private Paint paint;
    private int slideLeftMax;
     /**
       * 选择开关左侧的文字信息
       */
     private static final String showTextLeft = "登录";
        /**
      * 选择开关右侧的文字信息
      */
      private static final String showTextRight = "注册";
        //默认显示右侧
     private String showText ;

    public MyToggleBtn(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    private Context ctx;
    /**
     *  初始化
     */
    private void init(Context context){
        ctx=context;
        showText=showTextLeft;
        paint=new Paint();
        paint.setAntiAlias(true);//抗锯齿
        paint.setTextSize(DensityUtil.dp2px(15));
        Paint.FontMetrics fontMetrics=paint.getFontMetrics();
        miniHeight=fontMetrics.bottom-fontMetrics.top+getPaddingTop()+getBottom()+DensityUtil.dp2px(15);
        float textWidth=0;
        textWidth=paint.measureText(showTextLeft);//貌似没啥用
        paint.measureText(showTextLeft);
        paint.measureText(showTextRight);
        miniWidth=(float)(1.5*miniHeight+textWidth+getPaddingLeft()+getPaddingRight()+DensityUtil.dp2px(10));
        //滑动图片 左边界的最大值
         slideLeftMax=(int)(miniWidth-miniHeight);
        paint.setColor(Color.WHITE);
        setOnClickListener(this);
    }

    /**
     * 当前的开关状态
     * true为开
     * false为关
     */
    private boolean currState=false;
    private int slideLeft=0;
    private boolean isScroll;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制背景图
        paint.setColor(Color.RED);
        paint.setStrokeWidth(1);
        paint.setStyle(paint.getStyle().FILL);
        canvas.drawCircle((float)(miniHeight*0.5),(float)(miniHeight*0.5),(float)(miniHeight*0.5),paint);
        canvas.drawCircle((float)(miniWidth-miniHeight*0.5), (float)(miniHeight*0.5), (float)(miniHeight*0.5), paint);
        canvas.drawRect((float)(miniHeight*0.5), 0, (float)(miniWidth-miniHeight*0.5), miniHeight, paint);
        float textWidth = paint.measureText(showText);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float fontTotalHeight = fontMetrics.bottom - fontMetrics.top;
        float newY = getMeasuredHeight() / 2 - fontMetrics.descent+ (fontMetrics.descent - fontMetrics.ascent) / 2;
        float baseX = 0;
        paint.setColor(Color.BLACK);
         if(!currState){
       baseX = (float) ((getMeasuredWidth()-textWidth)-getPaddingRight()-miniHeight*0.5);
       }else{
     baseX = (float) (0+getPaddingLeft()+miniHeight*0.5);
      }
      canvas.drawText(showText, baseX, newY, paint);
        // 绘制滑动图片
        //canvas.drawBitmap(slideBitmap, slideLeft, 0, paint);
        paint.setColor(Color.WHITE);
        canvas.drawCircle((float)(slideLeft+miniHeight*0.5), (float)(miniHeight*0.5), (float)(miniHeight*0.5+0.5), paint);
}

    /**
     * 指定view的大小
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //当前view的宽度，就和背景图的大小一致
        int measuredWidth=(int)miniWidth;
        int measuredHeight=(int)miniHeight;
        //指定测量的宽高
        setMeasuredDimension(measuredWidth,measuredHeight);
    }
    Handler handler=new Handler();
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            //要做的事情
            if (currState){
                if (slideLeft<slideLeftMax){
                    isScroll=true;
                    slideLeft+=20;
                    handler.postDelayed(this,100);
                }else {
                    isScroll=false;
                }
            }else {
                if (slideLeft>0){
                    isScroll=true;
                    slideLeft-=15;
                    handler.postDelayed(this,100);
                }else {
                    isScroll=false;
                }
            }
            ((Activity)(ctx)).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (currState){//开状态
                        showText=showTextLeft;
                        if (onSwitchListener!=null){
                            onSwitchListener.OnRightClick();
                        }

                    }else {
                        showText=showTextRight;
                        if (onSwitchListener!=null){
                            onSwitchListener.OnLeftClick();
                        }
                    }
                    flushView();
                }
            });
        }
    };
        private IOnSwitchListener onSwitchListener;
        public interface IOnSwitchListener{
        public void OnLeftClick();
        public void OnRightClick();
}
        public void setOnSwitchListener(IOnSwitchListener onSwitchListener){
            this.onSwitchListener = onSwitchListener;
}

    /**
     * 响应view的点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
    //如果发生了滑动的动作，就不执行以下代码
        if (isSliding){
            return;
        }
        if (isScroll){
            return;
        }
        currState=!currState;
        handler.postDelayed(runnable,100);
        //切换按钮的开关状态

    }
    /**
     * 刷新状态
     * 根据当前的状态，刷新页面
     */
    private void flushState(){
        if (currState){//开状态
            showText=showTextLeft;
            slideLeft=slideLeftMax;
            if (onSwitchListener!=null){
                onSwitchListener.OnRightClick();
            }

        }else {
            showText=showTextRight;
            //关状态
            slideLeft=0;
            if (onSwitchListener!=null){
                onSwitchListener.OnLeftClick();
            }
        }
        flushView();
    }
    //上一个事件中的X坐标
    private int lastX;
    //down事件中的X坐标
    private int downX;
    /**
     * 判断触摸时，是否发生滑动事件
     */
    private boolean isSliding=false;

    /**
     * 重写该方法 处理触摸事件
     * 如果该view消费了事件，那么 返回true
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // super 注释掉以后，onclick事件，就失效了，因为，点击这个动作，也是从onTouchEvent 方法中解析出来，符合一定的要求，就是一个点击事件
        // 系统中，如果发现，view产生了up事件，就认为，发生了onclick动作,就行执行listener.onClick方法

        super.onTouchEvent(event);
        /*
         * 点击切换开关，与触摸滑动切换开关，就会产生冲突
         * 我们自己规定，如果手指在屏幕上移动，超过15个象素，就按滑动来切换开关，同时禁用点击切换开关的动作
         */
        if (isScroll){
            return true;
        }
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX=lastX=(int)event.getX();//获得相对于当前view的坐标
//                event.getRawX();//是相对于屏幕的坐标
                //down事件发生时，肯定不是滑动的动作
                isSliding=false;
                break;
            case MotionEvent.ACTION_MOVE:
                //获得距离
                int disX=(int)(event.getX()-lastX);
                //改变滑动图片的左边界
                slideLeft+=disX;
                flushView();
                //为lastX重新赋值
                lastX=(int)event.getX();
                //判断是否发生滑动事件
                if (Math.abs(event.getX()-downX)>15){//手指在屏幕上滑动的距离大于15像素
                    isSliding=true;

                }
                break;
                case MotionEvent.ACTION_UP:
                    if (isSliding){
                        if (slideLeft>slideLeftMax/2){
                            currState=true;
                        }else {
                            currState=false;
                        }
                        flushState();
                    }
                    break;
        }
        return true;
    }

    /*
    刷新页面
     */

    private void flushView(){
        //保证slideLeft>=0 同时<=slideLeftMax
        if (slideLeft<0){
            slideLeft=0;
        }
        if (slideLeft>slideLeftMax){
            slideLeft=slideLeftMax;
        }
        invalidate();//刷新页面
    }
}
