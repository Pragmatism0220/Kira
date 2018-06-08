package com.moemoe.lalala.view.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.Gson;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.moemoe.lalala.R;
import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.entity.BadgeEntity;
import com.moemoe.lalala.model.entity.BagDirEntity;
import com.moemoe.lalala.model.entity.CommentV2Entity;
import com.moemoe.lalala.model.entity.CommentV2SecEntity;
import com.moemoe.lalala.model.entity.DocDetailEntity;
import com.moemoe.lalala.model.entity.DocDetailNormalEntity;
import com.moemoe.lalala.model.entity.DocTagEntity;
import com.moemoe.lalala.model.entity.FolderType;
import com.moemoe.lalala.model.entity.Image;
import com.moemoe.lalala.model.entity.NewCommentEntity;
import com.moemoe.lalala.model.entity.NewDocType;
import com.moemoe.lalala.model.entity.ShareArticleEntity;
import com.moemoe.lalala.model.entity.TabEntity;
import com.moemoe.lalala.model.entity.TagLikeEntity;
import com.moemoe.lalala.model.entity.TagSendEntity;
import com.moemoe.lalala.model.entity.UserTopEntity;
import com.moemoe.lalala.model.entity.tag.UserUrlSpan;
import com.moemoe.lalala.netamusic.data.model.Music;
import com.moemoe.lalala.netamusic.data.model.MusicListType;
import com.moemoe.lalala.netamusic.player.AudioPlayer;
import com.moemoe.lalala.netamusic.player.MusicPreferences;
import com.moemoe.lalala.netamusic.player.Notifier;
import com.moemoe.lalala.netamusic.player.OnPlayerEventListener;
import com.moemoe.lalala.netamusic.player.PlayModeEnum;
import com.moemoe.lalala.utils.AlertDialogUtil;
import com.moemoe.lalala.utils.BitmapUtils;
import com.moemoe.lalala.utils.DensityUtil;
import com.moemoe.lalala.utils.DialogUtils;
import com.moemoe.lalala.utils.EncoderUtils;
import com.moemoe.lalala.utils.FileUtil;
import com.moemoe.lalala.utils.LevelSpan;
import com.moemoe.lalala.utils.NetworkUtils;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.utils.StorageUtils;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.utils.TagUtils;
import com.moemoe.lalala.utils.ToastUtils;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.utils.tag.TagControl;
import com.moemoe.lalala.view.activity.BaseAppCompatActivity;
import com.moemoe.lalala.view.activity.CommentSecListActivity;
import com.moemoe.lalala.view.activity.CommunityV1Activity;
import com.moemoe.lalala.view.activity.CreateCommentV2Activity;
import com.moemoe.lalala.view.activity.CreateForwardV2Activity;
import com.moemoe.lalala.view.activity.ImageBigSelectActivity;
import com.moemoe.lalala.view.activity.JuBaoActivity;
import com.moemoe.lalala.view.activity.NewDocDetailActivity;
import com.moemoe.lalala.view.activity.PersonalV2Activity;
import com.moemoe.lalala.view.activity.WebViewActivity;
import com.moemoe.lalala.view.widget.longimage.LongImageView;
import com.moemoe.lalala.view.widget.netamenu.BottomMenuFragment;
import com.moemoe.lalala.view.widget.netamenu.MenuItem;
import com.moemoe.lalala.view.widget.view.DocLabelView;
import com.moemoe.lalala.view.widget.view.NewDocLabelAdapter;

import org.slf4j.MDC;

import java.io.File;
import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.CropTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


/**
 * Created by Haru on 2016/4/14 0014.
 */
public class DocRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SeekBar.OnSeekBarChangeListener, OnPlayerEventListener {

    private static final int TYPE_CREATOR = 0;
    private static final int TYPE_TEXT = 1;
    private static final int TYPE_IMAGE = 2;
    private static final int TYPE_MUSIC = 3;
    private static final int TYPE_LINK = 4;
    private static final int TYPE_CHAPTER = 5;
    private static final int TYPE_LABEL = 6;
    private static final int TYPE_COMMENT = 7;
    private static final int TYPE_COIN = 8;
    private static final int TYPE_COIN_TEXT = 9;
    private static final int TYPE_COIN_IMAGE = 10;
    private static final int TYPE_FLOOR = 11;
    private static final int TYPE_FOLDER = 12;
    private static final int TYPE_TITLE = 13;
    private static final int TYPE_USERLIKE = 14;

    // private static final long UPDATE_PROGRESS_INTERVAL = 1000;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    // private Player mPlayer;
    private MusicHolder mMusicHolder;
    private LabelHolder mLabelHolder;
    // private Song mMusicInfo;
    private int mTagsPosition = -1;
    private DocDetailEntity mDocBean;
    //    private ArrayList<CommentV2Entity> mComments;
    private ArrayList<DocDetailNormalEntity> mComments;
    private ArrayList<DocTagEntity> mTags;
    private OnItemClickListener onItemClickListener;
    private BottomMenuFragment fragment;
    private boolean sortTime;
    private int commentType = 1;//0 转发 1 评论 2 赞
    // private Handler mHandler = new Handler();
    private ArrayList<DocDetailNormalEntity> mPreComments;
    private boolean showFavorite = true;
    private int mPrePosition;
    private Music mMusicInfo;
    private boolean isFirstClick = true;
    private boolean isDraggingProgress;
    private int mLastProgress;
    private int mPreMode;
    private int mPinLun;
    private boolean isCommentLz = true;
    private int sortType = 0;

    //    private Runnable mProgressCallback = new Runnable() {
//        @Override
//        public void run() {
//            if (mPlayer.isPlaying()) {
//                int progress = (int) (mMusicHolder.sbMusicTime.getMax()
//                        * ((float) mPlayer.getProgress() / (float) getCurrentSongDuration()));
//                updateProgressTextWithDuration(mPlayer.getProgress());
//                if (progress >= 0 && progress <= mMusicHolder.sbMusicTime.getMax()) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        mMusicHolder.sbMusicTime.setProgress(progress, true);
//                    } else {
//                        mMusicHolder.sbMusicTime.setProgress(progress);
//                    }
//                    mHandler.postDelayed(this, UPDATE_PROGRESS_INTERVAL);
//                }
//            }
//        }
//    };

    public boolean isShowFavorite() {
        return showFavorite;
    }

    public void setShowFavorite(boolean showFavorite) {
        this.showFavorite = showFavorite;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public DocRecyclerViewAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mComments = new ArrayList<>();
        mTags = new ArrayList<>();
        mPreComments = new ArrayList<>();
        // mPlayer = Player.getInstance(mContext);
        // mPlayer.registerCallback(this);
        mPreMode = MusicPreferences.getPlayMode();
        fragment = new BottomMenuFragment();
        AudioPlayer.get().addOnPlayEventListener(this);
    }

