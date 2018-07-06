package com.moemoe.lalala.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.di.components.DaggerOldDocComponent;
import com.moemoe.lalala.di.modules.OldDocModule;
import com.moemoe.lalala.model.entity.BannerEntity;
import com.moemoe.lalala.model.entity.DocResponse;
import com.moemoe.lalala.model.entity.DocTagEntity;
import com.moemoe.lalala.model.entity.Image;
import com.moemoe.lalala.model.entity.SimpleUserEntity;
import com.moemoe.lalala.model.entity.TagLikeEntity;
import com.moemoe.lalala.model.entity.TagSendEntity;
import com.moemoe.lalala.presenter.OldDocContract;
import com.moemoe.lalala.presenter.OldDocPresenter;
import com.moemoe.lalala.utils.MenuVItemDecoration;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.view.activity.NewDocDetailActivity;
import com.moemoe.lalala.view.activity.NewDynamicActivity;
import com.moemoe.lalala.view.activity.PersonalV2Activity;
import com.moemoe.lalala.view.adapter.OldDocAdapter;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;
import com.moemoe.lalala.view.widget.recycler.PullAndLoadView;
import com.moemoe.lalala.view.widget.recycler.PullCallback;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by yi on 2017/9/4.
 */

public class OldDocFragment extends BaseFragment implements OldDocContract.View {

    @BindView(R.id.list)
    PullAndLoadView mListDocs;

    @Inject
    OldDocPresenter mPresenter;
    private OldDocAdapter mAdapter;
    private boolean isLoading = false;

    public static OldDocFragment newInstance(String userId) {
        OldDocFragment fragment = new OldDocFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", userId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_onepull;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        DaggerOldDocComponent.builder()
                .oldDocModule(new OldDocModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        final String userId = getArguments().getString("id");
        mListDocs.getSwipeRefreshLayout().setColorSchemeResources(R.color.main_light_cyan, R.color.main_cyan);
        mListDocs.setLoadMoreEnabled(false);
        mListDocs.setLayoutManager(new LinearLayoutManager(getContext()));
        mListDocs.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bg_f6f6f6));
        mAdapter = new OldDocAdapter();
        mListDocs.getRecyclerView().addItemDecoration(new MenuVItemDecoration((int) getResources().getDimension(R.dimen.y24)));
        mListDocs.getRecyclerView().setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                DocResponse docResponse = mAdapter.getItem(position);
                Intent i = new Intent(getContext(), NewDocDetailActivity.class);
                i.putExtra("title", "个人中心");
                i.putExtra("uuid", docResponse.getId());
                startActivity(i);
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
                    mPresenter.loadOldDocList(userId, 0);
                } else {
                    mPresenter.loadOldDocList(userId, mAdapter.getItem(mAdapter.getList().size() - 1).getTimestamp());
                }
            }

            @Override
            public void onRefresh() {
                isLoading = true;
                mPresenter.loadOldDocList(userId, 0);
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
        mPresenter.loadOldDocList(userId, 0);

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
    public void loadOldDocListSuccess(ArrayList<DocResponse> list, boolean isPull) {
        isLoading = false;
        mListDocs.setComplete();
        mListDocs.setLoadMoreEnabled(true);
        if (isPull) {
            mAdapter.setList(list);
        } else {
            mAdapter.addList(list);
        }
    }

    @Override
    public void onCreateLabel(String s, String name, int position) {
        ((PersonalV2Activity) getContext()).showToast("添加成功");
        if (getContext() instanceof PersonalV2Activity) {
            ((PersonalV2Activity) getContext()).finalizeDialog();
        }
        DocTagEntity docTagEntity = new DocTagEntity();
        docTagEntity.setId(s);
        docTagEntity.setName(name);
        int size = mAdapter.getList().get(position).getTags().size();
        size = size == 0 ? 0 : size;
        mAdapter.getList().get(position).getTags().add(size, docTagEntity);
        mAdapter.notifyItemChanged(position);
    }

    @Override
    public void onLoadDocLikeSuccess(boolean isLike, int position) {
        mAdapter.getList().get(position).setThumb(isLike);
        if (isLike) {
            SimpleUserEntity userEntity = new SimpleUserEntity();
            userEntity.setUserName(PreferenceUtils.getAuthorInfo().getUserName());
            userEntity.setUserId(PreferenceUtils.getUUid());
            userEntity.setUserIcon(PreferenceUtils.getAuthorInfo().getHeadPath());
            mAdapter.getList().get(position).getThumbUsers().add(0, userEntity);
            mAdapter.getList().get(position).setThumbs(mAdapter.getList().get(position).getThumbs() + 1);
        } else {
            for (SimpleUserEntity userEntity : mAdapter.getList().get(position).getThumbUsers()) {
                if (userEntity.getUserId().equals(PreferenceUtils.getUUid())) {
                    mAdapter.getList().get(position).getThumbUsers().remove(userEntity);
                    break;
                }
            }
            mAdapter.getList().get(position).setThumbs(mAdapter.getList().get(position).getThumbs() - 1);
        }
        if (mAdapter.getHeaderLayoutCount() != 0) {
            mAdapter.notifyItemChanged(position + 1);
        } else {
            mAdapter.notifyItemChanged(position);
        }
    }

    @Override
    public void onBannerLoadSuccess(ArrayList<BannerEntity> bannerEntities) {

    }

    /**
     * 社区详情-广场-item点赞
     *
     * @param id
     * @param isLike
     * @param position
     */
    public void likeDoc(String id, boolean isLike, int position) {
        mPresenter.loadDocLike(id, isLike, position);
    }

    /**
     * 添加标签
     *
     * @param entity
     */
    public void createLabel(TagSendEntity entity, int position) {
        mPresenter.createLabel(entity, position);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    public void likeTag(boolean isLike, int position, TagLikeEntity entity, int parentPosition) {
        mPresenter.likeTag(isLike, position, entity, parentPosition);
    }

    @Override
    public void onPlusLabel(int position, boolean isLike, int parentPosition) {
        ((PersonalV2Activity) getContext()).finalizeDialog();
        DocResponse docResponse = mAdapter.getList().get(parentPosition);
        if (docResponse.getTags() != null && docResponse.getTags().size() > 0) {
            DocTagEntity tagEntity = docResponse.getTags().get(position);
            docResponse.getTags().remove(position);
            tagEntity.setLiked(isLike);
            if (isLike) {
                tagEntity.setLikes(tagEntity.getLikes() + 1);
                docResponse.getTags().add(position, tagEntity);
                docResponse.setTagLikes(docResponse.getTagLikes() + 1);
                mAdapter.getList().add(parentPosition, docResponse);
            } else {
                tagEntity.setLikes(tagEntity.getLikes() - 1);
                if (tagEntity.getLikes() > 0) {
                    docResponse.getTags().add(position, tagEntity);
                    docResponse.setTagLikes(docResponse.getTagLikes() - 1);
                    mAdapter.getList().add(parentPosition, docResponse);
                }
            }
            if (mAdapter.getHeaderLayoutCount() != 0) {
                mAdapter.notifyItemChanged(parentPosition + 1);
            } else {
                mAdapter.notifyItemChanged(parentPosition);
            }
        }
    }
}
