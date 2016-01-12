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
import son.nt.hellochao.fragment.profile.ForGetPwFragment;
import son.nt.hellochao.utils.Logger;

/**
 * this class should be implemented by
 *
 * @see android.view.ViewGroup ViewGroup
 */

/**
 * @see android.view.LayoutInflater.Factory
 */

public class LoginActivity extends AActivity implements LoginFragment.OnFragmentInteractionListener, View.OnClickListener,
SignUpFragment.OnFragmentInteractionListener{
    public static final String TAG = "LoginActivity";
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.debug(TAG, ">>>" + "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mFragmentTagStack.size() > 0) {
                    getSafeFragmentManager().popBackStackImmediate();
                    return true;
                } else {
                    finish();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRegister() {

    }

    @Override
    public void finishRegister(boolean isSuccess) {
        if (isSuccess) {
            startActivity(new Intent(this, ProfileActivity.class));
            finish();
        } else {
            getSafeFragmentManager().popBackStackImmediate();
        }
    }

    @Override
    public void onResetPw(String email) {
        showFragment(ForGetPwFragment.newInstance(email), true);
    }
}
