package son.nt.hellochao.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import son.nt.hellochao.R;
import son.nt.hellochao.base.AActivity;
import son.nt.hellochao.fragment.profile.ProfileFragment;

public class ProfileActivity extends AActivity {

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
}
