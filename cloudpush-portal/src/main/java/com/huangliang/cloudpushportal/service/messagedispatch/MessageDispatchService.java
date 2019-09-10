package com.huangliang.cloudpushportal.service.messagedispatch;

import com.huangliang.api.entity.request.SendRequest;

/**
 * 消息分发接口定义
 */
public interface MessageDispatchService {
    void send(String instants,SendRequest request);
}
