package com.moemoe.lalala.di.components;

import com.moemoe.lalala.di.modules.SearchAllModule;
import com.moemoe.lalala.di.scopes.UserScope;
import com.moemoe.lalala.view.activity.AllSearchActivity;

import dagger.Component;

/**
 * Created by yi on 2016/11/27.
 */
@UserScope
@Component(modules = SearchAllModule.class, dependencies = NetComponent.class)
public interface SearchAallComponent {
    void inject(AllSearchActivity activity);
}
