package com.zhuandian.chatsystem.business.fragment;

import android.content.Intent;
import android.view.View;
import com.zhuandian.base.BaseFragment;
import com.zhuandian.chatsystem.R;
import com.zhuandian.chatsystem.business.PersonalDataActivity;
import com.zhuandian.chatsystem.business.chat.model.UserModel;
import com.zhuandian.chatsystem.business.chat.ui.LoginActivity;
import butterknife.OnClick;
import cn.bmob.newim.BmobIM;

/**
 * desc :
 * author：xiedong
 * date：2019/5/11
 */
public class MyFragment extends BaseFragment {


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initView() {

    }


    @OnClick({R.id.tv_personal_data, R.id.tv_logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_personal_data:
                startActivity(new Intent(actitity, PersonalDataActivity.class));
                break;
            case R.id.tv_logout:
                logout();
                break;
        }
    }

    private void logout() {
        UserModel.getInstance().logout();
        //TODO 连接：3.2、退出登录需要断开与IM服务器的连接
        BmobIM.getInstance().disConnect();
        getActivity().finish();
        startActivity(new Intent(actitity, LoginActivity.class));
    }
}
