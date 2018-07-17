package com.moemoe.lalala.di.components;

import com.moemoe.lalala.di.modules.SelectUpterModule;
import com.moemoe.lalala.di.scopes.UserScope;
import com.moemoe.lalala.view.activity.SelectMovieActivity;
import com.moemoe.lalala.view.activity.SelectMusicActivity;

import dagger.Component;

/**
 * Created by Hygge on 2018/7/17.
 */
@UserScope
@Component(modules = SelectUpterModule.class,dependencies = NetComponent.class)
public interface SelectComponents {
    void inject(SelectMusicActivity activity);
    void inject(SelectMovieActivity activity);
}
