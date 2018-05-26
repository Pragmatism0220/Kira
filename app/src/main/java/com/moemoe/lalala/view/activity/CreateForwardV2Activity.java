package com.moemoe.lalala.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.di.components.DaggerCreateForwardComponent;
import com.moemoe.lalala.di.modules.CreateForwardModule;
import com.moemoe.lalala.model.entity.CommentSendV2Entity;
import com.moemoe.lalala.model.entity.DocResponse;
import com.moemoe.lalala.model.entity.DynamicContentEntity;
import com.moemoe.lalala.model.entity.FolderType;
import com.moemoe.lalala.model.entity.ForwardSendEntity;
import com.moemoe.lalala.model.entity.Image;
import com.moemoe.lalala.model.entity.MessageDynamicEntity;
import com.moemoe.lalala.model.entity.NewDynamicEntity;
import com.moemoe.lalala.model.entity.ProductDyEntity;
import com.moemoe.lalala.model.entity.RetweetEntity;
import com.moemoe.lalala.model.entity.ShareArticleEntity;
import com.moemoe.lalala.model.entity.ShareArticleSendEntity;
import com.moemoe.lalala.model.entity.ShareFolderEntity;
import com.moemoe.lalala.model.entity.ShareFolderSendEntity;
import com.moemoe.lalala.model.entity.ShareStreamSendEntity;
import com.moemoe.lalala.model.entity.tag.BaseTag;
import com.moemoe.lalala.model.entity.tag.UserUrlSpan;
import com.moemoe.lalala.presenter.CreateForwardContract;
import com.moemoe.lalala.presenter.CreateForwardPresenter;
import com.moemoe.lalala.utils.AlertDialogUtil;
import com.moemoe.lalala.utils.AndroidBug5497Workaround;
import com.moemoe.lalala.utils.DensityUtil;
import com.moemoe.lalala.utils.DialogUtils;
import com.moemoe.lalala.utils.ErrorCodeUtils;
import com.moemoe.lalala.utils.FileItemDecoration;
import com.moemoe.lalala.utils.NetworkUtils;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.SoftKeyboardUtils;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.utils.TagUtils;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.utils.tag.TagControl;
import com.moemoe.lalala.view.adapter.SelectItemAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.CropSquareTransformation;
import jp.wasabeef.glide.transformations.CropTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.moemoe.lalala.utils.StartActivityConstant.REQ_ALT_USER;
import static com.moemoe.lalala.utils.StartActivityConstant.REQ_FORWARD_DYNAMIC;

/**
 * 转发V2
 * Created by yi on 2018/3/19.
 */

public class CreateForwardV2Activity extends BaseAppCompatActivity implements CreateForwardContract.View {

    public static final int TYPE_ARTICLE = 0;
    public static final int TYPE_FOLDER = 1;
    public static final int TYPE_DYNAMIC = 2;
    public static final int TYPE_RETWEET = 3;
    public static final int TYPE_MUSIC = 4;
    public static final int TYPE_MOVIE = 5;
    public static final int TYPE_PRODUCT = 6;
    public static final int TYPE_MESSAGE = 7;
    private static final int LIMIT_CONTENT = 150;

    @BindView(R.id.et_content)
    EditText mEtContent;
    @BindView(R.id.tv_content_count)
    TextView mTvContentCount;
    @BindView(R.id.list)
    RecyclerView mRvImg;
    @BindView(R.id.ll_extra_root)
    LinearLayout mExtraRoot;
    @BindView(R.id.cb_comment)
    CheckBox mCbComment;
    @BindView(R.id.iv_add_img)
    ImageView mIvAddImg;
    @Inject
    CreateForwardPresenter mPresenter;

