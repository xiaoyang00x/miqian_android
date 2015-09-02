package com.miqian.mq.test;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.miqian.mq.R;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.IOException;
import java.util.List;

/**
 * Created by sunyong on 8/28/15.
 */
public class OkHttpsTest extends Activity {
    private static String ENDPOINT = "https://api.github.com/repos/square/okhttp/contributors";
//    private static final Gson GSON = new Gson();
    private static final TypeToken<List<Contributor>> CONTRIBUTORS = new TypeToken<List<Contributor>>() {
    };

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);

        ENDPOINT = "https://api.shicaidai.com/commonService/getBankList.do";
        ENDPOINT = "https://api.shicaidai.com/userService/login.do";
        ENDPOINT = "https://api.shicaidai.com/commonService/getBankList.do";
        //ENDPOINT = "https://192.168.1.223/commonService/getBankList.do";
    }

    public void testHttps(View v) {
        final OkHttpClient client = new OkHttpClient();
        // Create request for remote resource.
        RequestBody formBody = new FormEncodingBuilder()
//                .add("mobilePhone", "18555")
//                .add("password", "password")
                .add("", "")
                .build();

        final Request request = new Request.Builder().url(ENDPOINT).post(formBody).addHeader("phoneId", "11").addHeader("partnerId", "sss").addHeader("sign", "2332").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("", "error == " +e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                Headers responseHeaders = response.headers();
                for (int i = 0; i < responseHeaders.size(); i++) {
                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }
                Log.e("", "" + response.body().string());
                System.out.println(response.body().string());
            }
        });
    }

    static class Contributor {
        String login;
        int contributions;
    }

}
