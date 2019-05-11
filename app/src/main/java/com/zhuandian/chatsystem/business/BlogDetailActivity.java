package com.zhuandian.chatsystem.business;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuandian.base.BaseActivity;
import com.zhuandian.chatsystem.R;
import com.zhuandian.chatsystem.adapter.UserCommentAdapter;
import com.zhuandian.chatsystem.business.chat.bean.User;
import com.zhuandian.chatsystem.business.chat.ui.ChatActivity;
import com.zhuandian.chatsystem.business.util.MyUtils;
import com.zhuandian.chatsystem.entity.BlogComment;
import com.zhuandian.chatsystem.entity.BlogEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class BlogDetailActivity extends BaseActivity implements View.OnTouchListener {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_chat)
    TextView tvChat;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.comment_listview)
    RecyclerView commentRecyclewView;
    @BindView(R.id.comment_content)
    EditText commentContent;
    @BindView(R.id.submit_comment)
    TextView submitComment;
    @BindView(R.id.submit_comment_layout)
    LinearLayout submitCommentLayout;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.cl_root)
    CoordinatorLayout clRoot;
    private BlogEntity mDatas;
    private UserCommentAdapter userCommentAdapter;
    private List<BlogComment> commentDatas = new ArrayList<>();


    @Override
    protected int getLayoutId() {
        return R.layout.activity_blog_detail;
    }

    @Override
    protected void setUpView() {
        mDatas = (BlogEntity) getIntent().getSerializableExtra("item");
        getAllUserComment();  //得到所有参与该动态评论的用户信息和内容
        //给控件装值
        tvContent.setText(mDatas.getBlogContent());
        String createtTime[] = mDatas.getCreatedAt().split(" ");
        String currentTime[] = MyUtils.currentTime().split(" ");
        //判断创建时间跟当前时间是否同一天，是，只显示时间，不是，显示创建的日期，不显示时间
        if (createtTime[0].equals(currentTime[0])) {
            String createtTime1[] = createtTime[1].split(":");
            tvTime.setText("今天 " + createtTime1[0] + ":" + createtTime1[1]);
        } else {
            String createtTime1[] = createtTime[0].split("-");  //正则切割月份
            String createtTime2[] = createtTime[1].split(":");  //正则切割时间
            tvTime.setText(createtTime1[1] + "/" + createtTime1[2] + " " + createtTime2[0] + ":" + createtTime2[1]);

        }

    }


    /**
     * 绑定当前动态下的所有评论信息
     */
    private void getAllUserComment() {
        BmobQuery<BlogComment> query = new BmobQuery<BlogComment>();
        //用此方式可以构造一个BmobPointer对象。只需要设置objectId就行
        BlogEntity post = new BlogEntity();
        post.setObjectId(mDatas.getObjectId());   //得到当前动态的Id号，
        query.order("updatedAt");
        query.addWhereEqualTo("blogEntity", new BmobPointer(post));
        //希望同时查询该评论的发布者的信息，以及该帖子的作者的信息，这里用到上面`include`的并列对象查询和内嵌对象的查询
        query.include("author,BlogEntity.auther");
        query.findObjects(new FindListener<BlogComment>() {
            @Override
            public void done(List<BlogComment> objects, BmobException e) {
                if (e == null) {
                    //插入数据，通知列表更新
//                    commentDatas = objects;  直接赋值，因为前后绑定的apapter对应的list地址不是同一个，所以 notifyDataSetChanged无效，
                    commentDatas.clear();
                    commentDatas.addAll(objects); //把数据添加进集合
                    commentRecyclewView.setLayoutManager(new LinearLayoutManager(BlogDetailActivity.this));
                    userCommentAdapter = new UserCommentAdapter(BlogDetailActivity.this, commentDatas);
                    commentRecyclewView.setAdapter(userCommentAdapter);
                    userCommentAdapter.notifyDataSetChanged();
                } else {
                    Log.e("查询数据失败", "xiedongdong");
                }
            }
        });


    }

    /**
     * 得到动态相关的评论个数
     *
     * @param objectId
     * @return
     */
    private void setCommentCount(String objectId, final TextView countView) {
        BmobQuery<BlogComment> query = new BmobQuery<BlogComment>();
        //用此方式可以构造一个BmobPointer对象。只需要设置objectId就行
        BlogEntity post = new BlogEntity();
        post.setObjectId(objectId);   //得到当前动态的Id号，
        query.addWhereEqualTo("blogEntity", new BmobPointer(post));
        //希望同时查询该评论的发布者的信息，以及该帖子的作者的信息，这里用到上面`include`的并列对象查询和内嵌对象的查询
        query.include("author,blogEntity.auther");
        query.findObjects(new FindListener<BlogComment>() {

            @Override
            public void done(List<BlogComment> objects, BmobException e) {
                if (e == null) {
                    countView.setText(objects.size() + "");
                } else {
                    Log.e("查询数据失败", "xiedongdong");
                }
            }
        });

    }

    /**
     * 提交当前用户的评论，并且关联到该动态
     */
    private void submitUserComment() {
        String userComment = commentContent.getText().toString();  //得到用户输入框的评论内容
        if (!"".equals(userComment)) {
            User user = BmobUser.getCurrentUser(User.class);  //得到当前用户
            BlogEntity post = new BlogEntity();   //当前动态内容
            post.setObjectId(mDatas.getObjectId());  //得到当前的动态的id，与评论建立关联
            final BlogComment commentEntity = new BlogComment();
            commentEntity.setContent(userComment);
            commentEntity.setAuthor(user);
            commentEntity.setBlogEntity(post);
//            commentEntity.setImgUrl(user.getHeadImgUrl());   //头像从user实体中取，保证更换头像之后，动态更新
            commentEntity.save(new SaveListener<String>() {
                @SuppressLint("RestrictedApi")
                @Override
                public void done(String objectId, BmobException e) {
                    if (e == null) {
                        Toast.makeText(BlogDetailActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                        commentContent.setText(""); //清空输入框，防止用户二次评论时影响用户体验
                        commentRecyclewView.setVisibility(View.VISIBLE);
                        submitCommentLayout.setVisibility(View.INVISIBLE);
                        fab.setVisibility(View.VISIBLE);
                        getAllUserComment();  //重新加载一遍数据

                    } else {
                        Log.e("评论失败", "xiedongdong");
                    }
                }

            });
        } else {
            Toast.makeText(BlogDetailActivity.this, "评论内容不允许为空", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 当前view有能力处理事件
     *
     * @param v
     * @param event
     * @return true  截断事件的传递
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    @SuppressLint("RestrictedApi")
    @OnClick({R.id.submit_comment, R.id.fab, R.id.tv_chat})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit_comment:
                submitUserComment();
                break;
            case R.id.fab:
                commentRecyclewView.setVisibility(View.INVISIBLE);
                submitCommentLayout.setVisibility(View.VISIBLE);
                fab.setVisibility(View.INVISIBLE);
                break;
            case R.id.tv_chat:
                openChatPage();
                break;
        }
    }

    private void openChatPage() {
        if (BmobIM.getInstance().getCurrentStatus().getCode() != ConnectionStatus.CONNECTED.getCode()) {
            Toast.makeText(this, "IM服务器初始化失败，请重试...", Toast.LENGTH_SHORT).show();
            return;
        }

        //会话：创建一个常态会话入口，陌生人聊天
        BmobIMUserInfo bmobIMUserInfo = new BmobIMUserInfo(mDatas.getAuthor().getObjectId(), mDatas.getAuthor().getUsername(), mDatas.getAuthor().getAvatar());
        BmobIMConversation conversationEntrance = BmobIM.getInstance().startPrivateConversation(bmobIMUserInfo, null);
        Intent intent = new Intent(BlogDetailActivity.this, ChatActivity.class);
        intent.putExtra("c", conversationEntrance);
        startActivity(intent);
    }
}
