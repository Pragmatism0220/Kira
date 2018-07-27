package com.moemoe.lalala.view.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.moemoe.lalala.BuildConfig;
import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.di.components.DaggerDetailComponent;
import com.moemoe.lalala.di.modules.DetailModule;
import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.api.NetSimpleResultSubscriber;
import com.moemoe.lalala.model.entity.BagDirEntity;
import com.moemoe.lalala.model.entity.CommentV2Entity;
import com.moemoe.lalala.model.entity.DocDetailEntity;
import com.moemoe.lalala.model.entity.DocDetailNormalEntity;
import com.moemoe.lalala.model.entity.DocTagEntity;
import com.moemoe.lalala.model.entity.FolderType;
import com.moemoe.lalala.model.entity.GiveCoinEntity;
import com.moemoe.lalala.model.entity.Image;
import com.moemoe.lalala.model.entity.REPORT;
import com.moemoe.lalala.model.entity.RichDocListEntity;
import com.moemoe.lalala.model.entity.RichEntity;
import com.moemoe.lalala.model.entity.ShareArticleEntity;
import com.moemoe.lalala.model.entity.SimpleUserV2Entity;
import com.moemoe.lalala.model.entity.TagLikeEntity;
import com.moemoe.lalala.model.entity.TagSendEntity;
import com.moemoe.lalala.model.entity.UserFollowTagEntity;
import com.moemoe.lalala.model.entity.UserTopEntity;
import com.moemoe.lalala.presenter.DocDetailContract;
import com.moemoe.lalala.presenter.DocDetailPresenter;
import com.moemoe.lalala.utils.AlertDialogUtil;
import com.moemoe.lalala.utils.AndroidBug5497Workaround;
import com.moemoe.lalala.utils.DialogUtils;
import com.moemoe.lalala.utils.ErrorCodeUtils;
import com.moemoe.lalala.utils.FileUtil;
import com.moemoe.lalala.utils.NetworkUtils;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.utils.ShareUtils;
import com.moemoe.lalala.utils.SoftKeyboardUtils;
import com.moemoe.lalala.utils.StorageUtils;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.utils.ToastUtils;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.view.adapter.DocRecyclerViewAdapter;
import com.moemoe.lalala.view.adapter.MusicListAdapter;
import com.moemoe.lalala.view.adapter.OnItemClickListener;
import com.moemoe.lalala.view.widget.netamenu.BottomMenuFragment;
import com.moemoe.lalala.view.widget.netamenu.MenuItem;
import com.moemoe.lalala.view.widget.recycler.PullAndLoadView;
import com.moemoe.lalala.view.widget.recycler.PullCallback;
import com.moemoe.lalala.view.widget.view.KeyboardListenerLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.ToDoubleBiFunction;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * 文章详情
 * Created by yi on 2016/12/2.
 */

public class NewDocDetailActivity extends BaseAppCompatActivity implements DocDetailContract.View {
    private final int MENU_FAVORITE = 102;
    private final int MENU_SHARE = 103;
    private final int MENU_REPORT = 104;
    private final int MENU_DELETE = 106;
    private final int TAG_DELETE = 107;
    private final int EDIT_DOC = 108;
    private final int MENU_EGG = 109;
    private final int MENU_FORWARD = 110;
    private final int MENU_SUBMISSION = 111;
    private final int MENU_TOP = 112;
    private final int MENU_BOUTIQUE = 113;
    private final int MENU_JUMPLZ = 114;
    private final int MENU_DELETE_V2 = 115;
    private final int ICON_NUM_LIMIT = 9;
    private final int REQ_GET_EDIT_VERSION_IMG = 2333;
    private final int REQ_TO_FOLDER = 30003;
    private final int REQ_DELETE_TAG = 30004;
    private final int RESULT_CREATE = 30005;

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_left_menu)
    TextView mTvFrom;
    @BindView(R.id.rv_list)
    PullAndLoadView mList;
    @BindView(R.id.iv_menu_list)
    ImageView mIvMenu;
    //    @BindView(R.id.edt_comment_input)
