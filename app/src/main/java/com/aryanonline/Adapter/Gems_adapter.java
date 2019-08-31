package com.aryanonline.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aryanonline.Model.GemsCategory;
import com.aryanonline.Model.NewCategory;
import com.aryanonline.NewSubActivity;

import java.util.ArrayList;

public class Gems_adapter  extends RecyclerView.Adapter<Gems_adapter.ViewHolder> {

    private static final String TAG = "NewCatAdapter";
    private ArrayList<GemsCategory> cctList;
    public Context context;
    String resId = "";
    String finalStatus = "";
    String Image;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameOnly;
        LinearLayout card;
        ImageView idProductImage;
        int pos;

        public ViewHolder(View view) {
            super(view);

            nameOnly = (TextView) view.findViewById(com.aryanonline.R.id.nameOnly);
            // idProductImage = (ImageView) view.findViewById(R.id.idProductImage);
            //   card = (LinearLayout) view.findViewById(R.id.card_view);
        }
    }

    public static Context mContext;

    public Gems_adapter(Context mContext, ArrayList<GemsCategory> offer_list) {
        context = mContext;
        cctList = offer_list;
    }

    @Override
    public Gems_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(com.aryanonline.R.layout.new_cat_rw, parent, false);

        return new Gems_adapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final Gems_adapter.ViewHolder viewHolder, final int position) {
        GemsCategory newCategory = cctList.get(position);
        viewHolder.nameOnly.setText(newCategory.getName());

        viewHolder.nameOnly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GemsCategory newCategory = cctList.get(position);
                Intent intent = new Intent(context, NewSubActivity.class);
                intent.putExtra("NewCategory",newCategory.getCategoryId());
                context.startActivity(intent);
                // ((Activity)context).finish();
            }
        });
        viewHolder.pos = position;

    }

    @Override
    public int getItemCount() {
        return cctList.size();
    }

}

