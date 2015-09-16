package son.nt.hellochao.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by Sonnt on 9/14/15.
 */
public abstract class AFragment extends Fragment {
    protected abstract void initData();

    protected abstract void initLayout(View view);

    protected abstract void initListener(View view);

    protected abstract void updateLayout();

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initLayout(view);
        initListener(view);
        updateLayout();
    }
}
