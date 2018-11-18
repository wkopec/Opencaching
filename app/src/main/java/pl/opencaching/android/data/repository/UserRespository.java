package pl.opencaching.android.data.repository;

import javax.inject.Inject;

import io.realm.Realm;
import pl.opencaching.android.app.prefs.SessionManager;
import pl.opencaching.android.data.models.okapi.User;
import pl.opencaching.android.data.repository.base.RealmRepository;

public class UserRespository extends RealmRepository<User> {

    @Inject
    SessionManager sessionManager;

    @Inject
    public UserRespository(Realm realm) {
        super(realm);
    }

    public User loadUserByUuid(String uuid) {
        return realm.where(User.class)
                .equalTo("uuid", uuid)
                .findFirst();
    }

    public User getLoggedUser() {
        return realm.where(User.class)
                .equalTo("uuid", sessionManager.getUserUuid())
                .findFirst();
    }

}
