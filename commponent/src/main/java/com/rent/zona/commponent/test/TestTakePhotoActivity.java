package com.rent.zona.commponent.test;

import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.rent.zona.commponent.R;
import com.rent.zona.commponent.base.BaseActivity;
import com.rent.zona.commponent.dlg.ActionSheet;

import org.devio.takephoto.model.TResult;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * 想要调用拍照功能  intent中转入ExtraConstant.EXTRA_CAN_TAKE_PHOTO true 或者重写方法canTakePhoto 返回ture
 */
public class TestTakePhotoActivity extends BaseActivity {
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_takephoto);
        imageView= (ImageView) findViewById(R.id.image);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> items = new ArrayList<>();
                items.add("拍照");
                items.add("相册");
                ActionSheet as = new ActionSheet(TestTakePhotoActivity.this, null, items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which != ActionSheet.CANCEL_BUTTON_INDEX) {
                            switch (which) {
                                case 0:
                                    picByTake();
                                    break;
                                case 1:
                                    picBySelect(1);
                                    break;
                            }
                        }
                    }
                });
                as.show();
            }
        });
    }

    @Override
    protected boolean canTakePhoto() {
        return true;
    }

    /**
     * 注意takeCancel takeFail takeSuccess都是在异常线程中执行的  不能做更新ui的操作
     */
    @Override
    public void takeCancel() {
        super.takeCancel();
        //主线程更新ui
        executeUITask(Observable.just(""),r->{
            getActivityHelper().toast("您取消了操作",this);
        },null);
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);

        //主线程更新ui
        executeUITask(Observable.just(msg),r->{
            getActivityHelper().toast(r,this);
        },null);
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        System.out.println("成功了----------");
//        imageView.setImageBitmap(BitmapFactory.decodeFile(result.getImages().get(0).getCompressPath()));
//        executeUITask();
        //主线程更新ui
        executeUITask(Observable.just(result),r->{
            imageView.setImageBitmap(BitmapFactory.decodeFile(r.getImages().get(0).getCompressPath()));
        },null);
    }
}
