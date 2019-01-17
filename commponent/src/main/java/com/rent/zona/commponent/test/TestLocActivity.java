package com.rent.zona.commponent.test;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Rational;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.rent.zona.baselib.log.LibLogger;
import com.rent.zona.commponent.R;
import com.rent.zona.commponent.base.BaseActivity;
import com.rent.zona.commponent.dlg.CommonDialog;
import com.rent.zona.commponent.views.AppTitleBar;
import com.rent.zona.commponent.views.OnBackStackListener;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

//And Permission https://github.com/yanzhenjie/AndPermission
//高德 http://lbs.amap.com/api/android-location-sdk/guide/utilities/permission
public class TestLocActivity extends BaseActivity implements AMapLocationListener {
    public AMapLocationClient mLocationClient = null;
    public AMapLocationClientOption mLocationOption = null;

    TextView locResult;
    Button locBtn;
    AppTitleBar appTitleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_loc);
        ButterKnife.bind(this);
        mLocationClient = new AMapLocationClient(getApplicationContext());
//设置定位回调监听
        mLocationClient.setLocationListener(this);
        mLocationOption = new AMapLocationClientOption();
        configLocOption();

        mLocationClient.setLocationOption(mLocationOption);
        //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
        mLocationClient.stopLocation();
//        mLocationClient.startLocation();

        locResult= (TextView) findViewById(R.id.loc_result);
        locBtn= (Button) findViewById(R.id.loc_btn);
        appTitleBar= (AppTitleBar) findViewById(R.id.titlebar);
        appTitleBar.setTitle("定位测试");
        appTitleBar.setBackListener(new OnBackStackListener() {
            @Override
            public boolean onBackStack() {
                finish();
                return false;
            }
        });
        locBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndPermission.with(TestLocActivity.this)
                        .permission(
                                Permission.ACCESS_COARSE_LOCATION,
                                Permission.ACCESS_FINE_LOCATION,
                                Permission.WRITE_EXTERNAL_STORAGE,
                                Permission.READ_EXTERNAL_STORAGE,
                                Permission.READ_PHONE_STATE
                        )
                        .onGranted(new Action() {

                            @Override
                            public void onAction(List<String> permissions) {
                                LibLogger.di("权限","同意");
                                startLoc();
                            }
                        }).onDenied(new Action(){
                    @Override
                    public void onAction(List<String> permissions) {
                        LibLogger.di("权限","拒绝");
                        if (AndPermission.hasAlwaysDeniedPermission(TestLocActivity.this, permissions)) {
                            // 这里使用一个Dialog展示没有这些权限应用程序无法继续运行，询问用户是否去设置中授权。
                        CommonDialog dialog=new CommonDialog(TestLocActivity.this);
                        dialog.setTitle("温馨提示");
                        dialog.setMessage("没有权限 "+TextUtils.join(",\n", permissions)+"\n 无法继续运行");
                        dialog.setOkBtn("去设置", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // 如果用户继续：
                                AndPermission.permissionSetting(TestLocActivity.this).execute();
                            }
                        });
//                        dialog.setCancelBtn("算了", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                // 如果用户中断：
//                                executor.cancel();
//                            }
//                        });
                        dialog.show();
                        }
                    }
                }).rationale(new Rationale() {
                    @Override
                    public void showRationale(Context context, List<String> permissions, RequestExecutor executor) {
                        LibLogger.di("权限","showRationale");
// 这里使用一个Dialog询问用户是否继续授权。
                        List<String> permissionNames = Permission.transformText(context, permissions);
                        CommonDialog dialog=new CommonDialog(TestLocActivity.this);
                        dialog.setTitle("温馨提示");
                        dialog.setMessage("需要权限 "+TextUtils.join(",\n", permissionNames));
                        dialog.setOkBtn("继续", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // 如果用户继续：
                                executor.execute();
                            }
                        });

                        dialog.setCancelBtn("算了", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // 如果用户中断：
                                executor.cancel();
                            }
                        });
                        dialog.show();
                    }
                }).start();
            }
        });
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            String result="";
            if (amapLocation.getErrorCode() == 0) {
                //可在其中解析amapLocation获取相应内容。
                result+="\ngetLocationType  "+amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                result+="\ngetLatitude  "+amapLocation.getLatitude();//获取纬度
                result+="\ngetLongitude  "+amapLocation.getLongitude();//获取经度
                result+="\ngetAccuracy  "+amapLocation.getAccuracy();//获取精度信息
                result+="\ngetAddress  "+amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                result+="\ngetCountry  "+amapLocation.getCountry();//国家信息
                result+="\ngetProvince  "+amapLocation.getProvince();//省信息
                result+="\ngetCity  "+ amapLocation.getCity();//城市信息
                result+="\ngetDistrict  "+amapLocation.getDistrict();//城区信息
                result+="\ngetStreet  "+amapLocation.getStreet();//街道信息
                result+="\ngetStreetNum  "+amapLocation.getStreetNum();//街道门牌号信息
                result+="\ngetCityCode  "+ amapLocation.getCityCode();//城市编码
                result+="\ngetAdCode  "+amapLocation.getAdCode();//地区编码
                result+="\ngetAoiName  "+amapLocation.getAoiName();//获取当前定位点的AOI信息
                result+="\ngetBuildingId  "+amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
                result+="\ngetFloor  "+amapLocation.getFloor();//获取当前室内定位的楼层
                result+="\ngetGpsAccuracyStatus  "+amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
                //获取定位时间
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);
                result+="\ngetTime  "+date.toString();//获取GPS的当前状态
            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
                result= "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo();
            }
            locResult.setText(result);
        }
    }

    private void startLoc() {
//启动定位
        mLocationClient.startLocation();
    }

    private void stopLoc() {
        mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
    }

    private void configLocOption() {
        /**
         * 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
         * 说明：该部分功能从定位SDK v3.7.0开始提供。如果开发者选择了对应的定位场景，那么则不用自行设置AMapLocationClientOption中的其他参数，SDK会根据选择的场景自行定制option参数的值，当然开发者也可以在基础上进行自行设置。实际按最后一次设置的参数值生效。
         */
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        /**
         * 设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。AMapLocatioBattery_Saving低功耗模式nMode.。
         *  Hight_Accuracy 高精度定位模式：会同时使用网络定位和GPS定位，优先返回最高精度的定位结果，以及对应的地址描述信息。
         * Battery_Saving低功耗定位模式：不会使用GPS和其他传感器，只会使用网络定位（Wi-Fi和基站定位）；
         * Device_Sensors 仅用设备定位模式：不需要连接网络，只使用GPS进行定位，这种模式下不支持室内环境的定位，自 v2.9.0 版本支持返回地址描述信息。
         */
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        //获取一次定位结果：
////该方法默认为false。
//        mLocationOption.setOnceLocation(true);
//        //获取最近3s内精度最高的一次定位结果：
////设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
//        mLocationOption.setOnceLocationLatest(true);
//        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。 SDK默认采用连续定位模式，时间间隔2000ms。如果您需要自定义调用间隔：
//        mLocationOption.setInterval(1000);
//        //设置是否返回地址信息（默认返回地址信息）
//        mLocationOption.setNeedAddress(true);
//        //设置是否允许模拟位置,默认为true，允许模拟位置 设置是否允许模拟软件Mock位置结果，多为模拟GPS定位结果，默认为true，允许模拟位置。
//        mLocationOption.setMockEnable(true);
//        //注意：自 V3.1.0 版本之后setHttpTimeOut(long httpTimeOut)方法不仅会限制低功耗定位、高精度定位两种模式的定位超时时间，同样会作用在仅设备定位时。如果单次定位发生超时情况，定位随即终止；连续定位状态下当前这一次定位会返回超时，但按照既定周期的定位请求会继续发起。
//        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
//        mLocationOption.setHttpTimeOut(20000);
//        //关闭缓存机制 缓存机制默认开启，可以通过以下接口进行关闭。
//        //当开启定位缓存功能，在高精度模式和低功耗模式下进行的网络定位结果均会生成本地缓存，不区分单次定位还是连续定位。GPS定位结果不会被缓存。
//        mLocationOption.setLocationCacheEnable(false);
    }

    @Override
    protected void onDestroy() {
        mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
        super.onDestroy();
    }
    /** http://lbs.amap.com/api/android-location-sdk/guide/utilities/permission/
     * ● 目前手机设备在长时间黑屏或锁屏时CPU会休眠，这导致定位SDK不能正常进行位置更新。若您有锁屏状态下获取位置的需求，您可以应用alarmManager实现1个可叫醒CPU的Timer，定时请求定位。
     ● 使用定位SDK务必要注册GPS和网络的使用权限。
     ● 在使用定位SDK时，请尽量保证网络畅通，如获取网络定位，地址信息等都需要设备可以正常接入网络。
     ● 定位SDK在国内返回高德类型坐标，海外定位将返回GPS坐标。
     ● V1.x版本定位SDK参考手册和错误码参考表可以点我获取。
     */
    /**
     *  定位类型 响应码
     0 定位失败 请通过AMapLocation.getErrorCode()方法获取错误码，并参考错误码对照表进行问题排查。
     1 GPS定位结果 通过设备GPS定位模块返回的定位结果，精度较高，在10米－100米左右
     2 前次定位结果 网络定位请求低于1秒、或两次定位之间设备位置变化非常小时返回，设备位移通过传感器感知。
     4  缓存定位结果 返回一段时间前设备在同样的位置缓存下来的网络定位结果
     5 Wifi定位结果 属于网络定位，定位精度相对基站定位会更好，定位精度较高，在5米－200米之间。
     6 基站定位结果 纯粹依赖移动、联通、电信等移动网络定位，定位精度在500米-5000米之间。
     8 离线定位结果
     */


    /**
     * 错误码对照表
     0 定位成功。 可以在定位回调里判断定位返回成功后再进行业务逻辑运算。
     1 一些重要参数为空，如context； 请对定位传递的参数进行非空判断。
     2 定位失败，由于仅扫描到单个wifi，且没有基站信息。 请重新尝试。
     3 获取到的请求参数为空，可能获取过程中出现异常。 请对所连接网络进行全面检查，请求可能被篡改。
     4 请求服务器过程中的异常，多为网络情况差，链路不通导致 请检查设备网络是否通畅，检查通过接口设置的网络访问超时时间，建议采用默认的30秒。
     5 请求被恶意劫持，定位结果解析失败。 您可以稍后再试，或检查网络链路是否存在异常。
     6 定位服务返回定位失败。 请获取errorDetail（通过getLocationDetail()方法获取）信息并参考定位常见问题进行解决。
     7 KEY鉴权失败。 请仔细检查key绑定的sha1值与apk签名sha1值是否对应，或通过高频问题查找相关解决办法。
     8 Android exception常规错误 请将errordetail（通过getLocationDetail()方法获取）信息通过工单系统反馈给我们。
     9 定位初始化时出现异常。 请重新启动定位。
     10 定位客户端启动失败。 请检查AndroidManifest.xml文件是否配置了APSService定位服务
     11 定位时的基站信息错误。 请检查是否安装SIM卡，设备很有可能连入了伪基站网络。
     12 缺少定位权限。 请在设备的设置中开启app的定位权限。
     13  定位失败，由于未获得WIFI列表和基站信息，且GPS当前不可用。 建议开启设备的WIFI模块，并将设备中插入一张可以正常工作的SIM卡，或者检查GPS是否开启；如果以上都内容都确认无误，请您检查App是否被授予定位权限。
     14  GPS 定位失败，由于设备当前 GPS 状态差。 建议持设备到相对开阔的露天场所再次尝试。
     15 定位结果被模拟导致定位失败 如果您希望位置被模拟，请通过setMockEnable(true);方法开启允许位置模拟
     16 当前POI检索条件、行政区划检索条件下，无可用地理围栏  建议调整检索条件后重新尝试，例如调整POI关键字，调整POI类型，调整周边搜区域，调整行政区关键字等。
     18 定位失败，由于手机WIFI功能被关闭同时设置为飞行模式 建议手机关闭飞行模式，并打开WIFI开关
     19 定位失败，由于手机没插sim卡且WIFI功能被关闭 建议手机插上sim卡，打开WIFI开关
     */
}
