package com.rent.zona.commponent.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;

import com.rent.zona.commponent.R;


/**
 * Created by Administrator on 2015/4/27.
 */

/**
 * To change clear icon, set
 * <p/>
 * <pre>
 * android:drawableRight="@drawable/custom_icon"
 * </pre>
 */
public class ClearableEditText extends AppCompatEditText implements OnTouchListener,
        OnFocusChangeListener, TextWatcher {


    public interface Listener {
        void didClearText();

        void didEmptyPressed();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private Drawable xD;
    private Listener listener;
    private Drawable mPressedDrawable;
    // drawable showed when text is empty
    private Drawable mEmptyDrawable;
    private Drawable mPressEmptyDrawable;

    private boolean mIsClearTapped;

    private OnTouchListener l;
    private OnFocusChangeListener f;


    private boolean showPressedDrawable = true;

    public void setShowPressedDrawable(boolean showPressedDrawable) {
        this.showPressedDrawable = showPressedDrawable;
        super.setOnFocusChangeListener(null);
    }

    public ClearableEditText(Context context) {
        super(context);
        init(context);
    }

    public ClearableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ClearableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        this.l = l;
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener f) {
        this.f = f;
    }

    public void setEmptyDrawable(Drawable drawable, Drawable pressDrawable) {
        mEmptyDrawable = drawable;
        if (pressDrawable == null) {
            mPressEmptyDrawable = mEmptyDrawable;
        } else {
            mPressEmptyDrawable = pressDrawable;
        }
    }

    public void showEmptyDrawable() {
        setCompoundDrawables(null, null, mEmptyDrawable, null);
    }

    public void showNullDrawable() {
        setCompoundDrawables(null, null, null, null);
    }

    public void setRightDrawableAlpha(int alpha) {
        Drawable[] drawables = getCompoundDrawables();
        Drawable rightDrawable = drawables[2];
        if (rightDrawable != null) {
            rightDrawable.setAlpha(alpha);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (getCompoundDrawables()[2] != null) {
            if (mIsClearTapped) {
                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    mIsClearTapped = false;
                    boolean isClearIconVisible = isClearIconVisible();
                    setCompoundDrawables(getCompoundDrawables()[0],
                            getCompoundDrawables()[1],
                            isClearIconVisible ? xD : mEmptyDrawable,
                            getCompoundDrawables()[3]);

                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        boolean tappedX = false;
                        if (isClearIconVisible) {
                            tappedX = event.getX() > (getWidth() - getPaddingRight() - xD
                                    .getIntrinsicWidth());
                        } else if (mEmptyDrawable != null) {
                            tappedX = event.getX() > (getWidth() - getPaddingRight() - mEmptyDrawable
                                    .getIntrinsicWidth());
                        }
                        if (tappedX) {
                            if (isClearIconVisible) {
                                setText("");
                                if (listener != null) {
                                    listener.didClearText();
                                }
                            } else {
                                if (listener != null) {
                                    listener.didEmptyPressed();
                                }
                            }
                        }

                        return true;
                    }
                }
            } else {
                boolean tappedX = false;
                boolean isClearIconVisible = isClearIconVisible();
                if (isClearIconVisible) {
                    tappedX = event.getX() > (getWidth() - getPaddingRight() - xD
                            .getIntrinsicWidth());
                } else if (mEmptyDrawable != null) {
                    tappedX = event.getX() > (getWidth() - getPaddingRight() - mEmptyDrawable
                            .getIntrinsicWidth());
                }
                if (tappedX) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        mIsClearTapped = true;
                        if (showPressedDrawable) {
                            setCompoundDrawables(getCompoundDrawables()[0],
                                    getCompoundDrawables()[1],
                                    isClearIconVisible ? mPressedDrawable : mPressEmptyDrawable,
                                    getCompoundDrawables()[3]);
                        }
                        return true;
                    }
                }
            }
        }
        if (l != null) {
            return l.onTouch(v, event);
        }
        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            setClearIconVisible(!TextUtils.isEmpty(getText()));
        } else {
            setClearIconVisible(false);
        }
        if (f != null) {
            f.onFocusChange(v, hasFocus);
        }
    }

    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        if (isFocused()) {
            setClearIconVisible(!TextUtils.isEmpty(text));
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private void init(Context context) {
        xD = getCompoundDrawables()[2];
        if (xD == null) {
            xD = getResources()
                    .getDrawable(R.drawable.edit_clean_btn_normal);
        }
        xD.setBounds(0, 0, xD.getIntrinsicWidth(), xD.getIntrinsicHeight());

        mPressedDrawable = context.getResources().getDrawable(R.drawable.edit_clean_btn_pressed);
        mPressedDrawable.setBounds(0, 0, mPressedDrawable.getIntrinsicWidth(), mPressedDrawable.getIntrinsicHeight());

        setClearIconVisible(false);
        super.setOnTouchListener(this);
        super.setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }

    private boolean isClearIconVisible() {
        Drawable drawable = getCompoundDrawables()[2];
        return (drawable != null && drawable != mEmptyDrawable
                && drawable != mPressEmptyDrawable);
    }

    protected void setClearIconVisible(boolean visible) {
        boolean wasVisible = isClearIconVisible();
        if (visible != wasVisible) {
            Drawable x = visible ? xD : mEmptyDrawable;
            setCompoundDrawables(getCompoundDrawables()[0],
                    getCompoundDrawables()[1], x, getCompoundDrawables()[3]);
        }
    }
}