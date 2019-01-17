package com.rent.zona.commponent.base;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rent.zona.commponent.R;

/**
 * 作者：刘路遥2018/5/18.
 * QQ：443915626
 * 版本：v1.0
 * 此类介绍：暂无
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {
    View convertView;
    Context context;

    public BaseViewHolder(View itemView, Context context) {
        super(itemView);
        this.convertView = itemView;
        this.context = context;
    }

    public void setText(int id, String text) {
        TextView tx = (TextView) convertView.findViewById(id);
        tx.setText(text);
    }
    public void setTextColor(int id, int color) {
        TextView tx = (TextView) convertView.findViewById(id);
        tx.setTextColor(color);
    }
    public void setImageResource(int id, int resouceId) {
        ImageView img= (ImageView) convertView.findViewById(id);
        img.setImageResource(resouceId);
    }
    public void setGlideImageView(int id, String resouceId) {
        ImageView img= (ImageView) convertView.findViewById(id);
        Glide.with(context).load(resouceId)
                .into(img);
    }
    public void setTextVisOrGone(int id, boolean isVisibility) {
        TextView tx = (TextView) convertView.findViewById(id);
        if (isVisibility){
            tx.setVisibility(View.VISIBLE);
        }else {
            tx.setVisibility(View.GONE);
        }
    }
    public View getView() {
        return convertView;
    }

}