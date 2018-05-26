package com.moemoe.lalala.di.modules;

import com.moemoe.lalala.presenter.NoticeContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Sora on 2018/3/24.
 */
@Module
public class NoticeModule {
    private NoticeContract.View mView;

    public NoticeModule(NoticeContract.View view){
        this.mView = view;
    }

    @Provides
    public NoticeContract.View provideView(){return mView;}
}
