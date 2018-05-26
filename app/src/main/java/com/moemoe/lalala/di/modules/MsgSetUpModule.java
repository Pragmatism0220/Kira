package com.moemoe.lalala.di.modules;

import com.moemoe.lalala.presenter.MsgSetUpContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Sora on 2018/3/7.
 */
@Module
public class MsgSetUpModule {
    private MsgSetUpContract.View mView;

    public MsgSetUpModule(MsgSetUpContract.View view){
        this.mView = view;
    }

    @Provides
    public MsgSetUpContract.View provideView(){return mView;}
}
