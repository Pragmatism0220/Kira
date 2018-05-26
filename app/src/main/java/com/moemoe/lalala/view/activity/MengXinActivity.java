package com.moemoe.lalala.view.activity;

import android.content.res.AssetManager;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.moemoe.lalala.R;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.utils.ViewUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import butterknife.BindView;

/**
 *
 * Created by yi on 2017/1/6.
 */

public class MengXinActivity extends BaseAppCompatActivity {

    @BindView(R.id.iv_bg)
    ImageView mIvBg;

    private int mCurNum;
    //private String schme;
    private ArrayList<String> resList;
    private String type;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_mengxin;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        ViewUtils.setStatusBarLight(getWindow(), null);
       // schme = getIntent().getStringExtra("schema");
        type = getIntent().getStringExtra("type");
        resList = getIntent().getStringArrayListExtra("gui");
        if(resList == null || resList.size() == 0 || TextUtils.isEmpty(type)){
            finish();
            return;
        }
        mCurNum = 0;
        try {
            AssetManager assetManager = this.getAssets();
            InputStream in = assetManager.open(resList.get(mCurNum));
            mIvBg.setImageDrawable(InputStream2Drawable(in));
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Drawable InputStream2Drawable(InputStream is){
        Drawable drawable = BitmapDrawable.createFromStream(is,"guiImg");
        return drawable;
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {

    }

    @Override
    protected void initListeners() {
        mIvBg.setOnClickListener(new NoDoubleClickListener(200) {
            @Override
            public void onNoDoubleClick(View v) {
                mCurNum++;
                if(mCurNum < resList.size()){
                    try {
                        AssetManager assetManager = getAssets();
                        InputStream in = assetManager.open(resList.get(mCurNum));
//                        if(mCurNum >= 13){
//                            in = assetManager.open(mCurNum + ".png");
//                        }else {
//                            in = assetManager.open(mCurNum + ".jpg");
//                        }
                        mIvBg.setImageDrawable(InputStream2Drawable(in));
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                  //  if(PreferenceUtils.isLogin()){
                        goToMain();
                   // }else {
                   //     go2Login();
                   // }
                }
            }
        });
    }


    private void goToMain(){
        saveLaunch();
//        Intent i = new Intent(this,MapActivity.class);
//        if(!TextUtils.isEmpty(schme)){
//            i.putExtra("schema",schme);
//        }
//        startActivity(i);
        finish();
    }

    private void saveLaunch(){
        PreferenceUtils.setActivityFirstLaunch(this,type,false);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onBackPressed() {

    }
}
