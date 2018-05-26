package com.moemoe.lalala.di.modules;

import com.moemoe.lalala.presenter.CommunityContract;
import com.moemoe.lalala.presenter.SavaDescribeContract;

import dagger.Module;
import dagger.Provides;

/**
 *
 * Created by yi on 2016/11/29.
 */
@Module
public class SavaDescribeModule {
    private SavaDescribeContract.View mView;

    public SavaDescribeModule(SavaDescribeContract.View view){
        this.mView = view;
    }

    @Provides
    public SavaDescribeContract.View provideView(){return mView;}
}
