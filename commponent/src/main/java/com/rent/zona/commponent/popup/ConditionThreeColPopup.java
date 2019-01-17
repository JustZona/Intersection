package com.rent.zona.commponent.popup;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.rent.zona.commponent.Itemdecoration.UniversalItemDecoration;
import com.rent.zona.commponent.R;

import java.util.ArrayList;
import java.util.List;

public class ConditionThreeColPopup extends PopupWindow {
    Context mcox;
    RecyclerView oneRv,twoRv,threeRv;
    View mContentView;
    OneAdapter mOneAdapter;
    RightAdapter mTwoAdapter,mThreeAdapter;
    TextView titleTv;
    View blankView;
    View titleLineView;
    CallBack mcallBack;
    public interface CallBack{
        void result(ConditionBean oneBean, ConditionBean twoBean, ConditionBean threeBean);
    }
    public ConditionThreeColPopup(Context context, ArrayList<? super ConditionBean> dataList, CallBack callBack) {
        this(context,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        setDataSource(dataList);
        setCallBack(callBack);
    }
    public ConditionThreeColPopup(Context context, int width, int height) {
        this(LayoutInflater.from(context).inflate(R.layout.popup_three_col_condition, null, false), width,height, false);
        this.mcox=context;
        // 设置PopupWindow的背景
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置PopupWindow是否能响应外部点击事件
        setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        setTouchable(true);
        initView();
    }

    public ConditionThreeColPopup(View contentView, int width, int height, boolean b) {
        super(contentView, width, height, false);
    }
    private void initView(){
        mContentView=getContentView();
        titleTv=mContentView.findViewById(R.id.title);
        blankView=mContentView.findViewById(R.id.blank);
        titleLineView=mContentView.findViewById(R.id.title_line);
        oneRv=mContentView.findViewById(R.id.one_rv);
        twoRv=mContentView.findViewById(R.id.two_rv);
        threeRv=mContentView.findViewById(R.id.three_rv);
        mOneAdapter=new OneAdapter();
        mTwoAdapter=new RightAdapter(true);
        mThreeAdapter=new RightAdapter(false);
        oneRv.setLayoutManager(new LinearLayoutManager(mcox));
        twoRv.setLayoutManager(new LinearLayoutManager(mcox));
        threeRv.setLayoutManager(new LinearLayoutManager(mcox));
        oneRv.setAdapter(mOneAdapter);
        twoRv.setAdapter(mTwoAdapter);
        threeRv.setAdapter(mThreeAdapter);
        twoRv.addItemDecoration(new UniversalItemDecoration() {
            @Override
            public UniversalItemDecoration.Decoration getItemOffsets(int position) {
                ColorDecoration decoration = new ColorDecoration();
                decoration.bottom=2;
                decoration.decorationColor= ContextCompat.getColor(mcox, R.color.common_divider_color);
                return decoration;
            }
        });
        threeRv.addItemDecoration(new UniversalItemDecoration() {
            @Override
            public Decoration getItemOffsets(int position) {
                ColorDecoration decoration = new ColorDecoration();
                decoration.bottom=2;
                decoration.decorationColor= ContextCompat.getColor(mcox, R.color.common_divider_color);
                return decoration;
            }
        });
        //点击半透明地方  弹框消失
        mContentView.findViewById(R.id.blank).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    public void setDataSource(ArrayList<? super ConditionBean> dataList){
        if(dataList==null || dataList.isEmpty()){
            return;
        }
        ConditionBean selectCondition=setDefaultSelect(dataList);
        mOneAdapter.setDataList(dataList);

        if(selectCondition.getSubitems()!=null && !selectCondition.getSubitems().isEmpty()){
            ConditionBean twoSelectCondition=setDefaultSelect(selectCondition.getSubitems());
            mTwoAdapter.setDataList(selectCondition.getSubitems());
            if(twoSelectCondition.getSubitems()!=null && !twoSelectCondition.getSubitems().isEmpty()){
                ConditionBean threeSelectCondition=setDefaultSelect(twoSelectCondition.getSubitems());
                mThreeAdapter.setDataList(twoSelectCondition.getSubitems());
            }
        }

    }
    private ConditionBean setDefaultSelect(ArrayList<? super ConditionBean> dataList){
        if(dataList==null || dataList.isEmpty()){
            return null;
        }
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
    public void setCallBack(CallBack callBack){
        mcallBack=callBack;
    }
    class OneAdapter extends RecyclerView.Adapter<OneAdapter.ViewHolder>{
        ArrayList<? super ConditionBean> leftBeans=new ArrayList<>();
        public void setDataList(ArrayList<? super ConditionBean> list){
            leftBeans.clear();
            if(list!=null && !list.isEmpty())
                for(int i=0;i<list.size();i++){
                    leftBeans.add((ConditionBean)list.get(i));
                }
            notifyDataSetChanged();
        }
        @NonNull
        @Override
        public OneAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new OneAdapter.ViewHolder(LayoutInflater.from(mcox).inflate(R.layout.item_popup_mulcondition_left,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull OneAdapter.ViewHolder holder, int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return leftBeans.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            TextView conditionTv;
            View leftIcon;
            ImageView rightIcon;
            ConditionBean bean;
            View itemView;
            public ViewHolder(View itemView) {
                super(itemView);
                this.itemView=itemView;
                conditionTv=itemView.findViewById(R.id.condition_tv);
                rightIcon=itemView.findViewById(R.id.right_icon);
                leftIcon=itemView.findViewById(R.id.left_iv);
                leftIcon.setVisibility(View.VISIBLE);
            }
            public void bind(int position){
                bean= (ConditionBean) leftBeans.get(position);
                conditionTv.setText(bean.getShowCondition());
                itemView.setSelected(bean.isSelected());
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                if(!bean.isSelected()){
                    for(int i=0;i<leftBeans.size();i++){
                        ConditionBean condition= (ConditionBean) leftBeans.get(i);
                        condition.setSelected(condition==bean);
                    }
                    mTwoAdapter.setDataList(bean.getSubitems());

                    ConditionBean twoSelect=setDefaultSelect(bean.getSubitems());
                    if(twoSelect==null){
                        mThreeAdapter.setDataList(null);
                    }else{
                        setDefaultSelect(twoSelect.getSubitems());
                        mThreeAdapter.setDataList(twoSelect.getSubitems());
                    }
                    notifyDataSetChanged();
                }

                if(bean.getSubitems()==null || bean.getSubitems().isEmpty()){
                    selectResult(bean,null,null);
                    return;
                }
            }
        }

    }
    private void selectResult(ConditionBean oneBean , ConditionBean twoBean, ConditionBean threeBean){
        if(mcallBack!=null ){
            mcallBack.result(oneBean, twoBean,threeBean);
        }
        getContentView().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        },100);
    }
    class RightAdapter extends RecyclerView.Adapter<RightAdapter.ViewHolder>{
        boolean isTwoCol=true;//是否是第二列的数据 否则就是第三列

        public RightAdapter(boolean isTwoCol) {
            this.isTwoCol = isTwoCol;
        }

        List<? super ConditionBean> rightBeans=new ArrayList<>();
        public void setDataList(List<? super ConditionBean> list){
            rightBeans.clear();
            if(list!=null && !list.isEmpty())
                for(int i=0;i<list.size();i++){
                    rightBeans.add((ConditionBean)list.get(i));
                }
            notifyDataSetChanged();
        }
        @NonNull
        @Override
        public RightAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new RightAdapter.ViewHolder(LayoutInflater.from(mcox).inflate(R.layout.item_popup_mulcondition_right,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull RightAdapter.ViewHolder holder, int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return rightBeans.size();
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
                bean= (ConditionBean) rightBeans.get(position);
                conditionTv.setText(bean.getShowCondition());
                itemView.setSelected(bean.isSelected());
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                if(!bean.isSelected()){
                    for(int i=0;i<rightBeans.size();i++){
                        ConditionBean condition= (ConditionBean) rightBeans.get(i);
                        condition.setSelected(condition==bean);
                    }
                    notifyDataSetChanged();
                    if(isTwoCol){
                        setDefaultSelect(bean.getSubitems());
                       mThreeAdapter.setDataList(bean.getSubitems());
                    }
                }
                if(isTwoCol && (bean.getSubitems()==null || bean.getSubitems().isEmpty())
                        || !isTwoCol){
                    selectResult(getSelect(mOneAdapter.leftBeans),getSelect(mTwoAdapter.rightBeans),getSelect(mThreeAdapter.rightBeans));
                }
            }
        }
    }
    public ConditionBean getSelect(List<? super ConditionBean> list){
        if(list==null || list.isEmpty()){
            return null;
        }
        for(int i=0;i<list.size();i++){
            ConditionBean condition= (ConditionBean) list.get(i);
            if(condition.isSelected()){
                return condition;
            }
        }
        return null;
    }
    public void setTitle(String title){
        titleTv.setVisibility(View.VISIBLE);
        titleLineView.setVisibility(View.VISIBLE);
        blankView.setVisibility(View.GONE);
        titleTv.setText(title);
    }
}

