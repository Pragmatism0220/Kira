package com.moemoe.lalala.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.di.components.DaggerRecommendTagComponent;
import com.moemoe.lalala.di.modules.RecommendTagModule;
import com.moemoe.lalala.model.entity.FolderType;
import com.moemoe.lalala.model.entity.RecommendTagEntity;
import com.moemoe.lalala.presenter.RecommendTagContract;
import com.moemoe.lalala.presenter.RecommendTagPresenter;
import com.moemoe.lalala.utils.AndroidBug5497Workaround;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.TagUtils;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.view.adapter.RecommendTagAdapter;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yi on 2017/12/19.
 */

public class RecommendTagActivity extends BaseAppCompatActivity implements RecommendTagContract.View {

    private static final int MAX_LENGTH = 10;
    @BindView(R.id.tv_toolbar_title)
    TextView mTvTitle;
    @BindView(R.id.tv_menu)
    TextView mTvSave;
    @BindView(R.id.tv_left_menu)
    TextView mTvDrop;
    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.list)
    RecyclerView mList;
    @BindView(R.id.tv_recommend_notice)
    TextView mTvNotic;
    @BindView(R.id.tv_add_user)
    TextView mTvAddUser;
    @BindView(R.id.label_1_root)
    View mTag1Root;
    @BindView(R.id.label_2_root)
    View mTag2Root;
    @BindView(R.id.label_3_root)
    View mTag3Root;
    @BindView(R.id.tv_label_add_1)
    TextView mTvTag1;
    @BindView(R.id.tv_label_add_2)
    TextView mTvTag2;
    @BindView(R.id.tv_label_add_3)
    TextView mTvTag3;

    @Inject
    RecommendTagPresenter mPresenter;

    private RecommendTagAdapter mAdapter;
    private ArrayList<String> mRes;
    private String mFolderType;
    private ArrayList<String> mTagsList;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_search_tag;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        DaggerRecommendTagComponent.builder()
                .recommendTagModule(new RecommendTagModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        ViewUtils.setStatusBarLight(getWindow(), $(R.id.top_view));
        AndroidBug5497Workaround.assistActivity(this);
        mFolderType = getIntent().getStringExtra("folderType");
        mTagsList = getIntent().getStringArrayListExtra("tags");
        if (TextUtils.isEmpty(mFolderType)) {
            finish();
            return;
        }
        String str = "";
        if (mFolderType.equals(FolderType.ZH.toString())) {
            str = "推荐综合标签";
        } else if (mFolderType.equals(FolderType.MH.toString())) {
            str = "推荐漫画标签";
        } else if (mFolderType.equals(FolderType.TJ.toString())) {
            str = "推荐图集标签";
        } else if (mFolderType.equals(FolderType.XS.toString())) {
            str = "推荐小说标签";
        } else if ("DOC".equals(mFolderType)) {
            str = "推荐帖子标签";
        }
        mTvNotic.setText(str);


        //填充标签
        if (mTagsList != null && mTagsList.size() != 0) {
            for (int i = 0; i < mTagsList.size(); i++) {
                if (i == 0) {
                    if (mTag1Root.getVisibility() == View.GONE) {
                        mTag1Root.setVisibility(View.VISIBLE);
                        mTvTag1.setText(mTagsList.get(0));
                        TagUtils.setBackGround(mTagsList.get(0), mTvTag1);
                    }
                } else if (i == 1) {
                    if (mTag2Root.getVisibility() == View.GONE) {
                        mTag2Root.setVisibility(View.VISIBLE);
                        mTvTag2.setText(mTagsList.get(1));
                        TagUtils.setBackGround(mTagsList.get(1), mTvTag2);
                    }
                } else {
                    mTag3Root.setVisibility(View.VISIBLE);
                    mTvTag3.setText(mTagsList.get(2));
                    TagUtils.setBackGround(mTagsList.get(2), mTvTag3);
                }
            }
            mRes = mTagsList;
        } else {
            mRes = new ArrayList<>();
        }
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = s.toString();
                if (!TextUtils.isEmpty(content)) {
                    mPresenter.loadKeyWordTag(content);
                    mTvNotic.setVisibility(View.GONE);
                    mTvAddUser.setVisibility(View.VISIBLE);
                } else {
                    mPresenter.loadRecommendTag(mFolderType);
                    mTvNotic.setVisibility(View.VISIBLE);
                    mTvAddUser.setVisibility(View.GONE);
                }
            }
        });

        mList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecommendTagAdapter();
        mList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                addToTop(mAdapter.getItem(position).getWord());
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        mPresenter.loadRecommendTag(mFolderType);
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {
        mTvTitle.setText("添加标签");
        mTvSave.setVisibility(View.VISIBLE);
        mTvSave.setText(getString(R.string.label_done));
        ViewUtils.setRightMargins(mTvSave, (int) getResources().getDimension(R.dimen.x36));
        mTvSave.setTextColor(ContextCompat.getColor(this, R.color.main_cyan));
        mTvSave.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                Intent intent = new Intent();
                intent.putStringArrayListExtra("tags", mRes);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        mTvDrop.setVisibility(View.VISIBLE);
        ViewUtils.setLeftMargins(mTvDrop, (int) getResources().getDimension(R.dimen.x36));
        mTvDrop.setTextColor(ContextCompat.getColor(this, R.color.black_1e1e1e));
        mTvDrop.setText(getString(R.string.label_give_up));
        mTvDrop.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                finish();
            }
        });
    }

    @OnClick({R.id.iv_clear, R.id.tv_add_user, R.id.iv_1_close, R.id.iv_2_close, R.id.iv_3_close})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_clear:
                mEtSearch.setText("");
                mPresenter.loadRecommendTag(mFolderType);
                break;
            case R.id.tv_add_user:
                addToTop(mEtSearch.getText().toString());
                break;
            case R.id.iv_1_close:
                mRes.remove(mTvTag1.getText().toString());
