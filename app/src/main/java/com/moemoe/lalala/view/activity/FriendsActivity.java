package com.moemoe.lalala.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.di.components.DaggerFriendsComponent;
import com.moemoe.lalala.di.modules.FriendsModule;
import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.entity.PhoneMenuEntity;
import com.moemoe.lalala.presenter.FriendsContract;
import com.moemoe.lalala.presenter.FriendsPresenter;
import com.moemoe.lalala.utils.AllMenmberDecoration;
import com.moemoe.lalala.utils.AndroidBug5497Workaround;
import com.moemoe.lalala.utils.DialogUtils;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.view.adapter.PhoneMenuListAdapter;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;
import com.moemoe.lalala.view.widget.recycler.PullAndLoadView;
import com.moemoe.lalala.view.widget.recycler.PullCallback;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import io.rong.imkit.RongIM;

/**
 * Created by Sora on 2018/4/2.
 */

public class FriendsActivity extends BaseAppCompatActivity implements FriendsContract.View {
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_toolbar_title)
    TextView mTvTitle;
    @BindView(R.id.rv_list)
    PullAndLoadView mListDocs;


    @Inject
    FriendsPresenter mPresenter;

    private PhoneMenuListAdapter adapter;
    private boolean isLoading;
    private ArrayList<PhoneMenuEntity> menuEntities;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, FriendsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ac_bar_list;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        ViewUtils.setStatusBarLight(getWindow(), $(R.id.top_view));
        AndroidBug5497Workaround.assistActivity(this);
        DaggerFriendsComponent.builder()
                .friendsModule(new FriendsModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        mListDocs.getRecyclerView().addItemDecoration(new AllMenmberDecoration((int) getResources().getDimension(R.dimen.y1)));
        mListDocs.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PhoneMenuListAdapter("哟");
        mListDocs.getSwipeRefreshLayout().setColorSchemeResources(R.color.main_light_cyan, R.color.main_cyan);
        mListDocs.setLoadMoreEnabled(true);
        mListDocs.getRecyclerView().setAdapter(adapter);
        mPresenter.loadUserFriends(0);
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
        mTvTitle.setText("选择好友");
    }

    @Override
    protected void initListeners() {
        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (DialogUtils.checkLoginAndShowDlg(FriendsActivity.this)) {
                    if (!TextUtils.isEmpty(PreferenceUtils.getAuthorInfo().getRcToken()) && !TextUtils.isEmpty(adapter.getItem(position).getRcTargetId())) {
                        RongIM.getInstance().startPrivateChat(FriendsActivity.this, adapter.getItem(position).getRcTargetId(), adapter.getItem(position).getUserName());
                        finish();
                    } else {
                        showToast("打开私信失败");
                    }
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
                if (adapter.getList().size() == 0) {
                    mPresenter.loadUserFriends(0);
                } else {
                    mPresenter.loadUserFriends(adapter.getItemCount());
                }
            }

            @Override
            public void onRefresh() {
                isLoading = true;
                mPresenter.loadUserFriends(0);
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
        mListDocs.setComplete();
    }

    @Override
    public void onloadUserFriendsSuccess(ArrayList<PhoneMenuEntity> entity, boolean isPull) {
        isLoading = false;
        mListDocs.setComplete();
        if (entity.size() >= ApiService.LENGHT) {
            mListDocs.setLoadMoreEnabled(true);
        } else {
            mListDocs.setLoadMoreEnabled(false);
        }
        if (isPull) {
            menuEntities = entity;
            adapter.setList(entity);
        } else {
            ArrayList<PhoneMenuEntity> entityArrayList = entity;
            for (int i = 0; i < menuEntities.size(); i++) {
                String userId = menuEntities.get(i).getUserId();
                for (int j = 0; j < entityArrayList.size(); j++) {
                    String userId1 = entityArrayList.get(j).getUserId();
                    if (userId.equals(userId1)) {
                        entityArrayList.remove(j);
                        i--;
                    }
                }
            }
            adapter.addList(entityArrayList);
            menuEntities.addAll(entityArrayList);
        }
    }
}
