package com.example.administrator.essim.models;

import com.example.administrator.essim.anotherProj.HomeListFragment;
import com.example.administrator.essim.anotherProj.HomeProfileFragment;
import com.example.administrator.essim.fragments.FragmentPixivLeft;

import java.util.ArrayList;
import java.util.List;

public class Reference {
    public static List<HotTag> sHotTags = new ArrayList<>();
    public static AuthorWorks sAuthorWorks, sSearchResult, tempWork;
    public static PixivRankItem sPixivRankItem;
    public static PixivIllustItem sPixivIllustItem;
    public static FragmentPixivLeft sFragmentPixivLeft;
    public static HomeProfileFragment sHomeProfileFragment;
    public static HomeListFragment sHomeListFragment;
}
