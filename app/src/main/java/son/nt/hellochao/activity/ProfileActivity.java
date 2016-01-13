package son.nt.hellochao.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import son.nt.hellochao.R;
import son.nt.hellochao.base.AActivity;
import son.nt.hellochao.fragment.RankFragment;
import son.nt.hellochao.fragment.profile.ProfileFragment;

public class ProfileActivity extends AActivity implements ProfileFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    @Override
    protected Fragment fromFragment(Bundle savedInstanceState) {
        return ProfileFragment.newInstance("","");
    }

    @Override
    protected int fromContentId() {
        return R.id.ll_profile;
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
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onRankSystem() {
        showFragment(new RankFragment(),true);
    }
}
