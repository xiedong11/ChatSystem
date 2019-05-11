package com.zhuandian.chatsystem.entity;

import com.zhuandian.chatsystem.business.chat.bean.User;

import cn.bmob.v3.BmobObject;

/**
 * desc :
 * author：xiedong
 * date：2019/5/11
 */
public class BlogComment extends BmobObject {

    private String content;
    private User author;
    private BlogEntity blogEntity;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public BlogEntity getBlogEntity() {
        return blogEntity;
    }

    public void setBlogEntity(BlogEntity blogEntity) {
        this.blogEntity = blogEntity;
    }
}
