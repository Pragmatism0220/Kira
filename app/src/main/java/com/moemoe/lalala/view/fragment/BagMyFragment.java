package com.moemoe.lalala.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.di.components.DaggerBagMyComponent;
import com.moemoe.lalala.di.modules.BagMyModule;
import com.moemoe.lalala.model.entity.BagMyShowEntity;
import com.moemoe.lalala.model.entity.FolderType;
import com.moemoe.lalala.model.entity.NewBagEntity;
import com.moemoe.lalala.model.entity.ShowFolderEntity;
import com.moemoe.lalala.presenter.BagMyContract;
import com.moemoe.lalala.presenter.BagMyPresenter;
import com.moemoe.lalala.utils.FolderVDecoration;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.view.activity.BaseAppCompatActivity;
import com.moemoe.lalala.view.activity.FileMovieActivity;
import com.moemoe.lalala.view.activity.NewFileCommonActivity;
import com.moemoe.lalala.view.activity.NewFileManHuaActivity;
import com.moemoe.lalala.view.activity.NewFileXiaoshuoActivity;
import com.moemoe.lalala.view.activity.ShopDetailActivity;
import com.moemoe.lalala.view.adapter.BagCollectionTopAdapter;
import com.moemoe.lalala.view.adapter.BagMyAdapter;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;
import com.moemoe.lalala.view.widget.recycler.PullAndLoadView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;

import static com.moemoe.lalala.utils.StartActivityConstant.REQUEST_CODE_CREATE_DOC;
import static com.moemoe.lalala.utils.StartActivityConstant.REQ_CREATE_FOLDER;

/**
 * Created by yi on 2016/12/15.
 */

public class BagMyFragment extends BaseFragment implements BagMyContract.View {

    @BindView(R.id.list)
    PullAndLoadView mListDocs;
    @BindView(R.id.ll_not_show)
    View mLlShow;
    @BindView(R.id.rl_capacity)
    RelativeLayout mRlCapacity;
    @BindView(R.id.tv_title)
    TextView mTvCapacity;
    @BindView(R.id.tv_more_add)
    TextView mTvAdd;
    @BindView(R.id.tab_layout1)
    View mView;
    @BindView(R.id.ll_comment_pannel)
    View mLLComment;

    @Inject
    BagMyPresenter mPresenter;

    private BagMyAdapter mAdapter;
    private View mTop;
    private BagCollectionTopAdapter mTopAdapter;
    private String type;
    private String userId;
    private String sign;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_simple_pulltorefresh_list;
    }

    public static BagMyFragment newInstance(String type, String userId, String sign) {
        BagMyFragment fragment = new BagMyFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        bundle.putString("uuid", userId);
        bundle.putString("sign", sign);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void release() {
        if (mPresenter != null) mPresenter.release();
        super.release();
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        DaggerBagMyComponent.builder()
                .bagMyModule(new BagMyModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        type = getArguments().getString("type");
        userId = getArguments().getString("uuid");
        sign = getArguments().getString("sign");
        mListDocs.setVisibility(View.VISIBLE);
        mLLComment.setVisibility(View.GONE);
        mLlShow.setVisibility(View.GONE);
        mAdapter = new BagMyAdapter(userId, type,sign);
        mListDocs.getRecyclerView().setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        mListDocs.getRecyclerView().setAdapter(mAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mListDocs.setLayoutManager(layoutManager);
        mListDocs.getSwipeRefreshLayout().setEnabled(false);
        mListDocs.setLoadMoreEnabled(false);
        mAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        if ("collection".equals(type)) {
            mRlCapacity.setVisibility(View.GONE);
            mTop = LayoutInflater.from(getContext()).inflate(R.layout.item_collection_top, null);
            RecyclerView rv = mTop.findViewById(R.id.rv_list);
            LinearLayoutManager m = new LinearLayoutManager(getContext());
            m.setOrientation(LinearLayoutManager.HORIZONTAL);
            rv.setLayoutManager(m);
            rv.addItemDecoration(new FolderVDecoration());
            mTopAdapter = new BagCollectionTopAdapter();
            rv.setAdapter(mTopAdapter);
            mTopAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    ShowFolderEntity entity = mTopAdapter.getItem(position);
                    if (entity.getType().equals(FolderType.ZH.toString())) {
                        NewFileCommonActivity.startActivity(getContext(), FolderType.ZH.toString(), entity.getFolderId(), entity.getCreateUser());
                    } else if (entity.getType().equals(FolderType.TJ.toString())) {
                        NewFileCommonActivity.startActivity(getContext(), FolderType.TJ.toString(), entity.getFolderId(), entity.getCreateUser());
                    } else if (entity.getType().equals(FolderType.MH.toString())) {
                        NewFileManHuaActivity.startActivity(getContext(), FolderType.MH.toString(), entity.getFolderId(), entity.getCreateUser());
                    } else if (entity.getType().equals(FolderType.XS.toString())) {
                        NewFileXiaoshuoActivity.startActivity(getContext(), FolderType.XS.toString(), entity.getFolderId(), entity.getCreateUser());
                    } else if (entity.getType().equals(FolderType.SP.toString())) {
                        FileMovieActivity.startActivity(getContext(), FolderType.SP.toString(), entity.getFolderId(), entity.getCreateUser());
                    } else if (entity.getType().equals(FolderType.YY.toString())) {
                        FileMovieActivity.startActivity(getContext(), FolderType.YY.toString(), entity.getFolderId(), entity.getCreateUser());
                    }
                }

                @Override
                public void onItemLongClick(View view, int position) {

                }
            });
            if (!"myCollection".equals(sign)) {
                mAdapter.addHeaderView(mTop);
            }
        }
        mPresenter.loadContent(type, userId);
        mPresenter.loadBagData(userId);
        mTvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String temp = "neta://com.moemoe.lalala/url_inner_1.0?http://www.moemoe.la/shubao/?token=" + PreferenceUtils.getToken();
//                Uri uri = Uri.parse(temp);
//                IntentUtils.toActivityFromUri(getContext(), uri, null);
                Intent i = new Intent(getContext(), ShopDetailActivity.class);
                i.putExtra("uuid", "e271079f-63cf-4029-a852-006ce7970e5d");
                startActivity(i);
            }
        });

    }


    @Override
    public void onFailure(int code, String msg) {

    }

    @Override
    public void onLoadSuccess(ArrayList<BagMyShowEntity> entities) {
        if ("my".equals(type)) {
            mAdapter.setList(entities);
        } else {
            BagMyShowEntity shoucang = entities.get(0);
            mTopAdapter.setList(shoucang.getItems());

            entities.remove(0);
            mAdapter.setList(entities);
        }
    }

    @Override
    public void onLoadBagSuccess(NewBagEntity entity) {
        if ("my".equals(type)) {
            if (entity.getUserId().equals(PreferenceUtils.getUUid())) {
                mRlCapacity.setVisibility(View.VISIBLE);
                mView.setVisibility(View.VISIBLE);
                mTvCapacity.setText("我的容量：" + getString(R.string.label_bag_space, getSize(entity.getUseSize()), getSize(entity.getMaxSize())));
            }
        }
    }

    private int getSize(long size) {
        if (size == 0) return 0;
        return (int) (size / 1024 / 1024);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == REQUEST_CODE_CREATE_DOC || requestCode == REQ_CREATE_FOLDER) && resultCode == BaseAppCompatActivity.RESULT_OK) {
            mPresenter.loadContent(type, userId);
        }
    }
}
