package com.moemoe.lalala.di.modules;

import com.moemoe.lalala.presenter.AllMemberContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Sora on 2018/3/7.
 */
@Module
public class AllMemberModule {
    private AllMemberContract.View mView;

    public AllMemberModule(AllMemberContract.View view){
        this.mView = view;
    }

    @Provides
    public AllMemberContract.View provideView(){return mView;}
}
