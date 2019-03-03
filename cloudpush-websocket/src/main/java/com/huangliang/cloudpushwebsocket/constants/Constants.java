package com.huangliang.cloudpushwebsocket.constants;

public interface Constants {

	public String WEBSOCKET = "websocket";

	public String UPGRADE = "Upgrade";
	
	public String POST = "POST";

	public String GET = "GET";
	
	public String CHANNELID = "channelId";

	/**
	 * 发送方式枚举类
	 * @author hehe
	 *
	 */
	public enum SendType {
		
		ChannelId("1","根据推送标识发送"), GroupCode("2", "根据群组名发送");
		
		public String code;
		
		public String info;
		
		private SendType(String code, String info) {
			this.code = code;
			this.info = info;
		}
	}
	
	/**
	 * 消息枚举类
	 * @author hehe
	 *
	 */
	public enum MessageType {
		
		NOTICE("1","通知消息"), THROUGH("2", "透传消息");
		
		public String code;
		
		public String info;
		
		private MessageType(String code, String info) {
			this.code = code;
			this.info = info;
		}
	}
	
	/**
	 * 请求枚举类
	 * @author hehe
	 *
	 */
	public enum HttpUrlType {
		
		GetUser("GetUser","查询系统中的用户"),
		GetGroupUser("GetGroupUser","查询群组中的用户"), 
		GetUserGroup("GetUserGroup","查询查询用户拥有的群组"), 
		GetGroupCount("GetGroupCount","查询群组中的用户数量"), 
		CreateGroup("CreateGroup","创建群组"), 
		AddUserToGroup("AddUserToGroup","向群组中添加用户"), 
		RemoveUserFromGroup("RemoveUserFromGroup","向群组中移除用户"),
		SendToUser("SendToUser","向某用户发送消息"),
		SendToGroup("SendToGroup", "向某一群组发送消息");
		
		public String url;
		
		public String info;
		
		private HttpUrlType(String url, String info) {
			this.url = url;
            this.info = info;
		}
	}
}