    private int mType;
    private SelectItemAdapter mSelectAdapter;
    private ArrayList<Object> mPaths;
    private String mId;
    private String mRtType;
    private String mUserId;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_create_comment_v2;
    }

    public static void startActivityForResult(Context context, NewDynamicEntity entity) {
        Intent i = new Intent(context, CreateForwardV2Activity.class);
        i.putExtra("type", TYPE_DYNAMIC);
        i.putExtra("dynamic", entity);
        ((BaseAppCompatActivity) context).startActivityForResult(i, REQ_FORWARD_DYNAMIC);
    }

    public static void startActivityForResult(Context context, DocResponse entity) {
        Intent i = new Intent(context, CreateForwardV2Activity.class);
        i.putExtra("type", TYPE_DYNAMIC);
        i.putExtra("dynamic", entity);
        ((BaseAppCompatActivity) context).startActivityForResult(i, REQ_FORWARD_DYNAMIC);
    }

    public static void startActivity(Context context, ShareArticleEntity entity) {
        Intent i = new Intent(context, CreateForwardV2Activity.class);
        i.putExtra("type", TYPE_ARTICLE);
        i.putExtra("article", entity);
        context.startActivity(i);
    }

    public static void startActivity(Context context, ShareFolderEntity entity, String mUserId) {
        Intent i = new Intent(context, CreateForwardV2Activity.class);
        i.putExtra("type", TYPE_FOLDER);
        i.putExtra("folder", entity);
        i.putExtra("mUserId", mUserId);
        context.startActivity(i);
    }

    public static void startActivity(Context context, int type, ShareFolderEntity entity, String mUserId) {
        Intent i = new Intent(context, CreateForwardV2Activity.class);
        i.putExtra("type", type);
        i.putExtra("folder", entity);
        i.putExtra("mUserId", mUserId);
        context.startActivity(i);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        AndroidBug5497Workaround.assistActivity(this);
        DaggerCreateForwardComponent.builder()
                .createForwardModule(new CreateForwardModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        mType = getIntent().getIntExtra("type", TYPE_ARTICLE);
        SoftKeyboardUtils.showSoftKeyboard(this, mEtContent);
        mUserId = getIntent().getStringExtra("mUserId");
        mEtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Editable editable = mEtContent.getText();
                int len = editable.length();
                if (len > LIMIT_CONTENT) {
                    int selEndIndex = Selection.getSelectionEnd(editable);
                    mEtContent.setText(editable.subSequence(0, LIMIT_CONTENT));
                    editable = mEtContent.getText();
                    int newLen = editable.length();
                    if (selEndIndex > newLen) {
                        selEndIndex = editable.length();
                    }
                    Selection.setSelection(editable, selEndIndex);
                }
                mTvContentCount.setText(mEtContent.getText().length() + "/" + LIMIT_CONTENT);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if (mType == TYPE_DYNAMIC) {
            NewDynamicEntity mDynamic = getIntent().getParcelableExtra("dynamic");
            if (mDynamic == null) {
                onBackPressed();
                return;
            }
            mId = mDynamic.getId();
            String temp;
            if (!"DYNAMIC".equals(mDynamic.getType())) {
                temp = "//<at_user user_id=" + mDynamic.getCreateUser().getUserId() + ">@" + mDynamic.getCreateUser().getUserName() + "</at_user>: " + mDynamic.getText();
            } else {
                temp = "";
            }
            mEtContent.setHint("转发到动态");
            mEtContent.setText(TagControl.getInstance().paresToSpann(this, temp));
            mEtContent.setSelection(0);
            mSelectAdapter = new SelectItemAdapter(this);
            mSelectAdapter.setSelectSize(0);
            mRvImg.setLayoutManager(new GridLayoutManager(this, 3));
            mRvImg.addItemDecoration(new FileItemDecoration());
            mRvImg.setAdapter(mSelectAdapter);
            mPaths = new ArrayList<>();
            mIvAddImg.setVisibility(View.VISIBLE);
            createForward(mDynamic);
            mCbComment.setVisibility(View.VISIBLE);
            mCbComment.setText("同时评论");
        }
        if (mType == TYPE_ARTICLE) {
            ShareArticleEntity mArticle = getIntent().getParcelableExtra("article");
            if (mArticle == null) {
                onBackPressed();
                return;
            }
            mEtContent.setHint("转发到动态");
            mIvAddImg.setVisibility(View.GONE);
            mId = mArticle.getDocId();
            mCbComment.setText("同时评论");
            createArticle(mArticle);
        }
        if (mType == TYPE_FOLDER || mType == TYPE_MOVIE || mType == TYPE_MUSIC) {
            ShareFolderEntity mFolder = getIntent().getParcelableExtra("folder");
            if (mFolder == null) {
                onBackPressed();
                return;
            }
            mId = mFolder.getFolderId();
            mIvAddImg.setVisibility(View.GONE);
            createFolder(mFolder);
        }
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {

    }

    @Override
    protected void initListeners() {
    }

    @Override
    protected void initData() {

    }

    private void createArticle(final ShareArticleEntity folderEntity) {
        View article = LayoutInflater.from(this).inflate(R.layout.item_feed_type_5_v3, null);
        ImageView ivCover = article.findViewById(R.id.iv_cover);
        ImageView ivAvatar = article.findViewById(R.id.iv_user_avatar);
        TextView tvMark = article.findViewById(R.id.tv_mark);
        TextView tvUserName = article.findViewById(R.id.tv_user_name);
        TextView tvTag1 = article.findViewById(R.id.tv_tag_1);
        TextView tvTag2 = article.findViewById(R.id.tv_tag_2);
        TextView tvReadNum = article.findViewById(R.id.tv_read_num);
        TextView tvTitle = article.findViewById(R.id.tv_title);

        int w = DensityUtil.getScreenWidth(this) - getResources().getDimensionPixelSize(R.dimen.x48);
        int h = getResources().getDimensionPixelSize(R.dimen.y400);
        Glide.with(this)
                .load(StringUtils.getUrl(this, folderEntity.getCover(), w, h, false, true))
                .error(R.drawable.shape_gray_e8e8e8_background)
                .placeholder(R.drawable.shape_gray_e8e8e8_background)
                .bitmapTransform(new CropTransformation(this, w, h))
                .into(ivCover);

        tvMark.setText("文章");
        tvTitle.setText(folderEntity.getTitle());
        int size = (int) getResources().getDimension(R.dimen.y32);
        Glide.with(this)
                .load(StringUtils.getUrl(this, folderEntity.getDocCreateUser().getHeadPath(), size, size, false, true))
                .error(R.drawable.bg_default_circle)
                .placeholder(R.drawable.bg_default_circle)
                .bitmapTransform(new CropCircleTransformation(this))
                .into(ivAvatar);

        ivAvatar.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                ViewUtils.toPersonal(CreateForwardV2Activity.this, folderEntity.getDocCreateUser().getUserId());
            }
        });

        tvUserName.setText(folderEntity.getDocCreateUser().getUserName());
        tvReadNum.setText("阅读" + folderEntity.getReadNum());

        //tag
        View[] tagsId = {tvTag1, tvTag2};
        tvTag1.setOnClickListener(null);
        tvTag2.setOnClickListener(null);
        if (folderEntity.getTexts().size() > 1) {
            tvTag1.setVisibility(View.VISIBLE);
            tvTag2.setVisibility(View.VISIBLE);
        } else if (folderEntity.getTexts().size() > 0) {
            tvTag1.setVisibility(View.VISIBLE);
            tvTag2.setVisibility(View.INVISIBLE);
        } else {
            tvTag1.setVisibility(View.INVISIBLE);
            tvTag2.setVisibility(View.INVISIBLE);
        }
        int tagSize = tagsId.length > folderEntity.getTexts().size() ? folderEntity.getTexts().size() : tagsId.length;
        for (int i = 0; i < tagSize; i++) {
            TagUtils.setBackGround(folderEntity.getTexts().get(i).getText(), tagsId[i]);
        }
        mExtraRoot.addView(article);
    }

    private void createFolder(final ShareFolderEntity folderEntity) {
        View folder = LayoutInflater.from(this).inflate(R.layout.item_feed_type_2_v3, null);
        folder.setBackgroundResource(R.drawable.shape_gray_f6f6f6_background_y8);
        TextView tvMark = folder.findViewById(R.id.tv_mark);
        ImageView ivCover = folder.findViewById(R.id.iv_cover);
        TextView tvTitle = folder.findViewById(R.id.tv_title);
        TextView tvTag1 = folder.findViewById(R.id.tv_tag_1);
        TextView tvTag2 = folder.findViewById(R.id.tv_tag_2);
        ImageView ivAvatar = folder.findViewById(R.id.iv_user_avatar);
        TextView tvUserName = folder.findViewById(R.id.tv_user_name);
        TextView tvCoin = folder.findViewById(R.id.tv_coin);
        TextView tvExtra = folder.findViewById(R.id.tv_extra);
        View coverRoot = folder.findViewById(R.id.fl_cover_root);
        folder.findViewById(R.id.iv_play).setVisibility(View.GONE);

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) coverRoot.getLayoutParams();
        lp.topMargin = getResources().getDimensionPixelSize(R.dimen.y16);
        lp.bottomMargin = getResources().getDimensionPixelSize(R.dimen.y16);
        lp.leftMargin = getResources().getDimensionPixelSize(R.dimen.x16);
        coverRoot.requestLayout();

        int w = getResources().getDimensionPixelSize(R.dimen.x222);
        int h = getResources().getDimensionPixelSize(R.dimen.y160);
        Glide.with(this)
                .load(StringUtils.getUrl(this, folderEntity.getFolderCover(), w, h, false, true))
                .error(R.drawable.shape_gray_e8e8e8_background)
                .placeholder(R.drawable.shape_gray_e8e8e8_background)
                .bitmapTransform(new CropTransformation(this, w, h)
                        , new RoundedCornersTransformation(this, getResources().getDimensionPixelSize(R.dimen.y8), 0))
                .into(ivCover);

        tvTitle.setText(folderEntity.getFolderName());

        //tag
        View[] tagsId = {tvTag1, tvTag2};
        tvTag1.setOnClickListener(null);
        tvTag2.setOnClickListener(null);
        if (folderEntity.getFolderTags().size() > 1) {
            tvTag1.setVisibility(View.VISIBLE);
            tvTag2.setVisibility(View.VISIBLE);
        } else if (folderEntity.getFolderTags().size() > 0) {
            tvTag1.setVisibility(View.VISIBLE);
            tvTag2.setVisibility(View.INVISIBLE);
        } else {
            tvTag1.setVisibility(View.INVISIBLE);
            tvTag2.setVisibility(View.INVISIBLE);
        }
        int tagSize = tagsId.length > folderEntity.getFolderTags().size() ? folderEntity.getFolderTags().size() : tagsId.length;
        for (int i = 0; i < tagSize; i++) {
            TagUtils.setBackGround(folderEntity.getFolderTags().get(i), tagsId[i]);
        }

        //user
        int avatarSize = getResources().getDimensionPixelSize(R.dimen.y32);
        Glide.with(this)
                .load(StringUtils.getUrl(this, folderEntity.getCreateUser().getHeadPath(), avatarSize, avatarSize, false, true))
                .error(R.drawable.bg_default_circle)
                .placeholder(R.drawable.bg_default_circle)
                .bitmapTransform(new CropCircleTransformation(this))
                .into(ivAvatar);
        tvUserName.setText(folderEntity.getCreateUser().getUserName());
        ivAvatar.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                ViewUtils.toPersonal(CreateForwardV2Activity.this, folderEntity.getCreateUser().getUserId());
            }
        });
        tvUserName.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                ViewUtils.toPersonal(CreateForwardV2Activity.this, folderEntity.getCreateUser().getUserId());
            }
        });

        tvMark.setVisibility(View.VISIBLE);
        if (folderEntity.getFolderType().equals(FolderType.ZH.toString())) {
            tvMark.setText("综合");
            tvMark.setBackgroundResource(R.drawable.shape_rect_zonghe);
        } else if (folderEntity.getFolderType().equals(FolderType.TJ.toString())) {
            tvMark.setText("图集");
            tvMark.setBackgroundResource(R.drawable.shape_rect_tuji);
        } else if (folderEntity.getFolderType().equals(FolderType.MH.toString())) {
            tvMark.setText("漫画");
            tvMark.setBackgroundResource(R.drawable.shape_rect_manhua);
        } else if (folderEntity.getFolderType().equals(FolderType.XS.toString())) {
            tvMark.setText("小说");
            tvMark.setBackgroundResource(R.drawable.shape_rect_xiaoshuo);
        } else if (folderEntity.getFolderType().equals(FolderType.SP.toString())) {
            tvMark.setText("视频集");
            tvMark.setBackgroundResource(R.drawable.shape_rect_shipin);
        } else if (folderEntity.getFolderType().equals(FolderType.YY.toString())) {
            tvMark.setText("音乐集");
            tvMark.setBackgroundResource(R.drawable.shape_rect_yinyue);
        } else {
            tvMark.setVisibility(View.GONE);
        }

        tvCoin.setText(folderEntity.getCoin() + "节操");
        tvExtra.setText(folderEntity.getItems() + "项");
        mExtraRoot.addView(folder);
    }

    private void createMessage(final MessageDynamicEntity entity) {
        View folder = LayoutInflater.from(this).inflate(R.layout.item_feed_type_6_v3, null);
        ImageView ivAvatar = folder.findViewById(R.id.iv_user_avatar);
        TextView tvUserName = folder.findViewById(R.id.tv_user_name);
        TextView tvContent = folder.findViewById(R.id.tv_content);
        TextView tvDesc = folder.findViewById(R.id.tv_content_desc);

        int size = getResources().getDimensionPixelSize(R.dimen.y44);
        Glide.with(this)
                .load(StringUtils.getUrl(this, entity.getHeadPath(), size, size, false, true))
                .error(R.drawable.bg_default_circle)
                .placeholder(R.drawable.bg_default_circle)
                .bitmapTransform(new CropCircleTransformation(this))
                .into(ivAvatar);
        tvUserName.setText(entity.getUserName());
        folder.findViewById(R.id.ll_user_root).setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                ViewUtils.toPersonal(CreateForwardV2Activity.this, entity.getUserId());
            }
        });
        tvContent.setText(entity.getShowMsg());
        tvDesc.setText(entity.getDate());
        mExtraRoot.addView(folder);
    }

    private void createProduct(ProductDyEntity entity) {
        View folder = LayoutInflater.from(this).inflate(R.layout.item_feed_type_7_v3, null);
        ImageView ivCover = folder.findViewById(R.id.iv_cover);
        TextView tvTitle = folder.findViewById(R.id.tv_title);
        TextView tvContent = folder.findViewById(R.id.tv_content);
        TextView tv_coin = folder.findViewById(R.id.tv_coin);

        int size = getResources().getDimensionPixelSize(R.dimen.y140);
        Glide.with(this)
                .load(StringUtils.getUrl(this, entity.getIcon(), size, size, false, true))
                .error(R.drawable.shape_gray_e8e8e8_background)
                .placeholder(R.drawable.shape_gray_e8e8e8_background)
                .into(ivCover);
        tvTitle.setText(entity.getProductName());
        tvContent.setText(entity.getDescribe());
        String str = "";
        if (entity.getCoin() > 0) {
            str = entity.getCoin() + "节操";
        }
        if (entity.getRmb() > 0) {
            str += String.format(Locale.getDefault(), "%.2f元", (float) entity.getRmb() / 100);
        }
        tv_coin.setText(str);
        mExtraRoot.addView(folder);
    }

    private void createDelete() {
        TextView tv = new TextView(this);
        tv.setText("该内容已被删除");
        tv.setTextColor(ContextCompat.getColor(this, R.color.white));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, this.getResources().getDimension(R.dimen.x36));
        tv.setGravity(Gravity.CENTER);
        tv.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_e8e8e8));
        int h = (int) getResources().getDimension(R.dimen.y320);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, h);
        tv.setLayoutParams(lp);
        mExtraRoot.addView(tv);
    }

    private void createDynamic(String path, CharSequence content) {
        View folder = LayoutInflater.from(this).inflate(R.layout.item_forward_dynamic, null);
        ImageView iv = folder.findViewById(R.id.iv_cover);
        TextView tv = folder.findViewById(R.id.tv_content);
        if (TextUtils.isEmpty(path)) {
            iv.setVisibility(View.GONE);
        } else {
            int size = (int) getResources().getDimension(R.dimen.x180);
            Glide.with(this)
                    .load(StringUtils.getUrl(this, path, size, size, false, true))
                    .error(R.drawable.shape_gray_e8e8e8_background)
                    .placeholder(R.drawable.shape_gray_e8e8e8_background)
                    .bitmapTransform(new CropSquareTransformation(this))
                    .into(iv);
        }
        tv.setText(content);
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        mExtraRoot.addView(folder);
    }

    private void createForward(NewDynamicEntity entity) {
        mRtType = entity.getType();
        if ("DELETE".equals(entity.getType())) {
            createDelete();
        }
        if ("DYNAMIC".equals(entity.getType())) {
            DynamicContentEntity dynamicContentEntity = new Gson().fromJson(entity.getDetail(), DynamicContentEntity.class);
            String path = "";
            if (dynamicContentEntity.getImages() != null && dynamicContentEntity.getImages().size() > 0) {
                path = dynamicContentEntity.getImages().get(0).getPath();
            }
            String retweetContent = "@" + entity.getCreateUser().getUserName() + ": " + TagControl.getInstance().paresToSpann(this, entity.getText()).toString();
            String retweetColorStr = "@" + entity.getCreateUser().getUserName() + ":";
            SpannableStringBuilder style1 = new SpannableStringBuilder(retweetContent);
            UserUrlSpan span = new UserUrlSpan(this, entity.getCreateUser().getUserId(), null);
            style1.setSpan(span, retweetContent.indexOf(retweetColorStr), retweetContent.indexOf(retweetColorStr) + retweetColorStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            createDynamic(path, style1);
        }
        if ("FOLDER".equals(entity.getType()) || "MUSIC".equals(entity.getType()) || "MOVIE".equals(entity.getType())) {
            ShareFolderEntity folderEntity = new Gson().fromJson(entity.getDetail(), ShareFolderEntity.class);
            createFolder(folderEntity);
        }
        if ("ARTICLE".equals(entity.getType())) {
            ShareArticleEntity articleEntity = new Gson().fromJson(entity.getDetail(), ShareArticleEntity.class);
            createArticle(articleEntity);
        }
        if ("RETWEET".equals(entity.getType())) {
            RetweetEntity retweetEntity = new Gson().fromJson(entity.getDetail(), RetweetEntity.class);
            String retweetContent = "@" + retweetEntity.getCreateUserName() + ": " + TagControl.getInstance().paresToSpann(this, retweetEntity.getContent()).toString();
            String retweetColorStr = "@" + retweetEntity.getCreateUserName() + ":";
            SpannableStringBuilder style1 = new SpannableStringBuilder(retweetContent);
            UserUrlSpan span = new UserUrlSpan(this, retweetEntity.getCreateUserId(), null);
            style1.setSpan(span, retweetContent.indexOf(retweetColorStr), retweetContent.indexOf(retweetColorStr) + retweetColorStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            String path = "";
            if (retweetEntity.getImages() != null && retweetEntity.getImages().size() > 0) {
                path = retweetEntity.getImages().get(0).getPath();
            }
            createDynamic(path, style1);
        }
        if ("PRODUCT".equals(entity.getType())) {
            final ProductDyEntity folderEntity = new Gson().fromJson(entity.getDetail(), ProductDyEntity.class);
            createProduct(folderEntity);
        }
        if ("MESSAGE".equals(entity.getType())) {
            MessageDynamicEntity folderEntity = new Gson().fromJson(entity.getDetail(), MessageDynamicEntity.class);
            createMessage(folderEntity);
        }

    }

    @OnClick({R.id.iv_add_img, R.id.iv_alt_user, R.id.tv_send, R.id.go_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add_img:
                if (mPaths.size() < 1) {
                    try {
                        ArrayList<String> res = new ArrayList<>();
                        for (Object tmp : mPaths) {
                            res.add((String) tmp);
                        }
                        DialogUtils.createImgChooseDlg(CreateForwardV2Activity.this, null, CreateForwardV2Activity.this, res, 1).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    showToast("只能选择一张图片");
                }
                break;
            case R.id.iv_alt_user:
                Intent i3 = new Intent(CreateForwardV2Activity.this, SearchActivity.class);
                i3.putExtra("show_type", SearchActivity.SHOW_USER);
                startActivityForResult(i3, REQ_ALT_USER);
                break;
            case R.id.tv_send:
                done();
                break;
            case R.id.go_back:
                onBackPressed();
                break;
        }
    }


    public void onBackPressed() {
        super.onBackPressed();
        SoftKeyboardUtils.dismissSoftKeyboard(this);
        finish();
    }

    private void done() {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            showToast(R.string.msg_connection);
            return;
        }
        if (TextUtils.isEmpty(mEtContent.getText())) {
            showToast(R.string.msg_doc_content_cannot_null);
            return;
        }
        if (TextUtils.isEmpty(mEtContent.getText().toString().replace(" ", ""))) {
            showToast("转发内容不能全为空格");
            return;
        }
        if (mEtContent.getText().length() > LIMIT_CONTENT) {
            showToast("超过字数限制");
            return;
        }
        createDialog();
        if (mType == TYPE_DYNAMIC) {
            ForwardSendEntity entity = new ForwardSendEntity();
            entity.rt = mCbComment.isChecked();
            entity.dynamicId = mId;
            entity.etText = TagControl.getInstance().paresToString(mEtContent.getText());
            entity.type = mRtType;
            if (mPaths.size() > 0) {
                Image image = new Image();
                image.setPath((String) mPaths.get(0));
                entity.img = image;
            } else {
                entity.img = new Image();
            }
            mPresenter.createForward(mType, entity);
        } else if (mType == TYPE_ARTICLE) {
            ShareArticleSendEntity entity = new ShareArticleSendEntity();
            entity.docId = mId;
            entity.shareText = TagControl.getInstance().paresToString(mEtContent.getText());
            mPresenter.createForward(mType, entity);
        } else if (mType == TYPE_FOLDER) {
            ShareFolderSendEntity entity = new ShareFolderSendEntity();
            entity.folderId = mId;
            entity.shareText = TagControl.getInstance().paresToString(mEtContent.getText());
            entity.folderCreateUser = mUserId;
            mPresenter.createForward(mType, entity);
        } else if (mType == TYPE_MOVIE || mType == TYPE_MUSIC) {
            mPresenter.createForward(mType, new ShareStreamSendEntity(mId, mUserId, TagControl.getInstance().paresToString(mEtContent.getText())));
        }

        if (mCbComment.isChecked()) {
            CommentSendV2Entity commentSendV2Entity = new CommentSendV2Entity();
            commentSendV2Entity.rt = mCbComment.isChecked();
            commentSendV2Entity.content = TagControl.getInstance().paresToString(mEtContent.getText());;
            commentSendV2Entity.commentTo = "";
            Set<HashMap<String, String>> attr = TagControl.getInstance().getAttr("at_user", mEtContent.getText());
            ArrayList<String> atUser = new ArrayList<>();
            for (HashMap<String, String> map : attr) {
                atUser.addAll(map.values());
            }
            commentSendV2Entity.atUsers = atUser;
            ArrayList<String> objects = new ArrayList<>();
            if (mPaths != null && mPaths.size() > 0) {
                for (int i = 0; i < mPaths.size(); i++) {
                    objects.add((String) mPaths.get(i));
                }
            }
            mPresenter.createComment(false, mId, commentSendV2Entity, objects, 0);
        }
    }

    private void insertTextInCurSelection(String str, String id) {
        SpannableStringBuilder lastEditStr = new SpannableStringBuilder(mEtContent.getText());
        int cursorIndex = mEtContent.getSelectionStart();
        BaseTag tag = new BaseTag();
        tag.setTag("at_user");
        tag.setSpan(new UserUrlSpan(this, tag));
        HashMap<String, String> attrs = new HashMap<>();
        attrs.put("user_id", id);
        tag.setAttrs(attrs);
        if (cursorIndex < 0) {
            lastEditStr.insert(lastEditStr.length(), str + " ");
            lastEditStr.setSpan(tag.getSpan(), mEtContent.getText().length(), mEtContent.getText().length() + str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            lastEditStr.insert(cursorIndex, str + " ");
            lastEditStr.setSpan(tag.getSpan(), cursorIndex, cursorIndex + str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        mEtContent.setText(lastEditStr);
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_ALT_USER && resultCode == RESULT_OK) {
            if (data != null) {
                String userId = data.getStringExtra("user_id");
                String userName = data.getStringExtra("user_name");
                insertTextInCurSelection("@" + userName, userId);
            }
        } else {
            mRvImg.setVisibility(View.VISIBLE);
            DialogUtils.handleImgChooseResult(this, requestCode, resultCode, data, new DialogUtils.OnPhotoGetListener() {

                @Override
                public void onPhotoGet(ArrayList<String> photoPaths, boolean override) {
                    mPaths.clear();
                    mPaths.addAll(photoPaths);
                    mSelectAdapter.setData(mPaths);
                }
            });
        }
    }

    @Override
    public void onFailure(int code, String msg) {
        finalizeDialog();
        ErrorCodeUtils.showErrorMsgByCode(this, code, msg);
    }

    @Override
    public void onCreateForwardSuccess(float i) {
        finalizeDialog();
        if (i == -1) {
            showToast("转发成功");
            onBackPressed();
        } else {
//            Intent intent = new Intent();
//            intent.putExtra("coin",i);
//            setResult(RESULT_OK,intent);
            AlertDialogUtil alertDialogUtil = AlertDialogUtil.getInstance();
            alertDialogUtil.createHongbaoDialog(this, i);
            alertDialogUtil.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    onBackPressed();
                }
            });
            alertDialogUtil.showDialog();
        }
    }

    @Override
    public void onCreateForwardSuccess() {
        finalizeDialog();
        showToast("转发成功");
        onBackPressed();
    }

    @Override
    public void onCreateCommentSuccess() {
        finalizeDialog();
        setResult(RESULT_OK);
        onBackPressed();
    }
}
