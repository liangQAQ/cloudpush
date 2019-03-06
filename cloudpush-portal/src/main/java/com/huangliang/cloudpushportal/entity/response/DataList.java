package com.huangliang.cloudpushportal.entity.response;

import com.huangliang.api.constants.CommonConsts;

import java.io.Serializable;
import java.util.List;


public class DataList<T> implements Serializable
{

    /**
	 * 
	 */
	private static final long serialVersionUID = -7436387203422872449L;

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

    private String msg;

    /**
     * 返回数据条数
     */
    private int totalCount;

    /**
     * 返回数据
     */
    private List<T> dataList;

    public DataList()
    {
        this.ret = CommonConsts.ERROR;
    }

    public DataList(String ret, String retInfo)
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

    public DataList(String ret, String retInfo, List<T> dataList, int totalCount)
    {
        this.ret = ret;
        this.retInfo = retInfo;
        this.msg = retInfo;
        this.dataList = dataList;

        if (totalCount == 0)
        {
            if (dataList != null)
            {
                this.totalCount = dataList.size();
            }
        }
        else
        {
            this.totalCount = totalCount;
        }
        if (CommonConsts.SUCCESS.equals(ret))
        {
            success = true;
        }
        else
        {
            success = false;
        }
    }

    public int getTotalCount()
    {
        return totalCount;
    }

    public void setTotalCount(int totalCount)
    {
        this.totalCount = totalCount;
    }

    public String getRet()
    {
        return ret;
    }

    public void setRet(String ret)
    {
        this.ret = ret;
    }

    public boolean isSuccess()
    {
        return success;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public void setSuccess(boolean success)
    {
        this.success = success;
    }

    public String getRetInfo()
    {
        return retInfo;
    }

    public void setRetInfo(String retInfo)
    {
        this.retInfo = retInfo;
    }

    public List<T> getDataList()
    {
        return dataList;
    }

    public void setDataList(List<T> dataList)
    {
        this.dataList = dataList;
    }

}
