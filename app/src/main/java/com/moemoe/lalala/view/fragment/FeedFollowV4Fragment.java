package com.moemoe.lalala.view.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.di.components.DaggerLuntan2Component;
import com.moemoe.lalala.di.modules.Luntan2Module;
import com.moemoe.lalala.model.entity.DepartmentGroupEntity;
import com.moemoe.lalala.model.entity.DocResponse;
import com.moemoe.lalala.model.entity.DocTagEntity;
import com.moemoe.lalala.model.entity.SimpleUserEntity;
import com.moemoe.lalala.model.entity.TagLikeEntity;
import com.moemoe.lalala.model.entity.TagSendEntity;
import com.moemoe.lalala.presenter.Luntan2Contract;
import com.moemoe.lalala.presenter.Luntan2Presenter;
import com.moemoe.lalala.utils.DocEvent;
import com.moemoe.lalala.utils.ErrorCodeUtils;
import com.moemoe.lalala.utils.IntentUtils;
import com.moemoe.lalala.utils.MenuVItemDecoration;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.view.activity.FeedV3Activity;
import com.moemoe.lalala.view.activity.LoginActivity;
import com.moemoe.lalala.view.activity.NewDocDetailActivity;
import com.moemoe.lalala.view.adapter.OldDocAdapter;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;
import com.moemoe.lalala.view.widget.recycler.PullAndLoadView;
import com.moemoe.lalala.view.widget.recycler.PullCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import io.rong.imlib.model.Conversation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static android.app.Activity.RESULT_OK;
import static com.moemoe.lalala.utils.StartActivityConstant.REQUEST_CODE_CREATE_DOC;
import static com.moemoe.lalala.utils.StartActivityConstant.REQ_LOGIN;
import static com.moemoe.lalala.view.activity.LoginActivity.RESPONSE_LOGIN_SUCCESS;

/**
 * 社区-关注页
 * Created by yi on 2018/1/11.
 */

public class FeedFollowV4Fragment extends BaseFragment implements Luntan2Contract.View {

    @BindView(R.id.list)
    PullAndLoadView mListDocs;
    @BindView(R.id.iv_to_wen)
    ImageView mCreateDoc;

    View mLogin;

    @Inject
    Luntan2Presenter mPresenter;
    private View groupView;
    private OldDocAdapter mAdapter;
    private boolean isLoading = false;
    private String id;
    private boolean canDoc;
    private boolean isRefresh = false;


    public static FeedFollowV4Fragment newInstance(String id, String name, boolean isCanDoc) {
        FeedFollowV4Fragment fragment = new FeedFollowV4Fragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("name", name);
        bundle.putBoolean("isCanDoc", isCanDoc);
        fragment.setArguments(bundle);
        return new FeedFollowV4Fragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_feed_follow_v4;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        id = "all";
        final String name = "全部";
        canDoc = true;
        //埋点统计：动态——关注
        if (getActivity() instanceof FeedV3Activity) {
            clickEvent("社团-关注");
        }
        DaggerLuntan2Component.builder()
                .luntan2Module(new Luntan2Module(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);

        if (PreferenceUtils.isLogin()) {
            mPresenter.loadDepartmentGroup(id);
            mPresenter.loadFollowList(0);
        } else {
            ViewStub stub = rootView.findViewById(R.id.stub_login);
            View view = stub.inflate();
            mLogin = view.findViewById(R.id.iv_to_login);
            mLogin.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    Intent i = new Intent(getContext(), LoginActivity.class);
                    getActivity().startActivityForResult(i, REQ_LOGIN);
                }
            });
        }
