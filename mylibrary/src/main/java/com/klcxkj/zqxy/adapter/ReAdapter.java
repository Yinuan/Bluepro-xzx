package com.klcxkj.zqxy.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.klcxkj.mylibrary.R;
import com.klcxkj.zqxy.databean.DeciveType;

/**
 * autor:OFFICE-ADMIN
 * time:2018/7/24
 * email:yinjuan@klcxkj.com
 * description:
 */

public class ReAdapter extends MyAdapter<DeciveType> {


    /**
     * 构造方法描述:基类构造方法
     *
     * @param mContext
     */
    public ReAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView =View.inflate(mContext, R.layout.package_item,null);
        }
        TextView textView =convertView.findViewById(R.id.package_count_txt);
        LinearLayout layout =convertView.findViewById(R.id.current_package_layout);
        DeciveType value =getItem(position);
       if (!value.getDevname().equals("其他")){
           textView.setText(value.getDevname()+"元");
       }else {
           textView.setText(value.getDevname());
       }

        int type =value.getTypeid();

        switch (type){
            case 0:
                layout.setBackgroundResource(R.drawable.xcrount_lightgray_5);
                break;
            case 1:
                layout.setBackgroundResource(R.drawable.xcrount_blue_5);
                break;
        }
        return convertView;
    }
}
