package com.miqian.mq.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.entity.HomeBulletinData;
import com.miqian.mq.entity.HomePageInfo;

/**
 * Created by guolei_wang on 16/5/24.
 * 首页公告
 */
public class HomeBulletinHolder extends HomeBaseViewHolder {
    private ImageView img_bulletin;
    private TextView tv_content;

    public HomeBulletinHolder(View itemView) {
        super(itemView);

        img_bulletin = (ImageView) itemView.findViewById(R.id.img_bulletin);
        tv_content = (TextView) itemView.findViewById(R.id.tv_content);
    }

    @Override
    public void bindView(HomePageInfo mData) {
        if(mData.getBsPushData() != null && !TextUtils.isEmpty(mData.getBsPushData().getTitle())) {
            tv_content.setText(mData.getBsPushData().getTitle());
        }
    }
}
