package son.nt.hellochao;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.squareup.otto.Subscribe;

import son.nt.hellochao.base.AActivity;
import son.nt.hellochao.fragment.PlayingFragment;
import son.nt.hellochao.fragment.ScrollFragment;
import son.nt.hellochao.otto.GoHot;
import son.nt.hellochao.utils.OttoBus;

public class ScrollingActivity extends AActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        OttoBus.register(this);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OttoBus.unRegister(this);
    }

    @Override
    protected Fragment fromFragment(Bundle savedInstanceState) {
        return new ScrollFragment();
    }

    @Override
    protected int fromContentId() {
        return R.id.scroll_ll_main;
    }

    @Subscribe
    public void onFromTopFragment (GoHot goHot) {
        showFragment(new PlayingFragment(), true);
    }
}
