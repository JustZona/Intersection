package com.rent.zona.commponent.pickerwheel.config;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;



public class FormatConfig {

    private Locale locale;
    private final Map<String, String> timeFormatMap;

    private FormatConfig() {
        timeFormatMap = new HashMap<>();
    }

    private static class FormatConfigHolder {
        private static final FormatConfig INSTANCE = new FormatConfig();
    }

    public static FormatConfig getInstance() {
        return FormatConfigHolder.INSTANCE;
    }

    public void putConfig(String key, String value) {
        timeFormatMap.put(key, value);
    }

    public String getFormatValue(String key) {
        return timeFormatMap.get(key);
    }

    public Map<String, String> getTimeFormatMap() {
        return timeFormatMap;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Locale getLocale() {
        return locale;
    }
}
