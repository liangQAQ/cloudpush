package com.huangliang.cloudpushwebsocket;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

//@RunWith(SpringRunner.class)
@SpringBootTest
public class CloudpushWebsocketApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void a1(){
        Map<String,String> a = new HashMap<String,String>();
        a.put("a","b");
        a.put("a2","b2");
        a.put("a3","b3");
        a.put("a4","b4");
        a.put("a5","b5");
        synchronized (a){
            for(String key:a.keySet()){
                if("a3".equals(key)){
                    a.remove(key);
                }
            }
        }


        System.out.println(a);
    }

}
