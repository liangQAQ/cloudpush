package com.huangliang.cloudpushwebsocket.netty;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
public class ServerInitializer extends ChannelInitializer<SocketChannel> {

	@Autowired
	private WebsocketRequestHandler websocketRequestHandler;
	@Autowired
	private HttpRequestHandler httpRequestHandler;

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(65536));
        pipeline.addLast(new ChunkedWriteHandler());
		// http处理握手
		pipeline.addLast(httpRequestHandler);
		//websocket处理websocket消息
		pipeline.addLast(websocketRequestHandler);
	}
}