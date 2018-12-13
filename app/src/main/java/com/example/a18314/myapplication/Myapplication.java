package com.example.a18314.myapplication;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;

public class Myapplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        EMOptions options = new EMOptions();
// 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        EaseUI.getInstance().init(getApplicationContext(), options);

    }
}
