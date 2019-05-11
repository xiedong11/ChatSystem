package com.zhuandian.chatsystem.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.zhuandian.base.BaseAdapter;
import com.zhuandian.base.BaseViewHolder;
import com.zhuandian.chatsystem.R;
import com.zhuandian.chatsystem.entity.BlogEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * desc :
 * author：xiedong
 * date：2019/5/11
 */
public class BlogAdapter extends BaseAdapter<BlogEntity, BaseViewHolder> {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_time)
    TextView tvTime;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public BlogAdapter(List<BlogEntity> mDatas, Context context) {
        super(mDatas, context);
    }

    @Override
    protected void converData(BaseViewHolder myViewHolder, final BlogEntity blogEntity, int position) {
        ButterKnife.bind(this, myViewHolder.itemView);
        tvName.setText("作者：" + blogEntity.getAuthor().getUsername());
        tvTime.setText(blogEntity.getCreatedAt());
        tvTitle.setText(blogEntity.getBlogTitle());
        tvContent.setText(blogEntity.getBlogContent());
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(blogEntity);
                }
            }
        });
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_blog;
    }

    public interface OnItemClickListener {
        void onClick(BlogEntity blogEntity);
    }
}

