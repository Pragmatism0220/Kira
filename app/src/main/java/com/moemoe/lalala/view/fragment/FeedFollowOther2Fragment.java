package com.moemoe.lalala.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.di.components.DaggerFeedFollowOther2Component;
import com.moemoe.lalala.di.modules.FeedFollowOther2Module;
import com.moemoe.lalala.model.entity.DocResponse;
import com.moemoe.lalala.model.entity.DocTagEntity;
import com.moemoe.lalala.model.entity.SimpleUserEntity;
import com.moemoe.lalala.model.entity.TagLikeEntity;
import com.moemoe.lalala.model.entity.TagSendEntity;
import com.moemoe.lalala.presenter.FeedFollowOther2Contract;
import com.moemoe.lalala.presenter.FeedFollowOther2Presenter;
import com.moemoe.lalala.utils.ErrorCodeUtils;
import com.moemoe.lalala.utils.MenuVItemDecoration;
import com.moemoe.lalala.utils.NewCommunityDecoration;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.view.activity.CommunityV1Activity;
import com.moemoe.lalala.view.activity.NewDocDetailActivity;
import com.moemoe.lalala.view.adapter.CommumityTopAdapter;
import com.moemoe.lalala.view.adapter.OldDocAdapter;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;
import com.moemoe.lalala.view.widget.recycler.PullAndLoadView;
import com.moemoe.lalala.view.widget.recycler.PullCallback;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;
import static com.moemoe.lalala.utils.StartActivityConstant.REQUEST_CODE_CREATE_DOC;

/**
 * Created by yi on 2017/9/4.
 */

public class FeedFollowOther2Fragment extends BaseFragment implements FeedFollowOther2Contract.View {

    private int RESULT_DOC_DETILE = 110;

    @BindView(R.id.list)
    PullAndLoadView mListDocs;
    @BindView(R.id.iv_to_wen)
    ImageView mCreateDoc;

    @Inject
    FeedFollowOther2Presenter mPresenter;

    private OldDocAdapter mAdapter;
    private boolean isLoading;
    private String id;
    private View topView;
    private RecyclerView mTopList;
    private CommumityTopAdapter mTopAdapter;
    private boolean manager;
    private String name;

