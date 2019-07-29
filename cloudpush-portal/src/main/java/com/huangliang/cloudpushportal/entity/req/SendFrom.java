package com.huangliang.cloudpushportal.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 发送接口请求类
 */
@Data
@ApiModel(value="推送请求对象模型")
public class SendFrom {

    /**
     * 发送目的地
     */
    @NotEmpty
    @ApiModelProperty(value="推送目的地的数组" ,required=true)
    private List<String> to;
    /**
     * 发送内容
     */
    @NotEmpty
    @ApiModelProperty(value="推送消息的内容" ,required=true)
    private String msg;

    @ApiModelProperty(value="延时推送(单位秒,暂未实现)" ,required=false)
    private Integer delay;

    @ApiModelProperty(value="是否发送给所有人" ,required=false)
    private Boolean sendToAll;

}
