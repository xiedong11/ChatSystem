package com.zhuandian.chatsystem.business.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhuandian.base.BaseFragment;
import com.zhuandian.chatsystem.R;
import com.zhuandian.chatsystem.adapter.BlogAdapter;
import com.zhuandian.chatsystem.business.BlogDetailActivity;
import com.zhuandian.chatsystem.business.NewBlogActivity;
import com.zhuandian.chatsystem.entity.BlogEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * desc :
 * author：xiedong
 * date：2019/5/11
 */
public class BlogFragment extends BaseFragment {
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.tv_new_blog)
    TextView tvNewBlog;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_blog;
    }

    @Override
    protected void initView() {
        initData();
    }

    private void initData() {
        BmobQuery<BlogEntity> query = new BmobQuery<>();
        query.order("-updatedAt")
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


    @OnClick(R.id.tv_new_blog)
    public void onClick() {
        startActivityForResult(new Intent(actitity, NewBlogActivity.class), 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            initData();
        }
    }
}