//                mRes.removeInHouse(0);
//                if (mTag3Root.getVisibility() == View.VISIBLE) {
//                    mTag3Root.setVisibility(View.GONE);
//                    mTvTag1.setText(mTvTag2.getText());
//                    mTvTag2.setText(mTvTag3.getText());
//                    TagUtils.setBackGround(mTvTag2.getText().toString(), mTvTag1);
//                    TagUtils.setBackGround(mTvTag3.getText().toString(), mTvTag2);
//                } else if (mTag2Root.getVisibility() == View.VISIBLE) {
//                    mTag2Root.setVisibility(View.GONE);
//                    mTvTag1.setText(mTvTag2.getText());
//                    TagUtils.setBackGround(mTvTag2.getText().toString(), mTvTag1);
//                } else {
                mTag1Root.setVisibility(View.GONE);
                mTvTag1.setText("");
//                }
                break;
            case R.id.iv_2_close:
//                mRes.removeInHouse(1);
                mRes.remove(mTvTag2.getText().toString());
//                if (mTag3Root.getVisibility() == View.VISIBLE) {
//                    mTag3Root.setVisibility(View.GONE);
//                    mTvTag2.setText(mTvTag3.getText());
//                    TagUtils.setBackGround(mTvTag3.getText().toString(), mTvTag2);
//                } else {
                mTag2Root.setVisibility(View.GONE);
                mTvTag2.setText("");
//                }
                break;
            case R.id.iv_3_close:
//                mRes.removeInHouse(3);
                mRes.remove(mTvTag3.getText().toString());
                mTag3Root.setVisibility(View.GONE);
                break;
        }
    }

    private void addToTop(String content) {
        if (mRes.size() >= 3) {
            showToast("最多只能添加3个标签");
        } else {
            if (TextUtils.isEmpty(content)) {
                return;
            }
            if (mRes.contains(content)) {
                showToast("已经存在该标签");
                return;
            }
            mRes.add(content);
            if (mTag1Root.getVisibility() == View.GONE) {
                mTag1Root.setVisibility(View.VISIBLE);
                mTvTag1.setText(content);
                TagUtils.setBackGround(content, mTvTag1);
            } else if (mTag2Root.getVisibility() == View.GONE) {
                mTag2Root.setVisibility(View.VISIBLE);
                mTvTag2.setText(content);
                TagUtils.setBackGround(content, mTvTag2);
            } else {
                mTag3Root.setVisibility(View.VISIBLE);
                mTvTag3.setText(content);
                TagUtils.setBackGround(content, mTvTag3);
            }
        }
    }

    @Override
    protected void initListeners() {
        // 控制输入框最多输入10个字符长度（五个汉字）
        mEtSearch.setFilters(new InputFilter[]{new InputFilter() {

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                // 输入内容是否超过设定值，最多输入五个汉字10个字符
                if (getTextLength(dest.toString()) + getTextLength(source.toString()) > MAX_LENGTH) {
                    // 输入框内已经有10个字符则返回空字符
                    if (getTextLength(dest.toString()) >= 10) {
                        return "";
                        // 如果输入框内没有字符，且输入的超过了10个字符，则截取前五个汉字
                    } else if (getTextLength(dest.toString()) == 0) {
                        return source.toString().substring(0, 5);
                    } else {
                        // 输入框已有的字符数为双数还是单数
                        if (getTextLength(dest.toString()) % 2 == 0) {
                            return source.toString().substring(0, 5 - (getTextLength(dest.toString()) / 2));
                        } else {
                            return source.toString().substring(0, 5 - (getTextLength(dest.toString()) / 2 + 1));
                        }
                    }
                }
                return null;
            }
        }});
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onFailure(int code, String msg) {

    }

    @Override
    public void onLoadRecommendTagSuccess(ArrayList<RecommendTagEntity> entities) {
        mAdapter.setList(entities);
    }

    @Override
    public void onLoadKeyWordTagSuccess(ArrayList<RecommendTagEntity> entities) {
        mAdapter.setList(entities);
    }

    /**
     * 获取字符数量 汉字占2个，英文占一个
     *
     * @param text
     * @return
     */
    public static int getTextLength(String text) {
        int length = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) > 255) {
                length += 2;
            } else {
                length++;
            }
        }
        return length;
    }

}
