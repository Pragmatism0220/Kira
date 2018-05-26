package com.moemoe.lalala.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.di.components.DaggerMsgSetUpComponent;
import com.moemoe.lalala.di.modules.MsgSetUpModule;
import com.moemoe.lalala.model.entity.AuthorInfo;
import com.moemoe.lalala.presenter.MsgSetUpContract;
import com.moemoe.lalala.presenter.MsgSetUpPresenter;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.utils.ViewUtils;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by Sora on 2018/3/26.
 */

public class MsgSetUpActivity extends BaseAppCompatActivity implements MsgSetUpContract.View {
    @BindView(R.id.iv_indicate_img)
    ImageView mIvMian;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_toolbar_title)
    TextView mTvTitle;

    @Inject
    MsgSetUpPresenter mPresenter;

    private boolean isFalg;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_msg_set_up;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        ViewUtils.setStatusBarLight(getWindow(), $(R.id.top_view));

        DaggerMsgSetUpComponent.builder()
                .msgSetUpModule(new MsgSetUpModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);

        isFalg= PreferenceUtils.getAuthorInfo().getShield();
        mIvMian.setSelected(!isFalg);
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {
        mIvBack.setVisibility(View.VISIBLE);
        mTvTitle.setText("消息设置");
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void initListeners() {
        mIvMian.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                mPresenter.loadGroupShield(isFalg);
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onFailure(int code, String msg) {

    }

    @Override
    public void onloadGroupShield(boolean falg) {
        mIvMian.setSelected(!falg);
        isFalg = falg;
        AuthorInfo authorInfo = new AuthorInfo();
        authorInfo.setShield(falg);
        PreferenceUtils.setAuthorInfo(authorInfo);
        if (falg) {
            showToast("设置免打扰成功");
        } else {
            showToast("取消免打扰成功");
        }
    }
}
