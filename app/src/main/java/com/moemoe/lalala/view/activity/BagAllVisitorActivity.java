package com.moemoe.lalala.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.di.components.DaggerBagVisitorComponent;
import com.moemoe.lalala.di.modules.BagVisitorModule;
import com.moemoe.lalala.model.entity.BagVisitorEntity;
import com.moemoe.lalala.model.entity.UserTopEntity;
import com.moemoe.lalala.presenter.BagVisitorContract;
import com.moemoe.lalala.presenter.BagVisitorPresenter;
import com.moemoe.lalala.utils.AndroidBug5497Workaround;
import com.moemoe.lalala.utils.MenuVItemDecoration;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.view.adapter.AllMemBerAdapter;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;
import com.moemoe.lalala.view.widget.recycler.PullAndLoadView;
import com.moemoe.lalala.view.widget.recycler.PullCallback;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by Sora on 2018/3/6.
 */

public class BagAllVisitorActivity extends BaseAppCompatActivity implements BagVisitorContract.View {
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_toolbar_title)
    TextView mTvTitle;
    @BindView(R.id.rv_list)
    PullAndLoadView mListDosc;
    private AllMemBerAdapter adapter;
    private boolean isLoading;

    @Inject
    BagVisitorPresenter mPresenter;

    private ArrayList<UserTopEntity> arrayList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.ac_bar_list;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        ViewUtils.setStatusBarLight(getWindow(), $(R.id.top_view));
        AndroidBug5497Workaround.assistActivity(this);
        DaggerBagVisitorComponent.builder()
                .bagVisitorModule(new BagVisitorModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);

        mListDosc.getSwipeRefreshLayout().setPadding(0, (int) getResources().getDimension(R.dimen.y24), 0, 0);
        mListDosc.setLayoutManager(new LinearLayoutManager(this));
        mListDosc.getSwipeRefreshLayout().setColorSchemeResources(R.color.main_light_cyan, R.color.main_cyan);
        mListDosc.setLoadMoreEnabled(false);
        mListDosc.setBackgroundColor(Color.WHITE);
        mListDosc.getRecyclerView().addItemDecoration(new MenuVItemDecoration((int) getResources().getDimension(R.dimen.y1)));
        adapter = new AllMemBerAdapter();
        mListDosc.getRecyclerView().setAdapter(adapter);

        isLoading = true;

        mPresenter.loadBagVisitor(PreferenceUtils.getUUid(), 0);
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
        mTvTitle.setText("最近来访");
        mTvTitle.setTextColor(getResources().getColor(R.color.main_cyan));
    }

    @Override
    protected void initListeners() {
        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (arrayList.size() > 0) {
                    ViewUtils.toPersonal(BagAllVisitorActivity.this, arrayList.get(position).getUserId());
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        mListDosc.setPullCallback(new PullCallback() {
            @Override
            public void onLoadMore() {
                isLoading = true;
                if (adapter.getList().size() == 0) {
                    mPresenter.loadBagVisitor(PreferenceUtils.getUUid(), 0);
                } else {
                    mPresenter.loadBagVisitor(PreferenceUtils.getUUid(), adapter.getItemCount());
                }
            }

            @Override
            public void onRefresh() {
                isLoading = true;
                mPresenter.loadBagVisitor(PreferenceUtils.getUUid(), 0);
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

    }

    @Override
    public void onFailure(int code, String msg) {
        isLoading = false;
        mListDosc.setComplete();
    }

    @Override
    public void onLoadBagVisitorSuccess(ArrayList<UserTopEntity> entities, boolean isPull) {
        isLoading = false;
        mListDosc.setComplete();
        mListDosc.setLoadMoreEnabled(true);
        if (entities.size() > 0) {
            arrayList = entities;
            adapter.setList(entities);
        }
    }
}
