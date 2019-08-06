package com.aryanonline.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aryanonline.Model.WishModel;
import com.aryanonline.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class WishAdapter extends RecyclerView.Adapter<WishAdapter.ViewHolder> {

    private static final String TAG = "WishAdapter";
    private ArrayList<WishModel> wList;
    public Context context;
    String resId = "";
    String finalStatus = "";
    String Img;
   // String communStr = "http://ihisaab.in/winnerseven/uploads/userprofile/";

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView pdName,pdModel,  pdStock, pdPrice;
        //  LinearLayout card;
        CircleImageView imageView;
        TextView name, date, time, score;
        LinearLayout mainButton;
        int pos;

        public ViewHolder(View view) {
            super(view);
            pdName = (TextView) view.findViewById(R.id.pdName);
            pdModel = (TextView) view.findViewById(R.id.pdModel);
            pdStock = (TextView) view.findViewById(R.id.pdStock);
            pdPrice = (TextView) view.findViewById(R.id.pdPrice);
            imageView = (CircleImageView) view.findViewById(R.id.imageView);
        }
    }

    public static Context mContext;

    public WishAdapter(Context mContext, ArrayList<WishModel> wish_list) {
        context = mContext;
       wList = wish_list;

    }

    @Override
    public WishAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wish_row, parent, false);

        return new WishAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final WishAdapter.ViewHolder viewHolder, final int position) {
        WishModel wishModel = wList.get(position);
        viewHolder.pdName.setText(wishModel.getProductName());
        viewHolder.pdModel.setText(wishModel.getModel());
        viewHolder.pdStock.setText(wishModel.getStock());
        viewHolder.pdPrice.setText(wishModel.getPrice());
        viewHolder.pos = position;

    }

    @Override
    public int getItemCount() {
        return wList.size();
    }
}
