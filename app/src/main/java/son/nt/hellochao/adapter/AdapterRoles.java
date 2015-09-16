package son.nt.hellochao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import son.nt.hellochao.R;
import son.nt.hellochao.dto.TopDto;
import son.nt.hellochao.utils.DatetimeUtils;

/**
 * Created by Sonnt on 7/14/15.
 */
public class AdapterRoles extends RecyclerView.Adapter<AdapterRoles.Holder> {
    private List<TopDto> list;
    private Context context;

    public AdapterRoles(Context cx, List<TopDto> list1) {
        this.list = list1;
        this.context = cx;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_roles, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final TopDto dto = list.get(position);
        holder.txtNo.setText("" + dto.getNo());
        holder.txtName.setText(dto.getName());
        holder.txtScore.setText("" + dto.getScore() + "/10");
        holder.txtTotal.setText("" + dto.getTotalTime() + "s");
        holder.txtSubmitTime.setText(DatetimeUtils.getTimeAgo(dto.getSubmitTime(), context));
//        Glide.with(holder.img.getContext()).load(dto.getLinkAvatar()).diskCacheStrategy(DiskCacheStrategy.ALL)
//        .fitCenter().into(holder.img);



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView txtName, txtScore, txtTotal, txtSubmitTime, txtNo;
        View view;
        public Holder(View view) {
            super(view);
            this.view = view.findViewById(R.id.row_roles_ll);
            img = (ImageView) view.findViewById(R.id.row_icon);
            txtName = (TextView) view.findViewById(R.id.row_name);
            txtScore = (TextView) view.findViewById(R.id.row_score);
            txtTotal = (TextView) view.findViewById(R.id.row_total_time);
            txtSubmitTime = (TextView) view.findViewById(R.id.row_submit_time);
            txtNo = (TextView) view.findViewById(R.id.row_no);
        }
    }
}
