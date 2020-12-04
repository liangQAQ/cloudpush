package com.huangliang.cloudpushportal.controller;

import com.huangliang.api.annotation.LogOperate;
import com.huangliang.api.entity.request.SendRequest;
import com.huangliang.api.entity.response.Response;
import com.huangliang.cloudpushportal.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Set;

@RestController
@Api(value = "MessageController")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @ApiOperation(value="消息推送接口", notes="根据用户标识进行推送，返回不存在的用户")
    @LogOperate()
    @RequestMapping(value="/message/send",method = RequestMethod.POST)
    public Response send(@RequestBody @Valid SendRequest request){
        Response result = null;
        Set notExist = messageService.execute(request);
        if(CollectionUtils.isNotEmpty(notExist)){
            result = new Response("存在找不到的客户端",notExist);
        }else{
            result = new Response();
        }
        return result;
    }
}
