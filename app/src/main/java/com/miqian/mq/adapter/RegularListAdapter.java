package com.miqian.mq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.entity.RegularEarn;
import com.miqian.mq.views.CircleBar;

import java.util.List;

/**
 * Created by guolei_wang on 15/9/17.
 */
public class RegularListAdapter extends RecyclerView.Adapter<RegularListAdapter.ViewHolder>{
    private List<RegularEarn> items;

    public RegularListAdapter(List<RegularEarn> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.regular_earn_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RegularEarn regularEarn = items.get(position);
        holder.tv_title.setText(regularEarn.getBdNm());
        holder.tv_sub_title.setText(regularEarn.getBdDesp());
        holder.tv_annurate_interest_rate.setText(regularEarn.getYrt());
        if(position > 0) {
            holder.layout_regular_earn_head.setVisibility(View.GONE);
        }else {
            holder.layout_regular_earn_head.setVisibility(View.VISIBLE);
        }
    }

    public void addAll(List<RegularEarn> earnList){
        items.addAll(earnList);
        notifyDataSetChanged();
    }

    public void clear(){
        items.clear();
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return items == null? 0 : items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private View layout_regular_earn_head;
        public TextView tv_title;
        public TextView tv_sub_title;
        public TextView tv_annurate_interest_rate;
        public TextView tv_duration;
        public TextView tv_progress;
        public TextView tv_sale_number;
        public CircleBar circlebar;
        public ViewHolder(View itemView) {
            super(itemView);

            layout_regular_earn_head = itemView.findViewById(R.id.layout_regular_earn_head);
            tv_title = (TextView)itemView.findViewById(R.id.tv_title);
            tv_sub_title = (TextView)itemView.findViewById(R.id.tv_sub_title);
            tv_annurate_interest_rate = (TextView)itemView.findViewById(R.id.tv_annurate_interest_rate);
            tv_duration = (TextView)itemView.findViewById(R.id.tv_duration);
            tv_progress = (TextView)itemView.findViewById(R.id.tv_progress);
            tv_sale_number = (TextView)itemView.findViewById(R.id.tv_sale_number);
            circlebar = (CircleBar)itemView.findViewById(R.id.circlebar);
        }
    }

}
