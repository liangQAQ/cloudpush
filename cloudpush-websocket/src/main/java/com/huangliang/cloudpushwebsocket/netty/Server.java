package com.huangliang.cloudpushwebsocket.netty;

import com.huangliang.api.constants.RedisPrefix;
import com.huangliang.cloudpushwebsocket.constants.Constants;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
@Slf4j
public class Server {

    private static final int InitPort = 9000;
    private static String nettyPort = "";
    private static final String url = "127.0.0.1";

    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    @Value("${eureka.instance.instance-id}")
    public String instanceId;

    @Autowired
    private ServerInitializer serverInitializer;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    private Channel channel;


    /**
     * 启动netty服务
     */
    public ChannelFuture start () {
        ChannelFuture f = null;
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(serverInitializer)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            //启动netty服务绑定端口
            f = bind(b,InitPort);
            channel = f.channel();
            //服务初始化
            init();
        } catch (Exception e) {
            log.error("Netty start error:", e);
        } finally {
            if (f != null && f.isSuccess()) {
//                log.info("Netty server listening " + address.getHostName() + " on port " + address.getPort() + " and ready for connections...");
            } else {
                log.error("Netty server start up Error!");
            }
        }

        return f;
    }

    /**
     * 递归启动，从port端口开始，绑定不成功就+1 继续绑定
     * @param serverBootstrap
     * @param port
     * @return
     */
    private static ChannelFuture bind(final ServerBootstrap serverBootstrap, final int port) {
        return serverBootstrap.bind(port).addListener(new GenericFutureListener<Future<? super Void>>() {
            public void operationComplete(Future<? super Void> future) {
                if (future.isSuccess()) {
                    log.info("netty端口在[" + port + "]启动成功!");
                    nettyPort = port+"";
                } else {
                    log.info("netty端口在[" + port + "]启动失败,继续尝试启动...");
                    bind(serverBootstrap, port + 1);
                }
            }
        });
    }

    private void init(){
        //设置redis中记录的websocket服务地址的连接端口
        setRedisWebsocketPort();
        //创建该websocket服务所使用的rocketmq对应的topic
    }

    public void destroy() {
        if(channel != null) { channel.close();}
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }

    private void setRedisWebsocketPort() {
        if(redisTemplate.opsForHash().hasKey(RedisPrefix.WEBSOCKETSERVER,instanceId)){
            redisTemplate.opsForHash().put(RedisPrefix.WEBSOCKETSERVER,instanceId,nettyPort);
            log.info("设置实例[{}]的netty端口为[{}].",instanceId,nettyPort);
        }else{
            log.info("不存在[{}]的实例,netty初始化失败...",instanceId);
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                log.error(e.getMessage());
//            }
//            setRedisWebsocketPort();
        }
    }
}