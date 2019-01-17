package com.rent.zona.commponent.utils;

import android.text.TextUtils;

import java.io.IOException;

/**
 * @name：
 * @author： liuyun
 * @createTime： 2017/11/29
 * @modifyTime： 2017/11/29
 * @explain：说明
 */


public class OsUtil {
    public static final String MIUI_V5="V5";
    public static final String MIUI_V6="V6";
    public static final String MIUI_V7="V7";
    public static final String MIUI_V8="V8";
    public static final String MIUI_V9="V9";
    //MIUI标识
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

    //EMUI标识
    private static final String KEY_EMUI_VERSION_CODE = "ro.build.version.emui";
    private static final String KEY_EMUI_API_LEVEL = "ro.build.hw_emui_api_level";
    private static final String KEY_EMUI_CONFIG_HW_SYS_VERSION = "ro.confg.hw_systemversion";

    //Flyme标识
    private static final String KEY_FLYME_ID_FALG_KEY = "ro.build.display.id";
    private static final String KEY_FLYME_ID_FALG_VALUE_KEYWORD = "Flyme";
    private static final String KEY_FLYME_ICON_FALG = "persist.sys.use.flyme.icon";
    private static final String KEY_FLYME_SETUP_FALG = "ro.meizu.setupwizard.flyme";
    private static final String KEY_FLYME_PUBLISH_FALG = "ro.flyme.published";

    // vivo : FuntouchOS
    private static final String KEY_FUNTOUCHOS_BOARD_VERSION = "ro.vivo.board.version"; // "MD"
    private static final String KEY_FUNTOUCHOS_OS_NAME = "ro.vivo.os.name"; // "Funtouch"
    private static final String KEY_FUNTOUCHOS_OS_VERSION = "ro.vivo.os.version"; // "3.0"
    private static final String KEY_FUNTOUCHOS_DISPLAY_ID = "ro.vivo.os.build.display.id"; // "FuntouchOS_3.0"
    private static final String KEY_FUNTOUCHOS_ROM_VERSION = "ro.vivo.rom.version"; // "rom_3.1"

    public static String getMIMURom(){

        try {
            BuildProperties p =BuildProperties.getInstance();
            if(getRomType()== ROM_TYPE.MIUI){
                return p.getProperty(KEY_MIUI_VERSION_NAME,null);
            }else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * @param
     * @return ROM_TYPE ROM类型的枚举
     * @description获取ROM类型: MIUI_ROM, FLYME_ROM, EMUI_ROM, OTHER_ROM
     */
    public static ROM_TYPE getRomType() {
        return getRomType(null);
    }
    public static ROM_TYPE getRomType(BuildProperties buildPro) {
        ROM_TYPE rom_type = ROM_TYPE.OTHER;
        try {
            BuildProperties buildProperties ;
            if(buildPro==null) {
                buildProperties = BuildProperties.getInstance();
            }else {
                buildProperties=buildPro;
            }

            if (buildProperties.containsKey(KEY_EMUI_VERSION_CODE) || buildProperties.containsKey(KEY_EMUI_API_LEVEL) || buildProperties.containsKey(KEY_MIUI_INTERNAL_STORAGE)) {
                return ROM_TYPE.EMUI;
            }
            if (buildProperties.containsKey(KEY_MIUI_VERSION_CODE) || buildProperties.containsKey(KEY_MIUI_VERSION_NAME) || buildProperties.containsKey(KEY_MIUI_VERSION_NAME)) {
                return ROM_TYPE.MIUI;
            }
            if (buildProperties.containsKey(KEY_FLYME_ICON_FALG) || buildProperties.containsKey(KEY_FLYME_SETUP_FALG) || buildProperties.containsKey(KEY_FLYME_PUBLISH_FALG)) {
                return ROM_TYPE.FLYME;
            }
            if (buildProperties.containsKey(KEY_FLYME_ID_FALG_KEY)) {
                String romName = buildProperties.getProperty(KEY_FLYME_ID_FALG_KEY);
                if (!TextUtils.isEmpty(romName) && romName.contains(KEY_FLYME_ID_FALG_VALUE_KEYWORD)) {
                    return ROM_TYPE.FLYME;
                }
            }

            if(buildProperties.containsKey(KEY_FUNTOUCHOS_BOARD_VERSION)
                    ||buildProperties.containsKey(KEY_FUNTOUCHOS_OS_NAME)
                    ||buildProperties.containsKey(KEY_FUNTOUCHOS_OS_VERSION)
                    ||buildProperties.containsKey(KEY_FUNTOUCHOS_DISPLAY_ID)
                    ||buildProperties.containsKey(KEY_FUNTOUCHOS_ROM_VERSION)){
                return ROM_TYPE.VIVO;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return rom_type;
    }
    public static String getRomStr() {
        return getRomStr(null);
    }
    public static String getRomStr(BuildProperties buildPro) {
        ROM_TYPE rom_type = ROM_TYPE.OTHER;
        try {
            BuildProperties buildProperties ;
            if(buildPro==null) {
                buildProperties = BuildProperties.getInstance();
            }else {
                buildProperties=buildPro;
            }

            if (buildProperties.containsKey(KEY_EMUI_VERSION_CODE) || buildProperties.containsKey(KEY_EMUI_API_LEVEL) || buildProperties.containsKey(KEY_MIUI_INTERNAL_STORAGE)) {
                return buildProperties.getProperty(KEY_EMUI_API_LEVEL);
            }
            if (buildProperties.containsKey(KEY_MIUI_VERSION_CODE) || buildProperties.containsKey(KEY_MIUI_VERSION_NAME) || buildProperties.containsKey(KEY_MIUI_VERSION_NAME)) {
                return buildProperties.getProperty(KEY_MIUI_VERSION_NAME);
            }
            if (buildProperties.containsKey(KEY_FLYME_ICON_FALG) || buildProperties.containsKey(KEY_FLYME_SETUP_FALG) || buildProperties.containsKey(KEY_FLYME_PUBLISH_FALG)) {
                return buildProperties.getProperty(KEY_FLYME_ICON_FALG);
            }
            if (buildProperties.containsKey(KEY_FLYME_ID_FALG_KEY)) {
                String romName = buildProperties.getProperty(KEY_FLYME_ID_FALG_KEY);
                return romName;
            }

            if(buildProperties.containsKey(KEY_FUNTOUCHOS_BOARD_VERSION)
                    ||buildProperties.containsKey(KEY_FUNTOUCHOS_OS_NAME)
                    ||buildProperties.containsKey(KEY_FUNTOUCHOS_OS_VERSION)
                    ||buildProperties.containsKey(KEY_FUNTOUCHOS_DISPLAY_ID)
                    ||buildProperties.containsKey(KEY_FUNTOUCHOS_ROM_VERSION)){
                return buildProperties.getProperty(KEY_FUNTOUCHOS_ROM_VERSION);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    public enum ROM_TYPE {
        MIUI,
        FLYME,
        EMUI,
        VIVO,
        OTHER
    }

}
