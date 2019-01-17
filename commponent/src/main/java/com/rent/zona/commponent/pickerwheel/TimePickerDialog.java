package com.rent.zona.commponent.pickerwheel;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.rent.zona.commponent.R;
import com.rent.zona.commponent.pickerwheel.config.FormatConfig;
import com.rent.zona.commponent.pickerwheel.config.PickerConfig;
import com.rent.zona.commponent.pickerwheel.data.Type;
import com.rent.zona.commponent.pickerwheel.data.WheelCalendar;
import com.rent.zona.commponent.pickerwheel.listener.OnDateSetListener;
import com.rent.zona.commponent.pickerwheel.utils.PickerConstants;

import java.util.Calendar;
import java.util.Locale;


public class TimePickerDialog extends Dialog implements View.OnClickListener {
    PickerConfig mPickerConfig;
    private TimeWheel mTimeWheel;
    private long mCurrentMillSeconds;
    private View rootView; // 总的布局
    private View btnSubmit, btnCancel;
    private TextView titleView;
    private static final String TAG_SUBMIT = "submit";
    private static final String TAG_CANCEL = "cancel";


    public TimePickerDialog(Context context, PickerConfig pickerConfig) {
        super(context, R.style.MyWidget_ActionSheet);
        this.mPickerConfig = pickerConfig;
        setContentView(initView());

        Window win = getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0); // make dialog full screen
        WindowManager.LayoutParams wmlp = getWindow().getAttributes();
        wmlp.gravity = Gravity.BOTTOM;
        wmlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wmlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(wmlp);
        setCanceledOnTouchOutside(true);

        Locale locale = context.getResources().getConfiguration().locale;
        FormatConfig.getInstance().setLocale(locale);
    }

    View initView() {
        LayoutInflater mLayoutInflater = LayoutInflater.from(getContext());
        rootView = mLayoutInflater.inflate(R.layout.pw_time, null);
        titleView=rootView.findViewById(R.id.time_title);
        btnSubmit = rootView.findViewById(R.id.btn_submit);
        btnSubmit.setTag(TAG_SUBMIT);
        btnCancel = rootView.findViewById(R.id.btn_cancel);
        btnCancel.setTag(TAG_CANCEL);
        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        mTimeWheel = new TimeWheel(rootView, mPickerConfig);
        return rootView;
    }

    public void setTitle(CharSequence title){
        titleView.setText(title);
    }
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_cancel) {
            dismiss();
        } else if (i == R.id.btn_submit) {
            sureClicked();
        }
    }

    /*
    * @desc This method returns the current milliseconds. If current milliseconds is not set,
    *       this will return the system milliseconds.
    * @param none
    * @return long - the current milliseconds.
    */
    public long getCurrentMillSeconds() {
        if (mCurrentMillSeconds == 0)
            return System.currentTimeMillis();

        return mCurrentMillSeconds;
    }

    /*
    * @desc This method is called when onClick method is invoked by sure button. A Calendar instance is created and 
    *       initialized. 
    * @param none
    * @return none
    */
    void sureClicked() {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();

        calendar.set(Calendar.YEAR, mTimeWheel.getCurrentYear());
        calendar.set(Calendar.MONTH, mTimeWheel.getCurrentMonth() - 1);
        calendar.set(Calendar.DAY_OF_MONTH, mTimeWheel.getCurrentDay());
        calendar.set(Calendar.HOUR_OF_DAY, mTimeWheel.getCurrentHour());
        calendar.set(Calendar.MINUTE, mTimeWheel.getCurrentMinute());

        mCurrentMillSeconds = calendar.getTimeInMillis();
        if (mPickerConfig.mCallBack != null) {
            mPickerConfig.mCallBack.onDateSet(this, mCurrentMillSeconds);
        }
        dismiss();
    }

    public static class Builder {
        PickerConfig mPickerConfig;
        Context context;
        String mTitleString = "TimePicker";
        String mCancelString = "Cancel";
        String mSureString = "Complete";
        String mYear = "Year", mMonth = "Month", mDay = "Day", mHour = "Hour", mMinute = "Minute";

        public Builder(Context context) {
            mPickerConfig = new PickerConfig();
            this.context = context;
            setCancelStringId(context.getString(R.string.pickerview_cancel));
            setSureStringId(context.getString(R.string.pickerview_submit));
            setYearText(context.getString(R.string.pickerview_year));
            setMonthText(context.getString(R.string.pickerview_month));
            setDayText(context.getString(R.string.pickerview_day));
            setHourText(context.getString(R.string.pickerview_hours));
            setMinuteText(context.getString(R.string.pickerview_minutes));
        }

        public Builder setType(Type type) {
            mPickerConfig.mType = type;
            return this;
        }

        public Builder setThemeColor(int color) {
            mPickerConfig.mThemeColor = color;
            return this;
        }

        public Builder setCancelStringId(String left) {
            mCancelString = left;
            return this;
        }

        public Builder setSureStringId(String right) {
            mSureString = right;
            return this;
        }

        public Builder setTitleString(String title) {
            mTitleString = title;
            return this;
        }

        public Builder setToolBarTextColor(int color) {
            mPickerConfig.mToolBarTVColor = color;
            return this;
        }

        public Builder setWheelItemTextNormalColor(int color) {
            mPickerConfig.mWheelTVNormalColor = color;
            return this;
        }

        public Builder setWheelItemTextSelectorColor(int color) {
            mPickerConfig.mWheelTVSelectorColor = color;
            return this;
        }

        public Builder setWheelItemTextSize(int size) {
            mPickerConfig.mWheelTVSize = size;
            return this;
        }

        public Builder setCyclic(boolean cyclic) {
            mPickerConfig.cyclic = cyclic;
            return this;
        }

        public Builder setMinMillseconds(long millseconds) {
            mPickerConfig.mMinCalendar = new WheelCalendar(millseconds);
            return this;
        }

        public Builder setMaxMillseconds(long millseconds) {
            mPickerConfig.mMaxCalendar = new WheelCalendar(millseconds);
            return this;
        }

        public Builder setCurrentMillseconds(long millseconds) {
            mPickerConfig.mCurrentCalendar = new WheelCalendar(millseconds);
            return this;
        }

        public Builder setYearText(String year) {
            mYear = year;
            FormatConfig.getInstance().putConfig(PickerConstants.YEAR, year);
            return this;
        }

        public Builder setMonthText(String month) {
            mMonth = month;
            FormatConfig.getInstance().putConfig(PickerConstants.MONTH, month);
            return this;
        }

        public Builder setDayText(String day) {
            mDay = day;
            FormatConfig.getInstance().putConfig(PickerConstants.DAY, day);
            return this;
        }

        public Builder setHourText(String hour) {
            mHour = hour;
            FormatConfig.getInstance().putConfig(PickerConstants.HOUR, hour);
            return this;
        }

        public Builder setMinuteText(String minute) {
            mMinute = minute;
            FormatConfig.getInstance().putConfig(PickerConstants.MINUTE, minute);
            return this;
        }

        public Builder setCallBack(OnDateSetListener listener) {
            mPickerConfig.mCallBack = listener;
            return this;
        }

        public TimePickerDialog build() {
            return new TimePickerDialog(context, mPickerConfig);
        }

    }


}
