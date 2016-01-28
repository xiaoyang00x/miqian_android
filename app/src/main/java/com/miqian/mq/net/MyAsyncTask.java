package com.miqian.mq.net;

import android.content.Context;
import android.text.TextUtils;

import com.miqian.mq.entity.JpushInfo;
import com.miqian.mq.entity.Meta;
import com.miqian.mq.utils.ExtendOperationController;
import com.miqian.mq.utils.JsonUtil;
import com.miqian.mq.utils.LogUtil;
import com.miqian.mq.utils.MobileOS;
import com.umeng.update.UmengUpdateAgent;

import java.util.List;


/**
 * Description:
 *
 * @author Jackie
 */

public class MyAsyncTask extends MultiVersionAsyncTask<Void, Void, String> {
    private final String TAG = MyAsyncTask.class.getSimpleName();
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

        LogUtil.d(TAG, "----请求服务器----" + mUrl);
        String httpString = HttpUtils.httpPostRequest(mContext, mUrl, mList);
        if (!TextUtils.isEmpty(httpString)) {
            LogUtil.e("", ": " + httpString);
            return httpString;
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        try {
            if (result != null) {
                LogUtil.d(TAG, "----服务器返回数据----" + result);
                if (result.equals(NETWORK_ERROR)) {
                    callback.onFail(result);
                } else if (result.startsWith(SERVER_ERROR)) {
//                    Pref.saveString(Pref.SERVER_ERROR_CODE, result, mContext);
//                    TCAgent.onEvent(mContext, Pref.SERVER_ERROR_CODE, Pref.getString(Pref.USERID, mContext, Pref.VISITOR) + result);
                    callback.onFail(SERVER_ERROR);
                } else {
                    Meta response = JsonUtil.parseObject(result, Meta.class);
                    if (response.getCode().equals("999995")) {//token失效
                        JpushInfo jpushInfo = new JpushInfo();
                        jpushInfo.setContent(response.getMessage());//此处套用极光的类 ，统一方法调用
                        ExtendOperationController.getInstance().doNotificationExtendOperation(ExtendOperationController.OperationKey.CHANGE_TOKEN, jpushInfo);
                        callback.onFail("");
                        return;
                    } else if (response.getCode().equals("900000")) {
                        UmengUpdateAgent.forceUpdate(mContext);
                    }
                    callback.onSucceed(result);
                }
            } else {
                callback.onFail("数据异常，请联系客服");
            }
        } catch (Exception e) {
            e.printStackTrace();
            callback.onFail("数据异常，请联系客服");
        }

        mContext = null;
        mList = null;
        mUrl = null;
        callback = null;
    }
}
