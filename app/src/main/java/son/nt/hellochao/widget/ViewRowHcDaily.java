package son.nt.hellochao.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import son.nt.hellochao.R;
import son.nt.hellochao.parse_object.HelloChaoDaily;

/**
 * Created by Sonnt on 12/7/15.
 */
public class ViewRowHcDaily extends LinearLayout {
    TextView txtText, txtTranslate;
    View rowLL;
    HelloChaoDaily helloChaoDaily;
    ImageView icon;

    public ViewRowHcDaily(Context context) {
        this(context, null);
    }

    public ViewRowHcDaily(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewRowHcDaily(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.row_list_2lines, this, true);
        rowLL = findViewById(R.id.row_ll);
        txtText = (TextView) findViewById(R.id.txt_text);
        txtTranslate = (TextView) findViewById(R.id.txt_translate);
        icon = (ImageView) findViewById(R.id.img_src);
        rowLL.setOnClickListener(onClickListener);


        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ViewRowHcDaily);
        final Drawable src = typedArray.getDrawable(R.styleable.ViewRowHcDaily_src);
        if (src != null) {
//            icon.setImageDrawable(ContextCompat.getDrawable(context, src));
            icon.setImageDrawable(src);
        }


    }

    OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.row_ll:
                    if (callback != null) {
                        callback.onClick(helloChaoDaily);
                    }

                    break;
            }

        }
    };

    public interface OnViewRowHcDailyClick {
        void onClick(HelloChaoDaily data);
    }

    OnViewRowHcDailyClick callback;

    public void setOnViewRowHcDailyClickCallback(OnViewRowHcDailyClick cb) {
        this.callback = cb;
    }


    public void setData(HelloChaoDaily data) {
        if (data == null) {
            return;
        }
        this.helloChaoDaily = data;
        txtText.setText(data.getText());
        txtTranslate.setText(data.getTranslate());

    }


}
