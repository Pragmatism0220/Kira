package com.moemoe.lalala.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.di.components.DaggerCreateRichDocComponent;
import com.moemoe.lalala.di.modules.CreateRichDocModule;
import com.moemoe.lalala.event.RichImgRemoveEvent;
import com.moemoe.lalala.model.entity.DocPut;
import com.moemoe.lalala.model.entity.FolderType;
import com.moemoe.lalala.model.entity.Image;
import com.moemoe.lalala.model.entity.NewDocType;
import com.moemoe.lalala.model.entity.RichDocListEntity;
import com.moemoe.lalala.model.entity.RichEntity;
import com.moemoe.lalala.model.entity.ShareArticleEntity;
import com.moemoe.lalala.model.entity.UserFollowTagEntity;
import com.moemoe.lalala.model.entity.UserTopEntity;
import com.moemoe.lalala.presenter.CreateRichDocContract;
import com.moemoe.lalala.presenter.CreateRichDocPresenter;
import com.moemoe.lalala.utils.AlertDialogUtil;
import com.moemoe.lalala.utils.AndroidBug5497Workaround;
import com.moemoe.lalala.utils.DensityUtil;
import com.moemoe.lalala.utils.DialogUtils;
import com.moemoe.lalala.utils.ErrorCodeUtils;
import com.moemoe.lalala.utils.MusicLoader;
import com.moemoe.lalala.utils.NetworkUtils;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.utils.tag.TagControl;
import com.moemoe.lalala.view.widget.netamenu.BottomMenuFragment;
import com.moemoe.lalala.view.widget.netamenu.MenuItem;
import com.moemoe.lalala.view.widget.richtext.NetaRichEditor;
import com.moemoe.lalala.view.widget.view.KeyboardListenerLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.moemoe.lalala.utils.StartActivityConstant.REQ_RECOMMEND_TAG;
import static com.moemoe.lalala.utils.StartActivityConstant.REQ_WEN_ZHANG;

/**
 * 富文本帖子创建
 * Created by yi on 2017/5/15.
 */

public class CreateRichDocActivity extends BaseAppCompatActivity implements CreateRichDocContract.View {

    /**
     * 9张图片上限
     */
    private final int ICON_NUM_LIMIT = 36;
    public static final String TYPE_TAG_NAME_DEFAULT = "tag_default";
    public static final String TYPE_QIU_MING_SHAN = "qiu_ming_shan";
    private final int REQ_GET_FROM_SELECT_MUSIC = 1003;
    private final int REQ_ADD_HIDE = 1004;
    private final int REQ_ADD_SEARCH = 1005;
    private static final int REQ_SELECT_FOLDER = 5001;
    public static final int RESPONSE_CODE = 10000;
    public static final int REQUEST_CODE_CREATE_DOC = 11000;
    public static final int REQUEST_CODE_UPDATE_DOC = 12000;
    public static final int REQUEST_CODE_SELECT = 13000;

    @BindView(R.id.tv_left_menu)
    TextView mTvMenuLeft;
    @BindView(R.id.tv_toolbar_title)
    TextView mTvTitle;
    @BindView(R.id.tv_menu)
    TextView mTvMenuRight;
    @BindView(R.id.tv_right_menu)
    TextView mTvSave;
    @BindView(R.id.rich_et)
    NetaRichEditor mRichEt;
    @BindView(R.id.rl_ope_root)
    KeyboardListenerLayout mKlCommentBoard;
    @BindView(R.id.iv_add_bag)
    ImageView mIvAddBag;
    @BindView(R.id.iv_add_hide_doc)
    ImageView mIvAddHide;
    @BindView(R.id.iv_add_music)
    ImageView mIvAddMusic;
    @BindView(R.id.view_add_sep)
    View mViewAddSep;
    @BindView(R.id.rl_release_position)
    RelativeLayout mRlRelease;
    @BindView(R.id.tv_release_text)
    TextView mTvRelease;
    @BindView(R.id.rl_wenzhang)
    RelativeLayout RlWenZhangList;
    @BindView(R.id.tv_select)
    TextView mTvSelect;

    @Inject
    CreateRichDocPresenter mPresenter;

