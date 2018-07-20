package com.moemoe.lalala.di.components;

import com.moemoe.lalala.di.modules.LibraryModule;
import com.moemoe.lalala.di.scopes.UserScope;
import com.moemoe.lalala.view.activity.LibraryActivity;
import com.moemoe.lalala.view.fragment.LibraryFragment;

import dagger.Component;

/**
 * Created by Hygge on 2018/7/18.
 */
@UserScope
@Component(modules = LibraryModule.class, dependencies = NetComponent.class)
public interface LibraryComponent {
    void inject(LibraryActivity activity);
    void inject(LibraryFragment activity);
}