//        if (PreferenceUtils.isLogin()) {
//            if (canDoc) {
//                mCreateDoc.setImageResource(R.drawable.btn_send_wen);
//                mCreateDoc.setVisibility(View.VISIBLE);
//            }
//        }

        mListDocs.getSwipeRefreshLayout().setColorSchemeResources(R.color.main_light_cyan, R.color.main_cyan);
        mListDocs.setLoadMoreEnabled(false);
        mListDocs.setLayoutManager(new LinearLayoutManager(getContext()));
        mListDocs.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bg_f6f6f6));
        mAdapter = new OldDocAdapter();
        mListDocs.getRecyclerView().addItemDecoration(new MenuVItemDecoration((int) getResources().getDimension(R.dimen.y24)));
        mListDocs.getRecyclerView().setAdapter(mAdapter);
//        mCreateDoc.setOnClickListener(new NoDoubleClickListener() {
//            @Override
//            public void onNoDoubleClick(View v) {
//
//                Intent intent = new Intent(getContext(), CreateRichDocActivity.class);
//                intent.putExtra(CreateRichDocActivity.TYPE_QIU_MING_SHAN, 4);
//                intent.putExtra(CreateRichDocActivity.TYPE_TAG_NAME_DEFAULT, name);
//                intent.putExtra("tagId", "81942b2a-7b74-429b-a003-fb3713a5e226");
//                intent.putExtra("from_name", "摸鱼部");
//                intent.putExtra("from_schema", "");
//                startActivityForResult(intent, REQUEST_CODE_CREATE_DOC);
//            }
//        });
        mAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (getContext() instanceof FeedV3Activity) {

                    clickEvent("社团-关注列表点击");
                }

                DocResponse docResponse = mAdapter.getItem(position);
                Intent i = new Intent(getContext(), NewDocDetailActivity.class);
                i.putExtra("uuid", docResponse.getId());
                i.putExtra("title", "社团");
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
                    mPresenter.loadFollowList(0);
                } else {
                    mPresenter.loadFollowList(mAdapter.getItemCount());
                }
            }

            @Override
            public void onRefresh() {
                isLoading = true;
                isRefresh = true;
                mPresenter.loadFollowList(0);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_LOGIN && resultCode == RESPONSE_LOGIN_SUCCESS) {
            mPresenter.loadFollowList(0);
        } else if (requestCode == REQUEST_CODE_CREATE_DOC && resultCode == RESULT_OK) {
            mPresenter.loadFollowList(0);
        }
    }

    @Override
    public void release() {
        if (mPresenter != null) {
            mPresenter.release();
        }
    }

    @Override
    public void onLoadGroupSuccess(ArrayList<DepartmentGroupEntity> entities) {
        mListDocs.setVisibility(View.VISIBLE);
        if (mLogin != null) {
            mLogin.setVisibility(View.GONE);
        }
        if (entities.size() > 0) {
            if (groupView == null) {
                groupView = LayoutInflater.from(getContext()).inflate(R.layout.item_department_group, null);
                mAdapter.addHeaderView(groupView);
            }
            final DepartmentGroupEntity entity = entities.get(0);
            ImageView cover = groupView.findViewById(R.id.iv_group_img);
            TextView title = groupView.findViewById(R.id.tv_group_name);
            TextView num = groupView.findViewById(R.id.tv_group_num);
            ImageView addGroup = groupView.findViewById(R.id.iv_add_group);

            int size = (int) getResources().getDimension(R.dimen.y80);
            Glide.with(getContext())
                    .load(StringUtils.getUrl(getContext(), entity.getCover(), size, size, false, true))
                    .error(R.drawable.shape_gray_e8e8e8_background)
                    .placeholder(R.drawable.shape_gray_e8e8e8_background)
                    .bitmapTransform(new RoundedCornersTransformation(getContext(), (int) getResources().getDimension(R.dimen.y8), 0))
                    .into(cover);
            title.setText(entity.getGroupName());
            num.setText(entity.getUsers() + " 人");
            addGroup.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    if (entity.isJoin()) {
                        Uri uri = Uri.parse("rong://" + getContext().getApplicationInfo().packageName).buildUpon()
                                .appendPath("conversation").appendPath(Conversation.ConversationType.GROUP.getName())
                                .appendQueryParameter("targetId", entity.getId())
                                .appendQueryParameter("title", entity.getGroupName()).build();
                        IntentUtils.toActivityFromUri(getContext(), uri, null);
                    } else {
                        if (entity.isAuthority()) {
                            mPresenter.joinAuthor(entity.getId(), entity.getGroupName());
                        } else {
                            Uri uri = Uri.parse("rong://" + getContext().getApplicationInfo().packageName).buildUpon()
                                    .appendPath("conversation").appendPath("detail")
                                    .appendQueryParameter("targetId", entity.getId())
                                    .appendQueryParameter("title", entity.getGroupName()).build();
                            IntentUtils.toActivityFromUri(getContext(), uri, null);
                        }
                    }
                }
            });
        } else {
            if (groupView != null) {
                mAdapter.removeHeaderView(groupView);
                groupView = null;
            }
        }
    }

    @Override
    public void onLoadDocListSuccess(ArrayList<DocResponse> responses, boolean isPull) {

    }

    public void likeTag(boolean isLike, int position, TagLikeEntity entity, int parentPosition) {
        mPresenter.likeTag(isLike, position, entity, parentPosition);
    }

    @Override
    public void onLoadFollowListSuccess(ArrayList<DocResponse> responses, boolean isPull) {
//        if (canDoc) {
//            mCreateDoc.setImageResource(R.drawable.btn_send_wen);
//            mCreateDoc.setVisibility(View.VISIBLE);
//        }
        mListDocs.setVisibility(View.VISIBLE);
        if (mLogin != null) {
            mLogin.setVisibility(View.GONE);
        }
        isLoading = false;
        mListDocs.setComplete();
        mListDocs.setLoadMoreEnabled(true);
        if (isPull) {
            int docPosition = PreferenceUtils.getDocPosition(getContext());
            if (responses != null && responses.size() > 0) {
                int position = responses.get(0).getPosition();
                EventBus.getDefault().post(new DocEvent(docPosition - position));
                PreferenceUtils.setDocPosition(getContext(), position);
            } else {
                EventBus.getDefault().post(new DocEvent(0));
            }

            mAdapter.setList(responses);
        } else {
            mAdapter.addList(responses);
        }
    }

    public void likeDoc(String id, boolean isLie, int position) {
        mPresenter.likeDoc(id, isLie, position);
    }

    @Override
    public void onJoinSuccess(String id, String name) {
        Uri uri = Uri.parse("rong://" + getContext().getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(Conversation.ConversationType.GROUP.getName())
                .appendQueryParameter("targetId", id)
                .appendQueryParameter("title", name).build();
        IntentUtils.toActivityFromUri(getContext(), uri, null);
    }

    @Override
    public void onLikeDocSuccess(boolean isLike, int position) {
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
        if (getContext() instanceof FeedV3Activity) {
            ((FeedV3Activity) getContext()).finalizeDialog();
        }
        DocTagEntity docTagEntity = new DocTagEntity();
        docTagEntity.setId(s);
        docTagEntity.setName(name);
        mAdapter.getList().get(position).getTags().add(mAdapter.getList().get(position).getTags().size(), docTagEntity);
        mAdapter.notifyItemChanged(position);
    }

    @Override
    public void onPlusLabel(int position, boolean isLike, int parentPosition) {
        if (getContext() instanceof FeedV3Activity) {
            ((FeedV3Activity) getContext()).finalizeDialog();
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
        stayEvent("社团-关注");
    }

    public void onSmoothScrollBy() {
        mListDocs.getRecyclerView().smoothScrollToPosition(0);
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
    public void onFailure(int code, String msg) {
        isLoading = false;
        mListDocs.setComplete();
        ErrorCodeUtils.showErrorMsgByCode(getContext(), code, msg);
    }
}
