package com.rae.cnblogs.user.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.UICompat;
import com.rae.cnblogs.basic.BasicFragment;
import com.rae.cnblogs.basic.rx.AndroidObservable;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.api.IUserApi;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.user.R;
import com.rae.cnblogs.user.R2;

import butterknife.BindView;

/**
 * Created by rae on 2018/12/19.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class PersonalAccountFragment extends BasicFragment implements View.OnClickListener {

    @BindView(R2.id.et_text)
    EditText mTextView;
    @BindView(R2.id.tv_sub_title)
    TextView mTipsView;
    TextView mSaveView;

    private IUserApi mUserApi;
    private String mOldName;

    @Override
    protected int getLayoutId() {
        return R.layout.fm_personal_account;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserApi = CnblogsApiFactory.getInstance(getContext()).getUserApi();
    }

    @Override
    public void onResume() {
        super.onResume();
        UserInfoBean userInfo = UserProvider.getInstance().getLoginUserInfo();
        if (userInfo != null) {
            mOldName = userInfo.getAccount();
            mTextView.setText(userInfo.getAccount());
            mTextView.setSelection(mTextView.length());
            mTextView.setHint("请输入新的账号");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentActivity activity = getActivity();
        if (activity == null) return;
        mSaveView = activity.findViewById(R.id.btn_save);
        mSaveView.setOnClickListener(this);
        mTipsView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        final String text = mTextView.getText().toString();
        if (TextUtils.isEmpty(text)) return;

        mSaveView.setEnabled(false);
        mSaveView.setText("保存中");
        AndroidObservable.create(mUserApi.updateAccount(mOldName, text))
                .with(this)
                .subscribe(new ApiDefaultObserver<String>() {
                    @Override
                    protected void onError(String message) {
                        mSaveView.setEnabled(true);
                        mSaveView.setText(R.string.save);
                        UICompat.toastInCenter(getContext(), message);
                    }

                    @Override
                    protected void accept(String message) {
                        if (!message.contains("成功")) {
                            onError(message);
                            return;
                        }
                        UserProvider.getInstance().logout(); // 退出登录
                        AppRoute.finish();
                        AppRoute.routeToActionResult(getContext(), "账号修改成功");

                    }
                });
    }
}
