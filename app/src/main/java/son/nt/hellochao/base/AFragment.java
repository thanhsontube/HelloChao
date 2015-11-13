package son.nt.hellochao.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;

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
        ButterKnife.bind(this, view);
        initData();
        initLayout(view);
        initListener(view);
        updateLayout();
    }


    @Override
    public void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    protected AppCompatActivity getAActivity () {
        return (AppCompatActivity) getActivity();
    }

    protected boolean isSafe()
    {
        if (this.isRemoving() || this.getActivity() == null || this.isDetached() || !this.isAdded()
                || this.getView() == null)
        {
            return false;
        }

        return true;
    }
}