    public void releaseAdapter() {
        // mPlayer.pause();
        // mPlayer.unregisterCallback(this);
        if (!isFirstClick) {
            AudioPlayer.get().stopPlayer();
            AudioPlayer.get().setShowNotify(true);
            AudioPlayer.get().clearList(false);
            AudioPlayer.get().initList(MusicListType.TYPE_NORMAL);
            MusicPreferences.savePlayMode(mPreMode);
        }
        AudioPlayer.get().removeOnPlayEventListener(this);
    }

//    @Override
//    public void onSwitchLast(@Nullable Song last) {
//        onSongUpdate(last);
//    }
//
//    @Override
//    public void onSwitchNext(@Nullable Song next) {
//        onSongUpdate(next);
//    }
//
//    @Override
//    public void onComplete(@Nullable Song next) {
//        onSongUpdate(next);
//    }
//
//    @Override
//    public void onPlayStatusChanged(boolean isPlaying) {
//        if(mMusicHolder != null && mMusicHolder.ivMusicCtrl != null){
//            if (isPlaying) {
//                Song playing = mPlayer.getPlayingSong();
//                if(mMusicInfo != null && playing.getPath().equals(mMusicInfo.getPath())){
//                    mHandler.removeCallbacks(mProgressCallback);
//                    mHandler.post(mProgressCallback);
//                    mMusicHolder.ivMusicCtrl.setImageResource(R.drawable.btn_doc_video_stop);
//                }
//            } else {
//                mMusicHolder.ivMusicCtrl.setImageResource(R.drawable.btn_doc_video_play);
//                mHandler.removeCallbacks(mProgressCallback);
//            }
//        }
//    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_CREATOR:
                return new CreatorHolder(mLayoutInflater.inflate(R.layout.item_new_doc_creator, parent, false));
            case TYPE_TEXT:
                return new TextHolder(mLayoutInflater.inflate(R.layout.item_new_doc_text, parent, false));
            case TYPE_IMAGE:
                return new ImageHolder(mLayoutInflater.inflate(R.layout.item_new_doc_image, parent, false));
            case TYPE_MUSIC:
                return new MusicHolder(mLayoutInflater.inflate(R.layout.item_new_doc_music, parent, false));
            case TYPE_LINK:
                return new LinkHolder(mLayoutInflater.inflate(R.layout.item_new_doc_music, parent, false));
            case TYPE_CHAPTER:
                return new ChapterHolder(mLayoutInflater.inflate(R.layout.item_new_doc_label_root, parent, false));
            case TYPE_LABEL:
                return new LabelHolder(mLayoutInflater.inflate(R.layout.item_new_doc_label, parent, false));
            case TYPE_COMMENT:
                return new CommentHolder(mLayoutInflater.inflate(R.layout.item_new_comment, parent, false));
            case TYPE_COIN:
                return new CoinHideViewHolder(mLayoutInflater.inflate(R.layout.item_new_doc_hide_top, parent, false));
            case TYPE_COIN_TEXT:
                return new HideTextHolder(mLayoutInflater.inflate(R.layout.item_new_doc_hide_text, parent, false));
            case TYPE_COIN_IMAGE:
                return new HideImageHolder(mLayoutInflater.inflate(R.layout.item_new_doc_hide_image, parent, false));
            case TYPE_FLOOR:
                return new FloorHolder((mLayoutInflater.inflate(R.layout.item_dynamic_coin, parent, false)));
            case TYPE_FOLDER:
                return new BagFavoriteHolder(mLayoutInflater.inflate(R.layout.item_feed_type_2_v3, parent, false));
            case TYPE_TITLE:
                return new TextHolder(mLayoutInflater.inflate(R.layout.item_new_doc_text, parent, false));
            case TYPE_USERLIKE:
                return new LikeHolder(mLayoutInflater.inflate(R.layout.item_menber, parent, false));
            default:
                return new EmptyViewHolder(mLayoutInflater.inflate(R.layout.item_empty, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(view, position);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemLongClick(view, position);
                }
                return false;
            }
        });
        if (holder instanceof CreatorHolder) {
            CreatorHolder creatorHolder = (CreatorHolder) holder;
            createCreator(creatorHolder, position);
        } else if (holder instanceof TextHolder) {
            TextHolder textHolder = (TextHolder) holder;
            createText(textHolder, position);
        } else if (holder instanceof ImageHolder) {
            ImageHolder imageHolder = (ImageHolder) holder;
            createImage(imageHolder, position, 36);
        } else if (holder instanceof MusicHolder) {
            mMusicHolder = (MusicHolder) holder;
            createMusic(position);
        } else if (holder instanceof LinkHolder) {
            LinkHolder linkHolder = (LinkHolder) holder;
            createLink(linkHolder, position);
        } else if (holder instanceof ChapterHolder) {
            ChapterHolder chapterHolder = (ChapterHolder) holder;
            createChapter(chapterHolder, position);
        } else if (holder instanceof LabelHolder) {
            mLabelHolder = (LabelHolder) holder;
            createLabel();
        } else if (holder instanceof CommentHolder) {
            CommentHolder commentHolder = (CommentHolder) holder;
            createComment(commentHolder, position);
        } else if (holder instanceof HideTextHolder) {
            HideTextHolder textHolder = (HideTextHolder) holder;
            createHideText(textHolder, position);
            HideTextHolder hideTextHolder = (HideTextHolder) holder;
            if (position == mDocBean.getDetails().size() + mDocBean.getCoinDetails().size() + 2) {
                hideTextHolder.mRoot.setBackgroundResource(R.drawable.shape_dash_foot);
            } else {
                hideTextHolder.mRoot.setBackgroundResource(R.drawable.shape_dash_mid);
            }
        } else if (holder instanceof HideImageHolder) {
            HideImageHolder imageHolder = (HideImageHolder) holder;
            createHideImage(imageHolder, position, 72);
            HideImageHolder hideImageHolder = (HideImageHolder) holder;
            if (position == mDocBean.getDetails().size() + mDocBean.getCoinDetails().size() + 2) {
                hideImageHolder.mRoot.setBackgroundResource(R.drawable.shape_dash_foot);
            } else {
                hideImageHolder.mRoot.setBackgroundResource(R.drawable.shape_dash_mid);
            }
        } else if (holder instanceof CoinHideViewHolder) {
            CoinHideViewHolder hideViewHolder = (CoinHideViewHolder) holder;
            if (mDocBean.getCoinDetails().size() > 0) {
                hideViewHolder.llHide.setVisibility(View.GONE);
                hideViewHolder.llTop.setVisibility(View.VISIBLE);
            } else {
                hideViewHolder.llHide.setVisibility(View.VISIBLE);
                hideViewHolder.llTop.setVisibility(View.GONE);
                if (!mDocBean.isCoinComment()) {
                    ForegroundColorSpan span = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.pink_fb7ba2));
                    String content = "需要消耗 " + mDocBean.getCoin() + "枚节操";
                    String extra = mDocBean.getCoin() + "枚节操";
                    SpannableStringBuilder style = new SpannableStringBuilder(content);
                    style.setSpan(span, content.indexOf(extra), content.indexOf(extra) + extra.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    hideViewHolder.hideText.setText(style);
                    hideViewHolder.lock.setText("(解锁 " + mDocBean.getNowNum() + "/" + mDocBean.getMaxNum() + ")");
                    hideViewHolder.shareRoot.setVisibility(View.VISIBLE);
                    hideViewHolder.shareRoot2.setOnClickListener(new NoDoubleClickListener() {
                        @Override
                        public void onNoDoubleClick(View v) {
                            ((NewDocDetailActivity) mContext).showShareToBuy();
                        }
                    });
                } else {
                    hideViewHolder.shareRoot.setVisibility(View.GONE);
                    hideViewHolder.hideText.setText(mContext.getString(R.string.label_reply_show));
                }
            }
            hideViewHolder.llHide.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    if (!mDocBean.isCoinComment()) {
                        final AlertDialogUtil alertDialogUtil = AlertDialogUtil.getInstance();
                        alertDialogUtil.createGetHideDialog(mContext, mDocBean.getCoin());
                        alertDialogUtil.setOnClickListener(new AlertDialogUtil.OnClickListener() {
                            @Override
                            public void CancelOnClick() {
                                alertDialogUtil.dismissDialog();
                            }

                            @Override
                            public void ConfirmOnClick() {
                                getCoinContent();
                                alertDialogUtil.dismissDialog();
                            }
                        });
                        alertDialogUtil.showDialog();
                    } else {
                        ((NewDocDetailActivity) mContext).replyNormal();
                    }
                }
            });
        } else if (holder instanceof FloorHolder) {
            FloorHolder floorHolder = (FloorHolder) holder;
            createFloor(floorHolder);
        } else if (holder instanceof BagFavoriteHolder) {
            BagFavoriteHolder bagFavoriteHolder = (BagFavoriteHolder) holder;
            createFolderItem(bagFavoriteHolder, position);
        } else if (holder instanceof LikeHolder) {
            LikeHolder likeHolder = (LikeHolder) holder;
            createLike(likeHolder, position);
        }
    }

    private void setMusicInfo(Music musicInfo) {
        this.mMusicInfo = musicInfo;
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (mDocBean != null) {
            size = mDocBean.getDetails().size() + mComments.size() + 4;
            if (mDocBean.getCoin() > 0) {
                size += mDocBean.getCoinDetails().size() + 1;
            }
            if (mDocBean.getFolderInfo() != null && !TextUtils.isEmpty(mDocBean.getFolderInfo().getFolderId())) {
                size++;
            }
        }
        return size;
    }

    public ArrayList<DocDetailNormalEntity> getmComments() {
        return mComments;
    }

    public Object getItem(int position) {
        if (position == 0) {
            return "";
        } else if (position == 1) {
            return mDocBean.getTitle();
        } else if (position < mDocBean.getDetails().size() + 2) {
            return mDocBean.getDetails().get(position - 2).getTrueData();
        } else if (position == mDocBean.getDetails().size() + 2) {
            if (mDocBean.getCoin() > 0) {
                return "";
            } else if (mDocBean.getFolderInfo() != null && !TextUtils.isEmpty(mDocBean.getFolderInfo().getFolderId())) {
                return mDocBean.getFolderInfo();
            } else {
                return mDocBean.getTags();
            }
        } else if (mDocBean.getCoinDetails().size() > 0 && position > mDocBean.getDetails().size() + 2 && position < mDocBean.getDetails().size() + mDocBean.getCoinDetails().size() + 3) {
            return mDocBean.getCoinDetails().get(position - 3 - mDocBean.getDetails().size()).getTrueData();
        } else if (mDocBean.getCoin() > 0 && position == mDocBean.getDetails().size() + mDocBean.getCoinDetails().size() + 3) {
            if (mDocBean.getFolderInfo() != null && !TextUtils.isEmpty(mDocBean.getFolderInfo().getFolderId())) {
                return mDocBean.getFolderInfo();
            } else {
                return mDocBean.getTags();
            }
        } else if (position == mTagsPosition) {
            return mDocBean.getTags();
        } else if (position == mTagsPosition + 1) {
            return "";
        } else {
//            if (mDocBean.getCoin() > 0 && mDocBean.getFolderInfo() != null && !TextUtils.isEmpty(mDocBean.getFolderInfo().getFolderId())) {
//                return mComments.get(position - mDocBean.getDetails().size() - 6 - mDocBean.getCoinDetails().size());
//            } else if (mDocBean.getCoin() > 0) {
//                return mComments.get(position - mDocBean.getDetails().size() - 5 - mDocBean.getCoinDetails().size());
//            } else if (mDocBean.getFolderInfo() != null && !TextUtils.isEmpty(mDocBean.getFolderInfo().getFolderId())) {
//                return mComments.get(position - mDocBean.getDetails().size() - 5);
//            } else {
//                return mComments.get(position - mDocBean.getDetails().size() - 4);
//            }
            if (mDocBean.getCoin() > 0 && mDocBean.getFolderInfo() != null && !TextUtils.isEmpty(mDocBean.getFolderInfo().getFolderId())) {
                return mComments.get(position - mDocBean.getDetails().size() - 6 - mDocBean.getCoinDetails().size());
            } else if (mDocBean.getCoin() > 0) {
                return mComments.get(position - mDocBean.getDetails().size() - 5 - mDocBean.getCoinDetails().size());
            } else if (mDocBean.getFolderInfo() != null && !TextUtils.isEmpty(mDocBean.getFolderInfo().getFolderId())) {
                return mComments.get(position - mDocBean.getDetails().size() - 5);
            } else {
                return mComments.get(position - mDocBean.getDetails().size() - 4);
            }
        }
    }

    public void setTags(ArrayList<DocTagEntity> entities) {
        mDocBean.setTags(entities);
        notifyItemChanged(mTagsPosition);
    }

    public void setData(DocDetailEntity docBean) {
//        this.mComments.clear();
        this.mComments.clear();
        this.mDocBean = docBean;
        if (mDocBean != null) {
            if (mDocBean.getCoin() > 0 && mDocBean.getFolderInfo() != null && !TextUtils.isEmpty(mDocBean.getFolderInfo().getFolderId())) {
                mTagsPosition = mDocBean.getDetails().size() + mDocBean.getCoinDetails().size() + 4;
            } else if (mDocBean.getCoin() > 0) {
                mTagsPosition = mDocBean.getDetails().size() + mDocBean.getCoinDetails().size() + 3;
            } else if (mDocBean.getFolderInfo() != null && !TextUtils.isEmpty(mDocBean.getFolderInfo().getFolderId())) {
                mTagsPosition = mDocBean.getDetails().size() + 3;
            } else {
                mTagsPosition = mDocBean.getDetails().size() + 2;
            }
        }
        notifyDataSetChanged();
    }

    public void setComment(ArrayList<DocDetailNormalEntity> beans) {
        if (beans.size() > 0) {
            int bgSize = getItemCount() - mComments.size();
            int bfSize = mComments.size();
            this.mComments.clear();
            mComments.addAll(beans);
            int afSize = mComments.size();
            int btSize = afSize - bfSize;
            // notifyItemChanged(mTagsPosition + 1);
            if (btSize > 0) {
                notifyItemRangeChanged(bgSize, bfSize);
                notifyItemRangeInserted(bgSize + bfSize, btSize);
            } else {
                notifyItemRangeChanged(bgSize, afSize);
                notifyItemRangeRemoved(bgSize + afSize, -btSize);
            }
        } else {
            int bgSize = getItemCount() - mComments.size();
            int bfSize = mComments.size();
            this.mComments.clear();
            // notifyItemChanged(mTagsPosition + 1);
            notifyItemRangeRemoved(bgSize, bfSize);
        }
    }

    public void addComment(ArrayList<DocDetailNormalEntity> beans) {
        int bgSize = getItemCount();
        int bfSize = mComments.size();
        this.mComments.addAll(beans);
        int afSize = mComments.size();
        int btSize = afSize - bfSize;
        // notifyItemChanged(mTagsPosition + 1);
        notifyItemRangeInserted(bgSize, btSize);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_CREATOR;
        } else if (position == 1) {
            return TYPE_TITLE;
        } else if (position < mDocBean.getDetails().size() + 2) {
            String type = mDocBean.getDetails().get(position - 2).getType();
            return NewDocType.getType(type);
        } else if (position == mDocBean.getDetails().size() + 2) {
            if (mDocBean.getCoin() > 0) {
                return TYPE_COIN;
            } else if (mDocBean.getFolderInfo() != null && !TextUtils.isEmpty(mDocBean.getFolderInfo().getFolderId())) {
                return TYPE_FOLDER;
            } else {
                return TYPE_LABEL;
            }
        } else if (mDocBean.getCoinDetails().size() > 0 && position > mDocBean.getDetails().size() + 2 && position < mDocBean.getDetails().size() + mDocBean.getCoinDetails().size() + 3) {
            String type = mDocBean.getCoinDetails().get(position - 3 - mDocBean.getDetails().size()).getType();
            return NewDocType.getType(type) + 8;
        } else if (mDocBean.getCoin() > 0 && position == mDocBean.getDetails().size() + mDocBean.getCoinDetails().size() + 3) {
            if (mDocBean.getFolderInfo() != null && !TextUtils.isEmpty(mDocBean.getFolderInfo().getFolderId())) {
                return TYPE_FOLDER;
            } else {
                return TYPE_LABEL;
            }
        } else if (position == mTagsPosition) {
            return TYPE_LABEL;
        } else if (position == mTagsPosition + 1) {
            return TYPE_FLOOR;
        } else {
            DocDetailNormalEntity entity = (DocDetailNormalEntity) getItem(position);
            if (entity.getType() == TYPE_USERLIKE) {
                return TYPE_USERLIKE;
            }
            return TYPE_COMMENT;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            if (!isFirstClick && mMusicHolder != null) {
                if (Math.abs(progress - mLastProgress) >= DateUtils.SECOND_IN_MILLIS) {
                    mMusicHolder.tvMusicTime.setText(StringUtils.getMinute(progress) + "/" + StringUtils.getMinute(mMusicHolder.sbMusicTime.getMax()));
                    mLastProgress = progress;
                }
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        //mHandler.removeCallbacks(mProgressCallback);
        if (!isFirstClick && mMusicHolder != null) {
            isDraggingProgress = true;
        }

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
//        seekTo(getDuration(seekBar.getProgress()));
//        if (mPlayer.isPlaying()) {
//            mHandler.removeCallbacks(mProgressCallback);
//            mHandler.post(mProgressCallback);
//        }
        if (!isFirstClick && mMusicHolder != null) {
            isDraggingProgress = false;
            if (AudioPlayer.get().isPlaying() || AudioPlayer.get().isPausing()) {
                int progress = seekBar.getProgress();
                AudioPlayer.get().seekTo(progress);
            } else {
                seekBar.setProgress(0);
            }
        }

    }

    @Override
    public void onChange(Music music) {
        if (!isFirstClick && mMusicHolder != null) {
            mLastProgress = 0;
        }

    }

    @Override
    public void onPlayerStart() {
        if (!isFirstClick && mMusicHolder != null) {
            mMusicHolder.ivMusicCtrl.setImageResource(R.drawable.btn_doc_video_stop);
        }
    }

    @Override
    public void onPlayerPause() {
        if (!isFirstClick && mMusicHolder != null) {
            mMusicHolder.ivMusicCtrl.setImageResource(R.drawable.btn_doc_video_play);
        }
    }

    @Override
    public void onPublish(int progress) {
        if (!isFirstClick && mMusicHolder != null) {
            if (!isDraggingProgress) {
                mMusicHolder.sbMusicTime.setProgress(progress);
            }
        }
    }

    @Override
    public void onBufferingUpdate(int percent) {
        if (percent != 0) {
            if (!isFirstClick && mMusicHolder != null) {
                mMusicHolder.sbMusicTime.setSecondaryProgress(mMusicHolder.sbMusicTime.getMax() * 100 / percent);
            }
        }
    }

    private static class CreatorHolder extends RecyclerView.ViewHolder {

        ImageView mIvCreator;
        ImageView mIvVip;
        ImageView mIvSex;
        TextView mTvCreator;
        TextView tvLevel;
        TextView mTvTime;
        TextView mFollow;
        View rlHuiZhang1;
        TextView tvHuiZhang1;
        View[] huiZhangRoots;
        TextView[] huiZhangTexts;

        CreatorHolder(View itemView) {
            super(itemView);
            mIvCreator = itemView.findViewById(R.id.iv_avatar);
            mIvVip = itemView.findViewById(R.id.iv_vip);
            mIvSex = itemView.findViewById(R.id.iv_sex);
            mTvCreator = itemView.findViewById(R.id.tv_name);
            mTvTime = itemView.findViewById(R.id.tv_time);
            tvLevel = itemView.findViewById(R.id.tv_level);
            tvHuiZhang1 = itemView.findViewById(R.id.tv_huizhang_1);
            mFollow = itemView.findViewById(R.id.tv_follow);
            rlHuiZhang1 = itemView.findViewById(R.id.fl_huizhang_1);
            huiZhangRoots = new View[]{rlHuiZhang1};
            huiZhangTexts = new TextView[]{tvHuiZhang1};
        }
    }

    public void followUserSuccess(boolean isFollow) {
        mDocBean.setFollowUser(isFollow);
        notifyItemChanged(0);
    }

    private void createCreator(final CreatorHolder holder, int position) {
        int size = (int) mContext.getResources().getDimension(R.dimen.x80);
        Glide.with(mContext)
                .load(StringUtils.getUrl(mContext, ApiService.URL_QINIU + mDocBean.getUserIcon(), size, size, false, true))
                .error(R.drawable.bg_default_circle)
                .placeholder(R.drawable.bg_default_circle)
                .bitmapTransform(new CropCircleTransformation(mContext))
                .into(holder.mIvCreator);
        if (mDocBean.getUserId().equals(PreferenceUtils.getUUid())) {
            holder.mFollow.setVisibility(View.GONE);
        } else {
            holder.mFollow.setVisibility(View.VISIBLE);
        }
        holder.mFollow.setSelected(mDocBean.isFollowUser());
        holder.mFollow.setText(mDocBean.isFollowUser() ? mContext.getString(R.string.label_followed) : mContext.getString(R.string.label_follow));
        holder.mFollow.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                ((NewDocDetailActivity) mContext).followUser(mDocBean.getUserId(), mDocBean.isFollowUser());
            }
        });

        if (mDocBean.isVip()) {
            holder.mIvVip.setVisibility(View.VISIBLE);
        } else {
            holder.mIvVip.setVisibility(View.GONE);
        }
        holder.mIvSex.setImageResource(mDocBean.getUserSex().equalsIgnoreCase("M") ? R.drawable.ic_user_girl : R.drawable.ic_user_boy);
        holder.mTvCreator.setText(mDocBean.getUserName());

