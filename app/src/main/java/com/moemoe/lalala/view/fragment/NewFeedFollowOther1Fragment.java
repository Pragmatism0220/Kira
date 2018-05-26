package com.moemoe.lalala.view.fragment;

import android.os.Bundle;
import android.os.Trace;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.moemoe.lalala.R;

import butterknife.BindView;

/**
 * Created by Sora on 2018/2/28.
 */

public class NewFeedFollowOther1Fragment extends BaseFragment {

    @BindView(R.id.tv_type_sort)
    TextView mTvTypeSort;

    private boolean isType = true;
    private String id;
    private FeedFollowOther1Fragment mItem1Fragment;
    private NewUploadFragment mItem2Fragment;

    public static NewFeedFollowOther1Fragment newInstance(String id, boolean isAdmin) {
        NewFeedFollowOther1Fragment fragment = new NewFeedFollowOther1Fragment();
        Bundle bundle = new Bundle();
        bundle.putString("uuid", id);
        bundle.putBoolean("isAdmin", isAdmin);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_new_feed_follow_other_1;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        id = getArguments().getString("uuid");

        $(R.id.fl_type_sort).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                isType = !isType;
                if (isType) {
                    if (mItem1Fragment.isHidden()) {
                        FragmentTransaction mFragmentTransaction = getChildFragmentManager().beginTransaction();
                        mFragmentTransaction.show(mItem1Fragment).hide(mItem2Fragment);
                        mFragmentTransaction.commit();
                        mTvTypeSort.setText("类型排序");
                    }
                } else {
                    if (mItem2Fragment.isHidden()) {
                        FragmentTransaction mFragmentTransaction = getChildFragmentManager().beginTransaction();
                        mFragmentTransaction.show(mItem2Fragment).hide(mItem1Fragment);
                        mFragmentTransaction.commit();
                        mTvTypeSort.setText("最新上传");
                    }
                }
            }
        });


        mItem1Fragment = FeedFollowOther1Fragment.newInstance(id, getArguments().getBoolean("isAdmin"),true);
        mItem2Fragment = NewUploadFragment.newInstance(id);
        FragmentTransaction mFragmentTransaction = getChildFragmentManager().beginTransaction();
        mFragmentTransaction.add(R.id.fl_container, mItem1Fragment);
        mFragmentTransaction.add(R.id.fl_container, mItem2Fragment);
        mFragmentTransaction.show(mItem1Fragment).hide(mItem2Fragment);
        mFragmentTransaction.commit();

    }
    public void setSmoothScrollToPosition(){
        if (mItem2Fragment!=null){
            mItem2Fragment.setSmoothScrollToPosition();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        startTime();
    }

    @Override
    protected void pauseTime() {
        super.pauseTime();
        pauseTime();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stayEvent("社团-社团-书包");
    }
}
