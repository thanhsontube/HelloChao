package son.nt.hellochao.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import son.nt.hellochao.R;
import son.nt.hellochao.base.AActivity;
import son.nt.hellochao.fragment.LoginFragment;
import son.nt.hellochao.fragment.SignUpFragment;
import son.nt.hellochao.utils.Logger;

/**
 * this class should be implemented by
 * @see android.view.ViewGroup ViewGroup
 *
 */

/**
 * @see android.view.LayoutInflater.Factory
 */

public class LoginActivity extends AActivity implements LoginFragment.OnFragmentInteractionListener, View.OnClickListener {
    public static final String TAG = "LoginActivity";
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.debug(TAG, ">>>" + "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        toolbar = (Toolbar) findViewById(R.id.login_toolbar);

        toolbar.setTitle("");

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Logger.debug(TAG, ">>>" + "CLICK item:");
                if (item.getItemId() == android.R.id.home) {
                    finish();
                    return true;
                }
                return false;
            }
        });

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSafeActionBar().setDisplayHomeAsUpEnabled(true);
            getSafeActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        }


    }

    @Override
    protected Fragment fromFragment(Bundle savedInstanceState) {
        return LoginFragment.newInstance("", "");
    }

    @Override
    protected int fromContentId() {
        return R.id.ll_login;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onLoginSuccess() {
        startActivity(new Intent(this, ProfileActivity.class));
        finish();

    }

    @Override
    public void onSignUp(String email, String password) {
        showFragment(SignUpFragment.newInstance(email, password), true);
    }

    @Override
    public void onClick(View view) {
        Logger.debug(TAG, ">>>" + "onClick");
        switch (view.getId()) {
            case android.R.id.home:
                finish();
                return;
        }
    }
}
