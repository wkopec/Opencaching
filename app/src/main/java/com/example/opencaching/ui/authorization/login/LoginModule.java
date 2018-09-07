package com.example.opencaching.ui.authorization.login;

import dagger.Binds;
import dagger.Module;

@Module
public interface LoginModule {

    @Binds
    LoginContract.View bindView(LoginFragment loginFragment);
}
