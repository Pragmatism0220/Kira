package com.moemoe.lalala.view.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.di.components.DaggerNewFeedComponent;
import com.moemoe.lalala.di.modules.NewFeedModule;
import com.moemoe.lalala.event.CommentEvent;
import com.moemoe.lalala.event.CountEvent;
import com.moemoe.lalala.model.entity.BannerEntity;
import com.moemoe.lalala.model.entity.Comment24Entity;
import com.moemoe.lalala.model.entity.DepartmentGroupEntity;
import com.moemoe.lalala.model.entity.DiscoverEntity;
import com.moemoe.lalala.model.entity.DocResponse;
import com.moemoe.lalala.model.entity.DocTagEntity;
import com.moemoe.lalala.model.entity.GiveCoinEntity;
import com.moemoe.lalala.model.entity.NewDynamicEntity;
import com.moemoe.lalala.model.entity.ShowFolderEntity;
import com.moemoe.lalala.model.entity.SimpleUserEntity;
import com.moemoe.lalala.model.entity.TagLikeEntity;
import com.moemoe.lalala.model.entity.TagSendEntity;
import com.moemoe.lalala.model.entity.XianChongEntity;
import com.moemoe.lalala.presenter.NewFeedContract;
import com.moemoe.lalala.presenter.NewFeedPresenter;
import com.moemoe.lalala.utils.BannerImageLoader;
import com.moemoe.lalala.utils.ErrorCodeUtils;
import com.moemoe.lalala.utils.IntentUtils;
import com.moemoe.lalala.utils.NewCommunityDecoration;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.view.activity.BottomSheetAdapter;
import com.moemoe.lalala.view.activity.CreateCommentV2Activity;
import com.moemoe.lalala.view.activity.FeedV3Activity;
import com.moemoe.lalala.view.activity.LoginActivity;
import com.moemoe.lalala.view.activity.NewDocDetailActivity;
import com.moemoe.lalala.view.adapter.OldDocAdapter;
import com.moemoe.lalala.view.adapter.XianChongListAdapter;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;
import com.moemoe.lalala.view.widget.recycler.PullAndLoadView;
import com.moemoe.lalala.view.widget.recycler.PullCallback;
import com.moemoe.lalala.view.widget.view.SlideScrollView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;
import static com.moemoe.lalala.utils.StartActivityConstant.REQ_LOGIN;
import static com.moemoe.lalala.utils.StartActivityConstant.REQ_SELECT_TAG;
import static com.moemoe.lalala.view.activity.LoginActivity.RESPONSE_LOGIN_SUCCESS;

/**
 * Created by yi on 2017/9/4.
 */

public class NewFollowMainV3Fragment extends BaseFragment implements NewFeedContract.View {

    @BindView(R.id.list)
    PullAndLoadView mListDocs;
    @BindView(R.id.iv_to_wen)
    ImageView mIvCreateDynamic;

    @Inject
    NewFeedPresenter mPresenter;

    private Banner banner;
    private View bannerView;
    private View xianChongView;
    private View hotView;
    private OldDocAdapter mAdapter;
    private long minIdx;
    private long maxIdx;
    private boolean isLoading = false;
    private String type;
    private String id;
    View mLogin;
    private String mDocId;


    private RecyclerView mList;
    private ImageView mClose;
    private TextView mTo;

    private BottomSheetAdapter mSheetAdapter;
    private BottomSheetDialog mDialog;
    private BottomSheetBehavior mBehavior;


