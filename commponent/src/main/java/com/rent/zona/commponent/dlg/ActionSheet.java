package com.rent.zona.commponent.dlg;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.rent.zona.commponent.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActionSheet extends Dialog {

    public static final int CANCEL_BUTTON_INDEX = -1;
    public static final int DESTRUCTIVE_BUTTON_INDEX = -2;

    private static final int ACTION_ITEM_NORMAL = 0;
    private static final int ACTION_ITEM_DESTRUCTIVE = 1;
    private static final int ACTION_ITEM_CANCEL = 2;

    public Object userInfo;

    private String mTitle;
    private final String mCancelTitle;
    private final String mDestructiveButtonTitle;
    private final List<String> mOtherBtnTitles;

    private ListView mContentList;
    private ActionAdapter mAdapter;
    private TextView tvTitle;
    private TextView tvCancel;

    private final OnClickListener mClickListener;
    private final int mTag;

    public ActionSheet(Context context, String title,
                       List<String> otherBtns, OnClickListener l) {
        this(context, R.style.MyWidget_ActionSheet, title, context.getString(R.string.cancel), null,
                otherBtns, l, -9999);
    }

    public ActionSheet(Context context, String title,
                       String[] otherBtns, OnClickListener l) {
        this(context, title, context.getString(R.string.cancel), null, otherBtns, l);
    }

    public ActionSheet(Context context, String title,
                       List<String> otherBtns, OnClickListener l, int tag) {
        this(context, R.style.MyWidget_ActionSheet, title, context.getString(R.string.cancel), null,
                otherBtns, l, tag);
    }

    public ActionSheet(Context context, String title,
                       String cancelTitle, String destructiveButtonTitle,
                       String[] otherBtns, OnClickListener l) {
        this(context, R.style.MyWidget_ActionSheet, title, cancelTitle, destructiveButtonTitle,
                Arrays.asList(otherBtns), l, -9999);
    }

    public ActionSheet(Context context, int theme, String title,
                       String cancelTitle, String destructiveButtonTitle,
                       List<String> otherBtns, OnClickListener l, int tag) {
        super(context, theme);
        setContentView(R.layout.dialog_action_sheet);
        mTitle = title;
        mCancelTitle = cancelTitle;
        mDestructiveButtonTitle = destructiveButtonTitle;
        mOtherBtnTitles = otherBtns;
        mClickListener = l;
        mTag = tag;

        Window win = getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0); // make dialog full screen
        WindowManager.LayoutParams wmlp = getWindow().getAttributes();
        wmlp.gravity = Gravity.BOTTOM;
        wmlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wmlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(wmlp);
        setCanceledOnTouchOutside(true);
    }

    public int getTag() {
        return mTag;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public ActionSheet animation(int animationId) {
        getWindow().setWindowAnimations(animationId);
        return this;
    }

    @Override
    public void show() {
        super.show();
        initTitle();
    }

    private void initTitle() {
        if (!TextUtils.isEmpty(mTitle)) {
            if (tvTitle == null) {
                View titleView = this.getLayoutInflater().inflate(
                        R.layout.action_sheet_title, null);
                tvTitle = (TextView) titleView.findViewById(R.id.tv_title);
                mContentList.addHeaderView(titleView, null, false);
            }
            tvTitle.setText(mTitle);
        }
    }

    private void initCancel() {
        if (!TextUtils.isEmpty(mCancelTitle)) {
            if (tvCancel == null) {
                tvCancel = (TextView) findViewById(R.id.actoin_sheet_cancel);
            }
            tvCancel.setOnClickListener(v -> {
                if (mClickListener != null) {
                    mClickListener.onClick(ActionSheet.this,
                            CANCEL_BUTTON_INDEX);
                }
                dismiss();
            });
            tvCancel.setText(mCancelTitle);
            tvCancel.setVisibility(View.VISIBLE);
        } else {
            tvCancel.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContentList = (ListView) this.findViewById(android.R.id.list);
        initTitle();
        initCancel();

        ArrayList<ActionItem> items = new ArrayList<ActionItem>(5);
        if (mDestructiveButtonTitle != null) {
            items.add(new ActionItem(ACTION_ITEM_DESTRUCTIVE,
                    mDestructiveButtonTitle));
        }

        if (mOtherBtnTitles != null) {
            for (String text : mOtherBtnTitles) {
                items.add(new ActionItem(ACTION_ITEM_NORMAL, text));
            }
        }
        mAdapter = new ActionAdapter(getContext(), items);
        mContentList.setAdapter(mAdapter);
    }

    private int getActionItemBtnIndex(ActionItem item, int pos) {
        int index = 0;
        if (item.mType == ACTION_ITEM_CANCEL) {
            index = CANCEL_BUTTON_INDEX;
        } else if (item.mType == ACTION_ITEM_DESTRUCTIVE) {
            index = DESTRUCTIVE_BUTTON_INDEX;
        } else {
            if (mDestructiveButtonTitle != null) {
                index = pos - 1;
            } else {
                index = pos;
            }
        }

        return index;
    }

    private class ActionItem {
        int mType;
        String mTitle;

        ActionItem(int type, String title) {
            mType = type;
            mTitle = title;
        }
    }

    private class ActionAdapter extends ArrayAdapter<ActionItem> {
        private LayoutInflater mInflater;

        public ActionAdapter(Context context, List<ActionItem> objects) {
            super(context, 0, objects);
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getViewTypeCount() {
            return 3;
        }

        @Override
        public int getItemViewType(int position) {
            ActionItem item = this.getItem(position);
            return item.mType;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final int type = this.getItemViewType(position);
            ActionItem item = getItem(position);

            if (convertView == null) {
                switch (type) {
                    case ACTION_ITEM_NORMAL: {
                        convertView = mInflater.inflate(
                                R.layout.item_action_sheet, null);
                    }
                    break;
                    case ACTION_ITEM_DESTRUCTIVE: {
                        convertView = mInflater.inflate(
                                R.layout.item_action_sheet_special,
                                null);
                    }
                    break;
                }
                Holder holder = new Holder();
                holder.textView = (TextView) convertView
                        .findViewById(R.id.tv_title);
                holder.textView.setOnClickListener(holder);
                convertView.setTag(holder);
            }

            Holder holder = (Holder) convertView.getTag();
            holder.mPos = position;
            holder.mItem = item;
            holder.textView.setText(item.mTitle);

            return convertView;
        }
    }

    private class Holder implements View.OnClickListener {
        TextView textView;
        ActionItem mItem;
        int mPos;

        @Override
        public void onClick(View v) {
            int index = getActionItemBtnIndex(mItem, mPos);
            if (mClickListener != null) {
                mClickListener.onClick(ActionSheet.this, index);
            }
            dismiss();
        }
    }
}
