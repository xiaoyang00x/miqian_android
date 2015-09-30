package com.miqian.mq.entity;

/**
 * Created by sunyong on 9/25/15.
 */
public class CommonEntity<T> {

  private String code;
  private String message;
  private T data;

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }


  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }
}
