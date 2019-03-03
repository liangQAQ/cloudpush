package com.huangliang.cloudpushwebsocket.entity.response;


import com.huangliang.websocket.constants.CommonConsts;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Message")
public class Message
{
    /**
     * ret = 0 时返回true
     * ret != 0 时返回false
     */
    private boolean success;
    
    /**
     * 消息状态码
     * 0 表示成功
     * 其它表示失败
     */
    private String ret;
    
    /**
     * 消息说明
     */
    private String retInfo;

    public Message()
    {
        this.ret = CommonConsts.ERROR;
    }

    public Message(String ret, String retInfo)
    {
        this.ret = ret;
        this.retInfo = retInfo;
        if(CommonConsts.SUCCESS.equals(ret))
        {
            success = true;
        }
        else
        {
            success = false;
        }
    }
    
    public String getRet()
    {
        return ret;
    }

    public void setRet(String ret)
    {
        this.ret = ret;
    }

    public String getRetInfo()
    {
        return retInfo;
    }

    public void setRetInfo(String retInfo)
    {
        this.retInfo = retInfo;
    }

    public boolean isSuccess()
    {
        return success;
    }

    public void setSuccess(boolean success)
    {
        this.success = success;
    }

}