//        if ("USER".equals(mDocBean.getFrom())) {
//            String timeShow = "关注人 · " + StringUtils.timeFormat(mDocBean.getCreateTime());
//            setText(R.id.tv_time, timeShow);
//        } else {
        if (!TextUtils.isEmpty(mDocBean.getTagName())) {
            String temp = "社团 " + mDocBean.getTagName() + " · " + StringUtils.timeFormat(mDocBean.getCreateTime());
            ForegroundColorSpan span = new ForegroundColorSpan(ContextCompat.getColor(mContext, TagUtils.getColor(mDocBean.getTagName())));
            SpannableStringBuilder style = new SpannableStringBuilder(temp);
            style.setSpan(span, temp.indexOf(mDocBean.getTagName()), temp.indexOf(mDocBean.getTagName()) + mDocBean.getTagName().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                setText(R.id.tv_time, style);
            holder.mTvTime.setText(style);
        } else {
            if (mDocBean.isManager()) {
                String manager = "部长 · " + StringUtils.timeFormat(mDocBean.getCreateTime());
                ForegroundColorSpan span = new ForegroundColorSpan(ContextCompat.getColor(mContext, TagUtils.getColor(mDocBean.getTagName())));
                SpannableStringBuilder style = new SpannableStringBuilder(manager);
                style.setSpan(span, manager.indexOf("部长"), manager.indexOf("部长") + ("部长").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.mTvTime.setText(style);
            } else {
                holder.mTvTime.setText(StringUtils.timeFormat(mDocBean.getCreateTime()));
            }
        }
//        }


//        holder.mTvTime.setText(StringUtils.timeFormat(mDocBean.getCreateTime()));
        holder.mTvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CommunityV1Activity.class);
                String id = mDocBean.getTagId();
                String text = mDocBean.getTagName();
                intent.putExtra("uuid", id + "");
                intent.putExtra("title", text + "");
                mContext.startActivity(intent);
            }
        });
        LevelSpan levelSpan = new LevelSpan(ContextCompat.getColor(mContext, R.color.white), mContext.getResources().getDimension(R.dimen.x12));
        String content = "LV" + mDocBean.getUserLevel();
        String colorStr = "LV";
        SpannableStringBuilder style = new SpannableStringBuilder(content);
        style.setSpan(levelSpan, content.indexOf(colorStr), content.indexOf(colorStr) + colorStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tvLevel.setText(style);
        float radius2 = mContext.getResources().getDimension(R.dimen.y4);
        float[] outerR2 = new float[]{radius2, radius2, radius2, radius2, radius2, radius2, radius2, radius2};
        RoundRectShape roundRectShape2 = new RoundRectShape(outerR2, null, null);
        ShapeDrawable shapeDrawable2 = new ShapeDrawable();
        shapeDrawable2.setShape(roundRectShape2);
        shapeDrawable2.getPaint().setStyle(Paint.Style.FILL);
        shapeDrawable2.getPaint().setColor(StringUtils.readColorStr(mDocBean.getUserLevelColor(), ContextCompat.getColor(mContext, R.color.main_cyan)));
        holder.tvLevel.setBackgroundDrawable(shapeDrawable2);
        holder.mIvCreator.setTag(R.id.id_creator_uuid, mDocBean.getUserId());
        holder.mIvCreator.setOnClickListener(mAvatarListener);
        ViewUtils.badge(mContext, holder.huiZhangRoots, holder.huiZhangTexts, mDocBean.getBadgeList());
    }

    private static class TextHolder extends RecyclerView.ViewHolder {

        TextView mTvText;

        TextHolder(View itemView) {
            super(itemView);
            mTvText = itemView.findViewById(R.id.tv_doc_content);
        }
    }

    private void createText(final TextHolder holder, int position) {
        String content = (String) getItem(position);
        if (position == 1 && !TextUtils.isEmpty(content)) {
            holder.mTvText.setTextColor(ContextCompat.getColor(mContext, R.color.black_1e1e1e));
            holder.mTvText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            holder.mTvText.getPaint().setFakeBoldText(true);
            holder.mTvText.setText(mDocBean.getTitle());
        } else if (!TextUtils.isEmpty(content)) {
            holder.mTvText.setTextColor(ContextCompat.getColor(mContext, R.color.gray_444444));
            holder.mTvText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
            holder.mTvText.getPaint().setFakeBoldText(false);
            holder.mTvText.setText(StringUtils.getUrlClickableText(mContext, StringUtils.buildAtUserToShow(mContext, (String) getItem(position))));
            holder.mTvText.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            holder.mTvText.setVisibility(View.GONE);
        }
    }

    private static class HideTextHolder extends RecyclerView.ViewHolder {
        private LinearLayout mRoot;
        private TextView mTvText;

        HideTextHolder(View itemView) {
            super(itemView);
            mRoot = itemView.findViewById(R.id.ll_root);
            mTvText = itemView.findViewById(R.id.tv_doc_content);
        }
    }

    private void createHideText(final HideTextHolder holder, int position) {
        holder.mTvText.setText(StringUtils.getUrlClickableText(mContext, StringUtils.buildAtUserToShow(mContext, (String) getItem(position))));
        holder.mTvText.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private static class ImageHolder extends RecyclerView.ViewHolder {
        ImageView mIvImage;
        LongImageView mIvLongImage;

        ImageHolder(View itemView) {
            super(itemView);
            mIvImage = itemView.findViewById(R.id.iv_doc_image);
            mIvLongImage = itemView.findViewById(R.id.iv_doc_long_image);
        }
    }

    private void createImage(final ImageHolder holder, final int position, int size) {
        final Image image = (Image) getItem(position);
        if (image.getW() <= 0 || image.getH() <= 0) {
            holder.mIvImage.setVisibility(View.GONE);
            holder.mIvLongImage.setVisibility(View.GONE);
        } else {
            final int[] wh = BitmapUtils.getDocIconSizeFromW(image.getW() * 2, image.getH() * 2, DensityUtil.getScreenWidth(mContext) - (int) mContext.getResources().getDimension(R.dimen.x72));
            if (((float) wh[1] / wh[0]) > 16.0f / 9.0f) {
                holder.mIvImage.setVisibility(View.GONE);
                holder.mIvLongImage.setVisibility(View.VISIBLE);
                String temp = EncoderUtils.MD5(ApiService.URL_QINIU + image.getPath()) + ".jpg";
                final File longImage = new File(StorageUtils.getGalleryDirPath(), temp);
                ViewGroup.LayoutParams layoutParams = holder.mIvLongImage.getLayoutParams();
                layoutParams.width = wh[0];
                layoutParams.height = wh[1];
                holder.mIvLongImage.setLayoutParams(layoutParams);
                if (longImage.exists()) {
                    holder.mIvLongImage.setImage(longImage.getAbsolutePath());
                } else {
                    FileDownloader.getImpl().create(ApiService.URL_QINIU + image.getPath())
                            .setPath(StorageUtils.getGalleryDirPath() + temp)
                            .setCallbackProgressTimes(1)
                            .setListener(new FileDownloadListener() {
                                @Override
                                protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                                }

                                @Override
                                protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                                }

                                @Override
                                protected void completed(BaseDownloadTask task) {
                                    BitmapUtils.galleryAddPic(mContext, longImage.getAbsolutePath());
                                    holder.mIvLongImage.setImage(longImage.getAbsolutePath());
                                    notifyItemChanged(position);
                                }

                                @Override
                                protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                                }

                                @Override
                                protected void error(BaseDownloadTask task, Throwable e) {

                                }

                                @Override
                                protected void warn(BaseDownloadTask task) {

                                }
                            }).start();
                }
            } else {
                holder.mIvImage.setVisibility(View.VISIBLE);
                holder.mIvLongImage.setVisibility(View.GONE);
                if (FileUtil.isGif(image.getPath())) {
                    ViewGroup.LayoutParams layoutParams = holder.mIvImage.getLayoutParams();
                    layoutParams.width = wh[0];
                    layoutParams.height = wh[1];
                    holder.mIvImage.setLayoutParams(layoutParams);
                    Glide.with(mContext)
                            .load(ApiService.URL_QINIU + image.getPath())
                            .asGif()
                            .override(wh[0], wh[1])
                            .dontAnimate()
                            .placeholder(R.drawable.shape_gray_e8e8e8_background)
                            .error(R.drawable.shape_gray_e8e8e8_background)
                            .into(holder.mIvImage);
                } else {
                    ViewGroup.LayoutParams layoutParams = holder.mIvImage.getLayoutParams();
                    layoutParams.width = wh[0];
                    layoutParams.height = wh[1];
                    holder.mIvImage.setLayoutParams(layoutParams);
                    Glide.with(mContext)
                            .load(StringUtils.getUrl(mContext, ApiService.URL_QINIU + image.getPath(), wh[0], wh[1], true, true))
                            .override(wh[0], wh[1])
                            .dontAnimate()
                            .placeholder(R.drawable.shape_gray_e8e8e8_background)
                            .error(R.drawable.shape_gray_e8e8e8_background)
                            .into(holder.mIvImage);
                }
            }
        }
    }

    private static class HideImageHolder extends RecyclerView.ViewHolder {
        private LinearLayout mRoot;
        private ImageView mIvImage;
        private LongImageView mIvLongImage;

        HideImageHolder(View itemView) {
            super(itemView);
            mRoot = itemView.findViewById(R.id.ll_root);
            mIvImage = itemView.findViewById(R.id.iv_doc_image);
            mIvLongImage = itemView.findViewById(R.id.iv_doc_long_image);
        }
    }

    private void createHideImage(final HideImageHolder holder, final int position, int size) {
        final Image image = (Image) getItem(position);
        if (image.getW() <= 0 || image.getH() <= 0) {
            holder.mIvImage.setVisibility(View.GONE);
            holder.mIvLongImage.setVisibility(View.GONE);
        } else {
            final int[] wh = BitmapUtils.getDocIconSizeFromW(image.getW() * 2, image.getH() * 2, DensityUtil.getScreenWidth(mContext) - (int) mContext.getResources().getDimension(R.dimen.x144));
            if (wh[1] > 4000) {
                holder.mIvImage.setVisibility(View.GONE);
                holder.mIvLongImage.setVisibility(View.VISIBLE);
                String temp = EncoderUtils.MD5(ApiService.URL_QINIU + image.getPath()) + ".jpg";
                final File longImage = new File(StorageUtils.getGalleryDirPath(), temp);
                ViewGroup.LayoutParams layoutParams = holder.mIvLongImage.getLayoutParams();
                layoutParams.width = wh[0];
                layoutParams.height = wh[1];
                holder.mIvLongImage.setLayoutParams(layoutParams);
                if (longImage.exists()) {
                    holder.mIvLongImage.setImage(longImage.getAbsolutePath());
                } else {
                    FileDownloader.getImpl().create(ApiService.URL_QINIU + image.getPath())
                            .setPath(StorageUtils.getGalleryDirPath() + temp)
                            .setCallbackProgressTimes(1)
                            .setListener(new FileDownloadListener() {
                                @Override
                                protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                                }

                                @Override
                                protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                                }

                                @Override
                                protected void completed(BaseDownloadTask task) {
                                    BitmapUtils.galleryAddPic(mContext, longImage.getAbsolutePath());
                                    holder.mIvLongImage.setImage(longImage.getAbsolutePath());
                                    notifyItemChanged(position);
                                }

                                @Override
                                protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                                }

                                @Override
                                protected void error(BaseDownloadTask task, Throwable e) {

                                }

                                @Override
                                protected void warn(BaseDownloadTask task) {

                                }
                            }).start();
                }
            } else {
                if (FileUtil.isGif(image.getPath())) {
                    ViewGroup.LayoutParams layoutParams = holder.mIvImage.getLayoutParams();
                    layoutParams.width = wh[0];
                    layoutParams.height = wh[1];
                    holder.mIvImage.setLayoutParams(layoutParams);
                    Glide.with(mContext)
                            .load(ApiService.URL_QINIU + image.getPath())
                            .asGif()
                            .override(wh[0], wh[1])
                            .dontAnimate()
                            .placeholder(R.drawable.shape_gray_e8e8e8_background)
                            .error(R.drawable.shape_gray_e8e8e8_background)
                            .into(holder.mIvImage);
                } else {
                    ViewGroup.LayoutParams layoutParams = holder.mIvImage.getLayoutParams();
                    layoutParams.width = wh[0];
                    layoutParams.height = wh[1];
                    holder.mIvImage.setLayoutParams(layoutParams);
                    Glide.with(mContext)
                            .load(StringUtils.getUrl(mContext, ApiService.URL_QINIU + image.getPath(), wh[0], wh[1], true, true))
                            .override(wh[0], wh[1])
                            .dontAnimate()
                            .placeholder(R.drawable.shape_gray_e8e8e8_background)
                            .error(R.drawable.shape_gray_e8e8e8_background)
                            .into(holder.mIvImage);
                }
            }
        }
    }

    private static class MusicHolder extends RecyclerView.ViewHolder {
        ImageView mIvImage;
        LongImageView mIvLongImage;
        ImageView ivMusicCtrl;
        TextView tvMusicTitle;
        TextView tvMusicTime;
        SeekBar sbMusicTime;
        View musicRoot;

        MusicHolder(View itemView) {
            super(itemView);
            ivMusicCtrl = itemView.findViewById(R.id.iv_music_ctrl);
            tvMusicTitle = itemView.findViewById(R.id.tv_music_name);
            tvMusicTime = itemView.findViewById(R.id.tv_music_seek);
            sbMusicTime = itemView.findViewById(R.id.sb_music);
            musicRoot = itemView.findViewById(R.id.rl_music_root);
            mIvImage = itemView.findViewById(R.id.iv_doc_image);
            mIvLongImage = itemView.findViewById(R.id.iv_doc_long_image);
        }
    }

    private void createMusic(final int position) {
        Object o = getItem(position);
        if (o instanceof DocDetailEntity.DocMusic) {
            DocDetailEntity.DocMusic music = (DocDetailEntity.DocMusic) o;
            mMusicHolder.tvMusicTitle.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            mMusicHolder.tvMusicTime.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            mMusicHolder.musicRoot.setBackgroundResource(R.drawable.bg_rect_gray_doc_music);
            mMusicHolder.tvMusicTitle.setText(music.getName());
            mMusicHolder.ivMusicCtrl.setOnClickListener(musicCtrl);
            mMusicHolder.sbMusicTime.setOnSeekBarChangeListener(this);
            mMusicHolder.ivMusicCtrl.setImageResource(R.drawable.btn_doc_video_play);

//            if(mPlayer.isPlaying()){
//                Song musicInfo = mPlayer.getPlayingSong();
//                if(musicInfo.getPath().equals(music.getUrl())){
//                    musicInfo.setDuration(music.getTimestamp());
//                    mMusicHolder.ivMusicCtrl.setImageResource(R.drawable.btn_doc_video_stop);
//                }else {
//                    mMusicHolder.ivMusicCtrl.setImageResource(R.drawable.btn_doc_video_play);
//                }
//            }else {
//                mMusicHolder.ivMusicCtrl.setImageResource(R.drawable.btn_doc_video_play);
//            }

//            Song musicInfo = new Song();
//            musicInfo.setCoverPath(music.getCover().getPath());
//            musicInfo.setPath(music.getUrl());
//            musicInfo.setDisplayName(music.getName());
//            musicInfo.setDuration(music.getTimestamp());

            Music musicInfo = new Music();
            musicInfo.setCoverPath(music.getCover().getPath());
            musicInfo.setPath(music.getUrl());
            musicInfo.setTitle(music.getName());
            musicInfo.setFileName(music.getName());
            musicInfo.setDuration(music.getTimestamp());

            setMusicInfo(musicInfo);
            mMusicHolder.sbMusicTime.setMax(music.getTimestamp());

            //  Song curMusic = mPlayer.getPlayingSong();
            mMusicHolder.tvMusicTime.setText(StringUtils.getMinute(0) + "/" + StringUtils.getMinute(music.getTimestamp()));
//            if(curMusic != null){
//                if(curMusic.getPath().equals(musicInfo.getPath())){
//                    onSongUpdate(musicInfo);
//                } 
//            }
            final Image image = music.getCover();
            if (image.getW() <= 0 || image.getH() <= 0) {
                mMusicHolder.mIvImage.setVisibility(View.GONE);
                mMusicHolder.mIvLongImage.setVisibility(View.GONE);
            } else {
                final int[] wh = BitmapUtils.getDocIconSizeFromW(image.getW(), image.getH(), DensityUtil.getScreenWidth(mContext) - (int) mContext.getResources().getDimension(R.dimen.x40));
                if (wh[1] > 4000) {
                    mMusicHolder.mIvImage.setVisibility(View.GONE);
                    mMusicHolder.mIvLongImage.setVisibility(View.VISIBLE);
                    String temp = EncoderUtils.MD5(ApiService.URL_QINIU + image.getPath()) + ".jpg";
                    final File longImage = new File(StorageUtils.getGalleryDirPath(), temp);
                    ViewGroup.LayoutParams layoutParams = mMusicHolder.mIvLongImage.getLayoutParams();
                    layoutParams.width = wh[0];
                    layoutParams.height = wh[1];
                    mMusicHolder.mIvLongImage.setLayoutParams(layoutParams);
                    if (longImage.exists()) {
                        mMusicHolder.mIvLongImage.setImage(longImage.getAbsolutePath());
                    } else {
                        FileDownloader.getImpl().create(ApiService.URL_QINIU + image.getPath())
                                .setPath(StorageUtils.getGalleryDirPath() + temp)
                                .setCallbackProgressTimes(1)
                                .setListener(new FileDownloadListener() {
                                    @Override
                                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                                    }

                                    @Override
                                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                                    }

                                    @Override
                                    protected void completed(BaseDownloadTask task) {
                                        BitmapUtils.galleryAddPic(mContext, longImage.getAbsolutePath());
                                        mMusicHolder.mIvLongImage.setImage(longImage.getAbsolutePath());
                                        notifyItemChanged(position);
                                    }

                                    @Override
                                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                                    }

                                    @Override
                                    protected void error(BaseDownloadTask task, Throwable e) {

                                    }

                                    @Override
                                    protected void warn(BaseDownloadTask task) {

                                    }
                                }).start();
                    }
                } else {
                    if (FileUtil.isGif(image.getPath())) {
                        ViewGroup.LayoutParams layoutParams = mMusicHolder.mIvImage.getLayoutParams();
                        layoutParams.width = wh[0];
                        layoutParams.height = wh[1];
                        mMusicHolder.mIvImage.setLayoutParams(layoutParams);
                        Glide.with(mContext)
                                .load(ApiService.URL_QINIU + image.getPath())
                                .asGif()
                                .override(wh[0], wh[1])
                                .placeholder(R.drawable.shape_gray_e8e8e8_background)
                                .error(R.drawable.shape_gray_e8e8e8_background)
                                .into(mMusicHolder.mIvImage);
                    } else {
                        ViewGroup.LayoutParams layoutParams = mMusicHolder.mIvImage.getLayoutParams();
                        layoutParams.width = wh[0];
                        layoutParams.height = wh[1];
                        mMusicHolder.mIvImage.setLayoutParams(layoutParams);
                        Glide.with(mContext)
                                .load(StringUtils.getUrl(mContext, ApiService.URL_QINIU + image.getPath(), wh[0], wh[1], true, true))
                                .override(wh[0], wh[1])
                                .placeholder(R.drawable.shape_gray_e8e8e8_background)
                                .error(R.drawable.shape_gray_e8e8e8_background)
                                .into(mMusicHolder.mIvImage);
                    }
                }
            }
        }
    }

