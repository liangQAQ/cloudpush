package com.huangliang.cloudpushwebsocket;

import com.huangliang.api.util.NetUtils;
import com.huangliang.cloudpushwebsocket.netty.Server;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class CloudpushWebsocketApplication implements CommandLineRunner {

    private static String yamlPath = "application.yml";
//    private static String yamlPath = "src/main/resources/application.yml";
    private static String yamlKey = "hostip";

    @Value("${hostip}")
    private static String hostip;

    @Autowired
    private Server socketServer;

    public static void main(String[] args) {
        init();
        SpringApplication.run(CloudpushWebsocketApplication.class, args);
    }

    @Override
    public void run(String... args){

        ChannelFuture future = socketServer.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> socketServer.destroy()));
        future.channel().closeFuture().syncUninterruptibly();
    }

    private static void init(){
        //根据网卡动态设置ip（为了将netty的ip:port维护进redis）
        initIp();
    }

    private static void initIp() {
        //优先获取jvm参数中指定的ip
        hostip = System.getProperty(yamlKey);
        if(StringUtils.isNotEmpty(hostip)){
            log.info("jvm启动参中指定ip为[{}]",hostip);
            return ;
        }else{
            String localIP = NetUtils.getLocalHost();
            hostip = localIP;
            System.setProperty(yamlKey,localIP);
            log.info("自动获取ip为[{}]",localIP);
        }
        log.info("若此ip不是与网关gateway通信的内网ip，请尝试通过jvm参数指定[{}]","java -jar -Dhostip=x.x.x.x");
    }
}
