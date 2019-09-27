package com.huangliang.cloudpushtask.task;

import com.huangliang.api.constants.RedisPrefix;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 根据各websocket实例的连接数量，
 * 来计算网关应该给各个websocket实例所分配的路由权重
 * 写入redis供网关获取
 */
@Component
@EnableScheduling
@Slf4j
public class CountWebsocketServerWeight {

    private static final Integer TOTALWEIGHT = 100;

    @Autowired
    private RedisTemplate redisTemplate;

    @Scheduled(fixedRate=60000)
    private void execute(){
        log.info("计算权重任务开始..");
        //每个服务器上对应的websocket连接数量
        Map<String,String> result = new HashMap<>();
        int totalCount = 0;
        //连接数为0的服务数量
        int newServerCount = 0;
        //所有服务
        Map<String,String> map = redisTemplate.opsForHash().entries(RedisPrefix.WEBSOCKETSERVER);
        for(Map.Entry<String,String> entry : map.entrySet()){
            if(StringUtils.isEmpty(entry.getValue())){
                continue;//只返回设置好netty websocket端口的实例地址
            }
            int count = redisTemplate.opsForSet().size(RedisPrefix.PREFIX_SERVERCLIENTS+entry.getKey()).intValue();
            if(count == 0){
                newServerCount++;
            }
            result.put(entry.getKey(),count+"");
            totalCount = totalCount + count;
        }
        if(newServerCount>0){
            result = getWeightWithNewServer(result,newServerCount);
        }else{
            result = getWeightWithOutNewServer(result,totalCount);
        }
        //覆盖写入redis供网关使用
        redisTemplate.delete(RedisPrefix.WEBSOCKETWEIGHT);
        redisTemplate.opsForHash().putAll(RedisPrefix.WEBSOCKETWEIGHT,result);
        log.info("计算结果为:"+result);
    }

    /**
     * 计算存在新连上来的websocket实例权重
     * 权重全分配至新连上来的实例，其他的为0
     * @param map
     * @param newServerCount
     * @return
     */
    private Map<String, String> getWeightWithNewServer(Map<String,String> map ,int newServerCount) {
        int weight = 1;
        for(Map.Entry<String,String> entry : map.entrySet()){
            Integer instantceCount = Integer.parseInt(entry.getValue());
            if(instantceCount == 0){
                weight = TOTALWEIGHT/newServerCount;
            }else{
                weight = 0 ;
            }
            entry.setValue(weight+"");
        }
        return map;
    }

    /**
     * 计算不存在新连接上来的实例的所有权重
     * @param result
     * @param totalCount
     * @return
     */
    private Map<String, String> getWeightWithOutNewServer(Map<String, String> result, int totalCount) {
        Map<String,Double> weight = new HashMap<>(result.size());
        Double totalWeight = 0.0;
        for(Map.Entry<String,String> entry : result.entrySet()){
            Double itemWeight =  ((double)totalCount/Double.parseDouble(entry.getValue()));
            totalWeight = totalWeight + itemWeight;
            weight.put(entry.getKey(), itemWeight);
        }
        //转换为百分比
        for(Map.Entry<String,Double> entry : weight.entrySet()){
            result.put(entry.getKey(),(int)((entry.getValue()/totalWeight)*100)+"");
        }
        return result;
    }
}
