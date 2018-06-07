package com.moemoe.lalala.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
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
import com.moemoe.lalala.view.adapter.NewVisitorAdapter;
import com.moemoe.lalala.view.base.BaseActivity;
import com.moemoe.lalala.view.widget.recycler.PullAndLoadView;
import com.moemoe.lalala.view.widget.recycler.PullCallback;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class NewVisitorActivity extends BaseActivity implements NewVisitorsContract.View {

    private ActivityNewVisitorBinding binding;
    private TextView mTvTitle;
    private ImageView mIvBack;
    private boolean isLoading = false;
    private NewVisitorAdapter mAdapter;

    private PullAndLoadView mRv;

    @Inject
    NewVisitorPresenter mPresenter;

    @Override
    protected void initComponent() {
        DaggerNewVisitorsComponent.builder()
                .newVisitorModule(new NewVisitorModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_visitor);
        binding.setPresenter(new Presenter());
        mTvTitle = findViewById(R.id.tv_toolbar_title);
        mIvBack = findViewById(R.id.iv_back);
        mRv = findViewById(R.id.new_root_rv);
        mRv.getSwipeRefreshLayout().setColorSchemeResources(R.color.main_light_cyan, R.color.main_cyan);
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new NewVisitorAdapter();
        mRv.getRecyclerView().setAdapter(mAdapter);
        mRv.setLoadMoreEnabled(false);
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
                mPresenter.getNewVisitorsInfo(2, mAdapter.getItemCount());
            }

            @Override
            public void onRefresh() {
                isLoading = true;
                mPresenter.getNewVisitorsInfo(2, mAdapter.getItemCount());
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

    }

    @Override
    protected void initData() {
        mPresenter.getNewVisitorsInfo(10, 0);
    }


    @Override
    public void getNewVisitorsInfoSuccess(ArrayList<VisitorsEntity> entities, boolean isPull) {
        Log.i("asd", "getNewVisitorsInfoSuccess: " + entities);
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
