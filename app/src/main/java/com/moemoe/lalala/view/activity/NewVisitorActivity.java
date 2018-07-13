package com.moemoe.lalala.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.databinding.ActivityNewVisitorBinding;
import com.moemoe.lalala.di.components.DaggerNewVisitorsComponent;
import com.moemoe.lalala.di.modules.NewVisitorModule;
import com.moemoe.lalala.model.entity.VisitorsEntity;
import com.moemoe.lalala.presenter.NewVisitorPresenter;
import com.moemoe.lalala.presenter.NewVisitorsContract;
import com.moemoe.lalala.utils.ErrorCodeUtils;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.view.adapter.NewVisitorAdapter;
import com.moemoe.lalala.view.base.BaseActivity;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;
import com.moemoe.lalala.view.widget.recycler.PullAndLoadView;
import com.moemoe.lalala.view.widget.recycler.PullCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class NewVisitorActivity extends BaseActivity implements NewVisitorsContract.View {

    private ActivityNewVisitorBinding binding;
    private PullAndLoadView mItemView;
    private TextView mTvTitle;
    private ImageView mIvBack;
    private boolean isLoading = false;
    private NewVisitorAdapter mAdapter;
    private ArrayList<VisitorsEntity> info;
    private String uuid;

    private PullAndLoadView mRv;

    @Inject
    NewVisitorPresenter mPresenter;

    @Override
    protected void initComponent() {
        ViewUtils.setStatusBarLight(getWindow(), $(R.id.top_view));
        DaggerNewVisitorsComponent.builder()
                .newVisitorModule(new NewVisitorModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_visitor);
        binding.setPresenter(new Presenter());
        mItemView = findViewById(R.id.root_rv);
        mTvTitle = findViewById(R.id.tv_toolbar_title);
        mIvBack = findViewById(R.id.iv_back);
        mRv = findViewById(R.id.new_root_rv);
        mRv.getSwipeRefreshLayout().setColorSchemeResources(R.color.main_light_cyan, R.color.main_cyan);
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new NewVisitorAdapter();
        mRv.getRecyclerView().setAdapter(mAdapter);
        mRv.setLoadMoreEnabled(false);
        uuid = getIntent().getStringExtra(UUID);
    }


    @Override
    protected void initViews(Bundle savedInstanceState) {

    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {
        mTvTitle.setText("访客记录");
        mTvTitle.setTextColor(getResources().getColor(R.color.main_cyan));
        mIvBack.setVisibility(View.VISIBLE);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void initListeners() {
        mRv.setLoadMoreEnabled(true);
        mRv.setPullCallback(new PullCallback() {
            @Override
            public void onLoadMore() {
                isLoading = true;
                if (uuid == null) {
                    mPresenter.getNewVisitorsInfo(10, mAdapter.getItemCount());
                } else {
                    mPresenter.getHisVisitorsInfo(10, mAdapter.getItemCount(), uuid);
                }

            }

            @Override
            public void onRefresh() {
                isLoading = true;
                if (uuid == null) {
                    mPresenter.getNewVisitorsInfo(10, mAdapter.getItemCount());
                } else {
                    mPresenter.getHisVisitorsInfo(10, mAdapter.getItemCount(), uuid);
                }
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
        mAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

    }

    @Override
    protected void initData() {
        if (uuid == null) {
            mPresenter.getNewVisitorsInfo(10, 0);
        } else {
            mPresenter.getHisVisitorsInfo(10, 0, uuid);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (uuid == null) {
            mPresenter.getNewVisitorsInfo(10, 0);
        } else {
            mPresenter.getHisVisitorsInfo(10, 0, uuid);
        }
    }


    @Override
    public void getNewVisitorsInfoSuccess(final ArrayList<VisitorsEntity> entities, boolean isPull) {
        Log.i("asd", "getNewVisitorsInfoSuccess: " + entities);
        info = new ArrayList<>();
        info.clear();
        info.addAll(entities);
        isLoading = false;
        mRv.setComplete();
        if (isPull) {
            mAdapter.setList(entities);
        } else {
            mAdapter.addList(entities);
        }
    }


    @Override
    public void getHiiVisitorsInfoSccess(ArrayList<VisitorsEntity> entities, boolean isPull) {
        info = new ArrayList<>();
        info.clear();
        info.addAll(entities);
        isLoading = false;
        mRv.setComplete();
        if (isPull) {
            mAdapter.setList(entities);
        } else {
            mAdapter.addList(entities);
        }
    }

    public class Presenter {
        public void onClick(View v) {
            switch (v.getId()) {

            }
        }
    }


    public static class TempEntity {
        private Date titleDate;
        private List<VisitorsEntity> items = new ArrayList<>();

        public Date getTitleDate() {
            return titleDate;
        }

        public void setTitleDate(Date titleDate) {
            this.titleDate = titleDate;
        }

        public List<VisitorsEntity> getItems() {
            return items;
        }

        public void setItems(List<VisitorsEntity> items) {
            this.items = items;
        }

        @Override
        public String toString() {
            return "TempEntity{" +
                    "titleDate=" + titleDate +
                    ", items=" + items +
                    '}';
        }
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) mPresenter.release();
        super.onDestroy();
    }

    @Override
    public void onFailure(int code, String msg) {
        isLoading = false;
        mRv.setComplete();
        ErrorCodeUtils.showErrorMsgByCode(getApplicationContext(), code, msg);
    }
}
