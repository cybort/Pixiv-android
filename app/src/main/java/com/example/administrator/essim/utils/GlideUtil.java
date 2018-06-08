package com.example.administrator.essim.utils;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.Headers;
import com.example.administrator.essim.response.IllustsBean;
import com.example.administrator.essim.response.SearchUserResponse;
import com.example.administrator.essim.response.UserDetailResponse;

import java.util.HashMap;
import java.util.Map;

/*
//       \\           //
//        \\         //
//         \\       //
//   ##DDDDDDDDDDDDDDDDDDDDDD##
//   ## DDDDDDDDDDDDDDDDDDDD ##   ________   ___   ___        ___   ________   ___   ___        ___
//   ## hh                hh ##   |\   __  \ |\  \ |\  \      |\  \ |\   __  \ |\  \ |\  \      |\  \
//   ## hh    //    \\    hh ##   \ \  \|\ /_\ \  \\ \  \     \ \  \\ \  \|\ /_\ \  \\ \  \     \ \  \
//   ## hh   //      \\   hh ##    \ \   __  \\ \  \\ \  \     \ \  \\ \   __  \\ \  \\ \  \     \ \  \
//   ## hh                hh ##     \ \  \|\  \\ \  \\ \  \____ \ \  \\ \  \|\  \\ \  \\ \  \____ \ \  \
//   ## hh      wwww      hh ##      \ \_______\\ \__\\ \_______\\ \__\\ \_______\\ \__\\ \_______\\ \__\
//   ## hh                hh ##       \|_______| \|__| \|_______| \|__| \|_______| \|__| \|_______| \|__|
//   ## MMMMMMMMMMMMMMMMMMMM ##
//   ##MMMMMMMMMMMMMMMMMMMMMM##                                        Release 1.22.2. Powered by 主站前端.
//        \/            \/
 */


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

    public GlideUrl getMediumImageUrl(String id, String url) {
        Headers headers = () -> {
            Map<String, String> header = new HashMap<>();
            header.put("Referer", "https://www.pixiv.net/member_illust.php?mode=medium&illust_id=" +
                    id);
            return header;
        };
        return new GlideUrl(url, headers);
    }

    public GlideUrl getLargeImageUrl(IllustsBean illustsBean, int index) {
        Headers headers = () -> {
            Map<String, String> header = new HashMap<>();
            header.put("Referer", "https://www.pixiv.net/member_illust.php?mode=medium&illust_id=" +
                    illustsBean.getId());
            return header;
        };
        if (illustsBean.getPage_count() > 1) {
            return new GlideUrl(illustsBean.getMeta_pages().get(index).getImage_urlsX().getOriginal(), headers);
        } else {
            return new GlideUrl(illustsBean.getMeta_single_page().getOriginal_image_url(), headers);
        }
    }

    public GlideUrl getHead(SearchUserResponse.UserPreviewsBean userPreviewsBean) {
        Headers headers = () -> {
            Map<String, String> header = new HashMap<>();
            header.put("Referer", "https://www.pixiv.net/member.php?id=" +
                    userPreviewsBean.getUser().getId());
            return header;
        };

        return new GlideUrl(userPreviewsBean.getUser().getProfile_image_urls().getMedium(), headers);
    }

    public GlideUrl getHead(UserDetailResponse.UserBean userPreviewsBean) {
        Headers headers = () -> {
            Map<String, String> header = new HashMap<>();
            header.put("Referer", "https://www.pixiv.net/member.php?id=" +
                    userPreviewsBean.getId());
            return header;
        };

        return new GlideUrl(userPreviewsBean.getProfile_image_urls().getMedium(), headers);
    }

    public GlideUrl getHead(long userID, String url) {
        Headers headers = () -> {
            Map<String, String> header = new HashMap<>();
            header.put("Referer", "https://www.pixiv.net/member.php?id=" +
                    String.valueOf(userID));
            return header;
        };

        return new GlideUrl(url, headers);
    }

    public GlideUrl getHead(String url) {
        Headers headers = () -> {
            Map<String, String> header = new HashMap<>();
            header.put("Referer", "https://www.pixiv.net/showcase/");
            return header;
        };

        return new GlideUrl(url, headers);
    }
}
