package com.huangliang.api.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@AllArgsConstructor
public class WebsocketMessage implements Serializable {

    //一次会话的id，推送和回执保持一致
    @JsonInclude(value=JsonInclude.Include.NON_NULL)
    private String messageId;
    //消息的类型
    @JsonInclude(value=JsonInclude.Include.NON_NULL)
    private Integer type;
    //目标客户端标识
    @JsonInclude(value=JsonInclude.Include.NON_NULL)
    private String to;
    //来源
    @JsonInclude(value=JsonInclude.Include.NON_NULL)
    private String msg;
    //消息内容
    @JsonIgnore
    private String from;
    //触发类型 1.接口调用触发 2.websocket通信触发
    private Integer trigger;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date activeTime = new Date();

    public WebsocketMessage(String messageId, Integer type, String to, String msg, String from, Integer trigger) {
        this.messageId = messageId;
        this.type = type;
        this.to = to;
        this.msg = msg;
        this.from = from;
        this.trigger = trigger;
    }

    public enum Type {
        //服务端向客户端发送类型
        SENDTOCLIENT(1,"SENDTOCLIENT"),
        //服务端向客户端发送回执类型
        SENDTOCLIENTACK(2,"SENDTOCLIENTACK"),
        //客户端向服务端发送类型
        SENDTOSERVER(3,"SENDTOSERVER"),
        //客户端向服务端发送回执类型
        SENDTOSERVERACK(4,"SENDTOSERVERACK"),
        //心跳类型
        HEARTBEAT(5,"HEARTBEAT"),
        //心跳类型回执
        HEARTBEATACK(6,"HEARTBEATACK");

        public Integer code;
        public String info;

        Type(Integer code, String info){
            this.code = code;
            this.info = info;
        }
        public Integer getCode() {
            return code;
        }
        public String getInfo() {
            return info;
        }
    }

    public enum Trigger {
        //触发推送的方式
        //1.接口请求的方式
        HTTP(1,"HTTP"),
        //2.websocket消息触发
        WEBSOCKET(2,"WEBSOCKET");

        public Integer code;
        public String info;

        Trigger(Integer code, String info) {
            this.code = code;
            this.info = info;
        }


        public Integer getCode() {
            return code;
        }
        public String getInfo() {
            return info;
        }
    }
}