    public static NewFollowMainV3Fragment newInstance(String type) {
        NewFollowMainV3Fragment fragment = new NewFollowMainV3Fragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static NewFollowMainV3Fragment newInstance(String type, String userId) {
        NewFollowMainV3Fragment fragment = new NewFollowMainV3Fragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
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
        clickEvent("社团-广场");
        id = "tag";
        DaggerNewFeedComponent.builder()
                .newFeedModule(new NewFeedModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);

        if (PreferenceUtils.isLogin()) {
            mPresenter.loadOldDocList(id, 0);

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
        type = getArguments().getString("type");
        EventBus.getDefault().register(this);
        if (PreferenceUtils.isLogin()) {
            if ("ground".equals(type)) {
                mPresenter.requestBannerData("CLASSROOM");
                mPresenter.loadXianChongList();
//                mIvCreateDynamic.setVisibility(View.GONE);
//                mIvCreateDynamic.setImageResource(R.drawable.btn_send_wen);
//                mIvCreateDynamic.setOnClickListener(new NoDoubleClickListener() {
//                    @Override
//                    public void onNoDoubleClick(View v) {
//
//                        clickEvent("社团-广场发帖");
//
//                        Intent intent = new Intent(getContext(), CreateRichDocActivity.class);
//                        intent.putExtra(CreateRichDocActivity.TYPE_QIU_MING_SHAN, 4);
////                    intent.putExtra(CreateRichDocActivity.TYPE_TAG_NAME_DEFAULT, name);
//                        intent.putExtra("tagId", "81942b2a-7b74-429b-a003-fb3713a5e226");
//                        intent.putExtra("from_name", "摸鱼部");
//                        intent.putExtra("from_schema", "");
//                        startActivityForResult(intent, REQUEST_CODE_CREATE_DOC);
//                    }
//                });
            }
        }


        mListDocs.getSwipeRefreshLayout().setColorSchemeResources(R.color.main_light_cyan, R.color.main_cyan);
        mListDocs.setLoadMoreEnabled(false);
        mListDocs.setLayoutManager(new LinearLayoutManager(getContext()));
        mListDocs.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bg_f6f6f6));
        mAdapter = new OldDocAdapter();
        mListDocs.getRecyclerView().addItemDecoration(new NewCommunityDecoration((int) getResources().getDimension(R.dimen.y24), 2));
        mListDocs.getRecyclerView().setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (getContext() instanceof FeedV3Activity) {
                    clickEvent("社团-广场列表点击");
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


        minIdx = PreferenceUtils.getDiscoverMinIdx(getContext(), type);
        maxIdx = PreferenceUtils.getDiscoverMaxIdx(getContext(), type);


        mListDocs.setPullCallback(new PullCallback() {
            @Override
            public void onLoadMore() {
                isLoading = true;
                if (mAdapter.getList().size() == 0) {
                    mPresenter.loadOldDocList(id, 0);
                } else {
                    mPresenter.loadOldDocList(id, mAdapter.getItem(mAdapter.getList().size() - 1).getTimestamp());
                }
            }

            @Override
            public void onRefresh() {
                isLoading = true;
                mPresenter.loadOldDocList(id, 0);
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

    private void showSheetDialog(final String id) {
        View view = View.inflate(getContext(), R.layout.dialog_bottom_sheet, null);
        mList = view.findViewById(R.id.dialog_bottomsheet_list);
        mClose = view.findViewById(R.id.dialog_bottomsheet_close);
        mTo = view.findViewById(R.id.dialog_bottomsheet_bottom);
        mSheetAdapter = new BottomSheetAdapter();
        mList.setHasFixedSize(true);
        mList.setLayoutManager(new LinearLayoutManager(getContext()));
        mList.setItemAnimator(new DefaultItemAnimator());
        mList.setAdapter(mSheetAdapter);
        mDialog = new BottomSheetDialog(getContext(), R.style.dialog);
        mDialog.setContentView(view);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());
//        mBehavior.setPeekHeight(800);
        mBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        if (mDialog != null) {
            mDialog.show();
        }
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDialog != null) {
                    mDialog.dismiss();
                }
            }
        });
        mTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDialog != null) {
                    CreateCommentV2Activity.startActivity(getContext(), id, false, "", 0, id);
                }
            }
        });


        //CreateCommentV2Activity.startActivity(context, entity.getId(), false, "", 0, entity.getId());

    }

    public void onSmoothScrollBy() {
        if (mListDocs != null) {
            mListDocs.getRecyclerView().smoothScrollToPosition(0);
        }
    }

    public void likeTag(boolean isLike, int position, TagLikeEntity entity, int parentPosition) {
        mPresenter.likeTag(isLike, position, entity, parentPosition);
    }

    public void likeDoc(String id, boolean isLie, int position) {
        mPresenter.likeDoc(id, isLie, position);
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
    public void onLoadListSuccess(ArrayList<NewDynamicEntity> resList, boolean isPull) {

    }

    @Override
    public void onBannerLoadSuccess(final ArrayList<BannerEntity> bannerEntities) {
        mListDocs.setVisibility(View.VISIBLE);
//        mIvCreateDynamic.setVisibility(View.VISIBLE);
        if (mLogin != null) {
            mLogin.setVisibility(View.GONE);
        }
        if (bannerEntities.size() > 0) {
            if (bannerView == null) {
                bannerView = LayoutInflater.from(getContext()).inflate(R.layout.item_new_banner, null);
                banner = bannerView.findViewById(R.id.banner);
                mAdapter.addHeaderView(bannerView, 0);
            }
            banner.setImages(bannerEntities)
                    .setImageLoader(new BannerImageLoader())
                    .setDelayTime(4000)
                    .setIndicatorGravity(BannerConfig.CENTER)
                    .start();
            banner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {

                    clickEvent("社团-广场banner");

                    BannerEntity bean = bannerEntities.get(position);
                    if (!TextUtils.isEmpty(bean.getSchema())) {
                        Uri uri = Uri.parse(bean.getSchema());
                        IntentUtils.toActivityFromUri(getContext(), uri, null);
                    }
                }
            });
        } else {
            if (bannerView != null) {
                mAdapter.removeHeaderView(bannerView);
                bannerView = null;
            }
        }
    }

    @Override
    public void onLoadXianChongSuccess(ArrayList<XianChongEntity> entities) {
        mListDocs.setVisibility(View.VISIBLE);
        if (mLogin != null) {
            mLogin.setVisibility(View.GONE);
        }
        if (entities.size() > 0) {
            if (xianChongView == null) {
                xianChongView = LayoutInflater.from(getContext()).inflate(R.layout.item_class_featured, null);

                int count = mAdapter.getHeaderViewCount();
                if (count == 0) {
                    mAdapter.addHeaderView(xianChongView);
                } else if (count == 1) {
                    View view = mAdapter.getmHeaderLayout().getChildAt(0);
                    if (view == bannerView) {
                        mAdapter.addHeaderView(xianChongView);
                    } else {
                        mAdapter.addHeaderView(xianChongView, 0);
                    }
                } else if (count == 2) {
                    mAdapter.addHeaderView(xianChongView, 1);
                }

            }
            RecyclerView rvList = xianChongView.findViewById(R.id.rv_class_featured);
            rvList.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.y180)));
            rvList.setBackgroundColor(Color.WHITE);
            final XianChongListAdapter recyclerViewAdapter = new XianChongListAdapter();
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            rvList.setLayoutManager(layoutManager);
            TextView text = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.item_text, null);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER_VERTICAL;
            lp.leftMargin = (int) getResources().getDimension(R.dimen.x20);
            lp.rightMargin = (int) getResources().getDimension(R.dimen.x12);
            text.setLayoutParams(lp);
            recyclerViewAdapter.addHeaderView(text, -1, LinearLayout.HORIZONTAL);
            rvList.setAdapter(recyclerViewAdapter);
            recyclerViewAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    XianChongEntity entity = recyclerViewAdapter.getItem(position);
                    ViewUtils.toPersonal(getContext(), entity.getUserId());
                }

                @Override
                public void onItemLongClick(View view, int position) {

                }
            });
            recyclerViewAdapter.setList(entities);
        } else {
            if (xianChongView != null) {
                mAdapter.removeHeaderView(xianChongView);
                xianChongView = null;
            }
        }
    }

    @Override
    public void onLoadFolderSuccess(ArrayList<ShowFolderEntity> entities) {

    }

    @Override
    public void onLoadCommentSuccess(Comment24Entity entity) {

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
    public void onLoadDiscoverListSuccess(ArrayList<DiscoverEntity> entities, boolean isPull) {

    }

    @Override
    public void loadOldDocListSuccess(ArrayList<DocResponse> list, boolean isPull) {
        mListDocs.setVisibility(View.VISIBLE);
        if (mLogin != null) {
            mLogin.setVisibility(View.GONE);
        }
        isLoading = false;
        mListDocs.setComplete();
        mListDocs.setLoadMoreEnabled(true);
        if (isPull) {
            mAdapter.setList(list);
        } else {
            mAdapter.addList(list);
        }
    }

    /**
     * 标签点击成功
     *
     * @param position
     * @param isLike
     * @param parentPosition
     */
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

//    public void giveCoin(int count) {
//
//        GiveCoinEntity bean = new GiveCoinEntity(count, mDocId);
//        mPresenter.giveCoin(bean);
//    }

    @Override
    public void onGiveCoin(int coins) {
        Toast.makeText(getContext(), R.string.label_give_coin_success, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadGroupSuccess(ArrayList<DepartmentGroupEntity> entity) {

    }

    @Override
    public void onLoadDocListSuccess(ArrayList<DocResponse> responses, boolean isPull) {
        mListDocs.setVisibility(View.VISIBLE);
        if (mLogin != null) {
            mLogin.setVisibility(View.GONE);
        }
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
    public void onJoinSuccess(String id, String name) {

    }

    @Override
    public void onCreateLabel(String s, String name, int position) {
        ((FeedV3Activity) getContext()).showToast("添加成功");
        if (getContext() instanceof FeedV3Activity) {
            ((FeedV3Activity) getContext()).finalizeDialog();
        }

        DocTagEntity tag = new DocTagEntity();
        tag.setLiked(true);
        tag.setId(s);
        tag.setLikes(1);
        tag.setName(name);
        mAdapter.getList().get(position).getTags().add(tag);
        if (mAdapter.getHeaderLayoutCount() != 0) {
            mAdapter.notifyItemChanged(position + 1);
        } else {
            mAdapter.notifyItemChanged(position);
        }

    }

    private void changeIdx(ArrayList<DiscoverEntity> entities) {
        if (entities.size() > 0) {
            long min = entities.get(0).getTimestamp();
            long max = entities.get(0).getTimestamp();
            for (DiscoverEntity entity : entities) {
                if (entity.getTimestamp() == 0) {
                    min = 0;
                    max = 0;
                    break;
                } else if (entity.getTimestamp() > max) {
                    max = entity.getTimestamp();
                } else if (entity.getTimestamp() < min) {
                    min = entity.getTimestamp();
                }
            }
            if (min == 0 && max == 0) {
                minIdx = maxIdx = 0;
            } else {
                if (min < minIdx || minIdx == 0) {
                    minIdx = min;
                }
                if (max > maxIdx) {
                    maxIdx = max;
                }
            }
            if ("random".equals(type)) {
                PreferenceUtils.setDiscoverMinIdx(getContext(), type, minIdx);
                PreferenceUtils.setDiscoverMaxIdx(getContext(), type, maxIdx);
            }
        }
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
        if (requestCode == REQ_LOGIN && resultCode == RESPONSE_LOGIN_SUCCESS) {
            mPresenter.loadOldDocList(id, 0);
            mPresenter.requestBannerData("CLASSROOM");
            mPresenter.loadXianChongList();
        } else if (requestCode == REQ_SELECT_TAG && resultCode == RESULT_OK) {
            mPresenter.loadOldDocList(id, 0);
            mPresenter.requestBannerData("CLASSROOM");
            mPresenter.loadXianChongList();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(CountEvent event) {
        if (event != null) {
            GiveCoinEntity bean = new GiveCoinEntity(event.getCount(), event.getDocId());
            mPresenter.giveCoin(bean);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommentEvent(CommentEvent event) {
        if (event != null) {
            showSheetDialog(event.getId());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stayEvent("社团-广场");
        EventBus.getDefault().unregister(this);
    }

}
