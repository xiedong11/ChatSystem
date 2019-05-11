package com.zhuandian.chatsystem;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.zhuandian.base.BaseActivity;
import com.zhuandian.base.BaseFragment;
import com.zhuandian.chatsystem.adapter.HomePageAdapter;
import com.zhuandian.chatsystem.business.chat.ui.fragment.ContactFragment;
import com.zhuandian.chatsystem.business.chat.ui.fragment.ConversationFragment;
import com.zhuandian.chatsystem.business.fragment.BlogFragment;
import com.zhuandian.chatsystem.business.fragment.HomeFragment;
import com.zhuandian.chatsystem.business.fragment.MyFragment;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

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
