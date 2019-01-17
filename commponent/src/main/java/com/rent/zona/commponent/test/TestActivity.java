package com.rent.zona.commponent.test;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.rent.zona.baselib.event.EventBus;
import com.rent.zona.baselib.glide.GlideImageLoader;
import com.rent.zona.baselib.glide.GlideImageView;
import com.rent.zona.baselib.glide.progress.CircleProgressView;
import com.rent.zona.baselib.glide.progress.OnGlideImageViewListener;
import com.rent.zona.baselib.log.LibLogger;
import com.rent.zona.baselib.network.HttpClient;
import com.rent.zona.baselib.network.encrypt.RSAUtil;
import com.rent.zona.baselib.network.httpbean.TResponse;
import com.rent.zona.baselib.rx.ObservableHelper;
import com.rent.zona.baselib.rx.OkHttpRxCall;
import com.rent.zona.baselib.utils.DateTimeUtils;
import com.rent.zona.baselib.utils.DeviceUtil;
import com.rent.zona.baselib.utils.GsonFactory;
import com.rent.zona.baselib.utils.MD5;
import com.rent.zona.commponent.R;
import com.rent.zona.commponent.base.BaseActivity;
import com.rent.zona.commponent.dlg.ActionSheet;
import com.rent.zona.commponent.dlg.CommonDialog;
import com.rent.zona.commponent.helper.ActivityUIHelper;
import com.rent.zona.commponent.pickerwheel.CommonWheel;
import com.rent.zona.commponent.pickerwheel.TimePickerDialog;
import com.rent.zona.commponent.pickerwheel.bean.AbstractPickerBean;
import com.rent.zona.commponent.pickerwheel.data.Type;
import com.rent.zona.commponent.pickerwheel.listener.OnDateSetListener;
import com.rent.zona.commponent.pickerwheel.wheel.CommonPickerDialog;
import com.rent.zona.commponent.test.bean.DemoPickerBean;
import com.rent.zona.commponent.test.event.AEvent;
import com.rent.zona.commponent.test.net.TestServiceFactory;
import com.rent.zona.commponent.ui.LookPicActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TestActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "TestActivity";
    public static void launch(Context context){
        context.startActivity(new Intent(context,TestActivity.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_test);
        findViewById(R.id.net_request).setOnClickListener(this);
        findViewById(R.id.upload_pic).setOnClickListener(this);
        findViewById(R.id.upload_pic_param).setOnClickListener(this);
        findViewById(R.id.download).setOnClickListener(this);
        findViewById(R.id.test_eventbus).setOnClickListener(this);
        findViewById(R.id.btn_dialog).setOnClickListener(this);
        findViewById(R.id.btn_action_sheet).setOnClickListener(this);
        findViewById(R.id.time_picker).setOnClickListener(this);
        findViewById(R.id.city_picker).setOnClickListener(this);
        findViewById(R.id.picker).setOnClickListener(this);
        findViewById(R.id.test1).setOnClickListener(this);
        findViewById(R.id.test2).setOnClickListener(this);
        findViewById(R.id.test3).setOnClickListener(this);
        findViewById(R.id.test4).setOnClickListener(this);
        findViewById(R.id.take_photo).setOnClickListener(this);
        findViewById(R.id.look_pic).setOnClickListener(this);
        findViewById(R.id.encrypt_decrypt1).setOnClickListener(this);
        findViewById(R.id.encrypt_decrypt2).setOnClickListener(this);
        findViewById(R.id.loc).setOnClickListener(this);
        findViewById(R.id.share).setOnClickListener(this);
        //subscribeEvent 两种写法
//        subscribeEvent(AEvent.class, e -> LibLogger.i(TAG, "AEvent: " + e.nameA));
        subscribeEvent(AEvent.class,this::aevent);



        loadImage();
    }
    private void loadImage() {
        GlideImageView glideImageView = (GlideImageView) findViewById(R.id.glideImageView);
        CircleProgressView progressView1 = (CircleProgressView) findViewById(R.id.progressView1);
        View maskView = findViewById(R.id.maskView);
        String cat = "https://raw.githubusercontent.com/sfsheng0322/GlideImageView/master/screenshot/cat.jpg";
        String cat_thumbnail = "https://raw.githubusercontent.com/sfsheng0322/GlideImageView/master/screenshot/cat_thumbnail.jpg";
        glideImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.finishAfterTransition(TestActivity.this);
            }
        });

        RequestOptions requestOptions = glideImageView.requestOptions(R.color.black)
                .centerCrop();
        RequestOptions requestOptionsWithoutCache = glideImageView.requestOptions(R.color.black)
                .centerCrop()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE);

        GlideImageLoader imageLoader = glideImageView.getImageLoader();

        imageLoader.setOnGlideImageViewListener(cat, new OnGlideImageViewListener() {
            @Override
            public void onProgress(int percent, boolean isDone, GlideException exception) {
                if (exception != null && !TextUtils.isEmpty(exception.getMessage())) {
                    Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                }
                progressView1.setProgress(percent);
                progressView1.setVisibility(isDone ? View.GONE : View.VISIBLE);
                maskView.setVisibility(isDone ? View.GONE : View.VISIBLE);
            }
        });

        imageLoader.requestBuilder(cat, requestOptionsWithoutCache)
                .thumbnail(Glide.with(TestActivity.this)
                        .load(cat_thumbnail)
                        .apply(requestOptions))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(glideImageView);
    }
     private void aevent(AEvent event){
         LibLogger.i(TAG, "AEvent: " + event.getNameA());
     }
    @Override
    public void onClick(View v) {
        int id=v.getId();
        if(id==R.id.net_request){


//            showProgress("登陆中");
//            sendRequest(CommonServiceFactory.getInstance().commonService().register("13896041111","123456", DeviceUtil.getUUID(this),"android"), r->{
//                dismissProgress();
//                getActivityHelper().toast("注册成功",TestActivity.this);
//            });
            return;
        }
        if(id==R.id.upload_pic){
            uploadFile();//uploadFile1();
            return;
        }
        if(id==R.id.upload_pic_param){
            uploadFileAndParam();
            return;
        }
        if(id==R.id.download){
            downLoad();
            return;
        }
        if(id==R.id.test_eventbus){
            postEvent();
            return;
        }
        if(id==R.id.btn_dialog){
            showCommonDlg();
            return;
        }
        if(id==R.id.btn_action_sheet){
            showActionSheet();
            return;
        }
        if(id==R.id.time_picker){
            showTimePicker();
            return;
        }
        if(id==R.id.city_picker){
            showCityPicker();
            return;
        }
        if(id==R.id.picker){
            showPicker();
            return;
        }
        if(id==R.id.test1){
            TestPullRefreshFragment.launch(this);
            return;
        }
        if(id==R.id.test2){
            startActivity(new Intent(this, TestPullRefreshActivity.class));
            return;
        }
        if(id==R.id.test3){
            TestPullUpDownRefreshFragment.launch(this);
            return;
        }
        if(id==R.id.test4){
            startActivity(new Intent(this, TestPullUpDownRefreshActivity.class));
            return;
        }
        if(id==R.id.take_photo){
            startActivity(new Intent(this, TestTakePhotoActivity.class));
            return;
        }
        if(id==R.id.look_pic){
            lookPic();
            return;
        }
        if(id==R.id.encrypt_decrypt1){
            //需要加密的数据
            String clearText01 = "大家好";
            //公钥加密结果
            String publicEncryptedResult = RSAUtil.encryptDataByPublicKey(clearText01);
            //私钥解密结果
            String privateDecryptedResult = RSAUtil.decryptedToStrByPrivate(publicEncryptedResult);
            getActivityHelper().toast(privateDecryptedResult,this);
            return;
        }
        if(id==R.id.encrypt_decrypt2){
            String clearText02 = "希望大家多多支持我的博客，不足之处还望斧正！";
            //私钥加密结果
            String privateEncryptedResult = RSAUtil.encryptDataByPrivateKey(clearText02);
            //公钥解密结果
            String publicDecryptedResult = RSAUtil.decryptedToStrByPublicKey(privateEncryptedResult);
            getActivityHelper().toast(publicDecryptedResult,this);
            return;
        }
        if(id==R.id.loc){
            startActivity(new Intent(this, TestLocActivity.class));
            return;
        }
        if(id==R.id.share){
            startActivity(new Intent(this, TestShareActivity.class));
            return;
        }
    }
    private void lookPic(){
       String cat = "https://raw.githubusercontent.com/sfsheng0322/GlideImageView/master/screenshot/cat.jpg";
       String cat_thumbnail = "https://raw.githubusercontent.com/sfsheng0322/GlideImageView/master/screenshot/cat_thumbnail.jpg";

       String girl = "https://raw.githubusercontent.com/sfsheng0322/GlideImageView/master/screenshot/girl.jpg";
       String girl_thumbnail = "https://raw.githubusercontent.com/sfsheng0322/GlideImageView/master/screenshot/girl_thumbnail.jpg";

        LookPicActivity.launch(this,1, cat,girl);
    }
    private void showPicker(){
        CommonPickerDialog<DemoPickerBean> mCommonPickerDialog = CommonPickerDialog.build(TestActivity.this);
        //设置数据源
        mCommonPickerDialog.setDataSource(test("城市"));

        //数据刷新,会刷新当前改变的列的后续级联数据,在这个回调中,给出新的显示数据
        //点"确定"的结果回调
        mCommonPickerDialog.setOnPickerResultListener(new CommonPickerDialog.OnPickerResultListener() {
            @Override
            public void onPickerResult(List resuilt) {
                LibLogger.d(TAG, "picker result:" + resuilt.get(0));
            }
        });
        mCommonPickerDialog.show();
    }
    private void showCityPicker(){
        CommonPickerDialog<DemoPickerBean> mCommonPickerDialog = CommonPickerDialog.build(TestActivity.this);
        //设置数据源
        mCommonPickerDialog.setDataSource(test("城市"), test("城区"), test("小区"));

        //数据刷新,会刷新当前改变的列的后续级联数据,在这个回调中,给出新的显示数据
//        mCommonPickerDialog.setOnUpdateNextDataListener(new CommonWheel.OnUpdateNextDataListener() {
//
//            @Override
//            public List updateNextData(AbstractPickerBean changeT, List nowData) {
//                if (changeT != null) {
//                    ProvinceBean  provinceBean= (ProvinceBean) changeT;
//                    if(provinceBean.getCitys()!=null && !provinceBean.getCitys().isEmpty()){
//                        return provinceBean.getCitys();
//                    }else{
//                        return provinceBean.getDistrictlist();
//                    }
//                } else {
//                    return new ArrayList<ProvinceBean>();
//                }
//            }
//        });
        mCommonPickerDialog.setOnUpdateNextDataListener(new CommonWheel.OnUpdateNextDataListener() {
            @Override
            public List updateNextData(AbstractPickerBean changeT, List nowData) {//changeT 改变的对象  nowData变更后的值  有几个wheel nowData的size就有几个
                DemoPickerBean bean = (DemoPickerBean) nowData.get(0);
                if (bean.showContent().contains("城市")) {
                    return test("城区_1");
                } else if (bean.showContent().contains("城区")) {
                    return test("小区_1");
                }
                return test("小区_0");//变更后 联动更改挨着的wheel列表
            }
        });
        //点"确定"的结果回调
        mCommonPickerDialog.setOnPickerResultListener(new CommonPickerDialog.OnPickerResultListener() {
            @Override
            public void onPickerResult(List resuilt) {
                LibLogger.d(TAG, "picker result:" + resuilt);
            }
        });
        mCommonPickerDialog.show();
    }
    List<DemoPickerBean> test(String content) {
        List<DemoPickerBean> list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            DemoPickerBean bean = new DemoPickerBean(content + "_" + i);
            list.add(bean);
        }
        return list;
    }
 private void showTimePicker(){
     long tenYears = 10L * 365 * 1000 * 60 * 60 * 24L;
     TimePickerDialog mDialogAll = new TimePickerDialog.Builder(this)
             .setCallBack(new OnDateSetListener() {
                 @Override
                 public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                        String date= DateTimeUtils.getYMDChinaFormatInstance().format(new Date(millseconds));
                        getActivityHelper().toast(date,TestActivity.this);
                 }
             })
             .setCyclic(false)
             .setMinMillseconds(System.currentTimeMillis())
             .setMaxMillseconds(System.currentTimeMillis() + tenYears)
             .setCurrentMillseconds(System.currentTimeMillis())
             .setType(Type.ALL)//五种选择模式，年月日时分，年月日，时分，月日时分，年月
             .build();
     mDialogAll.show();
 }
    private void showActionSheet() {
        List<String> items = new ArrayList<>();
        items.add("item1");
        items.add("item2");
        items.add("item3");
        ActionSheet as = new ActionSheet(this, null, items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which != ActionSheet.CANCEL_BUTTON_INDEX) {
                    switch (which) {
                        case 0:
                        case 1:
                        case 2:
                            getActivityHelper().toast(items.get(which),TestActivity.this);
                            break;
                        default:
                            break;
                    }
                }
            }
        });
        as.show();
    }

    private void showCommonDlg(){
        CommonDialog dialog = new CommonDialog(this);
        dialog.setTitle("对话框标题");
        dialog.setMessage("对话框内容对话框内容对话框内容对话框内容对话框内容对话框内容");
        dialog.setOkBtn(R.string.ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        dialog.setCancelBtn(R.string.cancel, null);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });
        dialog.show();
    }
    private void postEvent(){
        EventBus.getDefault().post(new AEvent("你好  hello"));//发出事件
        //1首先发出事件
        //2接收事件 在继承BaseFragment/BaseActivity的子类的onViewCreated/onCreate 调用subscribeEvent 请看本类的示例
    }
    public void downLoad(){
        String url = "http://file.cctsl.cn/AppVer/Android";
        File dest = new File("/sdcard/tsl_" + System.currentTimeMillis() + ".apk");


        Observable<String> observable = HttpClient.download(url, dest);
        executeBkgTask(observable, path -> ActivityUIHelper.toast("path: " + path, this), null);

//        url = "http://www.baidu.com";
//        dest = new File("/sdcard/baidu_" + System.currentTimeMillis() + ".txt");
//
//        observable = HttpClient.download(url, dest);
//        executeBkgTask(observable, path -> ActivityUIHelper.toast("path: " + path, this), null);
    }
    public void uploadFileAndParam() {
        File file = new File("/sdcard/a.png");
        if (file.exists()) {
            RequestBody requestBody =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);
            Observable<TResponse<String>> observable =
                    TestServiceFactory.getInstance().testService().PhotoUpload(requestBody,"userid123456");
            sendRequest(observable, s -> LibLogger.i(TAG, "file url is: " + s));
        } else {
            Toast.makeText(this, "/sdcard/a.png is not exist", Toast.LENGTH_SHORT).show();
        }

    }
    public void uploadFile() {
        File file = new File("/sdcard/a.png");
        if (file.exists()) {
            RequestBody requestBody =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);
            Observable<TResponse<String>> observable =
                    TestServiceFactory.getInstance().testService().mendUpload(requestBody);
            sendRequest(observable, s -> LibLogger.i(TAG, "file url is: " + s));
        } else {
            Toast.makeText(this, "/sdcard/a.png is not exist", Toast.LENGTH_SHORT).show();
        }

    }
    public void uploadFile1() {
        File file = new File("/sdcard/a.png");
        if (file.exists()) {
            RequestBody requestBody=new MultipartBody.Builder("AaB03x")
                      .setType(MultipartBody.FORM)
                    .addFormDataPart("image", file.getName(), RequestBody.create(MediaType.parse("image/png"), file))//可多次 addFormDataPart
                    .build();

            Request request = new Request.Builder()
                    .url("http://file.cctsl.cn/FileUpload/MendUpload")
                    .post(requestBody)
                    .build();

            Observable<Response> observable = ObservableHelper.create(new OkHttpRxCall(request));
            executeBkgTask(observable, s -> {
                try {
                    LibLogger.i(TAG, "file url is: " + s.body().string());
                } catch (IOException e) {
                }
            }, null);
        } else {
            Toast.makeText(this, "/sdcard/a.png is not exist", Toast.LENGTH_SHORT).show();
        }
    }
}
