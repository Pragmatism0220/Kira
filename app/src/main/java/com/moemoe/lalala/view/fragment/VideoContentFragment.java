package com.moemoe.lalala.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moemoe.lalala.R;
import com.moemoe.lalala.event.RefreshListEvent;
import com.moemoe.lalala.model.entity.FolderType;
import com.moemoe.lalala.model.entity.KiraVideoEntity;
import com.moemoe.lalala.model.entity.ShowFolderEntity;
import com.moemoe.lalala.model.entity.StreamFileEntity;
import com.moemoe.lalala.utils.DensityUtil;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.utils.TagUtils;
import com.moemoe.lalala.utils.ToastUtils;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.view.activity.FileMovieActivity;
import com.moemoe.lalala.view.activity.KiraMusicActivity;
import com.moemoe.lalala.view.activity.KiraVideoActivity;
import com.moemoe.lalala.view.activity.NewBagActivity;
import com.moemoe.lalala.view.activity.NewFileCommonActivity;
import com.moemoe.lalala.view.activity.NewFileManHuaActivity;
import com.moemoe.lalala.view.activity.NewFileXiaoshuoActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.CropTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 *
 * Created by yi on 2018/1/17.
 */

public class VideoContentFragment extends BaseFragment{

    @BindView(R.id.tv_fav)
    TextView mTvFav;

    private KiraVideoEntity mVideo;
    private String mFolderId;

    public static VideoContentFragment newInstance(String folderId,KiraVideoEntity videoEntity){
        VideoContentFragment fragment = new VideoContentFragment();
        Bundle bundle = new Bundle();
        bundle.putString("folderId",folderId);
        bundle.putParcelable("video",videoEntity);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_video_content;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mFolderId = getArguments().getString("folderId");
        mVideo = getArguments().getParcelable("video");
        bindView(mVideo);
    }

    @OnClick({R.id.tv_download,R.id.tv_fav,R.id.tv_forward,R.id.tv_to_bag})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_download:
                ((KiraVideoActivity)getContext()).downloadRaw();
                break;
            case R.id.tv_fav:
                ((KiraVideoActivity)getContext()).favMovie(mVideo.isFav());
                break;
            case R.id.tv_forward:
                ((KiraVideoActivity)getContext()).rtMovie();
                break;
            case R.id.tv_to_bag:
                if(!TextUtils.isEmpty(mUserId)){
                    Intent i2 = new Intent(getContext(),NewBagActivity.class);
                    i2.putExtra("uuid",mUserId);
                    startActivity(i2);
                }

