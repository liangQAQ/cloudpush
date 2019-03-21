package com.huangliang.api.constants;

public interface Constants {
    String WEBSOCKET_SERVER = "websocket";

    String ROCKETMQ_TOPIC_PREFIX = "websocket";

    String ROCKETMQ_GROUP_PREFIX = "group";

    String WEBSOCKET = "websocket";

    String UPGRADE = "Upgrade";

    String SYSTEM = "System";

    String POST = "POST";

    String GET = "GET";

    String CHANNELID = "channelId";

    enum MessageType {
        //服务端向客户端推送类型
        SEND("SEND"),
        //客户端收到消息回执类型
        ACK("ACK"),
        //心跳类型
        HEARTBEAT("HEARTBEAT");

        String value;

        MessageType(String s) {
            this.value = s;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    enum MessageTrigger {
        //客户端收到消息回执类型
        HTTP("HTTP"),
        //心跳类型
        WEBSOCKET("WEBSOCKET");

        String value;

        MessageTrigger(String s) {
            this.value = s;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
