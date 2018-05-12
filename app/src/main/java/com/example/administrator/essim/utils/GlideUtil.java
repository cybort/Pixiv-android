package com.example.administrator.essim.utils;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.Headers;

import java.util.HashMap;
import java.util.Map;

import com.example.administrator.essim.response.IllustsBean;
import com.example.administrator.essim.response.SearchUserResponse;
import com.example.administrator.essim.response.UserDetailResponse;

public class GlideUtil {

    public GlideUrl getMediumImageUrl(IllustsBean illustsBean) {
        Headers headers = () -> {
            Map<String, String> header = new HashMap<>();
            header.put("Referer", "https://www.pixiv.net/member_illust.php?mode=medium&illust_id=" +
                    illustsBean.getId());
            return header;
        };
        return new GlideUrl(illustsBean.getImage_urls().getMedium(), headers);
    }

    public GlideUrl getMediumImageUrl(IllustsBean illustsBean, int index) {
        Headers headers = () -> {
            Map<String, String> header = new HashMap<>();
            header.put("Referer", "https://www.pixiv.net/member_illust.php?mode=medium&illust_id=" +
                    illustsBean.getId());
            return header;
        };
        if (illustsBean.getPage_count() == 1) {
            return new GlideUrl(illustsBean.getImage_urls().getMedium(), headers);
        } else {
            return new GlideUrl(illustsBean.getMeta_pages().get(index).getImage_urlsX().getMedium(), headers);
        }
    }

    public GlideUrl getLargeImageUrl(IllustsBean illustsBean) {
        Headers headers = () -> {
            Map<String, String> header = new HashMap<>();
            header.put("Referer", "https://www.pixiv.net/member_illust.php?mode=medium&illust_id=" +
                    illustsBean.getId());
            return header;
        };
        if (illustsBean.getPage_count() > 1) {
            return new GlideUrl(illustsBean.getMeta_pages().get(0).getImage_urlsX().getOriginal());
        } else {
            return new GlideUrl(illustsBean.getMeta_single_page().getOriginal_image_url(), headers);
        }
    }

    public GlideUrl getHead(SearchUserResponse.UserPreviewsBean userPreviewsBean) {
        Headers headers = () -> {
            Map<String, String> header = new HashMap<>();
            header.put("Referer", " https://www.pixiv.net/member.php?id=" +
                    userPreviewsBean.getUser().getId());
            return header;
        };

        return new GlideUrl(userPreviewsBean.getUser().getProfile_image_urls().getMedium(), headers);
    }

    public GlideUrl getHead(UserDetailResponse.UserBean userPreviewsBean) {
        Headers headers = () -> {
            Map<String, String> header = new HashMap<>();
            header.put("Referer", " https://www.pixiv.net/member.php?id=" +
                    userPreviewsBean.getId());
            return header;
        };

        return new GlideUrl(userPreviewsBean.getProfile_image_urls().getMedium(), headers);
    }

    public GlideUrl getHead(String userID, String url) {
        Headers headers = () -> {
            Map<String, String> header = new HashMap<>();
            header.put("Referer", " https://www.pixiv.net/member.php?id=" +
                    userID);
            return header;
        };

        return new GlideUrl(url, headers);
    }
}
