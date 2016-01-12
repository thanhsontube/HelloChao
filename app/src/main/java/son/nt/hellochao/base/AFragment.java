package son.nt.hellochao.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.ButterKnife;
import son.nt.hellochao.R;

/**
 * Created by Sonnt on 9/14/15.
 */
public abstract class AFragment extends Fragment {
    protected abstract void initData();

    protected abstract void initLayout(View view);

    protected abstract void initListener(View view);

    protected abstract void updateLayout();

    protected ProgressDialog mProgressDialog;


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
        hideProgressDialog();
        super.onDestroy();
    }

    protected AppCompatActivity getAActivity() {
        return (AppCompatActivity) getActivity();
    }

    protected boolean isSafe() {
        if (this.isRemoving() || this.getActivity() == null || this.isDetached() || !this.isAdded()
                || this.getView() == null) {
            return false;
        }

        return true;
    }

    public void showProgressDialog(final String message, boolean isCancelable) {
        if (!this.isSafe()) {
            return;

        }

        if (this.mProgressDialog == null) {
            this.mProgressDialog = new ProgressDialog(getAActivity());
            this.mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }

        this.mProgressDialog.setMessage(message);
        this.mProgressDialog.setCancelable(isCancelable);

        if (this.mProgressDialog.isShowing()) {
            return;
        }

        this.mProgressDialog.show();

    }

    public void hideProgressDialog() {
        if (this.mProgressDialog == null) {
            return;
        }

        this.mProgressDialog.dismiss();
        this.mProgressDialog = null;
    }

    protected void setupToolbarIfNeeded(View view, String title) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        getAActivity().setSupportActionBar(toolbar);
        getAActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getAActivity().getSupportActionBar().setTitle(title);
    }
}