//    private void onSongUpdate(@Nullable Song song){
//        if(song == null || mMusicInfo == null || !mMusicInfo.getPath().equals(song.getPath())){
//            mMusicHolder.ivMusicCtrl.setImageResource(R.drawable.btn_doc_video_play);
//            mMusicHolder.sbMusicTime.setProgress(0);
//            updateProgressTextWithProgress(0);
//            seekTo(0);
//            mHandler.removeCallbacks(mProgressCallback);
//            return;
//        }
//        updateProgressTextWithDuration(mPlayer.getProgress());
//        mHandler.removeCallbacks(mProgressCallback);
//        if (mPlayer.isPlaying()) {
//            mHandler.post(mProgressCallback);
//            mMusicHolder.ivMusicCtrl.setImageResource(R.drawable.btn_doc_video_stop);
//        }
//    }

//    private void seekTo(int duration) {
//        mPlayer.seekTo(duration);
//    }

//    private void updateProgressTextWithDuration(int duration) {
//        mMusicHolder.tvMusicTime.setText(StringUtils.getMinute(duration) + "/" + StringUtils.getMinute(mPlayer.getPlayingSong().getDuration()));
//    }
//
//    private int getDuration(int progress) {
//        return (int) (getCurrentSongDuration() * ((float) progress / mMusicHolder.sbMusicTime.getMax()));
//    }
//
//    private void updateProgressTextWithProgress(int progress) {
//        int targetDuration = getDuration(progress);
//        Song playing = mPlayer.getPlayingSong();
//        if(playing != null){
//            mMusicHolder.tvMusicTime.setText(StringUtils.getMinute(targetDuration) + "/" + StringUtils.getMinute(playing.getDuration()));
//        }
//    }
//
//    private int getCurrentSongDuration() {
//        Song currentSong = mPlayer.getPlayingSong();
//        int duration = 0;
//        if (currentSong != null) {
//            duration = currentSong.getDuration();
//        }
//        return duration;
//    }

    private class BagFavoriteHolder extends RecyclerView.ViewHolder {

        ImageView ivCover;
        TextView tvMark;
        TextView tvCoin;
        TextView tvExtra;
        ImageView ivPlay;
        TextView tvTitle;
        ImageView ivAvatar;
        TextView tvUserName;
        TextView tvExtraContent;
        TextView tvTag1;
        TextView tvTag2;
        TextView tvPlayNum;
        TextView tvDanmuNum;
        FrameLayout flCover;

        public BagFavoriteHolder(View itemView) {
            super(itemView);
            itemView.setBackgroundResource(R.drawable.shape_gray_f6f6f6_background_y8);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            layoutParams.setMargins((int) mContext.getResources().getDimension(R.dimen.x24),
                    (int) mContext.getResources().getDimension(R.dimen.x24),
                    (int) mContext.getResources().getDimension(R.dimen.x24),
                    (int) mContext.getResources().getDimension(R.dimen.x24));
            ivCover = itemView.findViewById(R.id.iv_cover);
            tvMark = itemView.findViewById(R.id.tv_mark);
            tvCoin = itemView.findViewById(R.id.tv_coin);
            tvExtra = itemView.findViewById(R.id.tv_extra);
            ivPlay = itemView.findViewById(R.id.iv_play);
            tvTitle = itemView.findViewById(R.id.tv_title);
            ivAvatar = itemView.findViewById(R.id.iv_user_avatar);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            tvExtraContent = itemView.findViewById(R.id.tv_extra_content);
            tvTag1 = itemView.findViewById(R.id.tv_tag_1);
            tvTag2 = itemView.findViewById(R.id.tv_tag_2);
            tvPlayNum = itemView.findViewById(R.id.tv_play_num);
            tvDanmuNum = itemView.findViewById(R.id.tv_danmu_num);
            flCover = itemView.findViewById(R.id.fl_cover_root);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) flCover.getLayoutParams();
            params.setMargins(0,
                    (int) mContext.getResources().getDimension(R.dimen.x16),
                    (int) mContext.getResources().getDimension(R.dimen.x8),
                    (int) mContext.getResources().getDimension(R.dimen.x16));
        }
    }

    private void createFolderItem(BagFavoriteHolder holder, int position) {


        final BagDirEntity item = (BagDirEntity) getItem(position);
        int w = mContext.getResources().getDimensionPixelSize(R.dimen.x222);
        int h = mContext.getResources().getDimensionPixelSize(R.dimen.y160);
        Glide.with(mContext)
                .load(StringUtils.getUrl(mContext, item.getCover(), w, h, false, true))
                .error(R.drawable.shape_gray_e8e8e8_background)
                .placeholder(R.drawable.shape_gray_e8e8e8_background)
                .bitmapTransform(new CropTransformation(mContext, w, h)
                        , new RoundedCornersTransformation(mContext, mContext.getResources().getDimensionPixelSize(R.dimen.y8), 0))
                .into(holder.ivCover);

        holder.tvExtra.setText(item.getNumber() + "项");
        holder.tvExtraContent.setText(StringUtils.timeFormat(item.getUpdateTime()));

        if (item.getFolderType().equals(FolderType.ZH.toString())) {
            holder.tvMark.setVisibility(View.VISIBLE);
            holder.ivPlay.setVisibility(View.GONE);
            holder.tvMark.setText("综合");
            holder.tvMark.setBackgroundResource(R.drawable.shape_rect_zonghe);
        } else if (item.getFolderType().equals(FolderType.TJ.toString())) {
            holder.tvMark.setVisibility(View.VISIBLE);
            holder.ivPlay.setVisibility(View.GONE);
            holder.tvMark.setText("图集");
            holder.tvMark.setBackgroundResource(R.drawable.shape_rect_tuji);
        } else if (item.getFolderType().equals(FolderType.MH.toString())) {
            holder.tvMark.setVisibility(View.VISIBLE);
            holder.ivPlay.setVisibility(View.GONE);
            holder.tvMark.setText("漫画");
            holder.tvMark.setBackgroundResource(R.drawable.shape_rect_manhua);
        } else if (item.getFolderType().equals(FolderType.XS.toString())) {
            holder.tvMark.setVisibility(View.VISIBLE);
            holder.ivPlay.setVisibility(View.GONE);
            holder.tvMark.setText("小说");
            holder.tvMark.setBackgroundResource(R.drawable.shape_rect_xiaoshuo);
        } else if (item.getFolderType().equals(FolderType.WZ.toString())) {
            holder.tvMark.setVisibility(View.VISIBLE);
            holder.ivPlay.setVisibility(View.GONE);
            holder.tvMark.setText("文章");
            holder.tvMark.setBackgroundResource(R.drawable.shape_rect_zonghe);
        } else if (item.getFolderType().equals(FolderType.SP.toString())) {
            holder.tvMark.setVisibility(View.VISIBLE);
            holder.ivPlay.setVisibility(View.GONE);
            holder.tvMark.setText("视频集");
            holder.tvMark.setBackgroundResource(R.drawable.shape_rect_shipin);
        } else if (item.getFolderType().equals(FolderType.YY.toString())) {
            holder.tvMark.setVisibility(View.VISIBLE);
            holder.ivPlay.setVisibility(View.GONE);
            holder.tvMark.setText("音乐集");
            holder.tvMark.setBackgroundResource(R.drawable.shape_rect_yinyue);
        } else {
            holder.tvMark.setVisibility(View.GONE);
        }

        holder.tvTitle.setText(item.getName());

        //tag
        View[] tagsId = {holder.tvTag1, holder.tvTag2};
        holder.tvTag1.setOnClickListener(null);
        holder.tvTag2.setOnClickListener(null);
        if (item.getTexts().size() > 1) {
            holder.tvTag1.setVisibility(View.VISIBLE);
            holder.tvTag2.setVisibility(View.VISIBLE);
        } else if (item.getTexts().size() > 0) {
            holder.tvTag1.setVisibility(View.VISIBLE);
            holder.tvTag2.setVisibility(View.INVISIBLE);
        } else {
            holder.tvTag1.setVisibility(View.INVISIBLE);
            holder.tvTag2.setVisibility(View.INVISIBLE);
        }
        int size = tagsId.length > item.getTexts().size() ? item.getTexts().size() : tagsId.length;
        for (int i = 0; i < size; i++) {
            TagUtils.setBackGround(item.getTexts().get(i).getText(), tagsId[i]);
            tagsId[i].setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    //TODO 跳转标签页
                }
            });
        }

        //user
        int avatarSize = mContext.getResources().getDimensionPixelSize(R.dimen.y32);
        Glide.with(mContext)
                .load(StringUtils.getUrl(mContext, item.getUserIcon(), avatarSize, avatarSize, false, true))
                .error(R.drawable.bg_default_circle)
                .placeholder(R.drawable.bg_default_circle)
                .bitmapTransform(new CropCircleTransformation(mContext))
                .into(holder.ivAvatar);
        holder.tvUserName.setText(item.getUserName());
        holder.ivAvatar.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                ViewUtils.toPersonal(mContext, item.getUserId());
            }
        });
        holder.tvUserName.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                ViewUtils.toPersonal(mContext, item.getUserId());
            }
        });

        if (item.getCoin() > 0) {
            holder.tvCoin.setText(item.getCoin() + "节操");
        } else {
            holder.tvCoin.setText("免费");
        }
    }

    private static class LinkHolder extends RecyclerView.ViewHolder {

        View mCoverRoot;
        ImageView ivMusicCtrl;
        TextView tvMusicTitle;
        TextView tvMusicTime;
        SeekBar sbMusicTime;
        View musicRoot;

        LinkHolder(View itemView) {
            super(itemView);
            ivMusicCtrl = itemView.findViewById(R.id.iv_music_ctrl);
            tvMusicTitle = itemView.findViewById(R.id.tv_music_name);
            tvMusicTime = itemView.findViewById(R.id.tv_music_seek);
            sbMusicTime = itemView.findViewById(R.id.sb_music);
            musicRoot = itemView.findViewById(R.id.rl_music_root);
            mCoverRoot = itemView.findViewById(R.id.rl_image_root);
            mCoverRoot.setVisibility(View.GONE);
        }
    }

    private void createLink(final LinkHolder holder, int position) {
        DocDetailEntity.DocLink bean = (DocDetailEntity.DocLink) getItem(position);
        holder.tvMusicTitle.setTextColor(ContextCompat.getColor(mContext, R.color.black));
        holder.tvMusicTime.setTextColor(ContextCompat.getColor(mContext, R.color.gray_929292));
        holder.musicRoot.setBackgroundResource(R.drawable.bg_rect_gray_doc_link);
        holder.tvMusicTitle.setText(bean.getName());
        holder.tvMusicTime.setText(bean.getUrl());
        holder.sbMusicTime.setVisibility(View.GONE);
        Glide.with(mContext)
                .load(StringUtils.getUrl(mContext, ApiService.URL_QINIU + bean.getIcon().getPath(), (int) mContext.getResources().getDimension(R.dimen.y90), (int) mContext.getResources().getDimension(R.dimen.y90), false, true))
                .override((int) mContext.getResources().getDimension(R.dimen.y90), (int) mContext.getResources().getDimension(R.dimen.y90))
                .placeholder(R.drawable.bg_default_circle)
                .error(R.drawable.bg_default_circle)
                .into(holder.ivMusicCtrl);
    }

    private static class ChapterHolder extends RecyclerView.ViewHolder {

        private DocLabelView mDvLabel;

        ChapterHolder(View itemView) {
            super(itemView);
            mDvLabel = itemView.findViewById(R.id.dv_doc_label_root);
        }
    }

    private void createChapter(ChapterHolder holder, int position) {
        final ArrayList<DocDetailEntity.DocGroupLink.DocGroupLinkDetail> details = ((DocDetailEntity.DocGroupLink) getItem(position)).getItems();
        holder.mDvLabel.setChapter(details, mContext);
        holder.mDvLabel.setItemClickListener(new DocLabelView.LabelItemClickListener() {
            @Override
            public void itemClick(int position) {
                DocDetailEntity.DocGroupLink.DocGroupLinkDetail detail = details.get(position);
                WebViewActivity.startActivity(mContext, detail.getUrl());
            }
        });
    }

    private static class LabelHolder extends RecyclerView.ViewHolder {

        DocLabelView mDvLabel;
        NewDocLabelAdapter docLabelAdapter;
        TextView tvForward;
        TextView tvComment;
        TextView tvTag;
        View fRoot;
        View cRoot;
        View tRoot;
        View RBottomRoot;

        LabelHolder(View itemView) {
            super(itemView);
            mDvLabel = itemView.findViewById(R.id.dv_doc_label_root);
            docLabelAdapter = new NewDocLabelAdapter(itemView.getContext(), false);
            tvForward = itemView.findViewById(R.id.tv_forward_num);
            tvComment = itemView.findViewById(R.id.tv_comment_num);
            tvTag = itemView.findViewById(R.id.tv_tag_num);
            fRoot = itemView.findViewById(R.id.fl_forward_root);
            cRoot = itemView.findViewById(R.id.fl_comment_root);
            tRoot = itemView.findViewById(R.id.fl_tag_root);
            RBottomRoot = itemView.findViewById(R.id.rl_list_bottom_root);
        }
    }

    private void createLabel() {
        mTags.clear();
        mTags.addAll(mDocBean.getTags());
        mLabelHolder.mDvLabel.setDocLabelAdapter(mLabelHolder.docLabelAdapter);
        mLabelHolder.docLabelAdapter.setData(mTags, true);
        mLabelHolder.mDvLabel.setItemClickListener(new DocLabelView.LabelItemClickListener() {
            @Override
            public void itemClick(int position) {
                addlabel(position);
            }
        });
        mLabelHolder.RBottomRoot.setVisibility(View.GONE);
        mLabelHolder.tvForward.setCompoundDrawablePadding(0);
        mLabelHolder.tvComment.setCompoundDrawablePadding(0);
        mLabelHolder.tvTag.setCompoundDrawablePadding(0);
        mLabelHolder.tvTag.setSelected(mDocBean.isThumb());
        mLabelHolder.fRoot.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                ShareArticleEntity entity = new ShareArticleEntity();
                entity.setDocId(mDocBean.getId());
                entity.setTitle(mDocBean.getShare().getTitle());
                entity.setContent(mDocBean.getShare().getDesc());
                entity.setCover(mDocBean.getCover());
                entity.setCreateTime(mDocBean.getCreateTime());
                UserTopEntity entity2 = new UserTopEntity();
                if (mDocBean.getBadgeList().size() > 0) {
                    entity2.setBadge(mDocBean.getBadgeList().get(0));
                } else {
                    entity2.setBadge(null);
                }
                entity2.setHeadPath(mDocBean.getUserIcon());
                entity2.setLevel(mDocBean.getUserLevel());
                entity2.setLevelColor(mDocBean.getUserLevelColor());
                entity2.setSex(mDocBean.getUserSex());
                entity2.setUserId(mDocBean.getUserId());
                entity2.setUserName(mDocBean.getUserName());
                entity.setTexts(mDocBean.getTexts());
                entity.setDocCreateUser(entity2);
                CreateForwardV2Activity.startActivity(mContext, entity);
            }
        });
        mLabelHolder.cRoot.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
