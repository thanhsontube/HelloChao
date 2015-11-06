package son.nt.hellochao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import son.nt.hellochao.R;
import son.nt.hellochao.dto.HomeEntity;
import son.nt.hellochao.otto.GoESL;
import son.nt.hellochao.utils.Logger;
import son.nt.hellochao.utils.OttoBus;

/**
 * Created by Sonnt on 7/14/15.
 */
public class AdapterListEsl extends RecyclerView.Adapter<AdapterListEsl.Holder> {
    public static final String TAG = "AdapterListEsl";
    private List<HomeEntity> list;
    private Context context;

    public AdapterListEsl(Context cx, List<HomeEntity> list1) {
        this.list = list1;
        this.context = cx;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_esl_list, parent, false);
        return new Holder(view);
    }

    class Holder extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView fromName;
        TextView message;
        TextView createTime;
        View view;

        public Holder(View itemView) {
            super(itemView);
            this.view = itemView;
            this.avatar = (ImageView) itemView.findViewById(R.id.row_chat_avatar);
            this.fromName = (TextView) itemView.findViewById(R.id.row_chat_fromName);
            this.message = (TextView) itemView.findViewById(R.id.row_chat_message);
            this.createTime = (TextView) itemView.findViewById(R.id.row_chat_create_day);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Logger.debug(TAG, ">>>" + "onClick:" + getAdapterPosition());
                    OttoBus.post(new GoESL(list.get(getAdapterPosition())));
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(Holder viewHolder, int position) {
        final HomeEntity dto = list.get(position);
        viewHolder.fromName.setText(dto.getHomeDescription());
        viewHolder.message.setText(dto.getHomeTitle());
        Glide.with(viewHolder.avatar.getContext()).load(dto.getHomeImage())
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.avatar);


        viewHolder.createTime.setText(dto.getHomeGroup());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
