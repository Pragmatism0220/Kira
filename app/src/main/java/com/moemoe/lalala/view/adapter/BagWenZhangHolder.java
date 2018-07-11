package com.moemoe.lalala.view.adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.BagMyShowEntity;
import com.moemoe.lalala.model.entity.FolderType;
import com.moemoe.lalala.model.entity.ShowFolderEntity;
import com.moemoe.lalala.model.entity.WenZhangFolderEntity;
import com.moemoe.lalala.utils.DensityUtil;
import com.moemoe.lalala.utils.IntentUtils;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.utils.TagUtils;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.view.activity.BaseAppCompatActivity;
import com.moemoe.lalala.view.activity.CreateRichDocActivity;
import com.moemoe.lalala.view.activity.FileMovieActivity;
import com.moemoe.lalala.view.activity.NewDocDetailActivity;
import com.moemoe.lalala.view.activity.NewFileCommonActivity;
import com.moemoe.lalala.view.activity.NewFileManHuaActivity;
import com.moemoe.lalala.view.activity.NewFileXiaoshuoActivity;
import com.moemoe.lalala.view.activity.NewFolderActivity;
import com.moemoe.lalala.view.activity.NewFolderEditActivity;
import com.moemoe.lalala.view.activity.NewFolderWenZhangActivity;
import com.moemoe.lalala.view.widget.adapter.ClickableViewHolder;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.CropSquareTransformation;
import jp.wasabeef.glide.transformations.CropTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.moemoe.lalala.utils.StartActivityConstant.REQUEST_CODE_CREATE_DOC;

/**
 * Created by yi on 2017/7/21.
 */

public class BagWenZhangHolder extends ClickableViewHolder {
    //
//    ImageView iv;
//    ImageView select;
//    TextView title;
//    TextView content;
//    TextView time;
//    TextView readNum;
//    TextView commentNum;
//    TextView fromName;
//
//
//    public BagWenZhangHolder(View itemView) {
//        super(itemView);
//        iv = $(R.id.iv_cover);
//        select = $(R.id.iv_select);
//        title = $(R.id.tv_title);
//        content = $(R.id.tv_content);
//        time = $(R.id.tv_time);
//        readNum = $(R.id.tv_read_num);
//        commentNum = $(R.id.tv_comment_num);
//        fromName = $(R.id.tv_from_name);
//    }
//
//    public void createItem(final WenZhangFolderEntity entity, final int position, boolean isSelect){
//        select.setVisibility(isSelect?View.VISIBLE:View.GONE);
//        select.setSelected(entity.isSelect());
//        title.setText(entity.getTitle());
//        content.setText(entity.getContent());
//        time.setText(StringUtils.timeFormat(entity.getCreateTime()));
//        readNum.setText("阅读 " + entity.getRead());
//        commentNum.setText("评论 " + entity.getComments());
//        if(!entity.getDocType().equals("书包")){
//            setVisible(R.id.tv_tmp,true);
//            fromName.setVisibility(View.VISIBLE);
//            fromName.setText(entity.getDocType());
//            fromName.setOnClickListener(new NoDoubleClickListener() {
//                @Override
//                public void onNoDoubleClick(View v) {
//                    if (!TextUtils.isEmpty(entity.getDocTypeSchema())) {
//                        Uri uri = Uri.parse(entity.getDocTypeSchema());
//                        IntentUtils.toActivityFromUri(itemView.getContext(), uri,v);
//                    }
//                }
//            });
//        }else {
//            setVisible(R.id.tv_tmp,false);
//            fromName.setVisibility(View.GONE);
//            fromName.setOnClickListener(null);
//        }
//        Glide.with(itemView.getContext())
//                .load(StringUtils.getUrl(itemView.getContext(),entity.getCover(),(int)itemView.getContext().getResources().getDimension(R.dimen.x112),(int)itemView.getContext().getResources().getDimension(R.dimen.y148), false, true))
//                .placeholder(R.drawable.shape_gray_e8e8e8_background)
//                .error(R.drawable.shape_gray_e8e8e8_background)
//                .bitmapTransform(new CropTransformation(itemView.getContext(),(int)itemView.getContext().getResources().getDimension(R.dimen.x112),(int)itemView.getContext().getResources().getDimension(R.dimen.y148)),new RoundedCornersTransformation(itemView.getContext(),(int)itemView.getContext().getResources().getDimension(R.dimen.y8),0))
//                .into(iv);
//    }

