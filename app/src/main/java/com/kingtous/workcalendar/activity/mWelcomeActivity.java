package com.kingtous.workcalendar.activity;

import android.os.Bundle;
import android.os.Handler;

import com.kingtous.workcalendar.R;
import com.stephentuso.welcome.BackgroundColor;
import com.stephentuso.welcome.TitlePage;
import com.stephentuso.welcome.WelcomeActivity;
import com.stephentuso.welcome.WelcomeConfiguration;

public class mWelcomeActivity extends WelcomeActivity {
    private Handler mHandler=new Handler();
    // 启动图
    @Override
    protected WelcomeConfiguration configuration() {
        return new WelcomeConfiguration.Builder(this)
                .defaultBackgroundColor(R.color.app_primary_color)
                .page(new TitlePage(R.drawable.welcome_screen,
                        "工作日历")
                )
                .swipeToDismiss(true)
                .showActionBarBackButton(false)
                .showNextButton(false)
                .showNextButton(false)
                .useCustomDoneButton(true)
                .build();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },2000);
    }
}
