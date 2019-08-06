package com.aryanonline.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aryanonline.Config.BaseURL;
import com.aryanonline.Model.NewCategory;
import com.aryanonline.Model.OfferModel;
import com.aryanonline.Model.TopModel;
import com.aryanonline.NewActivity;
import com.aryanonline.NewSubActivity;
import com.aryanonline.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class NewCatAdapter extends RecyclerView.Adapter<NewCatAdapter.ViewHolder> {

    private static final String TAG = "NewCatAdapter";
    private ArrayList<NewCategory> cctList;
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

            nameOnly = (TextView) view.findViewById(R.id.nameOnly);
           // idProductImage = (ImageView) view.findViewById(R.id.idProductImage);
         //   card = (LinearLayout) view.findViewById(R.id.card_view);
        }
    }

    public static Context mContext;

    public NewCatAdapter(Context mContext, ArrayList<NewCategory> offer_list) {
        context = mContext;
        cctList = offer_list;

    }

    @Override
    public NewCatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.new_cat_rw, parent, false);

        return new NewCatAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final NewCatAdapter.ViewHolder viewHolder, final int position) {
        NewCategory newCategory = cctList.get(position);
         viewHolder.nameOnly.setText(newCategory.getName());

        viewHolder.nameOnly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewCategory newCategory = cctList.get(position);
                Intent intent = new Intent(context, NewSubActivity.class);
                intent.putExtra("NewCategory", newCategory);
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
