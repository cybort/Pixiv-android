package com.example.administrator.essim.response;

import java.io.Serializable;
import java.util.List;

public class RelatedIllust implements Serializable {
    public String getNext_url() {
        return next_url;
    }

    public void setNext_url(String next_url) {
        this.next_url = next_url;
    }

    public List<IllustsBean> getIllusts() {
        return illusts;
    }

    public void setIllusts(List<IllustsBean> illusts) {
        this.illusts = illusts;
    }

    public String next_url;
    public List<IllustsBean> illusts;
}
