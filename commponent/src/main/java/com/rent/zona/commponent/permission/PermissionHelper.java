package com.rent.zona.commponent.permission;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import com.rent.zona.baselib.configs.LibConfigs;
import com.rent.zona.baselib.log.LibLogger;
import com.rent.zona.baselib.utils.DeviceUtil;
import com.rent.zona.commponent.R;
import com.rent.zona.commponent.dlg.CommonDialog;
import com.rent.zona.commponent.utils.OsUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class PermissionHelper {
    private static final String TAG = PermissionHelper.class.getSimpleName();

    public interface PermissionResult {
        void onPermissionGranted(int requestCode);

        void onPermissionDenied(int requestCode, String... noPermissions);
    }
    public static boolean checkSelfPermission(Context context, String permission){
        try {
            return ActivityCompat.checkSelfPermission(context, permission)== PackageManager.PERMISSION_GRANTED;
        } catch (RuntimeException e) {
            return false;
        }
    }
    public static void requestPermission(final Activity activity, String requestPermission, String applyDes, final int requestCode, PermissionResult permissionGrant) {
        requestPermission(activity,null,requestPermission,applyDes,requestCode,permissionGrant);
    }
    public static void requestPermission(final Activity activity, Fragment fragment, String requestPermission, String applyDes, final int requestCode, PermissionResult permissionGrant) {
        if (activity == null && fragment==null) {
            return;
        }
        //如果是6.0以下的手机，ActivityCompat.checkSelfPermission()会始终等于PERMISSION_GRANTED，
        // 但是，如果用户关闭了你申请的权限，ActivityCompat.checkSelfPermission(),会导致程序崩溃(java.lang.RuntimeException: Unknown exception code: 1 msg null)，
        // 你可以使用try{}catch(){},处理异常，也可以判断系统版本，低于23就不申请权限，直接做你想做的。permissionGrant.onPermissionGranted(requestCode);
//        if (Build.VERSION.SDK_INT < 23) {
//            permissionGrant.onPermissionGranted(requestCode);
//            return;
//        }
        int checkSelfPermission;
        try {
            checkSelfPermission = ActivityCompat.checkSelfPermission(activity, requestPermission);

        } catch (RuntimeException e) {
            permissionGrant.onPermissionDenied(requestCode);
            return;
        }

        if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
            if(!TextUtils.isEmpty(applyDes)) {
                CommonDialog dlg = new CommonDialog(activity);
                dlg.setTitle("权限申请");
                dlg.setMessage(applyDes);
                dlg.setCancelBtnVisible(false);
                dlg.setOkBtn(R.string.go_open, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (fragment != null) {
                            fragment.requestPermissions(new String[]{requestPermission}, requestCode);
                        } else {
                            ActivityCompat.requestPermissions(activity, new String[]{requestPermission}, requestCode);
                        }
                    }
                });
                dlg.show();
            }else{
                if (fragment != null) {
                    fragment.requestPermissions(new String[]{requestPermission}, requestCode);
                } else {
                    ActivityCompat.requestPermissions(activity, new String[]{requestPermission}, requestCode);
                }
            }

        } else {
            permissionGrant.onPermissionGranted(requestCode);
        }
    }

    /**
     * 一次申请多个权限
     */
    public static void requestMultiPermissions(final Activity activity, Fragment fragment, int requestCode, String applyDes, PermissionResult grant, String... permisions ) {

        if(permisions==null || permisions.length==0){
            return;
        }
        final List<String> noPermissionsList = getNoGrantedPermission(activity, permisions);

        if((noPermissionsList==null || noPermissionsList.size()==0)){
            grant.onPermissionGranted(requestCode);
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//系统版本大于等于6.0
            if(!TextUtils.isEmpty(applyDes)) {
                CommonDialog dlg = new CommonDialog(activity);
                dlg.setTitle("权限申请");
                dlg.setMessage(applyDes);
                dlg.setCancelBtnVisible(false);
                dlg.setOkBtn(R.string.go_open, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (fragment != null) {
                            fragment.requestPermissions(noPermissionsList.toArray(new String[noPermissionsList.size()]), requestCode);
                        } else {
                            ActivityCompat.requestPermissions(activity, noPermissionsList.toArray(new String[noPermissionsList.size()]), requestCode);
                        }
                    }
                });
                dlg.show();
            }else{
                if (fragment != null) {
                    fragment.requestPermissions(noPermissionsList.toArray(new String[noPermissionsList.size()]), requestCode);
                } else {
                    ActivityCompat.requestPermissions(activity, noPermissionsList.toArray(new String[noPermissionsList.size()]), requestCode);
                }
            }
        }else{
            grant.onPermissionDenied(requestCode,noPermissionsList.toArray(new String[noPermissionsList.size()]));
        }
    }

    public static ArrayList<String> getNoGrantedPermission(Activity activity, String... permisions) {

        ArrayList<String> permissions = new ArrayList<>();

        for (int i = 0; i < permisions.length; i++) {
            String requestPermission = permisions[i];

            //TODO checkSelfPermission
            int checkSelfPermission = -1;
            try {
                checkSelfPermission = ActivityCompat.checkSelfPermission(activity, requestPermission);
            } catch (RuntimeException e) {
                permissions.add(requestPermission);
                continue;
            }

            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
//                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, requestPermission)) {//由于有些系统shouldShowRequestPermissionRationale不支持 此处不建议做判断
//                    if (isShouldRationale) {
                        permissions.add(requestPermission);
//                    }
//
//                } else {
//                    if (!isShouldRationale) {
//                        permissions.add(requestPermission);
//                    }
//                }

            }
        }

        return permissions;
    }
    public static boolean hasNoGranted(Context context, String...permisions){
        for(String per:permisions){
            int checkSelfPermission = -1;
            try {
                checkSelfPermission = ActivityCompat.checkSelfPermission(context, per);
            } catch (RuntimeException e) {
                 return true;
            }
            if(checkSelfPermission== PackageManager.PERMISSION_DENIED){
                return true;
            }
        }
        return false;
    }
    public static void requestPermissionsResult(final int requestCode, @NonNull String[] permissions,
                                                @NonNull int[] grantResults, PermissionResult permissionGrant) {

        if (grantResults.length == 1) {
             if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                 permissionGrant.onPermissionGranted(requestCode);
             }else{
                 permissionGrant.onPermissionDenied(requestCode,permissions);
             }
        }else{
            ArrayList<String> notGranted = new ArrayList<>();
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    notGranted.add(permissions[i]);
                }
            }
            if (notGranted.size() == 0) {
                permissionGrant.onPermissionGranted(requestCode);
            } else {
                String[] noPermissions=new String[notGranted.size()];
                for(int i=0;i<notGranted.size();i++){
                    noPermissions[i]=notGranted.get(i);
                }
                permissionGrant.onPermissionDenied(requestCode, noPermissions);
            }
        }

    }

    public static void permissionDined(Context context, String mLessMDes, String mdes){

        CommonDialog dlg = new CommonDialog(context);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {//系统版本小于6.0
            dlg.setTitle("温馨提示");
            dlg.setMessage(mLessMDes);
            dlg.setCancelBtnVisible(false);
            dlg.setOkBtn(R.string.know, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dlg.dismiss();
                }
            });
        }else{
            dlg.setTitle("开启权限");
            dlg.setMessage(mdes);

            dlg.setOkBtn(R.string.go_open, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goSettingActivity(context);
                    dlg.dismiss();
                }
            });
            dlg.setCancelBtn(R.string.cancel, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dlg.dismiss();
                }
            });
        }
        dlg.show();
    }

    /**
     * 去设置页面
     * @param context
     */
    public static void goSettingActivity(Context context) {
//        Intent intent = new Intent();
//        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
//        intent.setData(uri);
//        context.startActivity(intent);


//        Intent intent = null;
//        String mimuRom = OsUtil.getMIMURom();
//
//            if (OsUtil.MIUI_V6.equals(mimuRom) || OsUtil.MIUI_V7.equals(mimuRom)) {
//            intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
//            intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
//            intent.putExtra("extra_pkgname", context.getPackageName());
//        } else if (OsUtil.MIUI_V8.equals(mimuRom)) {
//            intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
//            intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
//            intent.putExtra("extra_pkgname", context.getPackageName());
//        } else {
//            intent = new Intent();
//            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//            Uri uri = Uri.fromParts("package", context.getPackageName(), null);
//            intent.setData(uri);
//        }

        OsUtil.ROM_TYPE rom_type=OsUtil.getRomType();
        Intent intent = null;
        switch (rom_type){
            case MIUI:
                String mimuRom = OsUtil.getMIMURom();
                if (OsUtil.MIUI_V6.equals(mimuRom) || OsUtil.MIUI_V7.equals(mimuRom)) {
                    intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                    intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
                    intent.putExtra("extra_pkgname", context.getPackageName());
                } else if (OsUtil.MIUI_V8.equals(mimuRom)) {
                    intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                    intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
                    intent.putExtra("extra_pkgname", context.getPackageName());
                }
                break;
            case FLYME:
                intent = new Intent();
                intent.setAction("com.meizu.safe.security.SHOW_APPSEC");
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.putExtra("packageName", context.getPackageName());
                break;
            case EMUI:
                break;
            case VIVO:
                intent = new Intent();
                intent.putExtra("packagename",context.getPackageName());
                intent.putExtra("title",context.getString(R.string.app_name));
                //i管家包名 6.0有不相同的两款手机
                intent.setComponent(ComponentName.unflattenFromString("com.iqoo.secure/.safeguard.SoftPermissionDetailActivity"));
                if (startSafely(context,intent)){
                    return;
                }
                intent.setComponent(ComponentName.unflattenFromString("com.vivo.permissionmanager/.activity.SoftPermissionDetailActivity"));
                if (startSafely(context,intent)){
                    return;
                }
                break;

        }
        if(intent==null){
            intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", context.getPackageName(), null);
            intent.setData(uri);
        }
        context.startActivity(intent);
    }

    private static boolean startSafely(Context context, Intent intent) {
        try {
            context.startActivity(intent);
            return true;
        }catch (Exception e){
            if(LibConfigs.isDebugLog()){
                e.printStackTrace();
                LibLogger.dw("权限跳转异常", DeviceUtil.getDeviceBrand());
                LibLogger.dw("权限跳转异常", OsUtil.getRomStr());
            }
            return false;
        }
    }
    public static boolean checkCamera(Context cox){
        int checkSelfPermission;
        try {
            checkSelfPermission = ActivityCompat.checkSelfPermission(cox, Manifest.permission.CAMERA);
        } catch (RuntimeException e) {
            checkSelfPermission = PackageManager.PERMISSION_GRANTED;
        }
        if (checkSelfPermission == PackageManager.PERMISSION_GRANTED) {
              if(isCameraCanUse()){
                  return true;
              }
        }
        return false;
    }
    /**
     * 测试当前摄像头能否被使用
     *
     * @return
     */
    public static boolean isCameraCanUse() {
        boolean canUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open(0);

            if(OsUtil.ROM_TYPE.VIVO==OsUtil.getRomType()){
               return isHasPermission(mCamera);
            }else{
                mCamera.setDisplayOrientation(90);
            }
        } catch (Exception e) {
            canUse = false;
        }
        if (canUse) {
            mCamera.release();
            mCamera = null;
        }
        //Timber.v("isCameraCanuse="+canUse);
        return canUse;
    }

    /**
     * 针对vivo
     * @return
     */
    private static boolean isHasPermission(Camera camera){
        Field fieldPassword = null;
        try {
            fieldPassword = camera.getClass().getDeclaredField("mHasPermission");
            fieldPassword.setAccessible(true);
            return (boolean) fieldPassword.get(camera);
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }

    }
    public static boolean isDenied(Context cox, String permission){
        int checkSelfPermission;
        try {
            checkSelfPermission = ActivityCompat.checkSelfPermission(cox, Manifest.permission.CAMERA);
        } catch (RuntimeException e) {
            checkSelfPermission = PackageManager.PERMISSION_GRANTED;
        }
        if (checkSelfPermission == PackageManager.PERMISSION_DENIED) {
            return true;
        }
        return false;
    }
}
