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
import son.nt.hellochao.dto.RankDto;

/**
 * Created by Sonnt on 1/13/16.
 */
public class RankAdapter extends RecyclerView.Adapter<RankAdapter.ViewHolder> {
    private Context context;
    List<RankDto> list;

    public RankAdapter(Context context, List<RankDto> l) {
        this.context = context;
        this.list = l;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;

        TextView txt1;
        TextView txt2;
        TextView txt3;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.img_src);
            txt1 = (TextView) itemView.findViewById(R.id.txt_text);
            txt2 = (TextView) itemView.findViewById(R.id.txt_translate);
            txt3 = (TextView) itemView.findViewById(R.id.txt_more);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_list_3lines, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RankDto dto = list.get(position);
        holder.txt1.setText(dto.getRankName());
        if (dto.getRankScoreStandard() == -1) {
            holder.txt2.setText("Special");
        } else {
            holder.txt2.setText("" + dto.getRankScoreStandard());
        }
        holder.txt3.setText("" + dto.getRankNo());
        Glide.with(context).load(dto.getRankIcon()).diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().into(holder.icon);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
