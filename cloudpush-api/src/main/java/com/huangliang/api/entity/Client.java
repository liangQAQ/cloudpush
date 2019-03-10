package com.huangliang.api.entity;

import ch.qos.logback.core.util.DatePatternToRegexUtil;
import com.huangliang.api.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 客户端websocket连接对象属性描述
 */
@Data
@NoArgsConstructor
public class Client implements Serializable {

    private String channelId;

    private String host;

    private String lastActiveTime;

    public Client(String channelId, String host) {
        this.channelId = channelId;
        this.host = host;
        this.lastActiveTime = DateUtils.getCurrentDateTime();
    }
}
