package com.moemoe.lalala.di.modules;

import com.moemoe.lalala.presenter.FeedBagContract;
import com.moemoe.lalala.presenter.NewUploadContract;

import dagger.Module;
import dagger.Provides;

/**
 *
 * Created by yi on 2016/11/29.
 */
@Module
public class NewUploadModule {
    private NewUploadContract.View mView;

    public NewUploadModule(NewUploadContract.View view){
        this.mView = view;
    }

    @Provides
    public NewUploadContract.View provideView(){return mView;}
}
