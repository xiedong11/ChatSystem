package com.zhuandian.chatsystem;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.zhuandian.base.BaseActivity;
import com.zhuandian.base.BaseFragment;
import com.zhuandian.chatsystem.adapter.HomePageAdapter;
import com.zhuandian.chatsystem.business.chat.bean.User;
import com.zhuandian.chatsystem.business.chat.event.RefreshEvent;
import com.zhuandian.chatsystem.business.chat.ui.fragment.ContactFragment;
import com.zhuandian.chatsystem.business.chat.ui.fragment.ConversationFragment;
import com.zhuandian.chatsystem.business.chat.util.IMMLeaks;
import com.zhuandian.chatsystem.business.fragment.BlogFragment;
import com.zhuandian.chatsystem.business.fragment.HomeFragment;
import com.zhuandian.chatsystem.business.fragment.MyFragment;


import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

/**
 * 项目首页
 */
public class ChatMainActivity extends BaseActivity {


    @BindView(R.id.vp_home)
    ViewPager vpHome;
    @BindView(R.id.tab_bottom)
    BottomNavigationView tabBottom;
    private static final int PAGE_HOME = 0;
    private static final int PAGE_BLOG = 1;
    private static final int PAGE_CHAT = 2;
    private static final int PAGE_MY = 3;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat_main;
    }

    @Override
    protected void setUpView() {

        initChatSystem();


        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new HomeFragment());
        fragmentList.add(new BlogFragment());
        fragmentList.add(new ConversationFragment());
        fragmentList.add(new MyFragment());

        vpHome.setAdapter(new HomePageAdapter(getSupportFragmentManager(), fragmentList));
        vpHome.setOffscreenPageLimit(4);

        vpHome.setCurrentItem(PAGE_HOME);
        initBottomTab();
    }

    private void initChatSystem() {
        final User user = BmobUser.getCurrentUser(User.class);
        //TODO 连接：3.1、登录成功、注册成功或处于登录状态重新打开应用后执行连接IM服务器的操作
        //判断用户是否登录，并且连接状态不是已连接，则进行连接操作
        if (!TextUtils.isEmpty(user.getObjectId()) &&
                BmobIM.getInstance().getCurrentStatus().getCode() != ConnectionStatus.CONNECTED.getCode()) {
            BmobIM.connect(user.getObjectId(), new ConnectListener() {
                @Override
                public void done(String uid, BmobException e) {
                    if (e == null) {
                        //服务器连接成功就发送一个更新事件，同步更新会话及主页的小红点
                        //TODO 会话：2.7、更新用户资料，用于在会话页面、聊天页面以及个人信息页面显示
                        BmobIM.getInstance().
                                updateUserInfo(new BmobIMUserInfo(user.getObjectId(),
                                        user.getUsername(), user.getAvatar()));
                        EventBus.getDefault().post(new RefreshEvent());
                    } else {
                        Toast.makeText(ChatMainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            //TODO 连接：3.3、监听连接状态，可通过BmobIM.getInstance().getCurrentStatus()来获取当前的长连接状态
            BmobIM.getInstance().setOnConnectStatusChangeListener(new ConnectStatusChangeListener() {
                @Override
                public void onChange(ConnectionStatus status) {
                    Toast.makeText(ChatMainActivity.this, status.getMsg(), Toast.LENGTH_SHORT).show();
                    Logger.i(BmobIM.getInstance().getCurrentStatus().getMsg());
                }
            });
        }
        //解决leancanary提示InputMethodManager内存泄露的问题
        IMMLeaks.fixFocusedViewLeak(getApplication());
    }

    private void initBottomTab() {
        vpHome.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabBottom.getMenu().getItem(position).setChecked(true);
            }
        });
        tabBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.tab_home:
                        vpHome.setCurrentItem(PAGE_HOME);
                        break;
                    case R.id.tab_blog:
                        vpHome.setCurrentItem(PAGE_BLOG);
                        break;
                    case R.id.tab_chat:
                        vpHome.setCurrentItem(PAGE_CHAT);
                        break;
                    case R.id.tab_my:
                        vpHome.setCurrentItem(PAGE_MY);
                        break;
                }
                return true;
            }
        });
    }

}
