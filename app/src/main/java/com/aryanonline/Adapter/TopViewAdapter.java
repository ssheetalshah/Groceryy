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
import com.aryanonline.Model.TopModel;
import com.aryanonline.NewActivity;
import com.aryanonline.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class TopViewAdapter extends RecyclerView.Adapter<TopViewAdapter.ViewHolder> {
    private static final String TAG = "TopViewAdapter";
    private ArrayList<TopModel> TopList;
    public Context context;
    String resId = "";
    String finalStatus = "";
    String Image;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView idProductName, idProductprice;
        //  LinearLayout card;
        ImageView idProductImage;
        LinearLayout mainButton;
        int pos;

        public ViewHolder(View view) {
            super(view);

            idProductName = (TextView) view.findViewById(R.id.idProductName);
            idProductprice = (TextView) view.findViewById(R.id.idProductprice);
            idProductImage = (ImageView) view.findViewById(R.id.idProductImage);
            mainButton = (LinearLayout) view.findViewById(R.id.mainButton);
            // card = (LinearLayout) view.findViewById(R.id.card_view);
        }
    }

    public static Context mContext;

    public TopViewAdapter(Context mContext, ArrayList<TopModel> top_list) {
        context = mContext;
        TopList = top_list;

    }

    @Override
    public TopViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.topview_row, parent, false);

        return new TopViewAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TopViewAdapter.ViewHolder viewHolder, final int position) {
        TopModel topModel = TopList.get(position);
        viewHolder.idProductName.setText(topModel.getProductname());
        String s = topModel.getPrice();
        Image = topModel.getImage();
        String ss = s.indexOf(".") < 0 ? s : s.replaceAll("0*$", "").replaceAll("\\.$", "");
        viewHolder.idProductprice.setText(ss);
        Glide.with(context)
                .load(BaseURL.IMG_PRODUCT_URL + Image)
                .placeholder(R.drawable.aplogo)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(viewHolder.idProductImage);
        // viewHolder.card.setTag(viewHolder);

        viewHolder.mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TopModel topModel = TopList.get(position);
                Intent intent = new Intent(context, NewActivity.class);
                intent.putExtra("TopModel", topModel);
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        });
        viewHolder.pos = position;

    }

    @Override
    public int getItemCount() {
        return TopList.size();
    }

}
