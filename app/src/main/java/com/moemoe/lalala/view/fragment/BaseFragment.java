package com.moemoe.lalala.view.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.model.api.NetSimpleResultSubscriber;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yi on 2016/12/1.
 */

public abstract class BaseFragment extends Fragment {

    protected View rootView;
    private Unbinder bind;
    private int mStayTime;
    private Handler mHandler = new Handler();
    private Runnable timeRunnabel = new Runnable() {
        @Override
        public void run() {
            mStayTime++;
            mHandler.postDelayed(this,1000);
        }
    };
    protected void startTime(){
        mHandler.post(timeRunnabel);
    }

    protected void pauseTime(){
        mHandler.removeCallbacks(timeRunnabel);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null){
            rootView = inflater.inflate(getLayoutId(), container, false);
            bind = ButterKnife.bind(this, rootView);
            initViews(savedInstanceState);
        }
        init();
        return rootView;
    }

    protected void init(){

    }
    /**
     * 点击事件标记
     * @param event 事件名
     */
    protected void clickEvent(String event){
        MoeMoeApplication.getInstance().getNetComponent().getApiService().clickDepartment(event)
                .subscribeOn(Schedulers.io())
                .subscribe(new NetSimpleResultSubscriber() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFail(int code, String msg) {

                    }
                });
    }


    /**
     * 页面进入的时间计算
     * @param event 事件名
     */
    protected void stayEvent(String event){
        MoeMoeApplication.getInstance().getNetComponent().getApiService().stayDepartment(event,mStayTime)
                .subscribeOn(Schedulers.io())
                .subscribe(new NetSimpleResultSubscriber() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFail(int code, String msg) {

                    }
                });
    }
    protected abstract int getLayoutId();
    protected abstract void initViews(Bundle savedInstanceState);

    @SuppressWarnings("unchecked")
    public <V extends View> V $(@IdRes int id){
        return (V)rootView.findViewById(id);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void release(){
        if(bind != null){
            try {
                bind.unbind();
            }catch (Exception e){

            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

}
