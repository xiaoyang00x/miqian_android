package com.miqian.mq.net;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;
import com.squareup.okhttp.Callback;


/**
 * Created by Administrator on 2015/9/4.
 */
public class HttpUtils {

    public static void httpPostRequest(String url, List<Param> list, Callback callback) {
//        String string = null;
        final OkHttpClient client = new OkHttpClient();

        FormEncodingBuilder builder = new FormEncodingBuilder();
        if (list != null && list.size() > 0) {
            for (Param param : list) {
                builder.add(param.key, param.value);
            }
        }
        RequestBody requestBody = builder.build();

        final Request request = new Request.Builder().url(url).post(requestBody).addHeader("phoneId", "11").addHeader("partnerId", "sss").addHeader("sign", "2332").build();
        client.newCall(request).enqueue(callback);

//        client.newCall(request).enqueue(new Callback() {
//
//            @Override
//            public void onFailure(Request request, IOException e) {
////                Log.e("", "error == " +e.getMessage());
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Response response) throws IOException {
//                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
//
////                Headers responseHeaders = response.headers();
////                for (int i = 0; i < responseHeaders.size(); i++) {
////                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
////                }
////                Log.e("", "" + response.body().string());
//                System.out.println(response.body().string());
//            }
//        });


//
//        Response response;
//        try {
//            response = client.newCall(request).execute();
//            string = response.body().string();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return string;
    }
}
