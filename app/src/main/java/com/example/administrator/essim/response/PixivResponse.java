package com.example.administrator.essim.response;

import java.util.List;

public class PixivResponse {
    private List<String> search_auto_complete_keywords;

    public List<String> getSearch_auto_complete_keywords() {
        return this.search_auto_complete_keywords;
    }

    public void setSearch_auto_complete_keywords(List<String> paramList) {
        this.search_auto_complete_keywords = paramList;
    }
}