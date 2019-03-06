package com.huangliang.cloudpushportal.entity.response;


import com.huangliang.api.constants.CommonConsts;

@lombok.Data
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

}