//    EditText mEdtCommentInput;
    @BindView(R.id.iv_comment_send)
    View mTvSendComment;
    @BindView(R.id.tv_doc_content)
    TextView mTvComment;
    @BindView(R.id.rl_send_root)
    View mSendRoot;
    @BindView(R.id.iv_go_pinlun)
    ImageView mGoPinLun;
    @BindView(R.id.rl_list_bottom_root_2)
    RelativeLayout mRlListBottm;
    @BindView(R.id.tv_forward_num_2)
    TextView mTvForwardNum;
    @BindView(R.id.tv_comment_num_2)
    TextView mTvCommentNum;
    @BindView(R.id.tv_like_num)
    TextView mTvLikeNum;
    @BindView(R.id.rl_tag_root_2)
    RelativeLayout mRlTagRoot2;
    @BindView(R.id.fl_tag_root_2)
    FrameLayout mFlTagRoot2;
    @BindView(R.id.iv_like_item)
    ImageView mIvLikeItem;
    @BindView(R.id.ll_like_user_root)
    LinearLayout mLlLikeUserRoot;
    @BindView(R.id.rl_community)
    RelativeLayout mRlCommunity;
    @BindView(R.id.tv_community_name)
    TextView mTvCommunityName;
    @BindView(R.id.tv_join)
    TextView mJoin;

    @Inject
    DocDetailPresenter mPresenter;
    /**
     * 当前回复给谁
     */
    private String mToUserId;
    private String mDocId;
    private String mUserName;
    private String mShareDesc;
    private String mShareTitle;
    private String mShareIcon;
    private DocRecyclerViewAdapter mAdapter;
    private BottomMenuFragment bottomMenuFragment;
    private int mCurType = 0;//0.对楼主 1.对某楼 2.对标签
    private ArrayList<Image> mImages;
    private ArrayList<String> mIconPaths = new ArrayList<>();
    private boolean hasLoaded = false;
    private ArrayList<DocTagEntity> mDocTags;
    private int mPosition;
    private int mCommentNum;
    private boolean isLoading;
    private KeyboardListenerLayout mKlCommentBoard;
    private int mPrePosition;
    private boolean isFrist = true;
    private String lzType;
    private Gson gson = new Gson();
    private boolean isPinLun;
    private String title;
    private String content;
    private String tagId;
    private Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mRlCommunity != null) {
                if (mRlCommunity.getVisibility() == View.VISIBLE) {
//                    mRlCommunity.setVisibility(View.GONE);
                    showIn();
                }
            }
        }
    };
    private boolean isHideShare = false;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_one_recycleview;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        ViewUtils.setStatusBarLight(getWindow(), $(R.id.top_view));
        AndroidBug5497Workaround.assistActivity(this);
        DaggerDetailComponent.builder()
                .detailModule(new DetailModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        mDocId = getIntent().getStringExtra(UUID);
        mPosition = !TextUtils.isEmpty(getIntent().getStringExtra("position")) ? Integer.valueOf(getIntent().getStringExtra("position")) : -1;
        if (TextUtils.isEmpty(mDocId)) {
            finish();
        }
        title = getIntent().getStringExtra("title");
        isPinLun = getIntent().getBooleanExtra("isPinLun", false);
        mUserName = "";
        mShareDesc = "";
        mShareTitle = "";
        mShareIcon = "";
        mIvMenu.setVisibility(View.VISIBLE);
        mList.setLoadMoreEnabled(true);
        isLoading = true;
        mList.getSwipeRefreshLayout().setColorSchemeResources(R.color.main_light_cyan, R.color.main_cyan);
        mAdapter = new DocRecyclerViewAdapter(this);
        mList.getRecyclerView().setAdapter(mAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mList.setLayoutManager(layoutManager);
        mImages = new ArrayList<>();
        mTvSendComment.setEnabled(false);
        mRlListBottm.setVisibility(View.VISIBLE);
        mRlListBottm.setBackground(getResources().getDrawable(R.color.white));

        Intent intent = getIntent();
        String userId = intent.getStringExtra("uuid");
    }


    @Override
    protected void initToolbar(Bundle savedInstanceState) {
        mTvFrom.setVisibility(View.VISIBLE);
        mTvFrom.setTextColor(ContextCompat.getColor(this, R.color.main_cyan));
        if (TextUtils.isEmpty(title)) {
            mTvFrom.setText("文章");
        } else {
            mTvFrom.setText(title);
        }
        mIvMenu.setVisibility(View.VISIBLE);

        mTvFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mList.getRecyclerView() != null) {
                    mList.getRecyclerView().smoothScrollToPosition(0);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        onBackPressed("finish");
    }

    public void onBackPressed(String type) {
        Intent i = new Intent();
        i.putExtra("position", mPosition);
        i.putExtra("type", type);
        setResult(RESULT_OK, i);
        finish();
    }

    @Override
    protected void initListeners() {
        mIvBack.setVisibility(View.VISIBLE);
        mIvBack.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                onBackPressed("finish");
            }
        });
        mTvFrom.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                onBackPressed("finish");
            }
        });
        mIvMenu.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (bottomMenuFragment != null)
                    bottomMenuFragment.show(getSupportFragmentManager(), "DocDetailMenu");
                isHideShare = false;
            }
        });
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Object o = mAdapter.getItem(position);
                if (o instanceof Image) {
                    Image img = (Image) o;
                    Intent intent = new Intent(NewDocDetailActivity.this, ImageBigSelectActivity.class);
                    intent.putExtra(ImageBigSelectActivity.EXTRA_KEY_FILEBEAN, mImages);
                    intent.putExtra(ImageBigSelectActivity.EXTRAS_KEY_FIRST_PHTOT_INDEX,
                            mImages.indexOf(img));
                    startActivity(intent);
                } else if (o instanceof DocDetailEntity.DocLink) {
                    DocDetailEntity.DocLink link = (DocDetailEntity.DocLink) o;
                    WebViewActivity.startActivity(NewDocDetailActivity.this, link.getUrl());
                } else if (o instanceof BagDirEntity) {
                    BagDirEntity entity = (BagDirEntity) o;
                    if (entity.getFolderType().equals(FolderType.ZH.toString())) {
                        NewFileCommonActivity.startActivity(NewDocDetailActivity.this, FolderType.ZH.toString(), entity.getFolderId(), entity.getUserId());
                    } else if (entity.getFolderType().equals(FolderType.TJ.toString())) {
                        NewFileCommonActivity.startActivity(NewDocDetailActivity.this, FolderType.TJ.toString(), entity.getFolderId(), entity.getUserId());
                    } else if (entity.getFolderType().equals(FolderType.MH.toString())) {
                        NewFileManHuaActivity.startActivity(NewDocDetailActivity.this, FolderType.MH.toString(), entity.getFolderId(), entity.getUserId());
                    } else if (entity.getFolderType().equals(FolderType.XS.toString())) {
                        NewFileXiaoshuoActivity.startActivity(NewDocDetailActivity.this, FolderType.XS.toString(), entity.getFolderId(), entity.getUserId());
                    } else if (entity.getFolderType().equals(FolderType.SP.toString())) {
                        FileMovieActivity.startActivity(NewDocDetailActivity.this, FolderType.SP.toString(), entity.getFolderId(), entity.getUserId());
                    } else if (entity.getFolderType().equals(FolderType.YY.toString())) {
                        FileMovieActivity.startActivity(NewDocDetailActivity.this, FolderType.YY.toString(), entity.getFolderId(), entity.getUserId());
                    }
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Object o = mAdapter.getItem(position);
                if (o instanceof String) {
                    ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData mClipData = ClipData.newPlainText("绅士内容", (String) o);
                    cmb.setPrimaryClip(mClipData);
                    ToastUtils.showShortToast(NewDocDetailActivity.this, "复制成功");
                }
            }
        });
        mList.setPullCallback(new PullCallback() {
            @Override
            public void onLoadMore() {
                isLoading = true;
                if ("楼主".equals(lzType)) {
                    if (mAdapter.getmComments() != null && mAdapter.getmComments().size() > 0) {
                        mPresenter.loadGetCommentsLz(mDocId, mAdapter.getmComments().size());
                    } else {
                        mPresenter.loadGetCommentsLz(mDocId, 0);
                    }
                } else {
                    if (mAdapter.getCommentType() == 2) {
                        if (mAdapter.getmComments() != null && mAdapter.getmComments().size() > 0) {
                            mPresenter.loadDocLikeUsers(mDocId, mAdapter.getmComments().size());
                        } else {
                            mPresenter.loadDocLikeUsers(mDocId, 0);
                        }
                    } else {
                        if (TextUtils.isEmpty(content)) {
                            if (mAdapter.getmComments() != null && mAdapter.getmComments().size() > 0) {
                                mPresenter.requestTopComment(mDocId, mAdapter.getCommentType(), mAdapter.getSortType(), mAdapter.getmComments().size(), mAdapter.getmComments().size());
                            } else {
                                mPresenter.requestTopComment(mDocId, mAdapter.getCommentType(), mAdapter.getSortType(), 0, 0);
                            }
                            return;
                        }
                        if (mAdapter.getmComments() != null && mAdapter.getmComments().size() > 0) {
                            if (mAdapter.getSortType() == 0) {
                                mPresenter.requestTopComment(mDocId, mAdapter.getCommentType(), mAdapter.getSortType(), Integer.valueOf(content) - 1 + mAdapter.getmComments().size(), mAdapter.getmComments().size());
                            } else if (mAdapter.getSortType() == 2) {
                                mPresenter.requestTopComment(mDocId, mAdapter.getCommentType(), mAdapter.getSortType(), mDoc.getComments() - Integer.valueOf(content) + mAdapter.getmComments().size(), mAdapter.getmComments().size());
                            } else {
                                mPresenter.requestTopComment(mDocId, mAdapter.getCommentType(), mAdapter.getSortType(), mAdapter.getmComments().size(), mAdapter.getmComments().size());
                            }
                        } else {
                            mPresenter.requestTopComment(mDocId, mAdapter.getCommentType(), mAdapter.getSortType(), 0, 0);
                        }
                    }
                }
            }

            @Override
            public void onRefresh() {
                isLoading = true;
                if ("楼主".equals(lzType)) {
                    mPresenter.loadGetCommentsLz(mDocId, 0);
                } else {
                    mPresenter.requestDoc(mDocId);
                }
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
        mGoPinLun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGoPinLun.setVisibility(View.GONE);
//                mRlCommunity.setVisibility(View.GONE);
                showIn();
                if (mAdapter.getTagsPosition() != -1) {
                    scrollToPosition(mAdapter.getTagsPosition() + 1);
                    LinearLayoutManager mLayoutManager =
                            (LinearLayoutManager) mList.getRecyclerView().getLayoutManager();
                    mLayoutManager.scrollToPositionWithOffset(mAdapter.getTagsPosition() + 1, 0);

                }
            }
        });
        mList.getRecyclerView().addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager l = (LinearLayoutManager) recyclerView.getLayoutManager();
                int lastVisibleItemPosition = l.findLastVisibleItemPosition();
                int firstVisibleItemPosition = l.findFirstVisibleItemPosition();
                if (mAdapter != null) {
                    if (dy > 0) {
                        if (mAdapter.getTagsPosition() + 1 == lastVisibleItemPosition) {
                            mGoPinLun.setVisibility(View.GONE);
                        }
                        if (firstVisibleItemPosition == 1) {
//                        mRlCommunity.setVisibility(View.GONE);
                            showIn();
                        }
                    }
                    if (dy < 0) {
                        if (mAdapter.getTagsPosition() + 1 == lastVisibleItemPosition) {
                            mGoPinLun.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });
        mPresenter.requestDoc(mDocId);
    }

    private void showIn() {
        ObjectAnimator phoneAnimator = ObjectAnimator.ofFloat(mRlCommunity, "translationY", 0, mRlCommunity.getHeight()).setDuration(300);
        phoneAnimator.setInterpolator(new OvershootInterpolator());
        AnimatorSet set = new AnimatorSet();
        set.play(phoneAnimator);
        set.start();
//        mRlCommunity.clearAnimation();
        mRlCommunity.setVisibility(View.GONE);
    }

    @Override
    protected void initData() {

    }

    public void scrollToPosition(int position) {
        mList.getRecyclerView().scrollToPosition(position);
    }

    public int getPosition() {
        return ((LinearLayoutManager) mList.getRecyclerView().getLayoutManager()).findFirstVisibleItemPosition();
    }

    private void deleteDoc() {
        if (NetworkUtils.checkNetworkAndShowError(this)) {
            final AlertDialogUtil alertDialogUtil = AlertDialogUtil.getInstance();
            alertDialogUtil.createPromptNormalDialog(this, getString(R.string.label_delete_confirm));
            alertDialogUtil.setButtonText(getString(R.string.label_confirm), getString(R.string.label_cancel), 0);
            alertDialogUtil.setOnClickListener(new AlertDialogUtil.OnClickListener() {
                @Override
                public void CancelOnClick() {
                    alertDialogUtil.dismissDialog();
                }

                @Override
                public void ConfirmOnClick() {
                    mPresenter.deleteDoc(mDocId);
                    alertDialogUtil.dismissDialog();
                }
            });
            alertDialogUtil.showDialog();

        }
    }

    private void favoriteDoc() {
        if (hasLoaded) {
            mPresenter.favoriteDoc(mDocId);
        }
    }

    private void reportDoc() {
        Intent intent = new Intent(this, JuBaoActivity.class);
        intent.putExtra(JuBaoActivity.EXTRA_NAME, mUserName);
        intent.putExtra(JuBaoActivity.EXTRA_CONTENT, mShareDesc);
        intent.putExtra(JuBaoActivity.UUID, mDocId);
        intent.putExtra(JuBaoActivity.EXTRA_TARGET, REPORT.DOC.toString());
        startActivity(intent);
    }

    public void showShareToBuy() {
        isHideShare = true;
        if (bottomMenuFragment != null) bottomMenuFragment.show(getSupportFragmentManager(), "doc");
//        final OnekeyShare oks = new OnekeyShare();
//        //关闭sso授权
//        oks.disableSSOWhenAuthorize();
//        oks.setTitle(mShareTitle);
//        String url = "http://2333.moemoe.la/share/newDoc/" + mDocId;
//        oks.setTitleUrl(url);
//        oks.setText(mShareDesc + " " + url);
//        oks.setImageUrl(ApiService.URL_QINIU + mShareIcon);
//        oks.setUrl(url);
//        oks.setSite(getString(R.string.app_name));
//        oks.setSiteUrl(url);
//        oks.setCallback(new PlatformActionListener() {
//            @Override
//            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//            }
//
//            @Override
//            public void onError(Platform platform, int i, Throwable throwable) {
//
//            }
//
//            @Override
//            public void onCancel(Platform platform, int i) {
//
//            }
//        });
        MoeMoeApplication.getInstance().getNetComponent().getApiService().shareKpi("doc")
                .subscribeOn(Schedulers.io())
                .subscribe(new NetSimpleResultSubscriber() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFail(int code, String msg) {

                    }
                });
