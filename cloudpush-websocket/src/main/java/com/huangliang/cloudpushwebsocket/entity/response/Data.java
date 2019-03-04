package com.huangliang.cloudpushwebsocket.entity.response;


import com.huangliang.cloudpushwebsocket.constants.CommonConsts;

public class Data<T>
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

    private String msg;

    /**
     * 消息说明
     */
    private String retInfo;

    /**
     * 返回数据
     */
    private T data;

    public Data()
    {
        this.ret = CommonConsts.ERROR;
    }

    public Data(String ret, String retInfo)
    {
        this.ret = ret;
        this.retInfo = retInfo;
        this.msg = retInfo;
        if (CommonConsts.SUCCESS.equals(ret))
        {
            success = true;
        }
        else
        {
            success = false;
        }
    }

    public Data(String ret, String retInfo, T data)
    {
        this.ret = ret;
        this.retInfo = retInfo;
        this.msg = retInfo;
        this.data = data;
        if (CommonConsts.SUCCESS.equals(ret))
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

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public T getData()
    {
        return data;
    }

    public void setData(T data)
    {
        this.data = data;
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
