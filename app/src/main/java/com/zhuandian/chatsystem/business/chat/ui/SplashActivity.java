package com.zhuandian.chatsystem.business.chat.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.zhuandian.chatsystem.ChatMainActivity;
import com.zhuandian.chatsystem.R;
import com.zhuandian.chatsystem.business.chat.base.BaseActivity;
import com.zhuandian.chatsystem.business.chat.bean.User;
import com.zhuandian.chatsystem.business.chat.model.UserModel;


/**
 * 启动界面
 *
 * @author :smile
 * @project:SplashActivity
 * @date :2016-01-15-18:23
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_im);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                User user = UserModel.getInstance().getCurrentUser();
                if (user == null) {
                    startActivity(LoginActivity.class, null, true);
                } else {
                    startActivity(ChatMainActivity.class, null, true);
                }
            }
        }, 1000);

    }
}
