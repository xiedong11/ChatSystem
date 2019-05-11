package com.zhuandian.chatsystem;

import android.app.Application;

import com.zhuandian.chatsystem.business.chat.DemoMessageHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import cn.bmob.newim.BmobIM;

/**
 * desc :
 * author：xiedong
 * date：2019/5/11
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //TODO 集成：1.8、初始化IM SDK，并注册消息接收器
        if (getApplicationInfo().packageName.equals(getMyProcessName())){
            BmobIM.init(this);
            BmobIM.registerDefaultMessageHandler(new DemoMessageHandler());
        }
    }

    /**
     * 获取当前运行的进程名
     * @return
     */
    public static String getMyProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
