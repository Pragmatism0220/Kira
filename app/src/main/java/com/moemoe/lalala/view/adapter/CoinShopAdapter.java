package com.moemoe.lalala.view.adapter;

import android.content.Context;
import android.view.View;

import com.bumptech.glide.Glide;
import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.CoinShopEntity;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.view.activity.CoinShopActivity;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;

/**
 *
 * Created by yi on 2017/6/26.
 */

public class CoinShopAdapter extends BaseRecyclerViewAdapter<CoinShopEntity,ShopItemViewHolder> {

    public CoinShopAdapter(Context context) {
        super(R.layout.item_coin_shop);
    }


    @Override
    protected void convert(ShopItemViewHolder helper, final CoinShopEntity item, int position) {
        Glide.with(context)
                .load(StringUtils.getUrl(context, item.getIcon(), (int)context.getResources().getDimension(R.dimen.y150),(int)context.getResources().getDimension(R.dimen.y150),false,true))
                .override((int)context.getResources().getDimension(R.dimen.y150),(int)context.getResources().getDimension(R.dimen.y150))
                .error(R.drawable.shape_gray_e8e8e8_background)
                .placeholder(R.drawable.shape_gray_e8e8e8_background)
                .into(helper.ivCommodity);
        helper.tvTitle.setText(item.getProductName());
        helper.tvDesc.setText(item.getDesc());
        helper.tvNum.setText("库存:" + (item.getStock() - item.getFreeze() < 0 ? 0 : item.getStock() - item.getFreeze()));
        helper.tvNumDesc.setText(item.getStockDesc());
        if(item.getStock() - item.getFreeze() <= 0){
            helper.tvBuy.setText("已售罄");
            helper.tvBuy.setSelected(true);
            helper.tvBuy.setOnClickListener(null);
        }else {
            StringBuilder price = new StringBuilder();
            if(item.getRmb() > 0){
                if(item.getRmb()%100 != 0){
                    price.append((float)item.getRmb()/100);
                }else {
                    price.append(item.getRmb()/100);
                }
                price.append( "元");
            }
            if(item.getRmb() > 0 && item.getCoin() > 0) price.append(" + ");
            if(item.getCoin() > 0){
                price.append(item.getCoin()).append("节操");
            }
            helper.tvBuy.setText(price);
            helper.tvBuy.setSelected(false);
            helper.tvBuy.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    ((CoinShopActivity)context).createOrder(item);
                }
            });
        }
    }

    @Override
    public int getItemType(int position) {
        return 0;
    }
}
