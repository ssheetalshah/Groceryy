package com.aryanonline.Adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import com.aryanonline.Model.Data;
import com.aryanonline.R;

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {


    List<Data> horizontalList = Collections.emptyList();
    Context context;


    public HorizontalAdapter(List<Data> horizontalList, Context context) {
        this.horizontalList = horizontalList;
        this.context = context;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView Image1;
        TextView idProductName1;
        public MyViewHolder(View view) {
            super(view);
            Image1=(ImageView) view.findViewById(R.id.Image1);
            idProductName1=(TextView) view.findViewById(R.id.idProductName1);
        }
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.demoview, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.Image1.setImageResource(horizontalList.get(position).imageId);
        holder.idProductName1.setText(horizontalList.get(position).txt);

        holder.Image1.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                String list = horizontalList.get(position).txt.toString();
               // Toast.makeText(MainActivity.this, list, Toast.LENGTH_SHORT).show();
            }

        });

    }


    @Override
    public int getItemCount()
    {
        return horizontalList.size();
    }
}
