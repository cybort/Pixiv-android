package com.example.administrator.essim.response;

import java.io.Serializable;
import java.util.List;

public class RecommendResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean contest_exists;
    private List<IllustsBean> illusts;
    private String next_url;
    private List<?> ranking_illusts;

    public List<IllustsBean> getIllusts() {
        return this.illusts;
    }

    public void setIllusts(List<IllustsBean> paramList) {
        this.illusts = paramList;
    }

    public String getNext_url() {
        return this.next_url;
    }

    public void setNext_url(String paramString) {
        this.next_url = paramString;
    }

    public List<?> getRanking_illusts() {
        return this.ranking_illusts;
    }

    public void setRanking_illusts(List<?> paramList) {
        this.ranking_illusts = paramList;
    }

    public boolean isContest_exists() {
        return this.contest_exists;
    }

    public void setContest_exists(boolean paramBoolean) {
        this.contest_exists = paramBoolean;
    }
}