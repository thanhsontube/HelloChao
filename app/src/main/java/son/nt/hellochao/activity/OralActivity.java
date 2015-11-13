package son.nt.hellochao.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import son.nt.hellochao.R;
import son.nt.hellochao.base.AActivity;
import son.nt.hellochao.fragment.OralFragment;
import son.nt.hellochao.fragment.TopFragment;

public class OralActivity extends AActivity implements OralFragment.OnFragmentInteractionListener{
    private boolean isTest = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oral);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected Fragment fromFragment(Bundle savedInstanceState) {
        return OralFragment.newInstance("", isTest);
    }

    @Override
    protected int fromContentId() {
        return R.id.oral_ll_main;
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
}
