package com.moemoe.lalala.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.di.components.DaggerNoticeComponent;
import com.moemoe.lalala.di.modules.NoticeModule;
import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.entity.FeedNoticeEntity;
import com.moemoe.lalala.presenter.NoticeContract;
import com.moemoe.lalala.presenter.NoticePresenter;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.view.adapter.FeedNoticeAdapter;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;
import com.moemoe.lalala.view.widget.recycler.PullAndLoadView;
import com.moemoe.lalala.view.widget.recycler.PullCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by Sora on 2018/3/24.
 */

public class NoticeChildFragment extends BaseFragment implements NoticeContract.View {


    @BindView(R.id.tv_notice)
    TextView mTvNotice;
    @BindView(R.id.list)
    PullAndLoadView mList;
    private long notifyTime = 0;

    @Inject
    NoticePresenter mPresenter;

    private FeedNoticeAdapter mAdapter;
    private boolean isLoading = false;
    private String type;

    public static NoticeChildFragment newInstance(String type) {
        NoticeChildFragment fragment = new NoticeChildFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_feed_follow_all;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        PreferenceUtils.clearMsg(getContext());

        DaggerNoticeComponent.builder()
                .noticeModule(new NoticeModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        type = getArguments().getString("type");
        mList.getSwipeRefreshLayout().setColorSchemeResources(R.color.main_light_cyan, R.color.main_cyan);
        mList.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new FeedNoticeAdapter();
        mList.getRecyclerView().setAdapter(mAdapter);
        mList.setLoadMoreEnabled(false);

        mAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mAdapter.getItemCount() == 0) {
                    notifyTime = 0;
                    mPresenter.loadNotifyList(type, notifyTime);
                } else {
                    mPresenter.loadNotifyList(type, notifyTime);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        mList.setPullCallback(new PullCallback() {
            @Override
            public void onLoadMore() {
                isLoading = true;
                if (mAdapter.getItemCount() == 0) {
                    notifyTime = 0;
                    mPresenter.loadNotifyList(type, notifyTime);
                } else {
                    mPresenter.loadNotifyList(type, notifyTime);
                }
            }

            @Override
            public void onRefresh() {
                isLoading = true;
                notifyTime = 0;
                mPresenter.loadNotifyList(type, notifyTime);
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
        isLoading = true;
        mPresenter.loadNotifyList(type, notifyTime);
    }

    @Override
    public void release() {
        if (mPresenter != null) {
            mPresenter.release();
        }
    }

    @Override
    public void onFailure(int code, String msg) {
        isLoading = false;
        mList.setComplete();
    }

    @Override
    public void onLoadNotifyListSuccess(ArrayList<FeedNoticeEntity> entities, boolean isPull) {
        isLoading = false;
        mList.setComplete();
        getNextTime(entities);
        if (entities.size() >= ApiService.LENGHT) {
            mList.setLoadMoreEnabled(true);
        } else {
            mList.setLoadMoreEnabled(false);
        }
        if (isPull) {
            mAdapter.setList(entities);
        } else {
            mAdapter.addList(entities);
        }
    }

    private void getNextTime(ArrayList<FeedNoticeEntity> entities) {
        boolean notifySet = false;

        for (int i = entities.size() - 1; i >= 0; i--) {
            FeedNoticeEntity entity = entities.get(i);
            if (!notifySet) {
                notifyTime = entity.getTimestamp();
            }
            notifySet = true;
            if (notifySet) {
                break;
            }
        }
    }
}

