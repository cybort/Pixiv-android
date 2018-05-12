package com.example.administrator.essim.response;

import java.io.Serializable;
import java.util.List;

public class UserIllustsResponse
        implements Serializable
{
    private List<IllustsBean> illusts;
    private String next_url;

    public List<IllustsBean> getIllusts()
    {
        return this.illusts;
    }

    public String getNext_url()
    {
        return this.next_url;
    }

    public void setIllusts(List<IllustsBean> paramList)
    {
        this.illusts = paramList;
    }

    public void setNext_url(String paramString)
    {
        this.next_url = paramString;
    }
}