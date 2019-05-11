package com.zhuandian.chatsystem.business.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuandian.base.BaseFragment;
import com.zhuandian.chatsystem.R;
import com.zhuandian.chatsystem.business.chat.bean.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * desc :
 * author：xiedong
 * date：2019/5/11
 */
public class MyFragment extends BaseFragment {
    @BindView(R.id.et_nick_name)
    EditText etNickName;
    @BindView(R.id.et_user_desc)
    EditText etUserDesc;
    @BindView(R.id.et_user_password)
    EditText etUserPassword;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    private User userEntity;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initView() {
        userEntity = BmobUser.getCurrentUser(User.class);
        etNickName.setText(userEntity.getNickName());
        etUserDesc.setText(userEntity.getUserInfo());
    }

    @OnClick(R.id.tv_submit)
    public void onClick() {
        if (!TextUtils.isEmpty(etNickName.getText().toString()) || !TextUtils.isEmpty(etUserDesc.getText().toString())) {
            userEntity.setNickName(etNickName.getText().toString());
            userEntity.setUserInfo(etUserDesc.getText().toString());
            userEntity.setPassword(etUserPassword.getText().toString());
            userEntity.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        Toast.makeText(actitity, "更新成功...", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(actitity, "昵称跟描述为必填项...", Toast.LENGTH_SHORT).show();
        }
    }
}
