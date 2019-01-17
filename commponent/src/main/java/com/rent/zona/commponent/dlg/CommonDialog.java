package com.rent.zona.commponent.dlg;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.rent.zona.baselib.log.LibLogger;
import com.rent.zona.commponent.R;
import com.rent.zona.commponent.utils.DensityUtils;
import com.rent.zona.commponent.views.CommonButton;

import java.util.LinkedList;


public class CommonDialog extends Dialog {
    private static final String TAG = "CommonDialog";

    public static final int DEFAULT_BTN_LEFT = 1;
    public static final int DEFAULT_BTN_RIGHT = 2;

    private static final float WINDOW_WIDTH_RATIO = 0.75f;
    private float windowWidthRatic=WINDOW_WIDTH_RATIO;
    private Context mContext;
    private ScrollView mContentHolder;
    private LinearLayout mLinearLayout;
    private View mLayoutButtons;
    private TextView mTitleView;
    private TextView mMsgView;
    private TextView mMsgHintView;
    private View mDlgLayout;

    private View mBtnPanelDivider;
    private CommonButton mOkButton;
    private CommonButton mCancelButton;
//    private View mCancelBtnDivider;

    private Button mDefaultBtn;

    private LinearLayout mVerticalBtnPanel;
    private EditText mContentEdit;

    private ImageView mCloseIcon;

    private ImageView mMsgIv;

    private boolean mEnableCancelWhenAppBackground;

    private static LinkedList<CommonDialog> sCommonDialog = new LinkedList<CommonDialog>();
    public CommonDialog(Context context) {
        this(context,-1);
    }