                break;
        }
    }



    private String mUserId;

    public void bindView(KiraVideoEntity entity){
        mVideo = entity;

        ((TextView)$(R.id.tv_title)).setText(mVideo.getFileName());
        ((TextView)$(R.id.tv_play_num)).setText(String.valueOf(mVideo.getPlayNum()));
        ((TextView)$(R.id.tv_danmu_num)).setText(String.valueOf(mVideo.getBarrageNum()));
        ((TextView)$(R.id.tv_time)).setText(StringUtils.timeFormat(mVideo.getUpdateTime()));
        ((TextView)$(R.id.tv_desc)).setText(mVideo.getSummary());
        //tag
        int[] tagsIds = {R.id.tv_tag_1,R.id.tv_tag_2};
        if(mVideo.getTexts().size() > 1){
            $(tagsIds[0]).setVisibility(View.VISIBLE);
            $(tagsIds[1]).setVisibility(View.VISIBLE);
        }else if(mVideo.getTexts().size() > 0){
            $(tagsIds[0]).setVisibility(View.VISIBLE);
            $(tagsIds[1]).setVisibility(View.INVISIBLE);
        }else {
            $(tagsIds[0]).setVisibility(View.INVISIBLE);
            $(tagsIds[1]).setVisibility(View.INVISIBLE);
        }
        int size1 = tagsIds.length > mVideo.getTexts().size() ? mVideo.getTexts().size() : tagsIds.length;
        for (int i = 0;i < size1;i++){
            TagUtils.setBackGround(mVideo.getTexts().get(i).getText(),$(tagsIds[i]));
            $(tagsIds[i]).setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    //TODO 跳转标签页
                }
            });
        }
        Glide.with(getContext())
                .load(StringUtils.getUrl(getContext(),mVideo.getUser().getHeadPath(),getResources().getDimensionPixelSize(R.dimen.y80),getResources().getDimensionPixelSize(R.dimen.y80),false,true))
                .error(R.drawable.bg_default_circle)
                .placeholder(R.drawable.bg_default_circle)
                .bitmapTransform(new CropCircleTransformation(getContext()))
                .into((ImageView) $(R.id.iv_avatar));
        $(R.id.iv_avatar).setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                ViewUtils.toPersonal(getContext(),mVideo.getUser().getUserId());
            }
        });
        mUserId = mVideo.getUser().getUserId();
        ((TextView)$(R.id.tv_user_name)).setText(mVideo.getUser().getUserName());

        //video
        int size = mVideo.getItemList().size() > 2 ? 2 : mVideo.getItemList().size();
        LinearLayout videoRoot = $(R.id.ll_video_root);
        if(size > 0){
            for(int i = 0;i < size;i++){
                videoRoot.addView(getVideoView(mVideo.getItemList().get(i),i));
            }
        }else {
            videoRoot.setVisibility(View.GONE);
        }

        $(R.id.tv_refresh).setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                EventBus.getDefault().post(new RefreshListEvent("recommend"));
            }
        });

        mTvFav.setSelected(mVideo.isFav());

        EventBus.getDefault().post(new RefreshListEvent(mVideo.getId()));

    }

    public void bindRefresh(ArrayList<ShowFolderEntity> entities){
        addBottomList(entities);
    }

    private View getVideoView(final StreamFileEntity entity, int position){
        View v = View.inflate(getContext(),R.layout.item_feed_type_3_v3,null);
        ImageView ivCover = v.findViewById(R.id.iv_cover);
        ImageView ivUserAvatar = v.findViewById(R.id.iv_user_avatar);
        TextView tvUserName = v.findViewById(R.id.tv_user_name);
        View userRoot = v.findViewById(R.id.ll_user_root);
        TextView tvMark = v.findViewById(R.id.tv_mark);
        TextView tvPlayNum = v.findViewById(R.id.tv_play_num);
        TextView tvDanmuNum = v.findViewById(R.id.tv_danmu_num);
        TextView tvCoin = v.findViewById(R.id.tv_coin);
        TextView tvTitle = v.findViewById(R.id.tv_title);
        TextView tvTag1 = v.findViewById(R.id.tv_tag_1);
        TextView tvTag2 = v.findViewById(R.id.tv_tag_2);

        int w = (DensityUtil.getScreenWidth(getContext()) - (int)getResources().getDimension(R.dimen.x72)) / 2;
        int h = getResources().getDimensionPixelSize(R.dimen.y250);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(w,h);
        LinearLayout.LayoutParams lp2;
        if(position == 1){
            lp2 = new LinearLayout.LayoutParams(w + (int)getResources().getDimension(R.dimen.x24), ViewGroup.LayoutParams.WRAP_CONTENT);
            v.setPadding((int)getResources().getDimension(R.dimen.x24),0,0,0);
        }else {
            lp2 = new LinearLayout.LayoutParams(w,ViewGroup.LayoutParams.WRAP_CONTENT);
            v.setPadding(0,0,0,0);
        }
        v.setLayoutParams(lp2);
        ivCover.setLayoutParams(lp);
        Glide.with(getContext())
                .load(StringUtils.getUrl(getContext(),entity.getCover(),w,h,false,true))
                .error(R.drawable.shape_gray_e8e8e8_background)
                .placeholder(R.drawable.shape_gray_e8e8e8_background)
                .bitmapTransform(new CropTransformation(getContext(),w,h),
                        new RoundedCornersTransformation(getContext(),getResources().getDimensionPixelSize(R.dimen.y8),0))
                .into(ivCover);

        int size = getResources().getDimensionPixelSize(R.dimen.y32);
        Glide.with(getContext())
                .load(StringUtils.getUrl(getContext(),entity.getIcon(),size,size,false,true))
                .error(R.drawable.bg_default_circle)
                .placeholder(R.drawable.bg_default_circle)
                .bitmapTransform(new CropCircleTransformation(getContext()))
                .into(ivUserAvatar);
         tvUserName.setText(entity.getUserName());
         userRoot.setOnClickListener(new NoDoubleClickListener() {
             @Override
             public void onNoDoubleClick(View v) {
                 ViewUtils.toPersonal(getContext(),entity.getUserId());
             }
         });

        tvMark.setText("视频");
        tvPlayNum.setText(String.valueOf(entity.getPlayNum()));
        tvDanmuNum.setText(String.valueOf(entity.getBarrageNum()));
        if(entity.getCoin() == 0){
            tvCoin.setText("免费");
        }else {
            tvCoin.setText(entity.getCoin() + "节操");
        }
        tvTitle.setText(entity.getFileName());

        //tag
        View[] tags = {tvTag1,tvTag2};
        if(entity.getTexts().size() > 1){
            tags[0].setVisibility(View.VISIBLE);
            tags[1].setVisibility(View.VISIBLE);
        }else if(entity.getTexts().size() > 0){
            tags[0].setVisibility(View.VISIBLE);
            tags[1].setVisibility(View.INVISIBLE);
        }else {
            tags[0].setVisibility(View.INVISIBLE);
            tags[1].setVisibility(View.INVISIBLE);
        }
        int size1 = tags.length > entity.getTexts().size() ? entity.getTexts().size() : tags.length;
        for (int i = 0;i < size1;i++){
            TagUtils.setBackGround(entity.getTexts().get(i).getText(),tags[i]);
            tags[i].setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    //TODO 跳转标签页
                }
            });
        }
        v.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                KiraVideoActivity.startActivity(getContext(),mFolderId,entity.getId(),entity.getFileName(),entity.getCover());
            }
        });
        return v;
    }

    public void changeFav(){
        mTvFav.setSelected(!mTvFav.isSelected());
        if(mTvFav.isSelected()){
            ToastUtils.showShortToast(getContext(),"收藏成功");
        }else {
            ToastUtils.showShortToast(getContext(),"取消收藏成功");
        }
    }

    private void addBottomList(ArrayList<ShowFolderEntity> entities){
        LinearLayout recommendRoot = rootView.findViewById(R.id.ll_recommend_root);
        recommendRoot.removeAllViews();
        if(entities.size() > 0){
            recommendRoot.setVisibility(View.VISIBLE);
            for (int n = 0;n < entities.size();n++){
                final ShowFolderEntity item = entities.get(n);
                View v = LayoutInflater.from(getContext()).inflate(R.layout.item_feed_type_2_v3, null);

                ImageView ivCover = v.findViewById(R.id.iv_cover);
                TextView tvMark = v.findViewById(R.id.tv_mark);
                TextView tvCoin = v.findViewById(R.id.tv_coin);
                TextView tvExtra = v.findViewById(R.id.tv_extra);
                ImageView ivPlay = v.findViewById(R.id.iv_play);
                TextView tvTitle = v.findViewById(R.id.tv_title);
                ImageView ivAvatar = v.findViewById(R.id.iv_user_avatar);
                TextView tvUserName = v.findViewById(R.id.tv_user_name);
                TextView tvExtraContent = v.findViewById(R.id.tv_extra_content);
                TextView tvTag1 = v.findViewById(R.id.tv_tag_1);
                TextView tvTag2 = v.findViewById(R.id.tv_tag_2);
                TextView tvPlayNum = v.findViewById(R.id.tv_play_num);
                TextView tvDanmuNum = v.findViewById(R.id.tv_danmu_num);

                int w = getResources().getDimensionPixelSize(R.dimen.x222);
                int h = getResources().getDimensionPixelSize(R.dimen.y160);
                Glide.with(getContext())
                        .load(StringUtils.getUrl(getContext(),item.getCover(),w,h,false,true))
                        .error(R.drawable.shape_gray_e8e8e8_background)
                        .placeholder(R.drawable.shape_gray_e8e8e8_background)
                        .bitmapTransform(new CropTransformation(getContext(),w,h)
                                ,new RoundedCornersTransformation(getContext(),getResources().getDimensionPixelSize(R.dimen.y8),0))
                        .into(ivCover);

                tvExtra.setText(item.getItems() + "项");
                tvExtraContent.setText(StringUtils.timeFormat(item.getTime()));

                if(item.getType().equals(FolderType.ZH.toString())){
                    tvMark.setVisibility(View.VISIBLE);
                    ivPlay.setVisibility(View.GONE);
                    tvMark.setText("综合");
                    tvMark.setBackgroundResource(R.drawable.shape_rect_zonghe);
                }else if(item.getType().equals(FolderType.TJ.toString())){
                    tvMark.setVisibility(View.VISIBLE);
                    ivPlay.setVisibility(View.GONE);
                    tvMark.setText("图集");
                    tvMark.setBackgroundResource(R.drawable.shape_rect_tuji);
                }else if(item.getType().equals(FolderType.MH.toString())){
                    tvMark.setVisibility(View.VISIBLE);
                    ivPlay.setVisibility(View.GONE);
                    tvMark.setText("漫画");
                    tvMark.setBackgroundResource(R.drawable.shape_rect_manhua);
                }else if(item.getType().equals(FolderType.XS.toString())){
                    tvMark.setVisibility(View.VISIBLE);
                    ivPlay.setVisibility(View.GONE);
                    tvMark.setText("小说");
                    tvMark.setBackgroundResource(R.drawable.shape_rect_xiaoshuo);
                }else if(item.getType().equals(FolderType.WZ.toString())){
                    tvMark.setVisibility(View.VISIBLE);
                    ivPlay.setVisibility(View.GONE);
                    tvMark.setText("文章");
                    tvMark.setBackgroundResource(R.drawable.shape_rect_zonghe);
                }else if(item.getType().equals(FolderType.SP.toString())){
                    tvMark.setVisibility(View.VISIBLE);
                    ivPlay.setVisibility(View.GONE);
                    tvMark.setText("视频集");
                    tvMark.setBackgroundResource(R.drawable.shape_rect_shipin);
                }else if(item.getType().equals(FolderType.YY.toString())){
                    tvMark.setVisibility(View.VISIBLE);
                    ivPlay.setVisibility(View.GONE);
                    tvMark.setText("音乐集");
                    tvMark.setBackgroundResource(R.drawable.shape_rect_yinyue);
                }else if("MOVIE".equals(item.getType())){
                    tvMark.setVisibility(View.GONE);
                    ivPlay.setVisibility(View.VISIBLE);
                    ivPlay.setImageResource(R.drawable.ic_baglist_video_play);
                    tvExtra.setText(item.getTimestamp());
                    ivPlay.setVisibility(View.VISIBLE);
                    tvPlayNum.setText(String.valueOf(item.getPlayNum()));
                    tvPlayNum.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(),R.drawable.ic_baglist_video_playtimes_gray),null,null,null);
                    tvDanmuNum.setVisibility(View.VISIBLE);
                    tvDanmuNum.setText(String.valueOf(item.getBarrageNum()));
                }else if("MUSIC".equals(item.getType())){
                    tvMark.setVisibility(View.GONE);
                    ivPlay.setImageResource(R.drawable.ic_baglist_music_play);
                    tvExtra.setText(item.getTimestamp());
                    tvDanmuNum.setVisibility(View.GONE);
                    tvPlayNum.setText(String.valueOf(item.getPlayNum()));
                    tvPlayNum.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(),R.drawable.ic_baglist_music_times),null,null,null);
                }

                tvTitle.setText(item.getFolderName());

                //tag
                View[] tagsId = {tvTag1,tvTag2};
                tvTag1.setOnClickListener(null);
                tvTag2.setOnClickListener(null);
                if(item.getTextsV2().size() > 1){
                    tvTag1.setVisibility(View.VISIBLE);
                    tvTag2.setVisibility(View.VISIBLE);
                }else if(item.getTextsV2().size() > 0){
                    tvTag1.setVisibility(View.VISIBLE);
                    tvTag2.setVisibility(View.INVISIBLE);
                }else {
                    tvTag1.setVisibility(View.INVISIBLE);
                    tvTag2.setVisibility(View.INVISIBLE);
                }
                int size = tagsId.length > item.getTextsV2().size() ? item.getTextsV2().size() : tagsId.length;
                for (int i = 0;i < size;i++){
                    TagUtils.setBackGround(item.getTextsV2().get(i).getText(),tagsId[i]);
                    tagsId[i].setOnClickListener(new NoDoubleClickListener() {
                        @Override
                        public void onNoDoubleClick(View v) {
                            //TODO 跳转标签页
                        }
                    });
                }

                //user
                int avatarSize = getResources().getDimensionPixelSize(R.dimen.y32);
                Glide.with(getContext())
                        .load(StringUtils.getUrl(getContext(),item.getUserIcon(),avatarSize,avatarSize,false,true))
                        .error(R.drawable.bg_default_circle)
                        .placeholder(R.drawable.bg_default_circle)
                        .bitmapTransform(new CropCircleTransformation(getContext()))
                        .into(ivAvatar);
                tvUserName.setText(item.getCreateUserName());
                ivAvatar.setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View v) {
                        ViewUtils.toPersonal(getContext(),item.getCreateUser());
                    }
                });
                tvUserName.setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View v) {
                        ViewUtils.toPersonal(getContext(),item.getCreateUser());
                    }
                });

                if(item.getCoin() > 0){
                    tvCoin.setText(item.getCoin() + "节操");
                }else {
                    tvCoin.setText("免费");
                }

                v.setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View v) {
                        if(item.getType().equals(FolderType.ZH.toString())){
                            NewFileCommonActivity.startActivity(getContext(),FolderType.ZH.toString(),item.getFolderId(),item.getCreateUser());
                        }else if(item.getType().equals(FolderType.TJ.toString())){
                            NewFileCommonActivity.startActivity(getContext(),FolderType.TJ.toString(),item.getFolderId(),item.getCreateUser());
                        }else if(item.getType().equals(FolderType.MH.toString())){
                            NewFileManHuaActivity.startActivity(getContext(),FolderType.MH.toString(),item.getFolderId(),item.getCreateUser());
                        }else if(item.getType().equals(FolderType.XS.toString())){
                            NewFileXiaoshuoActivity.startActivity(getContext(),FolderType.XS.toString(),item.getFolderId(),item.getCreateUser());
                        }else if(item.getType().equals(FolderType.YY.toString())){
                            FileMovieActivity.startActivity(getContext(),FolderType.YY.toString(),item.getFolderId(),item.getCreateUser());
                        }else if(item.getType().equals(FolderType.SP.toString())){
                            FileMovieActivity.startActivity(getContext(),FolderType.SP.toString(),item.getFolderId(),item.getCreateUser());
                        }else if("MOVIE".equals(item.getType())){
                            KiraVideoActivity.startActivity(getContext(),item.getUuid(),item.getFolderId(),item.getFolderName(),item.getCover());
                        }else if("MUSIC".equals(item.getType())){
                            KiraMusicActivity.startActivity(getContext(),item.getUuid(),item.getFolderId(),item.getFolderName(),item.getCover());
                        }
                    }
                });

                recommendRoot.addView(v);
            }
        }else {
            recommendRoot.setVisibility(View.GONE);
        }
    }
}
