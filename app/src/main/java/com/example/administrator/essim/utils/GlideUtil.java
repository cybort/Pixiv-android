package com.example.administrator.essim.utils;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.Headers;
import com.example.administrator.essim.response.IllustsBean;
import com.example.administrator.essim.response.SearchUserResponse;
import com.example.administrator.essim.response.UserDetailResponse;

import java.util.HashMap;
import java.util.Map;

/*
┌───┐   ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┐
│Esc│   │ F1│ F2│ F3│ F4│ │ F5│ F6│ F7│ F8│ │ F9│F10│F11│F12│ │P/S│S L│P/B│  ┌┐    ┌┐    ┌┐
└───┘   └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┘  └┘    └┘    └┘
┌───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───────┐ ┌───┬───┬───┐ ┌───┬───┬───┬───┐
│~ `│! 1│@ 2│# 3│$ 4│% 5│^ 6│& 7│* 8│( 9│) 0│_ -│+ =│ BacSp │ │Ins│Hom│PUp│ │N L│ / │ * │ - │
├───┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─────┤ ├───┼───┼───┤ ├───┼───┼───┼───┤
│ Tab │ Q │ W │ E │ R │ T │ Y │ U │ I │ O │ P │{ [│} ]│ | \ │ │Del│End│PDn│ │ 7 │ 8 │ 9 │   │
├─────┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴─────┤ └───┴───┴───┘ ├───┼───┼───┤ + │
│ Caps │ A │ S │ D │ F │ G │ H │ J │ K │ L │: ;│" '│ Enter  │               │ 4 │ 5 │ 6 │   │
├──────┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴────────┤     ┌───┐     ├───┼───┼───┼───┤
│ Shift  │ Z │ X │ C │ V │ B │ N │ M │< ,│> .│? /│  Shift   │     │ ↑ │     │ 1 │ 2 │ 3 │   │
├─────┬──┴─┬─┴──┬┴───┴───┴───┴───┴───┴──┬┴───┼───┴┬────┬────┤ ┌───┼───┼───┐ ├───┴───┼───┤ E││
│ Ctrl│    │Alt │         Space         │ Alt│    │    │Ctrl│ │ ← │ ↓ │ → │ │   0   │ . │←─┘│
└─────┴────┴────┴───────────────────────┴────┴────┴────┴────┘ └───┴───┴───┘ └───────┴───┴───┘
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

    public GlideUrl getHead(long userID, String url) {
        Headers headers = () -> {
            Map<String, String> header = new HashMap<>();
            header.put("Referer", " https://www.pixiv.net/member.php?id=" +
                    String.valueOf(userID));
            return header;
        };

        return new GlideUrl(url, headers);
    }
}