    /**
     *
     * @param context
     * @param windowWidthRatic 0-1
     */
    public CommonDialog(Context context, float windowWidthRatic) {
        super(context, R.style.MyWidget_CustomDialog);
        setContentView(R.layout.common_dialog);
        mContext = context;

        mDlgLayout = findViewById(R.id.dlg_view);
        mContentHolder = (ScrollView) findViewById(R.id.content_holder);
        mLinearLayout = (LinearLayout) findViewById(R.id.content_holder2);
        mLayoutButtons = findViewById(R.id.btn_panel);
        mTitleView = (TextView) findViewById(R.id.title);
        mMsgView = (TextView) findViewById(R.id.message);
        mMsgHintView = (TextView) findViewById(R.id.message_hint);
        mContentEdit = (EditText) findViewById(R.id.edittext);

        mOkButton = (CommonButton) findViewById(R.id.btn_ok);
        mCancelButton = (CommonButton) findViewById(R.id.btn_cancel);
        mCancelButton.setOnClickListener(new ExternalListener(null));
        mBtnPanelDivider = findViewById(R.id.btn_panel_divider);
        mMsgIv= (ImageView) findViewById(R.id.msg_iv);
//        mCancelBtnDivider = findViewById(R.id.cancel_btn_divider);

        mVerticalBtnPanel = (LinearLayout) findViewById(R.id.btn_panel_vertical);
        mCloseIcon = (ImageView) findViewById(R.id.close);

        setCancelable(true);
        setCanceledOnTouchOutside(false);
        //getWindow().setWindowAnimations(R.style.CommonDialogWindowAnim);
        if(windowWidthRatic>0){
            this.windowWidthRatic=windowWidthRatic;
        }
        int width = calculateDialogWidth(context);
        getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    public  int calculateDialogWidth(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        return Math.min((int) (metrics.widthPixels * windowWidthRatic + 0.5f),
                context.getResources().getDimensionPixelSize(R.dimen.common_dialog_max_width));
    }

    public static CommonDialog getCurrentDialog() {
        return sCommonDialog.peekLast();
    }

    @Override
    protected void onStart() {
        super.onStart();

        sCommonDialog.add(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        sCommonDialog.remove(this);
    }

    public void setCloseIconVisibility(int visibility) {
        mCloseIcon.setVisibility(visibility);
    }

    public void setCloseIconClickListener(View.OnClickListener listener) {
        mCloseIcon.setOnClickListener(listener);
    }

    public void setCancelWhenAppBackground(boolean enable) {
        mEnableCancelWhenAppBackground = enable;
    }

    public boolean isEnabledCancelWhenAppBackground() {
        return mEnableCancelWhenAppBackground;
    }

    @Override
    public void setTitle(int resId) {
        mTitleView.setText(resId);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitleView.setText(title);
    }

    public View getDlgView() {
        return mDlgLayout;
    }

    public void setNoTitleBar() {
        findViewById(R.id.titlebar_panel).setVisibility(View.GONE);
        findViewById(R.id.titlebar_panel_divider).setVisibility(View.GONE);
    }

    public void setImage(int resId){
        mMsgIv.setVisibility(View.VISIBLE);
        mMsgIv.setImageResource(resId);
        mContentHolder.setVisibility(View.VISIBLE);
    }
    public void setContanerView(View view) {
        ViewGroup contentHolder = mContentHolder;
        contentHolder.removeAllViews();
        contentHolder.addView(view);
        contentHolder.setVisibility(View.VISIBLE);
    }

    public void setContentPadding(int width) {
        if (View.VISIBLE == mContentHolder.getVisibility()) {
            mContentHolder.setPadding(width, mContentHolder.getPaddingTop(), width,
                    mContentHolder.getPaddingBottom());
        }
    }

    public void setContentPadding(int left, int top, int right, int bottom) {
        if (View.VISIBLE == mContentHolder.getVisibility()) {
            mContentHolder.setPadding(left, top, right, bottom);
        }
    }

    public void addContentView(View view, LinearLayout.LayoutParams params) {
        ViewGroup contentHolder = mLinearLayout;
        if (params != null) {
            contentHolder.addView(view, params);
        } else {
            contentHolder.addView(view);
        }
    }

    public void setMessage(int resId) {
        setMessage(mContext.getString(resId));
    }

    public void setMessage(CharSequence msg) {
        mMsgView.setText(msg);
        mMsgView.setVisibility(View.VISIBLE);
        mContentHolder.setVisibility(View.VISIBLE);

//        findViewById(R.id.titlebar_panel_divider).setVisibility(View.VISIBLE);
        //mTitleView.setPadding(0, 0, 0,
        //        getContext().getResources().getDimensionPixelSize(R.dimen.common_dialog_title_marginBottom));
    }
    public void setMsgHint(CharSequence msgHint){
        mMsgHintView.setText(msgHint);
        mMsgHintView.setVisibility(View.VISIBLE);
    }
    public void setMsgTextColor(int resColor){
        mMsgView.setTextColor(ContextCompat.getColor(getContext(),resColor));
    }
    public void setMsgGravity(int gravity){
        mMsgView.setGravity(gravity);
    }
    public void setMsgHintGravity(int gravity){
        mMsgHintView.setGravity(gravity);
    }
    public void setMessageAndLineSpacingExtra(CharSequence msg, float add, float mult) {
        setMessage(msg);
        mMsgView.setLineSpacing(add, mult);
    }

    public TextView getMessageView() {
        mContentHolder.setVisibility(View.VISIBLE);
        mMsgView.setVisibility(View.VISIBLE);
        return mMsgView;
    }

    public void setMessagePannelVisible(boolean visible) {
        mContentHolder.setVisibility(visible ? View.VISIBLE : View.GONE);
    }
    public void setOkBtn(int resId, View.OnClickListener clickListener) {
        setOkBtn(resId,clickListener,true);
    }
    public void setOkBtn(String okText, View.OnClickListener clickListener) {
        setOkBtn(okText,clickListener,true);
    }
    public void setOkBtn(String okText, View.OnClickListener clickListener, boolean isUseExternalListener) {
        showButtonPanel();
        mOkButton.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(okText)) {
            mOkButton.setText(okText);
        }

        if (clickListener != null) {
            if (isUseExternalListener){
                mOkButton.setOnClickListener(new ExternalListener(clickListener));
            }else{
                mOkButton.setOnClickListener(clickListener);
            }
        } else {
            mOkButton.setOnClickListener(new CloseListener());
        }
    }
    public void setOkBtn(int resId, View.OnClickListener clickListener, boolean isUseExternalListener) {
        setOkBtn(mOkButton.getContext().getString(resId),clickListener,isUseExternalListener);
//        showButtonPanel();
//        mOkButton.setVisibility(View.VISIBLE);
//        if (resId > 0) {
//            mOkButton.setText(resId);
//        }
//
//        if (clickListener != null) {
//            if (isUseExternalListener){
//                mOkButton.setOnClickListener(new ExternalListener(clickListener));
//            }else{
//                mOkButton.setOnClickListener(clickListener);
//            }
//        } else {
//            mOkButton.setOnClickListener(new CloseListener());
//        }
    }

    public void setOkBtnColor(int textColorId) {
        if (textColorId > 0) {
            mOkButton.setTextColor(mContext.getResources().getColor(textColorId));
        }
    }

    public void setOkBtnVisible(boolean visible) {
        mOkButton.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setCancelBtnVisible(boolean visible) {
        mCancelButton.setVisibility(visible ? View.VISIBLE : View.GONE);
        if (visible) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mOkButton.getLayoutParams();
            lp.leftMargin = DensityUtils.dip2px(getContext(), 20);
//            lp.leftMargin = DensityUtils.dip2px(getContext(), 50);
//            lp.rightMargin = DensityUtils.dip2px(getContext(),50);
            mOkButton.setLayoutParams(lp);
        } else {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mOkButton.getLayoutParams();
//            lp.leftMargin = 0;
            lp.leftMargin = DensityUtils.dip2px(getContext(), 50);
            lp.rightMargin = DensityUtils.dip2px(getContext(),50);
            mOkButton.setLayoutParams(lp);
        }
    }


    /**
     * This method should have click listener and dismiss dialog by yourself
     */
    public void setOkBtnNotDismiss(int resId, View.OnClickListener clickListener) {
        showButtonPanel();
        mOkButton.setVisibility(View.VISIBLE);
        if (resId > 0) {
            mOkButton.setText(resId);
        }

        mOkButton.setOnClickListener(clickListener);
    }

    public void setBtnEnable(int whichBtn, boolean enable) {
        Button button = null;

        if (whichBtn == DEFAULT_BTN_LEFT) {
            button = mOkButton;
        } else if (whichBtn == DEFAULT_BTN_RIGHT) {
            button = mCancelButton;
        } else {
            button = null;
            LibLogger.w(TAG, "Set Bad bt: " + whichBtn);
        }

        if (button != null) {
            button.setEnabled(enable);
        }
    }
    public void setCancelBtn(int resId, View.OnClickListener clickListener) {
        setCancelBtn(mOkButton.getContext().getString(resId),clickListener);
    }
    public void setCancelBtn(String cancelTest, View.OnClickListener clickListener) {
        showButtonPanel();
        mCancelButton.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(cancelTest)) {
            mCancelButton.setText(cancelTest);
        }

        if (clickListener != null) {
            mCancelButton.setOnClickListener(new ExternalListener(clickListener));
        } else {
            mCancelButton.setOnClickListener(new CloseListener());
        }
    }

//    public void setCancelBtn(int resId, View.OnClickListener clickListener) {
//        showButtonPanel();
//        mCancelButton.setVisibility(View.VISIBLE);
//        if (resId > 0) {
//            mCancelButton.setText(resId);
//        }
//
//        if (clickListener != null) {
//            mCancelButton.setOnClickListener(new ExternalListener(clickListener));
//        } else {
//            mCancelButton.setOnClickListener(new CloseListener());
//        }
//    }

