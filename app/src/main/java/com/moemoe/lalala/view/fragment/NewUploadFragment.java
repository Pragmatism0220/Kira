package com.moemoe.lalala.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.di.components.DaggerNewUploadComponent;
import com.moemoe.lalala.di.modules.NewUploadModule;
import com.moemoe.lalala.model.entity.FolderType;
import com.moemoe.lalala.model.entity.ShowFolderEntity;
import com.moemoe.lalala.presenter.NewUploadContract;
import com.moemoe.lalala.presenter.NewUploadPresenter;
import com.moemoe.lalala.utils.CommunityV1Decoration;
import com.moemoe.lalala.utils.FeedBagDecoration;
import com.moemoe.lalala.utils.NewFeedBagDecoration;
import com.moemoe.lalala.view.activity.FileMovieActivity;
import com.moemoe.lalala.view.activity.KiraMusicActivity;
import com.moemoe.lalala.view.activity.KiraVideoActivity;
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
 * Created by Sora on 2018/2/28.
 */

public class NewUploadFragment extends BaseFragment implements NewUploadContract.View {

    @BindView(R.id.list)
    PullAndLoadView mListDocs;
    @BindView(R.id.iv_to_wen)
    ImageView mIvCreateFolder;

    @Inject
    NewUploadPresenter mPresenter;

    private FeedBagAdapter mAdapter;
    private boolean isLoading = false;
    private String id;

    public static NewUploadFragment newInstance(String id) {
        NewUploadFragment fragment = new NewUploadFragment();
        Bundle bundle = new Bundle();
        bundle.putString("uuid", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_onepull;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        DaggerNewUploadComponent.builder()
                .newUploadModule(new NewUploadModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        mIvCreateFolder.setVisibility(View.GONE);
        mIvCreateFolder.setImageResource(R.drawable.btn_feed_create_bag);
        id = getArguments().getString("uuid");
        mListDocs.getSwipeRefreshLayout().setColorSchemeResources(R.color.main_light_cyan, R.color.main_cyan);
        mListDocs.setLoadMoreEnabled(true);
        mListDocs.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mListDocs.getRecyclerView().addItemDecoration(new CommunityV1Decoration(getResources().getDimensionPixelSize(R.dimen.x24), 2));
        mAdapter = new FeedBagAdapter();
        mListDocs.getRecyclerView().setAdapter(mAdapter);
        mListDocs.setBackgroundColor(Color.WHITE);
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
                } else if (entity.getType().equals(FolderType.YY.toString())) {
                    FileMovieActivity.startActivity(getContext(), FolderType.YY.toString(), entity.getFolderId(), entity.getCreateUser());
                } else if (entity.getType().equals(FolderType.SP.toString())) {
                    FileMovieActivity.startActivity(getContext(), FolderType.SP.toString(), entity.getFolderId(), entity.getCreateUser());
                } else if ("MOVIE".equals(entity.getType())) {
                    KiraVideoActivity.startActivity(getContext(), entity.getUuid(), entity.getFolderId(), entity.getFolderName(), entity.getCover());
                } else if ("MUSIC".equals(entity.getType())) {
                    KiraMusicActivity.startActivity(getContext(), entity.getUuid(), entity.getFolderId(), entity.getFolderName(), entity.getCover());
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
                mPresenter.loadTagBagNewest(id, mAdapter.getList().size());
            }

            @Override
            public void onRefresh() {
                isLoading = true;
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
        mPresenter.loadTagBagNewest(id, 0);
    }

    public  void setSmoothScrollToPosition(){
        mListDocs.getRecyclerView().smoothScrollToPosition(0);
    }
    @Override
    public void onFailure(int code, String msg) {
        isLoading = false;
        mListDocs.setComplete();
    }

    public void release() {
        if (mPresenter != null) mPresenter.release();
        super.release();
    }

    @Override
    public void onLoadTagBagNewestSuccess(ArrayList<ShowFolderEntity> entities, boolean isPull) {
        isLoading = false;
        mListDocs.setComplete();
        if (isPull) {
            mAdapter.setList(entities);
        } else {
            mAdapter.addList(entities);
        }
    }
}
