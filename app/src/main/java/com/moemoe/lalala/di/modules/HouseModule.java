package com.moemoe.lalala.di.modules;

import com.moemoe.lalala.presenter.DormitoryContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Hygge on 2018/5/24.
 */
@Module
public class HouseModule {
    private DormitoryContract.View mView;

    public HouseModule(DormitoryContract.View view) {
        this.mView = view;
    }
    @Provides
    public DormitoryContract.View provideView(){return mView;}
}
