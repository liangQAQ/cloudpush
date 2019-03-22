package com.huangliang.cloudpushwebsocket.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class WebsocketMessage implements Serializable {

    //一次会话的id，推送和回执保持一致
    private String id;
    //消息的类型
    private String type;
    //目标客户端标识
    private String to;
    //消息内容
    private String msg;
    //来源
    private String from;
    //触发类型 1.接口调用触发 2.websocket通信触发
    private String trigger;

}
