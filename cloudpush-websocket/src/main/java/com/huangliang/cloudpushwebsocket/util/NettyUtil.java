package com.huangliang.cloudpushwebsocket.util;

import com.huangliang.api.constants.Constants;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;
import io.netty.handler.codec.http.multipart.MemoryAttribute;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class NettyUtil {
	
	/**
	 * 将请求中的参数与参数值转换为Map
	 * @param req
	 * @return
	 */
	public static Map<String, String> getRequestParams(HttpRequest req) {
		Map<String, String> requestParams = new HashMap<String, String>();
		if (req.getMethod().toString() == Constants.GET) {
			QueryStringDecoder decoder = new QueryStringDecoder(req.getUri());
			Map<String, List<String>> parame = decoder.parameters();
			Iterator<Entry<String, List<String>>> iterator = parame.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, List<String>> next = iterator.next();
				requestParams.put(next.getKey(), next.getValue().get(0));
			}
		}
		if (req.getMethod().toString() == Constants.POST) {
			HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), req);
			List<InterfaceHttpData> postData = decoder.getBodyHttpDatas(); 
			for (InterfaceHttpData data : postData) {
				if (data.getHttpDataType() == HttpDataType.Attribute) {
					MemoryAttribute attribute = (MemoryAttribute) data;
					requestParams.put(attribute.getName(), attribute.getValue());
				}
			}
		}
		return requestParams;
	}
}
