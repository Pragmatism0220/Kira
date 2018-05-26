package com.moemoe.lalala.di.components;

import com.moemoe.lalala.di.modules.AddAddressModule;
import com.moemoe.lalala.di.modules.SubmissionHistoryModule;
import com.moemoe.lalala.di.scopes.UserScope;
import com.moemoe.lalala.view.activity.AddAddressActivity;
import com.moemoe.lalala.view.activity.SubmissionHistoryActivity;

import dagger.Component;

/**
 * Created by yi on 2016/11/27.
 */
@UserScope
@Component(modules = SubmissionHistoryModule.class,dependencies = NetComponent.class)
public interface SubmissionHistoryComponent {
    void inject(SubmissionHistoryActivity activity);
}
