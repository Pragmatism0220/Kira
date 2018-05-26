package com.moemoe.lalala.view.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.di.components.DaggerBagFavoriteComponent;
import com.moemoe.lalala.di.modules.BagFavoriteModule;
import com.moemoe.lalala.model.entity.FolderType;
import com.moemoe.lalala.model.entity.ShowFolderEntity;
import com.moemoe.lalala.presenter.BagFavoriteContract;
import com.moemoe.lalala.presenter.BagFavoritePresenter;
import com.moemoe.lalala.utils.NewFeedBagDecoration;
import com.moemoe.lalala.view.activity.NewFileCommonActivity;
import com.moemoe.lalala.view.activity.NewFileManHuaActivity;
import com.moemoe.lalala.view.activity.NewFileXiaoshuoActivity;
import com.moemoe.lalala.view.adapter.FeedBagAdapter;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;
import com.moemoe.lalala.view.widget.recycler.PullAndLoadView;
import com.moemoe.lalala.view.widget.recycler.PullCallback;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * 书包-我的购买
 * Created by Sora on 2018/3/6.
 */

public class BagBuyFragment extends BaseFragment implements BagFavoriteContract.View {

    @BindView(R.id.rv_list)
    PullAndLoadView mListDocs;

    @Inject
    BagFavoritePresenter mPresenter;

    private FeedBagAdapter mAdapter;
    private boolean isLoading;

    public static BagBuyFragment newInstance() {
        return new BagBuyFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ac_list;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        DaggerBagFavoriteComponent.builder()
                .bagFavoriteModule(new BagFavoriteModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);

        mListDocs.getSwipeRefreshLayout().setColorSchemeResources(R.color.main_light_cyan, R.color.main_cyan);
        mListDocs.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mListDocs.getRecyclerView().addItemDecoration(new NewFeedBagDecoration(getResources().getDimensionPixelSize(R.dimen.x24), 2));
        mListDocs.setLoadMoreEnabled(false);
        mAdapter = new FeedBagAdapter();
        mListDocs.getRecyclerView().setAdapter(mAdapter);
        mListDocs.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));


        mAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ShowFolderEntity entity = mAdapter.getItem(position);
                if (entity.getType().equals(FolderType.ZH.toString())) {
                    NewFileCommonActivity.startActivity(getContext(), FolderType.ZH.toString(), entity.getFolderId(), entity.getCreateUser());
                } else if (entity.getType().equals(FolderType.TJ.toString())) {
                    NewFileCommonActivity.startActivity(getContext(), FolderType.TJ.toString(), entity.getFolderId(), entity.getCreateUser());
                } else if (entity.getType().equals(FolderType.MH.toString())) {
                    NewFileManHuaActivity.startActivity(getContext(), FolderType.MH.toString(), entity.getFolderId(), entity.getCreateUser());
                } else if (entity.getType().equals(FolderType.XS.toString())) {
                    NewFileXiaoshuoActivity.startActivity(getContext(), FolderType.XS.toString(), entity.getFolderId(), entity.getCreateUser());
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        mListDocs.setPullCallback(new PullCallback() {
            @Override
            public void onLoadMore() {
                isLoading = true;
                if (mAdapter.getList().size() == 0) {
                    mPresenter.getFavoriteList(0);
                } else {
                    mPresenter.getFavoriteList(mAdapter.getItemCount());
                }
            }

            @Override
            public void onRefresh() {
                isLoading = true;
                mPresenter.getFavoriteList(0);
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
        mPresenter.getFavoriteList(0);
    }

    @Override
    public void onFailure(int code, String msg) {
        isLoading = false;
        mListDocs.setComplete();

    }

    @Override
    public void loadListSuccess(ArrayList<ShowFolderEntity> entities, boolean isPull) {
        isLoading = false;
        mListDocs.setVisibility(View.VISIBLE);
        mListDocs.setComplete();
        if (isPull) {
            mAdapter.setList(entities);
        } else {
            mAdapter.addList(entities);
        }
    }

    @Override
    public void deleteSuccess() {

    }

    @Override
    public void release() {
        if (mPresenter != null) mPresenter.release();
        super.release();
    }
}
