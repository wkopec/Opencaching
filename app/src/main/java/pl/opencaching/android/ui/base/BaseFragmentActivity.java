package pl.opencaching.android.ui.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import pl.opencaching.android.R;
import pl.opencaching.android.ui.authorization.login.LoginFragment;
import pl.opencaching.android.ui.geocache.new_log.NewLogFragment;

public class BaseFragmentActivity extends BaseActivity {

    private static final String FRAGMENT_KEY = "FRAGMENT_KEY";

    public static final int LOGIN_FRAGMENT = 1;
    public static final int NEW_LOG_FRAGMENT = 2;

    public static void launchFragmentActivity(Activity activity, int fragmentKey, Bundle bundle) {
        Intent intent = new Intent(activity, BaseFragmentActivity.class);
        intent.putExtras(bundle);
        intent.putExtra(FRAGMENT_KEY, fragmentKey);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_fragment);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int fragmentKey = getIntent().getIntExtra(FRAGMENT_KEY, 0);
        BaseFragment fragment;

        switch (fragmentKey) {
            case LOGIN_FRAGMENT:
                fragment = new LoginFragment();
                break;
            case NEW_LOG_FRAGMENT:
                fragment = new NewLogFragment();
                break;
            default:
                throw new IllegalStateException("Fragment key do not match or was not present");
        }

        fragment.setArguments(getIntent().getExtras());
        replaceFragment(fragment, false);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