    /**
     * Set the default button, which will be focused.
     *
     * @param whichBtn Must be one of {@link #DEFAULT_BTN_LEFT} or {@link #} or
     *                 {@link #DEFAULT_BTN_RIGHT}
     */
    public void setDefaultButton(int whichBtn) {
        if (whichBtn == DEFAULT_BTN_LEFT) {
            mDefaultBtn = mOkButton;
        }
        if (whichBtn == DEFAULT_BTN_RIGHT) {
            mDefaultBtn = mCancelButton;
        } else {
            mDefaultBtn = null;
            LibLogger.w(TAG, "Bad default bt: " + whichBtn);
        }

        if (mDefaultBtn != null) {
            mDefaultBtn.setSelected(true);
        }
    }

    public Button addVerticalButton(int resId, View.OnClickListener clickListener) {
        showVerticalButtonPanel();
        Button btn = addVerticalButton(resId, R.drawable.sel_common_btn_bg, clickListener);
        addHorizontalDividerLine();
        return btn;
    }

    public Button addVerticalButton(String text, View.OnClickListener clickListener) {
        showVerticalButtonPanel();
        Button btn = addVerticalButton(text, R.drawable.sel_common_btn_bg, clickListener);
        addHorizontalDividerLine();
        return btn;
    }

    public void addVerticalBottomButton(int resId, View.OnClickListener clickListener) {
        showVerticalButtonPanel();
        addVerticalButton(resId, R.drawable.dialog_bottom_btn_bg, clickListener);
    }

