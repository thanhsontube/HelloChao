package son.nt.hellochao.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import son.nt.hellochao.R;
import son.nt.hellochao.ResourceManager;
import son.nt.hellochao.dto.RankDto;
import son.nt.hellochao.interface_app.AppAPI;
import son.nt.hellochao.loader.GunBoundLevelLoader;
import son.nt.hellochao.loader.LoaderManager;
import son.nt.hellochao.utils.Logger;

public class HtmlCleanerActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "HtmlCleanerActivity";
    @Bind(R.id.t_gunbound)
    View viewGunBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html_cleaner);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewGunBound.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.t_gunbound:
                getGunBoundLevel();
                break;
        }
    }

    private void getGunBoundLevel() {
        Logger.debug(TAG, ">>>" + "getGunBoundLevel");

        LoaderManager manager = new LoaderManager();
        manager.execute(new GunBoundLevelLoader(this, ResourceManager.getInstance().getMyPath().getGunBoundLevel()) {
            @Override
            public void onLoaderStart() {
            }

            @Override
            public void onLoaderSuccess(List<RankDto> entity) {
                for (RankDto d : entity) {
                    AppAPI.getInstance().pushRank(d);
                }

            }

            @Override
            public void onLoaderFail(Throwable e) {
            }
        });

    }
}