//        oks.show(this);
    }

    private void showShare() {
//        final OnekeyShare oks = new OnekeyShare();
//        //关闭sso授权
//        oks.disableSSOWhenAuthorize();
//        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
//        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
//        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
//        oks.setTitle(mShareTitle);
//        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//        String url;
//        if (BuildConfig.DEBUG) {
//            url = ApiService.SHARE_BASE_DEBUG + mDocId;
//        } else {
//            url = ApiService.SHARE_BASE + mDocId;
//        }
//        oks.setTitleUrl(url);
//        // text是分享文本，所有平台都需要这个字段
//        oks.setText(mShareDesc + " " + url);
//        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
//        oks.setImageUrl(ApiService.URL_QINIU + mShareIcon);
//        // url仅在微信（包括好友和朋友圈）中使用
//        oks.setUrl(url);
//        // site是分享此内容的网站名称，仅在QQ空间使用
//        oks.setSite(getString(R.string.app_name));
//        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//        oks.setSiteUrl(url);
//        // 启动分享GUI
//        oks.setCallback(new PlatformActionListener() {
//            @Override
//            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//                MoeMoeApplication.getInstance().getNetComponent().getApiService().shareKpi("{\"doc\":\"" + mDocId + "\"}")//{"doc":"uuid"}
//                        .subscribeOn(Schedulers.io())
//                        .subscribe(new NetSimpleResultSubscriber() {
//                            @Override
//                            public void onSuccess() {
//
//                            }
//
//                            @Override
//                            public void onFail(int code, String msg) {
//
//                            }
//                        });
//                mPresenter.shareDoc();
//            }
//
//            @Override
//            public void onError(Platform platform, int i, Throwable throwable) {
//
//            }
//
//            @Override
//            public void onCancel(Platform platform, int i) {
//
//            }
//        });
//        oks.show(this);
    }

    @Override
    protected void onDestroy() {
        setResult(RESULT_OK);
        if (mPresenter != null) mPresenter.release();
        if (mAdapter != null) mAdapter.releaseAdapter();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick({R.id.iv_comment_send, R.id.fl_comment_root_2, R.id.fl_forward_root_2, R.id.fl_tag_root_2, R.id.rl_tag_root_2, R.id.tv_join})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_comment_send:
                if (mCurType == 2) {
//                    sendLabel();
                } else {
//                    sendComment(false, null);
                }
                break;
            case R.id.fl_comment_root_2:
                if (NetworkUtils.checkNetworkAndShowError(NewDocDetailActivity.this)) {
                    CreateCommentV2Activity.startActivity(this, mDoc.getId(), false, "", 0, mDocId);
                }
                break;
            case R.id.fl_forward_root_2:
                final AlertDialogUtil alertDialogUtil = AlertDialogUtil.getInstance();
                alertDialogUtil.createEditDialog(NewDocDetailActivity.this, PreferenceUtils.getAuthorInfo().getCoin(), 0);
                alertDialogUtil.setOnClickListener(new AlertDialogUtil.OnClickListener() {
                    @Override
                    public void CancelOnClick() {
                        alertDialogUtil.dismissDialog();
                    }

                    @Override
                    public void ConfirmOnClick() {
                        if (DialogUtils.checkLoginAndShowDlg(NewDocDetailActivity.this)) {
                            String content = alertDialogUtil.getEditTextContent();
                            if (!TextUtils.isEmpty(content) && Integer.valueOf(content) > 0) {
                                giveCoin(Integer.valueOf(content));
                                alertDialogUtil.dismissDialog();
                            } else {
                                ToastUtils.showShortToast(NewDocDetailActivity.this, R.string.msg_input_err_coin);
                            }
                        }
                    }
                });
                alertDialogUtil.showDialog();
//                } else {
//                    ((NewDocDetailActivity) mContext).replyNormal();
//                }
//                getCoinContent();
//                ShareArticleEntity entity = new ShareArticleEntity();
//                entity.setDocId(mDoc.getId());
//                entity.setTitle(mDoc.getShare().getTitle());
//                entity.setContent(mDoc.getShare().getDesc());
//                entity.setCover(mDoc.getCover());
//                entity.setCreateTime(mDoc.getCreateTime());
//                UserTopEntity entity2 = new UserTopEntity();
//                if (mDoc.getBadgeList().size() > 0) {
//                    entity2.setBadge(mDoc.getBadgeList().get(0));
//                } else {
//                    entity2.setBadge(null);
//                }
//                entity2.setHeadPath(mDoc.getUserIcon());
//                entity2.setLevel(mDoc.getUserLevel());
//                entity2.setLevelColor(mDoc.getUserLevelColor());
//                entity2.setSex(mDoc.getUserSex());
//                entity2.setUserId(mDoc.getUserId());
//                entity2.setUserName(mDoc.getUserName());
//                entity.setTexts(mDoc.getTexts());
//                entity.setDocCreateUser(entity2);
//                if (NetworkUtils.checkNetworkAndShowError(NewDocDetailActivity.this)) {
//                    CreateForwardV2Activity.startActivity(this, entity);
//                }


                // TODO: 2018/6/27 标签 


                break;
            case R.id.fl_tag_root_2:
                if (NetworkUtils.checkNetworkAndShowError(NewDocDetailActivity.this)) {
                    mPresenter.likeDoc(mDoc.getId(), mDoc.isThumb(), 0);
                }
                break;
            case R.id.rl_tag_root_2:
                mPresenter.likeDoc(mDoc.getId(), mDoc.isThumb(), 0);
                break;
            case R.id.tv_join:
                mPresenter.loadJoin(tagId, true);
                break;

        }
    }

    public void likeDoc(String id, boolean isLike, int position) {
        mPresenter.likeDoc(mDoc.getId(), mDoc.isThumb(), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CreateRichDocActivity.REQUEST_CODE_UPDATE_DOC && resultCode == CreateRichDocActivity.RESPONSE_CODE) {
            autoSendComment();
        } else if (requestCode == 6666) {
            if (resultCode == RESULT_OK && data != null) {
                int position = data.getIntExtra(JuBaoActivity.EXTRA_POSITION, -1);
                if (position != -1 && mAdapter != null) {
                    mAdapter.ownerDelSuccess(position);
                }
            }
        } else if (requestCode == REQ_GET_EDIT_VERSION_IMG) {
            if (resultCode == RESULT_OK && data != null) {
                String photoPath = null;
                Uri u = data.getData();
                if (u != null) {
                    String schema = u.getScheme();
                    if ("file".equals(schema)) {
                        photoPath = u.getPath();
                    } else if ("content".equals(schema)) {
                        photoPath = StorageUtils.getTempFile(System.currentTimeMillis() + ".jpg").getAbsolutePath();
                        InputStream is;
                        FileOutputStream fos;
                        try {
                            is = getContentResolver().openInputStream(u);
                            fos = new FileOutputStream(new File(photoPath));
                            FileUtil.copyFile(is, fos);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (FileUtil.isValidGifFile(photoPath)) {
                            String newFile = StorageUtils.getTempFile(System.currentTimeMillis() + ".gif").getAbsolutePath();
                            FileUtil.copyFile(photoPath, newFile);
                            FileUtil.deleteOneFile(photoPath);
                            photoPath = newFile;
                        }
                    }
                    mIconPaths.add(photoPath);
                }
            }
        } else if (requestCode == REQ_TO_FOLDER && resultCode == RESULT_OK) {
            boolean change = data.getBooleanExtra("change", false);
            int position = data.getIntExtra("position", -1);
            BagDirEntity entity = data.getParcelableExtra("info");
            if (entity != null && change) {
                if (position != -1) {
                    Object o = mAdapter.getItem(position);
                    ((BagDirEntity) o).setBuy(entity.isBuy());
                }
            }
        } else if (requestCode == REQ_DELETE_TAG && resultCode == RESULT_OK) {
            ArrayList<DocTagEntity> entities = data.getParcelableArrayListExtra("tags");
            if (entities != null) {
                mDocTags = entities;
                mAdapter.setTags(entities);
            }
        } else if (requestCode == RESULT_CREATE && resultCode == RESULT_OK) {
            mPresenter.requestDoc(mDocId);
        }
        SoftKeyboardUtils.dismissSoftKeyboard(NewDocDetailActivity.this);
    }

    public void clearEdit() {
        mToUserId = "";
//        mEdtCommentInput.setText("");
        mCurType = 0;
//        mEdtCommentInput.setHint(R.string.a_hint_input_comment);
    }

    private void sendLabel() {
//        if (!hasLoaded) {
//            return;
//        }
//        if (DialogUtils.checkLoginAndShowDlg(this)) {
//            String content = mEdtCommentInput.getText().toString();
//            if (!TextUtils.isEmpty(content)) {
//                SoftKeyboardUtils.dismissSoftKeyboard(this);
//                mAdapter.createLabel(content);
//            } else {
//                showToast(R.string.msg_doc_comment_not_empty);
//            }
//        }
    }
//
//    private void sendComment(final boolean auto, String str) {
//        if (!hasLoaded) {
//            return;
//        }
//        if (!NetworkUtils.checkNetworkAndShowError(this)) {
//            return;
//        }
//        if (DialogUtils.checkLoginAndShowDlg(this)) {
//            String content = mEdtCommentInput.getText().toString();
//            if (auto) content = str;
//            if (TextUtils.isEmpty(content)) {
//                showToast(R.string.msg_doc_comment_not_empty);
//                return;
//            }
//            if (TextUtils.isEmpty(content) && mIconPaths.size() == 0) {
//                showToast(R.string.msg_doc_comment_not_empty);
//                return;
//            }
//            final ArrayList<Image> images = BitmapUtils.handleUploadImage(mIconPaths);
//            ArrayList<String> paths = new ArrayList<>();
//            if (images != null && images.size() > 0) {
//                for (int i = 0; i < images.size(); i++) {
//                    paths.add(images.get(i).getPath());
//                }
//            }
//            SoftKeyboardUtils.dismissSoftKeyboard(this);
//            createDialog();
//            CommentSendEntity bean = new CommentSendEntity(content, mDocId, null, mToUserId);
//            mPresenter.sendComment(paths, bean);
//
//        }
//    }

    public void likeTag(boolean isLike, int position, TagLikeEntity entity) {
        mPresenter.likeTag(isLike, position, entity);
    }

//    public void reply(NewCommentEntity bean) {
//        mToUserId = bean.getFromUserId();
//        mEdtCommentInput.setText("");
//        mEdtCommentInput.setHint("回复 " + bean.getFromUserName() + ": ");
//        mEdtCommentInput.requestFocus();
//        SoftKeyboardUtils.showSoftKeyboard(this, mEdtCommentInput);
//    }

    public void replyNormal() {
//        mEdtCommentInput.setText("");
//        mEdtCommentInput.requestFocus();
//        SoftKeyboardUtils.showSoftKeyboard(this, mEdtCommentInput);
//        CreateCommentV2Activity.startActivity(NewDocDetailActivity.this, mDocId, false, "", 0,mDocId);
        CreateCommentV2Activity.startActivity(NewDocDetailActivity.this, mDocId, false, "", 0, RESULT_CREATE);
    }

    public void addDocLabelView() {
//        mSendRoot.setVisibility(View.GONE);
//        mEdtCommentInput.setText("");
//        mEdtCommentInput.setHint("添加标签吧~~");
//        mEdtCommentInput.requestFocus();
//        SoftKeyboardUtils.showSoftKeyboard(this, mEdtCommentInput);
        mCurType = 2;
        final AlertDialogUtil alertDialogUtil = AlertDialogUtil.getInstance();
        alertDialogUtil.createDocEditDialog(this,"");
        alertDialogUtil.setOnClickListener(new AlertDialogUtil.OnClickListener() {
            @Override
            public void CancelOnClick() {
                alertDialogUtil.dismissDialog();
            }

            @Override
            public void ConfirmOnClick() {
                if (DialogUtils.checkLoginAndShowDlg(NewDocDetailActivity.this)) {
                    String content = alertDialogUtil.getEditTextContent();
                    if (!TextUtils.isEmpty(content)) {
                        mAdapter.createLabel(content);
                        alertDialogUtil.dismissDialog();
                    } else {
                        showToast("标签名不能哟！");
                    }
                }
            }
        });
        alertDialogUtil.showDialog();
    }

    public void autoSendComment() {
        mPresenter.requestDoc(mDocId);
    }

    @Override
    public void onFailure(int code, String msg) {
        finalizeDialog();
        isLoading = false;
        mList.setComplete();
        ErrorCodeUtils.showErrorMsgByCode(NewDocDetailActivity.this, code, msg);
    }

    @Override
    public void onPlusLabel(int position, boolean isLike) {
        finalizeDialog();
        mAdapter.plusSuccess(isLike, position);
    }

    @Override
    public void onDeleteCommentSuccess(int position) {
        showToast("删除评论成功");
        DocDetailNormalEntity entity = (DocDetailNormalEntity) mAdapter.getItem(position);
        mAdapter.getmComments().remove(entity);
        mAdapter.notifyDataSetChanged();
    }

    public void deleteComment(String id, String commentId, final int position) {
        mPresenter.deleteComment(id, commentId, position);
    }

    public void giveCoin(int count) {
        GiveCoinEntity bean = new GiveCoinEntity(count, mDocId);
        mPresenter.giveCoin(bean);
    }

    @Override
    public void onGiveCoin(int coins) {
        showToast(R.string.label_give_coin_success);
        mAdapter.onGiveCoin(coins);
    }

    public void followUser(String userId, boolean follow) {
        mPresenter.followUser(userId, follow);
    }

    @Override
    public void onFollowSuccess(boolean isFollow) {
        mAdapter.followUserSuccess(isFollow);
    }

    @Override
    public void favoriteCommentSuccess(boolean isFavorite, int position) {
        if (isFavorite) {
            showToast("点赞成功");
        } else {
            showToast("取消点赞成功");
        }
        DocDetailNormalEntity entitys = (DocDetailNormalEntity) mAdapter.getItem(position);
        CommentV2Entity commentV2Entity = new Gson().fromJson(entitys.getData()
                , CommentV2Entity.class);
        commentV2Entity.setLike(isFavorite);
        String json = new Gson().toJson(commentV2Entity);
        entitys.setData(json);
        ((DocDetailNormalEntity) mAdapter.getItem(position)).setData(json);
        if (isFavorite) {
            CommentV2Entity entity = new Gson().fromJson(((DocDetailNormalEntity) mAdapter.getItem(position)).getData(),
                    CommentV2Entity.class);
            entity.setLikes(new Gson().fromJson(((DocDetailNormalEntity) mAdapter.getItem(position)).getData(),
                    CommentV2Entity.class).getLikes() + 1);
            String json1 = new Gson().toJson(entity);
            ((DocDetailNormalEntity) mAdapter.getItem(position)).setData(json1);
        } else {
            CommentV2Entity entity = new Gson().fromJson(((DocDetailNormalEntity) mAdapter.getItem(position)).getData(),
                    CommentV2Entity.class);
            entity.setLikes(new Gson().fromJson(((DocDetailNormalEntity) mAdapter.getItem(position)).getData(),
                    CommentV2Entity.class).getLikes()
                    - 1);
            String json1 = new Gson().toJson(entity);
            ((DocDetailNormalEntity) mAdapter.getItem(position)).setData(json1);
        }
        mAdapter.notifyItemChanged(position);
    }

    @Override
    public void onDocTopSuccess(boolean isStatus, String docId) {
        if (isStatus) {
            bottomMenuFragment.changeItemTextById(MENU_TOP, "取消置顶", R.drawable.btn_doc_option_send_minister_post);
            showToast("置顶成功");
        } else {
            bottomMenuFragment.changeItemTextById(MENU_TOP, "置顶", R.drawable.btn_doc_option_send_minister_post);
            showToast("取消置顶成功");
        }
    }

    @Override
    public void onLoadJoinSuccess(boolean isJoin) {
        showToast("加入成功");
//     mRlCommunity.setVisibility(View.GONE);
        showIn();
    }

    @Override
    public void onDocExcellentSuccess(boolean isStatus, String docId) {
        if (isStatus) {
            bottomMenuFragment.changeItemTextById(MENU_BOUTIQUE, "取消精品", R.drawable.btn_doc_option_send_minister_post);
            showToast("设置成功");
        } else {
            bottomMenuFragment.changeItemTextById(MENU_BOUTIQUE, "设为精品", R.drawable.btn_doc_option_send_minister_post);
            showToast("取消成功");
        }
    }

    @Override
    public void onLoadGetCommentsLzSuccess(ArrayList<CommentV2Entity> entities, boolean isPull) {
        finalizeDialog();
        isLoading = false;
        mList.setComplete();

        ArrayList<DocDetailNormalEntity> list = new ArrayList<>();
        for (int i = 0; i < entities.size(); i++) {
            DocDetailNormalEntity entity2 = new DocDetailNormalEntity();
            entity2.setType(7);
            String asJsonObject = gson.toJson(entities.get(i));
            entity2.setData(asJsonObject);
            list.add(entity2);
        }
        if (isPull) {
            mAdapter.setComment(list);
        } else {
            mAdapter.addComment(list);
        }
    }

    @Override
    public void onLikeDocSuccess(boolean isLike, int position) {

        if (isLike) {
//            mTvLikeNum.setText("已赞");
//            mTvLikeNum.setSelected(true);
            showToast("点赞成功");
        } else {
//            mTvLikeNum.setText("赞");
//            mTvLikeNum.setSelected(false);
            showToast("取消点赞");
        }
        mDoc.setThumb(isLike);
        if (isLike) {
            if (mDoc.getThumbUsers() == null || mDoc.getThumbUsers().size() == 0) {
                SimpleUserV2Entity userEntity = new SimpleUserV2Entity();
                userEntity.setUserName(PreferenceUtils.getAuthorInfo().getUserName());
                userEntity.setUserId(PreferenceUtils.getUUid());
                userEntity.setHeadPath(PreferenceUtils.getAuthorInfo().getHeadPath());
                ArrayList<SimpleUserV2Entity> simpleUserEntities = new ArrayList<>();
                simpleUserEntities.add(userEntity);
                mDoc.setThumbUsers(simpleUserEntities);
                mDoc.setThumbs(mDoc.getThumbs() + 1);
            } else {
                SimpleUserV2Entity userEntity = new SimpleUserV2Entity();
                userEntity.setUserName(PreferenceUtils.getAuthorInfo().getUserName());
                userEntity.setUserId(PreferenceUtils.getUUid());
                userEntity.setHeadPath(PreferenceUtils.getAuthorInfo().getHeadPath());
                mDoc.getThumbUsers().add(0, userEntity);
                mDoc.setThumbs(mDoc.getThumbs() + 1);
            }
        } else {
            for (SimpleUserV2Entity userEntity : mDoc.getThumbUsers()) {
                if (userEntity.getUserId().equals(PreferenceUtils.getUUid())) {
                    mDoc.getThumbUsers().remove(userEntity);
                    break;
                }
            }
            mDoc.setThumbs(mDoc.getThumbs() - 1);
        }
        setLikeRoot(mDoc);
    }

    @Override
    public void onLoadDocLikeUsers(ArrayList<UserTopEntity> entities, boolean isPull) {
        finalizeDialog();
        isLoading = false;
        mList.setComplete();
        ArrayList<DocDetailNormalEntity> list = new ArrayList<>();
        for (int i = 0; i < entities.size(); i++) {
            DocDetailNormalEntity entity2 = new DocDetailNormalEntity();
            entity2.setType(14);
            String asJsonObject = gson.toJson(entities.get(i));
            entity2.setData(asJsonObject);
            list.add(entity2);
        }
        if (isPull) {
            mAdapter.setComment(list);
        } else {
            mAdapter.addComment(list);
        }
    }


    public void getCoinContent() {
        mPresenter.getCoinContent(mDocId);
    }

    @Override
    public void onGetCoinContent() {
        autoSendComment();
        showToast(R.string.label_use_coin_success);
    }

    public void createLabel(TagSendEntity entity) {
        createDialog();
        mPresenter.createLabel(entity);
    }

    @Override
    public void onCreateLabel(String s, String name) {
        finalizeDialog();
        clearEdit();
        mAdapter.onCreateLabel(s, name);
    }

    private void initPopupMenus(final DocDetailEntity entity) {
        bottomMenuFragment = new BottomMenuFragment();
        ArrayList<MenuItem> items = new ArrayList<>();
        MenuItem item;
        if (isHideShare) {
            item = new MenuItem(1, "QQ", R.drawable.btn_doc_option_send_qq);
            items.add(item);

            item = new MenuItem(2, "QQ空间", R.drawable.btn_doc_option_send_qzone);
            items.add(item);

            item = new MenuItem(3, "微信", R.drawable.btn_doc_option_send_wechat);
            items.add(item);

            item = new MenuItem(4, "微信朋友圈", R.drawable.btn_doc_option_send_pengyouquan);
            items.add(item);

            item = new MenuItem(5, "微博", R.drawable.btn_doc_option_send_weibo);
            items.add(item);
        } else {
            if (entity.getUserId().equals(PreferenceUtils.getUUid())) {
                item = new MenuItem(TAG_DELETE, getString(R.string.label_tag_ctrl), R.drawable.btn_doc_option_tag);
                items.add(item);

                item = new MenuItem(EDIT_DOC, getString(R.string.label_update_post), R.drawable.btn_doc_option_edit);
                items.add(item);

                item = new MenuItem(MENU_SUBMISSION, "投稿", R.drawable.btn_doc_option_contribute);
                items.add(item);

                item = new MenuItem(MENU_DELETE, getString(R.string.label_delete), R.drawable.btn_doc_option_delete);
                items.add(item);

            } else {
                if (entity.isFavoriteFlag()) {
                    item = new MenuItem(MENU_FAVORITE, getString(R.string.label_cancel_favorite), R.drawable.btn_doc_option_collected);
                } else {
                    item = new MenuItem(MENU_FAVORITE, getString(R.string.label_favorite), R.drawable.btn_doc_option_collect);
                }
                items.add(item);

                item = new MenuItem(MENU_REPORT, getString(R.string.label_jubao), R.drawable.btn_doc_option_report);
                items.add(item);
            }

            item = new MenuItem(MENU_JUMPLZ, "跳转楼层", R.drawable.btn_doc_option_jump);
            items.add(item);

//            item = new MenuItem(MENU_FORWARD, "转发到动态", R.drawable.btn_doc_option_forward);
//            items.add(item);
//
//            item = new MenuItem(MENU_SHARE, getString(R.string.label_share), R.drawable.btn_doc_option_share);
//            items.add(item);

            if (entity.getUserId().equals(PreferenceUtils.getUUid())) {
                if (entity.isManager()) {
                    if (entity.isTop()) {
                        item = new MenuItem(MENU_TOP, "取消置顶", R.drawable.btn_doc_option_send_minister_post, true);
                    } else {
                        item = new MenuItem(MENU_TOP, "置顶", R.drawable.btn_doc_option_send_minister_post, true);
                    }
                    items.add(item);

                    if (entity.isExcellent()) {
                        item = new MenuItem(MENU_BOUTIQUE, "取消精品", R.drawable.btn_doc_option_send_minister_best, true);
                    } else {
                        item = new MenuItem(MENU_BOUTIQUE, "设为精品", R.drawable.btn_doc_option_send_minister_best, true);
                    }
                    items.add(item);

//                item = new MenuItem(MENU_DELETE_V2, "违规删帖", R.drawable.btn_doc_option_send_minister_delete, true);
//                items.add(item);
                }
            } else {
                if (entity.isManager()) {
                    if (entity.isExcellent()) {
                        item = new MenuItem(MENU_BOUTIQUE, "取消精品", R.drawable.btn_doc_option_send_minister_best, true);
                    } else {
                        item = new MenuItem(MENU_BOUTIQUE, "设为精品", R.drawable.btn_doc_option_send_minister_best, true);
                    }
                    items.add(item);

                    if (entity.isTop()) {
                        item = new MenuItem(MENU_TOP, "取消置顶", R.drawable.btn_doc_option_send_minister_post, true);
                    } else {
                        item = new MenuItem(MENU_TOP, "置顶", R.drawable.btn_doc_option_send_minister_post, true);
                    }
                    items.add(item);


                    item = new MenuItem(MENU_DELETE_V2, "违规删帖", R.drawable.btn_doc_option_send_minister_delete, true);
                    items.add(item);
                }
            }
            bottomMenuFragment.setIsShare(true);
        }
        bottomMenuFragment.setShowTop(false);
        bottomMenuFragment.setMenuItems(items);
        bottomMenuFragment.setMenuType(BottomMenuFragment.TYPE_HORIZONTAL);


        bottomMenuFragment.setmClickListener(new BottomMenuFragment.MenuItemClickListener() {
            @Override
            public void OnMenuItemClick(int itemId) {
                if (isHideShare) {
                    String url = "http://2333.moemoe.la/share/newDoc/" + mDocId;
                    switch (itemId) {
                        case 1:
                            ShareUtils.shareQQ(NewDocDetailActivity.this, mShareTitle, url, mShareDesc + " " + url, ApiService.URL_QINIU + mShareIcon, platformActionListener);
                            break;
                        case 2:
                            ShareUtils.shareQQzone(NewDocDetailActivity.this, mShareTitle, url, mShareDesc + " " + url, ApiService.URL_QINIU + mShareIcon, platformActionListener);
                            ;
                            break;
                        case 3:
                            ShareUtils.shareWechat(NewDocDetailActivity.this, mShareTitle, url, mShareDesc + " " + url, ApiService.URL_QINIU + mShareIcon, platformActionListener);
                            break;
                        case 4:
                            ShareUtils.sharepyq(NewDocDetailActivity.this, mShareTitle, url, mShareDesc + " " + url, ApiService.URL_QINIU + mShareIcon, platformActionListener);
                            break;
                        case 5:
                            ShareUtils.shareWeibo(NewDocDetailActivity.this, mShareTitle, url, mShareDesc + " " + url, ApiService.URL_QINIU + mShareIcon, platformActionListener);
                            break;

                    }
                } else {
                    if (itemId == MENU_FAVORITE) {
                        favoriteDoc();
                    } else if (itemId == MENU_REPORT) {
                        reportDoc();
                    } else if (itemId == MENU_SHARE) {
                        if (hasLoaded) {
                            showShare();
                        } else {
                            showToast(R.string.label_doc_not_loaded);
                        }
                    } else if (itemId == MENU_DELETE) {
                        deleteDoc();
                    } else if (itemId == TAG_DELETE) {
                        if (mDocTags != null) {
                            Intent i = new Intent(NewDocDetailActivity.this, TagControlActivity.class);
                            i.putParcelableArrayListExtra("tags", mDocTags);
                            i.putExtra(UUID, mDocId);
                            startActivityForResult(i, REQ_DELETE_TAG);
                        }
                    } else if (itemId == EDIT_DOC) {
                        gotoEditDoc();
                    } else if (itemId == MENU_FORWARD) {
                        ShareArticleEntity entity1 = new ShareArticleEntity();
                        entity1.setDocId(mDoc.getId());
                        entity1.setTitle(mDoc.getShare().getTitle());
                        entity1.setContent(mDoc.getShare().getDesc());
                        entity1.setCover(mDoc.getCover());
                        entity1.setCreateTime(mDoc.getCreateTime());
                        UserTopEntity entity2 = new UserTopEntity();
                        if (mDoc.getBadgeList().size() > 0) {
                            entity2.setBadge(mDoc.getBadgeList().get(0));
                        } else {
                            entity2.setBadge(null);
                        }
                        entity2.setHeadPath(mDoc.getUserIcon());
                        entity2.setLevel(mDoc.getUserLevel());
                        entity2.setLevelColor(mDoc.getUserLevelColor());
                        entity2.setSex(mDoc.getUserSex());
                        entity2.setUserId(mDoc.getUserId());
                        entity2.setUserName(mDoc.getUserName());
                        entity1.setDocCreateUser(entity2);
                        entity1.setTexts(mDoc.getTexts());
                        CreateForwardV2Activity.startActivity(NewDocDetailActivity.this, entity1);
                    } else if (itemId == MENU_SUBMISSION) {
                        Intent i = new Intent(NewDocDetailActivity.this, SubmissionActivity.class);
                        i.putExtra(UUID, mDocId);
                        i.putExtra("doc_name", entity.getTitle());
                        startActivity(i);
                    } else if (itemId == MENU_TOP) {
                        mPresenter.loadDocTop(entity.isTop(), entity.getId());
                    } else if (itemId == MENU_BOUTIQUE) {
                        mPresenter.loadDocExcellent(entity.isExcellent(), entity.getId());
                    } else if (itemId == MENU_JUMPLZ) {
                        if (mAdapter.getSortType() != 1 && mDoc.getComments() > 0) {
                            final AlertDialogUtil dialogUtil = AlertDialogUtil.getInstance();
                            dialogUtil.createEditDialog(NewDocDetailActivity.this, mDoc.getComments(), 1);
                            dialogUtil.setOnClickListener(new AlertDialogUtil.OnClickListener() {
                                @Override
                                public void CancelOnClick() {
                                    dialogUtil.dismissDialog();
                                }

                                @Override
                                public void ConfirmOnClick() {
                                    content = dialogUtil.getEditTextContent();
                                    try {
                                        if (!TextUtils.isEmpty(content) && Integer.valueOf(content) > 0) {
                                            if (mDoc.getComments() != 0 && Integer.valueOf(content) > mDoc.getComments()) {
                                                showToast("超过当前楼层数");
                                            } else {
                                                if (mAdapter.getSortType() == 0) {
                                                    mPresenter.requestTopComment(mDocId, mAdapter.getCommentType(), mAdapter.getSortType(), Integer.valueOf(content) - 1, 0);
                                                } else if (mAdapter.getSortType() == 2) {
                                                    mPresenter.requestTopComment(mDocId, mAdapter.getCommentType(), mAdapter.getSortType(), mDoc.getComments() - Integer.valueOf(content), 0);
                                                }
                                                dialogUtil.dismissDialog();
                                            }
                                        } else {
                                            showToast("楼层数必须大于0");
                                        }
                                    } catch (Exception e) {
                                        showToast("楼层数必须大于0");
                                    }
                                }
                            });
                            dialogUtil.showDialog();
                        } else {
                            showToast("当前没有楼层");
                        }
                    } else if (itemId == MENU_DELETE_V2) {
                        Intent intent = new Intent(NewDocDetailActivity.this, DeleteActivity.class);
                        intent.putExtra(JuBaoActivity.EXTRA_NAME, mUserName);
                        intent.putExtra(JuBaoActivity.EXTRA_CONTENT, mShareDesc);
                        intent.putExtra(JuBaoActivity.UUID, mDocId);
                        intent.putExtra(JuBaoActivity.EXTRA_TARGET, REPORT.DOC.toString());
                        startActivity(intent);
                    }
                }
            }
        });

        bottomMenuFragment.setShareOnClickListener(new BottomMenuFragment.ShareItemClickListener() {
            @Override
            public void OnShareItemClick(String shareName) {
                // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
                // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
                // text是分享文本，所有平台都需要这个字段
                // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
                if (hasLoaded) {
//                    showShare();
                    String url;
                    if (BuildConfig.DEBUG) {
                        url = ApiService.SHARE_BASE_DEBUG + mDocId;
                    } else {
                        url = ApiService.SHARE_BASE + mDocId;
                    }
                    switch (shareName) {
                        case "QQ":
                            ShareUtils.shareQQ(NewDocDetailActivity.this, mShareTitle, url, mShareDesc + " " + url, ApiService.URL_QINIU + mShareIcon, platformActionListener);
                            break;
                        case "QQZone":
                            ShareUtils.shareQQzone(NewDocDetailActivity.this, mShareTitle, url, mShareDesc + " " + url, ApiService.URL_QINIU + mShareIcon, platformActionListener);
                            break;
                        case "WeChat":
                            ShareUtils.shareWechat(NewDocDetailActivity.this, mShareTitle, url, mShareDesc + " " + url, ApiService.URL_QINIU + mShareIcon, platformActionListener);
                            break;
                        case "WeChatPyq":
                            ShareUtils.sharepyq(NewDocDetailActivity.this, mShareTitle, url, mShareDesc + " " + url, ApiService.URL_QINIU + mShareIcon, platformActionListener);
                            break;
                        case "WeiBo":
                            ShareUtils.shareWeibo(NewDocDetailActivity.this, mShareTitle, url, mShareDesc + " " + url, ApiService.URL_QINIU + mShareIcon, platformActionListener);
                            break;
                    }
                } else {
                    showToast(R.string.label_doc_not_loaded);
                }
            }

        });

    }

    /**
     * 分享回调
     */
    PlatformActionListener platformActionListener = new PlatformActionListener() {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            Log.e("kid", "分享成功");
            if (isHideShare) {

            } else {
                MoeMoeApplication.getInstance().getNetComponent().getApiService().shareKpi("{\"doc\":\"" + mDocId + "\"}")//{"doc":"uuid"}
                        .subscribeOn(Schedulers.io())
                        .subscribe(new NetSimpleResultSubscriber() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onFail(int code, String msg) {

                            }
                        });
                mPresenter.shareDoc();
            }
            if (bottomMenuFragment != null) {
                bottomMenuFragment.dismiss();
            }
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            Log.e("kid", "分享失败");
            if (bottomMenuFragment != null) {
                bottomMenuFragment.dismiss();
            }
        }

        @Override
        public void onCancel(Platform platform, int i) {
            Log.e("kid", "分享取消");
            if (bottomMenuFragment != null) {
                bottomMenuFragment.dismiss();
            }
        }
    };

    private void gotoEditDoc() {
        Intent i = new Intent(NewDocDetailActivity.this, CreateRichDocActivity.class);
        i.putExtra("doc", createRichDocFromDoc());
        i.putExtra(UUID, mDocId);
        i.putExtra("tagId", mDoc.getTagId());
        i.putExtra("from_name", mDoc.getTagName());
        startActivityForResult(i, CreateRichDocActivity.REQUEST_CODE_UPDATE_DOC);
    }

    private RichDocListEntity createRichDocFromDoc() {
        RichDocListEntity entity = new RichDocListEntity();
        entity.setDocId(mDocId);
        entity.setTitle(mDoc.getTitle());
        entity.setFolderId(mDoc.getFolderInfo() == null ? "" : mDoc.getFolderInfo().getFolderId());
        entity.setTags(mDoc.getTags());
        ArrayList<String> list = new ArrayList<>();
        for (UserFollowTagEntity entity1 : mDoc.getTexts()) {
            list.add(entity1.getText());
        }
        entity.setTexts(list);
        entity.setBgCover(mDoc.getCover());
        entity.setHidType(mDoc.isCoinComment());
        for (DocDetailEntity.Detail detail : mDoc.getDetails()) {
            RichEntity richEntity = new RichEntity();
            if (detail.getType().equals("DOC_TEXT")) {
                richEntity.setInputStr((String) detail.getTrueData());
            } else if (detail.getType().equals("DOC_IMAGE")) {
                Image image = (Image) detail.getTrueData();
                richEntity.setImage(image);
            } else if (detail.getType().equals("DOC_MUSIC")) {
                DocDetailEntity.DocMusic music = (DocDetailEntity.DocMusic) detail.getTrueData();
                entity.setMusicPath(music.getUrl());
                entity.setMusicTitle(music.getName());
                entity.setTime(music.getTimestamp());
                entity.setCover(music.getCover());
            }
            entity.getList().add(richEntity);
        }
        if (mDoc.getCoinDetails() != null) {
            if (mDoc.getDetails() != null) {
                for (DocDetailEntity.Detail detail : mDoc.getCoinDetails()) {
                    RichEntity richEntity = new RichEntity();
                    if (detail.getType().equals("DOC_TEXT")) {
                        richEntity.setInputStr((String) detail.getTrueData());
                    } else if (detail.getType().equals("DOC_IMAGE")) {
                        Image image = (Image) detail.getTrueData();
                        richEntity.setImage(image);
                    }
                    entity.getHideList().add(richEntity);
                }
            }
        }
        return entity;
    }

    private DocDetailEntity mDoc;

    @Override
    public void onDocLoaded(final DocDetailEntity entity) {
        mDoc = entity;
        mList.setComplete();
        isReplyShow = entity.isCoinComment() && (entity.getCoinDetails() == null || (entity.getCoinDetails() != null && entity.getCoinDetails().size() <= 0));
        mCommentNum = entity.getComments();
        hasLoaded = true;
        mUserName = entity.getUserName();
        mShareDesc = entity.getShare().getDesc();
        mShareTitle = entity.getShare().getTitle();
        mShareIcon = entity.getShare().getIcon();
        mDocTags = entity.getTags();
        tagId = entity.getTagId();

        if (entity.getCoinPays() > 0) {
            mTvForwardNum.setText(StringUtils.getNumberInLengthLimit(entity.getCoinPays(), 4));
        } else {
            mTvForwardNum.setText("打赏");
        }
        if (entity.getComments() > 0) {
            mTvCommentNum.setText(StringUtils.getNumberInLengthLimit(entity.getComments(), 4));
        } else {
            mTvCommentNum.setText("评论");
        }
        setLikeRoot(entity);
        if (bottomMenuFragment == null) initPopupMenus(entity);
        mImages.clear();
        Gson gson = new Gson();
        if (entity.getCoinDetails() != null) {
            for (DocDetailEntity.Detail detail : entity.getCoinDetails()) {
                if (detail.getType().equals("DOC_TEXT")) {
                    detail.setTrueData(detail.getData().get("text").getAsString());
                } else if (detail.getType().equals("DOC_IMAGE")) {
                    Image image = gson.fromJson(detail.getData(), Image.class);
                    detail.setTrueData(image);
                    mImages.add(image);
                } else if (detail.getType().equals("DOC_MUSIC")) {
                    detail.setTrueData(gson.fromJson(detail.getData(), DocDetailEntity.DocMusic.class));
                } else if (detail.getType().equals("DOC_LINK")) {
                    detail.setTrueData(gson.fromJson(detail.getData(), DocDetailEntity.DocLink.class));
                } else if (detail.getType().equals("DOC_GROUP_LINK")) {
                    detail.setTrueData(gson.fromJson(detail.getData(), DocDetailEntity.DocGroupLink.class));
                }
            }
        }
        if (entity.getDetails() != null) {
            for (DocDetailEntity.Detail detail : entity.getDetails()) {
                if (detail.getType().equals("DOC_TEXT")) {
                    detail.setTrueData(detail.getData().get("text").getAsString());
                } else if (detail.getType().equals("DOC_IMAGE")) {
                    Image image = gson.fromJson(detail.getData(), Image.class);
                    detail.setTrueData(image);
                    mImages.add(image);
                } else if (detail.getType().equals("DOC_MUSIC")) {
                    detail.setTrueData(gson.fromJson(detail.getData(), DocDetailEntity.DocMusic.class));
                } else if (detail.getType().equals("DOC_LINK")) {
                    detail.setTrueData(gson.fromJson(detail.getData(), DocDetailEntity.DocLink.class));
                } else if (detail.getType().equals("DOC_GROUP_LINK")) {
                    detail.setTrueData(gson.fromJson(detail.getData(), DocDetailEntity.DocGroupLink.class));
                }
            }
        }
        mAdapter.setData(entity);
        isLoading = true;
        mPresenter.requestTopComment(mDocId, mAdapter.getCommentType(), mAdapter.getSortType(), 0, 0);
        // mPresenter.requestTopComment(mDoc.getId());
        if (isFrist) {
            isFrist = false;
            if (!entity.isJoinTag()) {
                if (TextUtils.isEmpty(entity.getTagName())) {
                    return;
                }
                mRlCommunity.setVisibility(View.VISIBLE);
                mTvCommunityName.setText("想要加入 [" + entity.getTagName() + "] 社团吗？");
                handler.postDelayed(runnable, 5000);//每两秒执行一次runnable
//                AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.2f);
//                alphaAnimation.setDuration(5000);
//                alphaAnimation.setFillAfter(true);
//                //开始动画
//                mRlCommunity.startAnimation(alphaAnimation);
            } else {
//                mRlCommunity.setVisibility(View.GONE);
                showIn();
            }
            if (isPinLun) {
                CreateCommentV2Activity.startActivity(this, mDoc.getId(), false, "", 0, mDocId);
            }
        }
    }

    private void setLikeRoot(DocDetailEntity entity) {
        if (entity.getThumbs() == 0) {
            mFlTagRoot2.setVisibility(View.VISIBLE);
            mRlTagRoot2.setVisibility(View.GONE);
        } else {
            mFlTagRoot2.setVisibility(View.GONE);
            mRlTagRoot2.setVisibility(View.VISIBLE);
            mIvLikeItem.setSelected(entity.isThumb());
            mTvLikeNum.setText(StringUtils.getNumberInLengthLimit(entity.getThumbs(), 4));
            int trueSize = (int) getResources().getDimension(R.dimen.y48);
            int imgSize = (int) getResources().getDimension(R.dimen.y44);
            int startMargin = (int) -getResources().getDimension(R.dimen.y10);
            int showSize = 4;
            if (entity.getThumbUsers() != null) {
                if (entity.getThumbUsers().size() < showSize) {
                    showSize = entity.getThumbUsers().size();
                }
                mLlLikeUserRoot.removeAllViews();
                if (showSize == 4) {
                    ImageView iv = new ImageView(this);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(trueSize, trueSize);
                    lp.leftMargin = startMargin;
                    iv.setLayoutParams(lp);
                    iv.setImageResource(R.drawable.btn_feed_like_more);
                    mLlLikeUserRoot.addView(iv);
                }
                for (int i = showSize - 1; i >= 0; i--) {
                    final SimpleUserV2Entity userEntity = entity.getThumbUsers().get(i);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(trueSize, trueSize);
                    if (i != 0) {
                        lp.leftMargin = startMargin;
                    }
                    View likeUser = LayoutInflater.from(this).inflate(R.layout.item_white_border_img, null);
                    likeUser.setLayoutParams(lp);
                    ImageView img = likeUser.findViewById(R.id.iv_img);
                    Glide.with(this)
                            .load(StringUtils.getUrl(this, userEntity.getHeadPath(), imgSize, imgSize, false, true))
                            .error(R.drawable.bg_default_circle)
                            .placeholder(R.drawable.bg_default_circle)
                            .bitmapTransform(new CropCircleTransformation(this))
                            .into(img);
                    img.setOnClickListener(new NoDoubleClickListener() {
                        @Override
                        public void onNoDoubleClick(View v) {
//                        ViewUtils.toPersonal(context, userEntity.getUserId());
                        }
                    });
                    mLlLikeUserRoot.addView(likeUser);
                }
            }
        }
    }

    public void requestComment() {
        lzType = "评论";
        mPresenter.requestTopComment(mDocId, mAdapter.getCommentType(), mAdapter.getSortType(), 0, 0);
    }

    public void loadLikeRequst() {
        mPresenter.loadDocLikeUsers(mDocId, 0);
    }

    public void loadGetCommentsLz(String type) {
        lzType = type;
        mPresenter.loadGetCommentsLz(mDocId, 0);
    }

    public void favoriteComment(String id, boolean isFavorite, int position) {
        mPresenter.favoriteComment(mDoc.getId(), id, isFavorite, position);
    }

    public void goComment(String commentId, boolean isSelc, int commenType, String replyName) {
        CreateCommentV2Activity.startActivity(this, commentId, isSelc, "", commenType, replyName, mDocId);

    }

    @Override
    public void onLoadTopCommentSuccess(ArrayList<CommentV2Entity> commentV2Entities, boolean isPull) {
        finalizeDialog();
        isLoading = false;
        mList.setComplete();

        ArrayList<DocDetailNormalEntity> list = new ArrayList<>();
        for (int i = 0; i < commentV2Entities.size(); i++) {
            DocDetailNormalEntity entity2 = new DocDetailNormalEntity();
            entity2.setType(7);
            String asJsonObject = gson.toJson(commentV2Entities.get(i));
            entity2.setData(asJsonObject);
            list.add(entity2);
        }
        if (isPull) {
            mAdapter.setComment(list);
        } else {
            mAdapter.addComment(list);
        }

        mTvComment.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        mTvComment.setPadding((int) getResources().getDimension(R.dimen.x24), 0, 0, 0);
        mTvComment.setTextColor(ContextCompat.getColor(this, R.color.gray_d7d7d7));
        mTvComment.setText("输入评论...");
        mTvComment.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                CreateCommentV2Activity.startActivity(NewDocDetailActivity.this, mDocId, false, "", 0, RESULT_CREATE);
            }
        });

        if (((LinearLayoutManager) mList.getRecyclerView().getLayoutManager()).findLastVisibleItemPosition() > mAdapter.getTagsPosition()) {
            mGoPinLun.setVisibility(View.GONE);
        }


    }

    @Override
    public void onDeleteDoc() {
        showToast("删除成功");
        onBackPressed("delete");
    }

    @Override
    public void onFavoriteDoc(boolean favorite) {
        if (favorite) {
            bottomMenuFragment.changeItemTextById(MENU_FAVORITE, getString(R.string.label_cancel_favorite), R.drawable.btn_doc_option_collected);
            showToast(R.string.label_favorite_success);
        } else {
            bottomMenuFragment.changeItemTextById(MENU_FAVORITE, getString(R.string.label_favorite), R.drawable.btn_doc_option_collect);
            showToast(R.string.label_cancel_favorite_success);
        }
    }

    private boolean isReplyShow = false;

    @Override
    public void onSendComment() {
        finalizeDialog();
        clearEdit();
        mIconPaths.clear();
        showToast(R.string.msg_send_comment_success);
        if (isReplyShow) {
            autoSendComment();
        }
    }
}
