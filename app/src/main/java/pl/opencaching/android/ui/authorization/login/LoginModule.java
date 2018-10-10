package pl.opencaching.android.ui.authorization.login;

import dagger.Binds;
import dagger.Module;

@Module
public interface LoginModule {

    @Binds
    LoginContract.View bindView(LoginFragment loginFragment);
}