    private Button addVerticalButton(int resId, int bgResId, View.OnClickListener clickListener) {
        return addVerticalButton(getContext().getString(resId), bgResId, clickListener);
    }

    private Button addVerticalButton(String text, int bgResId, View.OnClickListener clickListener) {
        Button btn = (Button) View.inflate(mContext, R.layout.common_dialog_btn, null);
        btn.setText(text);
        btn.setBackgroundResource(bgResId);
        if (clickListener != null) {
            btn.setOnClickListener(new ExternalListener(clickListener));
        } else {
            btn.setOnClickListener(new CloseListener());
        }
        mVerticalBtnPanel.addView(btn, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                mContext.getResources().getDimensionPixelSize(R.dimen.common_dialog_btn_height)));

        return btn;
    }

    private void addHorizontalDividerLine() {
        View line = View.inflate(mContext, R.layout.common_divider_horizontal, null);
        mVerticalBtnPanel.addView(line, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                mContext.getResources().getDimensionPixelSize(R.dimen.common_divider_line_size)));
    }

    private void showButtonPanel() {
        mLayoutButtons.setVisibility(View.VISIBLE);
    }

    private void showVerticalButtonPanel() {
        mVerticalBtnPanel.setVisibility(View.VISIBLE);
    }

    public void setDefaultEditText(String defaultText) {
        setDefaultEditText(defaultText,-1);
    }
    public void setHintEditText(String hintText) {
        setHintEditText(hintText,-1);
    }
    public void setHintEditText(String hintText, int maxLength) {
        mContentEdit.setVisibility(View.VISIBLE);
        mContentEdit.setHint(hintText);
        if(maxLength>0) {
            mContentEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        }
        mContentHolder.setVisibility(View.VISIBLE);
    }
    public void setDefaultEditText(String defaultText, int maxLength) {
        mContentEdit.setVisibility(View.VISIBLE);
        mContentEdit.setText(defaultText);
        if(maxLength>0) {
            mContentEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        }
        mContentHolder.setVisibility(View.VISIBLE);
    }
    public String getContentEditText() {
        return mContentEdit.getText().toString();
    }

    public EditText getContentEditView() {
        return mContentEdit;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mDefaultBtn != null) {
            mDefaultBtn.setSelected(false);
            mDefaultBtn = null;
        }
        return super.dispatchTouchEvent(ev);
    }

    private class CloseListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            cancel();
        }
    }

    private class ExternalListener implements View.OnClickListener {
        private View.OnClickListener mListener;

        public ExternalListener(View.OnClickListener listener) {
            mListener = listener;
        }

        @Override
        public void onClick(View v) {
            CommonDialog.this.dismiss();
            if(mListener!=null)
                mListener.onClick(v);
        }
    }

    public void setFocusBtn(boolean isOkbtn){
        if(isOkbtn){
            mOkButton.setEnableColor(ContextCompat.getColor(mContext,R.color.theme_color));
            mOkButton.setStrokeColor(ContextCompat.getColor(mContext,R.color.theme_color));
            mOkButton.setTextColor(Color.WHITE);
            mOkButton.setPressColor(-1);
            mCancelButton.setEnableColor(0xffEFEFEF);
            mCancelButton.setStrokeColor(0xffCCCCCC);
            mCancelButton.setTextColor(ContextCompat.getColor(mContext,R.color.font_accent));
            mCancelButton.setPressColor(-1);
        }else{
            mCancelButton.setEnableColor(ContextCompat.getColor(mContext,R.color.theme_color));
            mCancelButton.setStrokeColor(ContextCompat.getColor(mContext,R.color.theme_color));
            mCancelButton.setTextColor(Color.WHITE);
            mCancelButton.setPressColor(-1);
            mOkButton.setEnableColor(0xffEFEFEF);
            mOkButton.setStrokeColor(0xffCCCCCC);
            mOkButton.setTextColor(ContextCompat.getColor(mContext,R.color.font_accent));
            mOkButton.setPressColor(-1);
        }
    }
}
