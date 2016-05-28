package com.miqian.mq.net;

public interface ICallback<T> {

    void onSucceed(T result);

    void onFail(String error);

}
