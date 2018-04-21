package com.example.administrator.essim.activities;

import com.example.administrator.essim.R;
import com.stephentuso.welcome.BasicPage;
import com.stephentuso.welcome.TitlePage;
import com.stephentuso.welcome.WelcomeActivity;
import com.stephentuso.welcome.WelcomeConfiguration;

public class MyWelcomeActivity extends WelcomeActivity {
    @Override
    protected WelcomeConfiguration configuration() {
        return new WelcomeConfiguration.Builder(this)
                .defaultBackgroundColor(R.color.colorPrimary)
                .page(new TitlePage(R.drawable.ic_equalizer_black_24dp,
                        "Pixiv 图片排行榜")
                )
                .page(new BasicPage(R.drawable.ic_card_giftcard_black_24dp,
                        "每日更新数据",
                        "海量图片资源")
                        .background(R.color.colorAccent)
                )
                .page(new BasicPage(R.drawable.ic_edit_black_24dp,
                        "搜集我的一言",
                        "start it now!")
                        .background(R.color.light_pink)
                )
                .swipeToDismiss(true)
                .build();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
