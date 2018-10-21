package pl.opencaching.android.data.repository;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;
import pl.opencaching.android.data.models.okapi.Attribute;
import pl.opencaching.android.data.repository.base.RealmRepository;

public class AttributeRepository extends RealmRepository<Attribute> {

    @Inject
    public AttributeRepository(Realm realm) {
        super(realm);
    }

    public Attribute loadAttributeByCode(String acode) {
        return realm.where(Attribute.class)
                .equalTo("acode", acode)
                .findFirst();
    }

    public RealmResults<Attribute> loadAttributesIncludes(String[] includesCodes) {
        return realm.where(Attribute.class)
                .in("code", includesCodes)
                .findAll();
    }

}
