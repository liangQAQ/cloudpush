package com.huangliang.cloudpushportal.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class WebsocketServer implements Serializable {
    private String address;
}
