package com.moemoe.lalala.view.adapter;

import android.view.View;

import com.bumptech.glide.Glide;
import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.OrderEntity;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;

/**
 *
 * Created by yi on 2017/6/26.
 */

public class OrderListAdapter extends BaseRecyclerViewAdapter<OrderEntity,OrderViewHolder> {

    public OrderListAdapter() {
        super(R.layout.item_order_history);
    }


    @Override
    protected void convert(OrderViewHolder helper, final OrderEntity item,int position) {
        Glide.with(context)
                .load(StringUtils.getUrl(context, item.getIcon(), (int)context.getResources().getDimension(R.dimen.y150),(int)context.getResources().getDimension(R.dimen.y150),false,true))
                .override((int)context.getResources().getDimension(R.dimen.y150),(int)context.getResources().getDimension(R.dimen.y150))
                .error(R.drawable.shape_gray_e8e8e8_background)
                .placeholder(R.drawable.shape_gray_e8e8e8_background)
                .into(helper.ivCommodity);
        helper.tvName.setText(item.getProductName());
        helper.tvStatus.setVisibility(View.VISIBLE);
        if(item.getStatus() == 1){
            helper.tvStatus.setSelected(true);
            helper.tvStatus.setText("等待支付");
            helper.tvTime.setText("订单有效期至：" + item.getEndTime());
        }else if(item.getStatus() == 2){
            helper.tvStatus.setSelected(false);
            helper.tvStatus.setText("购买成功");
            helper.tvTime.setText(item.getEndTime());
        }else {
            helper.tvStatus.setVisibility(View.INVISIBLE);
            helper.tvTime.setText(item.getEndTime());
        }

    }

    @Override
    public int getItemType(int position) {
        return 0;
    }
}
