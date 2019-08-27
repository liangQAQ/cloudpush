# cloudpush
一个基于springcloud，netty，redis，rocketmq的websocket推送服务
### cloudpush-api
    公共代码，被其他模块所引用
### cloudpush-eureka
    springcloud-eureka注册中心，方便平滑的上下线服务，水平拓展
### cloudpush-websocket
    由springboot启动的一个netty服务，对外的netty端口提供websocket的握手，消息的收发服务
### cloudpush-portal
    对外提供所需的所有http接口服务
### cloudpush-gateway
    springcloud-gateway网管，统一所有的http，websocket入口。方便未来鉴权、过滤、限流。
    目前根据各个websocket实例的连接数量，把新来的websocket连接请求路由到较少连接数的websocket实例上。
### 推送流程
    1.websocket实例收到客户端的ws握手请求以后，将客户端的推送标识，处理握手的websocket服务实例名
    （当websocket模块集群部署时，应用名都为websocket，实例名各不相同）等相关信息记录进redis。
    2.portal模块对外暴露的接口接收请求
    根据请求中的客户端推送标识从redis中找出对应客户端所在的websocket实例，
    向具体客户端所在的websocket实例发送消息。
    3.对应websocket实例接收请求对客户端发起websocket消息推送
    