//                CreateCommentV2Activity.startActivity(mContext, mDocBean.getId(), false, "", 0);
            }
        });
        mLabelHolder.tRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ((NewDocDetailActivity) mContext).likeDoc(mDocBean.getId(), mDocBean.isThumb(), 0);
            }
        });
    }

    private void giveCoin(int count) {
        if (!NetworkUtils.checkNetworkAndShowError(mContext)) {
            return;
        }
        ((NewDocDetailActivity) mContext).giveCoin(count);
    }

    public void onGiveCoin(int coins) {
        mDocBean.setCoinPays(mDocBean.getCoinPays() + coins);
        notifyItemChanged(mTagsPosition + 1);
    }


    private void getCoinContent() {
        if (!NetworkUtils.checkNetworkAndShowError(mContext)) {
            return;
        }
        ((NewDocDetailActivity) mContext).getCoinContent();
    }


    private static class LikeHolder extends RecyclerView.ViewHolder {

        ImageView mAvatar;
        TextView mTvName;
        ImageView mSex;
        TextView mTvLevel;
        FrameLayout mFlHuiZhang;
        TextView mTvHuiZhang;
        TextView mTvContext, mTvTime, mTvFollow;

        LikeHolder(View itemView) {
            super(itemView);
            mAvatar = itemView.findViewById(R.id.iv_avatar);
            mTvName = itemView.findViewById(R.id.tv_name);
            mSex = itemView.findViewById(R.id.iv_sex);
            mTvLevel = itemView.findViewById(R.id.tv_level);
            mFlHuiZhang = itemView.findViewById(R.id.fl_huizhang_1);
            mTvContext = itemView.findViewById(R.id.tv_contxt);
            mTvHuiZhang = itemView.findViewById(R.id.tv_huizhang_1);
            mTvTime = itemView.findViewById(R.id.tv_time);
            mTvFollow = itemView.findViewById(R.id.tv_follow);
        }
    }

    private void createLike(final LikeHolder holder, final int position) {
        DocDetailNormalEntity item = (DocDetailNormalEntity) getItem(position);
        final UserTopEntity entity = new Gson().fromJson(item.getData(), UserTopEntity.class);
        int size = (int) mContext.getResources().getDimension(R.dimen.x80);
        Glide.with(mContext)
                .load(StringUtils.getUrl(mContext, entity.getHeadPath(), size, size, false, true))
                .error(R.drawable.bg_default_circle)
                .placeholder(R.drawable.bg_default_circle)
                .bitmapTransform(new CropCircleTransformation(mContext))
                .into(holder.mAvatar);
        holder.mTvName.setText(entity.getUserName());
        holder.mSex.setImageResource(entity.getSex().equalsIgnoreCase("M") ? R.drawable.ic_user_girl : R.drawable.ic_user_boy);
        LevelSpan levelSpan = new LevelSpan(ContextCompat.getColor(mContext, R.color.white), mContext.getResources().getDimension(R.dimen.x12));
        final String content = "LV" + entity.getLevel();
        String colorStr = "LV";
        SpannableStringBuilder style = new SpannableStringBuilder(content);
        style.setSpan(levelSpan, content.indexOf(colorStr), content.indexOf(colorStr) + colorStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.mTvLevel.setText(style);

        holder.mFlHuiZhang.setVisibility(View.VISIBLE);
        holder.mTvTime.setVisibility(View.VISIBLE);
        holder.mTvTime.setText(StringUtils.timeFormat(entity.getCreateTime()));
        float radius2 = mContext.getResources().getDimension(R.dimen.y4);
        float[] outerR2 = new float[]{radius2, radius2, radius2, radius2, radius2, radius2, radius2, radius2};
        RoundRectShape roundRectShape2 = new RoundRectShape(outerR2, null, null);
        ShapeDrawable shapeDrawable2 = new ShapeDrawable();
        shapeDrawable2.setShape(roundRectShape2);
        shapeDrawable2.getPaint().setStyle(Paint.Style.FILL);
        shapeDrawable2.getPaint().setColor(StringUtils.readColorStr(entity.getLevelColor(), ContextCompat.getColor(mContext, R.color.main_cyan)));
        holder.mTvLevel.setBackgroundDrawable(shapeDrawable2);

        View[] huizhang = {holder.mFlHuiZhang};
        TextView[] huizhangT = {holder.mTvHuiZhang};
        BadgeEntity badge = entity.getBadge();
        if (badge != null) {
            ArrayList<BadgeEntity> badgeEntities = new ArrayList<>();
            badgeEntities.add(entity.getBadge());
            ViewUtils.badge(mContext, huizhang, huizhangT, badgeEntities);
        } else {
            huizhang[0].setVisibility(View.GONE);
            huizhangT[0].setVisibility(View.GONE);
        }

        holder.mTvContext.setText(entity.getSignature());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewUtils.toPersonal(mContext, entity.getUserId());
            }
        });
        int focus = entity.getFocus();
        if (entity.getUserId().equals(PreferenceUtils.getUUid())) {
            holder.mTvFollow.setVisibility(View.GONE);
        } else {
            holder.mTvFollow.setVisibility(View.VISIBLE);
        }
        if (focus == -1) {
            holder.mTvFollow.setBackgroundResource(R.drawable.btn_rect_corner_cyan_follow_y30);
            holder.mTvFollow.setText("互相关注");
            holder.mTvFollow.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            holder.mTvFollow.setSelected(true);
        } else if (focus == 1) {
            holder.mTvFollow.setText("已关注");
            holder.mTvFollow.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            holder.mTvFollow.setBackgroundResource(R.drawable.btn_rect_corner_cyan_y30);
            holder.mTvFollow.setSelected(true);
        } else {
            holder.mTvFollow.setText("未关注");
            holder.mTvFollow.setTextColor(ContextCompat.getColor(mContext, R.color.main_cyan));
            holder.mTvFollow.setBackgroundResource(R.drawable.btn_rect_corner_cyan_y30);
            holder.mTvFollow.setSelected(false);
        }
    }


    private static class FloorHolder extends RecyclerView.ViewHolder {

        ImageView mIvGiveCoin;
        TextView mTvCoinNum;
        TextView mSort;
        CommonTabLayout pageIndicator;
        TextView mTvCommentLz;
        RelativeLayout mRlPinLun;
        View mViewSep;

        FloorHolder(View itemView) {
            super(itemView);
            mIvGiveCoin = itemView.findViewById(R.id.iv_give_coin);
            mTvCoinNum = itemView.findViewById(R.id.tv_got_coin);
            mSort = itemView.findViewById(R.id.tv_sort);
            pageIndicator = itemView.findViewById(R.id.indicator_person_data);
            mRlPinLun = itemView.findViewById(R.id.rl_pinlun);
            mViewSep = itemView.findViewById(R.id.view_sep);
            mTvCommentLz = itemView.findViewById(R.id.tv_comment_lz);
        }
    }

    private void createFloor(final FloorHolder holder) {
        holder.mTvCoinNum.setText(mContext.getString(R.string.label_got_coin, mDocBean.getCoinPays()));
        if (mDocBean.getUserId().equals(PreferenceUtils.getUUid())) {
            holder.mIvGiveCoin.setImageResource(R.drawable.btn_doc_givecoins_given_enabel);
            holder.mIvGiveCoin.setEnabled(false);
        } else {
            holder.mIvGiveCoin.setEnabled(true);
            holder.mIvGiveCoin.setImageResource(R.drawable.btn_give_coin);
            holder.mIvGiveCoin.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    final AlertDialogUtil alertDialogUtil = AlertDialogUtil.getInstance();
                    alertDialogUtil.createEditDialog(mContext, PreferenceUtils.getAuthorInfo().getCoin(), 0);
                    alertDialogUtil.setOnClickListener(new AlertDialogUtil.OnClickListener() {
                        @Override
                        public void CancelOnClick() {
                            alertDialogUtil.dismissDialog();
                        }

                        @Override
                        public void ConfirmOnClick() {
                            if (DialogUtils.checkLoginAndShowDlg(mContext)) {
                                String content = alertDialogUtil.getEditTextContent();
                                if (!TextUtils.isEmpty(content) && Integer.valueOf(content) > 0) {
                                    giveCoin(Integer.valueOf(content));
                                    alertDialogUtil.dismissDialog();
                                } else {
                                    ToastUtils.showShortToast(mContext, R.string.msg_input_err_coin);
                                }
                            }
                        }
                    });
                    alertDialogUtil.showDialog();
                }
            });
        }
        String[] mTitles = {"转发 " + StringUtils.getNumberInLengthLimit(mDocBean.getRtNum(), 4),
                "评论 " + StringUtils.getNumberInLengthLimit(mDocBean.getComments(), 4),
                "赞 " + StringUtils.getNumberInLengthLimit(mDocBean.getThumbs(), 4)};
        ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
        for (String title : mTitles) {
            mTabEntities.add(new TabEntity(title, R.drawable.ic_personal_bag, R.drawable.ic_personal_bag));
        }
        if (mDocBean.getComments() == 0) {
            ArrayList<DocDetailNormalEntity> entities = DocRecyclerViewAdapter.this.getmComments();
            if (entities == null || entities.size() == 0) {
                holder.mRlPinLun.setVisibility(View.GONE);
                holder.mViewSep.setVisibility(View.GONE);
            } else {
                holder.mRlPinLun.setVisibility(View.VISIBLE);
                holder.mViewSep.setVisibility(View.VISIBLE);
            }
        }else {
            holder.mRlPinLun.setVisibility(View.VISIBLE);
            holder.mViewSep.setVisibility(View.VISIBLE);
        }
        holder.pageIndicator.setTabData(mTabEntities);
        holder.pageIndicator.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                commentType = position;
                if (commentType == 0) {
//                    holder.mSort.setVisibility(View.GONE);
                    holder.mRlPinLun.setVisibility(View.GONE);
                } else if (commentType == 1) {
                    holder.mRlPinLun.setVisibility(View.VISIBLE);
                } else if (commentType == 2) {
                    holder.mRlPinLun.setVisibility(View.GONE);
                }
                if (mPreComments.size() == 0) {
                    if (commentType == 2) {
                        ((NewDocDetailActivity) mContext).loadLikeRequst();
                    } else {
                        ((NewDocDetailActivity) mContext).requestComment();
                        if (mDocBean.getComments() == 0) {
                            ArrayList<DocDetailNormalEntity> entities = DocRecyclerViewAdapter.this.getmComments();
                            if (entities == null || entities.size() == 0) {
                                holder.mRlPinLun.setVisibility(View.GONE);
                                holder.mViewSep.setVisibility(View.GONE);
                            } else {
                                holder.mRlPinLun.setVisibility(View.VISIBLE);
                                holder.mViewSep.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                } else {
                    ArrayList<DocDetailNormalEntity> temp = mPreComments;
                    mPreComments = DocRecyclerViewAdapter.this.getmComments();
//                    if(commentType == 0){
//                        DocRecyclerViewAdapter.this.setShowFavorite(false);
//                    }else {
                    DocRecyclerViewAdapter.this.setShowFavorite(true);
//                    }
                    int pre = mPrePosition;
                    mPrePosition = ((NewDocDetailActivity) mContext).getPosition();
                    DocRecyclerViewAdapter.this.setComment(temp);
                    if (pre != -1) ((NewDocDetailActivity) mContext).scrollToPosition(pre);
                }
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
        holder.mTvCommentLz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCommentLz) {
                    holder.mTvCommentLz.setText("查看全部");
                } else {
                    holder.mTvCommentLz.setText("只看楼主");
                }
                if (isCommentLz) {
                    ((NewDocDetailActivity) mContext).loadGetCommentsLz("楼主");
                } else {
                    ((NewDocDetailActivity) mContext).requestComment();
                }
                isCommentLz = !isCommentLz;
            }
        });
        holder.pageIndicator.setCurrentTab(1);

        holder.mSort.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (sortType == 0) {
                    holder.mSort.setText("楼层排序");
                } else if (sortType == 1) {
                    holder.mSort.setText("热门排序");
                } else if (sortType == 2) {
                    holder.mSort.setText("最新评论");
                }
                showMenuSort(holder.mSort);
            }
        });
    }

    private void showMenuSort(final TextView view) {
        ArrayList<MenuItem> items = new ArrayList<>();
        MenuItem item;
        item = new MenuItem(0, "楼层排序");
        items.add(item);

        item = new MenuItem(1, "热门排序");
        items.add(item);

        item = new MenuItem(2, "最新评论");
        items.add(item);
        fragment.setMenuItems(items);
        fragment.setMenuType(BottomMenuFragment.TYPE_VERTICAL);
        fragment.setmClickListener(new BottomMenuFragment.MenuItemClickListener() {
            @Override
            public void OnMenuItemClick(int itemId) {
                if (itemId == 0) {
                    view.setText("楼层排序");
                    sortType = 0;
                } else if (itemId == 1) {
                    view.setText("热门排序");
                    sortType = 1;
                } else if (itemId == 2) {
                    view.setText("最新评论");
                    sortType = 2;
                }
                ((NewDocDetailActivity) mContext).requestComment();
            }
        });
        fragment.show(((BaseAppCompatActivity) mContext).getSupportFragmentManager(), "DocComment");
    }

    public void setSortType(int sortType) {
        this.sortType = sortType;
    }

    public int getSortType() {
        return sortType;
    }

    public int getTagsPosition() {
        return mTagsPosition;
    }

    public boolean isSortTime() {
        return sortTime;
    }

    public int getCommentType() {
        return commentType;
    }

    public void setCommentType(int commentType) {
        this.commentType = commentType;
    }

    public void setSortTime(boolean sortTime) {
        this.sortTime = sortTime;
    }

    private static class CommentHolder extends RecyclerView.ViewHolder {

        ImageView avatar;
        TextView userName;
        TextView time;
        TextView content;
        TextView level;
        TextView favorite;
        LinearLayout llImg;
        LinearLayout llComment;

        CommentHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.iv_avatar);
            userName = itemView.findViewById(R.id.tv_name);
            level = itemView.findViewById(R.id.tv_level);
            favorite = itemView.findViewById(R.id.tv_favorite);
            content = itemView.findViewById(R.id.tv_comment);
            llImg = itemView.findViewById(R.id.ll_comment_img);
            llComment = itemView.findViewById(R.id.ll_comment_root);
            time = itemView.findViewById(R.id.tv_comment_time);
        }
    }

    private void createComment(final CommentHolder holder, final int position) {
        final DocDetailNormalEntity entitys = (DocDetailNormalEntity) getItem(position);
        final CommentV2Entity entity = new Gson().fromJson(entitys.getData(), CommentV2Entity.class);
        int size = (int) mContext.getResources().getDimension(R.dimen.x72);
        Glide.with(mContext)
                .load(StringUtils.getUrl(mContext, entity.getCreateUser().getHeadPath(), size, size, false, true))
                .error(R.drawable.bg_default_circle)
                .placeholder(R.drawable.bg_default_circle)
                .bitmapTransform(new CropCircleTransformation(mContext))
                .into(holder.avatar);
        holder.avatar.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                ViewUtils.toPersonal(mContext, entity.getCreateUser().getUserId());
            }
        });
        holder.userName.setText(entity.getCreateUser().getUserName());
        LevelSpan levelSpan = new LevelSpan(ContextCompat.getColor(mContext, R.color.white), mContext.getResources().getDimension(R.dimen.x12));
        final String content = "LV" + entity.getCreateUser().getLevel();
        String colorStr = "LV";
        SpannableStringBuilder style = new SpannableStringBuilder(content);
        style.setSpan(levelSpan, content.indexOf(colorStr), content.indexOf(colorStr) + colorStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.level.setText(style);
        float radius2 = mContext.getResources().getDimension(R.dimen.y4);
        float[] outerR2 = new float[]{radius2, radius2, radius2, radius2, radius2, radius2, radius2, radius2};
        RoundRectShape roundRectShape2 = new RoundRectShape(outerR2, null, null);
        ShapeDrawable shapeDrawable2 = new ShapeDrawable();
        shapeDrawable2.setShape(roundRectShape2);
        shapeDrawable2.getPaint().setStyle(Paint.Style.FILL);
        shapeDrawable2.getPaint().setColor(StringUtils.readColorStr(entity.getCreateUser().getLevelColor(), ContextCompat.getColor(mContext, R.color.main_cyan)));
        holder.level.setBackgroundDrawable(shapeDrawable2);
        if (showFavorite) {
            holder.favorite.setVisibility(View.VISIBLE);
            holder.favorite.setSelected(entity.isLike());
            holder.favorite.setText(entity.getLikes() + "");
            holder.favorite.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    ((NewDocDetailActivity) mContext).favoriteComment(entity.getCommentId(), entity.isLike(), position);
                }
            });
        } else {
            holder.favorite.setVisibility(View.GONE);
        }

        holder.favorite.setSelected(entity.isLike());
        holder.favorite.setText(entity.getLikes() + "");
        holder.favorite.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                ((NewDocDetailActivity) mContext).favoriteComment(entity.getCommentId(), entity.isLike(), position);
            }
        });


        holder.content.setText(TagControl.getInstance().paresToSpann(mContext, entity.getContent()));
        holder.content.setMovementMethod(LinkMovementMethod.getInstance());
        if (entity.getImages().size() > 0) {
            holder.llImg.setVisibility(View.VISIBLE);
            holder.llImg.removeAllViews();
            for (int i = 0; i < entity.getImages().size(); i++) {
                final int pos = i;
                Image image = entity.getImages().get(i);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.topMargin = (int) mContext.getResources().getDimension(R.dimen.y10);
                if (FileUtil.isGif(image.getPath())) {
                    ImageView imageView = new ImageView(mContext);
                    setGif(image, imageView, params);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, ImageBigSelectActivity.class);
                            intent.putExtra(ImageBigSelectActivity.EXTRA_KEY_FILEBEAN, entity.getImages());
                            intent.putExtra(ImageBigSelectActivity.EXTRAS_KEY_FIRST_PHTOT_INDEX,
                                    pos);
                            mContext.startActivity(intent);
                        }
                    });
                    holder.llImg.addView(imageView, holder.llImg.getChildCount(), params);
                } else {
                    ImageView imageView = new ImageView(mContext);
                    setImage(image, imageView, params);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, ImageBigSelectActivity.class);
                            intent.putExtra(ImageBigSelectActivity.EXTRA_KEY_FILEBEAN, entity.getImages());
                            intent.putExtra(ImageBigSelectActivity.EXTRAS_KEY_FIRST_PHTOT_INDEX,
                                    pos);
                            mContext.startActivity(intent);
                        }
                    });
                    holder.llImg.addView(imageView, holder.llImg.getChildCount(), params);
                }
            }
        } else {
            holder.llImg.setVisibility(View.GONE);
        }
        int idx = entity.getIdx();
        if (idx == 0) {
            holder.time.setText(StringUtils.timeFormat(entity.getCreateTime()));
        } else {
            holder.time.setText(idx + "楼 · " + StringUtils.timeFormat(entity.getCreateTime()));
        }
        //sec comment
        if (entity.getHotComments() != null && entity.getHotComments().size() > 0) {
            holder.llComment.setVisibility(View.VISIBLE);
            holder.llComment.removeAllViews();
            for (CommentV2SecEntity secEntity : entity.getHotComments()) {
                TextView tv = new TextView(mContext);
                tv.setTextColor(ContextCompat.getColor(mContext, R.color.gray_444444));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.x28));

                String retweetContent = "@" + secEntity.getCreateUser().getUserName();
                if (!TextUtils.isEmpty(secEntity.getCommentTo())) {
                    retweetContent += " 回复 " + "@" + secEntity.getCommentToName();
//                    String retweetColorStr2 = "@" + secEntity.getCommentToName();
//                    style1 = new SpannableStringBuilder(retweetContent);
//                    UserUrlSpan span = new UserUrlSpan(mContext, secEntity.getCommentTo(), null);
//                    style1.setSpan(span, retweetContent.indexOf(retweetColorStr2), retweetContent.indexOf(retweetColorStr2) + retweetColorStr2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
//                retweetContent += ": " + secEntity.getContent();
//                if (style1 == null) {
//                    style1 = new SpannableStringBuilder(retweetContent);
//                }
                SpannableStringBuilder style1 = new SpannableStringBuilder(retweetContent);
                style1.append(TagControl.getInstance().paresToSpann(mContext, ": " + secEntity.getContent()));
                String retweetColorStr = "@" + secEntity.getCreateUser().getUserName();
                UserUrlSpan span = new UserUrlSpan(mContext, secEntity.getCreateUser().getUserId(), null);
                style1.setSpan(span, retweetContent.indexOf(retweetColorStr), retweetContent.indexOf(retweetColorStr) + retweetColorStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                if (!TextUtils.isEmpty(secEntity.getCommentTo())) {
                    String retweetColorStr1 = "@" + secEntity.getCommentToName();
                    UserUrlSpan span1 = new UserUrlSpan(mContext, secEntity.getCommentTo(), null);
                    style1.setSpan(span1, retweetContent.indexOf(retweetColorStr1), retweetContent.indexOf(retweetColorStr1) + retweetColorStr1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                tv.setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View v) {
                        CommentSecListActivity.startActivity(mContext, entity, mDocBean.getId());
                    }
                });

                tv.setText(style1);
                tv.setMovementMethod(LinkMovementMethod.getInstance());
                holder.llComment.addView(tv);
            }
            if (entity.getComments() > entity.getHotComments().size()) {
                TextView tv = new TextView(mContext);
                tv.setTextColor(ContextCompat.getColor(mContext, R.color.main_cyan));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.x28));
                tv.setText("全部" + StringUtils.getNumberInLengthLimit(entity.getComments(), 3) + "条回复");
                tv.setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View v) {
                        CommentSecListActivity.startActivity(mContext, entity, mDocBean.getId());
                    }
                });
                holder.llComment.addView(tv);
            }
        } else {
            holder.llComment.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (mContext instanceof NewDocDetailActivity) {
                    ((NewDocDetailActivity) mContext).goComment(entity.getCommentId(), true, 0, entity.getCreateUser().getUserName());
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showMenu(entity, position);
                return true;
            }
        });
    }

    private void showMenu(final CommentV2Entity bean, final int position) {
        ArrayList<MenuItem> items = new ArrayList<>();
        MenuItem item;
        item = new MenuItem(0, mContext.getString(R.string.label_reply));
        items.add(item);

        if (isShowFavorite()) {
            item = new MenuItem(5, "点赞");
            items.add(item);
        }

        item = new MenuItem(1, mContext.getString(R.string.label_copy_dust));
        items.add(item);

        item = new MenuItem(2, mContext.getString(R.string.label_jubao));
        items.add(item);

//        if(TextUtils.equals(PreferenceUtils.getUUid(), bean.getCreateUser().getUserId()) ){
//            item = new MenuItem(3,mContext.getString(R.string.label_delete));
//            items.add(item);
//        }else if(TextUtils.equals(PreferenceUtils.getUUid(), mDocBean.getUserId())){
//            item = new MenuItem(4,mContext.getString(R.string.label_delete));
//            items.add(item);
//        }

        if (mDocBean.isCanDelete()) {
            item = new MenuItem(3, mContext.getString(R.string.label_delete));
            items.add(item);
        }

        fragment.setShowTop(true);
        fragment.setTopContent(bean.getContent());
        fragment.setMenuItems(items);
        fragment.setMenuType(BottomMenuFragment.TYPE_VERTICAL);
        fragment.setmClickListener(new BottomMenuFragment.MenuItemClickListener() {
            @Override
            public void OnMenuItemClick(int itemId) {
                if (itemId == 0) {
                    CreateCommentV2Activity.startActivity(mContext, bean.getCommentId(), true, "", 0, mDocBean.getId());
                } else if (itemId == 2) {
                    Intent intent = new Intent(mContext, JuBaoActivity.class);
                    intent.putExtra(JuBaoActivity.EXTRA_NAME, bean.getCreateUser().getUserName());
                    intent.putExtra(JuBaoActivity.EXTRA_CONTENT, bean.getContent());
                    intent.putExtra(JuBaoActivity.EXTRA_TYPE, 4);
                    intent.putExtra(JuBaoActivity.UUID, bean.getCommentId());
                    intent.putExtra(JuBaoActivity.EXTRA_TARGET, "COMMENT");
                    mContext.startActivity(intent);
                } else if (itemId == 3) {
                    ((NewDocDetailActivity) mContext).deleteComment(mDocBean.getId(), bean.getCommentId(), position);
                } else if (itemId == 1) {
                    String content = bean.getContent();
                    ClipboardManager cmb = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData mClipData = ClipData.newPlainText("回复内容", content);
                    cmb.setPrimaryClip(mClipData);
                    ToastUtils.showShortToast(mContext, mContext.getString(R.string.label_level_copy_success));
                } else if (itemId == 4) {
                    ((NewDocDetailActivity) mContext).deleteComment(mDocBean.getId(), bean.getCommentId(), position);
                } else if (itemId == 5) {
                    ((NewDocDetailActivity) mContext).favoriteComment(bean.getCommentId(), bean.isLike(), position);
                }
            }
        });
        fragment.show(((BaseAppCompatActivity) mContext).getSupportFragmentManager(), "DocComment");
    }

    private static class CoinHideViewHolder extends RecyclerView.ViewHolder {

        View llTop;
        View llHide;
        TextView hideText;
        View shareRoot;
        TextView lock;
        View shareRoot2;

        CoinHideViewHolder(View itemView) {
            super(itemView);
            llTop = itemView.findViewById(R.id.ll_hide_top);
            llHide = itemView.findViewById(R.id.ll_hide);
            hideText = itemView.findViewById(R.id.tv_hide_text);
            shareRoot = itemView.findViewById(R.id.ll_share_root);
            shareRoot2 = itemView.findViewById(R.id.ll_share_root_2);
            lock = itemView.findViewById(R.id.tv_unlock);
        }
    }

    private void setGif(Image image, ImageView gifImageView, LinearLayout.LayoutParams params) {
        final int[] wh = BitmapUtils.getDocIconSizeFromW(image.getW() * 2, image.getH() * 2, DensityUtil.getScreenWidth(mContext) - (int) mContext.getResources().getDimension(R.dimen.x168));
        params.width = wh[0];
        params.height = wh[1];
        Glide.with(mContext)
                .load(ApiService.URL_QINIU + image.getPath())
                .asGif()
                .override(wh[0], wh[1])
                .placeholder(R.drawable.shape_gray_e8e8e8_background)
                .error(R.drawable.shape_gray_e8e8e8_background)
                .into(gifImageView);
    }

    private void setImage(Image image, final ImageView imageView, LinearLayout.LayoutParams params) {
        final int[] wh = BitmapUtils.getDocIconSizeFromW(image.getW() * 2, image.getH() * 2, DensityUtil.getScreenWidth(mContext) - (int) mContext.getResources().getDimension(R.dimen.x168));
        params.width = wh[0];
        params.height = wh[1];
        Glide.with(mContext)
                .load(StringUtils.getUrl(mContext, ApiService.URL_QINIU + image.getPath(), wh[0], wh[1], true, true))
                .override(wh[0], wh[1])
                .placeholder(R.drawable.shape_gray_e8e8e8_background)
                .error(R.drawable.shape_gray_e8e8e8_background)
                .into(imageView);
    }

    private View.OnClickListener musicCtrl = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (NetworkUtils.isNetworkAvailable(mContext)) {
                if (isFirstClick) {
                    AudioPlayer.get().stopPlayer();
                    AudioPlayer.get().clearList(false);
                    AudioPlayer.get().add(mMusicInfo);
                    isFirstClick = false;
                    MusicPreferences.savePlayMode(PlayModeEnum.LOOP.value());
                    AudioPlayer.get().setShowNotify(false);
                    Notifier.get().cancelAll();
                }
                AudioPlayer.get().playPause();
//                Song musicInfo = mPlayer.getPlayingSong();
//                if(mPlayer.isPlaying() && musicInfo.getPath().equals(mMusicInfo.getPath())){
//                    mPlayer.pause();
//                    mMusicHolder.ivMusicCtrl.setImageResource(R.drawable.btn_doc_video_play);
//                }else if(musicInfo != null && musicInfo.getPath().equals(mMusicInfo.getPath())){
//                    mPlayer.play();
//                    mMusicHolder.ivMusicCtrl.setImageResource(R.drawable.btn_doc_video_stop);
//                }else {
//                    PlayList playList = new PlayList(mMusicInfo);
//                    mPlayer.play(playList,0);
//                    mMusicHolder.ivMusicCtrl.setImageResource(R.drawable.btn_doc_video_stop);
//                }
            } else {
                ToastUtils.showShortToast(mContext, mContext.getString(R.string.msg_connection));
            }
        }
    };

    public void createLabel(final String tagName) {
        if (!NetworkUtils.checkNetworkAndShowError(mContext)) {
            return;
        }
        if (mDocBean != null) {
            if (DialogUtils.checkLoginAndShowDlg(mContext)) {
                TagSendEntity bean = new TagSendEntity(mDocBean.getId(), tagName);
                ((NewDocDetailActivity) mContext).createLabel(bean);
            }
        }
    }

    public void onCreateLabel(String s, String name) {
        DocTagEntity tag = new DocTagEntity();
        tag.setLiked(true);
        tag.setId(s);
        tag.setLikes(1);
        tag.setName(name);
        mTags.add(tag);
        mLabelHolder.mDvLabel.notifyAdapter();
    }

    public void ownerDelSuccess(int position) {
        NewCommentEntity bean = (NewCommentEntity) getItem(position);
        bean.setContent("已被楼主删除");
        bean.setNewDeleteFlag(true);
        notifyItemChanged(position);
    }

    private void plusLabel(final int position) {
        if (!NetworkUtils.checkNetworkAndShowError(mContext)) {
            return;
        }
        if (mDocBean != null) {
            if (DialogUtils.checkLoginAndShowDlg(mContext)) {
                final DocTagEntity tagBean = mTags.get(position);
                TagLikeEntity bean = new TagLikeEntity(mDocBean.getId(), tagBean.getId());
                ((BaseAppCompatActivity) mContext).createDialog();
                ((NewDocDetailActivity) mContext).likeTag(tagBean.isLiked(), position, bean);
            }
        }
    }

    public void plusSuccess(boolean isLike, int position) {
        DocTagEntity tagBean = mTags.get(position);
        mTags.remove(position);
        tagBean.setLiked(isLike);
        if (isLike) {
            tagBean.setLikes(tagBean.getLikes() + 1);
            mTags.add(position, tagBean);
        } else {
            tagBean.setLikes(tagBean.getLikes() - 1);
            if (tagBean.getLikes() > 0) {
                mTags.add(position, tagBean);
            }
        }
        mLabelHolder.mDvLabel.notifyAdapter();
    }


    private void addlabel(int position) {
        if (position < mTags.size()) {
            plusLabel(position);
        } else {
            ((NewDocDetailActivity) mContext).addDocLabelView();
        }
    }

    private View.OnClickListener mAvatarListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            String uuid = (String) v.getTag(R.id.id_creator_uuid);
            if (!TextUtils.isEmpty(uuid) && !uuid.equals(PreferenceUtils.getUUid())) {
                Intent i = new Intent(mContext, PersonalV2Activity.class);
                i.putExtra(BaseAppCompatActivity.UUID, uuid);
                mContext.startActivity(i);
            }
        }
    };
}