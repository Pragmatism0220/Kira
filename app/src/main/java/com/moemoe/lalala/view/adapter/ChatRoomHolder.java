package com.moemoe.lalala.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.FolderType;
import com.moemoe.lalala.model.entity.ShowFolderEntity;
import com.moemoe.lalala.utils.DensityUtil;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.utils.TagUtils;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.view.widget.adapter.ClickableViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import io.rong.imkit.RongIM;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.model.UserInfo;
import io.rong.message.TextMessage;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.CropTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.mob.MobSDK.getContext;

/**
 * Created by yi on 2017/7/21.
 */

public class ChatRoomHolder extends ClickableViewHolder {


    public ChatRoomHolder(View itemView) {
        super(itemView);

    }

    public void createItem(final UIMessage entity, final int position) {

        String extra = ((TextMessage) entity.getMessage().getContent()).getExtra();
        try {
            JSONObject jsonObject = new JSONObject(extra);
            String member = jsonObject.getString("member");
            TagUtils.setTextAndBorderColor(context, (TextView) $(R.id.tv_mark), member);
            UserInfo userInfo = entity.getMessage().getContent().getUserInfo();
            if (userInfo == null) {
                userInfo = RongUserInfoManager.getInstance().getUserInfo(entity.getSenderUserId());
            }
            if (userInfo == null) {
                setText(R.id.tv_name, entity.getSenderUserId() + ":");
            } else {
                setText(R.id.tv_name, userInfo.getName() + ":");
            }
            setText(R.id.tv_content, ((TextMessage) entity.getMessage().getContent()).getContent());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
  