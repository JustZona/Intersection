package com.rent.zona.commponent.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.rent.zona.baselib.configs.FilePathConfig;
import com.rent.zona.baselib.glide.GlideImageLoader;
import com.rent.zona.baselib.glide.GlidePhotoImageView;
import com.rent.zona.baselib.glide.progress.CircleProgressView;
import com.rent.zona.baselib.glide.progress.OnGlideImageViewListener;
import com.rent.zona.baselib.network.httpbean.TaskException;
import com.rent.zona.baselib.utils.FileHelper;
import com.rent.zona.commponent.R;
import com.rent.zona.commponent.base.BaseActivity;
import com.rent.zona.commponent.dlg.ActionSheet;
import com.rent.zona.commponent.helper.DialogHelper;
import com.rent.zona.commponent.ui.bean.PicBean;
import com.rent.zona.commponent.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * 查看图片列表  pics ArrayList<PicBean> 或ArrayList<String>
 */
public class LookPicActivity extends BaseActivity implements ViewPager.OnPageChangeListener,View.OnClickListener{
    private static final String EXTRA_PIC_URIS = "extra.pic_uris";
    private static final String EXTRA_SHOW_INDEX="extra.show_index";
    private ViewPager picVP;
    private TextView picIndexTv;
    private ArrayList<Object> pics = new ArrayList<>();
    //    private PicAdapter mAdapter;
    private int curIndex=0;
    PicPagerAdapter mAdapter;

    /**
     *
     * @param context
     * @param showIndex 从 0 开始
     * @param pics
     */
    public static void launch(Context context, int showIndex, PicBean... pics) {
        if (pics != null && pics.length > 0) {
            ArrayList<PicBean> list = new ArrayList<>();
            list.addAll(Arrays.asList(pics));
            Intent intent = new Intent(context, LookPicActivity.class);
            intent.putExtra(EXTRA_PIC_URIS, list);
            intent.putExtra(EXTRA_SHOW_INDEX,showIndex);
            context.startActivity(intent);
        } else {
            DialogHelper.toast("图片为空", Toast.LENGTH_SHORT, context);
        }
    }
    public static void launch(Context context, int showIndex, List<Object> urls) {
        launch(context,showIndex, (Object[])urls.toArray(new Object[0]));
    }
    public static void launch(Context context, int showIndex, Object... pics) {
        if (pics != null && pics.length > 0) {
            ArrayList<String> list = new ArrayList<>();
            if(pics!=null){
                for(Object pic:pics){
                    list.add(pic.toString());
                }
            }
            Intent intent = new Intent(context, LookPicActivity.class);
            intent.putExtra(EXTRA_PIC_URIS, list);
            intent.putExtra(EXTRA_SHOW_INDEX,showIndex);
            context.startActivity(intent);
        } else {
            DialogHelper.toast("图片为空", Toast.LENGTH_SHORT, context);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_pic);
        if (getIntent().getStringArrayListExtra(EXTRA_PIC_URIS) != null) {
            pics.addAll(getIntent().getStringArrayListExtra(EXTRA_PIC_URIS));
        } else {
            pics.addAll(getIntent().getParcelableArrayListExtra(EXTRA_PIC_URIS));
        }
        curIndex=getIntent().getIntExtra(EXTRA_SHOW_INDEX,0);
        picVP= (ViewPager) findViewById(R.id.viewpager);
        picIndexTv = (TextView) findViewById(R.id.pic_index);
        mAdapter=new PicPagerAdapter();
        picVP.setAdapter(mAdapter);
        picVP.addOnPageChangeListener(this);
        picIndexTv.setText((curIndex+1)+"/"+pics.size());
        picVP.setCurrentItem(curIndex);
    }
    @Override
    public void setStatusBar() {
        StatusBarUtil.transparencyBar(this);
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        curIndex=position;
        picIndexTv.setText((curIndex+1)+"/"+pics.size());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        finish();
    }

