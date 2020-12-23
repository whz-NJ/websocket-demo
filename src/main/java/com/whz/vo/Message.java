package com.whz.vo;

/**
 * @author whz
 * @create 2020-12-23 8:53
 * @desc TODO: add description here
 **/
public class Message {
    private String title;
    private String content;
    private String receiver;
    private String sender;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getReceiver() {
    return receiver;
  }

  public void setReceiver(String receiver) {
    this.receiver = receiver;
  }

  public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }
}