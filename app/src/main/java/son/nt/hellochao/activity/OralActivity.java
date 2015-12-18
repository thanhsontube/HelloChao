package son.nt.hellochao.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import son.nt.hellochao.R;
import son.nt.hellochao.base.AActivity;
import son.nt.hellochao.fragment.OralFragment;
import son.nt.hellochao.fragment.TopFragment;
import son.nt.hellochao.fragment.dialog.DialogSetting;

public class OralActivity extends AActivity implements OralFragment.OnFragmentInteractionListener{
    private boolean isTest = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_oral);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
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

    @Override
    public void onVolume() {
        DialogFragment f = (DialogFragment) getSafeFragmentManager().findFragmentByTag("volume");
        FragmentTransaction ft = getSafeFragmentManager().beginTransaction();
        if (f != null) {
            ft.remove(f);
        }
        ft.addToBackStack(null);

        f = DialogSetting.newInstance("", "");
        f.show(ft, "volume");
    }
}
