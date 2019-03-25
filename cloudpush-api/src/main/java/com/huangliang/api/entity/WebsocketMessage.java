package com.huangliang.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class WebsocketMessage implements Serializable {

    //一次会话的id，推送和回执保持一致
    private String id;
    //消息的类型
    private Integer type;
    //目标客户端标识
    private String to;
    //来源

    private String msg;
    //消息内容
    @JsonIgnore
    private String from;
    //触发类型 1.接口调用触发 2.websocket通信触发
    private Integer trigger;

    public enum Type {
        //服务端向客户端推送类型
        SEND(1,"SEND"),
        //客户端收到消息回执类型
        ACK(2,"ACK"),
        //心跳类型
        HEARTBEAT(3,"HEARTBEAT");

        Integer code;
        String info;

        Type(Integer code, String info){
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }

    public enum Trigger {
        //触发推送的方式
        //1.接口请求的方式
        HTTP(1,"HTTP"),
        //2.websocket消息触发
        WEBSOCKET(2,"WEBSOCKET");

        Integer code;
        String info;

        Trigger(Integer code, String info) {
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }
}
