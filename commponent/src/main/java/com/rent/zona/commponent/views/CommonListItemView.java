package com.rent.zona.commponent.views;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rent.zona.commponent.R;

public class CommonListItemView extends RelativeLayout {

    public static final int LIST_ITEM_VIEW_STYLE_SINGLE_ROW = 1;

    public static final int LIST_ITEM_VIEW_STYLE_DOUBLE_ROW = 2;

    private RelativeLayout mRootView;

    private ImageView mLeftIcon;

    private View mTextContainer;

    private TextView mText;

    private TextView mDetailText;

    private TextView mRightText;

    private ImageView mRightIcon;

    private ImageView mTipDot;

    private SwitchButton mSwitchButton;

    private TextView mLeftText;

    private EditText mEditText;

    public CommonListItemView(Context context) {
        super(context);
        initView();
    }

    public CommonListItemView(Context context, int rowStyle) {
        super(context);
        initView();
        setStyle(rowStyle);
    }

    public CommonListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        setAttrs(context, attrs);
    }

    public CommonListItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        setAttrs(context, attrs);
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_common_item_list, this, true);
        mRootView = (RelativeLayout) findViewById(R.id.root_container);
        mTextContainer = findViewById(R.id.ll_text_container);
        mLeftIcon = (ImageView) findViewById(R.id.view_left_icon);
        mText = (TextView) findViewById(R.id.view_title_text);
        mDetailText = (TextView) findViewById(R.id.view_descript_text);
        mRightText = (TextView) findViewById(R.id.view_right_text);
        mRightIcon = (ImageView) findViewById(R.id.view_right_icon);
        mTipDot = (ImageView) findViewById(R.id.view_tip_dot);
        mSwitchButton = (SwitchButton) findViewById(R.id.switch_button);
        mLeftText = (TextView) findViewById(R.id.view_left_text);
        mEditText = (EditText) findViewById(R.id.view_edit_text);
    }

    public void addRightView(View rightView) {
        LayoutParams params = (LayoutParams) rightView.getLayoutParams();
        addRightView(rightView, params);
    }

    public void addRightView(View rightView, LayoutParams params) {
        if (params != null) {
            params.addRule(ALIGN_PARENT_RIGHT);
            mRootView.addView(rightView);
        }
    }


    public void hideTipDot() {
        mTipDot.setVisibility(View.INVISIBLE);
    }

    public void showTipDot() {
        mTipDot.setVisibility(View.VISIBLE);
    }

    private void setAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CommonListItemView);
        if (a.hasValue(R.styleable.CommonListItemView_item_style)) {
            int d = a.getInt(R.styleable.CommonListItemView_item_style, LIST_ITEM_VIEW_STYLE_SINGLE_ROW);
            setStyle(d);
        }
        if (a.hasValue(R.styleable.CommonListItemView_leftSrc)) {
            Drawable d = a.getDrawable(R.styleable.CommonListItemView_leftSrc);
            mLeftIcon.setImageDrawable(d);
            mLeftIcon.setVisibility(View.VISIBLE);
            LayoutParams params = (LayoutParams) mTextContainer.getLayoutParams();
            params.setMargins(0, 0, 0, 0);
            mTextContainer.setLayoutParams(params);
        }
        if (a.hasValue(R.styleable.CommonListItemView_mainText)) {
            String text = a.getString(R.styleable.CommonListItemView_mainText);
            mText.setText(text);
        }
        if (a.hasValue(R.styleable.CommonListItemView_mainTextColor)) {
            int textColor = a.getColor(R.styleable.CommonListItemView_mainTextColor, Color.RED);
            mText.setTextColor(textColor);
        }
        if (a.hasValue(R.styleable.CommonListItemView_mainTextHintColor)) {
            int textColor = a.getColor(R.styleable.CommonListItemView_mainTextHintColor, Color.GRAY);
            mText.setHintTextColor(textColor);
        }
        if (a.hasValue(R.styleable.CommonListItemView_hint)) {
            String text = a.getString(R.styleable.CommonListItemView_hint);
            mText.setHint(text);
        }
        if (a.hasValue(R.styleable.CommonListItemView_mainTextSize)) {
            mText.setTextSize(TypedValue.COMPLEX_UNIT_PX, a.getDimension(R.styleable.CommonListItemView_mainTextSize, 0));
        }
        if (a.hasValue(R.styleable.CommonListItemView_detailText)) {
            String description = a.getString(R.styleable.CommonListItemView_detailText);
            setDetailText(description);
        }
        if (a.hasValue(R.styleable.CommonListItemView_rightSrc)) {
            Drawable d = a.getDrawable(R.styleable.CommonListItemView_rightSrc);
            mRightIcon.setImageDrawable(d);
            mRightIcon.setVisibility(View.VISIBLE);
        }
        if (a.hasValue(R.styleable.CommonListItemView_rightSrcSize)) {
            ViewGroup.LayoutParams layoutParams=mRightIcon.getLayoutParams();
            int size = a.getDimensionPixelSize(R.styleable.CommonListItemView_rightSrcSize, 0);
            layoutParams.width=layoutParams.height=size;
            mRightIcon.setLayoutParams(layoutParams);
        }

        if (a.hasValue(R.styleable.CommonListItemView_rightVisibility)) {
//            mRightIcon.setVisibility(a.getInt(R.styleable.CommonListItemView_rightVisibility,View.VISIBLE));
            mRightIcon.setVisibility(View.VISIBLE);
        }

        if (a.hasValue(R.styleable.CommonListItemView_rightText)) {
            mRightText.setVisibility(View.VISIBLE);
            mRightText.setText(a.getString(R.styleable.CommonListItemView_rightText));
            if (mRightIcon.getVisibility() != VISIBLE) {
                updateRightTextPos();
            }
            LayoutParams params = (LayoutParams) mTextContainer.getLayoutParams();
            params.addRule(RelativeLayout.LEFT_OF, mRightText.getId());
            mTextContainer.setLayoutParams(params);
        }
        if (a.hasValue(R.styleable.CommonListItemView_rightTextSize)) {
            mRightText.setTextSize(TypedValue.COMPLEX_UNIT_PX, a.getDimension(R.styleable.CommonListItemView_rightTextSize, 0));
        }
        if (a.hasValue(R.styleable.CommonListItemView_rightText_single_line)) {
            if (a.getBoolean(R.styleable.CommonListItemView_rightText_single_line, false)) {
                mRightText.setMaxLines(1);
            } else {
                mRightText.setMaxLines(2);
            }
        }

        if (a.hasValue(R.styleable.CommonListItemView_rightTextColor)) {
            int textColor = a.getColor(R.styleable.CommonListItemView_rightTextColor, Color.RED);
            mRightText.setTextColor(textColor);
        }
        if (a.hasValue(R.styleable.CommonListItemView_detailTextColor)) {
            int textColor = a.getColor(R.styleable.CommonListItemView_detailTextColor, Color.RED);
            mDetailText.setTextColor(textColor);
        }
        if (a.hasValue(R.styleable.CommonListItemView_left_text)) {
            mLeftText.setVisibility(View.VISIBLE);
            mLeftText.setText(a.getString(R.styleable.CommonListItemView_left_text));

        }
        if (a.hasValue(R.styleable.CommonListItemView_left_text_mar_left)) {
            LayoutParams lp = (LayoutParams) mLeftText.getLayoutParams();
            lp.leftMargin = a.getDimensionPixelSize(R.styleable.CommonListItemView_left_text_mar_left, 0);
            mLeftText.setLayoutParams(lp);
        }
        if (a.hasValue(R.styleable.CommonListItemView_edit_text)) {
            mEditText.setVisibility(View.VISIBLE);
            mEditText.setText(a.getString(R.styleable.CommonListItemView_edit_text));
            mText.setVisibility(View.GONE);

        }
        if (a.hasValue(R.styleable.CommonListItemView_edit_text_size)) {
            mEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, a.getDimension(R.styleable.CommonListItemView_edit_text_size, 0));
        }
        if (a.hasValue(R.styleable.CommonListItemView_edit_text_hint_size)) {
            mEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, a.getDimension(R.styleable.CommonListItemView_edit_text_size, 0));
        }
        if (a.hasValue(R.styleable.CommonListItemView_edit_text_length)) {
            mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(a.getInt(R.styleable.CommonListItemView_edit_text_length, 10))});
        }
        if (a.hasValue(R.styleable.CommonListItemView_edit_text_hint)) {
            mEditText.setVisibility(View.VISIBLE);
            mEditText.setHint(a.getString(R.styleable.CommonListItemView_edit_text_hint));
            mText.setVisibility(View.GONE);

        }
        if (a.hasValue(R.styleable.CommonListItemView_left_text_color)) {
            int textColor = a.getColor(R.styleable.CommonListItemView_left_text_color, Color.RED);
            mLeftText.setTextColor(textColor);
        }
        if (a.hasValue(R.styleable.CommonListItemView_left_text_size)) {
            mLeftText.setTextSize(TypedValue.COMPLEX_UNIT_PX, a.getDimension(R.styleable.CommonListItemView_left_text_size, 0));
        }
        if (a.hasValue(R.styleable.CommonListItemView_left_text_wide)) {
            int wide = a.getDimensionPixelSize(R.styleable.CommonListItemView_left_text_wide, 120);
            ViewGroup.LayoutParams layoutParams = mLeftText.getLayoutParams();
            layoutParams.width = wide;
            mLeftText.setLayoutParams(layoutParams);
        }
        if (a.hasValue(R.styleable.CommonListItemView_left_src_size)) {
            int size = a.getDimensionPixelSize(R.styleable.CommonListItemView_left_src_size, 120);
            ViewGroup.LayoutParams layoutParams = mLeftIcon.getLayoutParams();
            layoutParams.width = size;
            layoutParams.height = size;
            mLeftIcon.setLayoutParams(layoutParams);
        }
        if (a.hasValue(R.styleable.CommonListItemView_left_src_mar_right)) {
            int size = a.getDimensionPixelSize(R.styleable.CommonListItemView_left_src_mar_right, 0);
            LayoutParams layoutParams = (LayoutParams) mLeftIcon.getLayoutParams();
            layoutParams.rightMargin = size;
            mLeftIcon.setLayoutParams(layoutParams);
        }
        if (a.hasValue(R.styleable.CommonListItemView_left_src_mar_left)) {
            int size = a.getDimensionPixelSize(R.styleable.CommonListItemView_left_src_mar_left, 0);
            LayoutParams layoutParams = (LayoutParams) mLeftIcon.getLayoutParams();
            layoutParams.leftMargin = size;
            mLeftIcon.setLayoutParams(layoutParams);
        }
        if (a.hasValue(R.styleable.CommonListItemView_detailTextLeftSrc)) {
            Drawable d = a.getDrawable(R.styleable.CommonListItemView_detailTextLeftSrc);
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            mDetailText.setCompoundDrawables(d, null, null, null);
            mDetailText.setCompoundDrawablePadding(10);

        }
        if(a.hasValue(R.styleable.CommonListItemView_root_bg)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mRootView.setBackground(a.getDrawable(R.styleable.CommonListItemView_root_bg));
            }else{
                mRootView.setBackgroundDrawable(a.getDrawable(R.styleable.CommonListItemView_root_bg));
            }
        }
        if (a.hasValue(R.styleable.CommonListItemView_switchbutton_visibility)) {
            int visibility = a.getInt(R.styleable.CommonListItemView_switchbutton_visibility, 0);
            switch (visibility) {
                case 0:
                    mSwitchButton.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    mSwitchButton.setVisibility(View.INVISIBLE);
                    break;
                case 2:
                    mSwitchButton.setVisibility(View.GONE);
                    break;
            }
        }
        if (a.hasValue(R.styleable.CommonListItemView_switchbutton_checked)) {
            mSwitchButton.setChecked(a.getBoolean(R.styleable.CommonListItemView_switchbutton_checked, false));
        }
        a.recycle();
    }

    private void updateRightTextPos() {
        int rightIconVisibility = mRightIcon.getVisibility();
        LayoutParams params = (LayoutParams) mRightText.getLayoutParams();
        if (rightIconVisibility != VISIBLE) {
            params.rightMargin = getResources().getDimensionPixelSize(R.dimen.common_horizon_margin);
            params.addRule(ALIGN_PARENT_RIGHT);
            mRightText.setLayoutParams(params);
        } else {
            params.rightMargin = getResources().getDimensionPixelSize(R.dimen.item_list_content_padding_s);
            params.addRule(ALIGN_PARENT_RIGHT, 0);
            mRightText.setLayoutParams(params);
        }
    }

    private void setViewVisibility(boolean visible, View view) {
        if (visible) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    /**
     * @param itemStyle One of {@link #LIST_ITEM_VIEW_STYLE_SINGLE_ROW}, {@link
     *                  #LIST_ITEM_VIEW_STYLE_DOUBLE_ROW}
     * @Description 设置列表的style, 单行列表或双行列表
     * @attr ref android.R.styleable.CommonListItemView#item_style
     */
    public void setStyle(int itemStyle) {
        int viewHeight = 0;
        int leftSize = 0;
        switch (itemStyle) {
            case LIST_ITEM_VIEW_STYLE_SINGLE_ROW:
                viewHeight = getResources().getDimensionPixelOffset(R.dimen.item_list_row_height);
                leftSize = getResources().getDimensionPixelSize(R.dimen.ic_1);
                mDetailText.setVisibility(View.GONE);
                break;
            case LIST_ITEM_VIEW_STYLE_DOUBLE_ROW:
                viewHeight = getResources().getDimensionPixelOffset(R.dimen.item_list_double_row_height);
                leftSize = getResources().getDimensionPixelSize(R.dimen.at_1);
                mDetailText.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
        setRootHeight(viewHeight);
        setLeftIconSize(leftSize);
    }


    private void setRootHeight(int height) {
        LayoutParams params = (LayoutParams) mRootView.getLayoutParams();
        params.height = height;
        mRootView.setLayoutParams(params);
    }

    public void setRootMargin(int left, int top, int right, int bottom) {
        LayoutParams params = (LayoutParams) mRootView.getLayoutParams();
        params.setMargins(left, top, right, bottom);
        mRootView.setLayoutParams(params);
    }

    public void setLeftIconSize(int size) {
        LayoutParams leftParams = (LayoutParams) mLeftIcon.getLayoutParams();
        leftParams.width = size;
        leftParams.height = size;
        mLeftIcon.setLayoutParams(leftParams);
    }

    public void setLeftIconResource(int resId) {
        mLeftIcon.setImageResource(resId);
        setLeftIconVisibility(View.VISIBLE);
    }

    public void setLeftIconVisibility(int visibility) {
        mLeftIcon.setVisibility(visibility);
        LayoutParams params = (LayoutParams) mTextContainer.getLayoutParams();
        if (visibility == View.VISIBLE) {
            params.setMargins(0, 0, 0, 0);
        } else {
            params.setMargins(getResources().getDimensionPixelSize(R.dimen.item_list_padding), 0, 0, 0);
        }
        mTextContainer.setLayoutParams(params);
    }

    public ImageView getLeftImageView() {
        return mLeftIcon;
    }

    public void setText(CharSequence text) {
        mText.setText(text);
    }

    public void setInputText(CharSequence text) {
        mEditText.setText(text);
        if (!TextUtils.isEmpty(text))
            mEditText.setSelection(text.length());
    }
    public EditText getEditText(){
        return mEditText;
    }

    public CharSequence getInputText() {
        return mEditText.getText();
    }

    public void setText(int resId) {
        mText.setText(resId);
    }

    public CharSequence getText() {
        return mText.getText();
    }

    public void setDetailText(CharSequence text) {
        mDetailText.setText(text);
        setDetailTextVisibility(View.VISIBLE);
    }

    public void setDetailText(int resId) {
        mDetailText.setText(resId);
        setDetailTextVisibility(View.VISIBLE);
    }

    public void setDetailTextVisibility(int visibility) {
        mDetailText.setVisibility(visibility);
    }

    public void setRightIconResource(int resId) {
        mRightIcon.setImageResource(resId);
        setRightIconVisibility(View.VISIBLE);
    }

    public void setRightIconResource(Bitmap bitmap) {
        mRightIcon.setImageBitmap(bitmap);
        setRightIconVisibility(View.VISIBLE);
    }

    public ImageView getRightImageView() {
        return mRightIcon;
    }

    public void setRightIconVisibility(int visibility) {
        mRightIcon.setVisibility(visibility);
        if (visibility != VISIBLE) {
            updateRightTextPos();
        }
    }

    public TextView getDetailText() {
        return mDetailText;
    }

    public void setRightText(String rightText) {
        setRightText(rightText, "");
    }

    public void setRightText(String rightText, String textColor) {
        if (!TextUtils.isEmpty(textColor)) {
            mRightText.setTextColor(Color.parseColor(textColor));
        }

        mRightText.setVisibility(View.VISIBLE);
        mRightText.setText(rightText);
        if (mRightIcon.getVisibility() != VISIBLE) {
            updateRightTextPos();
        }
        LayoutParams params = (LayoutParams) mTextContainer.getLayoutParams();
        params.addRule(RelativeLayout.LEFT_OF, mRightText.getId());
        mTextContainer.setLayoutParams(params);

    }


    public View getTextContainer() {
        return mTextContainer;
    }

    public TextView getRightTextView() {
        return mRightText;
    }

    public CharSequence getRightText() {
        return mRightText.getText();
    }

    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener) {
        mSwitchButton.setOnCheckedChangeListener(listener);
        mSwitchButton.setVisibility(View.VISIBLE);
    }

    public SwitchButton getSwitch() {
        return mSwitchButton;
    }

    public void setSwitchButtonChecked(boolean checked){
        mSwitchButton.setChecked(checked);
    }
    public boolean isSwitchButtonChecked(){
        return mSwitchButton.isChecked();
    }
    @Override
    public void setBackgroundResource(int resid) {
        super.setBackgroundResource(resid);
        mRootView.setBackgroundResource(resid);
    }

    public void setTextColor(int color) {
        mText.setTextColor(color);
    }

    public void setEditTextEditable(boolean editable) {
        mEditText.setEnabled(editable);
    }

    public boolean isEditTextEnable() {
        return mEditText.isEnabled();
    }

    public void setLeftText(CharSequence leftText){
        mLeftText.setVisibility(View.VISIBLE);
        mLeftText.setText(leftText);
    }
    public void setOnEditorActionListener(TextView.OnEditorActionListener editorActionListener) {
        mEditText.setOnEditorActionListener(editorActionListener);
    }
    public void setEditTextshowSoftInput(Context context){
//        mEditText.setFocusable(true);
//        mEditText.setFocusableInTouchMode(true);
//        mEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText,0);
    }
    public void setEditTextHideInput(Activity context){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void setEditMaxLength(int length){
        mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});

    }
}
