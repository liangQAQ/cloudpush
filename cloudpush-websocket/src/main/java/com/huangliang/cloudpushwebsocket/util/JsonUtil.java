package com.huangliang.cloudpushwebsocket.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.StringWriter;

/**
 * json转换工具类
 */
public class JsonUtil
{
    private static final Logger log = null;

    /**
     * 对象转json串
     */
    public static String objectToJson(Object object)
    {
        StringWriter sw = new StringWriter();
        try
        {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(sw, object);
//            objectMapper.writeValue(sw, object);
            return sw.toString();
        }
        catch (IOException e)
        {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 对象转json串
     */
    public static JSONObject beanToJson(Object object)
    {
        return JSONObject.fromObject(object);
    }

    /**
     * json转对象
     */
    public static <T> T jsonToObject(String json, Class<T> clazz)
    {
        if (StringUtils.isEmpty(json))
        {
            return null;
        }
        try
        {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.readValue(json, clazz);
        }
        catch (IOException e)
        {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * json转对象
     */
    public static <T> T jsonToBean(String json, Class<T> clazz)
    {
        if (StringUtils.isEmpty(json))
        {
            return null;
        }
        return (T) JSONObject.toBean(JSONObject.fromObject(json), clazz);
    }

    /**
     * json转对象
     */
    public static <T> T jsonToBean(JSONObject json, Class<T> clazz)
    {
        if (json == null || json.isEmpty())
        {
            return null;
        }
        return (T) JSONObject.toBean(json, clazz);
    }
}
