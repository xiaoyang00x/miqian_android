package com.miqian.mq.listener;

/**
 * Created by guolei_wang on 16/1/15.
 * 监听用户登录、注销
 */
public interface LoginListener {
    void loginSuccess();
    void logout();
}
