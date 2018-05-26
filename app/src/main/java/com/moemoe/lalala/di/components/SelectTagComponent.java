package com.moemoe.lalala.di.components;

import com.moemoe.lalala.di.modules.SelectTagModule;
import com.moemoe.lalala.di.scopes.UserScope;
import com.moemoe.lalala.view.activity.DocSelectActivity;
import com.moemoe.lalala.view.activity.FeedTagSelectActivity;
import com.moemoe.lalala.view.fragment.ClubListFragment;
import com.moemoe.lalala.view.fragment.NewCommunityFragment;

import dagger.Component;

/**
 *
 * Created by yi on 2016/11/27.
 */
@UserScope
@Component(modules = SelectTagModule.class,dependencies = NetComponent.class)
public interface SelectTagComponent {
    void inject(FeedTagSelectActivity activity);
    void inject(NewCommunityFragment fragment);
    void inject(DocSelectActivity activity);
    void inject(ClubListFragment fragment);
}
