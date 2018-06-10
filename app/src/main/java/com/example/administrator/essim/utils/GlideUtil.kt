package com.example.administrator.essim.utils

import com.bumptech.glide.load.model.GlideUrl
import com.example.administrator.essim.response.IllustsBean
import com.example.administrator.essim.response.SearchUserResponse
import com.example.administrator.essim.response.UserDetailResponse
import java.util.*

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


class GlideUtil {

    fun getMediumImageUrl(illustsBean: IllustsBean): GlideUrl {
        val headers = {
            val header = HashMap<String, String>()
            header["Referer"] = "https://www.pixiv.net/member_illust.php?mode=medium&illust_id=" + illustsBean.id
            header
        }
        return GlideUrl(illustsBean.image_urls.medium, headers)
    }

    fun getMediumImageUrl(illustsBean: IllustsBean, index: Int): GlideUrl {
        val headers = {
            val header = HashMap<String, String>()
            header["Referer"] = "https://www.pixiv.net/member_illust.php?mode=medium&illust_id=" + illustsBean.id
            header
        }
        return if (illustsBean.page_count == 1) {
            GlideUrl(illustsBean.image_urls.medium, headers)
        } else {
            GlideUrl(illustsBean.meta_pages[index].image_urlsX.medium, headers)
        }
    }

    fun getMediumImageUrl(id: String, url: String): GlideUrl {
        val headers = {
            val header = HashMap<String, String>()
            header["Referer"] = "https://www.pixiv.net/member_illust.php?mode=medium&illust_id=$id"
            header
        }
        return GlideUrl(url, headers)
    }

    fun getLargeImageUrl(illustsBean: IllustsBean, index: Int): GlideUrl {
        val headers = {
            val header = HashMap<String, String>()
            header["Referer"] = "https://www.pixiv.net/member_illust.php?mode=medium&illust_id=" + illustsBean.id
            header
        }
        return when {
            illustsBean.page_count > 1 -> GlideUrl(illustsBean.meta_pages[index].image_urlsX.original, headers)
            else -> GlideUrl(illustsBean.meta_single_page.original_image_url, headers)
        }
    }

    fun getHead(userPreviewsBean: SearchUserResponse.UserPreviewsBean): GlideUrl {
        val headers = {
            val header = HashMap<String, String>()
            header["Referer"] = "https://www.pixiv.net/member.php?id=" + userPreviewsBean.user.id
            header
        }

        return GlideUrl(userPreviewsBean.user.profile_image_urls.medium, headers)
    }

    fun getHead(userPreviewsBean: UserDetailResponse.UserBean): GlideUrl {
        val headers = {
            val header = HashMap<String, String>()
            header["Referer"] = "https://www.pixiv.net/member.php?id=" + userPreviewsBean.id
            header
        }

        return GlideUrl(userPreviewsBean.profile_image_urls.medium, headers)
    }

    fun getHead(userID: Long, url: String): GlideUrl {
        val headers = {
            val header = HashMap<String, String>()
            header["Referer"] = "https://www.pixiv.net/member.php?id=" + userID.toString()
            header
        }

        return GlideUrl(url, headers)
    }

    fun getHead(url: String): GlideUrl {
        val headers = {
            val header = HashMap<String, String>()
            header["Referer"] = "https://www.pixiv.net/showcase/"
            header
        }

        return GlideUrl(url, headers)
    }
}