    private String mFromSchema;
    private String mFromName;
    private int mImageSize;
    private ArrayList<RichEntity> mHideList;
    private String mFolderId = "";
    private boolean mHideType;
    private MusicLoader.MusicInfo mMusicInfo;
    private Image mMusicCover;
    private int mDocType;
    private RichDocListEntity mDoc;
    private ArrayList<String> mUserIds;
    private BottomMenuFragment bottomFragment;
    private String mFolderType;
    private String mBgCover = "";
    private int coverSize = -1;
    private boolean mIsCover = true;
    private String mDepartmentId;
    private boolean mIsUpdateDoc;
    private String mTagId;
    private Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String saveDoc = getSaveDoc();
            if (saveDoc != null) {
                PreferenceUtils.saveDoc(CreateRichDocActivity.this, saveDoc);
            }
        }
    };
    private boolean sendDoc = false;
    private boolean select;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_create_rich_doc;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        DaggerCreateRichDocComponent.builder()
                .createRichDocModule(new CreateRichDocModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        ViewUtils.setStatusBarLight(getWindow(), $(R.id.top_view));
        AndroidBug5497Workaround.assistActivity(this);
        Intent i = getIntent();
        if (i == null) {
            finish();
            return;
        }
        mFromSchema = i.getStringExtra("from_schema");
        mFromName = i.getStringExtra("from_name");
        mDepartmentId = i.getStringExtra("departmentId");
        mTagId = i.getStringExtra("tagId");
        boolean from_type = i.getBooleanExtra("from_type", false);
        mDocType = i.getIntExtra(TYPE_QIU_MING_SHAN, 0);
        mDoc = i.getParcelableExtra("doc");
        select = i.getBooleanExtra("select", false);
        mIvAddBag.setVisibility(View.VISIBLE);
        mIvAddHide.setVisibility(View.VISIBLE);
        mIvAddMusic.setVisibility(View.VISIBLE);
        mViewAddSep.setVisibility(View.VISIBLE);
        mHideList = new ArrayList<>();
        mUserIds = new ArrayList<>();
        mTvMenuLeft.setVisibility(View.VISIBLE);
        ViewUtils.setLeftMargins(mTvMenuLeft, (int) getResources().getDimension(R.dimen.x36));
        mTvMenuLeft.setTextColor(ContextCompat.getColor(this, R.color.black_1e1e1e));
        mTvMenuLeft.setText("返回");
        mTvTitle.setVisibility(View.VISIBLE);
        mImageSize = 0;
        if (mDoc != null) {
            mTvTitle.setText(getString(R.string.label_update_post));
            mIsUpdateDoc = true;
            mRichEt.setLabelAble();
            if (mDoc != null) {
                mRichEt.setTags(mDoc.getTexts());
            }
        } else {
            mTvTitle.setText(getString(R.string.label_create_post));
            mRichEt.setLabelAble();
            String res = PreferenceUtils.getDoc(this);
            if (!TextUtils.isEmpty(res)) {
                mDoc = getLocal();
                if (mDoc != null) {
                    mRichEt.setTags(mDoc.getTexts());
                }
            }
            mRichEt.setmKlCommentBoard(mKlCommentBoard);
        }
        mRichEt.setTop();
        mTvMenuRight.setVisibility(View.VISIBLE);
        ViewUtils.setRightMargins(mTvMenuRight, (int) getResources().getDimension(R.dimen.x36));
        mTvMenuRight.setText(getString(R.string.label_menu_publish_doc));
        mTvMenuRight.setTextColor(Color.WHITE);
        mTvMenuRight.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        mTvMenuRight.setWidth((int) getResources().getDimension(R.dimen.x88));
        mTvMenuRight.setHeight((int) getResources().getDimension(R.dimen.y48));
        mTvMenuRight.setBackgroundResource(R.drawable.shape_main_background_2);
        if (!mIsUpdateDoc) {
            mTvSave.setVisibility(View.VISIBLE);
            mTvSave.setText("清空");
            mTvSave.setTextColor(Color.WHITE);
            mTvSave.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
            mTvSave.setWidth((int) getResources().getDimension(R.dimen.x88));
            mTvSave.setHeight((int) getResources().getDimension(R.dimen.y48));
            mTvSave.setBackgroundResource(R.drawable.shape_rect_border_green_background_y4);
            mTvSave.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {

                    final AlertDialogUtil alertDialogUtil = AlertDialogUtil.getInstance();
                    alertDialogUtil.createPromptNormalDialog(CreateRichDocActivity.this, "确认清空么？");
                    alertDialogUtil.setOnClickListener(new AlertDialogUtil.OnClickListener() {
                        @Override
                        public void CancelOnClick() {
                            alertDialogUtil.dismissDialog();
                        }

                        @Override
                        public void ConfirmOnClick() {

                            PreferenceUtils.saveDoc(CreateRichDocActivity.this, "");
                            mRichEt.setHintTile();
                            mRichEt.setLastFocusEdit();
                            ArrayList<String> texts = new ArrayList<>();
                            mRichEt.setTags(texts);
                            mBgCover = null;
                            mRichEt.setCover("");
                            mHideList.clear();
                            mHideType = false;
                            mIvAddHide.setSelected(false);
                            mIvAddBag.setSelected(false);
                            mFolderId = "";
                            mMusicInfo = null;
                            mMusicCover = null;
                            mIvAddMusic.setSelected(false);
                            if (mImageSize > 0) {
                                mImageSize = 0;
                                mRichEt.removeImageVieweList();
                                mRichEt.createFirstEdit();
                            }
                            alertDialogUtil.dismissDialog();
                        }
                    });
                    alertDialogUtil.showDialog();

                }
            });
        }
        if (TextUtils.isEmpty(mDepartmentId)) {
            mRlRelease.setVisibility(View.VISIBLE);
        }
        initPopupMenus();
        mTvRelease.setText(mFromName);
        handler.postDelayed(runnable, 5000);//每两秒执行一次runnable
        if (select) {
            RlWenZhangList.setVisibility(View.VISIBLE);
        } else {
            RlWenZhangList.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (!sendDoc) {
            onBackPressed();
        }
        if (mPresenter != null) mPresenter.release();
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
        stayEvent("发帖");
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseTime();
//        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTime();
//        EventBus.getDefault().register(this);
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {

    }

    @Override
    protected void initListeners() {
        mTvMenuLeft.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                onBackPressed();
            }
        });
    }

    private RichDocListEntity getLocal() {
        return RichDocListEntity.toEntity(PreferenceUtils.getDoc(this));
    }

    private void saveToLocal() {
        String res = getSaveDoc();
        if (TextUtils.isEmpty(mBgCover)
                && TextUtils.isEmpty(mRichEt.getTitle())
                && mHideList.size() == 0
                && TextUtils.isEmpty(mFolderId)
                && mRichEt.getTags().size() == 0) {
            PreferenceUtils.saveDoc(CreateRichDocActivity.this, "");
            return;
        } else {
            if (res != null) {
                PreferenceUtils.saveDoc(this, res);
                showToast("保存草稿成功");
            }
        }
    }

    private String getSaveDoc() {
        RichDocListEntity doc = new RichDocListEntity();

        doc.setBgCover(mBgCover);
        doc.setHidType(mHideType);
        doc.setTitle(mRichEt.getTitle());

        ArrayList<RichEntity> list = new ArrayList<>();
        for (RichEntity entity : mRichEt.buildEditData()) {
            RichEntity entity1 = new RichEntity();
            if (!TextUtils.isEmpty(entity.getInputStr())) {
                entity1.setInputStr(StringUtils.buildDataAtUser(entity.getInputStr()));
            } else if (entity.getImage() != null && !TextUtils.isEmpty(entity.getImage().getPath())) {
                Image image = new Image();
                image.setPath(entity.getImage().getPath());
                image.setH(entity.getImage().getH());
                image.setW(entity.getImage().getW());
                image.setSize(entity.getImage().getSize());
                entity1.setImage(image);
            }
            list.add(entity1);
        }
        doc.setList(list);
        ArrayList<RichEntity> hideList = new ArrayList<>();
        for (RichEntity entity : mHideList) {
            RichEntity entity1 = new RichEntity();
            if (!TextUtils.isEmpty(entity.getInputStr())) {
                entity1.setInputStr(StringUtils.buildDataAtUser(entity.getInputStr()));
            } else if (entity.getImage() != null && !TextUtils.isEmpty(entity.getImage().getPath())) {
                Image image = new Image();
                image.setPath(entity.getImage().getPath());
                image.setH(entity.getImage().getH());
                image.setW(entity.getImage().getW());
                image.setSize(entity.getImage().getSize());
                entity1.setImage(image);
            }
            hideList.add(entity1);
        }
        doc.setHideList(hideList);
        if (mMusicInfo != null) {
            doc.setMusicPath(mMusicInfo.getUrl());
            doc.setMusicTitle(mMusicInfo.getTitle());
            doc.setTime(mMusicInfo.getDuration());
            doc.setCover(mMusicCover);
        } else {
            doc.setMusicPath("");
            doc.setMusicTitle("");
        }
        doc.setFolderId(mFolderId);
        //doc.setTags(mRichEt.getmTags());
        doc.setTexts(mRichEt.getTags());

        return RichDocListEntity.toJsonString(doc);
    }

    @Override
    protected void initData() {
        if (mDoc != null) {
            if (!TextUtils.isEmpty(mDoc.getBgCover())) {
                int w = (int) (DensityUtil.getScreenWidth(this) - getResources().getDimension(R.dimen.x36) * 2);
                int h = (int) getResources().getDimension(R.dimen.y200);
                mBgCover = mDoc.getBgCover();
                String path = mBgCover;
                if (mBgCover.startsWith("image")) {
                    path = StringUtils.getUrl(this, mBgCover, w, h, false, true);
                }
                mRichEt.setCover(path);
                coverSize = -1;
            }
            mHideType = mDoc.isHidType();
            if (!TextUtils.isEmpty(mDoc.getTitle())) {
                mRichEt.setTitle(mDoc.getTitle());
            }
            if (mDoc.getList().size() > 0) {
                RichEntity entity = mDoc.getList().get(0);
                if (TextUtils.isEmpty(entity.getInputStr())) {
                    mRichEt.addEditTextAtIndex(mRichEt.getLastIndex(), "");
                }
                Observable.fromIterable(mDoc.getList())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<RichEntity>() {

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {
                                RichEntity entity1 = mDoc.getList().get(mDoc.getList().size() - 1);
                                if (TextUtils.isEmpty(entity1.getInputStr())) {
                                    mRichEt.addEditTextAtIndex(mRichEt.getLastIndex(), "");
                                }
                            }

                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            @Override
                            public void onNext(RichEntity richEntity) {
                                if (!TextUtils.isEmpty(richEntity.getInputStr())) {
                                    mRichEt.addEditTextAtIndex(mRichEt.getLastIndex(), StringUtils.buildAtUserToEdit(CreateRichDocActivity.this, richEntity.getInputStr().toString()));
                                } else if (richEntity.getImage() != null && !TextUtils.isEmpty(richEntity.getImage().getPath())) {
                                    mRichEt.addImageViewAtIndex(mRichEt.getLastIndex(), richEntity.getImage().getPath(), richEntity.getImage().getW(), richEntity.getImage().getH(), richEntity.getImage().getSize());
                                    mImageSize++;
                                }
                            }
                        });
            } else {
                mRichEt.createFirstEdit();
            }
            if (mDoc.getHideList().size() > 0) {
                mHideList = mDoc.getHideList();
                mIvAddHide.setSelected(true);
            }
            if (!TextUtils.isEmpty(mDoc.getMusicPath())) {
                mMusicInfo = new MusicLoader.MusicInfo();
                mMusicInfo.setUrl(mDoc.getMusicPath());
                mMusicInfo.setTitle(mDoc.getMusicTitle());
                mMusicInfo.setDuration(mDoc.getTime());
                mMusicCover = mDoc.getCover();
                mIvAddMusic.setSelected(true);
            }
            if (!TextUtils.isEmpty(mDoc.getFolderId())) {
                mFolderId = mDoc.getFolderId();
                mIvAddBag.setSelected(true);
            }
        } else {
            mRichEt.createFirstEdit();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void richImageRemove(RichImgRemoveEvent event) {
        mImageSize--;
    }

    private void initPopupMenus() {
        bottomFragment = new BottomMenuFragment();
        ArrayList<MenuItem> items = new ArrayList<>();

        MenuItem item = new MenuItem(0, "综合");
        items.add(item);

        item = new MenuItem(1, "图集");
        items.add(item);

        item = new MenuItem(2, "漫画");
        items.add(item);

        item = new MenuItem(3, "小说");
        items.add(item);

        item = new MenuItem(4, "视频");
        items.add(item);

        item = new MenuItem(5, "音乐");
        items.add(item);


        bottomFragment.setShowTop(false);
        bottomFragment.setMenuItems(items);
        bottomFragment.setMenuType(BottomMenuFragment.TYPE_VERTICAL);
        bottomFragment.setmClickListener(new BottomMenuFragment.MenuItemClickListener() {
            @Override
            public void OnMenuItemClick(int itemId) {
                if (itemId == 0) {
                    mFolderType = FolderType.ZH.toString();
                } else if (itemId == 1) {
                    mFolderType = FolderType.TJ.toString();
                } else if (itemId == 2) {
                    mFolderType = FolderType.MH.toString();
                } else if (itemId == 3) {
                    mFolderType = FolderType.XS.toString();
                } else if (itemId == 4) {
//                    mFolderType = FolderType.MOVIE.toString();
                    mFolderType = FolderType.SP.toString();
                } else if (itemId == 5) {
//                    mFolderType = FolderType.MUSIC.toString();
                    mFolderType = FolderType.YY.toString();
                }
                Intent i1 = new Intent(CreateRichDocActivity.this, FolderSelectActivity.class);
                i1.putExtra("folderType", mFolderType);
                startActivityForResult(i1, REQ_SELECT_FOLDER);
            }
        });
    }

    private void createPost() {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            showToast(R.string.msg_connection);
            return;
        }
        if (!mRichEt.hasContent()) {
            showToast(R.string.msg_doc_content_cannot_null);
        } else {
            createDialog();
            mTvMenuRight.setEnabled(false);
            DocPut mDocEntity = new DocPut();
            if (TextUtils.isEmpty(mDepartmentId)) {
                mDocEntity.departmentId = "db320ad4-20e7-4fd3-84a8-3763f6a56366";
            } else {
                mDocEntity.departmentId = mDepartmentId;
            }
            mDocEntity.docType = mFromName;
            mDocEntity.tagId = mTagId;
            mDocEntity.docTypeSchema = mFromSchema;
            mDocEntity.bagFolderId = mFolderId;
            mDocEntity.title = mRichEt.getTitle();
            mDocEntity.tags = new ArrayList<>();
            if (mRichEt.getTags() != null) {
                mDocEntity.texts = mRichEt.getTags();
            }
            for (RichEntity entity : mRichEt.buildEditData()) {
                if (!TextUtils.isEmpty(entity.getInputStr())) {
                    DocPut.DocPutText docPutText = new DocPut.DocPutText();
                    docPutText.text = StringUtils.buildDataAtUser(entity.getInputStr());
                    mDocEntity.details.add(new DocPut.DocDetail(NewDocType.DOC_TEXT.toString(), docPutText));
                    mDocEntity.userIds.addAll(StringUtils.getAtUserIds(entity.getInputStr()));
                } else if (entity.getImage() != null && !TextUtils.isEmpty(entity.getImage().getPath())) {
                    DocPut.DocPutImage docPutImage = new DocPut.DocPutImage();
                    docPutImage.path = entity.getImage().getPath();
                    docPutImage.h = entity.getImage().getH();
                    docPutImage.w = entity.getImage().getW();
                    docPutImage.size = entity.getImage().getSize();
                    mDocEntity.details.add(new DocPut.DocDetail(NewDocType.DOC_IMAGE.toString(), docPutImage));
                }
            }
            for (RichEntity entity : mHideList) {
                if (!TextUtils.isEmpty(entity.getInputStr())) {
                    DocPut.DocPutText docPutText = new DocPut.DocPutText();
                    docPutText.text = entity.getInputStr().toString();
                    mDocEntity.coin.details.add(new DocPut.DocDetail(NewDocType.DOC_TEXT.toString(), docPutText));
                } else if (entity.getImage() != null && !TextUtils.isEmpty(entity.getImage().getPath())) {
                    DocPut.DocPutImage docPutImage = new DocPut.DocPutImage();
                    docPutImage.path = entity.getImage().getPath();
                    docPutImage.h = entity.getImage().getH();
                    docPutImage.w = entity.getImage().getW();
                    docPutImage.size = entity.getImage().getSize();
                    mDocEntity.coin.details.add(new DocPut.DocDetail(NewDocType.DOC_IMAGE.toString(), docPutImage));
                }
            }
            mDocEntity.userIds.addAll(mUserIds);
            mDocEntity.coinComment = mHideType;
            if (mMusicInfo != null) {
                DocPut.DocPutMusic docPutMusic = new DocPut.DocPutMusic();
                docPutMusic.name = mMusicInfo.getTitle();
                docPutMusic.timestamp = mMusicInfo.getDuration();
                docPutMusic.url = mMusicInfo.getUrl();
                docPutMusic.cover = mMusicCover;
                mDocEntity.details.add(new DocPut.DocDetail(NewDocType.DOC_MUSIC.toString(), docPutMusic));
            }
            if (mHideList.size() > 0) {
                mDocEntity.coin.coin = 1;
            } else {
                mDocEntity.coin.coin = 0;
            }
            mDocEntity.cover = mBgCover;
            mPresenter.createDoc(mDocEntity, mDocType, mDoc == null ? "" : mDoc.getDocId(), coverSize);
        }
    }

    @OnClick({R.id.iv_add_img, R.id.iv_alt_user, R.id.iv_add_hide_doc, R.id.iv_add_bag, R.id.iv_add_music, R.id.tv_menu, R.id.rl_release_position, R.id.tv_select})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add_img:
                choosePhoto();
                break;
            case R.id.iv_alt_user:
                Intent i3 = new Intent(CreateRichDocActivity.this, SearchActivity.class);
                i3.putExtra("show_type", SearchActivity.SHOW_USER);
                startActivityForResult(i3, REQ_ADD_SEARCH);
                break;
            case R.id.iv_add_hide_doc:
                if (mHideList.size() > 0) {
                    Intent i = new Intent(CreateRichDocActivity.this, CreateRichDocHideActivity.class);
                    i.putParcelableArrayListExtra("hide_list", mHideList);
                    i.putExtra("hide_type", mHideType);
                    startActivityForResult(i, REQ_ADD_HIDE);
                } else {
                    final AlertDialogUtil alertDialogUtil = AlertDialogUtil.getInstance();
                    alertDialogUtil.createNormalDialog(CreateRichDocActivity.this, "添加“节操可见”隐藏区\n需要消耗1节操");
                    alertDialogUtil.setOnClickListener(new AlertDialogUtil.OnClickListener() {
                        @Override
                        public void CancelOnClick() {
                            alertDialogUtil.dismissDialog();
                        }

                        @Override
                        public void ConfirmOnClick() {
                            Intent i = new Intent(CreateRichDocActivity.this, CreateRichDocHideActivity.class);
                            i.putParcelableArrayListExtra("hide_list", mHideList);
                            i.putExtra("hide_type", mHideType);
                            startActivityForResult(i, REQ_ADD_HIDE);
                            alertDialogUtil.dismissDialog();
                        }
                    });
                    alertDialogUtil.showDialog();
                }
                break;
            case R.id.iv_add_bag:
                if (bottomFragment != null)
                    bottomFragment.show(getSupportFragmentManager(), "createDoc");
                break;
            case R.id.iv_add_music:
                Intent i2 = new Intent(CreateRichDocActivity.this, AddMusicActivity.class);
                i2.putExtra("music_info", mMusicInfo);
                i2.putExtra("music_cover", mMusicCover);
                startActivityForResult(i2, REQ_GET_FROM_SELECT_MUSIC);
                break;
            case R.id.tv_menu:
                createPost();
                break;
            case R.id.rl_release_position:
                Intent intent = new Intent(CreateRichDocActivity.this, DocSelectActivity.class);
                intent.putExtra("uuid", mTagId);
                startActivityForResult(intent, REQUEST_CODE_SELECT);
                break;
            case R.id.tv_select:
                NewFolderWenZhangActivity.startActivityForResult(CreateRichDocActivity.this, PreferenceUtils.getUUid(), FolderType.WZ.toString(), "my", true);
                break;
        }
    }

    private void choosePhoto() {
        if (mImageSize < ICON_NUM_LIMIT) {
            try {
                ArrayList<String> temp = new ArrayList<>();
                mIsCover = false;
                DialogUtils.createImgChooseDlg(this, null, this, temp, ICON_NUM_LIMIT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showToast(R.string.msg_create_doc_9_jpg);
        }
    }

    public boolean ismIsCover() {
        return mIsCover;
    }

    public void setmIsCover(boolean mIsCover) {
        this.mIsCover = mIsCover;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_ADD_SEARCH && resultCode == RESULT_OK) {
            if (data != null) {
                String userId = data.getStringExtra("user_id");
                String userName = data.getStringExtra("user_name");
                mUserIds.add(userId);
                mRichEt.insertTextInCurSelection("@" + userName, userId);
            }
        } else if (requestCode == REQ_GET_FROM_SELECT_MUSIC && resultCode == RESULT_OK) {
            if (data != null) {
                mMusicInfo = data.getParcelableExtra("music_info");
                mMusicCover = data.getParcelableExtra("music_cover");
                mIvAddMusic.setSelected(true);
            } else {
                mMusicInfo = null;
                mIvAddMusic.setSelected(false);
            }
        } else if (requestCode == REQ_ADD_HIDE && resultCode == RESULT_OK) {
            if (data != null) {
                mHideList = data.getParcelableArrayListExtra("hide_list");
                mHideType = data.getBooleanExtra("hide_type", false);
                mUserIds = data.getStringArrayListExtra("at_user");
                if (mHideList.size() > 0) {
                    mIvAddHide.setSelected(true);
                } else {
                    mIvAddHide.setSelected(false);
                }
            } else {
                mHideList.clear();
                mHideType = false;
                mIvAddHide.setSelected(false);
            }
        } else if (requestCode == REQ_SELECT_FOLDER && resultCode == RESULT_OK) {
            if (!TextUtils.isEmpty(data.getStringExtra("folderId"))) {
                mIvAddBag.setSelected(true);
                mFolderId = data.getStringExtra("folderId");
                if ("11111111-1111-1111-1111-111111111111".equals(mFolderId)) {
                    showToast("不能选择收藏文件夹");
                    mFolderId = "";
                    mIvAddBag.setSelected(false);
                }
            } else {
                mIvAddBag.setSelected(false);
                mFolderId = "";
            }
        } else if (requestCode == REQ_RECOMMEND_TAG && resultCode == RESULT_OK) {
            if (data != null) {
                mRichEt.setTags(data.getStringArrayListExtra("tags"));
            }
        } else if (requestCode == REQUEST_CODE_SELECT && resultCode == RESULT_OK) {
            if (data != null) {
                mTagId = data.getStringExtra("uuid");
                mFromName = data.getStringExtra("context");
                mTvRelease.setText(mFromName);
            }
        } else if (requestCode == REQ_WEN_ZHANG && resultCode == RESULT_OK) {
            if (data != null) {
                String docId = data.getStringExtra("docId");
                Intent i = new Intent();
                i.putExtra("docId", docId);
                setResult(RESULT_OK, i);
                finish();
            }
        } else {
            DialogUtils.handleImgChooseResult(this, requestCode, resultCode, data, new DialogUtils.OnPhotoGetListener() {

                @Override
                public void onPhotoGet(final ArrayList<String> photoPaths, boolean override) {
                    if (mIsCover) {
                        mRichEt.setCover(photoPaths.get(0));
                        mBgCover = photoPaths.get(0);
                        coverSize = 0;
                    } else {
//                        Collections.reverse(photoPaths);
                        createDialog("图片插入中...");
                        onGetPhotos(photoPaths);
                        mIsCover = true;
                    }
                }
            });
        }
    }

    private void onGetPhotos(final ArrayList<String> paths) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> res) throws Exception {
                mRichEt.measure(0, 0);
                try {
                    for (String s : paths) {
                        res.onNext(s);
                    }
                    res.onComplete();
                } catch (Exception e) {
                    res.onError(e);
                }
            }

        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {

                    @Override
                    public void onError(Throwable e) {
                        finalizeDialog();
                        showToast("图片插入失败");
                    }

                    @Override
                    public void onComplete() {
                        finalizeDialog();
                        showToast("图片插入成功");
                    }

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        mRichEt.insertImage(s);
                        mImageSize++;
                    }
                });
    }

    @Override
    public void onBackPressed() {
//        if (mRichEt.hasContent() && !mIsUpdateDoc) {
//            //DialogUtils.showAbandonModifyDlg(this);
//            final AlertDialogUtil alertDialogUtil = AlertDialogUtil.getInstance();
//            alertDialogUtil.createNewNormalDialog(this, "是否保存草稿", "不保存");
//            alertDialogUtil.setOnClickListener(new AlertDialogUtil.OnClickListener() {
//                @Override
//                public void CancelOnClick() {
//                    alertDialogUtil.dismissDialog();
//                    PreferenceUtils.saveDoc(CreateRichDocActivity.this, "");
//                    finish();
//                }
//
//                @Override
//                public void ConfirmOnClick() {
//                    alertDialogUtil.dismissDialog();
//                    saveToLocal();
//                    finish();
//                }
//            });
//            alertDialogUtil.showDialog();
//        } else {
//            super.onBackPressed();
//        }
        saveToLocal();
        finish();
    }

    @Override
    public void onSendSuccess(final String id, final String path) {
        mTvMenuRight.setEnabled(true);
        sendDoc = true;
        finalizeDialog();
        if (mDoc != null) {
            showToast(R.string.msg_update_doc_success);
        } else {
            showToast(R.string.msg_create_doc_success);
        }
        if (!TextUtils.isEmpty(id)) {
            if (select) {
                Intent i = new Intent();
                i.putExtra("docId", id);
                setResult(RESULT_OK, i);
                PreferenceUtils.saveDoc(CreateRichDocActivity.this, "");
                finish();
                return;
            }
            final AlertDialogUtil dialogUtil = AlertDialogUtil.getInstance();

            dialogUtil.createPromptNormalDialog(this, "是否分享到动态");
            dialogUtil.setOnClickListener(new AlertDialogUtil.OnClickListener() {
                @Override
                public void CancelOnClick() {
                    Intent i = new Intent();
                    setResult(RESPONSE_CODE, i);
                    PreferenceUtils.saveDoc(CreateRichDocActivity.this, "");
                    finish();
                    dialogUtil.dismissDialog();
                }

                @Override
                public void ConfirmOnClick() {
                    ShareArticleEntity entity = new ShareArticleEntity();
                    entity.setDocId(id);
                    entity.setTitle(mRichEt.getTitle());
                    entity.setCover(path);
                    String content = "";
                    for (RichEntity tmp : mRichEt.buildEditData()) {
                        if (!TextUtils.isEmpty(tmp.getInputStr())) {
                            content = TagControl.getInstance().paresToString(tmp.getInputStr());
                            break;
                        }
                    }
                    entity.setContent(content);
                    UserTopEntity entity1 = new UserTopEntity();
                    entity1.setUserId(PreferenceUtils.getUUid());
                    entity1.setUserName(PreferenceUtils.getAuthorInfo().getUserName());
                    entity1.setLevel(PreferenceUtils.getAuthorInfo().getLevel());
                    entity1.setHeadPath(PreferenceUtils.getAuthorInfo().getHeadPath());
                    entity.setDocCreateUser(entity1);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    String mTime = sdf.format(new Date());
                    entity.setCreateTime(mTime);
                    ArrayList<UserFollowTagEntity> text = new ArrayList<>();
                    for (String tag : mRichEt.getTags()) {
                        UserFollowTagEntity tagEntity = new UserFollowTagEntity(tag, "");
                        text.add(tagEntity);
                    }
                    entity.setTexts(text);
                    CreateForwardActivity.startActivity(CreateRichDocActivity.this, entity);
                    PreferenceUtils.saveDoc(CreateRichDocActivity.this, "");
                    dialogUtil.dismissDialog();
                    finish();
                }
            });
            dialogUtil.showDialog();
        } else {
            Intent i = new Intent();
            setResult(RESPONSE_CODE, i);
            PreferenceUtils.saveDoc(CreateRichDocActivity.this, "");
            finish();
        }
    }

    @Override
    public void onFailure(int code, String msg) {
        mTvMenuRight.setEnabled(true);
        finalizeDialog();
        ErrorCodeUtils.showErrorMsgByCode(CreateRichDocActivity.this, code, msg);
    }
}
