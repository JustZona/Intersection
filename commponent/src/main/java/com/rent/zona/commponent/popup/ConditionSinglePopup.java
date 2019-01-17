package com.rent.zona.commponent.popup;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.rent.zona.commponent.R;

import java.util.ArrayList;
import java.util.List;

public class ConditionSinglePopup extends PopupWindow {
    Context mcox;
    RecyclerView contentRv;
    View mContentView;
    ConditionAdapter mAdapter;
    CallBack mcallBack;

    public void setItemTextSize(int itemTextSize) {
        this.itemTextSize = itemTextSize;
    }

    int itemTextSize; //单位sp
    public interface CallBack{
        void result(ConditionBean conditionBean);
    }
    public ConditionSinglePopup(Context context, ArrayList<? super ConditionBean> dataList, CallBack callBack) {
        this(context,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        setDataSource(dataList);
        setCallBack(callBack);
    }
    public ConditionSinglePopup(Context context, int width, int height) {
        this(LayoutInflater.from(context).inflate(R.layout.popup_single_condition, null, false), width, height, false);
        this.mcox=context;
        itemTextSize=13;
        // 设置PopupWindow的背景
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置PopupWindow是否能响应外部点击事件
        setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        setTouchable(true);
        initView();
    }

    public ConditionSinglePopup(View contentView, int width, int height, boolean b) {
        super(contentView, width, height, false);
    }
    protected void initView(){
        mContentView=getContentView();
        contentRv=mContentView.findViewById(R.id.condition_rv);
        mAdapter=new ConditionAdapter();
        contentRv.setLayoutManager(new LinearLayoutManager(mcox));
        contentRv.setAdapter(mAdapter);

        //点击半透明地方  弹框消失
        mContentView.findViewById(R.id.blank).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        }); //点击半透明地方  弹框消失
        mContentView.findViewById(R.id.blank1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    public void setCallBack(CallBack callBack){
        mcallBack=callBack;
    }
    public void setDataSource(ArrayList<? super ConditionBean> dataList){
        if(dataList==null || dataList.isEmpty()){
            return;
        }
        ConditionBean selectCondition=setDefaultSelect(dataList);
        selectCondition.setSelected(false);//关闭默认选中
        mAdapter.setDataList(dataList);
    }
    private ConditionBean setDefaultSelect(ArrayList<? super ConditionBean> dataList){
        //找出选中的condition 同时防止有多个选中的condition 默认选中第一个
        ConditionBean selectCondition=null;
        for(int i=0;i<dataList.size();i++){
            ConditionBean bean= (ConditionBean) dataList.get(i);
            if(bean.isSelected()){
                selectCondition=bean;
                continue;
            }
            if(selectCondition!=null){
                bean.setSelected(false);//同时防止有多个选中的condition
            }
        }

        if(selectCondition==null){
            selectCondition= (ConditionBean) dataList.get(0);
            selectCondition.setSelected(true);
        }
        return selectCondition;
    }
    class ConditionAdapter extends RecyclerView.Adapter<ConditionAdapter.ViewHolder>{
        List<? super ConditionBean> beans=new ArrayList<>();
        public void setDataList(List<? super ConditionBean> list){
            beans.clear();
            if(list!=null && !list.isEmpty())
                for(int i=0;i<list.size();i++){
                    beans.add((ConditionBean)list.get(i));
                }
            notifyDataSetChanged();
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ConditionAdapter.ViewHolder(LayoutInflater.from(mcox).inflate(R.layout.item_popup_release,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull ConditionAdapter.ViewHolder holder, int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return beans.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            TextView conditionTv;
            ImageView rightIcon;
            ConditionBean bean;
            View itemView;
            public ViewHolder(View itemView) {
                super(itemView);
                this.itemView=itemView;
                conditionTv=itemView.findViewById(R.id.condition_tv);
                rightIcon=itemView.findViewById(R.id.right_icon);
            }
            public void bind(int position){
                bean= (ConditionBean) beans.get(position);
                conditionTv.setGravity(Gravity.CENTER);
                conditionTv.setTextSize(TypedValue.COMPLEX_UNIT_SP,itemTextSize);
                resetStyle(this);
                conditionTv.setText(bean.getShowCondition());
                rightIcon.setImageResource(bean.getImgId());
                itemView.setSelected(bean.isSelected());
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                //选中之后字体颜色改变
               /* if(!bean.isSelected()){
                    for(int i=0;i<beans.size();i++){
                        ConditionBean condition= (ConditionBean) beans.get(i);
                        condition.setSelected(condition==bean);
                    }
                    notifyDataSetChanged();
                }*/

                selectResult(bean);
            }
        }
    }
    private void selectResult(ConditionBean conditionBean ){
        if(mcallBack!=null ){
            mcallBack.result(conditionBean);
        }
        getContentView().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        },100);
    }
    protected void resetStyle(ConditionAdapter.ViewHolder viewHolder){

    }
}
