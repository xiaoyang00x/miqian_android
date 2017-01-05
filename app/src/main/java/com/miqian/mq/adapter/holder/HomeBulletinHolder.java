package com.miqian.mq.adapter.holder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.AnnounceResultActivity;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.activity.user.NoticeActivity;
import com.miqian.mq.entity.HomePageInfo;
import com.miqian.mq.entity.MessageInfo;
import com.miqian.mq.utils.Pref;
import com.umeng.analytics.MobclickAgent;
import com.umeng.onlineconfig.OnlineConfigAgent;

/**
 * Created by guolei_wang on 16/5/24.
 * 首页公告
 */
public class HomeBulletinHolder extends HomeBaseViewHolder implements View.OnClickListener {
    private ImageView img_bulletin;
    private TextView tv_content;
    private View layout_bulletin;
    private View view_qq;
    private View divider;
    private Context mContext;
    private HomePageInfo data;
    private boolean isQQCache = false;          //手Q缓存开关

    public HomeBulletinHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        img_bulletin = (ImageView) itemView.findViewById(R.id.img_bulletin);
        tv_content = (TextView) itemView.findViewById(R.id.tv_content);
        layout_bulletin = itemView.findViewById(R.id.layout_bulletin);
        divider = itemView.findViewById(R.id.divider);
        view_qq = itemView.findViewById(R.id.view_qq);
//        isQQCache = "YES".equals(OnlineConfigAgent.getInstance().getConfigParams(mContext, "Cache"));

    }

    @Override
    public void bindView(HomePageInfo mData) {
        data = mData;
        if (mData.getBsPushData() != null && !TextUtils.isEmpty(mData.getBsPushData().getTitle())) {
            tv_content.setText(mData.getBsPushData().getTitle());

            //本地公告时间戳
            long bulletinLocalTime = Pref.getLong(Pref.DATA_BULLETIN_TIME, mContext, 0);

            if (mData.getBsPushData().getSendTime() > bulletinLocalTime) {
                img_bulletin.setImageResource(isQQCache?R.drawable.icon_home_bulletin_new_qq:R.drawable.icon_home_bulletin_new);
            } else {
                img_bulletin.setImageResource(isQQCache?R.drawable.icon_home_bulletin_qq:R.drawable.icon_home_bulletin);
            }
            if(isQQCache) {
                tv_content.setTextColor(Color.parseColor("#fbd79d"));
                layout_bulletin.setBackgroundColor(Color.parseColor("#ce2811"));
                divider.setBackgroundColor(Color.parseColor("#ac210e"));
            }else {
                tv_content.setTextColor(mContext.getResources().getColor(R.color.mq_bl1_v2));
                layout_bulletin.setBackgroundColor(Color.WHITE);
                divider.setBackgroundColor(mContext.getResources().getColor(R.color.mq_b6_v2));
            }
            view_qq.setVisibility(isQQCache?View.VISIBLE:View.GONE);
            tv_content.setOnClickListener(this);
            img_bulletin.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_content://跳公告详情
               startDetail();
                break;
            case R.id.img_bulletin://跳列表页
                if(isQQCache) {
                    startDetail();
                }else {
                    MobclickAgent.onEvent(mContext, "1004_1");
                    mContext.startActivity(new Intent(mContext, NoticeActivity.class));
                    Pref.saveLong(Pref.DATA_BULLETIN_TIME, data.getBsPushData().getSendTime(), mContext);
                    img_bulletin.setImageResource(R.drawable.icon_home_bulletin);
                }
                break;
            default:
                break;
        }
    }

    private void startDetail() {
        MobclickAgent.onEvent(mContext, "1004_2");
        MessageInfo messageInfo = data.getBsPushData();
        int msgType = messageInfo.getMsgType();

        switch (msgType) {
            // 内置浏览器
            case 50:
            case 51:
            case 52:
            case 53:
                WebActivity.startActivity(mContext, messageInfo.getJumpUrl());
                break;
            default:
                Intent intent = new Intent(mContext, AnnounceResultActivity.class);
                intent.putExtra("id", messageInfo.getId());
                intent.putExtra("isMessage", false);
                mContext.startActivity(intent);
        }
        Pref.saveLong(Pref.DATA_BULLETIN_TIME, data.getBsPushData().getSendTime(), mContext);
        img_bulletin.setImageResource(isQQCache?R.drawable.icon_home_bulletin_qq:R.drawable.icon_home_bulletin);
        boolean isReaded = Pref.getBoolean(Pref.PUSH + messageInfo.getId(), mContext, false);
        if (!isReaded) {
            Pref.saveBoolean(Pref.PUSH + messageInfo.getId(), true, mContext);
        }
    }
}
