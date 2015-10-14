package son.nt.hellochao;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import son.nt.hellochao.base.AActivity;
import son.nt.hellochao.fragment.EslLabFragment;
import son.nt.hellochao.fragment.LoginFragment;
import son.nt.hellochao.fragment.OralFragment;
import son.nt.hellochao.fragment.SignUpFragment;
import son.nt.hellochao.fragment.TopFragment;
import son.nt.hellochao.utils.OttoBus;


public class MainActivity extends AActivity implements MainActivityFragment.IInteraction, OralFragment.OnFragmentInteractionListener,
        LoginFragment.OnFragmentInteractionListener, SignUpFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OttoBus.register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OttoBus.unRegister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int fromContentId() {
        return R.id.ll_main;
    }

    @Override
    protected Fragment fromFragment(Bundle savedInstanceState) {
//        return OralFragment.newInstance("","");
        return new MainActivityFragment();
    }

    @Override
    public void onGotoOral() {
        showFragment(OralFragment.newInstance("", false), true);
    }

    @Override
    public void onGoSignUp() {
        showFragment(SignUpFragment.newInstance("", ""), true);
    }

    @Override
    public void onGoLogin() {
        showFragment(LoginFragment.newInstance("", ""), true);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onTop() {
        if (mFragmentTagStack.size() > 0) {
            getSafeFragmentManager().popBackStackImmediate();
        }
        showFragment(TopFragment.newInstance("", ""), true);
    }

    @Override
    public void onGoTop() {
        showFragment(TopFragment.newInstance("", ""), true);
    }

    @Override
    public void onGoPractice() {
        showFragment(OralFragment.newInstance("", false), true);

    }

    @Override
    public void onGoTest() {
        showFragment(OralFragment.newInstance("", true), true);

    }

    @Override
    public void onBackStackChanged() {
        super.onBackStackChanged();
        if (mFragmentTagStack.size() == 0) {
            getSupportActionBar().setTitle("Hello Chao");
        }
    }

    @Override
    public void onLoginSuccess() {

        if (mFragmentTagStack.size() > 0) {
            getSafeFragmentManager().popBackStackImmediate();
        }

        MainActivityFragment f = (MainActivityFragment) getSafeFragmentManager().findFragmentByTag(AActivity.FRAGMENT_KEY);
        f.updateStatus(true);
    }

    @Override
    public void onRegister() {
        if (mFragmentTagStack.size() > 0) {
            getSafeFragmentManager().popBackStackImmediate();
        }
        MainActivityFragment f = (MainActivityFragment) getSafeFragmentManager().findFragmentByTag(AActivity.FRAGMENT_KEY);
        f.updateStatus(true);
    }


    @Override
    public void onTest1() {
        showFragment(EslLabFragment.newInstance("", ""), true);

    }



}
