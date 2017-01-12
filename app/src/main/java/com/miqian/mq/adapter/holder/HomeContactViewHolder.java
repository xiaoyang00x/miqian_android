package com.miqian.mq.adapter.holder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.entity.HomePageInfo;
import com.miqian.mq.utils.MobileDeviceUtil;
import com.miqian.mq.utils.Pref;
import com.miqian.mq.utils.UserUtil;

import java.util.HashMap;
import java.util.Map;

import cn.udesk.UdeskConst;
import cn.udesk.UdeskSDKManager;

/**
 * Created by guolei_wang on 16/5/24.
 * 首页客服电话
 */
public class HomeContactViewHolder extends HomeBaseViewHolder {
    private TextView tv_online;
    private Context mContext;

    public HomeContactViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        tv_online = (TextView) itemView.findViewById(R.id.tv_online);
    }

    @Override
    public void bindView(final HomePageInfo mData) {
        tv_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setUdeskUserInfo();
                UdeskSDKManager.getInstance().showRobotOrConversation(mContext);
            }
        });
    }

    /**
     * 设置 udesk 所需用户信息
     */
    private void setUdeskUserInfo() {

        Map<String, String> info = new HashMap<String, String>();
        String userId = "";
        if (UserUtil.hasLogin(mContext)) {
            userId = UserUtil.getUserId(mContext);
        } else {
            userId = MobileDeviceUtil.getInstance(mContext).getMobileImei();
        }
        if (TextUtils.isEmpty(userId)) {
            userId = MobileDeviceUtil.getInstance(mContext).getUUID();
        }

        info.put(UdeskConst.UdeskUserInfo.USER_SDK_TOKEN, userId);
        //以下注释的字段都是可选的字段， 有邮箱建议填写
        info.put(UdeskConst.UdeskUserInfo.NICK_NAME, Pref.getString(Pref.REAL_NAME, mContext, ""));
        info.put(UdeskConst.UdeskUserInfo.CELLPHONE, Pref.getString(Pref.TELEPHONE, mContext, MobileDeviceUtil.getInstance(mContext).getPhoneNum()));

        UdeskSDKManager.getInstance().setUserInfo(mContext, userId, info);

    }
}
