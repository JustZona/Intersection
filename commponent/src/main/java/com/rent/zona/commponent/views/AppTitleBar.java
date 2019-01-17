package com.rent.zona.commponent.views;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rent.zona.baselib.cache.Query;
import com.rent.zona.commponent.BuildConfig;
import com.rent.zona.commponent.R;
import com.rent.zona.commponent.utils.DensityUtils;
import com.rent.zona.commponent.utils.ResourceUtils;


public class AppTitleBar extends RelativeLayout {
    private RelativeLayout mArrowTitleLayout;
    private TextView mArrowTitle;
    private ImageView mArrowImg;
    private boolean mArrowShow;
    private int rightTextColor;

    public interface OnTitleClickListener {
        void onExtendClick();
    }

    public interface OnArrowTitleClickListener {
        void onArrowTitleClick();
    }

    private Context mContext;
    private TextView mBack;
    private TextView mTitle;
    private FrameLayout mTitleExtend;

    public AppTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.AppTitleBar);
        CharSequence title = a.getText(R.styleable.AppTitleBar_title);
        a.recycle();

        setupViews();
        if (title != null) {
            setTitle(title);
        }

    }
    public void setTopPaddingForBar(){
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.KITKAT || (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT<Build.VERSION_CODES.M)){
            return;
        }
        /**
         * 获取状态栏高度——方法1
         * */
        int statusBarHeight1 = -1;
        //获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);
        }
        setPadding(getPaddingLeft(),statusBarHeight1,getPaddingRight(),getPaddingBottom());
        ViewGroup.LayoutParams layoutParams=getLayoutParams();
        if(layoutParams!=null){
            layoutParams.height+=statusBarHeight1;
        }else{
            layoutParams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,statusBarHeight1+ (int) getResources().getDimension(R.dimen.common_titlebar_height));

        }
        setLayoutParams(layoutParams);
    }

    private void setupViews() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        inflater.inflate(R.layout.view_app_titlebar, this, true);
        rightTextColor = getResources().getColor(R.color.common_dark);
        mBack = (TextView) findViewById(R.id.back);
        mTitle = (TextView) findViewById(R.id.title_text);
        mTitleExtend = (FrameLayout) findViewById(R.id.title_extend);
        mArrowTitleLayout = (RelativeLayout) findViewById(R.id.arrow_layout);
        mArrowTitle = (TextView) findViewById(R.id.arrow_title_text);
        mArrowImg = (ImageView) findViewById(R.id.arrow_img);

        mArrowTitleLayout.setBackgroundDrawable(ResourceUtils.createShape(
                ResourceUtils.adjustAlpha(getResources().getColor(R.color.white), 0.3f), 0, -1, DensityUtils.dip2px(mContext, 100)));

//        setBackgroundResource();
        setBackgroundResource(R.drawable.app_titlebar_bottomline_bg);//R.drawable.app_titlebar_bottomline_bg  bg_title


        post(new Runnable() {
            @Override
            public void run() {
                setTopPaddingForBar();
            }
        });
