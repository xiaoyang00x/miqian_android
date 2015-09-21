package com.miqian.mq.utils.net;

import com.squareup.okhttp.Request;

/**
 * Created by sunyong on 9/15/15.
 */
public class CommonHeaders {

  public static Request.Builder configRequestBuilder(Request.Builder builder) {
    builder.header("name", "hkjhkfgtyhuijoklp;['fghyjuiklop;['hello world")
        .header("name1",
            "asdfghjkl;'rftgyhujikopxcvbnm,dfghjklcvbnmk,ltyuiodfghjklxcvbnm,sdfghjkxcvbnm,dfghjklhello world1")
        .header("name2", "fghjkl;ghjkl;dfghjkldfghjkl;hello world2")
        .header("name3", "ghjkl;ertyuiopdfghjkl;rtyuiopdfghjkl;hello world3")
        .header("name4", "dfghjkl;xcvbnm,.dfghjklertyuiopdfghjkl;xcvbnm,.dfghjkl;hello world4")
        .header("name5", "dfghjkl;xcvbnm,.dfghjklertyuiopdfghjkl;xcvbnm,.dfghjkl;hello world5")
        .header("name6", "hedfghjkl;xcvbnm,.dfghjklertyuiopdfghjkl;xcvbnm,.dfghjkl;hellollo world6")
        .header("name7", "hello worldfghjkl;xcvbnm,.dfghjklertyuiopdfghjkl;xcvbnm,.dfghjkl;hellod7")
        .header("name8", "hello world8dfghjkl;xcvbnm,.dfghjklertyuiopdfghjkl;xcvbnm,.dfghjkl;hello")
        .header("name9", "hello wordfghjkl;xcvbnm,.dfghjklertyuiopdfghjkl;xcvbnm,.dfghjkl;hellold9")
        .header("name11",
            "heldfghjkl;xcvbnm,.dfghjklertyuiopdfghjkl;xcvbnm,.dfghjkl;hellolo wdfghjkl;xcvbnm,.dfghjklertyuiopdfghjkl;xcvbnm,.dfghjkl;helloorld11")
        .header("name22",
            "hello wdfghjkl;xcvbnm,.dfghjklertyuiopdfghjkl;xcvbnm,.dfghjkl;helloorld22")
        .header("name22",
            "hedfghjkl;xcvbnm,.dfghjklertyuiopdfghjkl;xcvbnm,.dfghjkl;hellollo world22")
        .header("name22",
            "heldfghjkl;xcvbnm,.dfghjklertyuiopdfghjkl;xcvbnm,.dfghjkl;hellolo wordfghjkl;xcvbnm,.dfghjklertyuiopdfghjkl;xcvbnm,.dfghjkl;hellold22");
    return builder;
  }
}
