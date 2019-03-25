package com.huangliang.cloudpushwebsocket.constants;

import io.netty.util.AttributeKey;

public interface Constants {

	public final static AttributeKey<String> attrChannelId = AttributeKey.valueOf("channelId");

	public final static AttributeKey<String> attrActiveTime = AttributeKey.valueOf("activeTime");

}
