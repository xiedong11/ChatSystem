package com.zhuandian.chatsystem.business;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zhuandian.base.BaseActivity;
import com.zhuandian.chatsystem.R;
import com.zhuandian.chatsystem.business.chat.bean.User;
import com.zhuandian.chatsystem.entity.BlogEntity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class NewBlogActivity extends BaseActivity {

    @BindView(R.id.blog_title)
    EditText blogTitle;
    @BindView(R.id.blog_content)
    EditText blogContent;
    @BindView(R.id.commit_content)
    LinearLayout commitContent;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_blog;
    }

    @Override
    protected void setUpView() {

    }


    @OnClick(R.id.commit_content)
    public void onClick() {
        String blogTitleStr = blogTitle.getText().toString();
        String blogContentStr = blogContent.getText().toString();
        if (TextUtils.isEmpty(blogTitleStr) || TextUtils.isEmpty(blogContentStr)) {
            Toast.makeText(this, "标题跟内容都不允许为空...", Toast.LENGTH_SHORT).show();
        } else {
            BlogEntity blogEntity = new BlogEntity();
            blogEntity.setAuthor(BmobUser.getCurrentUser(User.class));
            blogEntity.setBlogTitle(blogTitleStr);
            blogEntity.setBlogContent(blogContentStr);
            blogEntity.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        Toast.makeText(NewBlogActivity.this, "发布成功...", Toast.LENGTH_SHORT).show();
                        setResult(1);
                        NewBlogActivity.this.finish();
                    }
                }
            });
        }
    }
}