//        setTransparentTheme();
    }
    public void setWhiteTheme() {
        this.setBackResourceId(R.drawable.sel_titlebar_back);
        this.setBackgroundResource(R.drawable.app_titlebar_bg);
        this.setTitleColor(R.color.common_dark);
        this.setRightTextColor(this.mContext.getResources().getColor(R.color.common_dark));
    }
    public void setTransparentTheme() {
        this.setBackResourceId(R.drawable.icon_back);
        this.setBackgroundResource(R.drawable.bg_title);
        this.setTitleColor(-1);
        this.setRightTextColor(-1);
        this.setLeftTextColor(-1);
    }
    public void setLeftTextColor(int color) {
        View left = this.findViewById(R.id.left_title_bar);
        if (left instanceof TextView) {
            ((TextView)left).setTextColor(color);
        }

    }

    public void setLeftTextSize(int size) {
        View left = this.findViewById(R.id.left_title_bar);
        if (left instanceof TextView) {
            ((TextView)left).setTextSize(2, (float)size);
        }

    }

    public void setRightTextSize(int size) {
        View left = this.findViewById(R.id.right_title_bar);
        if (left instanceof TextView) {
            ((TextView)left).setTextSize(2, (float)size);
        }
    }

    public void setRightTextColor(int color) {
        this.rightTextColor = color;
        View left = this.findViewById(R.id.right_title_bar);
        if (left instanceof TextView) {
            ((TextView)left).setTextColor(color);
        }

    }
    public void hasBottomLine(boolean hasBottomLine){
        if(hasBottomLine){
            setBackgroundResource(R.drawable.app_titlebar_bottomline_bg);
        }else {
            setBackgroundResource(R.drawable.app_titlebar_bg);
        }
    }
    public AppTitleBar setBackListener(final OnBackStackListener listener) {
        if (listener != null) {
            mBack.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onBackStack();
                }
            });
            mBack.setFocusable(true);
        }
        return this;
    }

    public AppTitleBar setTitleExtendListener(final OnTitleClickListener listener) {
        if (listener != null) {
            mTitleExtend.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onExtendClick();
                }
            });
        }
        return this;
    }

    public AppTitleBar setArrowTitleListener(OnArrowTitleClickListener listener) {
        if (listener != null) {
            mArrowTitleLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onArrowTitleClick();
                    arrowChange();
                }
            });
        }
        return this;
    }

    public void arrowChange() {
        mArrowImg.clearAnimation();
        float start = 0f;
        float end = 180f;
        if (mArrowImg.getRotationX() == start) {
            start = 0f;
            end = 180f;
        } else {
            end = 0f;
            start = 180f;
        }
        ObjectAnimator anim = ObjectAnimator.ofFloat(mArrowImg, "rotationX", start, end);
        anim.setDuration(300);
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.start();
    }

    public void hideBack() {
        mBack.setVisibility(View.GONE);
    }

    public void showBack() {
        mBack.setVisibility(View.VISIBLE);
    }

    public void showArrowTitle(boolean show) {
        CharSequence title = getTitle();
        mArrowShow = show;
        if (show) {
            mArrowTitleLayout.setVisibility(View.VISIBLE);
            mTitle.setVisibility(View.GONE);
        } else {
            mArrowTitleLayout.setVisibility(View.GONE);
            mTitle.setVisibility(View.VISIBLE);
        }
        setTitle(title);
    }

    public void setArrowTitleLayoutBgk(int color) {
        if (mArrowShow) {
            mArrowTitleLayout.setBackgroundColor(color);
        }
    }

    public void setArrowTitleTextColor(int color) {
        if (mArrowShow) {
            mArrowTitle.setTextColor(color);
        }
    }

    public void setArrowTitleImageRes(int resId) {
        if (mArrowShow) {
            mArrowImg.setBackgroundResource(resId);
        }
    }

    public void setBackResourceId(int resId) {
        mBack.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
    }

    public TextView getTitleView() {
        if (mArrowShow) {
            return mArrowTitle;
        } else {
            return mTitle;
        }
    }

    public AppTitleBar setTitle(CharSequence title) {
        if (mArrowShow) {
            mArrowTitle.setText(title);
        } else {
            mTitle.setText(title);
        }
        return this;
    }

    public AppTitleBar setTitleColor(int ColorId) {
        if (mArrowShow) {
            mArrowTitle.setTextColor(ColorId);
        } else {
            mTitle.setTextColor(ColorId);
        }
        return this;
    }

    public AppTitleBar setTitleTextSize(int textSize) {
        if (mArrowShow) {
            mArrowTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        } else {
            mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }
        return this;
    }
    public AppTitleBar setTitleTextBold() {
        if (mArrowShow) {
            mArrowTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗
        } else {
            mTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗
        }
        return this;
    }

    public AppTitleBar setTitle(int resId) {
        return setTitle(mContext.getString(resId));
    }

    public CharSequence getTitle() {
        if (mArrowShow) {
            return mArrowTitle.getText();
        } else {
            return mTitle.getText();
        }
    }


    public AppTitleBar setTitleExtendView(View extendView, FrameLayout.LayoutParams params) {
        mTitleExtend.setVisibility(View.VISIBLE);
        mTitleExtend.removeAllViews();
        if (params != null) {
            mTitleExtend.addView(extendView, params);
        } else {
            mTitleExtend.addView(extendView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.MATCH_PARENT));
        }
        return this;
    }

    public View setLeftBar(View view) {
        removeView(mBack);
        view.setId(R.id.left_title_bar);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        addView(view, params);
        return view;
    }

    private TextView createBarTextView(int resId, OnClickListener l) {
        return createBarTextView(mContext.getString(resId), l);
    }

    public static TextView createBarTextView(String text, Context context, OnClickListener l) {
        TextView tv = new TextView(context);
        tv.setTextColor(context.getResources().getColorStateList(R.color.color_nav_bar_text));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                context.getResources().getDimensionPixelSize(R.dimen.text_size_14));
        tv.setGravity(Gravity.CENTER);
        tv.setText(text);
        tv.setOnClickListener(l);
        return tv;
    }

    private TextView createBarTextView(String text, OnClickListener l) {
        TextView tv = createBarTextView(text, mContext, l);
        int paddingH = mContext.getResources().getDimensionPixelSize(R.dimen.nav_bar_padding);
        tv.setPadding(paddingH, 0, paddingH, 0);

        return tv;
    }

    public TextView setLeftBar(String text, OnClickListener l) {
        removeView(mBack);
        TextView tv = createBarTextView(text, l);
        tv.setId(R.id.left_title_bar);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        addView(tv, params);

        return tv;
    }

    public TextView setLeftBar(int resId, OnClickListener l) {
        return setLeftBar(getResources().getString(resId), l);
    }

    public TextView setRightBar(String text, OnClickListener l) {
        return setRightBar(text,-1,l);
    }
    public void setRightBar(String text){
        View right=findViewById(R.id.right_title_bar);
        if(right!=null && right instanceof TextView){
            ((TextView) right).setText(text);
        }
    }
    public View getRightBar(){
        return findViewById(R.id.right_title_bar);
    }
    public TextView setRightBar(String text, int textColor , OnClickListener l) {
        TextView tv = createBarTextView(text, l);
        tv.setId(R.id.right_title_bar);
        if(textColor!=-1){
            tv.setTextColor(textColor);
        }else {
//            tv.setTextColor(getResources().getColor(R.color.common_dark));
            tv.setTextColor(rightTextColor);
        }

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        params.addRule(ALIGN_PARENT_RIGHT);
        addView(tv, params);
        return tv;
    }
    public TextView setRightBar(int resId, OnClickListener l) {
        return setRightBar(getResources().getString(resId), l);
    }

    public static ImageButton createBarImageView(int resId, Context context, OnClickListener l) {
        ImageButton iv = new ImageButton(context);
        iv.setOnClickListener(l);
        iv.setImageResource(resId);
        iv.setBackgroundResource(android.R.color.transparent);
        iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
        return iv;
    }

    private ImageView createBarImageView(int resId, OnClickListener l) {
        ImageButton iv = createBarImageView(resId, mContext, l);
        int paddingH = mContext.getResources().getDimensionPixelSize(R.dimen.nav_bar_padding);
        iv.setPadding(paddingH, 0, paddingH, 0);

        return iv;
    }

    public ImageView setLeftImageBar(int resId, OnClickListener l) {
        removeView(mBack);
        ImageView iv = createBarImageView(resId, l);
        iv.setId(R.id.left_title_bar);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        addView(iv, params);

        return iv;
    }

    public View setLeftViewBar(View view, OnClickListener l) {
        removeView(mBack);
        View barBtn = view.findViewById(R.id.left_title_bar);
        if (barBtn != null) {
            barBtn.setOnClickListener(l);
        } else {
            view.setId(R.id.left_title_bar);
            view.setOnClickListener(l);
        }
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        addView(view, params);

        return view;
    }

    public ImageView setRightImageBar(int resId, OnClickListener l) {
        ImageView iv = createBarImageView(resId, l);

        iv.setId(R.id.right_title_bar);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        params.bottomMargin=0;
        setTitleExtendView(iv, params);
        return iv;
    }

    public View setRightViewBar(View view, OnClickListener l) {
        View barBtn = view.findViewById(R.id.right_title_bar);
        if (barBtn != null) {
            barBtn.setOnClickListener(l);
        } else {
            view.setId(R.id.right_title_bar);
            view.setOnClickListener(l);
        }
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        setTitleExtendView(view, params);
        return view;
    }

    public void removeRightImageBar() {
        View view = findViewById(R.id.right_title_bar);
        if (view != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }
}
