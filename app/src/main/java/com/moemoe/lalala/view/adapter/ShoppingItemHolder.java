package com.moemoe.lalala.view.adapter;

import android.content.Intent;
import android.view.View;

import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.DocResponse;
import com.moemoe.lalala.view.widget.adapter.ClickableViewHolder;

/**
 * Created by Hygge on 2018/5/23.
 */

public class ShoppingItemHolder extends ClickableViewHolder {


    public ShoppingItemHolder(View itemView) {
        super(itemView);
    }

    public void createItem(final String entity, final int paposition) {
        $(R.id.iv_go_shopping).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(context, ShoppingDetalisActivity.class);
//                context.startActivity(intent);
            }
        });
    }
}
