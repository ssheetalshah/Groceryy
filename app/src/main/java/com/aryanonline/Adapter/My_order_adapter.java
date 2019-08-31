package com.aryanonline.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aryanonline.Model.My_order_model;

import java.util.List;

public class My_order_adapter extends RecyclerView.Adapter<My_order_adapter.MyViewHolder> {

    private List<My_order_model> modelList;

    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_orderno, tv_status, tv_date, tv_time, tv_price, tv_item;

        public MyViewHolder(View view) {
            super(view);
            tv_orderno = (TextView) view.findViewById(com.aryanonline.R.id.tv_order_no);
            tv_status = (TextView) view.findViewById(com.aryanonline.R.id.tv_order_status);
            tv_date = (TextView) view.findViewById(com.aryanonline.R.id.tv_order_date);
            tv_time = (TextView) view.findViewById(com.aryanonline.R.id.tv_order_time);
            tv_price = (TextView) view.findViewById(com.aryanonline.R.id.tv_order_price);
            tv_item = (TextView) view.findViewById(com.aryanonline.R.id.tv_order_item);
        }
    }

    public My_order_adapter(List<My_order_model> modelList) {
        this.modelList = modelList;
    }

    @Override
    public My_order_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(com.aryanonline.R.layout.row_my_order_rv, parent, false);

        context = parent.getContext();

        return new My_order_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(My_order_adapter.MyViewHolder holder, int position) {
        My_order_model mList = modelList.get(position);

        holder.tv_orderno.setText(context.getResources().getString(com.aryanonline.R.string.order_no) + mList.getSale_id());

        if(mList.getStatus().equals("0")) {
            holder.tv_status.setText(context.getResources().getString(com.aryanonline.R.string.pending));
        }else if(mList.getStatus().equals("1")){
            holder.tv_status.setText(context.getResources().getString(com.aryanonline.R.string.confirm));
            holder.tv_status.setTextColor(context.getResources().getColor(com.aryanonline.R.color.color_1));
        }else if(mList.getStatus().equals("2")){
            holder.tv_status.setText(context.getResources().getString(com.aryanonline.R.string.delivered));
            holder.tv_status.setTextColor(context.getResources().getColor(com.aryanonline.R.color.color_2));
        }else if(mList.getStatus().equals("3")){
            holder.tv_status.setText(context.getResources().getString(com.aryanonline.R.string.cancle));
            holder.tv_status.setTextColor(context.getResources().getColor(com.aryanonline.R.color.color_3));
        }

        holder.tv_date.setText(context.getResources().getString(com.aryanonline.R.string.date) + mList.getOn_date());
        holder.tv_time.setText(context.getResources().getString(com.aryanonline.R.string.time) + mList.getDelivery_time_from() + "-" + mList.getDelivery_time_to());
        holder.tv_price.setText(context.getResources().getString(com.aryanonline.R.string.currency) + mList.getTotal_amount());
        holder.tv_item.setText(context.getResources().getString(com.aryanonline.R.string.tv_cart_item) + mList.getTotal_items());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}
