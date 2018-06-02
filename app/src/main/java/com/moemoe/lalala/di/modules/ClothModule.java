package com.moemoe.lalala.di.modules;

import com.moemoe.lalala.presenter.ClothingContrarct;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2018/5/25.
 */
@Module
public class ClothModule {
    private ClothingContrarct.View mView;

    public ClothModule(ClothingContrarct.View view) {
        this.mView = view;
    }

    @Provides
    public ClothingContrarct.View provideView() {
        return mView;
    }
}
