package com.example.administrator.essim.response;

import java.io.Serializable;

public class IllustDetailResponse
        implements Serializable {
    private IllustsBean illust;

    public IllustsBean getIllust() {
        return this.illust;
    }

    public void setIllust(IllustsBean paramIllustBean) {
        this.illust = paramIllustBean;
    }
}