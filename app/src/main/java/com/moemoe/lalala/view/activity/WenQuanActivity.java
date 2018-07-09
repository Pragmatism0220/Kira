package com.moemoe.lalala.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.di.components.DaggerOldDocComponent;
import com.moemoe.lalala.di.modules.OldDocModule;
import com.moemoe.lalala.model.entity.BannerEntity;
import com.moemoe.lalala.model.entity.DocResponse;
import com.moemoe.lalala.model.entity.DocTagEntity;
import com.moemoe.lalala.model.entity.SimpleUserEntity;
import com.moemoe.lalala.model.entity.TagLikeEntity;
import com.moemoe.lalala.model.entity.TagSendEntity;
import com.moemoe.lalala.presenter.OldDocContract;
import com.moemoe.lalala.presenter.OldDocPresenter;
import com.moemoe.lalala.utils.DialogUtils;
import com.moemoe.lalala.utils.ErrorCodeUtils;
import com.moemoe.lalala.utils.IntentUtils;
import com.moemoe.lalala.utils.MenuVItemDecoration;
import com.moemoe.lalala.utils.NetworkUtils;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.view.adapter.OldDocAdapter;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;
import com.moemoe.lalala.view.widget.recycler.PullAndLoadView;
import com.moemoe.lalala.view.widget.recycler.PullCallback;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;


/**
 * Created by yi on 2016/11/30.
 */

public class WenQuanActivity extends BaseAppCompatActivity implements OldDocContract.View {

    private final String EXTRA_NAME = "name";

    @BindView(R.id.iv_back)
    View mIvBack;
    @BindView(R.id.tv_toolbar_title)
    TextView mTitle;
    @BindView(R.id.rv_list)
    PullAndLoadView mListDocs;
    @BindView(R.id.iv_send_post)
    ImageView mIvSend;
    @BindView(R.id.iv_menu_list)
    ImageView mIvMenuList;

    @Inject
    OldDocPresenter mPresenter;
    private OldDocAdapter mListAdapter;
    private String mRoomId;
    private boolean mIsLoading = false;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_bar_list;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        ViewUtils.setStatusBarLight(getWindow(), $(R.id.top_view));
        Intent i = getIntent();
        mRoomId = "";

