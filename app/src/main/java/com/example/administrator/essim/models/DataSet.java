package com.example.administrator.essim.models;

import com.example.administrator.essim.anotherProj.HomeListFragment;
import com.example.administrator.essim.anotherProj.HomeProfileFragment;

import java.util.ArrayList;
import java.util.List;

public class DataSet {
    public static List<HotTag> sHotTags = new ArrayList<>();
    public static AuthorWorks sAuthorWorks, sSearchResult;
    public static PixivRankItem sPixivRankItem;

    public static HomeProfileFragment sHomeProfileFragment;
    public static HomeListFragment sHomeListFragment;
}
