package com.aryanonline.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aryanonline.Config.BaseURL;
import com.aryanonline.Model.OfferModel;
import com.aryanonline.Model.TopModel;
import com.aryanonline.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class TopAdapter extends RecyclerView.Adapter<TopAdapter.ViewHolder> {
    private static final String TAG = "TopAdapter";
    private ArrayList<TopModel> TopList;
    public Context context;
    String resId = "";
    String finalStatus = "";
    String Image;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView idProductName,idProductprice;
      //  LinearLayout card;
        ImageView idProductImage;
        int pos;

        public ViewHolder(View view) {
            super(view);

            idProductName = (TextView) view.findViewById(R.id.idProductName);
            idProductprice = (TextView) view.findViewById(R.id.idProductprice);
            idProductImage = (ImageView) view.findViewById(R.id.idProductImage);
           // card = (LinearLayout) view.findViewById(R.id.card_view);
        }
    }

    public static Context mContext;

    public TopAdapter(Context mContext, ArrayList<TopModel> top_list) {
        context = mContext;
        TopList = top_list;

    }

    @Override
    public TopAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.top_row, parent, false);

        return new TopAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TopAdapter.ViewHolder viewHolder, final int position) {
        TopModel topModel = TopList.get(position);
        viewHolder.idProductName.setText(topModel.getProductName());
       // viewHolder.idProductprice.setText(topModel.getPrice());
        Image = topModel.getProductImage();
        Glide.with(context)
                .load(BaseURL.IMG_PRODUCT_URL+Image)
                .placeholder(R.drawable.shop)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(viewHolder.idProductImage);
       // viewHolder.card.setTag(viewHolder);
        viewHolder.pos = position;

    }

    @Override
    public int getItemCount() {
        return TopList.size();
    }

}
