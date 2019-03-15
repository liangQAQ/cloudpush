package com.huangliang.cloudpushportal.entity.req;

import lombok.Data;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 发送接口请求类
 */
@Data
public class SendFrom {

    /**
     * 发送目的地
     */
    @NotEmpty
    private List<String> to;
    /**
     * 发送内容
     */
    @NotEmpty
    private String msg;

    private String delay;

}
