package com.moemoe.lalala.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.di.components.DaggerJuBaoComponent;
import com.moemoe.lalala.di.modules.JuBaoModule;
import com.moemoe.lalala.model.entity.DelCommentEntity;
import com.moemoe.lalala.model.entity.ReportEntity;
import com.moemoe.lalala.presenter.JuBaoContract;
import com.moemoe.lalala.presenter.JuBaoPresenter;
import com.moemoe.lalala.utils.AndroidBug5497Workaround;
import com.moemoe.lalala.utils.ErrorCodeUtils;
import com.moemoe.lalala.utils.NetworkUtils;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.ViewUtils;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by yi on 2016/12/1.
 */

public class DeleteActivity extends BaseAppCompatActivity implements JuBaoContract.View, RadioGroup.OnCheckedChangeListener {
    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_CONTENT = "content";
    public static final String EXTRA_TARGET = "target";
    public static final String EXTRA_TYPE = "type";
    public static final String EXTRA_DOC_ID = "doc_id";
    public static final String EXTRA_POSITION = "position";
    public static final String EXTRA_PARENT_ID = "parent_id";

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_toolbar_title)
    TextView mTvTitle;
    private String mUuid;
    @BindView(R.id.tv_item_1)
    TextView mTvContent1;
    @BindView(R.id.tv_item_2)
    TextView mTvContent2;
    @BindView(R.id.rg_root)
    RadioGroup mRg;
    @BindView(R.id.rg_root_2)
    RadioGroup mRg2;
    @BindView(R.id.tv_jubao_go)
    TextView mBtnGo;
    @BindView(R.id.edt_content)
    EditText mEtContent;
    @Inject
    JuBaoPresenter mPresenter;
    private String mTarget;
    private String mType;
    private int mSelectType;//1 report 2.del 3.bag
    private String mDocId;
    private int position;
    private String mUserId;
    private Boolean changeGroup = false;
    private String parentId;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_jubao;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        ViewUtils.setStatusBarLight(getWindow(), $(R.id.top_view));
        AndroidBug5497Workaround.assistActivity(this);
        DaggerJuBaoComponent.builder()
                .juBaoModule(new JuBaoModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        mTvTitle.setText("删帖");
        mTvTitle.setVisibility(View.VISIBLE);
        if (getIntent() == null) {
            finish();
        }
        mUuid = getIntent().getStringExtra(UUID);
        parentId = getIntent().getStringExtra(EXTRA_PARENT_ID);
        String mName = getIntent().getStringExtra(EXTRA_NAME);
        String mContent = getIntent().getStringExtra(EXTRA_CONTENT);
        mSelectType = getIntent().getIntExtra(EXTRA_TYPE, 1);
        mDocId = getIntent().getStringExtra(EXTRA_DOC_ID);
        mTarget = getIntent().getStringExtra(EXTRA_TARGET);
        position = getIntent().getIntExtra(EXTRA_POSITION, -1);
        mUserId = getIntent().getStringExtra("userId");
        mTvContent1.setText(mName);
        mTvContent2.setText(mContent);
        mType = "色情";
        mBtnGo.setEnabled(true);
        mBtnGo.setText("完成");
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {

    }

    @Override
    protected void initListeners() {
        mIvBack.setVisibility(View.VISIBLE);
        mIvBack.setImageResource(R.drawable.btn_back_black_normal);
        mIvBack.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                finish();
            }
        });
        mRg.setOnCheckedChangeListener(this);
        mRg2.setOnCheckedChangeListener(this);
        mBtnGo.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                if (!NetworkUtils.checkNetworkAndShowError(DeleteActivity.this)) {
                    return;
                }
                createDialog();
                ReportEntity bean = new ReportEntity(mEtContent.getText().toString(), mType, mUuid, mTarget);
                mPresenter.deleteDocV2(mUuid, bean, position);

            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) mPresenter.release();
        super.onDestroy();
    }

    @Override
    public void onSuccess(Object o) {
    }

    @Override
    public void onDeleteDocV2Success(int position) {
        finalizeDialog();
        showToast(R.string.msg_jubao_success);
        Intent i = new Intent();
        i.putExtra(EXTRA_POSITION, position);
        setResult(RESULT_OK, i);
        finish();
    }

    @Override
    public void onFailure(int code, String msg) {
        finalizeDialog();
        ErrorCodeUtils.showErrorMsgByCode(DeleteActivity.this, code, msg);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioGroup != null && i > -1 && changeGroup == false) {
            if (radioGroup == mRg) {
                changeGroup = true;
                mRg2.clearCheck();
                changeGroup = false;
            } else if (radioGroup == mRg2) {
                changeGroup = true;
                mRg.clearCheck();
                changeGroup = false;
            }
            switch (i) {
                case R.id.cb_h:
                    mType = "色情";
                    break;
                case R.id.cb_mingan:
                    mType = "敏感";
                    break;
                case R.id.cb_manma:
                    mType = "谩骂";
                    break;
                case R.id.cb_ad:
                    mType = "广告";
                    break;
                case R.id.cb_shui:
                    mType = "水贴";
                    break;
            }
        }
    }
}
