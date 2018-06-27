package com.moemoe.lalala.view.activity;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.moemoe.lalala.R;
import com.moemoe.lalala.databinding.ActivityCommonUseBinding;
import com.moemoe.lalala.dialog.AlertDialog;
import com.moemoe.lalala.utils.CommonLoadingTask;
import com.moemoe.lalala.utils.FileUtil;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.view.base.BaseActivity;

public class CommonUseActivity extends BaseActivity {

    private ActivityCommonUseBinding binding;

    private TextView mTvTitle;
    private ImageView mIvBack;


    @Override
    protected void initComponent() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_common_use);
        binding.setPresenter(new Presenter());
        mTvTitle = findViewById(R.id.tv_toolbar_title);
        mIvBack = findViewById(R.id.iv_back);

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {
        mTvTitle.setText(R.string.common);
        mTvTitle.setTextColor(getResources().getColor(R.color.black));
        mIvBack.setVisibility(View.VISIBLE);
        mIvBack.setImageResource(R.drawable.btn_back_black_normal);
        mIvBack.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initData() {

    }

    private TextView getFunDetailTv(View v){
        return (TextView) v.findViewById(R.id.tv_function_detail);
    }

    /**
     * 清理缓存
     */
    private void clearCache(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.a_dlg_title);
        builder.setMessage(R.string.a_dlg_clear_cache);
        builder.setPositiveButton(R.string.label_clear, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                new CommonLoadingTask(CommonUseActivity.this, new CommonLoadingTask.TaskCallback() {

                    @Override
                    public Object processDataInBackground() {
                        return FileUtil.clearAllCache(CommonUseActivity.this);
                    }

                    @Override
                    public void handleData(Object o) {
                        TextView funDetailTv = getFunDetailTv(CommonUseActivity.this.findViewById(R.id.set_clear_cache));
                        funDetailTv.setText("");
                        funDetailTv.setVisibility(View.INVISIBLE);
                        showToast(R.string.msg_clear_cache_succ_with_size);
                    }

                }, null).execute();
            }
        });
        builder.setNegativeButton(R.string.label_cancel, null);
        try {
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public class Presenter {
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.wifi_item:
                    break;
                case R.id.up_data_number_item:
                    break;
                case R.id.clean_item:

                    break;
            }
        }


    }
}
