package com.moemoe.lalala.view.activity;

import android.databinding.DataBindingUtil;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.databinding.ActivityVisitorsBinding;
import com.moemoe.lalala.di.components.DaggerVisitorsComponent;
import com.moemoe.lalala.di.modules.VisitorsModule;
import com.moemoe.lalala.model.entity.VisitorsEntity;
import com.moemoe.lalala.presenter.VisitorPresenter;
import com.moemoe.lalala.presenter.VisitorsContract;
import com.moemoe.lalala.utils.ErrorCodeUtils;
import com.moemoe.lalala.utils.FolderDecoration;
import com.moemoe.lalala.utils.MenuHItemDecoration;
import com.moemoe.lalala.view.adapter.RootRecycleViewAdapter;
import com.moemoe.lalala.view.base.BaseActivity;
import com.moemoe.lalala.view.widget.recycler.PullAndLoadView;
import com.moemoe.lalala.view.widget.recycler.PullCallback;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * 访客记录
 */
public class VisitorsActivity extends BaseActivity implements VisitorsContract.View {

    private ActivityVisitorsBinding binding;
    private RootRecycleViewAdapter mAdapter;
    public static final String TAG = "VisitorsActivity";
    private List<VisitorsEntity> entityList = new ArrayList<>();
    private TextView mTvTitle;
    private ImageView mIvBack;
    private boolean isLoading = false;


    private PullAndLoadView mRv;
    @Inject
    VisitorPresenter mPresenter;

    @Override
    protected void initComponent() {
        DaggerVisitorsComponent.builder()
                .visitorsModule(new VisitorsModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_visitors);
        binding.setPresenter(new Presenter());
        mTvTitle = findViewById(R.id.tv_toolbar_title);
        mIvBack = findViewById(R.id.iv_back);
        mRv = findViewById(R.id.root_rv);

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
                mPresenter.getVisitorsInfo(10, mAdapter.getItemCount());
            }

            @Override
            public void onRefresh() {
                isLoading = true;
                mPresenter.getVisitorsInfo(10, mAdapter.getItemCount());
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
        mPresenter.getVisitorsInfo(10, mAdapter.getItemCount());
    }

    @Override
    public void onFailure(int code, String msg) {
        isLoading = false;
        mRv.setComplete();
        ErrorCodeUtils.showErrorMsgByCode(getApplicationContext(), code, msg);
    }

    @Override
    public void getVisitorsInfoSuccess(ArrayList<VisitorsEntity> entities, boolean isPull) {
        isLoading = false;
        mRv.setComplete();
        mRv.getSwipeRefreshLayout().setColorSchemeResources(R.color.main_light_cyan, R.color.main_cyan);
        mRv.setLoadMoreEnabled(false);
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RootRecycleViewAdapter(divideList(entities), this);
        mRv.getRecyclerView().setAdapter(mAdapter);
    }


    private List<TempEntity> divideList(List<VisitorsEntity> source) {
        List<TempEntity> data = new ArrayList<>();
        for (int i = 0; i < source.size(); i++) {
            VisitorsEntity currentItemBean = source.get(i);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(currentItemBean.getCreateTime()));
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date currentDate = calendar.getTime();

            if (i == 0) {
                TempEntity tempEntity = new TempEntity();
                tempEntity.setTitleDate(currentDate);
                data.add(tempEntity);
            }

            Date lastDate = data.get(data.size() - 1).getTitleDate();
            if (lastDate.compareTo(currentDate) == 0) {
                data.get(data.size() - 1).getItems().add(currentItemBean);
            } else {
                TempEntity tempEntity = new TempEntity();
                tempEntity.setTitleDate(currentDate);

                List<VisitorsEntity> itemBeen = new ArrayList<>();
                itemBeen.add(currentItemBean);
                tempEntity.setItems(itemBeen);

                data.add(tempEntity);
            }
        }
        return data;
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
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) mPresenter.release();
        super.onDestroy();
    }
}
