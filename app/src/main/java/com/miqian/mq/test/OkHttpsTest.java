package com.miqian.mq.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.miqian.mq.R;
import com.miqian.mq.entity.JpushInfo;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;

/**
 * Created by sunyong on 8/28/15.
 */
public class OkHttpsTest extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);
    }

    public void testHttps(View v) {

        HttpRequest.testHttp(new ICallback<JpushInfo>() {

            @Override
            public void onSucceed(JpushInfo result) {

            }

            @Override
            public void onFail(String error) {

            }
        }, "1232", "232");
    }

}
