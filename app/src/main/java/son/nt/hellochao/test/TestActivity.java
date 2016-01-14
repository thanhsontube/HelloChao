package son.nt.hellochao.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import son.nt.hellochao.R;
import son.nt.hellochao.parse_object.PointDto;

public class TestActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.test_push_point:
                processPushPointDto();
                break;
        }
    }

    private void processPushPointDto () {
       createPointDto("Link With Facebook", 1000);
       createPointDto("Addition Medium", 5);
       createPointDto("Addition Hard", 15);
    }
    private PointDto createPointDto (String name, int value) {
        PointDto dto;
        dto = new PointDto();
        dto.setPointName(name);
        dto.setPointValue(value);
        dto.saveInBackground();
        return dto;
    }
}
