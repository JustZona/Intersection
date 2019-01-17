package com.rent.zona.baselib.utils;

import android.content.Context;
import android.content.res.Resources;
import android.text.format.DateUtils;

import com.rent.zona.baselib.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class DateTimeUtils {
    private static final long SECONDS_PER_DAY = 3600 * 24;
    private static final long SECONDS_PER_HOUR = 3600;
    private static final long SECONDS_PER_MINUTE = 60;

    public static final long MINUTE_MS = 1000L * 60;
    public static final long HOUR_MS = MINUTE_MS * 60;
    public static final long DAY_MS = HOUR_MS * 24;

    private static SimpleDateFormat sDateTimeFormat = null;
    private static SimpleDateFormat sDateTimeNoSecFormat = null;
    private static SimpleDateFormat sDateFormat = null;
    private static SimpleDateFormat sSingleDateFormat = null;
    private static SimpleDateFormat sMmDdFormat = null;
    private static SimpleDateFormat sMDNoSecFormat = null;
    private static SimpleDateFormat sYMDNoSecFormat = null;
    private static SimpleDateFormat sWeekFormat = null;

    private static SimpleDateFormat sHmFormat = null;
    private static SimpleDateFormat sMDHMFormat = null;
    private static SimpleDateFormat sChinaMDHMFormat = null;
    private static SimpleDateFormat sYmdFormat = null;
    private static SimpleDateFormat sYearMonthFormat = null;
    private static SimpleDateFormat sPointYearMonthDayFormat = null;
    private static SimpleDateFormat sLineYMDHMDateFormat = null;
    private static SimpleDateFormat sYesterdayHmDateFormat = null;

    private static String china_year_month_day;
    private static String china_year_month_day_single;
    private static String year_month_day_hour_minute;
    private static String year_month_day_hour_minute_line;
    private static String china_year_month_day_hour_minute;
    private static String year_month_day_hour_minute_second;
    private static String year_month_day_point;
    private static String year_month_day;
    private static String year_month_day_single;
    private static String month_day;
    private static String month_day_hour_minute;
    private static String china_month_day;
    private static String hour_minute;
    private static String china_month_day_hour_minute;
    private static String china_year_month_single;
    private static String left, hour, minute, second;
    private static String week;
    static{
        china_year_month_day_single = "yyyy MMMM d";
        china_year_month_day ="yyyy MMMM dd";
        year_month_day_hour_minute ="yyyy-MM-dd HH:mm";
        year_month_day_hour_minute_line ="yyyy/MM/dd HH:mm";
        china_year_month_day_hour_minute = "yyyy MMMM dd HH:mm";
        year_month_day_hour_minute_second ="yyyy-MM-dd HH:mm:ss";
        year_month_day_point ="yyyy.MM.dd";
        year_month_day = "yyyy-MM-dd";
        year_month_day_single = "yyyy-M-d";
        month_day = "MM-dd";
        month_day_hour_minute = "MM-dd         HH:mm";
        china_month_day ="MMMM dd";
        hour_minute = "HH:mm";
        china_month_day_hour_minute = "MMMM dd HH:mm";
        china_year_month_single = "yyyy  MMMM";
        left = "还剩";
        hour ="时";
        minute = "分";
        second = "秒";
        week = "E";
    }

//    public static void initDateTimeUtils(Context context) {
//        Resources resources = context.getResources();
//
//        china_year_month_day_single = resources.getString(R.string.china_year_month_day_single);
//        china_year_month_day = resources.getString(R.string.china_year_month_day);
//        year_month_day_hour_minute = resources.getString(R.string.year_month_day_hour_minute);
//        year_month_day_hour_minute_line = resources.getString(R.string.year_month_day_hour_minute_line);
//        china_year_month_day_hour_minute = resources.getString(R.string.china_year_month_day_hour_minute);
//        year_month_day_hour_minute_second = resources.getString(R.string.year_month_day_hour_minute_second);
//        year_month_day_point = resources.getString(R.string.year_month_day_point);
//        year_month_day = resources.getString(R.string.year_month_day);
//        year_month_day_single = resources.getString(R.string.year_month_day_single);
//        month_day = resources.getString(R.string.month_day);
//        month_day_hour_minute = resources.getString(R.string.month_day_hour_minute);
//        china_month_day = resources.getString(R.string.china_month_day);
//        hour_minute = resources.getString(R.string.hour_minute);
//        china_month_day_hour_minute = resources.getString(R.string.china_month_day_hour_minute);
//        china_year_month_single = resources.getString(R.string.china_year_month_single);
//        left = resources.getString(R.string.left);
//        hour = resources.getString(R.string.hour);
//        minute = resources.getString(R.string.minute);
//        second = resources.getString(R.string.second);
//        week = "E";
//
//    }

    /**
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static SimpleDateFormat getDefaultFormatInstance() {
        if (sDateTimeFormat == null) {
            synchronized (DateTimeUtils.class) {
                if (sDateTimeFormat == null) {
                    sDateTimeFormat = new SimpleDateFormat(year_month_day_hour_minute_second, Locale.getDefault());
                }
            }
        }
        return sDateTimeFormat;
    }

    /**
     * @return "yyyy-MM-dd
     */
    public static SimpleDateFormat getDateFormatInstance() {
        if (sDateFormat == null) {
            synchronized (DateTimeUtils.class) {
                if (sDateFormat == null) {
                    sDateFormat = new SimpleDateFormat(year_month_day);
                }
            }
        }
        return sDateFormat;
    }

    /**
     * @return yyyy-M-d
     */
    public static SimpleDateFormat getSingleDateFormatInstance() {
        if (sSingleDateFormat == null) {
            synchronized (DateTimeUtils.class) {
                if (sSingleDateFormat == null) {
                    sSingleDateFormat = new SimpleDateFormat(year_month_day_single);
                }
            }
        }
        return sSingleDateFormat;
    }

    /**
     * @return MM-dd
     */
    public static SimpleDateFormat getMonthDayFormatInstance() {
        if (sMmDdFormat == null) {
            synchronized (DateTimeUtils.class) {
                if (sMmDdFormat == null) {
                    sMmDdFormat = new SimpleDateFormat(month_day);
                }
            }
        }
        return sMmDdFormat;
    }

    /**
     * @return HH:mm
     */
    public static SimpleDateFormat getHMFormatInstance() {
        if (sHmFormat == null) {
            synchronized (DateTimeUtils.class) {
                if (sHmFormat == null) {
                    sHmFormat = new SimpleDateFormat(hour_minute, Locale.getDefault());
                }
            }
        }
        return sHmFormat;
    }

    /**
     * @return yyyy年MM月dd日
     */
    public static SimpleDateFormat getYMDFormatInstance() {
        if (sYmdFormat == null) {
            synchronized (DateTimeUtils.class) {
                if (sYmdFormat == null) {
                    sYmdFormat = new SimpleDateFormat(
                            china_year_month_day, Locale.getDefault());
                }
            }
        }
        return sYmdFormat;
    }

    /**
     * @return yyyy年MM月dd日
     */
    public static SimpleDateFormat getSingleYMDFormatInstance() {
        if (sYmdFormat == null) {
            synchronized (DateTimeUtils.class) {
                if (sYmdFormat == null) {
                    sYmdFormat = new SimpleDateFormat(
                            china_year_month_day_single, Locale.getDefault());
                }
            }
        }
        return sYmdFormat;
    }

    /**
     * @return MM月dd日HH:mm
     */
    public static SimpleDateFormat getChinaMDHMFormatInstance() {
        if (sChinaMDHMFormat == null) {
            synchronized (DateTimeUtils.class) {
                if (sChinaMDHMFormat == null) {
                    sChinaMDHMFormat = new SimpleDateFormat(
                            china_month_day_hour_minute, Locale.getDefault());
                }
            }
        }
        return sMDHMFormat;
    }

    /**
     * @return MM-dd  HH:mm
     */
    public static SimpleDateFormat getMDHMFormatInstance() {
        if (sMDHMFormat == null) {
            synchronized (DateTimeUtils.class) {
                if (sMDHMFormat == null) {
                    sMDHMFormat = new SimpleDateFormat(
                            month_day_hour_minute, Locale.getDefault());
                }
            }
        }
        return sMDHMFormat;
    }


    /**
     * @return yyyy-MM-dd HH:mm
     */
    public static SimpleDateFormat getYMDHMFormatInstance() {
        if (sDateTimeNoSecFormat == null) {
            synchronized (DateTimeUtils.class) {
                if (sDateTimeNoSecFormat == null) {
                    sDateTimeNoSecFormat = new SimpleDateFormat(year_month_day_hour_minute, Locale.getDefault());
                }
            }
        }
        return sDateTimeNoSecFormat;
    }

    /**
     * @return yyyy年MM月dd日 HH点mm分
     */
    public static SimpleDateFormat getYMDChinaFormatInstance() {
        if (sYMDNoSecFormat == null) {
            synchronized (DateTimeUtils.class) {
                if (sYMDNoSecFormat == null) {
                    sYMDNoSecFormat = new SimpleDateFormat(china_year_month_day_hour_minute, Locale.getDefault());
                }
            }
        }
        return sYMDNoSecFormat;
    }

    /**
     * @return MM月dd日
     */
    public static SimpleDateFormat getMDHChinaMFormatInstance() {
        if (sMDNoSecFormat == null) {
            synchronized (DateTimeUtils.class) {
                if (sMDNoSecFormat == null) {
                    sMDNoSecFormat = new SimpleDateFormat(china_month_day, Locale.getDefault());
                }
            }
        }
        return sMDNoSecFormat;
    }

    /**
     * @return yyyy年M月
     */
    public static SimpleDateFormat getYearMonthFormatInstance() {
        if (sYearMonthFormat == null) {
            synchronized (DateTimeUtils.class) {
                if (sYearMonthFormat == null) {
                    sYearMonthFormat = new SimpleDateFormat(china_year_month_single, Locale.getDefault());
                }
            }
        }
        return sYearMonthFormat;
    }

    /**
     * @return yyyy.MM.dd
     */
    public static SimpleDateFormat getFullPointDateFormat() {
        if (sPointYearMonthDayFormat == null) {
            synchronized (DateTimeUtils.class) {
                if (sPointYearMonthDayFormat == null) {
                    sPointYearMonthDayFormat = new SimpleDateFormat(year_month_day_point, Locale.getDefault());
                }
            }
        }
        return sPointYearMonthDayFormat;
    }

    /**
     * @return yyyy/MM/dd HH:mm
     */
    public static SimpleDateFormat getLineYMDHMDateFormat() {
        if (sLineYMDHMDateFormat == null) {
            synchronized (DateTimeUtils.class) {
                if (sLineYMDHMDateFormat == null) {
                    sLineYMDHMDateFormat = new SimpleDateFormat(year_month_day_hour_minute_line, Locale.getDefault());
                }
            }
        }
        return sLineYMDHMDateFormat;
    }

    /**
     * 星期几
     *
     * @return
     */
    public static SimpleDateFormat getWeekFormat() {
        if (sWeekFormat == null) {
            synchronized (DateTimeUtils.class) {
                if (sWeekFormat == null) {
                    sWeekFormat = new SimpleDateFormat(week, Locale.getDefault());
                }
            }
        }
        return sWeekFormat;
    }

    /**
     * @param time returned by System.currentTimeMillis(), in milliseconds.
     * @return In format "yyyy-MM-dd HH:mm"
     */
    public static String formatDateTimeNoSecond(long time) {
        return getYMDHMFormatInstance().format(new Date(time));
    }

    /**
     * @param time returned by System.currentTimeMillis(), in milliseconds.
     * @return In format "yyyy-MM-dd HH:mm:ss"
     */
    public static String formatDefault(long time) {
        return getDefaultFormatInstance().format(new Date(time));
    }

    /**
     * Format time to stirng "yyyy-MM-dd"
     */
    public static String formatDate(long time) {
        return getDateFormatInstance().format(new Date(time));
    }

    /**
     * Format time to stirng "yyyy年MM月dd日"
     */
    public static String formatDateDay(long time) {
        return getYMDFormatInstance().format(new Date(time));
    }

    public static String formatDate(Date date) {
        return getDateFormatInstance().format(date);
    }

    /**
     * Format time to stirng "MM-dd"
     */
    public static String formatMonthDay(long time) {
        return getMonthDayFormatInstance().format(new Date(time));
    }


    /**
     * Format time to stirng "HH:mm"
     */
    public static String formatHHmm(long time) {
        return getHMFormatInstance().format(new Date(time));
    }

    /**
     * 星期几
     *
     * @param time
     * @return
     */
    public static String formatWeek(long time) {
        return getWeekFormat().format(new Date(time));
    }

    /**
     * To determine whether the same day
     */
    public static boolean isSameDay(long time1, long time2) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long millSecond = cal.getTimeInMillis();
        return (millSecond <= time2 && time2 < millSecond + DAY_MS);
    }

    /**
     * @return Current month first day's date. format: MM-dd
     */
    public static String getCurrentMonthFirstDayDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 1);
        return getMonthDayFormatInstance().format(cal.getTime());
    }

    /**
     * @return Last month first day's date. format: MM-dd
     */
    public static String getLastMonthFirstDayDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 1);
        cal.add(Calendar.MONTH, -1);
        return getMonthDayFormatInstance().format(cal.getTime());
    }

    /**
     * @return Last month last day's date. format: MM-dd
     */
    public static String getLastMonthLastDayDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 1);
        cal.add(Calendar.DATE, -1);
        return getMonthDayFormatInstance().format(cal.getTime());
    }

    /**
     * @return Yesterday date. format: MM-dd
     */
    public static String getYesterdayDate() {
        long befDateTimeMill = System.currentTimeMillis() - DAY_MS;
        return getMonthDayFormatInstance().format(new Date(befDateTimeMill));
    }
    public static String getCurDate(){
        return getSingleDateFormatInstance().format(new Date());
    }
    public static boolean isCurrentMonthFirstDay(long curTime) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long millSecond = cal.getTimeInMillis();
        return (millSecond <= curTime && curTime < millSecond
                + DAY_MS);
    }

    /**
     * get the date before day
     *
     * @param date
     * @param day  before
     * @return
     */
    public static Date getDateBeforeDay(Date date, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        return now.getTime();
    }

    public static Date getTodayBeforeDay(int day) {
        return getDateBeforeDay(new Date(), day);
    }

    public static Date getDateAfterDay(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }

    public static Date getDateAfterDay(int day) {
        return getDateAfterDay(new Date(), day);
    }

    public static String getRelativeTimeBySystemFormat(Context cxt, long date) {
        return DateUtils.getRelativeDateTimeString(cxt, date,
                DateUtils.SECOND_IN_MILLIS, DateUtils.DAY_IN_MILLIS, 0)
                .toString();
    }

    public static int getCurMonth() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        return c.get(Calendar.MONTH) + 1;
    }

    public static long getTime(String date,String formatStr) throws ParseException {
        SimpleDateFormat format=new SimpleDateFormat(formatStr, Locale.getDefault());
        return format.parse(date).getTime();
    }

    /**
     * Convert system time to integer with format: yyyymmdd (such as 20130417)
     *
     * @param sysTime System time from System.currentTimeMillis()
     */
    public static long getDayFromSysTime(long sysTime) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(sysTime);
        return c.get(Calendar.YEAR) * 10000 + (c.get(Calendar.MONTH) + 1) * 100
                + c.get(Calendar.DAY_OF_MONTH);
    }

    public static boolean isToday(long mills) {
        if (mills <= 0) {
            return false;
        }
        Calendar target = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        target.setTimeInMillis(mills);
        today.setTime(new Date());
        return isSameDay(today, target);
    }

    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA)
                && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    public static boolean isYesterday(long mills) {
        if (mills <= 0) {
            return false;
        }
        Calendar target = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        target.setTimeInMillis(mills);
        today.setTime(new Date());
        target.add(Calendar.DATE, 1);
        return isSameDay(today, target);
    }

    public static boolean isSameWeek(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA)
                && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR);
    }

    public static boolean isSameYear(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA)
                && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
    }

    public static boolean isThisWeek(long mills) {
        if (mills <= 0) {
            return false;
        }
        Calendar target = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        target.setTimeInMillis(mills);
        today.setTime(new Date());
        return isSameWeek(today, target);
    }

    public static long addHours(long src, float hours) {
        return src + (long) (hours * HOUR_MS);
    }

    public static long addMinutes(long src, float minutes) {
        return src + (long) (minutes * MINUTE_MS);
    }

    public static long subHours(long src, float hours) {
        return src - (long) (hours * HOUR_MS);
    }

    public static long subMinutes(long src, float minutes) {
        return src - (long) (minutes * MINUTE_MS);
    }

    /**
     * seconds to hms
     *
     * @param seconds
     * @return [0]=hour,[1]=minute
     */
    public static int[] getHM(long seconds) {
        int[] hms = new int[2];
        final int m = (int) ((seconds / 60) % 60);
        final int h = (int) (seconds / (60 * 60));
        hms[0] = h;
        hms[1] = m;
        return hms;
    }

    public static String getHMS(long seconds) {
        final long h = seconds / (60 * 60L);
        final long m = (seconds - h * 3600) / 60L;
        final long s = seconds - h * 3600 - m * 60;
        return String.format("%02d:%02d:%02d", h, m, s);
    }

    public static String getMS(long seconds) {
        final long m = (seconds) / 60L;
        final long s = seconds - m * 60;
        return String.format("%02d:%02d", m, s);
    }

    public static int getHours(long seconds) {
        return (int) (seconds / (60 * 60));
    }

    public static int getHoursCarry(long seconds) {
        double r = seconds / 3600D;
        return (int) Math.ceil(r);
    }

    public static boolean isChinese(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        if (locale != null) {
            return locale.equals(Locale.CHINA);
        }
        return false;
    }

    public static String createTime(int h, int m, int s) {
//        if (Locale.getDefault().equals(Locale.CHINA)) {
//            return left + h + hour + m + minute + s + second;
//        } else {
//            return h + " " + hour + " " + m + " " + minute + " " + s + " " + second + " " + left;
//        }

        if (Locale.getDefault().equals(Locale.CHINA)) {
            return "还剩" + h + "小时" + m + "分" + s + "秒";
        } else {
            return "Left:" + h + "H" + m + "M" + s + "s";
        }
    }
    public static String getTodayYesterdayDate(long time){
        Calendar calendar=Calendar.getInstance();
        Date date=new Date(time);
        calendar.setTime(new Date(time));
        Calendar today=getTodayCal();
        Calendar yesterday=getYesterdayCal();
        Date t=today.getTime();
        Date m=yesterday.getTime();
        if(calendar.before(yesterday)){
            return formatDateTimeNoSecond(time);
        }else if(calendar.before(today)){
            return "昨天 "+formatHHmm(time);
        }else {
            return "今天 "+formatHHmm(time);
        }
    }
    public static Calendar getTodayCal(){
        Calendar today=Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        return today;
    }
    public static Calendar getYesterdayCal(){
        Calendar yesterday=Calendar.getInstance();
        yesterday.add(Calendar.DATE,-1);
        yesterday.set(Calendar.HOUR_OF_DAY, 0);
        yesterday.set(Calendar.MINUTE, 0);
        yesterday.set(Calendar.SECOND, 0);
        return yesterday;
    }
}
