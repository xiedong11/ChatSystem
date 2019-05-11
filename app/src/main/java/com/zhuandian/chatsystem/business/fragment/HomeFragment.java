package com.zhuandian.chatsystem.business.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.zhuandian.base.BaseFragment;
import com.zhuandian.chatsystem.R;
import com.zhuandian.chatsystem.adapter.BlogAdapter;
import com.zhuandian.chatsystem.business.BlogDetailActivity;
import com.zhuandian.chatsystem.business.util.GlideImageLoader;
import com.zhuandian.chatsystem.entity.BlogEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * desc :
 * author：xiedong
 * date：2019/5/11
 */
public class HomeFragment extends BaseFragment {
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.rv_list)
    RecyclerView rvList;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        initBanner();
        initHotBlog();
    }

    private void initHotBlog() {
        BmobQuery<BlogEntity> query = new BmobQuery<>();
        query.order("-updatedAt")
                .setLimit(5)
                .include("author")
                .findObjects(new FindListener<BlogEntity>() {
                    @Override
                    public void done(List<BlogEntity> list, BmobException e) {
                        BlogAdapter adapter = new BlogAdapter(list, actitity);
                        rvList.setAdapter(adapter);
                        rvList.setLayoutManager(new LinearLayoutManager(actitity));
                        adapter.setOnItemClickListener(new BlogAdapter.OnItemClickListener() {
                            @Override
                            public void onClick(BlogEntity blogEntity) {
                                //跳转到详情页
                                Intent intent = new Intent(actitity, BlogDetailActivity.class);
                                intent.putExtra("item", blogEntity);
                                startActivity(intent);
                            }
                        });
                    }
                });

    }


    private void initBanner() {
        List<Integer> images = new ArrayList<>();
        images.add(R.drawable.user_1_1);
        images.add(R.drawable.user_1_2);

        //设置banner样式
        banner.setBannerStyle(BannerConfig.NUM_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(images);
        //设置轮播时间
        banner.setDelayTime(2000);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }


}
