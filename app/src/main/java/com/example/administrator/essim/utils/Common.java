package com.example.administrator.essim.utils;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.example.administrator.essim.models.Reference;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Administrator on 2017/10/28 0028.
 * <p>
 * quu..__
 * $$$b  `---.__
 * "$$b        `--.                          ___.---uuudP
 * `$$b           `.__.------.__     __.---'      $$$$"              .
 * "$b          -'            `-.-'            $$$"              .'|
 * ".                                       d$"             _.'  |
 * `.   /                              ..."             .'     |
 * `./                           ..::-'            _.'       |
 * /                         .:::-'            .-'         .'
 * :                          ::''\          _.'            |
 * .' .-.             .-.           `.      .'               |
 * : /'$$|           .@"$\           `.   .'              _.-'
 * .'|$u$$|          |$$,$$|           |  <            _.-'
 * | `:$$:'          :$$$$$:           `.  `.       .-'
 * :                  `"--'             |    `-.     \
 * :##.       ==             .###.       `.      `.    `\
 * |##:                      :###:        |        >     >
 * |#'     `..'`..'          `###'        x:      /     /
 * \                                   xXX|     /    ./
 * \                                xXXX'|    /   ./
 * /`-.                                  `.  /   /
 * :    `-  ...........,                   | /  .'
 * |         ``:::::::'       .            |<    `.
 * |             ```          |           x| \ `.:``.
 * |                         .'    /'   xXX|  `:`M`M':.
 * |    |                    ;    /:' xXXX'|  -'MMMMM:'
 * `.  .'                   :    /:'       |-'MMMM.-'
 * |  |                   .'   /'        .'MMM.-'
 * `'`'                   :  ,'          |MMM<
 * |                     `'            |tbap\
 * \                                  :MM.-'
 * \                 |              .''
 * \.               `.            /
 * /     .:::::::.. :           /
 * |     .:::::::::::`.         /
 * |   .:::------------\       /
 * /   .''               >::'  /
 * `',:                 :    .'
 * `:.:'
 */

public class Common {

    public static final String[] arrayOfString = {"1", "2", "3", "4", "5", "6", "7", "8", "9",
            "10"};
    public static final String[] arrayOfRankMode = {"日榜", "周榜", "月榜", "新人", "原创", "男性向", "女性向"};
    public static final String url_rank_daily = "https://api.imjad.cn/pixiv/v1/?type=rank&content=illust&" +
            "mode=daily&per_page=30&date=" + Common.getLastDay();
    public static final String url_rank_weekly = "https://api.imjad.cn/pixiv/v1/?type=rank&content=illust&" +
            "mode=weekly&per_page=30&date=" + Common.getLastDay();
    public static final String url_rank_monthly = "https://api.imjad.cn/pixiv/v1/?type=rank&content=all&" +
            "mode=monthly&per_page=30&date=" + Common.getLastDay();
    public static final String url_rank_rookie = "https://api.imjad.cn/pixiv/v1/?type=rank&content=all&" +
            "mode=rookie&per_page=30&date=" + Common.getLastDay();
    public static final String url_rank_original = "https://api.imjad.cn/pixiv/v1/?type=rank&content=all&" +
            "mode=original&per_page=30&date=" + Common.getLastDay();
    public static final String url_rank_male = "https://api.imjad.cn/pixiv/v1/?type=rank&content=all&" +
            "mode=male&per_page=30&date=" + Common.getLastDay();
    public static final String url_rank_female = "https://api.imjad.cn/pixiv/v1/?type=rank&content=all&" +
            "mode=female&per_page=30&date=" + Common.getLastDay();
    public static final String url = "https://api.imjad.cn/pixiv/v1/?type=tags&per_page=81";
    public static final String illust_item = "https://api.imjad.cn/pixiv/v1/?type=illust&id=";

    //创建网络连接并且设置回调
    public static void sendOkhttpRequest(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }

    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    //接收时间戳，格式化时间并返回
    public static String getTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        if (time.length() == 13) {
            return sdf.format(Long.parseLong(time));
        }
        if (time.length() == 10) {
            return sdf.format(new Date(Integer.parseInt(time) * 1000L));
        }
        return "没有日期数据哦";
    }

    //得到当前时间回退两天的日期
    public static String getLastDay() {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DATE, -2);
        Date today = now.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(today);
    }

    //快速打LOG
    public static <T> void showLog(T t) {
        Log.d("a line of my log", String.valueOf(t));
    }

    //返回分页的页数
    public static int getPageCount(String itemCount) {
        if (Integer.valueOf(itemCount) < 20) {
            return 1;
        } else if ((Integer.valueOf(itemCount) / 20 < 20) && (Integer.valueOf(itemCount) / 20 >= 1)) {
            return Integer.valueOf(itemCount) / 20;
        } else {
            return 20;
        }
    }

    public static String getRankType() {
        switch (Reference.sFragmentPixivLeft.currentDataType) {
            case 0:
                return "日榜";
            case 1:
                return "周榜";
            case 2:
                return "月榜";
            case 3:
                return "新人";
            case 4:
                return "原创";
            case 5:
                return "男性向";
            default:
                return "女性向";
        }
    }

    public static void refreshAlbum(Context context, File file) {
        context.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(file)));
    }

}
