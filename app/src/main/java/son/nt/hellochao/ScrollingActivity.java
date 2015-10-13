package son.nt.hellochao;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import son.nt.hellochao.base.AActivity;
import son.nt.hellochao.fragment.ScrollFragment;

public class ScrollingActivity extends AActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

    }

    @Override
    protected Fragment fromFragment(Bundle savedInstanceState) {
        return new ScrollFragment();
    }

    @Override
    protected int fromContentId() {
        return R.id.scroll_ll_main;
    }
}