    public static FeedFollowOther2Fragment newInstance(String id, String name, boolean manager) {
        FeedFollowOther2Fragment fragment = new FeedFollowOther2Fragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("name", name);
        bundle.putBoolean("manager", manager);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_onepull_new;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        DaggerFeedFollowOther2Component.builder()
                .feedFollowOther2Module(new FeedFollowOther2Module(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        id = getArguments().getString("id");
        name = getArguments().getString("name");

        mListDocs.getSwipeRefreshLayout().setColorSchemeResources(R.color.main_light_cyan, R.color.main_cyan);
        mListDocs.setLoadMoreEnabled(false);
        mListDocs.setLayoutManager(new LinearLayoutManager(getContext()));
        mListDocs.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bg_f6f6f6));
        mAdapter = new OldDocAdapter();
        mListDocs.getRecyclerView().addItemDecoration(new NewCommunityDecoration((int) getResources().getDimension(R.dimen.y24), 2));
        mListDocs.getRecyclerView().setAdapter(mAdapter);
//        mCreateDoc.setOnClickListener(new NoDoubleClickListener() {
//            @Override
//            public void onNoDoubleClick(View v) {
//                Intent intent = new Intent(getContext(), CreateRichDocActivity.class);
//                intent.putExtra(CreateRichDocActivity.TYPE_QIU_MING_SHAN, 4);
//                intent.putExtra(CreateRichDocActivity.TYPE_TAG_NAME_DEFAULT, name);
//                intent.putExtra("departmentId", id);
//                intent.putExtra("from_name", name);
//                intent.putExtra("from_schema", "");
//                startActivityForResult(intent, REQUEST_CODE_CREATE_DOC);
//            }
//        });
        mAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                clickEvent("社团-社团-广场列表点击");
                DocResponse docResponse = mAdapter.getItem(position);
                Intent i = new Intent(getContext(), NewDocDetailActivity.class);
                i.putExtra("uuid", docResponse.getId());
                i.putExtra("title",name);
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
                    mPresenter.loadDocTagList(id, 0);
                } else {
                    mPresenter.loadDocTagList(id, mAdapter.getItem(mAdapter.getList().size() - 1).getTimestamp());
                }
            }

            @Override
            public void onRefresh() {
                isLoading = true;
                mPresenter.loadDocTagList(id, 0);
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
        topView = LayoutInflater.from(getContext()).inflate(R.layout.ac_cm_square_top, null);
        mTopList = topView.findViewById(R.id.rl_top_list);
        mTopList.setLayoutManager(new LinearLayoutManager(getContext()));
        mTopList.addItemDecoration(new MenuVItemDecoration((int) getResources().getDimension(R.dimen.y1)));
        mTopList.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        mTopAdapter = new CommumityTopAdapter();
        mTopList.setAdapter(mTopAdapter);
        mAdapter.addHeaderView(topView);

        mPresenter.loadDocTagList(id, 0);
        mPresenter.loadDocTagTopList(id);


        mTopAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent i = new Intent(getContext(), NewDocDetailActivity.class);
                i.putExtra("uuid", mTopAdapter.getItem(position).getId());
                i.putExtra("title",name);
                ((CommunityV1Activity) getContext()).startActivityForResult(i, RESULT_DOC_DETILE);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    public void setSmoothScrollToPosition() {
        mListDocs.getRecyclerView().smoothScrollToPosition(0);
    }

    public String getCommuntiyName() {
        return name;
    }

    @Override
    public void onFailure(int code, String msg) {
        isLoading = false;
        mListDocs.setComplete();
        ErrorCodeUtils.showErrorMsgByCode(getContext(), code, msg);
    }

    public void release() {
        if (mPresenter != null) mPresenter.release();
        super.release();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CREATE_DOC && resultCode == RESULT_OK) {
            mPresenter.loadDocTagList(id, 0);
        } else if (requestCode == RESULT_DOC_DETILE && resultCode == RESULT_OK) {
            mPresenter.loadDocTagTopList(id);
        }
    }

    public void createLabel(TagSendEntity entity, int position) {
        mPresenter.createLabel(entity, position);

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
     * 社区标签点赞
     * @param isLike
     * @param position
     * @param entity
     * @param parentPosition
     */
    public void likeTag(boolean isLike, int position, TagLikeEntity entity, int parentPosition) {
        mPresenter.likeTag(isLike, position, entity, parentPosition);
    }
    @Override
    public void onLoadDocTagListSuccess(ArrayList<DocResponse> responses, boolean isPull) {
        isLoading = false;
        mListDocs.setComplete();
        mListDocs.setLoadMoreEnabled(true);
        if (isPull) {
            mAdapter.setList(responses);
        } else {
            mAdapter.addList(responses);
        }
    }

    @Override
    public void onLoadDocTagTopListSuccess(ArrayList<DocResponse> response) {
        isLoading = false;
        mListDocs.setComplete();
        if (response == null || response.size() == 0) {
            topView.setVisibility(View.GONE);
        } else {
            topView.setVisibility(View.VISIBLE);
            mTopAdapter.setList(response);

        }
    }

    @Override
    public void onLoadDocLikeSuccess(boolean isLike, int position) {
//        NewDynamicEntity entity = new Gson().fromJson(mAdapter.getList().get(position).getTargetObj(),NewDynamicEntity.class);
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
    public void onCreateLabel(String s, String name, int position) {
        if (getContext() instanceof CommunityV1Activity) {
            ((CommunityV1Activity) getContext()).showToast("添加成功");
            ((CommunityV1Activity) getContext()).finalizeDialog();
        }

        DocTagEntity docTagEntity = new DocTagEntity();
        docTagEntity.setId(s);
        docTagEntity.setName(name);
        int size = mAdapter.getList().get(position).getTags().size();
        size = size == 0 ? 0 : size;
        mAdapter.getList().get(position).getTags().add(size, docTagEntity);
        mAdapter.notifyItemChanged(position + 1);
    }
    @Override
    public void onPlusLabel(int position, boolean isLike, int parentPosition) {
        if (getContext() instanceof CommunityV1Activity) {
            ((CommunityV1Activity) getContext()).finalizeDialog();
        }
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
    @Override
    public void onResume() {
        super.onResume();
        startTime();
    }

    @Override
    protected void pauseTime() {
        super.pauseTime();
        pauseTime();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stayEvent("社团-社团-广场");
    }
}
