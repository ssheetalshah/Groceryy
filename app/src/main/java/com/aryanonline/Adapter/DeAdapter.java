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
import android.widget.RadioButton;
import android.widget.TextView;

import com.aryanonline.Config.BaseURL;
import com.aryanonline.Model.DModel;
import com.aryanonline.Model.TopModel;
import com.aryanonline.NewActivity;
import com.aryanonline.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class DeAdapter extends RecyclerView.Adapter<DeAdapter.ViewHolder> {

    private static final String TAG = "DeAdapter";
    private ArrayList<DModel> dList;
    public Context context;
    String resId = "";
    String finalStatus = "";
    private static RadioButton lastChecked = null;
    private static int lastCheckedPos = 0;
    private boolean ischecked = false;
    String Image;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_adres_username, tv_adres_phone, tv_adres_charge, tv_adres_deliver;
        //  LinearLayout card;
        ImageView idProductImage;
        LinearLayout mainButton;
        public RadioButton rb_select;
        int pos;

        public ViewHolder(View view) {
            super(view);

            tv_adres_username = (TextView) view.findViewById(R.id.tv_adres_username);
            tv_adres_phone = (TextView) view.findViewById(R.id.tv_adres_phone);
            tv_adres_charge = (TextView) view.findViewById(R.id.tv_adres_charge);
            tv_adres_deliver = (TextView) view.findViewById(R.id.tv_adres_deliver);
            rb_select = (RadioButton) view.findViewById(R.id.rb_adres);
            // card = (LinearLayout) view.findViewById(R.id.card_view);
        }
    }

    public static Context mContext;

    public DeAdapter(Context mContext, ArrayList<DModel> de_list) {
        context = mContext;
        dList = de_list;

    }

    @Override
    public DeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_delivery_time_rv_test, parent, false);

        return new DeAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DeAdapter.ViewHolder viewHolder, final int position) {
        DModel dModel = dList.get(position);
          viewHolder.tv_adres_username.setText(dModel.getFirstname());
          viewHolder.tv_adres_phone.setText(dModel.getAddressId());
//          viewHolder.tv_adres_charge.setText(dModel.getProductname());
//          viewHolder.tv_adres_deliver.setText(dModel.getProductname());

            viewHolder.pos = position;

        }

        @Override
        public int getItemCount () {
            return dList.size();
        }

    }