    /*
        圆角矩形
        image13.setRadius(15);
        image13.setBorderWidth(3);
        image13.setBorderColor(R.color.blue);
        image13.setPressedAlpha(0.3f);
        image13.setPressedColor(R.color.blue);
        image13.loadImage(url1, R.color.placeholder_color);
        // 圆形
        image14.setShapeType(ShapeImageView.ShapeType.CIRCLE);
        image14.setBorderWidth(3);
        image14.setBorderColor(R.color.orange);
        image14.setPressedAlpha(0.2f);
        image14.setPressedColor(R.color.orange);
        image14.loadImage(url1, R.color.placeho/lder_color);
        */
    class PicPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return pics.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            View itemView=View.inflate(LookPicActivity.this, R.layout.item_activity_look_pic, null);

            container.addView(itemView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            GlidePhotoImageView glideImageView = itemView.findViewById(R.id.glideImageView);
            CircleProgressView progressView = itemView.findViewById(R.id.progressView);
            loadImage(glideImageView, progressView, pics.get(position));
            itemView.setOnClickListener(LookPicActivity.this);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    List<String> items = new ArrayList<>();
                    items.add("保存图片到手机");
                    ActionSheet as = new ActionSheet(LookPicActivity.this, null, items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which != ActionSheet.CANCEL_BUTTON_INDEX) {
                                switch (which) {
                                    case 0:
                                        saveImage(pics.get(position).toString());

                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    });
                    as.show();
                    return false;
                }
            });
            return itemView;
        }

        private void saveImage(String url) {
            Glide.with(LookPicActivity.this).asBitmap().load(url).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    showProgress("");
                    executeBkgTask(Observable.create(new ObservableOnSubscribe<String>() {
                        @Override
                        public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                            if (FileHelper.saveBitmapToFile(resource, FilePathConfig.getImageCacheDir(LookPicActivity.this) +FileHelper.getFileNameByUrl(url) + ".jpg")) {
                                emitter.onNext("保存成功");
                            } else {
                                emitter.onError(new TaskException("保存失败", "保存失败"));
                            }

                        }
                    }), R -> {
                        dismissProgress();
                        getActivityHelper().toast(R, LookPicActivity.this);
                    }, null);
                }
            });
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }
    /**
     * @param glideImageView
     * @param progressView
     * @param imageBean      对象com.rent.qft.commponent.ui.beanPicBean  或 String
     */
    private void loadImage(GlidePhotoImageView glideImageView, CircleProgressView progressView, Object imageBean) {
        if (imageBean == null || TextUtils.isEmpty(imageBean.toString())) {
            return;
        }
        GlideImageLoader imageLoader = glideImageView.getImageLoader();
        RequestOptions requestOptions = imageLoader.requestOptions(R.color.black)
                .centerInside();
        RequestOptions requestOptionsWithoutCache = imageLoader.requestOptions(R.color.black)
                .centerInside();
//                    .skipMemoryCache(true)
//                    .diskCacheStrategy(DiskCacheStrategy.NONE);


        imageLoader.setOnGlideImageViewListener(imageBean.toString(), new OnGlideImageViewListener() {
            @Override
            public void onProgress(int percent, boolean isDone, GlideException exception) {
                if (exception != null && !TextUtils.isEmpty(exception.getMessage())) {
                    Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                }
                progressView.setProgress(percent);
                progressView.setVisibility(isDone ? View.GONE : View.VISIBLE);
            }
        });
        RequestBuilder requestBuilder = imageLoader.requestBuilder(imageBean.toString(), requestOptionsWithoutCache)
                .transition(DrawableTransitionOptions.withCrossFade());
        if (imageBean instanceof PicBean && !TextUtils.isEmpty(((PicBean) imageBean).getImageUrlThumbnail())) {
            requestBuilder = requestBuilder.thumbnail(Glide.with(LookPicActivity.this)
                    .load(((PicBean) imageBean).getImageUrlThumbnail())
                    .apply(requestOptions));
        }

        requestBuilder
                .into(glideImageView);
    }

}
