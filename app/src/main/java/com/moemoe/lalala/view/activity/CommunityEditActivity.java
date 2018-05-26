package com.moemoe.lalala.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.di.components.DaggerSaveDescribeComponent;
import com.moemoe.lalala.di.modules.SavaDescribeModule;
import com.moemoe.lalala.presenter.SavaDescribeContract;
import com.moemoe.lalala.presenter.SavaDescribeyPresenter;
import com.moemoe.lalala.utils.AndroidBug5497Workaround;
import com.moemoe.lalala.utils.ToastUtils;
import com.moemoe.lalala.utils.ViewUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Sora on 2018/3/2.
 */

public class CommunityEditActivity extends BaseAppCompatActivity implements SavaDescribeContract.View {
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_toolbar_title)
    TextView mTvToolbarTitle;
    @BindView(R.id.tv_right_menu)
    TextView mTvRightMenu;
    @BindView(R.id.edit_community)
    EditText mEdCommunity;
    @BindView(R.id.tv_character_num)
    TextView mTvCharacterNum;

    @Inject
    SavaDescribeyPresenter mPresenter;

    private int maxLength = 50;
    private String id;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_community_edit;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        ViewUtils.setStatusBarLight(getWindow(), $(R.id.top_view));
        AndroidBug5497Workaround.assistActivity(this);
        DaggerSaveDescribeComponent.builder()
                .savaDescribeModule(new SavaDescribeModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);


        mEdCommunity.setText(getIntent().getStringExtra("text"));
        id = getIntent().getStringExtra("uuid");
        mIvBack.setVisibility(View.VISIBLE);
        mTvRightMenu.setVisibility(View.VISIBLE);
        mTvToolbarTitle.setText("社区简介");
        mTvRightMenu.setText("保存");
        mTvRightMenu.setTextColor(getResources().getColor(R.color.main_cyan));

        mEdCommunity.addTextChangedListener(new TextWatcher() {

            private int selectionEnd;
            private int selectionStart;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mEdCommunity.setSelection(charSequence.length());
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mEdCommunity.setSelection(charSequence.length());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int length = maxLength - editable.length();
                selectionStart = mEdCommunity.getSelectionStart();
                selectionEnd = mEdCommunity.getSelectionEnd();
                mTvCharacterNum.setText(length + "");
                if (editable.length() > maxLength) {
                    editable.delete(selectionStart - 1, selectionEnd);
                    mEdCommunity.setText(editable);
                }
                mEdCommunity.setSelection(editable.length());
            }
        });
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {

    }

    @OnClick({R.id.iv_back, R.id.tv_right_menu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right_menu:
                String string = mEdCommunity.getText().toString();
                if (TextUtils.isEmpty(string)) {
                    showToast("简介不可为空");
                } else {
                    mPresenter.loadSaveDescribe(id, string);
                }
                break;
        }
    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onFailure(int code, String msg) {
        showToast("修改失败");
    }

    @Override
    public void onSaveDescribeSuccess() {
        showToast("保存成功");
        Intent intent = new Intent();
        intent.putExtra("text", mEdCommunity.getText().toString() + "");
        setResult(RESULT_OK, intent);
        ToastUtils.showLongToast(this, "保存成功");
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.release();
        }
        ToastUtils.cancelToast();
    }
}
