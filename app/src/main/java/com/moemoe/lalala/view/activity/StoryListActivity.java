package com.moemoe.lalala.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.di.components.DaggerStoryListComponent;
import com.moemoe.lalala.di.modules.StoryListModule;
import com.moemoe.lalala.model.entity.StoryListEntity;
import com.moemoe.lalala.presenter.StoryListContract;
import com.moemoe.lalala.presenter.StoryListPresenter;
import com.moemoe.lalala.utils.AndroidBug5497Workaround;
import com.moemoe.lalala.utils.ErrorCodeUtils;
import com.moemoe.lalala.utils.StoryListDecoration;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.view.adapter.StoryListAdapter;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;
import com.moemoe.lalala.view.widget.recycler.PullAndLoadView;
import com.moemoe.lalala.view.widget.recycler.PullCallback;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by Sora on 2018/4/8.
 */

public class StoryListActivity extends BaseAppCompatActivity implements StoryListContract.View {
    private int RESULT_BACK;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_toolbar_title)
    TextView mTvTitle;
    @BindView(R.id.rv_list)
    PullAndLoadView mListDocs;

    private String name;
    private boolean isLoading = true;
    @Inject
    StoryListPresenter mPresenter;
    private StoryListAdapter mAdapter;
    private String id;
    private int status;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_bar_list;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        ViewUtils.setStatusBarLight(getWindow(), $(R.id.top_view));
        AndroidBug5497Workaround.assistActivity(this);
        DaggerStoryListComponent.builder()
                .storyListModule(new StoryListModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        status = getIntent().getIntExtra("status", 0);
        setBack(R.drawable.btn_phone_back);


        mListDocs.getSwipeRefreshLayout().setColorSchemeResources(R.color.main_light_cyan, R.color.main_cyan);
        mListDocs.setLoadMoreEnabled(false);
        mListDocs.setLayoutManager(new LinearLayoutManager(this));
        mListDocs.setBackgroundColor(ContextCompat.getColor(this, R.color.bg_f6f6f6));
        mAdapter = new StoryListAdapter();
        mListDocs.getRecyclerView().addItemDecoration(new StoryListDecoration((int) getResources().getDimension(R.dimen.y2)));
        mListDocs.getRecyclerView().setAdapter(mAdapter);
        mListDocs.setPullCallback(new PullCallback() {
            @Override
            public void onLoadMore() {

            }

            @Override
            public void onRefresh() {
                isLoading = true;
                mPresenter.loadStoryFindList(id);
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public boolean hasLoadedAllItems() {
                return false;
            }
        });
        mPresenter.loadStoryFindList(id);
    }

    public void setBack(@DrawableRes int res) {
        if (res == 0) {
            mIvBack.setVisibility(View.GONE);
        } else {
            mIvBack.setVisibility(View.VISIBLE);
            mIvBack.setImageResource(res);
        }
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {
        mIvBack.setVisibility(View.VISIBLE);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTvTitle.setTextColor(getResources().getColor(R.color.main_cyan));
        if (status == 1) {
            if (!TextUtils.isEmpty(name)) {
                String[] split = name.split(" ");
                mTvTitle.setText(split[1]);
            }
        } else {
            mTvTitle.setText(name);
        }
    }

    @Override
    protected void initListeners() {
        mAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mAdapter.getItem(position).isFlag()) {
                    Intent i = new Intent(StoryListActivity.this, MapEventNewActivity.class);
                    i.putExtra("id", mAdapter.getItem(position).getStoryId());
                    i.putExtra("type",true);
//                    RESULT_BACK = position;
//                    startActivityForResult(i, RESULT_BACK);
                    startActivity(i);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

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
    public void onFailure(int code, String msg) {
        ErrorCodeUtils.showErrorMsgByCode(this, code, msg);
    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == RESULT_BACK || resultCode == RESULT_OK) {
//            if (data != null) {
//                mAdapter.getItem(RESULT_BACK).setFlag(true);
//                mAdapter.notifyItemChanged(RESULT_BACK);
//            }
//        }
//    }

    @Override
    public void onLoadStoryFindListSuccess(ArrayList<StoryListEntity> entity) {
        mListDocs.setComplete();
        isLoading = false;
        mAdapter.setList(entity);
    }
}
