package son.nt.hellochao;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import son.nt.hellochao.base.AActivity;
import son.nt.hellochao.fragment.LoginFragment;
import son.nt.hellochao.fragment.OralFragment;
import son.nt.hellochao.fragment.SignUpFragment;
import son.nt.hellochao.fragment.TopFragment;


public class MainActivity extends AActivity implements MainActivityFragment.IInteraction, OralFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        showFragment(OralFragment.newInstance("",""), true);
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
        showFragment(TopFragment.newInstance("", ""), true);
    }

    @Override
    public void onGoTop() {
        showFragment(TopFragment.newInstance("", ""), true);
    }
}
