package com.huangliang.cloudpushportal.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendMessage {

    @RequestMapping(value="/message/send")
    public String send(){

        return "";
    }
}