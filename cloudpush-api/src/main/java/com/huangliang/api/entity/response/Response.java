package com.huangliang.api.entity.response;

import com.huangliang.api.constants.CommonConsts;

public class Response<T>
{
    /**
     * code = 0 时返回true
     * code != 0 时返回false
     */
    private boolean success;

    /**
     * 消息状态码
     * 0 表示成功
     * 其它表示失败
     */
    private String code;

    private String msg;

    /**
     * 返回数据
     */
    private T data;

    public Response()
    {
        this.code = CommonConsts.SUCCESS;
        this.success = CommonConsts.TRUE;
        this.msg = CommonConsts.REQUST_SUC;
    }

    public Response(String msg,T data)
    {
        this.code = CommonConsts.SUCCESS;
        this.success = CommonConsts.TRUE;
        this.data = data;
        this.msg = msg;
    }

    public Response(String code, String retInfo)
    {
        this.code = code;
        this.msg = retInfo;
        if (CommonConsts.SUCCESS.equals(code))
        {
            success = true;
        }
        else
        {
            success = false;
        }
    }

    public Response(String code, String msg, T data)
    {
        this.code = code;
        this.msg = msg;
        this.data = data;
        if (CommonConsts.SUCCESS.equals(code))
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
        return code;
    }

    public void setRet(String code)
    {
        this.code = code;
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