        clickEvent("温泉");
        if (i != null) {
            String roomId = i.getStringExtra(UUID);
            if (!TextUtils.isEmpty(roomId)) {
                mRoomId = roomId;
            }
            String title = i.getStringExtra(EXTRA_NAME);
            if (!TextUtils.isEmpty(title)) {
                mTitle.setText(title);
                mTitle.setVisibility(View.VISIBLE);
            } else {
                mTitle.setVisibility(View.GONE);
            }
        }
        mTitle.setTextColor(ContextCompat.getColor(this, R.color.main_cyan));
        DaggerOldDocComponent.builder()
                .oldDocModule(new OldDocModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        mListDocs.getSwipeRefreshLayout().setColorSchemeResources(R.color.main_light_cyan, R.color.main_cyan);
        mListAdapter = new OldDocAdapter();
        mListDocs.getRecyclerView().addItemDecoration(new MenuVItemDecoration((int) getResources().getDimension(R.dimen.y24)));
        mListDocs.getRecyclerView().setAdapter(mListAdapter);
        mListDocs.setLayoutManager(new LinearLayoutManager(this));
        mListDocs.setLoadMoreEnabled(false);
        ImageView mBtnView = new ImageView(this);
        mBtnView.setImageResource(R.drawable.btn_show_pool_rank);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = (int) getResources().getDimension(R.dimen.y20);
        lp.leftMargin = (int) getResources().getDimension(R.dimen.x20);
        lp.gravity = Gravity.BOTTOM;
        mListDocs.addView(mBtnView, lp);
        mBtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.checkNetworkAndShowError(WenQuanActivity.this) && DialogUtils.checkLoginAndShowDlg(WenQuanActivity.this)) {
                    String temp = "neta://com.moemoe.lalala/url_inner_1.0?http://www.moemoe.la/shuirank/?token=" + PreferenceUtils.getToken();
                    Uri uri = Uri.parse(temp);
                    IntentUtils.toActivityFromUri(WenQuanActivity.this, uri, null);
                }
            }
        });
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {
        mIvMenuList.setVisibility(View.VISIBLE);
        mIvMenuList.setImageResource(R.drawable.btn_trends_search);
        mIvMenuList.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (DialogUtils.checkLoginAndShowDlg(WenQuanActivity.this)) {
                    clickEvent("地图-搜索");
                    Intent i6 = new Intent(WenQuanActivity.this, AllSearchActivity.class);
                    i6.putExtra("type", "all");
                    startActivity(i6);
                }
            }
        });
    }

    public void likeTag(boolean isLike, int position, TagLikeEntity entity, int parentPosition) {
        mPresenter.likeTag(isLike, position, entity, parentPosition);
    }

    @Override
    protected void initListeners() {
        mIvBack.setVisibility(View.VISIBLE);
        mIvBack.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                finish();
            }
        });
        mListAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                DocResponse docResponse = mListAdapter.getItem(position);
                Intent i = new Intent(WenQuanActivity.this, NewDocDetailActivity.class);
                i.putExtra(UUID, docResponse.getId());
                i.putExtra("title", "温泉");
                startActivity(i);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        mIvSend.setVisibility(View.VISIBLE);
        mIvSend.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                Intent intent = new Intent(WenQuanActivity.this, CreateRichDocActivity.class);
                intent.putExtra(CreateRichDocActivity.TYPE_QIU_MING_SHAN, 2);
                intent.putExtra(CreateRichDocActivity.TYPE_TAG_NAME_DEFAULT, "温泉");
                intent.putExtra("from_name", "温泉");
                intent.putExtra("departmentId", mRoomId);
                intent.putExtra("from_schema", "neta://com.moemoe.lalala/swim_2.0");
                startActivity(intent);
            }
        });
        mListDocs.setPullCallback(new PullCallback() {
            @Override
            public void onLoadMore() {
                mIsLoading = true;
                mPresenter.loadOldDocList("swim", mListAdapter.getItem(mListAdapter.getItemCount() - 1).getTimestamp());
            }

            @Override
            public void onRefresh() {
                mIsLoading = true;
                mPresenter.loadOldDocList("swim", 0);
            }

            @Override
            public boolean isLoading() {
                return mIsLoading;
            }

            @Override
            public boolean hasLoadedAllItems() {
                return false;
            }
        });
        mPresenter.loadOldDocList("swim", 0);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) mPresenter.release();
        stayEvent("wenquan");
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseTime();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTime();
    }

    @Override
    public void onFailure(int code, String msg) {
        mIsLoading = false;
        mListDocs.setComplete();
        ErrorCodeUtils.showErrorMsgByCode(WenQuanActivity.this, code, msg);
    }

    @Override
    public void onCreateLabel(String s, String name, int position) {
        showToast("添加成功");
        finalizeDialog();
        DocTagEntity docTagEntity = new DocTagEntity();
        docTagEntity.setId(s);
        docTagEntity.setName(name);
        int size = mListAdapter.getList().get(position).getTags().size();
        size = size == 0 ? 0 : size;
        mListAdapter.getList().get(position).getTags().add(size, docTagEntity);
        mListAdapter.notifyItemChanged(position);
    }

    @Override
    public void onLoadDocLikeSuccess(boolean isLike, int position) {
//        NewDynamicEntity entity = new Gson().fromJson(mAdapter.getList().get(position).getTargetObj(),NewDynamicEntity.class);
        mListAdapter.getList().get(position).setThumb(isLike);
        if (isLike) {
            SimpleUserEntity userEntity = new SimpleUserEntity();
            userEntity.setUserName(PreferenceUtils.getAuthorInfo().getUserName());
            userEntity.setUserId(PreferenceUtils.getUUid());
            userEntity.setUserIcon(PreferenceUtils.getAuthorInfo().getHeadPath());
            mListAdapter.getList().get(position).getThumbUsers().add(0, userEntity);
            mListAdapter.getList().get(position).setThumbs(mListAdapter.getList().get(position).getThumbs() + 1);
        } else {
            for (SimpleUserEntity userEntity : mListAdapter.getList().get(position).getThumbUsers()) {
                if (userEntity.getUserId().equals(PreferenceUtils.getUUid())) {
                    mListAdapter.getList().get(position).getThumbUsers().remove(userEntity);
                    break;
                }
            }
            mListAdapter.getList().get(position).setThumbs(mListAdapter.getList().get(position).getThumbs() - 1);
        }
        if (mListAdapter.getHeaderLayoutCount() != 0) {
            mListAdapter.notifyItemChanged(position + 1);
        } else {
            mListAdapter.notifyItemChanged(position);
        }
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
    public void loadOldDocListSuccess(ArrayList<DocResponse> list, boolean isPull) {
        mIsLoading = false;
        mListDocs.setLoadMoreEnabled(true);
        mListDocs.setComplete();
        if (isPull) {
            mListAdapter.setList(list);
        } else {
            mListAdapter.addList(list);
        }
    }

    @Override
    public void onBannerLoadSuccess(ArrayList<BannerEntity> bannerEntities) {

    }

    @Override
    public void onPlusLabel(int position, boolean isLike, int parentPosition) {
        finalizeDialog();
        DocResponse docResponse = mListAdapter.getList().get(parentPosition);
        if (docResponse.getTags() != null && docResponse.getTags().size() > 0) {
            DocTagEntity tagEntity = docResponse.getTags().get(position);
            docResponse.getTags().remove(position);
            tagEntity.setLiked(isLike);
            if (isLike) {
                tagEntity.setLikes(tagEntity.getLikes() + 1);
                docResponse.getTags().add(position, tagEntity);
                docResponse.setTagLikes(docResponse.getTagLikes() + 1);
                mListAdapter.getList().add(parentPosition, docResponse);
            } else {
                tagEntity.setLikes(tagEntity.getLikes() - 1);
                if (tagEntity.getLikes() > 0) {
                    docResponse.getTags().add(position, tagEntity);
                    docResponse.setTagLikes(docResponse.getTagLikes() - 1);
                    mListAdapter.getList().add(parentPosition, docResponse);
                }
            }
            if (mListAdapter.getHeaderLayoutCount() != 0) {
                mListAdapter.notifyItemChanged(parentPosition + 1);
            } else {
                mListAdapter.notifyItemChanged(parentPosition);
            }
        }
    }
}
