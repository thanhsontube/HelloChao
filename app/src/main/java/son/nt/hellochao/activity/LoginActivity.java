package son.nt.hellochao.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import son.nt.hellochao.R;
import son.nt.hellochao.base.AActivity;
import son.nt.hellochao.fragment.LoginFragment;
import son.nt.hellochao.fragment.SignUpFragment;

public class LoginActivity extends AActivity implements LoginFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

    }

    @Override
    public void onSignUp(String email, String password) {
        showFragment(SignUpFragment.newInstance(email, password), true);
    }
}