    public BagWenZhangHolder(View itemView) {
        super(itemView);
    }

    public void createItem(final WenZhangFolderEntity entity, final int position, boolean isSelect) {

        ImageView ivCover = (ImageView) $(R.id.iv_cover);
        TextView tvTitle = (TextView) $(R.id.tv_title);
        ImageView ivUserAvatar = (ImageView) $(R.id.iv_user_avatar);
        TextView tvUserName = (TextView) $(R.id.tv_user_name);
        View userRoot = (View) $(R.id.ll_user_root);
        TextView tvTag1 = (TextView) $(R.id.tv_tag_1);
        TextView tvTag2 = (TextView) $(R.id.tv_tag_2);

        int w, h;
        View viewStep = (View) $(R.id.view_step);
        viewStep.setVisibility(View.GONE);
        w = h = getResources().getDimensionPixelSize(R.dimen.y180);
        Glide.with(context)
                .load(StringUtils.getUrl(context, entity.getCover(), w, h, false, true))
                .error(R.drawable.shape_gray_e8e8e8_background)
                .placeholder(R.drawable.shape_gray_e8e8e8_background)
                .bitmapTransform(new CropSquareTransformation(context))
                .into(ivCover);

        TextView tvExtraContent = (TextView) $(R.id.tv_extra_content);
//                    tvExtraContent.setText("阅读 " + item.getPlayNum() + " · " + StringUtils.timeFormat(item.getTime()));

        tvExtraContent.setText(entity.getCreateTime());
        tvTitle.setText(entity.getTitle());

//        int size = getResources().getDimensionPixelSize(R.dimen.y32);
//        ivUserAvatar.setVisibility(View.VISIBLE);
//        Glide.with(context)
//                .load(StringUtils.getUrl(context, entity.get.getUserIcon(), size, size, false, true))
//                .error(R.drawable.bg_default_circle)
//                .placeholder(R.drawable.bg_default_circle)
//                .bitmapTransform(new CropCircleTransformation(context))
//                .into(ivUserAvatar);
//        tvUserName.setVisibility(View.VISIBLE);
//        tvUserName.setText(item.getCreateUserName());
//        userRoot.setOnClickListener(new NoDoubleClickListener() {
//            @Override
//            public void onNoDoubleClick(View v) {
//                ViewUtils.toPersonal(context, item.getCreateUser());
//            }
//        });

//        //tag
//        View[] tags = {tvTag1, tvTag2};
//        if (item.getTextsV2().size() > 1) {
//            tags[0].setVisibility(View.VISIBLE);
//            tags[1].setVisibility(View.VISIBLE);
//        } else if (item.getTextsV2().size() > 0) {
//            tags[0].setVisibility(View.VISIBLE);
//            tags[1].setVisibility(View.INVISIBLE);
//        } else {
//            tags[0].setVisibility(View.INVISIBLE);
//            tags[1].setVisibility(View.INVISIBLE);
//        }
//        int size1 = tags.length > item.getTextsV2().size() ? item.getTextsV2().size() : tags.length;
//        for (int i = 0; i < size1; i++) {
//            TagUtils.setBackGround(item.getTextsV2().get(i).getText(), tags[i]);
//            tags[i].setOnClickListener(new NoDoubleClickListener() {
//                @Override
//                public void onNoDoubleClick(View v) {
//                    //TODO 跳转标签页
//                }
//            });
//        }
//        itemView.setOnClickListener(new NoDoubleClickListener() {
//            @Override
//            public void onNoDoubleClick(View v) {
//                    Intent i = new Intent(itemView.getContext(), NewDocDetailActivity.class);
//                    i.putExtra("uuid", entity.getFolderId());
//                    i.putExtra("title", "书包");
//                    itemView.getContext().startActivity(i);
//            }
//        });
    }
}
