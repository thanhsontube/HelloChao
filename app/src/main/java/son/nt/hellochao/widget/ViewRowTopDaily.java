package son.nt.hellochao.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.parse.ParseUser;

import son.nt.hellochao.R;
import son.nt.hellochao.dto.DailyTopDto;
import son.nt.hellochao.utils.DatetimeUtils;

/**
 * Created by Sonnt on 12/7/15.
 */
public class ViewRowTopDaily extends LinearLayout {
    ImageView imgNo, imgAvatar, imgRank;
    TextView txtName, txtScore, txtTotal, txtAgo, txtNo;
    View viewTopTextNo;
    View rowLL;
    DailyTopDto data;

    public ViewRowTopDaily(Context context) {
        this(context, null);
    }

    public ViewRowTopDaily(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewRowTopDaily(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.row_top_daily, this, true);
        txtNo = (TextView) findViewById(R.id.row_top_text_no);
        imgRank = (ImageView) findViewById(R.id.row_top_user_rank);

        rowLL = findViewById(R.id.row_ll);
        txtName = (TextView) findViewById(R.id.txt_name);
        txtTotal = (TextView) findViewById(R.id.txt_total);
        txtAgo = (TextView) findViewById(R.id.txt_ago);
        txtScore = (TextView) findViewById(R.id.txt_correct_sentence);

        viewTopTextNo = findViewById(R.id.row_top_view_text_no);

        imgNo = (ImageView) findViewById(R.id.row_top_img_no);
        imgAvatar = (ImageView) findViewById(R.id.img_src);
        rowLL.setOnClickListener(onClickListener);
    }

    OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.row_ll:
                    if (callback != null) {
                        callback.onClick(data);
                    }

                    break;
            }

        }
    };

    public interface OnViewRowHcDailyClick {
        void onClick(DailyTopDto data);
    }

    OnViewRowHcDailyClick callback;

    public void setOnViewRowHcDailyClickCallback(OnViewRowHcDailyClick cb) {
        this.callback = cb;
    }


    public void setData(DailyTopDto data) {
        if (data == null) {
            return;
        }
        this.data = data;
        txtAgo.setText(DatetimeUtils.getTimeAgo(data.getSubmitTime().getTime(), getContext()));
        txtScore.setText((data.getCorrectSentence()<10 ? "0" + data.getCorrectSentence() : "" +data.getCorrectSentence())  + "/10");
        txtTotal.setText("" + data.getTotalSeconds() + "s");
        ParseUser parseUser = data.getParseUser("user");
        if (parseUser == null) {
            return;
        }
        txtName.setText(parseUser.getString("name"));

        if (!TextUtils.isEmpty(parseUser.getString("avatar"))) {

            Glide.with(getContext()).load(parseUser.getString("avatar")).fitCenter().diskCacheStrategy(DiskCacheStrategy.ALL).into(imgAvatar);
        }

    }


}
