package com.miqian.mq.net;

import android.content.Context;
import android.text.TextUtils;
import com.miqian.mq.entity.Meta;
import com.miqian.mq.utils.JsonUtil;
import com.miqian.mq.utils.MobileOS;
import com.miqian.mq.utils.UserUtil;
import java.util.List;


/**
 * Description:
 *
 * @author Jackie
 */

public class MyAsyncTask extends MultiVersionAsyncTask<Void, Void, String> {

    private Context mContext;
    private List<Param> mList;
    protected ICallback<String> callback;
    private String mUrl;

    public static final String NETWORK_ERROR = "您当前网络不可用";
    public static final String SERVER_ERROR = "服务端网络不通，请重试";

    public MyAsyncTask(Context context, String url, List<Param> list, ICallback<String> callback) {
        this.mContext = context;
        this.callback = callback;
        this.mUrl = url;
        this.mList = list;
    }

    @Override
    protected String doInBackground(Void... params) {
        if (MobileOS.getNetworkType(mContext) == -1) {
            return NETWORK_ERROR;
        }

        String httpString = HttpUtils.httpPostRequest(mContext, mUrl, mList);
        if (!TextUtils.isEmpty(httpString)) {
            return httpString;
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        try {
            if (result != null) {
                if (result.equals(NETWORK_ERROR)) {
                    callback.onFail(result);
                } else if (result.startsWith(SERVER_ERROR)) {
//                    Pref.saveString(Pref.SERVER_ERROR_CODE, result, mContext);
//                    TCAgent.onEvent(mContext, Pref.SERVER_ERROR_CODE, Pref.getString(Pref.USERID, mContext, Pref.VISITOR) + result);
                    callback.onFail(SERVER_ERROR);
                } else {
                    callback.onSucceed(result);
                    Meta response = JsonUtil.parseObject(result, Meta.class);
                    if (response.getCode().equals("999995")) {
                        UserUtil.clearUserInfo(mContext);
                    }
                }
            } else {
                callback.onFail("请求失败");
            }
        } catch (Exception e) {
          e.printStackTrace();
            callback.onFail("数据解析失败，请重试");
        }

        mContext = null;
        mList = null;
        mUrl = null;
        callback = null;
    }
}
