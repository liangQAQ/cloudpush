package com.huangliang.api.constants;

public interface RedisPrefix {
    //websocket的服务地址，类型-hash<ip:port,nettyPort>，key为tomcat实例名(ip:port)，value为websocket端口
    public static String WEBSOCKETSERVER = "websocket";

    //客户端连接前缀，后缀为客户端标识-hash
    public static String PREFIX_CLIENT = "client_";

    //websocket服务所连接的客户端id集合前缀-list
    public static String PREFIX_SERVERCLIENTS = "serverclients_";
}
