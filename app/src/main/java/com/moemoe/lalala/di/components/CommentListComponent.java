package com.moemoe.lalala.di.components;

import com.moemoe.lalala.di.modules.CommentListModule;
import com.moemoe.lalala.di.scopes.UserScope;
import com.moemoe.lalala.view.activity.CommentsListActivity;

import dagger.Component;

/**
 * Created by yi on 2016/11/27.
 */
@UserScope
@Component(modules = CommentListModule.class,dependencies = NetComponent.class)
public interface CommentListComponent {
    void inject(CommentsListActivity activity);
}
