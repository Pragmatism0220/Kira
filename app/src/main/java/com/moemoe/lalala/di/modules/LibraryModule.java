package com.moemoe.lalala.di.modules;

import com.moemoe.lalala.presenter.LibraryContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Hygge on 2018/7/18.
 */
@Module
public class LibraryModule {
    private LibraryContract.View mView;
    public LibraryModule(LibraryContract.View view) {
        this.mView = view;
    }

    @Provides
    public LibraryContract.View provideView() {
        return mView;
    }
}
